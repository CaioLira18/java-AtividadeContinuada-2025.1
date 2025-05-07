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
	private static ApoliceMediator instancia;

	private SeguradoPessoaDAO daoSegPes;
	private SeguradoEmpresaDAO daoSegEmp;
	private VeiculoDAO daoVel;
	private ApoliceDAO daoApo;
	private SinistroDAO daoSin;

	private ApoliceMediator(
			SeguradoPessoaDAO daoSegPes,
			SeguradoEmpresaDAO daoSegEmp,
			VeiculoDAO daoVel,
			ApoliceDAO daoApo,
			SinistroDAO daoSin
	) {
		this.daoSegPes = daoSegPes;
		this.daoSegEmp = daoSegEmp;
		this.daoVel = daoVel;
		this.daoApo = daoApo;
		this.daoSin = daoSin;
	}

	public static synchronized ApoliceMediator getInstancia() {
		if (instancia == null) {
			instancia = new ApoliceMediator(
					new SeguradoPessoaDAO(),
					new SeguradoEmpresaDAO(),
					new VeiculoDAO(),
					new ApoliceDAO(),
					new SinistroDAO()
			);
		} else {
			instancia.daoSegPes = new SeguradoPessoaDAO();
			instancia.daoSegEmp = new SeguradoEmpresaDAO();
			instancia.daoVel = new VeiculoDAO();
			instancia.daoApo = new ApoliceDAO();
			instancia.daoSin = new SinistroDAO();
		}
		return instancia;
	}

	public RetornoInclusaoApolice incluirApolice(DadosVeiculo dados) {
		if (dados == null) {
			return new RetornoInclusaoApolice(null, "Dados do veículo devem ser informados");
		}

		if (dados.getPlaca() == null || dados.getPlaca().isBlank()) {
			return new RetornoInclusaoApolice(null, "Placa do veículo deve ser informada");
		}

		if (dados.getCpfOuCnpj() == null || dados.getCpfOuCnpj().trim().isEmpty()) {
			return new RetornoInclusaoApolice(null, "CPF ou CNPJ deve ser informado");
		}

		String cpfOuCnpj = dados.getCpfOuCnpj().replaceAll("[^0-9]", "");

		if (cpfOuCnpj.length() == 11) {
			if (!ValidadorCpfCnpj.ehCpfValido(cpfOuCnpj)) {
				return new RetornoInclusaoApolice(null, "CPF inválido");
			}
		} else if (cpfOuCnpj.length() == 14) {
			if (!ValidadorCpfCnpj.ehCnpjValido(cpfOuCnpj)) {
				return new RetornoInclusaoApolice(null, "CNPJ inválido");
			}
		} else {
			return new RetornoInclusaoApolice(null, "CPF ou CNPJ inválido (tamanho incorreto)");
		}

		if (dados.getAno() < 2020 || dados.getAno() > 2025) {
			return new RetornoInclusaoApolice(null,
					"Ano tem que estar entre 2020 e 2025, incluindo estes");
		}

		if (dados.getCodigoCategoria() < 0 ||
				dados.getCodigoCategoria() >= CategoriaVeiculo.values().length) {
			return new RetornoInclusaoApolice(null, "Categoria inválida");
		}

		if (dados.getValorMaximoSegurado() == null) {
			return new RetornoInclusaoApolice(null, "Valor máximo segurado deve ser informado");
		}

		CategoriaVeiculo categoria = CategoriaVeiculo.values()[dados.getCodigoCategoria() - 1];
		double valorReferencia = 0.0;

		for (PrecoAno precoAno : categoria.getPrecosAnos()) {
			if (precoAno.getAno() == dados.getAno()) {
				valorReferencia = precoAno.getValorMaximo();
				break;
			}
		}

		BigDecimal valorMinimo = new BigDecimal(valorReferencia * 0.75).setScale(2, RoundingMode.HALF_UP);
		BigDecimal valorMaximo = new BigDecimal(valorReferencia).setScale(2, RoundingMode.HALF_UP);

		if (dados.getValorMaximoSegurado().compareTo(valorMinimo) < 0 ||
				dados.getValorMaximoSegurado().compareTo(valorMaximo) > 0) {
			return new RetornoInclusaoApolice(
					null,
					"Valor máximo segurado deve estar entre 75% e 100% do valor do carro encontrado na categoria"
			);
		}

		boolean isCpf = cpfOuCnpj.length() == 11;

		SeguradoPessoa segPes = isCpf ? daoSegPes.buscar(cpfOuCnpj) : null;
		SeguradoEmpresa segEmp = !isCpf ? daoSegEmp.buscar(cpfOuCnpj) : null;

		if ((isCpf && segPes == null) || (!isCpf && segEmp == null)) {
			return new RetornoInclusaoApolice(null, isCpf
					? "CPF inexistente no cadastro de pessoas"
					: "CNPJ inexistente no cadastro de empresas");
		}

		int anoAtual = LocalDate.now().getYear();
		String numero = isCpf
				? anoAtual + "000" + cpfOuCnpj + dados.getPlaca()
				: anoAtual + cpfOuCnpj + dados.getPlaca();

		Optional<Apolice> apoliceExistente = daoApo.findByNumero(numero);
		if (apoliceExistente.isPresent()) {
			return new RetornoInclusaoApolice(null, "Apólice já existente para ano atual e veículo");
		}

		Veiculo veiculo = daoVel.buscar(dados.getPlaca());
		if (veiculo == null) {
			veiculo = new Veiculo(dados.getPlaca(), dados.getAno(), segEmp, segPes, categoria);
			daoVel.incluir(veiculo);
		} else {
			if (isCpf) {
				veiculo.setProprietarioPessoa(segPes);
				veiculo.setProprietarioEmpresa(null);
			} else {
				veiculo.setProprietarioEmpresa(segEmp);
				veiculo.setProprietarioPessoa(null);
			}
			daoVel.alterar(veiculo);
		}

		BigDecimal vpa = dados.getValorMaximoSegurado().multiply(new BigDecimal("0.03"))
				.setScale(2, RoundingMode.HALF_UP);

		BigDecimal vpb = vpa;
		if (segEmp != null && segEmp.getEhLocadoraDeVeiculos()) {
			vpb = vpa.multiply(new BigDecimal("1.2"))
					.setScale(2, RoundingMode.HALF_UP);
		}

		BigDecimal bonus = isCpf ? segPes.getBonus() : segEmp.getBonus();
		BigDecimal vpc = vpb.subtract(bonus.divide(BigDecimal.TEN, 2, RoundingMode.HALF_UP));

		BigDecimal premio = vpc.compareTo(BigDecimal.ZERO) > 0 ? vpc : BigDecimal.ZERO;
		premio = premio.setScale(2, RoundingMode.HALF_UP);

		BigDecimal franquia = vpb.multiply(new BigDecimal("1.3"))
				.setScale(2, RoundingMode.HALF_UP);

		LocalDate dataInicio = LocalDate.now();
		Apolice apolice = new Apolice(
				numero,
				veiculo,
				franquia,
				premio,
				dados.getValorMaximoSegurado(),
				dataInicio
		);

		daoApo.insert(apolice);

		int anoAnterior = dataInicio.minusYears(1).getYear();
		final Veiculo veiculoFinal = veiculo;
		boolean teveSinistro = Arrays.stream(daoSin.buscarTodos())
				.anyMatch(s -> s.getDataHoraRegistro().getYear() == anoAnterior
						&& s.getVeiculo().equals(veiculoFinal));

		if (!teveSinistro) {
			BigDecimal credito = premio.multiply(new BigDecimal("0.3"))
					.setScale(2, RoundingMode.HALF_UP);
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

	public Apolice buscarApolice(String numero) {
		if (numero == null || numero.isBlank()) {
			return null;
		}
		return daoApo.findByNumero(numero).orElse(null);
	}

	public String excluirApolice(String numero) {
		if (numero == null || numero.isBlank()) {
			return "Número deve ser informado";
		}

		Optional<Apolice> apoliceOpt = daoApo.findByNumero(numero);
		if (apoliceOpt.isEmpty()) {
			return "Apólice inexistente";
		}

		Apolice apolice = apoliceOpt.get();
		int anoVigencia = apolice.getDataInicioVigencia().getYear();
		System.out.println("Ano da apólice: " + anoVigencia);

		boolean temSinistro = Arrays.stream(daoSin.buscarTodos())
				.anyMatch(s -> {
					int anoSinistro = s.getDataHoraSinistro().getYear(); // <-- alterado aqui
					return anoSinistro == anoVigencia && s.getVeiculo().equals(apolice.getVeiculo());
				});

		if (temSinistro) {
			return "Existe sinistro cadastrado para o veículo em questão e no mesmo ano da apólice";
		}

		daoApo.remove(numero);
		return null;
	}
}