# Evaluación para Java Developer

### Descripción
Es un api rest, el cual se encarga de registrar usuarios y devolver los existentes.

### Requerimientos

1. Java 11
2. mvn
3. git

### Como Iniciar

1. Construir el ejecutable

 ```bash
   git clone https://github.com/jselvamadrigal/evaluationtest.git
   cd evaluationtest
   mvn clean package install
 ```

2. Ejecutarlo

```bash
   cd api/target
   java -jar evaluationtest.jar
```

3. Navegar a la url

```url
    http:\\localhost:8080\api\v1\
```


### Pruebas en Postman    

En la raiz del proyecto se encuentra el archivo "NISUM.postman_collection.json" el cual contiene una colección que puede ser importada en postman.