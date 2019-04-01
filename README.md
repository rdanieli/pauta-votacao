# Projeto pautas para votação a título de Prova de Conceito(PoC) com o Framework Quarkus and a GraalVM supersônica.

### Apresentação:
#### Objetivo
Crie uma solução de backend para ser executada em nuvem que proveja as seguintes funcionalidades através de serviços REST:

 - Cadastrar uma nova pauta
 - Abrir uma sessão de votação em uma pauta (a sessão de votação deve ficar aberta por um tempo determinado na chamada de abertura ou 1 minuto por default)
 - Receber votos dos associados em pautas (os votos são apenas 'Sim'/'Não'. Cada associado é identificado por um id único e pode votar apenas uma vez por pauta)
 - Contabilizar os votos e dar o resultado da votação na pauta

### Decisões técnicas
Neste projeto foi considerado a criação de 3 entidades principais do dominio de negócio da solução:

  - Pauta
  - Sessão de Votação
  - Voto

Atualmente a solução preve uma Sessão de Votação por pauta. No entanto o fato de ter uma entidade intermediária Sessão de Votação, pode flexibilizar a solução no longo prazo.

O Resultado da votação pode ser parcial ou definitivo, o que permite ao usuário ao consultar uma pauta e já obter a quantidade de respostas SIM/NAO escolhidas.

### Ferramentas

- Quarkus: é um framework Kubernetes Native Java feito sob medida para GraalVM e HotSpot, criado a partir das melhores bibliotecas e padrões Java. O objetivo do Quarkus é tornar o Java uma plataforma líder em Kubernetes e ambientes Serverless, ao mesmo tempo em que oferece aos desenvolvedores um modelo unificado de programação reativa e imperativa para abordar de maneira ideal uma gama mais ampla de arquiteturas de aplicativos distribuídos.

Container First - Aplicações Java minimalistas para execução em contêineres eficientes proporcionando:

Inicialização rápida (milissegundos) (cold-start, AWS e Heroku) permite escalonamento automático de microsserviços em contêineres e Kubernetes, bem como execução no local FaaS, isto se dá devido a baixa utilização de memória que ajuda baixar a densidade do contêiner nas implantações de arquitetura de microsserviços os quais exigem vários contêineres.

Small App - Cada imagem nativa gerada é cerca de 89% menor que um Jar comum.

Unifica imperativo e reativo - Projetado para unir os dois modelos na mesma plataforma, resultando em uma forte alavancagem dentro de uma organização.

Baseado em padrões - Aproveitando as melhores bibliotecas tais como o Eclipse MicroProfile, o JPA / Hibernate, o JAX-RS / RESTEasy, o Eclipse Vert.x, o Netty e muito mais. O Quarkus também inclui uma estrutura de extensão que os autores de framework de terceiros podem aproveitar para estendê-lo. A estrutura de extensão do Quarkus reduz a complexidade para executar estruturas de terceiros no Quarkus e compilar para um binário nativo do GraalVM.

Microservice first - Traz um tempo de inicialização rápido e o código para aplicativos Java.

Tendo em vista todos esses benefícios achei interessante trazer o assunto e já criar uma Prova Conceitual para saber se é realmente um framework promissor.

### Executando o projeto
O projeto utiliza como banco o PostgreSQL e é preciso ter uma instância em execução para o projeto funcionar, a qual já é fornececida neste teste através de um Docker-compose.

- Subindo a aplicação com docker-compose :  
  ``docker-compose -f src/main/docker/docker-compose.yml up --build -d``    

Após acessar [localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

Documentação foi feita utilizando o Swagger: [swagger](http://localhost:8080/swagger-ui.html#/)

### Resultados

- Ao containerizar a aplicação os resultados são surpreendentes, o boot time da aplicação ficou em torno de 100 milisegundos
para uma aplicação com JPA, Rest e Hibernate.

- O desenvolvimento é mais rápido visto que é inicializado o maven em modo dev e só para o servidor quando o usuário quiser, alterações em nomes de método não são mais um problema.

- O framework traz algumas caracteristicas como o Hibernate Panache que tem uma visão diferente na manipulação das entidades de negócio, o que algumas literaturas citam como Objetos Ricos.

- O desenvolvimento foi rápido, porém o framework ainda não está preparado para um ambiente corporativo, precisa atingir maior maturidade frente aos concorrentes como spring.

- Ainda muitas integrações não estão disponíveis, a própria integração com o Swagger não é viabilizada ainda, mas acredito que logo vai estar disponível.

### Limitações

Ao enviar um request para o /v1/pautas/iniciar-sessao-votacao com uma data especifica, a data em questão não deve ter a letra 'Z' ao final. 

