# API Mutantes

## Resolución algoritmo
El arreglo con ADN lo delego a 8 threads que se encargan de recorrelo
desde distintas direcciones, son dos para buscar en las verticales uno desde el lado 
izquierdo y otro desde el lado derecho de la matriz hasta el centro, dos para buscar 
horizontalmente, uno partiendo desde abajo y el otro desde arriba hasta el centro, y 
4 que inician en cada vertice de la matriz y van verificando las diagonales.

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

## Pasos para levantar el proyecto localmente:
### Con gradle + spring boot 
   ```bash
    git clone git@github.com:dsalasboscan/mladn.git
    cd mladn
    ./gradlew bootRun
   ```
### Con docker
   ```bash
    git clone git@github.com:dsalasboscan/mladn.git
    cd mladn
    ./init-docker.sh
   ```

## Collection postman
Esta ubicada en la ruta: $PROJECT_ROOT/env o desde este link: https://www.getpostman.com/collections/8898a83a88057b558e57

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
 * groovy + spock (testing)
 * h2 

 
