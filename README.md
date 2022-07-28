# GenericDaos
GenericDao is a class similar to JpaRepository
the only difference is that we don't need a spring project to use GenericDao in our program. Configuration is very easy, all you need is a properties file.

We can add these 3 configurations to the application.properties file

```Properties
db.username=test
db.password=test
db.jdbc=jdbc:postgresql://localhost:5432/test
```
