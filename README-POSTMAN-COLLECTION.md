# Loja Suplementos API - Collection Postman

Esta collection contém todos os endpoints para testar a API REST da Loja de Suplementos construída com Spring Boot 3.5.3.

## 📋 Requisitos Implementados

### ✅ Requisitos Funcionais
- **Estilo arquitetural REST**: Implementado com Spring Boot e Spring Web
- **Entidades com relacionamentos**: 
  - **1-1**: Usuario ↔ Carrinho
  - **1-N**: Usuario → Pedidos, Usuario → Avaliações, Suplemento → Avaliações
  - **N-N**: Suplemento ↔ Categoria
- **CRUD completo**: Todos os recursos têm endpoints para Create, Read, Update e Delete
- **Auditoria/Logging**: Sistema de logs automático para todas as operações do banco
- **Verbos HTTP adequados**: GET, POST, PUT, PATCH, DELETE com status codes corretos
- **HATEOAS Nível 3**: Links de navegação em todas as respostas
- **DTOs**: Request/Response separados para cada operação
- **Autenticação JWT**: Login stateless com tokens JWT
- **Autorização baseada em roles**: ROLE_USER e ROLE_ADMIN
- **Busca paginada**: Todas as listagens suportam paginação

### 🏗️ Arquitetura
```
├── Controller Layer (REST endpoints)
├── Service Layer (Business logic)
├── Repository Layer (Data access)
├── Domain Layer (Entities)
├── DTO Layer (Data transfer objects)
├── Security Layer (JWT + Authorization)
├── Config Layer (Spring configuration)
└── Audit Layer (Automatic logging)
```

## 🚀 Como Usar

### 1. Importar no Postman
1. Abra o Postman
2. Clique em "Import"
3. Selecione os arquivos:
   - `Loja-Suplementos-API-Collection.postman_collection.json`
   - `Loja-Suplementos-API-Environment.postman_environment.json`

### 2. Configurar Environment
1. Selecione o environment "Loja Suplementos API - Environment"
2. Verifique se a variável `baseUrl` está definida como `http://localhost:8080/api/v1`

### 3. Executar a Aplicação
```bash
cd loja-suplementos-api
mvn spring-boot:run
```

### 4. Testar a API

#### Fluxo Recomendado:
1. **Autenticação**
   - Registrar usuário admin
   - Registrar usuário cliente
   - Fazer login com ambos (tokens são salvos automaticamente)

2. **Configurar Dados Base**
   - Criar categorias (Admin)
   - Criar suplementos (Admin)

3. **Fluxo do Cliente**
   - Visualizar suplementos
   - Adicionar itens ao carrinho
   - Criar pedido
   - Avaliar produtos

4. **Fluxo do Admin**
   - Gerenciar usuários
   - Gerenciar produtos
   - Acompanhar pedidos
   - Visualizar logs de auditoria

## 📁 Estrutura da Collection

### 1. Autenticação
- **POST** `/auth/register` - Registrar usuário
- **POST** `/auth/login` - Fazer login

### 2. Usuários
- **GET** `/usuarios` - Listar usuários (Admin)
- **GET** `/usuarios/{id}` - Obter usuário por ID
- **GET** `/usuarios/me` - Obter perfil atual
- **PUT** `/usuarios/{id}` - Atualizar usuário
- **DELETE** `/usuarios/{id}` - Deletar usuário

### 3. Categorias
- **GET** `/categorias` - Listar categorias
- **GET** `/categorias/{id}` - Obter categoria por ID
- **POST** `/categorias` - Criar categoria (Admin)
- **PUT** `/categorias/{id}` - Atualizar categoria (Admin)
- **DELETE** `/categorias/{id}` - Deletar categoria (Admin)

### 4. Suplementos
- **GET** `/suplementos` - Listar suplementos
- **GET** `/suplementos/{id}` - Obter suplemento por ID
- **GET** `/suplementos/categoria/{id}` - Buscar por categoria
- **GET** `/suplementos/search` - Buscar por nome/marca
- **POST** `/suplementos` - Criar suplemento (Admin)
- **PUT** `/suplementos/{id}` - Atualizar suplemento (Admin)
- **DELETE** `/suplementos/{id}` - Deletar suplemento (Admin)

### 5. Carrinho
- **GET** `/carrinho` - Obter carrinho do usuário
- **POST** `/carrinho/itens` - Adicionar item
- **PUT** `/carrinho/itens/{id}` - Atualizar quantidade
- **DELETE** `/carrinho/itens/{id}` - Remover item
- **DELETE** `/carrinho` - Limpar carrinho

### 6. Pedidos
- **GET** `/pedidos` - Listar pedidos (Admin)
- **GET** `/pedidos/{id}` - Obter pedido por ID
- **GET** `/pedidos/usuario/{id}` - Pedidos do usuário
- **GET** `/pedidos/status/{status}` - Buscar por status (Admin)
- **POST** `/pedidos` - Criar pedido
- **PATCH** `/pedidos/{id}/status` - Atualizar status (Admin)
- **DELETE** `/pedidos/{id}` - Deletar pedido

