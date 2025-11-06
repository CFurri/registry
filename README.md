# Registry: Sistema de Gestió de Personal

![Java](https://img.shields.io/badge/Java-21-blue?logo=java&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle-8.13-blue?logo=gradle)
![Database](https://img.shields.io/badge/MySQL-blue?logo=mysql&logoColor=white)
![Persistence](https://img.shields.io/badge/Persistence-JPA%20%7C%20JDBC-orange)
![Architecture](https://img.shields.io/badge/Architecture-Client%2FServer-brightgreen)

Una aplicació de consola i client/servidor per a la gestió d'empleats, torns, fitxatges i formacions d'una empresa.

## 🚀 Sobre el Projecte

Aquest projecte implementa un sistema de gestió de personal (Backoffice) amb dues modalitats d'execució:

1.  **Aplicació de Consola (CLI):** Un backoffice tradicional que s'executa localment i permet gestionar empleats, torns i formacions directament des del terminal.
2.  **Arquitectura Client/Servidor:**
    * **Servidor (`:server`):** Un servidor HTTP lleuger construït amb Sockets i `rawhttp` que exposa una API REST per gestionar els empleats.
    * **Client (`:client`):** Un client de consola que es connecta al servidor per consumir l'API i realitzar operacions CRUD (Crear, Llegir, Actualitzar, Esborrar) contra el servidor.

Una de les característiques clau del projecte és la seva **flexibilitat de persistència**: el nucli de l'aplicació pot funcionar tant amb una implementació de **JDBC pur** com amb **JPA (Hibernate)**, i el canvi entre elles es gestiona fàcilment mitjançant un fitxer de propietats.

## 🌟 Característiques

* **Gestió d'Empleats:** CRUD complet d'empleats.
* **Gestió de Torns:** Creació i consulta de torns (matí, tarda, nit...).
* **Gestió de Formacions:** Creació i consulta de cursos de formació.
* **Informes:** Generació d'informes bàsics (ex: informe d'arribades tardanes).
* **Dues Implementacions:** Tota la lògica de dades està disponible tant en JDBC (amb pools de connexions HikariCP) com en JPA (amb Hibernate).

## 🏗️ Arquitectura del Projecte

El projecte està organitzat en mòduls de Gradle independents, seguint el principi de Separació de Responsabilitats.

* **`:model`**: Defineix les interfícies del domini (ex: `Employee`, `Shift`). És la base del projecte i no té dependències.
* **`:repositories`**: Defineix les interfícies per a l'accés a dades (Patró Repositori), com `EmployeeRepository`.
* **`:jdbc`**: Implementació concreta dels repositoris utilitzant **JDBC pur** i el pool de connexions HikariCP.
* **`:jpa`**: Implementació concreta dels repositoris utilitzant **JPA (Hibernate)**.
* **`:app`**: L'aplicació de consola CLI. Conté els "Managers" (lògica de negoci) i un gestor d'injecció de dependències (DI) simple (`DIManager`) per seleccionar quina implementació (JDBC o JPA) utilitzar.
* **`:server`**: El servidor HTTP. Reutilitza el `DIManager` per exposar els repositoris a través d'una API REST.
* **`:client`**: L'aplicació client de consola que es connecta al `:server`.

## 🛠️ Stack Tecnològic

* **Llenguatge:** Java 21
* **Build:** Gradle (amb scripts en Kotlin `.kts`)
* **Base de Dades:** MySQL
* **Persistència:**
    * JPA (Hibernate)
    * JDBC pur
    * HikariCP (Connection Pooling)
* **Networking:** Sockets Java i `rawhttp-core`
* **Llibreries CLI:** `ascii-table` (per imprimir taules a la consola)
* **Proves:** JUnit 5, H2 Database (per a proves in-memory)
* **Altres:** Lombok, Jackson (per a JSON)

## ⚙️ Instal·lació i Execució

Per executar aquest projecte, necessitaràs tenir instal·lats Java 21, Gradle i un servidor MySQL.

### 1. Configuració de la Base de Dades

El projecte necessita una base de dades MySQL anomenada `registry`.

1.  Assegura't que el teu servidor MySQL està en marxa.
2.  Crea la base de dades:
    ```sql
    CREATE DATABASE registry;
    ```
3.  Configura la connexió. Has d'editar els dos fitxers de configuració perquè coincideixin amb el teu usuari i contrasenya de MySQL:
    * `jdbc/src/main/resources/datasource.properties` (per a la implementació JDBC)
    * `jpa/src/main/resources/META-INF/persistence.xml` (per a la implementació JPA)

4.  (Opcional) Pots utilitzar l'script `jdbc/src/test/resources/schema.sql` per crear les taules manualment si no fas servir JPA per generar-les.

### 2. Canviar entre JDBC i JPA

L'aplicació (tant `:app` com `:server`) utilitza el fitxer `app/src/main/resources/di.properties` per decidir quina implementació de repositoris carregar.

* **Per utilitzar JPA (per defecte):**
    ```properties
    repository_factory=cat.uvic.teknos.registry.jpa.repository.JpaRepositoryFactory
    model_factory=cat.uvic.teknos.registry.jpa.models.JpaModelFactory
    ```
* **Per utilitzar JDBC:**
    ```properties
    repository_factory=cat.uvic.teknos.registry.jdbc.repositories.JdbcRepositoryFactory
    model_factory=cat.uvic.teknos.registry.jdbc.models.JdbcModelFactory
    ```

### 3. Executar l'Aplicació

Pots executar els diferents mòduls utilitzant el Gradle Wrapper.

#### A. Aplicació de Consola (CLI)

Aquest mòdul s'executa localment i utilitza la configuració de `di.properties`.

```bash```
./gradlew :app:run

#### B. Arquitectura Client/Servidor

Per a aquesta modalitat, has d'obrir dos terminals.

1.  **Terminal 1 (Servidor):**
    El servidor s'iniciarà al port 9000 i utilitzarà la configuració de `di.properties`.
    ```bash
    ./gradlew :server:run
    ```

2.  **Terminal 2 (Client):**
    Un cop el servidor estigui en marxa, executa el client.
    ```bash
    ./gradlew :client:run
    ```

## 🧪 Proves

El projecte inclou tests unitaris i d'integració que s'executen contra una base de dades H2 in-memory. Per executar tots els tests:

```bash```
./gradlew test


## VERSIONS
| Version | Description                     |
|---------|---------------------------------|
| 1.0.0   | Monolitic Backoffice            |
| 2.0.0   | Server-Client edition           |
| 2.1.0   | Server-Client edition & Threads |