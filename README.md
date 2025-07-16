# 🏪 Loja de Suplementos API

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.3-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-17.4-blue.svg)](https://www.postgresql.org/)
[![Maven](https://img.shields.io/badge/Maven-3.8+-red.svg)](https://maven.apache.org/)
[![JWT](https://img.shields.io/badge/JWT-STATELESS-yellow.svg)](https://jwt.io/)
[![HATEOAS](https://img.shields.io/badge/REST-Level%203-success.svg)](https://en.wikipedia.org/wiki/HATEOAS)

> API REST completa para gerenciamento de loja de suplementos alimentares, desenvolvida com Spring Boot 3.5.3 e implementando todos os princípios REST Level 3 (HATEOAS).

## 📋 Índice

- [Sobre o Projeto](#sobre-o-projeto)
- [Funcionalidades](#funcionalidades)
- [Arquitetura](#arquitetura)
- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Pré-requisitos](#pré-requisitos)
- [Instalação](#instalação)
- [Configuração](#configuração)
- [Executando a Aplicação](#executando-a-aplicação)
- [Testando a API](#testando-a-api)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Documentação da API](#documentação-da-api)
- [Contribuindo](#contribuindo)
- [Licença](#licença)

## 🎯 Sobre o Projeto

A **Loja de Suplementos API** é uma aplicação backend robusta que implementa um sistema completo de e-commerce especializado em suplementos alimentares. O projeto foi desenvolvido seguindo as melhores práticas de desenvolvimento de APIs REST, implementando todos os níveis de maturidade REST (Level 3 - HATEOAS).

### 🎨 Principais Características

- **🔐 Autenticação JWT Stateless**: Sistema de autenticação sem estado usando JSON Web Tokens
- **👥 Autorização baseada em Roles**: Controle granular de acesso com ROLE_USER e ROLE_ADMIN
- **🔗 HATEOAS Level 3**: Links de navegação automáticos em todas as respostas
- **📊 Paginação Inteligente**: Todas as listagens suportam paginação e ordenação
- **🔍 Auditoria Completa**: Sistema de logs automático para todas as operações
- **✅ Validação Robusta**: Validação de dados em todas as camadas
- **🏗️ Arquitetura Limpa**: Separação clara de responsabilidades

## ⚡ Funcionalidades

### 👤 Gestão de Usuários
- ✅ Cadastro de usuários com roles (ADMIN/USER)
- ✅ Autenticação via JWT
- ✅ Perfil de usuário completo
- ✅ Controle de permissões por role

### 🛒 Sistema de Produtos
- ✅ CRUD completo de categorias
- ✅ CRUD completo de suplementos
- ✅ Relacionamento N-N entre produtos e categorias
- ✅ Sistema de estoque
- ✅ Busca avançada por nome, marca e categoria

### 🛍️ Carrinho de Compras
- ✅ Carrinho individual por usuário (relacionamento 1-1)
- ✅ Adicionar/remover itens
- ✅ Atualizar quantidades
- ✅ Cálculo automático de totais
- ✅ Validação de estoque

### 📦 Gestão de Pedidos
- ✅ Criação de pedidos a partir do carrinho
- ✅ Diferentes formas de pagamento
- ✅ Controle de status do pedido
- ✅ Histórico completo de pedidos

### ⭐ Sistema de Avaliações
- ✅ Avaliações com notas e comentários
- ✅ Relacionamento usuário-produto
- ✅ Controle de permissões (apenas donos podem editar)

### 📊 Auditoria e Logs
- ✅ Log automático de todas as operações CRUD
- ✅ Rastreamento de usuário e timestamp
- ✅ Informações de IP e User-Agent
- ✅ Dados antes/depois das alterações

## 🏗️ Arquitetura

A aplicação segue uma arquitetura em camadas (Layered Architecture) com separação clara de responsabilidades:

```
┌─────────────────────────────────────────────────────────────┐
│                    PRESENTATION LAYER                       │
│  ┌─────────────────┐  ┌─────────────────┐  ┌─────────────────┐
│  │  Controllers    │  │   Security      │  │   Exception     │
│  │   (REST API)    │  │   (JWT/OAuth)   │  │   Handlers      │
│  └─────────────────┘  └─────────────────┘  └─────────────────┘
└─────────────────────────────────────────────────────────────┘
                                │
┌─────────────────────────────────────────────────────────────┐
│                     SERVICE LAYER                          │
│  ┌─────────────────┐  ┌─────────────────┐  ┌─────────────────┐
│  │  Business       │  │   Validation    │  │   Audit         │
│  │   Logic         │  │   Rules         │  │   Service       │
│  └─────────────────┘  └─────────────────┘  └─────────────────┘
└─────────────────────────────────────────────────────────────┘
                                │
┌─────────────────────────────────────────────────────────────┐
│                   PERSISTENCE LAYER                        │
│  ┌─────────────────┐  ┌─────────────────┐  ┌─────────────────┐
│  │  Repositories   │  │   Entities      │  │   Mappers       │
│  │   (JPA)         │  │   (Domain)      │  │   (DTOs)        │
│  └─────────────────┘  └─────────────────┘  └─────────────────┘
└─────────────────────────────────────────────────────────────┘
                                │
┌─────────────────────────────────────────────────────────────┐
│                     DATABASE LAYER                         │
│                    PostgreSQL 17.4                         │
└─────────────────────────────────────────────────────────────┘
```

### 🔄 Fluxo de Dados

1. **Request**: Cliente faz requisição HTTP
2. **Security**: Filtro JWT valida autenticação/autorização
3. **Controller**: Recebe requisição e valida dados
4. **Service**: Executa lógica de negócio
5. **Audit**: Registra operação automaticamente (AOP)
6. **Repository**: Acessa dados no banco
7. **Response**: Retorna dados com links HATEOAS

## 🛠️ Tecnologias Utilizadas

### Backend
- **[Spring Boot 3.5.3](https://spring.io/projects/spring-boot)** - Framework principal
- **[Spring Security 6.4.2](https://spring.io/projects/spring-security)** - Autenticação e autorização
- **[Spring Data JPA 3.6.18](https://spring.io/projects/spring-data-jpa)** - Persistência de dados
- **[Spring HATEOAS 3.5.3](https://spring.io/projects/spring-hateoas)** - Links de navegação
- **[Spring AOP 6.4.2](https://spring.io/projects/spring-framework)** - Programação orientada a aspectos
- **[Hibernate 6.6.18](https://hibernate.org/)** - ORM (Object-Relational Mapping)

### Database
- **[PostgreSQL 17.4](https://www.postgresql.org/)** - Banco de dados relacional
- **[HikariCP](https://github.com/brettwooldridge/HikariCP)** - Connection pool

### Security
- **[JWT 0.11.5](https://jwt.io/)** - JSON Web Tokens
- **[BCrypt](https://spring.io/blog/2017/11/01/spring-security-5-0-x-password-storage-format)** - Hash de senhas

### Tools & Utils
- **[Maven 3.8+](https://maven.apache.org/)** - Gerenciamento de dependências
- **[Lombok](https://projectlombok.org/)** - Redução de código boilerplate
- **[Jackson](https://github.com/FasterXML/jackson)** - Serialização JSON
- **[Bean Validation](https://beanvalidation.org/)** - Validação de dados

### Development
- **[Java 21](https://www.oracle.com/java/)** - Linguagem de programação
- **[Spring Boot DevTools](https://docs.spring.io/spring-boot/docs/current/reference/html/using-boot-devtools.html)** - Ferramentas de desenvolvimento
- **[Spring Boot Actuator](https://docs.spring.io/spring-boot/docs/current/reference/html/production-ready-features.html)** - Monitoramento

## 📋 Pré-requisitos

Antes de executar a aplicação, certifique-se de ter as seguintes ferramentas instaladas:

- **Java 21** ou superior
- **Maven 3.8** ou superior
- **PostgreSQL 17.4** ou superior
- **Git** (para clonar o repositório)

### Verificando as versões:

```bash
# Verificar Java
java --version

# Verificar Maven
mvn --version

# Verificar PostgreSQL
psql --version

# Verificar Git
git --version
```

## 🚀 Instalação

### 1. Clone o repositório

```bash
git clone https://github.com/pierrecbrito/loja-suplementos-api.git
cd loja-suplementos-api
```

### 2. Configure o banco de dados

```sql
-- Conecte ao PostgreSQL e execute:
CREATE DATABASE loja_suplementos;
CREATE USER loja_user WITH ENCRYPTED PASSWORD 'loja_password';
GRANT ALL PRIVILEGES ON DATABASE loja_suplementos TO loja_user;
```

### 3. Configure as variáveis de ambiente

Crie um arquivo `.env` na raiz do projeto ou configure as variáveis de ambiente:

```bash
# Database
DB_HOST=localhost
DB_PORT=5432
DB_NAME=loja_suplementos
DB_USERNAME=loja_user
DB_PASSWORD=loja_password

# JWT
JWT_SECRET=minha-chave-secreta-super-secreta-512-bits-para-jwt-authentication
JWT_EXPIRATION=86400000

# Server
SERVER_PORT=8080
```

### 4. Instale as dependências

```bash
mvn clean install
```

## ⚙️ Configuração

### application.properties

```properties
# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/loja_suplementos
spring.datasource.username=loja_user
spring.datasource.password=loja_password
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# JWT Configuration
app.jwt.secret=minha-chave-secreta-super-secreta-512-bits-para-jwt-authentication
app.jwt.expiration=86400000

# Server Configuration
server.port=8080
server.servlet.context-path=/

# Logging
logging.level.com.suplementos.lojasuplementosapi=DEBUG
logging.level.org.springframework.security=DEBUG
```

## 🎮 Executando a Aplicação

### Desenvolvimento

```bash
# Executar com Maven
mvn spring-boot:run

# Ou compilar e executar o JAR
mvn clean package
java -jar target/loja-suplementos-api-0.0.1-SNAPSHOT.jar
```

### Produção

```bash
# Compilar para produção
mvn clean package -Pprod

# Executar com perfil de produção
java -jar -Dspring.profiles.active=prod target/loja-suplementos-api-0.0.1-SNAPSHOT.jar
```

### 🌐 Acessando a aplicação

- **API Base URL**: `http://localhost:8080/api/v1`
- **Health Check**: `http://localhost:8080/actuator/health`
- **Info**: `http://localhost:8080/actuator/info`

## 🧪 Testando a API

### Opção 1: Postman Collection

1. Importe os arquivos:
   - `Loja-Suplementos-API-Collection.postman_collection.json`
   - `Loja-Suplementos-API-Environment.postman_environment.json`

2. Configure o environment para `http://localhost:8080/api/v1`

3. Execute as requisições na seguinte ordem:
   - Registrar usuário admin
   - Fazer login
   - Criar categorias e produtos
   - Testar funcionalidades

### Opção 2: cURL

```bash
# Registrar usuário
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Admin Sistema",
    "email": "admin@sistema.com",
    "senha": "admin123",
    "telefone": "11999999999",
    "role": "ROLE_ADMIN"
  }'

# Fazer login
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@sistema.com",
    "senha": "admin123"
  }'

# Listar produtos (substitua YOUR_TOKEN pelo token recebido)
curl -X GET http://localhost:8080/api/v1/suplementos \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### Opção 3: Testes Automatizados

```bash
# Executar todos os testes
mvn test

# Executar testes específicos
mvn test -Dtest=UsuarioControllerTest
mvn test -Dtest=SuplementoServiceTest
```

## 📁 Estrutura do Projeto

```
loja-suplementos-api/
├── 📁 src/
│   ├── 📁 main/
│   │   ├── 📁 java/com/suplementos/lojasuplementosapi/
│   │   │   ├── 📁 base/              # Classes base abstratas
│   │   │   ├── 📁 config/            # Configurações (Security, AOP, etc.)
│   │   │   ├── 📁 controller/        # Controllers REST
│   │   │   ├── 📁 core/              # Constantes e utilitários
│   │   │   ├── 📁 domain/            # Entidades JPA
│   │   │   ├── 📁 dto/               # Data Transfer Objects
│   │   │   ├── 📁 erroHandling/      # Tratamento de erros
│   │   │   ├── 📁 hateoas/           # Recursos HATEOAS
│   │   │   ├── 📁 mapper/            # Mapeadores Entity ↔ DTO
│   │   │   ├── 📁 repository/        # Repositories JPA
│   │   │   ├── 📁 security/          # Configurações de segurança
│   │   │   ├── 📁 service/           # Lógica de negócio
│   │   │   └── 📄 LojaSuplementosApiApplication.java
│   │   └── 📁 resources/
│   │       ├── 📄 application.properties
│   │       ├── 📁 static/
│   │       └── 📁 templates/
│   └── 📁 test/                      # Testes unitários e integração
├── 📁 target/                        # Arquivos compilados
├── 📄 pom.xml                        # Configuração Maven
├── 📄 README.md                      # Este arquivo
├── 📄 README-POSTMAN-COLLECTION.md   # Documentação do Postman
├── 📄 Loja-Suplementos-API-Collection.postman_collection.json
├── 📄 Loja-Suplementos-API-Environment.postman_environment.json
└── 📄 .gitignore
```

### 🏛️ Arquitetura das Camadas

#### Domain Layer (Entidades)
- `Usuario` - Usuários do sistema
- `Categoria` - Categorias de produtos  
- `Suplemento` - Produtos da loja
- `Carrinho` - Carrinho de compras (1-1 com Usuario)
- `ItemCarrinho` - Itens do carrinho
- `Pedido` - Pedidos realizados
- `ItemPedido` - Itens do pedido
- `Avaliacao` - Avaliações dos produtos
- `Endereco` - Endereços de entrega
- `LogAuditoria` - Logs de auditoria

#### Controller Layer (REST API)
- `AuthController` - Autenticação e registro
- `UsuarioController` - Gestão de usuários
- `CategoriaController` - Gestão de categorias
- `SuplementoController` - Gestão de produtos
- `CarrinhoController` - Gestão do carrinho
- `PedidoController` - Gestão de pedidos
- `AvaliacaoController` - Gestão de avaliações
- `LogAuditoriaController` - Logs de auditoria

#### Service Layer (Lógica de Negócio)
- `UsuarioService` - Lógica de usuários
- `CategoriaService` - Lógica de categorias
- `SuplementoService` - Lógica de produtos
- `CarrinhoService` - Lógica do carrinho
- `PedidoService` - Lógica de pedidos
- `AvaliacaoService` - Lógica de avaliações
- `AuditoriaService` - Lógica de auditoria

#### Repository Layer (Acesso a Dados)
- `UsuarioRepository` - Acesso a dados de usuários
- `CategoriaRepository` - Acesso a dados de categorias
- `SuplementoRepository` - Acesso a dados de produtos
- `CarrinhoRepository` - Acesso a dados do carrinho
- `PedidoRepository` - Acesso a dados de pedidos
- `AvaliacaoRepository` - Acesso a dados de avaliações
- `LogAuditoriaRepository` - Acesso a dados de auditoria

## 📚 Documentação da API

### 🔐 Autenticação

A API utiliza JWT (JSON Web Tokens) para autenticação. Todas as requisições protegidas devem incluir o header:

```
Authorization: Bearer <token>
```

### 👥 Roles e Permissões

- **ROLE_USER**: Usuários normais (clientes)
- **ROLE_ADMIN**: Administradores (gestão completa)

### 🛣️ Endpoints Principais

#### Autenticação
- `POST /api/v1/auth/register` - Registrar usuário
- `POST /api/v1/auth/login` - Fazer login

#### Usuários
- `GET /api/v1/usuarios` - Listar usuários (Admin)
- `GET /api/v1/usuarios/{id}` - Obter usuário por ID
- `GET /api/v1/usuarios/me` - Obter perfil atual
- `PUT /api/v1/usuarios/{id}` - Atualizar usuário
- `DELETE /api/v1/usuarios/{id}` - Deletar usuário

#### Categorias
- `GET /api/v1/categorias` - Listar categorias
- `GET /api/v1/categorias/{id}` - Obter categoria por ID
- `POST /api/v1/categorias` - Criar categoria (Admin)
- `PUT /api/v1/categorias/{id}` - Atualizar categoria (Admin)
- `DELETE /api/v1/categorias/{id}` - Deletar categoria (Admin)

#### Produtos
- `GET /api/v1/suplementos` - Listar produtos
- `GET /api/v1/suplementos/{id}` - Obter produto por ID
- `GET /api/v1/suplementos/search` - Buscar produtos
- `POST /api/v1/suplementos` - Criar produto (Admin)
- `PUT /api/v1/suplementos/{id}` - Atualizar produto (Admin)
- `DELETE /api/v1/suplementos/{id}` - Deletar produto (Admin)

#### Carrinho
- `GET /api/v1/carrinho` - Obter carrinho
- `POST /api/v1/carrinho/itens` - Adicionar item
- `PUT /api/v1/carrinho/itens/{id}` - Atualizar quantidade
- `DELETE /api/v1/carrinho/itens/{id}` - Remover item
- `DELETE /api/v1/carrinho` - Limpar carrinho

#### Pedidos
- `GET /api/v1/pedidos` - Listar pedidos (Admin)
- `GET /api/v1/pedidos/{id}` - Obter pedido por ID
- `POST /api/v1/pedidos` - Criar pedido
- `PATCH /api/v1/pedidos/{id}/status` - Atualizar status (Admin)

#### Avaliações
- `GET /api/v1/avaliacoes` - Listar avaliações (Admin)
- `GET /api/v1/avaliacoes/{id}` - Obter avaliação por ID
- `POST /api/v1/avaliacoes` - Criar avaliação
- `PUT /api/v1/avaliacoes/{id}` - Atualizar avaliação
- `DELETE /api/v1/avaliacoes/{id}` - Deletar avaliação

#### Auditoria
- `GET /api/v1/logs` - Listar logs (Admin)
- `GET /api/v1/logs/{id}` - Obter log por ID
- `GET /api/v1/logs/tabela/{tabela}` - Logs por tabela
- `GET /api/v1/logs/operacao/{operacao}` - Logs por operação

### 📊 Paginação

Todas as listagens suportam paginação:

```
GET /api/v1/suplementos?page=0&size=10&sort=nome
```

Parâmetros:
- `page`: Número da página (começa em 0)
- `size`: Tamanho da página (padrão: 10)
- `sort`: Campo para ordenação

### 🔗 HATEOAS

Todas as respostas incluem links de navegação:

```json
{
  "content": {
    "id": 1,
    "nome": "Whey Protein",
    "preco": 89.90
  },
  "_links": {
    "self": {
      "href": "http://localhost:8080/api/v1/suplementos/1"
    },
    "avaliacoes": {
      "href": "http://localhost:8080/api/v1/avaliacoes/suplemento/1"
    }
  }
}
```

### 🛡️ Códigos de Status

- `200 OK` - Operação bem-sucedida
- `201 Created` - Recurso criado com sucesso
- `204 No Content` - Operação de delete bem-sucedida
- `400 Bad Request` - Dados inválidos
- `401 Unauthorized` - Token inválido ou ausente
- `403 Forbidden` - Sem permissão para a operação
- `404 Not Found` - Recurso não encontrado
- `409 Conflict` - Conflito (ex: email já existe)
- `500 Internal Server Error` - Erro interno do servidor

## 🤝 Contribuindo

Contribuições são bem-vindas! Siga estes passos:

1. **Fork** o projeto
2. **Crie** uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. **Commit** suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. **Push** para a branch (`git push origin feature/AmazingFeature`)
5. **Abra** um Pull Request

### 📝 Padrões de Commit

Use o padrão [Conventional Commits](https://www.conventionalcommits.org/):

```
feat: adiciona novo endpoint para busca de produtos
fix: corrige validação de email no cadastro
docs: atualiza documentação da API
style: formata código seguindo padrão
refactor: refatora service de usuários
test: adiciona testes para carrinho service
```

