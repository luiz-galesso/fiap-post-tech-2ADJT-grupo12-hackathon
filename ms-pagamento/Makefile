build:
	mvn compile

unit-test:
	@echo "Executando testes unitários"
	mvn test

integration-test:
	@echo "Executando testes integrados"
	mvn test -P integration-test

test: unit-test integration-test

start-app:
	@echo "Iniciando a aplicação"
	mvn spring-boot:start

package:
	@echo "Criando pacote para deploy"
	mvn package