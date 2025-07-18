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
    echo -e "  ${GREEN}./run.sh stop${NC}               - Para a API"
    echo -e "  ${GREEN}./run.sh status${NC}             - Mostra status da aplicação"
    echo -e "  ${GREEN}./run.sh logs${NC}               - Mostra logs da API"
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

# Verificar se a API está rodando
check_api_running() {
    if nc -z localhost 8080 2>/dev/null; then
        echo -e "${GREEN}✅ API já está rodando! (Porta 8080)${NC}"
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
    # Verifica se já está rodando
    if check_api_running; then
        echo -e "${YELLOW}ℹ️ API já está rodando em http://localhost:8080${NC}"
        echo -e "${YELLOW}💡 Para parar: ./run.sh stop${NC}"
        return 0
    fi
    
    echo -e "${GREEN}🎯 Iniciando a API Spring Boot...${NC}"
    echo -e "${BLUE}📱 A API estará disponível em: http://localhost:8080${NC}"
    echo -e "${BLUE}📚 Endpoints disponíveis:${NC}"
    echo -e "${BLUE}   - Usuários: http://localhost:8080/api/users${NC}"
    echo -e "${BLUE}   - Produtos: http://localhost:8080/api/products${NC}"
    echo -e "${BLUE}   - Pedidos: http://localhost:8080/api/orders${NC}"
    echo -e "${YELLOW}⏹️ Pressione Ctrl+C para parar${NC}"
    
    # Executa em background e mostra logs
    mvn spring-boot:run -pl presentation > app.log 2>&1 &
    APP_PID=$!
    
    # Aguarda um pouco para a aplicação inicializar
    sleep 15
    
    # Verifica se a aplicação está rodando
    if kill -0 $APP_PID 2>/dev/null && check_api_running; then
        echo -e "${GREEN}✅ API iniciada com sucesso! (PID: $APP_PID)${NC}"
        echo -e "${BLUE}🌐 Acesse: http://localhost:8080${NC}"
        echo -e "${YELLOW}💡 Para parar a aplicação: ./run.sh stop${NC}"
        echo -e "${YELLOW}💡 Logs: tail -f app.log${NC}"
        
        # Aguarda o usuário pressionar Ctrl+C
        trap "echo -e '\n${YELLOW}🛑 Parando aplicação...${NC}'; kill $APP_PID; exit 0" INT
        wait $APP_PID
    else
        echo -e "${RED}❌ Falha ao iniciar a API${NC}"
        echo -e "${YELLOW}💡 Verifique os logs: cat app.log${NC}"
        exit 1
    fi
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
    "stop")
        echo -e "${BLUE}🛑 Parando API...${NC}"
        # Encontra o processo Java da aplicação
        APP_PID=$(pgrep -f "spring-boot:run.*presentation" | head -1)
        if [ -n "$APP_PID" ]; then
            echo -e "${YELLOW}🔄 Parando processo $APP_PID...${NC}"
            kill $APP_PID
            sleep 3
            if kill -0 $APP_PID 2>/dev/null; then
                echo -e "${RED}⚠️ Processo não parou, forçando...${NC}"
                kill -9 $APP_PID
            fi
            echo -e "${GREEN}✅ API parada com sucesso!${NC}"
        else
            echo -e "${YELLOW}ℹ️ Nenhuma API rodando encontrada${NC}"
        fi
        ;;
    "status")
        echo -e "${BLUE}📊 Status da aplicação:${NC}"
        if check_oracle_running; then
            echo -e "${GREEN}✅ Oracle XE: Rodando${NC}"
        else
            echo -e "${RED}❌ Oracle XE: Parado${NC}"
        fi
        
        if check_api_running; then
            echo -e "${GREEN}✅ API: Rodando em http://localhost:8080${NC}"
            APP_PID=$(pgrep -f "spring-boot:run.*presentation" | head -1)
            if [ -n "$APP_PID" ]; then
                echo -e "${BLUE}📋 PID: $APP_PID${NC}"
            fi
        else
            echo -e "${RED}❌ API: Parada${NC}"
        fi
        ;;
    "logs")
        if [ -f "app.log" ]; then
            echo -e "${BLUE}📋 Mostrando logs da API:${NC}"
            tail -f app.log
        else
            echo -e "${YELLOW}ℹ️ Arquivo de log não encontrado${NC}"
            echo -e "${YELLOW}💡 Execute a API primeiro: ./run.sh run${NC}"
        fi
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