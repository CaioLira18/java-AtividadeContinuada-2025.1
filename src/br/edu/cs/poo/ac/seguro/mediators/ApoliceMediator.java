package br.edu.cs.poo.ac.seguro.mediators;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import br.edu.cs.poo.ac.seguro.daos.ApoliceDAO;
import br.edu.cs.poo.ac.seguro.daos.SeguradoEmpresaDAO;
import br.edu.cs.poo.ac.seguro.daos.SeguradoPessoaDAO;
import br.edu.cs.poo.ac.seguro.daos.SinistroDAO;
import br.edu.cs.poo.ac.seguro.daos.VeiculoDAO;
import br.edu.cs.poo.ac.seguro.entidades.*;

public class ApoliceMediator {
	private SeguradoPessoaDAO daoSegPes;
	private SeguradoEmpresaDAO daoSegEmp;
	private VeiculoDAO daoVel;
	private ApoliceDAO daoApo;
	private SinistroDAO daoSin;

	public ApoliceMediator(SeguradoPessoaDAO daoSegPes, SeguradoEmpresaDAO daoSegEmp, VeiculoDAO daoVel, ApoliceDAO daoApo, SinistroDAO daoSin) {
		this.daoSegPes = daoSegPes;
		this.daoSegEmp = daoSegEmp;
		this.daoVel = daoVel;
		this.daoApo = daoApo;
		this.daoSin = daoSin;
	}

	public RetornoInclusaoApolice incluirApolice(DadosVeiculo dados) {
		if (dados == null || dados.getPlaca() == null || dados.getPlaca().isBlank()) {
			return new RetornoInclusaoApolice(null, "Dados do veículo inválidos.");
		}

		Veiculo veiculo = daoVel.buscar(dados.getPlaca());
		SeguradoPessoa segPes = null;
		SeguradoEmpresa segEmp = null;
		boolean isCpf = dados.getCpfOuCnpj().length() == 11;

		if (isCpf) {
			segPes = daoSegPes.buscar(dados.getCpfOuCnpj());
			if (segPes == null) {
				return new RetornoInclusaoApolice(null, "Segurado pessoa não encontrado.");
			}
		} else {
			segEmp = daoSegEmp.buscar(dados.getCpfOuCnpj());
			if (segEmp == null) {
				return new RetornoInclusaoApolice(null, "Segurado empresa não encontrado.");
			}
		}

		if (veiculo == null) {
			CategoriaVeiculo categoria = CategoriaVeiculo.values()[dados.getCodigoCategoria()];
			veiculo = new Veiculo(dados.getPlaca(), dados.getAno(), categoria, segPes, segEmp);
			daoVel.incluir(veiculo);
		} else {
			veiculo.setProprietarioPessoa(segPes);
			veiculo.setProprietarioEmpresa(segEmp);
			daoVel.alterar(veiculo);
		}

		BigDecimal vpa = dados.getValorMaximoSegurado().multiply(new BigDecimal("0.03"));
		BigDecimal vpb = vpa;
		if (segEmp != null && segEmp.getEhLocadoraDeVeiculos()) {
			vpb = vpa.multiply(new BigDecimal("1.2"));
		}

		BigDecimal bonus = isCpf ? segPes.getBonus() : segEmp.getBonus();
		BigDecimal vpc = vpb.subtract(bonus.divide(BigDecimal.TEN, 2, RoundingMode.HALF_UP));
		BigDecimal premio = vpc.compareTo(BigDecimal.ZERO) > 0 ? vpc : BigDecimal.ZERO;
		BigDecimal franquia = vpb.multiply(new BigDecimal("1.3"));

		premio = premio.setScale(2, RoundingMode.HALF_UP);
		franquia = franquia.setScale(2, RoundingMode.HALF_UP);

		int anoAtual = LocalDate.now().getYear();
		String numero = isCpf
				? anoAtual + "000" + dados.getCpfOuCnpj() + dados.getPlaca()
				: anoAtual + dados.getCpfOuCnpj() + dados.getPlaca();

		if (daoApo.findByNumero(numero) != null) {
			return new RetornoInclusaoApolice(null, "Já existe apólice com este número.");
		}

		LocalDate dataInicio = LocalDate.now();
		Apolice apolice = new Apolice(numero, veiculo, premio, franquia, dataInicio);
		daoApo.insert(apolice);

		int anoAnterior = dataInicio.minusYears(1).getYear();
		final Veiculo veiculoFinal = veiculo;
		boolean teveSinistro = Arrays.stream(daoSin.buscarTodos())
				.anyMatch(s -> s.getDataHoraRegistro().getYear() == anoAnterior
						&& s.getVeiculo().equals(veiculoFinal));

		if (!teveSinistro) {
			BigDecimal credito = premio.multiply(new BigDecimal("0.3")).setScale(2, RoundingMode.HALF_UP);
			if (isCpf) {
				segPes.setBonus(segPes.getBonus().add(credito));
				daoSegPes.alterar(segPes);
			} else {
				segEmp.setBonus(segEmp.getBonus().add(credito));
				daoSegEmp.alterar(segEmp);
			}
		}

		return new RetornoInclusaoApolice(numero, null);
	}

	public Optional<Apolice> buscarApolice(String numero) {
		return daoApo.findByNumero(numero);
	}

	public String excluirApolice(String numero) {
		if (numero == null || numero.isBlank()) {
			return "Número inválido.";
		}
		Optional<Apolice> apolice = daoApo.findByNumero(numero);
		if (apolice == null) {
			return "Apólice não encontrada.";
		}
		int anoVigencia = apolice.get().getDataFimVigencia().getYear();
		Veiculo veiculo = apolice.get().getVeiculo();
		boolean temSinistro = Arrays.stream(daoSin.buscarTodos())
				.anyMatch(s -> s.getDataHoraRegistro().getYear() == anoVigencia
						&& s.getVeiculo().equals(veiculo));
		if (temSinistro) {
			return "Não é possível excluir apólice com sinistro no mesmo ano.";
		}
		daoApo.remove(numero);
		return null;
	}
}
