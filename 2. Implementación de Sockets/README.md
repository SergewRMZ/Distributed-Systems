Esta es una implementación educativa de una Mix Network utilizando RSA OAEP y AES-GCM para proveer anonimato, confidencialidad y autenticación.
Cada nodo Mix elimina una capa de cifrado del mensaje antes de enviarlo.

## Generación de llaves RSA.

Ejecutar el archivo KeyGenerator.java para poder generar los tres pares de llaves RSA.

# Ejecución de la Mixnet.

Ejecutar el siguiente comando en la consola desde la carpeta raíz del proyecto para generar los archivos .class
Creará una carpeta out/server con todos los archivos .class

```
javac -d out server/*.java
```


```
mix_port privateKeyName next_mix_port next_mix_host
```

## Ejemplo de ejecución.
Ejecutar el siguiente comando desde la raíz.

```
java -cp out server.App 8080 privateKey_A 8081 localhost
```