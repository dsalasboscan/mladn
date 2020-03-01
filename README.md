# API Mutantes

## Resolución algoritmo
El arreglo con ADN lo delego a 8 threads que se encargan de recorrelo
desde distintas direcciones para ir encontrando los mutantes.

Todos los Threads comparten una variable que van actualizando cuando 
consiguen un mutante, el thread principal que recibe el request HTTP inicia
el procesamiento de los 8 threads y se queda esperando a que los subprocesos
encuentren mas de 1 cadena que represente a un mutante o hasta
que todos terminen su trabajo.

Cada Thread se da cuenta del match de letras incrementando un contador interno si 
la letra adjacente es igual o reseteandolo si no lo es, cada vez que encuentre 4 
letras consecutivas sumara un nuevo mutante a la variable compartida.

## Entorno
- Cloud: https://mladn.herokuapp.com
- Local: localhost:9090

## Detectar mutantes

### `POST` /mutants

#### Request:
```json
{
  "dna": [
                "CTGCTACGAT",
                "TGAGATTAGG",
                "GCCACGACCC",
                "ATTGATCAAC",
                "CTCTTACGCG",
                "AGGCAAGATA",
                "TCTGCGTCCA",
                "TACAGCGTGT",
                "GTATGATCAC",
                "CGACACCATT"
        ]
}
```

#### Response:
HTTP 200 -> En caso de SI detectar un mutante
<br>
HTTP 403 -> En caso de NO detectar un mutante

### Obtener estadisticas de validacion de ADN
### `GET` /stats

#### Response:
```json
{
    "ratio": 0.83,
    "count_mutant_dna": 5,
    "count_human_dna": 1
}
```

 
 ## Pasos para probar levantar el proyecto localmente:
   ```bash
    git clone git@github.com:dsalasboscan/mladn.git
    cd mladn
    ./gradlew bootRun
   ```
 
 ## Coverage
Para ver coverage en la raiz del proyecto ejecutar:
  
  ```bash
  ./gradlew clean build jacocoTestReport
  ```
Los resultados estaran en la siguiente ruta: $PROJECT_ROOT/build/jacocoHtml/index.html

## Stack

 * java 1.8
 * spring-boot 2
 * spring data jdbc
 * spock (testing)
 * h2

 
