# Sistema de Gerenciamento de Seguros

## Descrição do Projeto

Este projeto implementa um sistema de gerenciamento de seguros, com foco em seguros de veículos. O sistema utiliza o padrão de projeto DAO (Data Access Object) para persistência de dados, permitindo operações CRUD (Create, Read, Update, Delete) para as diferentes entidades do sistema.

## Estrutura do Projeto

### Pacotes

- `br.edu.cs.poo.ac.seguro.entidades`: Contém as classes de entidades do sistema
- `br.edu.cs.poo.ac.seguro.daos`: Contém as classes DAO para persistência
- `br.edu.cs.poo.ac.seguro.testes`: Contém os testes automatizados para validação

### Entidades Principais

- **Segurado**: Classe base para os segurados, podendo ser pessoa física ou jurídica
- **SeguradoPessoa**: Representa um segurado que é pessoa física
- **SeguradoEmpresa**: Representa um segurado que é pessoa jurídica
- **Veiculo**: Representa um veículo a ser segurado
- **Apolice**: Representa um contrato de seguro para um veículo
- **Sinistro**: Representa um evento de sinistro ocorrido
- **Endereco**: Representa informações de endereço
- **Enum TipoSinistro**: Enumera os tipos possíveis de sinistros (COLISAO, INCENDIO, FURTO, ENCHENTE, DEPREDACAO)

### Classes DAO

- **DAOGenerico**: Classe base com funcionalidades comuns para os DAOs
- **SeguradoPessoaDAO**: Implementa persistência para SeguradoPessoa
- **SeguradoEmpresaDAO**: Implementa persistência para SeguradoEmpresa
- **VeiculoDAO**: Implementa persistência para Veiculo
- **SinistroDAO**: Implementa persistência para Sinistro
- **ApoliceDAO**: Implementa persistência para Apolice

## Funcionalidades

- Cadastro, consulta, atualização e exclusão de segurados (pessoas e empresas)
- Cadastro, consulta, atualização e exclusão de veículos
- Emissão e gerenciamento de apólices de seguro
- Registro e gerenciamento de sinistros
- Cálculo de bônus para segurados
- Suporte a diferentes tipos de sinistros

## Tecnologias Utilizadas

- Java
- JUnit para testes
- Lombok para redução de boilerplate em algumas classes
- Biblioteca PersistenciaObjetos.jar para suporte à persistência

## Como Executar

1. Adicione a dependência `PersistenciaObjetos.jar` ao projeto
2. Importe as classes em seus respectivos pacotes seguindo a estrutura indicada
3. Execute os testes automatizados para validar o funcionamento

## Padrões de Projeto

- **DAO (Data Access Object)**: Utilizado para abstrair e encapsular o acesso a fontes de dados
- **Herança**: Utilizada para especialização de segurados (pessoa/empresa)

## Detalhes de Implementação

- **Identidade de Objetos**: As classes `Sinistro` e `Apolice` utilizam o atributo `numero` como identificadores únicos
- **Polimorfismo**: Suporte inicial para polimorfismo que será expandido em versões futuras
- **Testes Automatizados**: Cada DAO possui sua classe de teste correspondente para validar operações CRUD

## Autores

Desenvolvido como atividade acadêmica para a disciplina de Programação Orientada a Objetos.
