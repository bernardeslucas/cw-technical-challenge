# Desafio Técnico CWI - Java Back-End
A etapa posterior ao CWI - Reset resultou no desafio técnico proposto abaixo. 

Para maiores informações sobre o programa, acesse [cwi.com.br](https://cwi.com.br/).

## Objetivo
 >No cooperativismo, cada associado possui um voto e as decisões são tomadas em assembleias, por votação. A partir disso, você precisa criar uma solução back-end para gerenciar essas sessões de votação. Essa solução deve ser executada na nuvem e promover as seguintes funcionalidades através de uma API REST:
>- Cadastrar uma nova pauta; 
>- Abrir uma sessão de votação em uma pauta (a sessão de votação deve ficar aberta por um tempo determinado na chamada de abertura ou 1 minuto por default);
>- Receber votos dos associados em pautas (os votos são apenas 'Sim'/'Não'. Cada associado é identificado por um id único e pode votar apenas uma vez por pauta);
>- Contabilizar os votos e dar o resultado da votação na pauta.
 
## Execução do projeto 
A última versão commitada encontra-se hospeada no heroku, [aqui](https://cwi-pauta.herokuapp.com/swagger-ui.html). Ao entrar no site, caso a aplicação esteja dormindo (30 minutos de inatividade), irá demorar um pouquinho para acordar, após isso, os testes podem ser feitos pelo Swagger UI.

A aplicação também pode ser testada localmente (localmente a aplicação se conecta ao mesmo banco de dados e no mesmo server do RabbitMQ da versão hospedada no heroku) diretamente pelo .jar:

```java -jar ./target/technical-challenge-0.0.1-SNAPSHOT.jar```

Então acesse [localhost](https://localhost:8080/swagger-ui.html).

## Decisões durante o desenvolvimento
#### Java + IntelliJ 
```
Linguagem requerida no desafio e ensinada no programa CWI Reset que resultou nesse desafio técnico. 
IDE também apresentada no programa e compatível com o desenvolvimento requerido. 
```
Para mais informações sobre o programa, acesse [cwi.com.br](https://cwi.com.br/).
#### [Spring](https://spring.io/Spring) 

```
Framework apresentado no CWI Reset, que é amplamente utilizado pela comunidade, pelo fato de facilitar
muito o desenvolvimento, devido a inúmera quantidade de coisas que são automatizadas por ele, principalmente
para o desenvolvimento de APIs via Spring Web Rest + Hibernate JPA, algumas das dependências utilizadas nesse desafio.
```

#### [Heroku](https://www.heroku.com/)
```
Heroku é uma plataforma cloud amplamente utilizada tanto pelas suas funcionalidades como pela sua facilidade
de uso, ajudando no desenvolvimento e no deploy da aplicação devido a sua integração com o GitHub.
```
#### [PostgreSQL](https://www.postgresql.org/)
```
Banco de dados que eu havia tido contato após o CWI Reset e também o banco de dados no qual o Heroku possui 
integração, fácil configuração, tanto online quanto offline.
```
#### Manipulação de erros + log
```
Com o auxílio do Spring, foi organizado as categorias de erro em Classes enum para melhor organização
e escalonamento. Os logs foram feitos com o @Slf4j devido a fácil implementação.
```
#### [RabbitMQ](https://www.rabbitmq.com/) e [Quartz](http://www.quartz-scheduler.org/)
```
Para a mensageria do resultado da votação foram utilizadas as duas ferramentas acima. Ao iniciar a 
sessão, é agendado um Job no Quartz com o EndDate da sessão de votação, ao ser disparado, ele faz 
o publishing do resultado, enquanto que na medida em que a aplicação está ligada, há um receiver (pseudo plataforma)
que lê essa mensagem na fila e faz o logging dela. 
```
#### [Sonarqube](https://www.sonarqube.org/)
```
Como ferramenta de qualidade, o projeto foi inspecionado pela ferramenta acima, onde nos mostrou 
alguns imports esquecido e não utilizados, nomenclaturas fora do padrão, dentre outros pequenos 
erros que foram corrigidos posteriormente a análise.
```
#### Versionamento API
```
Foi utilizado o sistema de versionamento por URL, a aplicação hoje roda em .v1/, pensando em futuras
versões, seria feito outra layer de controller/service mudando o path e reaproveitando as demais layers
da aplicação.
```
#### [Swagger](https://swagger.io/) - Documentação
```
Foi utilizado o Swagger devido a sua fácil configuração e implementação na aplicação.
```
