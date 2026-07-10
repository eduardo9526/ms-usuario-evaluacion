# Microservicio: Usuario_1 

Este microservicio se encarga de la gestión integral de usuarios dentro de la plataforma académica. Permite realizar operaciones CRUD (Crear, Leer, Actualizar y Eliminar) asegurando la persistencia de datos y la integridad de la información.

## Funcionalidades
El microservicio expone una lógica de negocio robusta a través de `UsuarioService`:
* **Listar Usuarios:** Recupera todos los usuarios registrados.
* **Búsqueda por ID:** Obtención detallada con manejo de errores (404 Not Found).
* **Registro de Usuarios:** Creación mediante objetos de transferencia de datos (Records).
* **Actualización:** Modificación de perfil, credenciales y roles.
* **Eliminación:** Borrado físico de registros por ID.

##  Instrucciones de Maven (Requisito Examen)
Para el cumplimiento de los estándares de la prueba, utilice los siguientes comandos:

### Ejecutar Pruebas y Test
Para ejecutar los test unitarios y validar los endpoints de Usuario:
```bash
./mvnw test







