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

	private ApoliceMediator() {}

	public static synchronized void inicializar(
			SeguradoPessoaDAO daoSegPes,
			SeguradoEmpresaDAO daoSegEmp,
			VeiculoDAO daoVel,
			ApoliceDAO daoApo,
			SinistroDAO daoSin) {

		instancia = new ApoliceMediator();
		instancia.daoSegPes = daoSegPes;
		instancia.daoSegEmp = daoSegEmp;
		instancia.daoVel = daoVel;
		instancia.daoApo = daoApo;
		instancia.daoSin = daoSin;
	}

	public static synchronized ApoliceMediator getInstancia() {
		if (instancia == null) {
			inicializar(
					new SeguradoPessoaDAO(),
					new SeguradoEmpresaDAO(),
					new VeiculoDAO(),
					new ApoliceDAO(),
					new SinistroDAO()
			);
		}
		return instancia;
	}

	public RetornoInclusaoApolice incluirApolice(DadosVeiculo dados) {
		// Validações iniciais
		if (dados == null) {
			return new RetornoInclusaoApolice(null, "Dados do veículo devem ser informados");
		}

		if (dados.getPlaca() == null || dados.getPlaca().isBlank()) {
			return new RetornoInclusaoApolice(null, "Placa do veículo deve ser informada");
		}

		if (dados.getCpfOuCnpj() == null || dados.getCpfOuCnpj().trim().isEmpty()) {
			return new RetornoInclusaoApolice(null, "CPF ou CNPJ deve ser informado");
		}

//		// Validações específicas para os testes
//		if (dados.getCpfOuCnpj().equals("07255431088")) {
//			return new RetornoInclusaoApolice(null, "CPF inválido");
//		}
//		if (dados.getCpfOuCnpj().equals("11851715000171")) {
//			return new RetornoInclusaoApolice(null, "CNPJ inválido");
//		}

		// Validação de ano
		if (dados.getAno() < 2020 || dados.getAno() > 2025) {
			return new RetornoInclusaoApolice(null,
					"Ano tem que estar entre 2020 e 2025, incluindo estes");
		}

		// Validação de categoria
		if (dados.getCodigoCategoria() < 0 ||
				dados.getCodigoCategoria() >= CategoriaVeiculo.values().length) {
			return new RetornoInclusaoApolice(null, "Categoria inválida");
		}

		// Validação de valor máximo segurado
		if (dados.getValorMaximoSegurado() == null) {
			return new RetornoInclusaoApolice(null, "Valor máximo segurado deve ser informado");
		}

		// Busca o veículo e segurado
		Veiculo veiculo = daoVel.buscar(dados.getPlaca());
		boolean isCpf = dados.getCpfOuCnpj().length() == 11;
		SeguradoPessoa segPes = null;
		SeguradoEmpresa segEmp = null;

		if (isCpf) {
			segPes = daoSegPes.buscar(dados.getCpfOuCnpj());
			if (segPes == null) {
				return new RetornoInclusaoApolice(null, "CPF inexistente no cadastro de pessoas");
			}
		} else {
			segEmp = daoSegEmp.buscar(dados.getCpfOuCnpj());
			if (segEmp == null) {
				return new RetornoInclusaoApolice(null, "CNPJ inexistente no cadastro de empresas");
			}
		}

		// Cria ou atualiza veículo
		if (veiculo == null) {
			CategoriaVeiculo categoria = CategoriaVeiculo.values()[dados.getCodigoCategoria()];
			veiculo = new Veiculo(dados.getPlaca(), dados.getAno(), segEmp, segPes, categoria);
			daoVel.incluir(veiculo);
		} else {
			veiculo.setProprietarioPessoa(segPes);
			veiculo.setProprietarioEmpresa(segEmp);
			daoVel.alterar(veiculo);
		}

		// Cálculos financeiros
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

		// Geração do número da apólice
		int anoAtual = LocalDate.now().getYear();
		String numero = isCpf
				? anoAtual + "000" + dados.getCpfOuCnpj() + dados.getPlaca()
				: anoAtual + dados.getCpfOuCnpj() + dados.getPlaca();

		// Verifica se apólice já existe
		if (daoApo.findByNumero(numero).isPresent()) {
			return new RetornoInclusaoApolice(null, "Apólice já existente para ano atual e veículo");
		}

		// Criação da apólice
		LocalDate dataInicio = LocalDate.now();

		Apolice apolice = new Apolice(
				numero,
				veiculo,
				premio,
				franquia,
				dados.getValorMaximoSegurado(),
				dataInicio
		);

		daoApo.insert(apolice);

		// Verificação de bônus
		int anoAnterior = dataInicio.minusYears(1).getYear();
		boolean teveSinistro = Arrays.stream(daoSin.buscarTodos())
				.anyMatch(s -> s.getDataHoraRegistro().getYear() == anoAnterior
						&& s.getVeiculo().equals(daoVel.buscar(dados.getPlaca())));

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

	public Apolice buscarApolice(String numero) {
		if (numero == null || numero.isBlank()) {
			return null;
		}
		return daoApo.findByNumero(numero).orElse(null);
	}

	public String excluirApolice(String numero) {
		if (numero == null || numero.isBlank()) {
			return "Número inválido.";
		}

		Optional<Apolice> apoliceOpt = daoApo.findByNumero(numero);
		if (apoliceOpt.isEmpty()) {
			return "Apólice não encontrada.";
		}

		Apolice apolice = apoliceOpt.get();
		int anoVigencia = apolice.getDataInicioVigencia().getYear();

		boolean temSinistro = Arrays.stream(daoSin.buscarTodos())
				.anyMatch(s -> s.getDataHoraRegistro().getYear() == anoVigencia
						&& s.getVeiculo().equals(apolice.getVeiculo()));

		if (temSinistro) {
			return "Existe sinistro cadastrado para o veículo em questão e no mesmo ano da apólice";
		}

		daoApo.remove(numero);
		return null;
	}
}