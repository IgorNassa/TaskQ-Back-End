# TaskQ — Back-end

Projeto Spring Boot com Java 17 para gerenciar usuários e cargos.

## Onde fica cada coisa

- `controller`: recebe as requisições HTTP e chama o serviço.
- `service`: verifica as regras, preenche as entidades e salva os dados.
- `repository`: consulta o banco com Spring Data JPA.
- `entity`: representa os dados persistidos.
- `dto/request`: define os campos recebidos.
- `dto/response`: define os campos devolvidos, sem expor a senha.
- `dto/mappers`: usa MapStruct para converter entidades em respostas.
- `audit`: registra datas e campos de autoria.
- `config`: configura o BCrypt para proteger as senhas.

Comece por `UserController`, depois leia `UserService` e `UserRepository`.
As exceções são lançadas diretamente nos serviços com `ResponseStatusException`.

## Rotas

| Método | Rota | Ação |
| --- | --- | --- |
| POST | /api/usuarios | Cadastrar usuário |
| GET | /api/usuarios | Listar usuários |
| GET | /api/usuarios/{id} | Buscar usuário |
| PUT | /api/usuarios/{id} | Atualizar usuário |
| PATCH | /api/usuarios/{id}/senha | Alterar senha |
| PATCH | /api/usuarios/{id}/ativar | Ativar usuário |
| DELETE | /api/usuarios/{id} | Inativar usuário, mantendo o registro |
| POST | /api/cargos | Cadastrar cargo |
| GET | /api/cargos | Listar cargos |
| GET | /api/cargos/{id} | Buscar cargo |
| PUT | /api/cargos/{id} | Atualizar cargo |
| DELETE | /api/cargos/{id} | Excluir cargo sem usuários vinculados |

Cadastro de cargo:

```json
{"nome": "Desenvolvedor"}
```

Cadastro de usuário (use o ID de um cargo existente):

```json
{
  "nome": "Igor Nassa",
  "email": "igor@exemplo.com",
  "senha": "MinhaSenha123",
  "cargoId": 1,
  "status": "ATIVO",
  "urlAvatar": null
}
```

Na atualização, envie os mesmos campos, exceto a senha.
Para alterar a senha, use a rota específica com `{"senha":"NovaSenha123"}`.
As respostas usam `nome`, `urlAvatar`, `criadoEm` e `atualizadoEm`.

## Regras e erros

- E-mail e nome do cargo não podem se repetir, desconsiderando maiúsculas e minúsculas.
- A atualização permite manter o próprio e-mail ou nome de cargo.
- Novos usuários começam com XP zero e elo INICIANTE.
- Um cargo com usuários vinculados não pode ser excluído, mesmo que estejam inativos.
- Os erros usam a resposta padrão Problem Details do Spring; a mensagem do serviço fica em `detail`.
- 400: dados inválidos; 404: registro inexistente; 409: duplicidade verificada pelo serviço; 422: cargo com usuários vinculados.
- Autenticação e permissões de acesso ainda precisam ser implementadas. A configuração de senha fornece apenas o BCrypt.
- As datas de auditoria são preenchidas automaticamente. A autoria depende da futura identificação do usuário autenticado.

## Executar e testar

Configure o JDK 17 e a conexão PostgreSQL pelas variáveis
`SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME` e `SPRING_DATASOURCE_PASSWORD`.
A criação e evolução do esquema continuam sob responsabilidade das migrações do projeto.

No PowerShell:

```powershell
.\mvnw.cmd spring-boot:run
.\mvnw.cmd clean test
```

Com a aplicação rodando, a documentação está em [Swagger UI](http://localhost:8080/swagger-ui.html).

Os testes usam H2 em memória com Flyway desativado.

## Mudança dos nomes

A API agora usa `/api/usuarios` no lugar de `/api/users`, `/senha` no lugar de
`/password` e `/ativar` no lugar de `/activate`. O front-end deve usar os novos
nomes de rotas e campos. Os nomes antigos não são aliases.

Os nomes existentes de tabelas e colunas foram preservados nas anotações JPA.
Nenhuma migração Flyway foi criada. O valor de elo `LEGACY` foi preservado para
manter compatibilidade com dados existentes.

Nomes como `findById`, `save`, `existsBy`, `IgnoreCase` e `AndIdNot` fazem parte
das convenções do Spring Data. As anotações e tipos do Java, Spring e MapStruct
também mantêm os nomes originais.
## Feriados

`GET /api/feriados` consulta a [fonte de feriados](https://rodriguesfas.github.io/holidays/national.json)
com o cliente Feign `HolidayAPI` e retorna uma lista com apenas `data`, `title` e `description`.
O campo `date` recebido da fonte vira `data` na resposta. Os outros campos são ignorados.
A data permanece como texto no formato dia-mês, sem recalcular feriados móveis por ano.

Exemplo do formato de resposta:

```json
[
  {
    "data": "01-01",
    "title": "Ano Novo",
    "description": "Início do ano."
  }
]
```

O serviço retorna HTTP 502 se a consulta externa falhar. Os limites configurados são
5 segundos para conectar e 10 segundos para leitura. Para trocar a origem, defina
`INTEGRACAO_FERIADOS_URL` com a URL base; o caminho consultado é `/holidays/national.json`.
Os testes de feriados usam uma API local simulada, sem depender da internet.
# TaskQ Back-end

API REST em Spring Boot para gestão de usuários, cargos, projetos e tarefas, com recompensa de XP e consulta externa de feriados.

## Executar

Requisitos: Java 17 e PostgreSQL. Defina `DB_PASSWORD`; `DB_URL` e `DB_USERNAME` são opcionais e possuem os valores do Supabase como padrão.

```powershell
$env:DB_PASSWORD="sua-senha"
./mvnw.cmd spring-boot:run
```

O Flyway aplica as migrations automaticamente. A aplicação valida o schema ao iniciar.

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI: `http://localhost:8080/v3/api-docs`
- Coleção Postman: `postman/TaskQ.postman_collection.json`
- Ambiente Postman: `postman/TaskQ.postman_environment.json`

## Validação

```powershell
./mvnw.cmd verify
```

O projeto usa Controller, Service, Repository, DTOs, MapStruct, Bean Validation, JPA, Flyway, OpenFeign e tratamento global de erros.
