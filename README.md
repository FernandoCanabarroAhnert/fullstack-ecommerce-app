# Projeto Full-Stack: E-Commerce 🛒

![Java](https://img.shields.io/badge/java-FF5722.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![Hibernate](https://img.shields.io/badge/Hibernate-F57F17?style=for-the-badge&logo=Hibernate&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-003B6F?style=for-the-badge&logo=postgresql&logoColor=white)
![PgAdmin](https://img.shields.io/badge/PgAdmin-316192?style=for-the-badge&logo=postgresql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![SendGrid](https://img.shields.io/badge/SendGrid-00BFFF?style=for-the-badge&logo=maildotru&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring%20Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white)
![GitHub Actions](https://img.shields.io/badge/GitHub%20Actions-181717?style=for-the-badge&logo=github&logoColor=white)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-05a77a?style=for-the-badge&logo=thymeleaf&logoColor=white)
![Flyway](https://img.shields.io/badge/Flyway-CC0202?style=for-the-badge&logo=flyway&logoColor=white)
![HTML](https://img.shields.io/badge/HTML5-E44D26?style=for-the-badge&logo=html5&logoColor=white)
![CSS](https://img.shields.io/badge/CSS3-264DE4?style=for-the-badge&logo=css3&logoColor=white)
![JavaScript](https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black)
![SASS](https://img.shields.io/badge/SASS-CF649A?style=for-the-badge&logo=sass&logoColor=white)
![Bootstrap](https://img.shields.io/badge/Bootstrap-7952B3?style=for-the-badge&logo=bootstrap&logoColor=white)

## Modelo de Domínio
![DiagramaClasses](https://github.com/user-attachments/assets/bb79ee19-937c-4227-acf9-fe344326597d)

## O que é o projeto? 🤔

A aplicação é um projeto MVC (Model, View, Controller), utilizando Spring Boot com Thymeleaf, que tem as seguintes funcionalidades: Cadastro e Login de Usuário com e-mail de confirmação e recuperação de senha. Pesquisa de produtos com diversos filtros, como categorias, marcas, nome e preço, com ordenação (nome e preço) e paginação. Adicionar produtos ao Carrinho e à Lista de Desejos. Editar e remover itens do Carrinho. Compra de produtos com 3 opções de pagamento: Cartão de Crédito, Pix e Boleto, sendo que com a forma de pagamento Boleto selecionada o usuário pode gerar o boleto no formato PDF, que foi criado utilizando o JasperSoftStudio e JasperReports. Após a compra, o usuário receberá um e-mail com um resumo do pedido, também em formato PDF e feito com o JasperReports. O usuário ainda pode alterar seus dados de cadastro (e-mail, nome, cpf, etc), alterar sua senha, adicionar endereços (a API da ViaCep é utilizada nessa parte, bastando o usuário informar somente o seu CEP e o seu número), adicionar cartões de crédito, conferir seus pedidos e resgatar cupons de desconto.

Quanto à parte de administração, há as seguintes funcionalidades: o gerenciamento (CRUD) de produtos (com o upload de imagens), categorias, marcas, usuários, cupons de desconto, e a visualização de pedidos. Todas as interfaces de gerenciamento contém consultar por nome/descrição (depende do que é que está sendo gerenciado), ordenação por campos do objeto e paginação. Todos eles também possuem a opção de exportar os dados em formato Excel, CSV e PDF (todos os PDFs foram feitos utilizando o JasperSoftStudio e o JasperReport). Quanto aos pedidos, ao clicar no botão de gerar PDF, irá aparecer um modal que permite selecionar alguns filtros para gerar o PDF com os pedidos, sendo eles: a data mínima e a máxima, o valor total de pedido mínimo e o máximo, o tipo de pagamento (Boleto, Pix ou Cartão) e por status do pedido. Ainda no relatório PDF de pedidos, na última página, irá conter 3 gráficos, sendo 1 de pizza e outros 2 de barras, com algumas estatísticas, sendo elas: a porcentagem referente a cada um das formas de pagamento, a quantidade de pedidos por mês e o valor total em pedidos por mês.

Quanto às ferramentas e tecnologias, foram utilizadas: Java com Spring Boot, o Banco de Dados PostgreSQL instanciado no RDS (Relational Database Service) na AWS e as Migrations com Flyway, garantindo o versionamento do banco de dados e a facilidade de mudar do ambiente local (Docker) para o da Nuvem (RDS) com os mesmas tabelas e dados. No Front-End, foi utilizado HTML, CSS, SASS e Bootstrap. Além disso, foi utilizado o Thymeleaf, que é um motor de templates para Java, que permite a criação de páginas HTML dinâmicas de forma intuitiva, com suporte a expressões condicionais, loops e integração direta com os objetos do backend, o GitHub Actions para o CI/CD da aplicação e para a automação do Deploy do projeto, que foi realizado em uma instância do EC2 na AWS.

## Tecnologias 💻
 
- [Spring Boot](https://spring.io/projects/spring-boot)
- [Spring MVC](https://docs.spring.io/spring-framework/reference/web/webmvc.html)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [Spring Security](https://spring.io/projects/spring-security)
- [PostgreSQL](https://www.postgresql.org/)
- [PgAdmin](https://www.pgadmin.org/)
- [Docker](https://www.docker.com/)
- [SendGrid](https://sendgrid.com/en-us)
- [GithubActions](https://docs.github.com/pt/actions)
- [Bean Validation](https://docs.spring.io/spring-framework/reference/core/validation/beanvalidation.html)
- [Thymeleaf](https://www.thymeleaf.org/)
- [Flyway](https://www.red-gate.com/products/flyway/community/)
- [JasperReports](https://community.jaspersoft.com/)
- [HTML](https://developer.mozilla.org/pt-BR/docs/Web/HTML)
- [CSS](https://developer.mozilla.org/pt-BR/docs/Web/CSS)
- [JavaScript](https://developer.mozilla.org/pt-BR/docs/Web/JavaScript)
- [SASS](https://sass-lang.com/)
- [Bootstrap](https://getbootstrap.com/)


## Como executar 🎉

1.Clonar repositório git:

```text
git clone https://github.com/FernandoCanabarroAhnert/fullstack-ecommerce-app.git
```

2.Instalar dependências.

```text
mvn clean install
```

3.Executar a aplicação Spring Boot.

### Usando Docker 🐳

- Clonar repositório git
- Construir o projeto:
```
./mvnw clean package
```
- Construir a imagem:
```
./mvnw spring-boot:build-image
```
- Executar o container:
```
docker run --name fullstack-ecommerce-app -p 8080:8080  -d fullstack-ecommerce-app:0.0.1-SNAPSHOT
```



