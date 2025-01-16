# Documentación Técnica del Proyecto: Sistema de Auditoría Reactivo

## **Descripción del Proyecto**
El sistema de auditoría reactivo permite registrar y visualizar en tiempo real las transacciones bancarias detalladamente, permitiendo a los usuarios visualizar sus saldos y movimientos en tiempo real.
---

## **Tecnologías Utilizadas**
- **Lenguaje de Programación:** Java 17
- **Framework:** Spring WebFlux
- **Base de Datos:** MongoDB
- **Control de Versiones:** Git (repositorio alojado en GitHub)
- **Metodología de Git:** Trunk-base
- **Pruebas:** JUnit y Mockito

---

## **Estructura del Proyecto**

### **Paquetes**

1. **`commons`**: Contiene clases DTO de entrada y utilizadas en distintas capas del proyecto.
   
2. **`config`**: Clases relacionadas con la configuración del sistema.
   - Configuración de Spring WebFlux.
   - Configuración de MongoDB.

3. **`controllers`**: Controladores que manejan las solicitudes HTTP.
   - **`TransactionController`**: Endpoints para operaciones de transacción.
   - **`AuditController`**: Endpoints para auditoría de cuentas y transacciones.

4. **`data`**: Contiene las entidades y los repositorios.
   - **Entidades:** Representan las colecciones de la base de datos (`Transaction`, `Account`).
   - **Repositorios:** Interfaces para interactuar con la base de datos mediante Spring Data MongoDB.

5. **`exceptions`**: Manejo centralizado de errores.
   - Clases personalizadas para excepciones como `NotFoundException`.
   - Controlador global de excepciones para retornar respuestas HTTP significativas.

6. **`mappers`**: Conversores de entidades a DTOs y viceversa.
   - Facilita la comunicación entre las entidades y los datos enviados o recibidos por el cliente.

7. **`services`**: Implementa la lógica de negocio.
   - **`TransactionService`**: Validación y procesamiento de transacciones.
   - **`AuditService`**: Obtención del saldo de cuenta y el historial de auditoría.

8. **Archivo `Application`**: Clase principal que inicializa la aplicación Spring WebFlux.

9. **`resources`**:
   - **`application.yaml`**: Configuración de la base de datos MongoDB y otros ajustes globales.

---

## **Modelo de Base de Datos**

### **Entidades**

1. **`Account`**:
   - **Atributos:**
     - `id`: Clave primaria.
     - `accountNumber`: Número de cuenta único.
     - `balance`: Saldo actual.
     - `userDocument`: Documento del usuario propietario de la cuenta.
   - **Relaciones:**
     - Uno a muchos con `Transaction`.

2. **`Transaction`**:
   - **Atributos:**
     - `id`: Clave primaria.
     - `userDocument`: Documento del usuario que realiza la transacción.
     - `accountNumber`: Número de cuenta asociada.
     - `amount`: Monto de la transacción.
     - `type`: Tipo de transacción (depósito o retiro).
     - `isSuccess`: Indica si la transacción fue exitosa.
     - `dateOperation`: Fecha de la transacción.
     - `preBalance`: Saldo antes de la transacción.
     - `postBalance`: Saldo después de la transacción.

---

## **Endpoints REST**

### **Transacciones (`/api/transaction`):**
- **POST:** `/api/transaction` - Realiza una transacción (depósito o retiro).

### **Auditorías (`/api/audit`):**
- **GET:** `/api/audit/balance/{accountNumber}` - Obtiene el saldo de una cuenta.
- **GET:** `/api/audit/history/{accountNumber}` - Obtiene el historial de auditoría de una cuenta.

---

## **Seguridad**
- Como este es un servicio interno, no se requiere uso de tokens para consumir otras aplicaciones.

---

## **Pruebas**

### **Pruebas Unitarias:**
- Usando **JUnit** y **Mockito**.
- Pruebas para:
  - Servicios (lógica de negocio).
  - Controladores (integración con la API REST).

### **Patrón AAA (Arrange-Act-Assert):**
- **Arrange:** Configurar los datos y dependencias necesarias.
- **Act:** Ejecutar el método o funcionalidad a probar.
- **Assert:** Verificar los resultados esperados.

---

## **Versionamiento**
- **Repositorio GitHub:**
  - metodologia trunk base
  - Estructura clara con commits descriptivos.
  - Uso de ramas para implementar nuevas funcionalidades.

---

---

**Autores:** Johan Sebastian Quimbayo


