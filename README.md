# Wyprawowo
Project dedicated to saving information about your hikes along with media (photos and videos).

## To use:
Don't. Seriously. It's just another uni project.

## To run:
First you need to clone the repository
```
git clone https://github.com/impune-pl/wyprawowo
```
Then make sure you have: openJDK 15, npm, yarn, MySQL and Maven installed.
You need a user with full privileges to a database in MySQL.
Default username and database name is `wyprawowo`, and preferred password is `123wyprawowo`.
If you use a different username, password or database name, you need to make changes to following lines:
```
spring.datasource.username=wyprawowo
spring.datasource.password=123wyprawowo
spring.datasource.url=jdbc:mysql://127.0.0.1:3306/wyprawowo?createDatabaseIfNotExist=false&serverTimezone=UTC&useLegacyDateTimeCode=false
```
in `src/main/resources/application.properties`

To compile the front end:
```
cd ./frontend_src
npm install
yarn build
```

To compile and run back-end:
```
mvn spring-boot:start
```

This app uses port 8081 by default.
Back end takes care of serving the front end and creating database structure (code-first approach).
