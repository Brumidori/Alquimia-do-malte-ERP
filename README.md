# Alquimia do Malte - ERP (API REST)

Resumo
-------
Projeto backend em Spring Boot que expõe uma API REST para o sistema "Alquimia do Malte" (ERP). Fornece gerenciamento de usuários, produtos, insumos, lotes, produção, vendas, entre outros domínios relacionados ao processo de produção.

Propósito
---------
- Servir como API central para o sistema ERP da Alquimia do Malte.
- Fornecer autenticação via JWT e endpoints REST documentados via OpenAPI/Swagger.

Arquitetura (visão geral)
-------------------------
O projeto segue uma arquitetura em camadas clássica para aplicações Spring Boot:

- Controller (REST endpoints): `src/main/java/.../controller` — expõe os endpoints HTTP.
- Service (regras de negócio): `src/main/java/.../service` — contém lógica de aplicação.
- Repository (persistência): `src/main/java/.../repository` — interfaces JPA para acesso a dados.
- Domain / Entity: `src/main/java/.../domain/entity` — modelos JPA/entidades.
- DTOs: `src/main/java/.../dto` — objetos de transferência (request/response).
- Security: `src/main/java/.../security` — configurações de segurança e JWT.
- Config: `src/main/java/.../config` — configurações (ex.: OpenAPI, Security)

Principais dependências
-----------------------
- Spring Boot Web MVC
- Spring Data JPA
- Spring Security + JWT (jjwt)
- PostgreSQL (driver)
- SpringDoc OpenAPI (Swagger UI)
- H2 (apenas para testes)

Requisitos para rodar localmente
-------------------------------
- Java 21 (o `pom.xml` define `<java.version>21</java.version>`)
- Git (para clonar)
- O projeto inclui o Maven Wrapper (`mvnw`) — não é necessário ter Maven instalado globalmente.

Executando a aplicação
----------------------
1) Torne o Maven wrapper executável (uma vez):

```bash
chmod +x ./mvnw
```

2) Rodar com as configurações padrão (usa as propriedades em `src/main/resources/application.properties`):

```bash
./mvnw spring-boot:run
```

Observação: por padrão `application.properties` pode estar configurado para conectar a um banco PostgreSQL (ex.: Supabase). Se preferir subir a aplicação sem depender do banco remoto, rode usando um banco H2 em memória (útil para desenvolvimento rápido):

```bash
./mvnw spring-boot:run -Dspring-boot.run.arguments="--spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=false;MODE=PostgreSQL --spring.datasource.driver-class-name=org.h2.Driver --spring.datasource.username=sa --spring.datasource.password= --spring.jpa.hibernate.ddl-auto=create-drop"
```

Conexão com Supabase / PostgreSQL
---------------------------------
Se quiser usar a instância Supabase fornecida, defina as variáveis de ambiente (recomendado) ou configure em `application.properties` **mas não deixe segredos no repositório**.

Exemplo de variáveis de ambiente (bash/zsh):

```bash
export DB_HOST=db.jzjskmljmosecjnqtagb.supabase.co
export DB_PORT=5432
export DB_NAME=postgres
export DB_USER=postgres
export DB_PASSWORD=YKU7ocwvpGNnFHwi
# JDBC URL com SSL (supabase requer SSL)
export JDBC_DATABASE_URL="jdbc:postgresql://$DB_HOST:$DB_PORT/$DB_NAME?sslmode=require"

# Rodar a aplicação usando as variáveis
./mvnw spring-boot:run -Dspring-boot.run.arguments="--spring.datasource.url=$JDBC_DATABASE_URL --spring.datasource.username=$DB_USER --spring.datasource.password=$DB_PASSWORD"
```

Swagger / OpenAPI (como abrir)
------------------------------
Após iniciar a aplicação localmente, a documentação interativa do OpenAPI (Swagger UI) estará disponível normalmente em uma das URLs abaixo:

- http://localhost:8080/swagger-ui/index.html
- http://localhost:8080/swagger-ui.html
- JSON OpenAPI: http://localhost:8080/v3/api-docs

Se a UI não carregar, tente:

```bash
open "http://localhost:8080/swagger-ui/index.html?url=/v3/api-docs"
curl -sS http://localhost:8080/v3/api-docs | jq .
```

Rodando testes
--------------
Os testes usam H2 em memória (configuração em `src/test/resources/application.properties`). Para executar todos os testes:

```bash
./mvnw test
```

Boas práticas / Git
-------------------
- Mantenha o Maven Wrapper (`mvnw`, `mvnw.cmd` e `.mvn/wrapper/*`) no repositório — ele garante builds reprodutíveis.
- Não comite segredos (senhas, tokens) em `application.properties`. Use variáveis de ambiente e adicione um `src/main/resources/application.properties.example` com placeholders.
- O `.gitignore` está configurado para ignorar `target/`, arquivos de IDE, `.env` e o `application.properties` com segredos.

Contribuindo
------------
- Abra uma issue descrevendo o que pretende implementar.
- Faça um fork, crie um branch com um nome descritivo, desenvolva e envie um Pull Request com descrição clara das mudanças.

Contato
-------
Para dúvidas sobre a execução local, testes ou deploy, adicione uma issue no repositório ou envie mensagem para o mantenedor do projeto.

