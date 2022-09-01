# GenericRepository
The GenericRepository Class is Difference method with Spring JpaRepository
The difference with JpaRepository is that GenericRepository doesn't need
to be a spring project in order to use it. That is, it can be easily used
in simple build tools like Maven or Gradle without spring. Very easy to use
and lightweight.

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

## New Feature @Table Annotattion Usage

```Java
@Table(name = "auth_user", schema = "public")
public class AuthUser {
    private Integer id;
    private String name;
    private String password;
}
```
