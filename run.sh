#!/bin/bash

# Cores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Função para mostrar ajuda
show_help() {
    echo -e "${BLUE}📖 Uso do script:${NC}"
    echo -e "  ${GREEN}./run.sh${NC}                    - Executa tudo (Docker + Build + Run)"
    echo -e "  ${GREEN}./run.sh build${NC}              - Apenas mvn clean install"
    echo -e "  ${GREEN}./run.sh clean${NC}              - Apenas mvn clean"
    echo -e "  ${GREEN}./run.sh test${NC}               - Executa testes"
    echo -e "  ${GREEN}./run.sh docker${NC}             - Apenas inicia Docker (Oracle XE)"
    echo -e "  ${GREEN}./run.sh docker-stop${NC}        - Para containers Docker"
    echo -e "  ${GREEN}./run.sh run${NC}                - Apenas executa a API"
    echo -e "  ${GREEN}./run.sh help${NC}               - Mostra esta ajuda"
    echo ""
    echo -e "${YELLOW}💡 Exemplos:${NC}"
    echo -e "  ${GREEN}./run.sh build${NC}              - Para fazer apenas o build"
    echo -e "  ${GREEN}./run.sh docker && ./run.sh build${NC} - Inicia Docker e depois faz build"
}

# Verificar se o Java está instalado
check_java() {
    if ! java -version > /dev/null 2>&1; then
        echo -e "${RED}❌ Java não está instalado. Por favor, instale o Java 17+ e tente novamente.${NC}"
        exit 1
    fi
    
    JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
    if [ "$JAVA_VERSION" -lt 17 ]; then
        echo -e "${RED}❌ Java 17+ é necessário. Versão atual: $JAVA_VERSION${NC}"
        exit 1
    fi
    
    echo -e "${GREEN}✅ Java $JAVA_VERSION detectado${NC}"
}

# Verificar se o Maven está instalado
check_maven() {
    if ! mvn -version > /dev/null 2>&1; then
        echo -e "${RED}❌ Maven não está instalado. Por favor, instale o Maven e tente novamente.${NC}"
        exit 1
    fi
    
    echo -e "${GREEN}✅ Maven detectado${NC}"
}

# Verificar se o Docker está rodando
check_docker() {
    if ! docker info > /dev/null 2>&1; then
        echo -e "${RED}❌ Docker não está rodando. Por favor, inicie o Docker e tente novamente.${NC}"
        exit 1
    fi
}

# Função para verificar se o Oracle XE já está rodando
check_oracle_running() {
    if nc -z localhost 1521 2>/dev/null; then
        echo -e "${GREEN}✅ Oracle XE já está rodando!${NC}"
        return 0
    fi
    return 1
}

# Função para iniciar Docker
start_docker() {
    # Verifica se já está rodando
    if check_oracle_running; then
        return 0
    fi
    
    echo -e "${YELLOW}🐳 Iniciando Oracle XE...${NC}"
    docker-compose up -d
    
    echo -e "${YELLOW}⏳ Aguardando Oracle XE estar pronto...${NC}"
    echo -e "${YELLOW}💡 Na primeira execução, pode levar até 3 minutos${NC}"
    sleep 30
    
    echo -e "${YELLOW}🔍 Verificando conexão com Oracle XE...${NC}"
    for i in {1..30}; do
        # Verifica se a porta está respondendo
        if nc -z localhost 1521 2>/dev/null; then
            echo -e "${GREEN}✅ Oracle XE está pronto! (Porta 1521 respondendo)${NC}"
            return 0
        fi
        
        if [ $i -eq 30 ]; then
            echo -e "${RED}❌ Timeout aguardando Oracle XE${NC}"
            echo -e "${YELLOW}💡 Dica: Verifique se o Docker está rodando e tente novamente${NC}"
            echo -e "${YELLOW}💡 Na primeira execução, o Oracle XE pode demorar mais${NC}"
            return 1
        fi
        echo -e "${YELLOW}⏳ Tentativa $i/30...${NC}"
        sleep 6
    done
}

# Função para executar a API
run_api() {
    echo -e "${GREEN}🎯 Iniciando a API Spring Boot...${NC}"
    echo -e "${BLUE}📱 A API estará disponível em: http://localhost:8080${NC}"
    echo -e "${BLUE}📚 Endpoints disponíveis:${NC}"
    echo -e "${BLUE}   - Usuários: http://localhost:8080/api/users${NC}"
    echo -e "${BLUE}   - Produtos: http://localhost:8080/api/products${NC}"
    echo -e "${BLUE}   - Pedidos: http://localhost:8080/api/orders${NC}"
    echo -e "${YELLOW}⏹️ Pressione Ctrl+C para parar${NC}"
    
    mvn spring-boot:run -pl infrastructure
}

# Verificar argumentos
case "${1:-}" in
    "build")
        echo -e "${BLUE}🔨 Executando mvn clean install...${NC}"
        check_java
        check_maven
        mvn clean install
        ;;
    "clean")
        echo -e "${BLUE}🧹 Executando mvn clean...${NC}"
        check_java
        check_maven
        mvn clean
        ;;
    "test")
        echo -e "${BLUE}🧪 Executando testes...${NC}"
        check_java
        check_maven
        mvn test
        ;;
    "docker")
        echo -e "${BLUE}🐳 Iniciando Docker...${NC}"
        check_docker
        
        # Verifica se já está rodando
        if check_oracle_running; then
            echo -e "${YELLOW}ℹ️ Oracle XE já está rodando, não é necessário reiniciar${NC}"
            exit 0
        fi
        
        # Se não estiver rodando, para containers e inicia
        docker-compose down
        start_docker
        ;;
    "docker-stop")
        echo -e "${BLUE}🛑 Parando Docker...${NC}"
        docker-compose down
        ;;
    "run")
        echo -e "${BLUE}🎯 Executando API...${NC}"
        check_java
        check_maven
        run_api
        ;;
    "help"|"-h"|"--help")
        show_help
        ;;
    "")
        # Execução completa (comportamento padrão)
        echo -e "${BLUE}🚀 Iniciando E-commerce Onion Architecture...${NC}"
        
        check_docker
        check_java
        check_maven
        echo -e "${GREEN}✅ Docker, Java e Maven verificados${NC}"
        
        # Iniciar Docker (só para containers se necessário)
        if ! start_docker; then
            exit 1
        fi
        
        # Build da aplicação
        echo -e "${YELLOW}🔨 Fazendo build da aplicação...${NC}"
        if ! mvn clean install; then
            echo -e "${RED}❌ Build falhou${NC}"
            exit 1
        fi
        
        echo -e "${GREEN}✅ Build concluído com sucesso!${NC}"
        
        # Executar a aplicação
        run_api
        ;;
    *)
        echo -e "${RED}❌ Comando desconhecido: $1${NC}"
        echo ""
        show_help
        exit 1
        ;;
esac 