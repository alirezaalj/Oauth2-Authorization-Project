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

#### Run in devmode
###### Project dependencies

- maven  : [How to use or Download](https://maven.apache.org/ "How to use or Download")
- java 17
- gmail account or other smtp email account
  if using gmail first do fallow this two step
  1- [Two Step Verification should be turned off.](https://support.google.com/accounts/answer/1064203?hl=en "Two Step Verification should be turned off.")
  2- [Allow Less Secure App(should be turned on).](https://myaccount.google.com/lesssecureapps "Allow Less Secure App(should be turned on).")
- postgres : *i suggest using docker*
  -- dockerhub : [https://hub.docker.com/_/postgres](https://hub.docker.com/_/postgres "https://hub.docker.com/_/postgres")
  -- create new database whit name : **oauth_server_db**
  -- using this commands for *pull *and use **postgres** whit docker

- run and pull PostgreSQL whit docker and config password to **postgres**  on port **5432**
```shell
docker run --name postgresql-container -p 5432:5432 -e POSTGRES_PASSWORD=postgres -d postgres
```
add your email info to **application-mail.yml** file and host info
```
spring:
  mail:
    host: <emai_host> #if useing gmail smtp.gmail.com
    port: 587
    username: <your_emai_account>
    password: <your_email_accunt_password>
```
#### Run
1. go to project folder
2. start by [spring-boot-maven-plugin](https://docs.spring.io/spring-boot/docs/current/maven-plugin/reference/htmlsingle/ "spring-boot-maven-plugin")

```shell
mvn spring-boot:run
```
3. project start on port 9000, and you can see the console - logging
4. command to create jar file
```shell
 mvn clean install
```
##### application is ready on http://localhost:9000/

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


Email Verifivation Template:


[![https://raw.githubusercontent.com/alirezaalj/Spring-Security-Authorization-Service/master/imgs/email-verify.png](https://raw.githubusercontent.com/alirezaalj/Spring-Security-Authorization-Service/master/imgs/email-verify.png "https://raw.githubusercontent.com/alirezaalj/Spring-Security-Authorization-Service/master/imgs/email-verify.png")](https://raw.githubusercontent.com/alirezaalj/Spring-Security-Authorization-Service/master/imgs/email-verify.png "https://raw.githubusercontent.com/alirezaalj/Spring-Security-Authorization-Service/master/imgs/email-verify.png")
