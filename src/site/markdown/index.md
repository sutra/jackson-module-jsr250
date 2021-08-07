## About Security Entity Filtering Module
Filter out the entity properties from the output of Jackson
serialization according to the DenyAll, RolesAllowed annotations.

### How to use

Register the `Jsr250Module` to `ObjectMapper`:
```java
ObjectMapper objectMapper = new ObjectMapper();
objectMapper.registerModule(new Jsr250Module());
```

Mark your properties with annotations `@RolesAllowed`, `@DenyAll`:
```java
public class User {

	@RolesAllowed("ROLE_ADMIN")
	public String getUsername() {
		return username;
	}

	@DenyAll
	public String getPassword() {
		return password;
	}

}
```

and then the relevant properties would be filtered out from the serialized JSON,
check `Jsr250ModuleTest` for details.

*Note: the roles is read from
`SecurityContextHolder.getContext().getAuthentication().getAuthorities()`
of [spring-security](https://spring.io/projects/spring-security).*
