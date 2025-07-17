# E-commerce com Onion Architecture

Este projeto implementa um sistema de e-commerce simples seguindo os princípios da Onion Architecture (Arquitetura Cebola) usando Java 23, Spring Boot 3.2.0 e Oracle XE.

## Arquitetura

O projeto está organizado em quatro camadas seguindo a Onion Architecture:

### 1. Domain (Núcleo)
- Contém as entidades de negócio: `User`, `Product`, `Order`, `OrderProduct`
- Regras de negócio puras sem dependências externas
- Anotações JPA para mapeamento objeto-relacional

### 2. Application (Casos de Uso)
- Interfaces de repositório
- Serviços de aplicação com lógica de negócio
- Orquestração de operações

### 3. Infrastructure (Adaptadores de Saída)
- Implementações concretas dos repositórios usando Spring Data JPA
- Configuração do banco de dados Oracle
- Configuração do Spring Boot

### 4. Presentation (Adaptadores de Entrada)
- Controladores REST
- DTOs para transferência de dados
- Validações de entrada

## Pré-requisitos

- Java 23
- Maven 3.8+
- Docker e Docker Compose
- Oracle Container Registry (para baixar a imagem do Oracle XE)

## Configuração

### 1. Configurar Oracle Container Registry

Primeiro, você precisa fazer login no Oracle Container Registry:

```bash
docker login container-registry.oracle.com
```

### 2. Iniciar o Oracle XE

```bash
docker-compose up -d
```

Aguarde alguns minutos para o Oracle inicializar completamente.

### 3. Compilar o projeto

```bash
mvn clean install
```

### 4. Executar a aplicação

```bash
mvn spring-boot:run -pl infrastructure
```

A aplicação estará disponível em: http://localhost:8080

## Endpoints da API

### Usuários

- `GET /api/users` - Listar todos os usuários
- `GET /api/users/{id}` - Buscar usuário por ID
- `GET /api/users/email/{email}` - Buscar usuário por email
- `POST /api/users` - Criar novo usuário
- `PUT /api/users/{id}` - Atualizar usuário
- `DELETE /api/users/{id}` - Deletar usuário

### Produtos

- `GET /api/products` - Listar todos os produtos
- `GET /api/products/{id}` - Buscar produto por ID
- `GET /api/products/search?name={name}` - Buscar produtos por nome
- `POST /api/products` - Criar novo produto
- `PUT /api/products/{id}` - Atualizar produto
- `PUT /api/products/{id}/stock?quantity={quantity}` - Atualizar estoque
- `DELETE /api/products/{id}` - Deletar produto

### Pedidos

- `GET /api/orders` - Listar todos os pedidos
- `GET /api/orders/{id}` - Buscar pedido por ID
- `GET /api/orders/user/{userId}` - Buscar pedidos por usuário
- `POST /api/orders` - Criar novo pedido
- `PUT /api/orders/{id}/status?status={status}` - Atualizar status do pedido
- `POST /api/orders/{orderId}/products?productId={productId}&quantity={quantity}` - Adicionar produto ao pedido
- `DELETE /api/orders/{orderId}/products/{productId}` - Remover produto do pedido
- `DELETE /api/orders/{id}` - Deletar pedido

## Exemplos de Uso

### Criar um usuário

```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "email": "joao@email.com",
    "name": "João Silva",
    "password": "123456",
    "address": "Rua das Flores, 123"
  }'
```

### Criar um produto

```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Smartphone XYZ",
    "description": "Smartphone de última geração",
    "price": 1299.99,
    "stockQuantity": 50
  }'
```

### Criar um pedido

```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "orderProducts": [
      {
        "productId": 1,
        "quantity": 2
      }
    ]
  }'
```

## Estrutura do Projeto

```
ecommerce-onion/
├── domain/                 # Entidades de negócio
├── application/            # Casos de uso e serviços
├── infrastructure/         # Implementações de repositório e configuração
├── presentation/           # Controladores REST e DTOs
├── docker-compose.yml      # Configuração do Oracle XE
└── pom.xml                 # Configuração Maven principal
```

## Tecnologias Utilizadas

- **Java 23** - Linguagem de programação
- **Spring Boot 3.2.0** - Framework de aplicação
- **Spring Data JPA** - Persistência de dados
- **Oracle XE** - Banco de dados
- **Maven** - Gerenciamento de dependências
- **Docker** - Containerização do banco de dados

## Benefícios da Onion Architecture

1. **Independência de Framework**: O domínio não depende de frameworks externos
2. **Testabilidade**: Fácil de testar cada camada isoladamente
3. **Flexibilidade**: Mudanças em uma camada não afetam outras
4. **Manutenibilidade**: Código organizado e fácil de manter
5. **Escalabilidade**: Fácil de adicionar novas funcionalidades

## Próximos Passos

- Implementar autenticação e autorização
- Adicionar testes unitários e de integração
- Implementar cache com Redis
- Adicionar documentação com Swagger
- Implementar logs estruturados
- Adicionar monitoramento com Micrometer 