# Spring Security Authorization Service
### Description
Oauth2 authorization Server.
this program is designed using Java and Spring Boot,
which can be used to register and authenticate **users** and **resource applications** in an advanced way.
This service can be used as register server

#### my website: [https://alirezaalijani.ir](https://alirezaalijani.ir "https://alirezaalijani.ir")
------------

#### Details:
- Oauth service
- Authentication and Authorization **froms**
- Use **fail attempts** to block access
- Use **java email** client to send emails
- Different accesses for users
- Creating a professional and **encrypted link** to verify user actions (**email verification**)
- Create an **html email template** and send **asymmetric** emails
- Use **Google recaptcha**
- **Jwt** for api authentication
- ٍSecuring webservice
- Dockerized application with **docker-compose**

##### Goals
- Authentication & Authorization server
- Account validation
- User management
- Distributed service

# How to use

#### Using maven and docker
###### Project dependencies
- Docker : [Get Started](https://www.docker.com/ "Get Started")
- docker-compose : [Overview of Docker Compose](https://docs.docker.com/compose/ "Overview of Docker Compose")
- maven  : [How to use or Download](https://maven.apache.org/ "How to use or Download")
- java 17
- gmail account or other smtp email account
  if using gmail first do fallow this two-step
  1- [Two-Step Verification should be turned off.](https://support.google.com/accounts/answer/1064203?hl=en "Two Step Verification should be turned off.")
  2- [Allow Less Secure App(should be turned on).](https://myaccount.google.com/lesssecureapps "Allow Less Secure App(should be turned on).")

#### Config
1. Download or clone project
```shell
git clone https://github.com/alirezaalj/Spring-Security-Authorization-Service.git
or 
wget https://github.com/alirezaalj/Spring-Security-Authorization-Service/archive/refs/heads/master.zip
```
2. go to project folder open command line in there
```shell
cd Spring-Security-Authorization-Service
cp config-repository-copy/* config-repository/
cd config-repository 
git init
git add .
git commit -m "Initial commit" 
cd ..
```
3. Change **auth-app-env** file configs
   change email configs in here is use gmail
 ```
SPRING_MAIL_HOST=smtp.gmail.com
SPRING_MAIL_HOST_PORT=587
SPRING_MAIL_USERNAME=<your_gmail_account>
SPRING_MAIL_PASSWORD=<account_password>
```
###### change other configs if you need
> the **recaptcha keys** in
> config file are for the **localhost**  if you are running
> on localhost there is no need to change them. or 
> config recaptcha enable to **false**
```
## application name
APPLICATION_INFO_NAME=Alireza Alijani Auth Service
## application domian
APPLICATION_INFO_HOST=security.alirezaalijani.ir
## contact email
APPLICATION_INFO_CONTACT_EMAIL=contact@alirezaalijani.ir
## encrypting keys
APPLICATION_SECURITY_ENCRYPTION_TOKEN_SECRET_KEY=tokenKey
APPLICATION_SECURITY_ENCRYPTION_TOKEN_SALT=5c0744940b5c369b
## token validation url - only domain can be changed
APPLICATION_SECURITY_LOGIN_VALIDATOR_VALIDATE_URL=http://localhost:9000/verification/{path}/{token}
## some client application redirect after login with token 
APPLICATION_SECURITY_LOGIN_SUCCESS_REDIRECT_URL=http://localhost:4200/validate/{token}
## google recaptch configs
GOOGLE_RECAPTCHA_ENABLE=true
GOOGLE_RECAPTCHA_KEY_SITE=<your domain recaptcha site>
GOOGLE_RECAPTCHA_KEY_SECRET=<your domain recaptcha key>
```
4. In application folder build **jar** file and **docker image**
```shell
mvn clean install -DskipTests
```
5. Run docker compose
```shell
cd docker-compose
# without ssl
docker-compose --env-file auth-app-env up

# with ssl
docker-compose --env-file auth-app-env-ssl up
```
##### application is ready on http://localhost:9000/
>Use postman for Oauth client authorization: **pkce, authorization_code**
> ### **postman public workspace** 
>https://www.postman.com/warped-station-341723/workspace/spring-auth-server/overview
> 
> application by default add 3 Oauth clients 

[![home](https://raw.githubusercontent.com/alirezaalj/Spring-Security-Authorization-Service/master/imgs/home.png "home")](https://raw.githubusercontent.com/alirezaalj/Spring-Security-Authorization-Service/master/imgs/home.png "home")
##### login  http://localhost:9000/auth/login
[![login](https://raw.githubusercontent.com/alirezaalj/Spring-Security-Authorization-Service/master/imgs/login.png "login")](https://raw.githubusercontent.com/alirezaalj/Spring-Security-Authorization-Service/master/imgs/login.png "login")

------------

#### Run in development mode
##### Project dependencies:
#### Maven and Java
- maven  : [How to use or Download](https://maven.apache.org/ "How to use or Download")
- java 17
#### email
- ~~gmail~~ account or other smtp email account
- **~~gmail~~ no longer support for Allow Less Secure App. So you should use other mail hosting.**
- if using ~~gmail~~ first do fallow this two-step (no longer support)
  ######1- [Two-Step Verification should be turned off.](https://support.google.com/accounts/answer/1064203?hl=en "Two Step Verification should be turned off.")
  ######2- [Allow Less Secure App(should be turned on).](https://myaccount.google.com/lesssecureapps "Allow Less Secure App(should be turned on).")
  add your email info to **conf/config-repository/oauth-mail.yml** file and host info
```yml
spring:
  mail:
    host: smtp.gmail.com # your smtp host
    port: 587 # your smtp host port
    username: <your_emai_account> # your smtp account username
    password: <your_email_accunt_password>  #your smtp account password
    properties:
      mail:
        smtp:
          auth: true
          connectiontimeout: 5000
          timeout: 5000
          writetimeout: 5000
          starttls:
            enable: true # if your host has TLS is enabled else set false
```
#### Database
- postgres : Run `postgres` on port **`5432`** with username: `postgres` And password: `postgres`
- *i suggest using docker*
- dockerhub : [https://hub.docker.com/_/postgres](https://hub.docker.com/_/postgres "https://hub.docker.com/_/postgres")
- using this commands for *pull *and use **postgres** whit docker
- run and pull PostgreSQL whit docker and config password to **postgres**  on port **5432**

```shell
docker run --name postgresql-container -p 5432:5432 -e POSTGRES_PASSWORD=postgres -d postgres
```
- create new database whit name : **oauth_server_db**
```shell
docker exec -it postgresql-container psql -U postgres -c "CREATE DATABASE oauth_server_db;"
```
- you can use **pgadmin4**  connecting to postgrsql
- if you are running postgrsql in your machine you can change **conf/config-repository/oauth-postgres.yml** file :
```yaml
postgres:
  host: localhost
  db: oauth_server_db # database name
  user: postgres # username
  pass: postgres # password
  port: 5432 # port
```
#### Redis server
- redis server is used for saving login failures, but you can run project without using redis by changing **conf/config-repository/oauth-app.yml**
```yaml
...
    login:
      theme: default
      validator:
        validate-url: ${application.info.host}/verification/{path}/{token}
      fall:
        service: memory # login failures will be saved on memory
        max-attempt: 10
        expire-after:
          duration: 1
          unit: DAYS
...
```
##### running redis: if you want to save failures on redis
```yaml
...
    login:
      theme: default
      validator:
        validate-url: ${application.info.host}/verification/{path}/{token}
      fall:
        service: redis # login failures will be saved on redis
...
```
- Redis : Run `redis` on port **`6379`** with no username And password
- *i suggest using docker*
- dockerhub : [https://hub.docker.com/_/redis](https://hub.docker.com/_/redis "https://hub.docker.com/_/redis")
- using this commands for *pull *and use **redis** whit docker
- run and pull Redis whit docker and config on port **`6379`**

```shell
docker run --name my-redis -p 6379:6379 -d redis
```

#### Running Project
1. go to project folder
2. ```cd "Oauth Authorzation Project"``` and Open this directory with your IDEA
3. open **`config-server`** module
4. copy all files inside **`conf/config-repository-copy/`** to **`conf/config-repository/`**
5. open command line in **`config-repository`** and run this commands:
``git init``
``git add .``
``git commit -m "Initial commit config repo"``
6. after any change on files in **`conf/config-repository/`** you must commit them, then config server can pull them and make it available for other services
7. Start by [spring-boot-maven-plugin](https://docs.spring.io/spring-boot/docs/current/maven-plugin/reference/htmlsingle/ "spring-boot-maven-plugin") with your IDEA

```shell
# or run with command line
cd config-server
# and
mvn spring-boot:run
```
8. project start on port 8888, and you can see the console - logging
9. and configs are available on urls: (you can check them) username: `spring_config_user` password: `spring_config_user`
- http://localhost:8888/oauth-app.yml
- http://localhost:8888/oauth-application.yml
- http://localhost:8888/oauth-base.yml
- http://localhost:8888/oauth-mail.yml
- http://localhost:8888/oauth-postgres.yml
- http://localhost:8888/oauth-redis.yml
10. If you are seen error like : _`error: invalid remote: origin`_ it's because of bad configuration try change configuration file in **`config-server/src/main/resources/application.yml`** and change `uri` to absolute path of `conf/config-repository`
```yaml
    config:
      server:
        git:
          uri: /home/user/Oauth2-Authorization-Project/conf/config-repository  # path to 'conf/config-repository' directory you can replace it with absolute path 
          default-label: master
```
#### After running Config Server we can run **`authorization-server`**
#### dependency:
1. `postgrsql` is running
2. `redis` is running -_if using redis in fall config_
3. `config-server` is running
4. `email` is configured 

Start by [spring-boot-maven-plugin](https://docs.spring.io/spring-boot/docs/current/maven-plugin/reference/htmlsingle/ "spring-boot-maven-plugin") with your IDEA

```shell
# or run with command line
cd authorization-serve
# and
mvn spring-boot:run
```
##### application is ready on http://localhost:9000/
##### The recaptcha is disabled if you have your domain recaptcha `key` and `secret` config them and make `enable: true` and commit changes inside config-repository
`conf/config-repository/oauth-app.yml`
```yaml
google:
  recaptcha:
    enable: false
    key:
      site: <your google key-site> # your google key-site for domain like: localhost
      secret: <your google key-secret> # your google key-secret for domain like: localhost
```
##### The User configurations and OAuth2 Clients config 
- all configs are static in class 
- ``authorization-server/src/main/java/ir/alirezaalijani/security/authorization/service/initializers/DataSourceInitializer.java``
- You can read more about Oauth2 

------------
#### images:
Login page:

[![https://raw.githubusercontent.com/alirezaalj/Spring-Security-Authorization-Service/master/imgs/login.png](https://raw.githubusercontent.com/alirezaalj/Spring-Security-Authorization-Service/master/imgs/login.png "https://raw.githubusercontent.com/alirezaalj/Spring-Security-Authorization-Service/master/imgs/login.png")](https://raw.githubusercontent.com/alirezaalj/Spring-Security-Authorization-Service/master/imgs/login.png "https://raw.githubusercontent.com/alirezaalj/Spring-Security-Authorization-Service/master/imgs/login.png")

------------

Registering Page:

[![https://raw.githubusercontent.com/alirezaalj/Spring-Security-Authorization-Service/master/imgs/registering.png](https://raw.githubusercontent.com/alirezaalj/Spring-Security-Authorization-Service/master/imgs/registering.png "https://raw.githubusercontent.com/alirezaalj/Spring-Security-Authorization-Service/master/imgs/registering.png")](https://raw.githubusercontent.com/alirezaalj/Spring-Security-Authorization-Service/master/imgs/registering.png "https://raw.githubusercontent.com/alirezaalj/Spring-Security-Authorization-Service/master/imgs/registering.png")

------------

Forget Password:


[![https://raw.githubusercontent.com/alirezaalj/Spring-Security-Authorization-Service/master/imgs/forget-password.png](https://raw.githubusercontent.com/alirezaalj/Spring-Security-Authorization-Service/master/imgs/forget-password.png "https://raw.githubusercontent.com/alirezaalj/Spring-Security-Authorization-Service/master/imgs/forget-password.png")](https://raw.githubusercontent.com/alirezaalj/Spring-Security-Authorization-Service/master/imgs/forget-password.png "https://raw.githubusercontent.com/alirezaalj/Spring-Security-Authorization-Service/master/imgs/forget-password.png")

------------

Contact page:


[![https://raw.githubusercontent.com/alirezaalj/Spring-Security-Authorization-Service/master/imgs/contact.png](https://raw.githubusercontent.com/alirezaalj/Spring-Security-Authorization-Service/master/imgs/contact.png "https://raw.githubusercontent.com/alirezaalj/Spring-Security-Authorization-Service/master/imgs/contact.png")](https://raw.githubusercontent.com/alirezaalj/Spring-Security-Authorization-Service/master/imgs/contact.png "https://raw.githubusercontent.com/alirezaalj/Spring-Security-Authorization-Service/master/imgs/contact.png")

------------


Email Verification Template:


[![https://raw.githubusercontent.com/alirezaalj/Spring-Security-Authorization-Service/master/imgs/email-verify.png](https://raw.githubusercontent.com/alirezaalj/Spring-Security-Authorization-Service/master/imgs/email-verify.png "https://raw.githubusercontent.com/alirezaalj/Spring-Security-Authorization-Service/master/imgs/email-verify.png")](https://raw.githubusercontent.com/alirezaalj/Spring-Security-Authorization-Service/master/imgs/email-verify.png "https://raw.githubusercontent.com/alirezaalj/Spring-Security-Authorization-Service/master/imgs/email-verify.png")
