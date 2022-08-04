# GenericDaos
GenericDao is a class similar to JpaRepository
the only difference is that we don't need a spring project to use GenericDao in our program. Configuration is very easy, all you need is a properties file.

We can add these 3 configurations to the application.properties file

```Properties
db.username=test
db.password=test
db.jdbc=jdbc:postgresql://localhost:5432/test
```

The main class we need is GenericRepository<T, ID>

```Java
public class GenericRepository<T, ID> {
}
```

```Java
public class AuthRepo extends GenericRepository<AuthUser, Integer> {
}
```

# Some Bugs

If this error occurs when working with LocalDate
Java 8 date/time type `java.time.LocalDate` not supported by default: add Module "com.fasterxml.jackson.datatype:jackson-datatype-jsr310"

```Xml
<dependency>
    <groupId>com.fasterxml.jackson.datatype</groupId>
    <artifactId>jackson-datatype-jsr310</artifactId>
    <version>2.13.3</version>
</dependency>
```

These annotations should be placed on the LocalDate field.

```Java
@JsonDeserialize(using = LocalDateDeserializer.class)
@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
private LocalDate createdAt
```
above instructions will solve your problem 👍
