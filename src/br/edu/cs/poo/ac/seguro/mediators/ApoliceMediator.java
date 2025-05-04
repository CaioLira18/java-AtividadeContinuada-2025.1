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

		CategoriaVeiculo categoria = CategoriaVeiculo.values()[dados.getCodigoCategoria()];
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

		// Busca o segurado primeiro
		SeguradoPessoa segPes = null;
		SeguradoEmpresa segEmp = null;

		if (isCpf) {
			segPes = daoSegPes.buscar(cpfOuCnpj);
			if (segPes == null) {
				return new RetornoInclusaoApolice(null, "CPF inexistente no cadastro de pessoas");
			}
		} else {
			segEmp = daoSegEmp.buscar(cpfOuCnpj);
			if (segEmp == null) {
				return new RetornoInclusaoApolice(null, "CNPJ inexistente no cadastro de empresas");
			}
		}

		// Gera o número da apólice após ter os dados do segurado
		int anoAtual = LocalDate.now().getYear();
		String numero = isCpf
				? anoAtual + "000" + cpfOuCnpj + dados.getPlaca()
				: anoAtual + cpfOuCnpj + dados.getPlaca();

		// Verificação de apólice existente
		if (daoApo.findByNumero(numero).isPresent()) {
			return new RetornoInclusaoApolice(null, "Apólice já existente para ano atual e veículo");
		}

		// Busca ou cria veículo
		Veiculo veiculo = daoVel.buscar(dados.getPlaca());
		if (veiculo == null) {
			veiculo = new Veiculo(dados.getPlaca(), dados.getAno(), segEmp, segPes, categoria);
			daoVel.incluir(veiculo);
		} else {
			// Atualiza proprietário se o veículo já existir
			if (isCpf) {
				veiculo.setProprietarioPessoa(segPes);
				veiculo.setProprietarioEmpresa(null);
			} else {
				veiculo.setProprietarioEmpresa(segEmp);
				veiculo.setProprietarioPessoa(null);
			}
			daoVel.alterar(veiculo);
		}

		// Cálculos financeiros
		BigDecimal valorBasePremio = dados.getValorMaximoSegurado().multiply(new BigDecimal("0.03"));
		BigDecimal valorBaseFranquia = valorBasePremio.multiply(new BigDecimal("1.3"));

		BigDecimal bonus = isCpf ? segPes.getBonus() : segEmp.getBonus();
		BigDecimal descontoBonus = bonus.divide(BigDecimal.TEN, 2, RoundingMode.HALF_UP);

		BigDecimal premio = valorBasePremio.subtract(descontoBonus);
		premio = premio.compareTo(BigDecimal.ZERO) > 0 ? premio : BigDecimal.ZERO;

		BigDecimal franquia = valorBaseFranquia.subtract(descontoBonus);
		franquia = franquia.compareTo(BigDecimal.ZERO) > 0 ? franquia : BigDecimal.ZERO;

		premio = premio.setScale(2, RoundingMode.HALF_UP);
		franquia = franquia.setScale(2, RoundingMode.HALF_UP);

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

		// Verificação de bônus (para crédito no próximo ano)
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