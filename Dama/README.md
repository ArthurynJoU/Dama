# Dáma

Konzolová textová verzia klasickej hry dáma, implementovaná v Jave s využitím Spring Boot, Spring Data JPA a PostgreSQL.

## Požiadavky

* Java 17 alebo novšia
* Maven 3.6+
* PostgreSQL 12+

## Inštalácia

1. Klonujte repozitár:

   ```bash
   git clone https://github.com/ArthurynJoU/Dama.git
   cd Dama
   ```
2. Vytvorte databázu (napr. v psql alebo DataGrip):

   ```sql
   CREATE DATABASE gamestudio;
   ```
3. Spustite DDL skript pre vytvorenie tabuliek:

   ```bash
   psql -U postgres -d gamestudio -f scripts/schema.sql
   ```
4. Otvorte `src/main/resources/application.properties` a upravte prístupové údaje:

   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/gamestudio
   spring.datasource.username=postgres
   spring.datasource.password=VAŠE_HESLO
   spring.jpa.hibernate.ddl-auto=update
   ```

## Spustenie aplikácie

### Server (REST API + JPA)

```bash
mvn clean package
mvn spring-boot:run -Pserver
```

Po štarte bude API dostupné na `http://localhost:8080/api/`.

### Klient (konzolové UI)

V druhom termináli spustite:

```bash
mvn spring-boot:run -Pclient
```

Nasledujte pokyny v konzole pre hru, komentáre a hodnotenie.

## Štruktúra projektu

```
Dama/
├── pom.xml
├── scripts/schema.sql       # SQL skript pre DB
├── src/
│   ├── main/
│   │   ├── java/org/dama/
│   │   │   ├── client/      # SpringClient, GameMenu
│   │   │   ├── console/     # ConsoleUI (menu hry)
│   │   │   ├── core/        # Logika hry (Board, Game, Move...)
│   │   │   ├── entity/      # JPA entity (Score, Comment, Rating)
│   │   │   ├── repository/  # Spring Data JPA repos
│   │   │   ├── server/      # GameStudioServer + REST kontroléry
│   │   │   └── service/     # Služby (JDBC, JPA, REST implementácie)
│   │   └── resources/
│   │       └── application.properties
│   └── test/                # Jednotkové testy
└── README.md                # Tento súbor
```

## Testovanie

```bash
mvn test
```

## Prispievanie

1. Forknite repozitár a vytvorte novú vetvu
2. Implementujte zmeny a spustite testy
3. Pošlite pull request

## Licencia

Tento projekt je dostupný pod MIT licenciou.
