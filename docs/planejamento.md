# Planejamento - Biblioteca Virtual API

API REST para uma biblioteca pública virtual. A ideia é cadastrar livros e deixar fácil para consultarem.

Integrante: João Vitor (GitHub: @jvitorserrao)

Stack: Java 17, Spring Boot 4.1.1, PostgreSQL, Flyway, Lombok, Swagger (springdoc).

## Requisitos

Funcionais:

- RF01 - CRUD de autores
- RF02 - CRUD de editoras
- RF03 - CRUD de categorias
- RF04 - CRUD de livros
- RF05 - CRUD de materiais digitais (apostilas, vídeos, PDFs, links)
- RF06 - Ao cadastrar um livro, gerar um QR Code com as informações dele e já devolver na resposta
- RF07 - Buscar o QR Code de um livro pelo ID

Não funcionais:

- Dados salvos no PostgreSQL, tabelas criadas por migrations (Flyway)
- Validação dos dados de entrada com mensagens de erro claras
- Códigos HTTP corretos (201, 400, 404, etc.)
- Todas as rotas documentadas no Swagger
- Projeto organizado em Model, DTO, Repository, Service e Controller

## Entidades

**Autor**
- id, nome, nacionalidade, dataNascimento, biografia

**Editora**
- id, nome, cnpj, cidade, site

**Categoria**
- id, nome, descricao, areaConhecimento, ativa

**Livro**
- id, titulo, isbn, anoPublicacao, numeroPaginas, sinopse, linkAcesso, qrCode
- pertence a um Autor, uma Editora e uma Categoria

**MaterialDigital**
- id, titulo, tipo (PDF, VIDEO, LINK, APOSTILA), url, descricao, serie (1º, 2º ou 3º ano)
- pertence a uma Categoria
- pode estar ligado a um Livro

Relacionamentos:

- Autor 1 - N Livro
- Editora 1 - N Livro
- Categoria 1 - N Livro
- Categoria 1 - N MaterialDigital
- Livro 1 - N MaterialDigital

## Funcionalidade extra: QR Code do livro

Quando um livro é cadastrado, o sistema monta um texto com título, autor, ISBN e link de acesso e manda para a API do goqr.me (api.qrserver.com). A API devolve a imagem do QR Code, que é salva no livro em Base64 e retornada junto na resposta do cadastro.

A chamada para a API externa vai ser feita com WebClient.

Rotas:

- `POST /livros` - cadastra e já retorna o QR Code
- `GET /livros/{id}/qrcode` - retorna a imagem PNG do QR Code

Se a API do goqr.me estiver fora do ar, o livro é salvo mesmo assim, sem o QR Code.

## Cronograma

Entrega no Moodle: 06/11.

| Período | O que fazer |
|---|---|
| 21/10 a 24/10 | Planejamento, criação do projeto, configuração do banco e do Swagger |
| 25/10 a 28/10 | Migrations e CRUDs de Autor, Editora e Categoria |
| 29/10 a 01/11 | CRUDs de Livro e MaterialDigital |
| 02/11 a 03/11 | Integração com o goqr.me (QR Code) |
| 04/11 a 05/11 | Validações, tratamento de erros e ajustes no Swagger |
| 06/11 | README, testes finais, ZIP da entrega e preparação da apresentação |

## Responsabilidades

Projeto feito individualmente, então todas as etapas (planejamento, código, banco, documentação e apresentação) são minhas.
