# PLAYFUTDAY
### API REST con SPRING - Proyecto 2ºDAM

<img src="https://img.shields.io/badge/Spring--Framework-5.7-green"/> <img src="https://img.shields.io/badge/Apache--Maven-3.8.6-blue"/> <img src="https://img.shields.io/badge/Java-17.0-brightgreen"/>

 <img src="https://niixer.com/wp-content/uploads/2020/11/spring-boot.png" width="500" alt="Spring Logo"/>
 
___


## **Documentación**

:point_right: [Dirección SWAGGER-IO](http://localhost:8080/swagger-ui/index.html)<br>
:point_right: [Dirección FRONTED](https://github.com/MaylorSr/playfutday_flutter)<br>
:point_right: Se incluye también una colección de Postman para probar datos.

## **DESCRIPCIÓN DEL PROYECTO** :speech_balloon:

Este es un proyecto práctico para el desarrollo de una API REST en lenguaje Java y manejando diferentes tecnologías en la que destaca **Spring**.

También se ha prácticado el manejo de **PostMan**, **Swagger** y **Docker** . Por tanto podrás desplegar la **ApiRest** en las properties, cambiando a perfil de "dev" para utilizar la base de datos de H2 y por otro lado usar el perfil de "prod" para utilizar la base de datos de Posgrest, para ello tendrás que realizar el siguiente comando "docker-compose up" para desplegarla con Docker.

El proyecto trata de una **ApiRest** que gestionará la parte del **Backed** de una red social para personas amantes al fútbol. Por ello, se ha inspirado en redes sociales actuales tales como **Instagram   <img src="https://simpleicons.org/icons/instagram.svg" alt="Instagram Icon" width="30" height="30" style="fill: #E4405F;">
 y Twitter <img src="https://simpleicons.org/icons/twitter.svg" alt="Instagram Icon" width="30" height="30" style="fill: #E4405F;"> **. <br>
 Además de incluír una lógica de negocio de una mezcla entre ambas. Por ello te presentaré a continuación las funcionalidades que se podrán realizar en la **API**.
 
  -Se te proporcionará usuarios para que puedas probarlos:<br>
 ### ADMIN
 - username: wbeetham0
 - password: QUE1chC2Jv
 ### USER
 - username: bmacalester1
 - password: 8dNbnHaX
 <br>En el import.sql encontrarás más usuarios si así deseas probar más, las contraseñas de cada usuario se econtrará comentada por encima del insert de cada uno de estos.
 
 #### USUARIOS ####
- Subir un post (incluye una imágen, tag y descripción)<br>
- Borrar su propio post<br>
- Publicar un comentario en un post
- Dar/Quitar Like a un post
- Obtener sus propios posts
- Crear cuenta de usuario
- Logearse
- Modificar datos de su perfil (contraseña, número de teléfono, descripción...)
- Darse de baja
- Obtener imágen por su nombre
-Obtener información de un usuario por su id
 #### ADMIN ####
-- APARTE DE TODA LAS FUNCIONALIDADES DEL ROL DE USUARIO, SE AÑADE:
- Eliminar un post de un usuario
- Eliminar un usuario
- Banear a un usuario para que no entre en la App
- Añadir/Quitar rol de **ADMIN** a un usuario
- Obtener todos los usuarios que estan registrados en la App
