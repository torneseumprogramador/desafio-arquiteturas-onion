# 🛒 Ecommerce Onion Architecture - Desafio de Arquiteturas de Software

## 📚 Sobre o Projeto

Este projeto foi desenvolvido como parte do **Desafio de Arquiteturas de Software** do curso [Arquiteturas de Software Modernas](https://www.torneseumprogramador.com.br/cursos/arquiteturas_software) ministrado pelo **Prof. Danilo Aparecido** na plataforma [Torne-se um Programador](https://www.torneseumprogramador.com.br/).

### 🎯 Objetivo

Implementar um sistema de e-commerce utilizando **Onion Architecture (Arquitetura Cebola)** com Java e Spring Boot, demonstrando boas práticas de desenvolvimento e organização de código em camadas.

## 🏗️ Arquitetura

O projeto segue os princípios da **Onion Architecture (Arquitetura Cebola)** com uma arquitetura em camadas bem definidas:

```
┌─────────────────────────────────────┐
│           Presentation              │ ← Controllers, DTOs, REST API
├─────────────────────────────────────┤
│            Application              │ ← Services, Use Cases, Interfaces
├─────────────────────────────────────┤
│              Domain                 │ ← Entities, Business Rules
├─────────────────────────────────────┤
│          Infrastructure             │ ← Repositories, Database, External Services
└─────────────────────────────────────┘
```

### 📁 Estrutura do Projeto

```
ecommerce-onion/
├── domain/                           # Camada de Domínio
│   ├── src/main/java/
│   │   └── com/ecommerce/domain/
│   │       ├── User.java             # Entidade de usuário
│   │       ├── Product.java          # Entidade de produto
│   │       ├── Order.java            # Entidade de pedido
│   │       └── OrderProduct.java     # Entidade de produto do pedido
│   └── pom.xml
├── application/                      # Camada de Aplicação
│   ├── src/main/java/
│   │   └── com/ecommerce/application/
│   │       ├── repository/           # Interfaces dos repositórios
│   │       └── service/              # Serviços de aplicação
│   └── pom.xml
├── infrastructure/                   # Camada de Infraestrutura
│   ├── src/main/java/
│   │   └── com/ecommerce/infrastructure/
│   │       ├── repository/           # Implementações dos repositórios
│   │       ├── App.java              # Classe principal
│   │       └── EcommerceApplication.java
│   ├── src/main/resources/
│   │   └── application.properties    # Configurações
│   └── pom.xml
├── presentation/                     # Camada de Apresentação
│   ├── src/main/java/
│   │   └── com/ecommerce/presentation/
│   │       ├── controller/           # Controllers REST
│   │       ├── dto/                  # Data Transfer Objects
│   │       └── App.java
│   └── pom.xml
├── docker-compose.yml               # Configuração do Oracle XE
├── run.sh                           # Script de execução
├── pom.xml                          # POM principal (multi-módulo)
└── README.md                        # Esta documentação
```

## 🚀 Tecnologias Utilizadas

- **Java 23** - Linguagem de programação
- **Spring Boot 3.2.0** - Framework de aplicação
- **Spring Data JPA** - ORM para acesso a dados
- **Oracle XE** - Banco de dados (via Docker)
- **Maven** - Gerenciamento de dependências
- **Docker Compose** - Containerização do banco de dados
- **Onion Architecture** - Arquitetura em camadas

## 📋 Pré-requisitos

- [Java 23 JDK](https://adoptium.net/)
- [Maven 3.8+](https://maven.apache.org/download.cgi)
- [Docker Desktop](https://www.docker.com/products/docker-desktop)
- [Git](https://git-scm.com/)

## ⚡ Como Executar

### Método Rápido (Recomendado)

```bash
# Clone o repositório
git clone <url-do-repositorio>
cd desafio-arquiteturas-onion/ecommerce-onion

# Execute o script que faz tudo automaticamente
./run.sh
```

### Método Manual

```bash
# 1. Configurar Oracle Container Registry
docker login container-registry.oracle.com

# 2. Iniciar Oracle XE
docker-compose up -d

# 3. Build da aplicação
mvn clean install

# 4. Executar a aplicação
mvn spring-boot:run -pl infrastructure
```

### Comandos Disponíveis no Script

```bash
./run.sh              # Executa tudo (Docker + Build + Run)
./run.sh build        # Apenas mvn clean install
./run.sh docker       # Apenas inicia Oracle XE
./run.sh docker-stop  # Para containers Docker
./run.sh run          # Apenas executa a aplicação
./run.sh help         # Mostra ajuda
```

## 🌐 Acessando a API

Após executar o projeto, a API estará disponível em:

- **API Base**: http://localhost:8080
- **Health Check**: http://localhost:8080/actuator/health

## 📖 Endpoints da API

### 👥 Usuários (Users)

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/api/users` | Criar usuário |
| GET | `/api/users` | Listar todos os usuários |
| GET | `/api/users/{id}` | Buscar usuário por ID |
| GET | `/api/users/email/{email}` | Buscar usuário por email |
| PUT | `/api/users/{id}` | Atualizar usuário |
| DELETE | `/api/users/{id}` | Remover usuário |

### 📦 Produtos (Products)

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/api/products` | Criar produto |
| GET | `/api/products` | Listar todos os produtos |
| GET | `/api/products/{id}` | Buscar produto por ID |
| GET | `/api/products/search?name={name}` | Buscar produtos por nome |
| PUT | `/api/products/{id}` | Atualizar produto |
| DELETE | `/api/products/{id}` | Remover produto |

### 🛒 Pedidos (Orders)

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/api/orders` | Criar pedido |
| GET | `/api/orders` | Listar todos os pedidos |
| GET | `/api/orders/{id}` | Buscar pedido por ID |
| GET | `/api/orders/user/{userId}` | Pedidos por usuário |
| PUT | `/api/orders/{id}/status?status={status}` | Atualizar status do pedido |
| POST | `/api/orders/{orderId}/products?productId={productId}&quantity={quantity}` | Adicionar produto ao pedido |
| DELETE | `/api/orders/{orderId}/products/{productId}` | Remover produto do pedido |
| DELETE | `/api/orders/{id}` | Remover pedido |

## 🏛️ Conceitos da Onion Architecture Implementados

### 📦 Entidades de Domínio

- **User** (Usuário) - Entidade de usuário do sistema
- **Product** (Produto) - Entidade de produto do catálogo
- **Order** (Pedido) - Entidade de pedido
- **OrderProduct** (Produto do Pedido) - Entidade de relacionamento

### 🔄 Camadas da Arquitetura

1. **Domain Layer** - Núcleo da aplicação
   - Entidades de negócio
   - Regras de negócio puras
   - Sem dependências externas

2. **Application Layer** - Casos de uso
   - Interfaces de repositório
   - Serviços de aplicação
   - Orquestração de operações

3. **Infrastructure Layer** - Adaptadores de saída
   - Implementações de repositório
   - Configuração do banco de dados
   - Configuração do Spring Boot

4. **Presentation Layer** - Adaptadores de entrada
   - Controllers REST
   - DTOs para transferência de dados
   - Validações de entrada

## 🧪 Exemplos de Uso

### Criar Usuário

```bash
curl -X POST "http://localhost:8080/api/users" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "joao@email.com",
    "name": "João Silva",
    "password": "123456",
    "address": "Rua das Flores, 123"
  }'
```

### Criar Produto

```bash
curl -X POST "http://localhost:8080/api/products" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Smartphone XYZ",
    "description": "Smartphone de última geração",
    "price": 1299.99,
    "stockQuantity": 50
  }'
```

### Criar Pedido

```bash
curl -X POST "http://localhost:8080/api/orders" \
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

## 🔧 Configuração do Banco de Dados

O projeto utiliza **Oracle XE** rodando em **Docker** com as seguintes configurações:

- **Host**: localhost
- **Porta**: 1521
- **SID**: XE
- **Usuário**: system
- **Senha**: oracle

### Connection String

```properties
spring.datasource.url=jdbc:oracle:thin:@localhost:1521:XE
spring.datasource.username=system
spring.datasource.password=oracle
spring.datasource.driver-class-name=oracle.jdbc.OracleDriver
```

## 📝 Configuração do Oracle Container Registry

Antes de executar o projeto, você precisa fazer login no Oracle Container Registry:

```bash
docker login container-registry.oracle.com
```

## 🎓 Aprendizados do Curso

Este projeto demonstra os seguintes conceitos aprendidos no curso:

1. **Onion Architecture (Arquitetura Cebola)**
   - Separação de responsabilidades em camadas
   - Inversão de dependência
   - Independência de frameworks

2. **Arquitetura em Camadas**
   - Domain Layer (núcleo)
   - Application Layer (casos de uso)
   - Infrastructure Layer (adaptadores de saída)
   - Presentation Layer (adaptadores de entrada)

3. **Padrões de Projeto**
   - Repository Pattern
   - Service Layer Pattern
   - DTO Pattern

4. **Boas Práticas**
   - SOLID Principles
   - Clean Code
   - Dependency Inversion Principle

## 🏆 Benefícios da Onion Architecture

1. **Independência de Framework**: O domínio não depende de frameworks externos
2. **Testabilidade**: Fácil de testar cada camada isoladamente
3. **Flexibilidade**: Mudanças em uma camada não afetam outras
4. **Manutenibilidade**: Código organizado e fácil de manter
5. **Escalabilidade**: Fácil de adicionar novas funcionalidades

## 👨‍🏫 Sobre o Professor

**Prof. Danilo Aparecido** é instrutor na plataforma [Torne-se um Programador](https://www.torneseumprogramador.com.br/), especializado em arquiteturas de software e desenvolvimento de sistemas escaláveis.

## 📚 Curso Completo

Para aprender mais sobre arquiteturas de software e aprofundar seus conhecimentos, acesse o curso completo:

**[Arquiteturas de Software Modernas](https://www.torneseumprogramador.com.br/cursos/arquiteturas_software)**

## 🚀 Próximos Passos

- Implementar autenticação e autorização com Spring Security
- Adicionar testes unitários e de integração com JUnit 5
- Implementar cache com Redis
- Adicionar documentação com Swagger/OpenAPI
- Implementar logs estruturados com Logback
- Adicionar monitoramento com Micrometer e Prometheus
- Implementar validações mais robustas com Bean Validation
- Adicionar tratamento de exceções global

## 🤝 Contribuição

Este projeto foi desenvolvido como parte de um desafio educacional. Contribuições são bem-vindas através de issues e pull requests.

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

---

**Desenvolvido com ❤️ para o curso de Arquiteturas de Software do [Torne-se um Programador](https://www.torneseumprogramador.com.br/)** 