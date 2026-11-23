# Baozi Store API

API REST da atividade prática de Desenvolvimento Web Back-End. A loja cadastra clientes e produtos e registra pedidos de um único produto por vez.

## Executar

Requisitos: JDK 17 ou superior e Maven 3.6.3 ou superior.

```bash
mvn clean package
java -jar target/baozi-store-api-1.0.0.jar
```

A API atende em `http://localhost:8080/api`. O H2 grava os dados em `data/baozi.mv.db`, criado automaticamente. Para uma sessão descartável de testes:

```bash
java -jar target/baozi-store-api-1.0.0.jar --spring.datasource.url=jdbc:h2:mem:baozi --spring.jpa.hibernate.ddl-auto=create-drop
```

## Endpoints

| Operação | Clientes | Produtos | Pedidos | Sucesso |
| --- | --- | --- | --- | --- |
| Criar | `POST /api/clientes` | `POST /api/produtos` | `POST /api/pedidos` | `201 Created` |
| Listar | `GET /api/clientes` | `GET /api/produtos` | `GET /api/pedidos` | `200 OK` |
| Consultar | `GET /api/clientes/{id}` | `GET /api/produtos/{id}` | `GET /api/pedidos/{id}` | `200 OK` |
| Excluir | `DELETE /api/clientes/{id}` | `DELETE /api/produtos/{id}` | `DELETE /api/pedidos/{id}` | `204 No Content` |

Envie os corpos dos POSTs como `application/json`. Cada criação responde com o registro criado e o cabeçalho `Location`. Exemplo dos três corpos, na ordem de cadastro:

```json
{"nome":"Cliente Demonstração","clienteDesde":"2026-11-23"}
```

```json
{"nome":"Baozi de legumes","preco":8.50,"estoque":true}
```

```json
{"clienteId":1,"produtoId":1,"quantidade":3}
```

Use no pedido os IDs retornados pelas duas primeiras requisições. O preço deve ser positivo e aceitar até duas casas decimais. `estoque` indica se o produto está disponível. A quantidade é um inteiro positivo. Os erros retornam JSON com status e detalhes. Um corpo inválido recebe `400`, um registro inexistente recebe `404` e uma operação incompatível com a disponibilidade ou com os vínculos de um pedido recebe `409`. Para remover um cliente ou produto vinculado, exclua seus pedidos antes.

## Postman

Importe [a coleção](postman/Baozi-Store.postman_collection.json) e execute as requisições na sequência numérica. A coleção salva os IDs gerados, verifica as respostas e confirma as exclusões ao final. Ajuste `baseUrl` se a API usar outra porta. A variável `nomeCliente` define o nome enviado no cadastro.

## Estrutura

- `model`: entidades JPA e chaves estrangeiras de Pedido para Cliente e Produto.
- `repository`: persistência com Spring Data JPA.
- `controller`: rotas REST com Spring MVC e tratamento de erros.
- `dto`: validação de entradas e representação JSON de Pedido.

[Diagrama de casos de uso](docs/casos-de-uso.png)
