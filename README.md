# RedSocialMongodb
Pasos para su instalación:
 - Descargar MongoDB:   https://www.mongodb.org/downloads
 - Importar librerías:  http://mvnrepository.com/artifact/org.mongodb/mongo-java-driver/2.6.1

Requisitos de la práctica

Se desea crear una aplicación Java que simule una parte de una red social. Para hacerlo, la aplicación se hará mediante una base de datos NoSQL, concretamente, MongoDB.

A continuación se detallan las funcionalidades de la aplicación.
 - Inicio de sesión / registro: Al arrancar la aplicación el usuario podrá entrar con su usuario o registrarse. Cuando se realiza el registro, el usuario añade los siguientes datos (nombre, apellidos, correo electrónico, Contraseña, Dirección (calle, numero, localidad y código postal).
 - Unión a un grupo: Si seleccionamos la opción de unirse a un grupo, el usuario indicará el nombre del grupo y se almacenaran la fecha y hora en la que se ha unido. Además, se actualizará el número de miembros que tiene el grupo si ya existe.
 - Comentar en un grupo: Otra opción que tendrá el usuario será, comentar en un grupo. Para realizarlo, se mostrará los grupos en los que se a unido el usuario y seleccionará en cual desea comentar. Cuando seleccione, añadirá el mensaje y la fecha y hora.
 - Visualización de comentarios que se han realizado en uno de mis grupos: Esta opción, se mostraran los comentarios que hay en el grupo seleccionado mostrando el nombre de usuario que ha realizado el comentario, fecha y hora del comentario y el autor que lo ha realizado.
 - Listar usuario que hay en mi localidad y en un grupo determinado: Mostrará el nombre y el mail de los usuarios que pertenecen a la misma localidad del usuario.
 - Mostrar número de usuarios por grupo: Mostrará de cada grupo del usuario, el número de usuarios que existen en ese grupo.
 - Darse de baja: Eliminará el usuario con el que se ha realizado login.