### 7. Avaliações
- **GET** `/avaliacoes` - Listar avaliações (Admin)
- **GET** `/avaliacoes/{id}` - Obter avaliação por ID
- **GET** `/avaliacoes/suplemento/{id}` - Avaliações do suplemento
- **GET** `/avaliacoes/usuario/{id}` - Avaliações do usuário
- **POST** `/avaliacoes` - Criar avaliação
- **PUT** `/avaliacoes/{id}` - Atualizar avaliação
- **DELETE** `/avaliacoes/{id}` - Deletar avaliação

### 8. Auditoria (Admin)
- **GET** `/logs` - Listar logs de auditoria
- **GET** `/logs/{id}` - Obter log por ID
- **GET** `/logs/tabela/{tabela}` - Logs por tabela
- **GET** `/logs/operacao/{operacao}` - Logs por operação

### 9. Testes de Autorização
- Requisições para testar controle de acesso
- Exemplos de erro 401 (Não autenticado) e 403 (Não autorizado)

## 🔐 Autorização

### ROLE_USER (Cliente)
- ✅ Visualizar produtos e categorias
- ✅ Gerenciar próprio carrinho
- ✅ Criar e visualizar próprios pedidos
- ✅ Criar e gerenciar próprias avaliações
- ✅ Visualizar e editar próprio perfil
- ❌ Acessar dados de outros usuários
- ❌ Gerenciar produtos/categorias
- ❌ Visualizar logs de auditoria

### ROLE_ADMIN (Administrador)
- ✅ Todas as permissões do ROLE_USER
- ✅ Gerenciar usuários
- ✅ Gerenciar produtos e categorias
- ✅ Visualizar todos os pedidos
- ✅ Atualizar status dos pedidos
- ✅ Visualizar logs de auditoria
- ✅ Acessar dados de todos os usuários

## 📊 Paginação

Todas as listagens suportam paginação via query parameters:
- `page`: Número da página (começa em 0)
- `size`: Tamanho da página (padrão: 10)
- `sort`: Campo para ordenação (ex: `nome`, `dataCriacao`)

Exemplo: `/suplementos?page=0&size=5&sort=nome`

## 🔗 HATEOAS

Todas as respostas incluem links de navegação:
```json
{
  "content": { ... },
  "_links": {
    "self": { "href": "http://localhost:8080/api/v1/suplementos/1" },
    "avaliacoes": { "href": "http://localhost:8080/api/v1/avaliacoes/suplemento/1" }
  }
}
```

## 📝 Logs de Auditoria

O sistema registra automaticamente:
- **INSERT**: Criação de novos registros
- **UPDATE**: Atualizações de registros
- **DELETE**: Exclusão de registros
- **SELECT**: Consultas por ID

Cada log inclui:
- Tabela afetada
- Tipo de operação
- ID do registro
- Usuário responsável
- Data/hora da operação
- IP do cliente
- Dados antes/depois (quando aplicável)

## 🧪 Testes

### Status Codes Esperados:
- **200 OK**: Operação bem-sucedida
- **201 Created**: Recurso criado com sucesso
- **204 No Content**: Operação de delete bem-sucedida
- **400 Bad Request**: Dados inválidos
- **401 Unauthorized**: Token inválido/ausente
- **403 Forbidden**: Sem permissão para a operação
- **404 Not Found**: Recurso não encontrado
- **409 Conflict**: Conflito (ex: email já existe)

### Cenários de Teste:
1. **Autenticação/Autorização**
   - Login com credenciais válidas/inválidas
   - Acesso com/sem token
   - Teste de permissões por role

2. **CRUD Operations**
   - Criar, ler, atualizar e deletar recursos
   - Validação de dados obrigatórios
   - Relacionamentos entre entidades

3. **Business Logic**
   - Adicionar itens ao carrinho
   - Criar pedidos
   - Avaliar produtos
   - Gerenciar estoque

4. **Paginação e Busca**
   - Navegação entre páginas
   - Filtros por diferentes critérios
   - Ordenação de resultados

## 🛠️ Tecnologias Utilizadas

- **Spring Boot 3.5.3**: Framework principal
- **Spring Security**: Autenticação e autorização
- **Spring Data JPA**: Persistência de dados
- **Spring HATEOAS**: Links de navegação
- **Spring AOP**: Auditoria automática
- **PostgreSQL**: Banco de dados
- **JWT**: Autenticação stateless
- **Maven**: Gerenciamento de dependências
- **Lombok**: Redução de código boilerplate
- **Bean Validation**: Validação de dados

## 📞 Suporte

Para dúvidas ou problemas:
1. Verifique se a aplicação está executando na porta 8080
2. Confirme se o banco PostgreSQL está disponível
3. Verifique os logs da aplicação para erros
4. Teste primeiro os endpoints de autenticação

## 🎯 Próximos Passos

Para expandir a API, considere:
- Implementar cache com Redis
- Adicionar documentação OpenAPI/Swagger
- Implementar rate limiting
- Adicionar métricas com Micrometer
- Implementar testes automatizados
- Adicionar CI/CD pipeline
