# Sistema de Matrícula Académica - EduAndes (Backend)

API REST, desarrollada en Spring Boot para la gestión académica y matrícula de estudiantes en EduAndes.

## Tecnologías Utilizadas
- Java 17 / 21
- Spring Boot 3.x (Spring Web, Spring Data JPA, Validation)
- Oracle Database (Perfil dev y prod)
- Springdoc OpenAPI (Swagger UI)
- Maven / Git

## Configuración y Ejecución

### Perfiles de Base de Datos
- **Desarrollo (dev):** Conexión local a Oracle Database (`localhost:1521/XEPDB1`) con usuario de aplicación `MATRIDB`.
- **Producción (prod):** Configuración parametrizada mediante variables de entorno del sistema (`SPRING_DATASOURCE_*`).


### Documentación de la API (Swagger)
Con la aplicación en ejecución en el puerto 8080:

Swagger UI: http://localhost:8080/swagger-ui.html

OpenAPI JSON: http://localhost:8080/v3/api-docs

Health Check: http://localhost:8080/api/v1/health

### Convenciones de Git
Flujo de trabajo basado en ramas: main, develop, feature/matricula-armas.

Integraciones controladas con merges explícitos --no-ff.

Etiquetado de versión de entrega de la Unidad 1: v1.0-unidad1.
### Ejecución local
```bash

mvn clean install
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```
