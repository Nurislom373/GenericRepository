# GenericDaos
GenericDao is a class similar to JpaRepository
the only difference is that we don't need a spring project to use GenericDao in our program. Configuration is very easy, all you need is a properties file.

We can add these 4 configurations to the application.properties file

```Properties
generic.username=postgres
generic.password=test
generic.host=localhost:5432
generic.database=test
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
