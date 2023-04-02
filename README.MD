# Taller final – Servicios bancarios

Continuando con lo planteado en tareas anteriores, se desea crear un software para un banco, específicamente servicios web en java. 

Se tiene una base de datos “banco.db” de 3 tablas, llamadas, usuarios, cuentas y transacciones.

Se tiene una restricción de 1 a muchos, donde una cuenta pertenece a un solo usuario, y un usuario posee diferentes cuentas, por lo tanto, en la tabla cuentas se hace una referencia de id a la tabla usuarios (llave foránea).

Así mismo la tabla transacciones tiene una referencia a la tabla cuentas, permitiendo que una cuenta tenga múltiples transacciones, pero una transacción pertenezca a una sola cuenta.

Tal como se muestra en el siguiente modelo de base de datos

### Diagrama relacional

![Diagrama UML](./IMG/Diagrama%20UML.png)

Se desea mantener los requerimientos planteados en el taller anterior (en cuanto a depositar, retirar y transferir). Se desea mantener la información de los usuarios, con sus cuentas, y el historial de sus transacciones. Implemente una solución en java con el servidor de aplicación Jetty, para cumplir con que:

1- El sistema permita crear, listar y eliminar usuarios.

2- Se desea que el sistema permita crear, listar y eliminar cuentas.

a. Para crear una cuenta es necesario que ya exista un usuario.

b. Se deben listar solo las cuentas de un usuario específico por id.

3- Se desea que el sistema permita realizar y ver transacciones.

a. Se debe almacenar en la tabla transacciones con el tipo de acción realizada (depositar, retirar y transferir). Debe existir cuentas que puedan realizar dicha acción. Use id.

b. Para transferencias no es necesario poner la cuenta destino, solo el tipo de cuenta a la que se transfirió.

c. Si la acción es diferente de transferir, el campo de “tipo_cuenta_destino” puede estar en nulo.

d. Se deben listar todas las transacciones de una cuenta, buscada por id.

## Nota:

1. Tener en cuenta en que, al ser una base de datos relacional con restricciones, debe tener un orden al eliminar registros. Al eliminar un usuario por ejemplo desencadena que debe eliminar primero sus cuentas, y para cada cuenta primero debe eliminar sus transacciones

2. Para las clases Repositorio no es necesario inicializar la tabla, se adjunta un main el cual pueden ejecutar al montar el proyecto (después de instalar en las dependencias el sqlite) que creará la base de datos. O mueven el archivo banco.db adjunto a la raíz del proyecto.

3. La cadena de conexión debe ser la siguiente "jdbc:sqlite:banco.db".

4. Se debe realizar la conexión para cada clase repositorio.

5. Usar una interfaz repositorio y servicio.

6. Los nombramientos de los endpoints quedan a disposición de cada uno.

7. No se puede alterar las tablas de la base de datos proporcionada.

8. Si tienen dudas para las consultas SQL me pueden escribir.

9. Se brinda tambien el DDL de la base de datos, para quien lo desee revisar.

10. Subir el proyecto a un repositorio Github o Gitlab.