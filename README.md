<div align="center">

# 🎯 TaskQ — Back-End

**API REST para gestão de usuários, cargos, projetos e tarefas — com gamificação por XP e consulta de feriados nacionais.**

<br/>

![Java](https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.1.1-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)
![Flyway](https://img.shields.io/badge/Flyway-CC0200?style=for-the-badge&logo=flyway&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)
![Swagger](https://img.shields.io/badge/OpenAPI-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)

![Coverage](https://img.shields.io/badge/cobertura_JaCoCo-98%25-brightgreen?style=flat-square)
![Tests](https://img.shields.io/badge/testes-unit%20%2B%20integra%C3%A7%C3%A3o-blue?style=flat-square)
![Status](https://img.shields.io/badge/status-em%20desenvolvimento-yellow?style=flat-square)

[Sobre](#-sobre-o-projeto) •
[Funcionalidades](#-funcionalidades) •
[Tecnologias](#-tecnologias) •
[Arquitetura](#-arquitetura) •
[Como rodar](#-como-executar) •
[Endpoints](#-endpoints) •
[Testes](#-testes) •
[Roadmap](#-roadmap)

</div>

---

## 📖 Sobre o projeto

O **TaskQ** é o back-end de uma plataforma de gerenciamento de tarefas com um toque de **gamificação**: cada tarefa concluída rende **XP** ao responsável, de acordo com a prioridade. Usuários pertencem a **cargos**, criam **projetos** e atribuem **tarefas**, que têm prazo, prioridade e status — e são marcadas automaticamente como atrasadas quando o prazo vence.

O projeto foi desenvolvido na **Uniamerica** com foco em boas práticas: arquitetura em camadas, DTOs imutáveis (`record`), validação, migrations versionadas, tratamento global de erros e uma bateria de testes automatizados.

---

## ✨ Funcionalidades

| | Recurso | Descrição |
|---|---|---|
| 👤 | **Usuários** | Cadastro, listagem, atualização, troca de senha (BCrypt), inativação lógica e reativação |
| 🏷️ | **Cargos** | CRUD completo, com nome único e bloqueio de exclusão se houver usuários vinculados |
| 📁 | **Projetos** | CRUD, validação de prazo, ações de **iniciar** e **concluir** |
| ✅ | **Tarefas** | CRUD, filtros por usuário e por projeto, ações de **iniciar** e **concluir** |
| 🏆 | **XP / Gamificação** | Conclusão de tarefa gera XP e registra uma transação auditável (sem duplicar) |
| ⏰ | **Atraso automático** | Rotina diária (meia-noite) e na inicialização marca tarefas e projetos vencidos como `ATRASADA` |
| 🎉 | **Feriados** | Consulta de feriados nacionais via **OpenFeign** com timeouts configurados |
| 🧾 | **Auditoria** | Datas de criação/atualização preenchidas automaticamente via JPA Auditing |
| 🚨 | **Erros padronizados** | `@RestControllerAdvice` devolve um corpo de erro consistente em toda a API |
| 📚 | **Documentação** | Swagger UI e OpenAPI gerados automaticamente |

### 🏆 Sistema de XP

Ao concluir uma tarefa, o responsável (`assignee`) recebe XP conforme a prioridade:

| Prioridade | XP concedido |
|:---:|:---:|
| 🟢 `BAIXA` | **10** |
| 🟡 `MEDIA` | **20** |
| 🔴 `ALTA` | **30** |

> 🔒 **Consistência garantida:** o XP é somado diretamente no banco (`incrementXp`) com *lock* pessimista na tarefa, evitando perda de pontos em conclusões simultâneas. Cada tarefa só premia **uma vez** — a transação é registrada em `tb_xp_transaction` com `reference_type = 'TASK'`.

---

## 🛠️ Tecnologias

| Categoria | Tecnologia |
|---|---|
| **Linguagem** | Java 17 |
| **Framework** | Spring Boot 4.1.1 (Web MVC, Data JPA, Validation) |
| **Banco de dados** | PostgreSQL (produção) • H2 em memória (testes) |
| **Migrations** | Flyway |
| **Mapeamento** | MapStruct 1.6.3 + Lombok |
| **Integração HTTP** | Spring Cloud OpenFeign (2025.1.3) |
| **Documentação** | springdoc-openapi 3.1.0 (Swagger UI) |
| **Testes** | JUnit 5, Spring Boot Test, MockMvc |
| **Cobertura** | JaCoCo 0.8.14 |
| **Build** | Maven Wrapper (`mvnw`) |

---

## 🏗️ Arquitetura

O projeto segue o padrão em camadas **Controller → Service → Repository**, com DTOs separando o contrato da API do modelo persistido.

```
src/main/java/uniamerica/tasksq_back_end
├── 📂 controller     → Endpoints REST (entrada HTTP)
├── 📂 service        → Regras de negócio (User, Role, Project, Task, Xp, Holiday)
├── 📂 repository     → Acesso a dados com Spring Data JPA
├── 📂 entity         → Entidades JPA e enums (status, prioridade, elo)
├── 📂 dto
│   ├── request       → Dados recebidos (records + Bean Validation)
│   ├── response      → Dados devolvidos (sem expor a senha)
│   └── mapper        → Conversões com MapStruct
├── 📂 audit          → AuditableEntity (datas e autoria)
├── 📂 config         → Configuração do BCrypt
├── 📂 exception      → GlobalExceptionHandler
└── 📂 integration    → Cliente Feign de feriados (HolidayAPI)
```


### 🔖 Enums

| Enum | Valores |
|---|---|
| `TaskStatus` (também usado em projetos) | `PENDENTE` · `ANDAMENTO` · `CONCLUIDO` · `ATRASADA` |
| `TaskPriority` | `BAIXA` · `MEDIA` · `ALTA` |
| `UserStatus` | `ATIVO` · `INATIVO` |
| `UserRank` (elo) | `INICIANTE` · `JUNIOR` · `PLENO` · `ESPECIALISTA` · `LEGACY` |

---

## 🚀 Como executar

### Pré-requisitos

- ☕ **JDK 17**
- 🐘 **PostgreSQL** em execução
- (Opcional) Postman para testar a API

### 1️⃣ Clone o repositório

```bash
git clone https://github.com/IgorNassa/TaskQ-Back-End.git
cd TaskQ-Back-End
```

### 2️⃣ Crie o banco de dados

```sql
CREATE DATABASE tasksq;
```

### 3️⃣ Configure a conexão

Edite `src/main/resources/application.yaml` com os dados do seu PostgreSQL:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/tasksq
    username: seu_usuario
    password: sua_senha
    driver-class-name: org.postgresql.Driver

  flyway:
    enabled: true
    locations: classpath:db/migration
```

> 💡 O **Flyway** cria todas as tabelas automaticamente na primeira execução (`V1__create_table.sql`).
> ⚠️ Evite versionar credenciais reais — prefira variáveis de ambiente como `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME` e `SPRING_DATASOURCE_PASSWORD`, que sobrescrevem o arquivo.

### 4️⃣ Rode a aplicação

<table>
<tr>
<td><b>🐧 Linux / 🍎 macOS</b></td>
<td><b>🪟 Windows (PowerShell)</b></td>
</tr>
<tr>
<td>

```bash
./mvnw spring-boot:run
```

</td>
<td>

```powershell
.\mvnw.cmd spring-boot:run
```

</td>
</tr>
</table>

A API sobe em **`http://localhost:8080`** 🎉

### 🔗 Links úteis

| Recurso | URL |
|---|---|
| 📚 Swagger UI | http://localhost:8080/swagger-ui.html |
| 📄 OpenAPI (JSON) | http://localhost:8080/v3/api-docs |
| 📮 Coleção Postman | [`postman/TaskQ.postman_collection.json`](postman/TaskQ.postman_collection.json) |
| 🌍 Ambiente Postman | [`postman/TaskQ.postman_environment.json`](postman/TaskQ.postman_environment.json) |

---

## 📡 Endpoints

> Todos os controllers aceitam CORS de qualquer origem (`@CrossOrigin("*")`).

### 👤 Usuários — `/api/usuarios`

| Método | Rota | Descrição |
|:---:|---|---|
| `POST` | `/api/usuarios` | Cadastrar usuário |
| `GET` | `/api/usuarios` | Listar usuários |
| `GET` | `/api/usuarios/{id}` | Buscar usuário por ID |
| `PUT` | `/api/usuarios/{id}` | Atualizar usuário (sem senha) |
| `PATCH` | `/api/usuarios/{id}/senha` | Alterar senha |
| `PATCH` | `/api/usuarios/{id}/ativar` | Reativar usuário |
| `DELETE` | `/api/usuarios/{id}` | Inativar usuário (mantém o registro) |

### 🏷️ Cargos — `/api/cargos`

| Método | Rota | Descrição |
|:---:|---|---|
| `POST` | `/api/cargos` | Cadastrar cargo |
| `GET` | `/api/cargos` | Listar cargos |
| `GET` | `/api/cargos/{id}` | Buscar cargo por ID |
| `PUT` | `/api/cargos/{id}` | Atualizar cargo |
| `DELETE` | `/api/cargos/{id}` | Excluir cargo (sem usuários vinculados) |

### 📁 Projetos — `/api/project`

| Método | Rota | Descrição |
|:---:|---|---|
| `POST` | `/api/project/create` | Criar projeto |
| `GET` | `/api/project/all` | Listar projetos |
| `GET` | `/api/project/{id}` | Buscar projeto por ID |
| `PUT` | `/api/project/update/{id}` | Atualizar projeto |
| `PUT` | `/api/project/start/{id}` | Iniciar projeto (`ANDAMENTO`) |
| `PUT` | `/api/project/completed/{id}` | Concluir projeto (`CONCLUIDO`) |
| `DELETE` | `/api/project/delete/{id}` | Excluir projeto (sem tarefas vinculadas) |

### ✅ Tarefas — `/api/task`

| Método | Rota | Descrição |
|:---:|---|---|
| `POST` | `/api/task/create` | Criar tarefa |
| `GET` | `/api/task/all` | Listar tarefas |
| `GET` | `/api/task/{id}` | Buscar tarefa por ID |
| `GET` | `/api/task/user/{id}` | Tarefas de um usuário |
| `GET` | `/api/task/project/{id}` | Tarefas de um projeto |
| `PUT` | `/api/task/update` | Atualizar tarefa (ID no corpo) |
| `PUT` | `/api/task/start/{id}` | Iniciar tarefa (`ANDAMENTO`) |
| `PUT` | `/api/task/completed/{id}` | Concluir tarefa e conceder XP 🏆 |
| `DELETE` | `/api/task/delete/{id}` | Excluir tarefa |

### 🎉 Feriados — `/api/feriados`

| Método | Rota | Descrição |
|:---:|---|---|
| `GET` | `/api/feriados` | Lista feriados nacionais (`data`, `title`, `description`) |

---

## 🧪 Exemplos de requisição

<details>
<summary><b>🏷️ Cadastrar cargo</b></summary>

```http
POST /api/cargos
Content-Type: application/json
```

```json
{
  "nome": "Desenvolvedor"
}
```

</details>

<details>
<summary><b>👤 Cadastrar usuário</b></summary>

```http
POST /api/usuarios
Content-Type: application/json
```

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

**Resposta `201 Created`**

```json
{
  "id": 1,
  "nome": "Igor Nassa",
  "email": "igor@exemplo.com",
  "urlAvatar": null,
  "cargo": { "id": 1, "nome": "Desenvolvedor" },
  "status": "ATIVO",
  "xp": 0,
  "elo": "INICIANTE",
  "createdAt": "2026-10-25T16:00:00",
  "updatedAt": "2026-10-25T16:00:00"
}
```

</details>

<details>
<summary><b>🔑 Alterar senha</b></summary>

```http
PATCH /api/usuarios/1/senha
Content-Type: application/json
```

```json
{
  "senha": "NovaSenha123"
}
```

</details>

<details>
<summary><b>📁 Criar projeto</b></summary>

```http
POST /api/project/create
Content-Type: application/json
```

```json
{
  "name": "Lançamento do App",
  "description": "Entrega da primeira versão do aplicativo",
  "status": "PENDENTE",
  "priority": "ALTA",
  "startDate": "2026-11-01",
  "deadLine": "2026-12-15",
  "ownerId": 1
}
```

</details>

<details>
<summary><b>✅ Criar tarefa</b></summary>

```http
POST /api/task/create
Content-Type: application/json
```

```json
{
  "title": "Implementar login",
  "description": "Tela e endpoint de autenticação",
  "status": "PENDENTE",
  "priority": "MEDIA",
  "assigneeId": 1,
  "deadLine": "2026-11-20",
  "projectId": 1,
  "creatorId": 1
}
```

> `creatorId` é opcional: se omitido, o criador será o dono do projeto.

</details>

<details>
<summary><b>🎉 Listar feriados</b></summary>

```http
GET /api/feriados
```

```json
[
  {
    "data": "01-01",
    "title": "Ano Novo",
    "description": "Início do ano."
  }
]
```

A fonte é [rodriguesfas.github.io/holidays](https://rodriguesfas.github.io/holidays/national.json). Para trocar a origem, defina a propriedade `integracao.feriados.url` (variável `INTEGRACAO_FERIADOS_URL`). Timeouts: **5 s** para conectar e **10 s** para leitura. Em caso de falha externa, a API responde **`502`**.

</details>

---

## ⚖️ Regras de negócio

- 📧 **E-mail** e **nome do cargo** são únicos (sem diferenciar maiúsculas/minúsculas); na atualização é permitido manter o próprio valor.
- 🆕 Novos usuários começam com **XP 0** e elo **`INICIANTE`**.
- 🔐 Senhas têm de **8 a 72 caracteres** e são armazenadas com **BCrypt** — nunca retornam nas respostas.
- 🗑️ `DELETE` de usuário faz **inativação lógica**; o registro é preservado.
- 🚫 Um cargo com usuários vinculados (mesmo inativos) **não pode ser excluído**.
- 🚫 Um projeto com tarefas vinculadas **não pode ser excluído**.
- 📅 O prazo do projeto (`deadLine`) **não pode ser anterior** ao início (`startDate`).
- ⏰ Tarefas e projetos com prazo vencido e não concluídos viram **`ATRASADA`** automaticamente.
- 🏆 Cada tarefa concede XP **uma única vez**.

### 🚨 Formato de erro

Todos os erros seguem o mesmo contrato:

```json
{
  "timestamp": "2026-10-25T16:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Dados inválidos",
  "path": "/api/usuarios",
  "fieldErrors": {
    "email": "deve ser um endereço de e-mail bem formado"
  }
}
```

| Código | Quando acontece |
|:---:|---|
| `400` | Dados inválidos, parâmetro ou corpo malformado |
| `404` | Registro ou rota inexistente |
| `409` | Duplicidade, violação de integridade ou exclusão bloqueada |
| `422` | Cargo com usuários vinculados |
| `502` | Falha na consulta externa de feriados |
| `500` | Erro interno inesperado |

---

## ✅ Testes

O projeto possui testes **unitários** (DTOs, services, controllers) e de **integração** (usuários, cargos, projetos e tarefas). Os testes usam **H2 em memória** (modo PostgreSQL) com o Flyway desativado, e a API de feriados é simulada localmente — nenhum teste depende da internet.

```bash
# Executar todos os testes
./mvnw clean test

# Testes + relatório de cobertura JaCoCo
./mvnw clean test jacoco:report

# Verificação completa
./mvnw verify
```

📊 O relatório de cobertura é gerado em `target/site/jacoco/index.html`.

```
src/test/java/uniamerica/tasksq_back_end
├── controller    → RoleControllerTest, UserControllerTest, TaskControllerTest, ...
├── service       → UserServiceTest, XpServiceTest, TaskServiceTest, ...
├── dto           → testes de request e response
└── integration   → fluxos completos com banco em memória
```

---

<div align="center">

Feito com ☕ e Spring Boot

</div>