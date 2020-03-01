# API Mutantes

## Detectar mutantes

### `POST` /mutants

#### Request:
```json
{
	
}
```

#### Response:
HTTP 200 -> En caso de SI detectar un mutante
<br>
HTTP 403 -> En caso de NO detectar un mutante

### Obtener estadisticas de validacion de ADN
### `GET` /stats

#### Request:
```json
{
	
}
```

#### Response:
```json
{

}
```

## Scripts BD:
#### Estan ubicados en el proyecto en la ruta: $PROJECT_ROOT/src/main/resources/sql-scripts/

## Stack

 * java 1.8
 * spring-boot 2
 * spring data jdbc
 * spock (testing)
 * h2

 
 ## Gradle build
 
Para buildear el proyecto se debe ejecutar
  
  ```bash
  ./gradlew build
  ```

Para ejecutar task de jacoco para reporte de coverage
  
  ```bash
  ./gradlew clean build jacocoTestReport
  ```
 
