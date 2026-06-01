# QTMTicket - Backend Service Documentation

## Panoramica Progetto

**QTMTicket** è un servizio backend autonomo per la gestione centralizzata dei ticket all'interno dell'ecosistema QTM.

### Caratteristiche Principali

- ✅ **Database Centralizzato**: Istanza SQLite unica (`qtmticket.db`) condivisa tra tutti i realm
- ✅ **CRUD Completo**: Create, Read, Update, Delete con operazioni transazionali
- ✅ **Ricerca Avanzata**: Filtri multipli (realm, progetto, paziente, stato, tipo)
- ✅ **Riferimenti Multipli**: 
  - Realm (provenienza)
  - Progetto (es. TENANTS, DASHBOARD, PATIENTS)
  - ID Paziente (FK logico)
  - ID Piano Terapeutico (FK logico)
- ✅ **Tipologie Flessibili**: 6 enum di tipo (BUG, FEATURE, SUPPORT, ISSUE, ENHANCEMENT, DOCUMENTATION)
- ✅ **Stato Gestito**: 6 enum di stato (OPEN, IN_PROGRESS, ON_HOLD, CLOSED, REJECTED, REOPENED)
- ✅ **Contenuto JSON**: Campo per gestione dinamica e flessibile del contenuto
- ✅ **Liquibase**: Gestione DB con changelog YAML versionato
- ✅ **API REST**: 12 endpoint con paginazione
- ✅ **Exception Handler**: Gestione centralizzata errori con `@RestControllerAdvice`
- ✅ **Logging**: SLF4J + Spring Boot (no System.out.println)
- ✅ **Testing**: Test di integrazione con database in-memory

---

## Architettura

```
QTMTicket (Backend-only, Spring Boot 3.5.12, Java 17)
│
├── Controller Layer (REST API)
│   ├── TicketController (12 endpoint CRUD + ricerca)
│   └── GlobalExceptionHandler (gestione errori centralizzata)
│
├── Service Layer (Business Logic)
│   └── TicketService (orchestrazione, validazioni, logica di dominio)
│
├── Repository Layer (Data Access)
│   └── TicketRepository (JPA, query custom con `@Query`)
│
├── Entity / DTO / Mapper
│   ├── TicketEntity (JPA entity con enum, timestamps auto)
│   ├── TicketDto (DTO per API REST)
│   └── TicketMapper (conversione Entity ↔ DTO)
│
├── Exception Handling
│   ├── TicketNotFoundException (404)
│   └── TicketValidationException (400)
│
└── Database (SQLite)
    └── Liquibase Changelog (YAML versionato)
```

---

## Endpoints API

### Base URL
```
http://localhost:8086/api/ticket/tickets
```

### 1. CRUD Operations

#### Crea Ticket
```http
POST /tickets
Content-Type: application/json

{
  "realm": "REALM_EXAMPLE",
  "project": "TENANTS",
  "patientId": "PAT-001",
  "therapeuticPlanId": "PLAN-001",
  "ticketType": "BUG",
  "title": "Login non funziona",
  "description": "Errore 500 al login",
  "contentJson": "{\"errorCode\": \"ERR_500\", \"endpoint\": \"/auth/login\"}"
}

Response: 201 Created
{
  "id": 1,
  "realm": "REALM_EXAMPLE",
  "project": "TENANTS",
  "patientId": "PAT-001",
  "therapeuticPlanId": "PLAN-001",
  "ticketType": "BUG",
  "status": "OPEN",
  "title": "Login non funziona",
  "description": "Errore 500 al login",
  "contentJson": "{\"errorCode\": \"ERR_500\", \"endpoint\": \"/auth/login\"}",
  "createdAt": "2026-06-01T09:20:00",
  "updatedAt": "2026-06-01T09:20:00"
}
```

#### Recupera Ticket per ID
```http
GET /tickets/{id}

Response: 200 OK
{...ticket details...}
```

#### Aggiorna Ticket
```http
PUT /tickets/{id}
Content-Type: application/json

{
  "realm": "REALM_EXAMPLE",
  "project": "TENANTS",
  "patientId": "PAT-001",
  "therapeuticPlanId": "PLAN-001",
  "ticketType": "BUG",
  "status": "IN_PROGRESS",
  "title": "Login non funziona - in analisi",
  "description": "Errore 500 al login - issue tracciata",
  "contentJson": "{\"errorCode\": \"ERR_500\", \"endpoint\": \"/auth/login\", \"assignedTo\": \"dev-team\"}"
}

Response: 200 OK
{...updated ticket...}
```

#### Elimina Ticket
```http
DELETE /tickets/{id}

Response: 204 No Content
```

---

### 2. Ricerca e Filtri

#### Per Realm (paginato)
```http
GET /tickets/by-realm/REALM_EXAMPLE?page=0&size=10&sort=createdAt,desc

Response: 200 OK
{
  "content": [{...}, {...}],
  "pageable": {...},
  "totalElements": 25,
  "totalPages": 3,
  "number": 0
}
```

#### Per Progetto (paginato)
```http
GET /tickets/by-project/TENANTS?page=0&size=10

Response: 200 OK
{...paginated results...}
```

#### Per Paziente (paginato)
```http
GET /tickets/by-patient/PAT-001?page=0&size=10

Response: 200 OK
{...paginated results...}
```

#### Per Piano Terapeutico (paginato)
```http
GET /tickets/by-plan/PLAN-001?page=0&size=10

Response: 200 OK
{...paginated results...}
```

#### Per Stato (paginato)
```http
GET /tickets/by-status/OPEN?page=0&size=10

Response: 200 OK
{...paginated results...}
```

#### Ricerca Avanzata (filtri multipli)
```http
GET /tickets/search?realm=REALM_EXAMPLE&project=TENANTS&patientId=PAT-001&status=OPEN&page=0&size=10

Response: 200 OK
{...paginated results...}
```

#### Ticket Aperti per Paziente
```http
GET /tickets/patient/PAT-001/open

Response: 200 OK
[
  {...ticket1...},
  {...ticket2...}
]
```

---

### 3. Gestione Stato

#### Cambia Stato Ticket
```http
PATCH /tickets/{id}/status/CLOSED

Response: 200 OK
{
  "id": 1,
  "status": "CLOSED",
  "updatedAt": "2026-06-01T10:00:00",
  ...
}
```

**Stati disponibili**: `OPEN`, `IN_PROGRESS`, `ON_HOLD`, `CLOSED`, `REJECTED`, `REOPENED`

---

## Modello Dati

### Tabella `tickets`

| Campo | Tipo | Nullo | Note |
|-------|------|-------|-------|
| id | BIGINT | No | PK, auto-increment |
| realm | VARCHAR(100) | No | Realm di provenienza |
| project | VARCHAR(100) | No | Progetto di provenienza |
| patient_id | VARCHAR(100) | No | ID paziente (FK logico) |
| therapeutic_plan_id | VARCHAR(100) | Sì | ID piano terapeutico (FK logico) |
| ticket_type | VARCHAR(50) | No | Enum: BUG, FEATURE, SUPPORT, ISSUE, ENHANCEMENT, DOCUMENTATION |
| status | VARCHAR(50) | No | Enum: OPEN, IN_PROGRESS, ON_HOLD, CLOSED, REJECTED, REOPENED |
| title | VARCHAR(500) | No | Titolo/Oggetto |
| description | TEXT | Sì | Descrizione dettagliata |
| content_json | TEXT | Sì | JSON per gestione dinamica |
| created_at | DATETIME | No | Data creazione (auto) |
| updated_at | DATETIME | No | Data aggiornamento (auto) |

### Indici
- `idx_tickets_realm`
- `idx_tickets_project`
- `idx_tickets_patient_id`
- `idx_tickets_status`
- `idx_tickets_therapeutic_plan_id`
- `idx_tickets_ticket_type`

---

## Configurazione

### application.yml

```yaml
spring:
  datasource:
    url: jdbc:sqlite:./data/qtmticket.db  # File database centralizzato
  jpa:
    hibernate:
      ddl-auto: validate                    # Usa Liquibase, no DDL auto
  liquibase:
    change-log: classpath:/db/changelog/db.changelog-master.yaml

server:
  port: 8086                                # Porta servizio
  servlet:
    context-path: /api/ticket               # Context path
```

---

## Build e Esecuzione

### Compilazione
```bash
cd c:\Users\Dago\git\QTM\QTMTicket
mvn clean install
```

### Esecuzione
```bash
mvn spring-boot:run

# Oppure direttamente il JAR
java -jar target/qtm-ticket-1.0.0.jar
```

### Database
- File: `./data/qtmticket.db` (viene creato automaticamente)
- Migrations: Gestite da Liquibase

---

## Eccezioni

### 404 Not Found
```json
{
  "status": 404,
  "message": "Ticket con ID 999 non trovato",
  "detail": "Il ticket richiesto non esiste"
}
```

### 400 Bad Request (Validation)
```json
{
  "status": 400,
  "message": "Errore di validazione",
  "detail": "Il realm è obbligatorio"
}
```

### 500 Internal Server Error
```json
{
  "status": 500,
  "message": "Errore interno del server",
  "detail": "..."
}
```

---

## Logging

- **Livello default**: INFO
- **Per `com.qtm.ticket`**: DEBUG
- **Logger**: SLF4J + Spring Boot Logging
- **Non consentito**: `System.out.println()`, `System.err.println()`

---

## Testing

### Test di Integrazione
```bash
mvn test
```

Test file: `src/test/java/com/qtm/ticket/TicketServiceIntegrationTest.java`

Copertura:
- ✅ createTicket()
- ✅ getTicketById()
- ✅ updateTicket()
- ✅ deleteTicket()
- ✅ getTicketsByRealm()
- ✅ getTicketsByPatient()
- ✅ changeStatus()
- ✅ Validation Exceptions
- ✅ Open tickets retrieval

---

## Struttura File Progetto

```
QTMTicket/
├── pom.xml                               # Maven configuration
├── README.md                             # Documentazione breve
├── .gitignore
│
├── src/main/java/com/qtm/ticket/
│   ├── QtmTicketApplication.java         # Spring Boot main
│   │
│   ├── controller/
│   │   ├── TicketController.java         # 12 REST endpoints
│   │   └── GlobalExceptionHandler.java   # Exception handling
│   │
│   ├── service/
│   │   └── TicketService.java            # Business logic
│   │
│   ├── repository/
│   │   └── TicketRepository.java         # JPA + custom queries
│   │
│   ├── entity/
│   │   └── TicketEntity.java             # JPA entity con enum e timestamps
│   │
│   ├── dto/
│   │   └── TicketDto.java                # Transfer object
│   │
│   ├── mapper/
│   │   └── TicketMapper.java             # Entity ↔ DTO conversion
│   │
│   └── exception/
│       ├── TicketNotFoundException.java
│       └── TicketValidationException.java
│
├── src/main/resources/
│   ├── application.yml                   # Application config (prod)
│   └── db/changelog/
│       ├── db.changelog-master.yaml      # Master changelog
│       └── 20260601-01-create-tickets-table.yaml
│
└── src/test/
    ├── java/com/qtm/ticket/
    │   └── TicketServiceIntegrationTest.java
    └── resources/
        └── application-test.yml          # Test config (in-memory)
```

---

## Dipendenze Principali

- **Spring Boot**: 3.5.12
- **Java**: 17
- **Database**: SQLite 3.45.3.0
- **Hibernate**: 6.6.44 (community dialect for SQLite)
- **Liquibase**: Versione definita da Spring Boot BOM
- **Lombok**: Riduzione verbosità codice
- **Jackson**: JSON serialization
- **JUnit 5**: Testing framework

---

## Note di Implementazione

### Design Decisions

1. **Database Centralizzato**: Singola istanza `qtmticket.db` condivisa tra tutti i realm, garantisce consistenza e semplifica backup/replica

2. **Enum per Tipologie e Stato**: Garantisce integrità dati e facilita validazione lato server

3. **JSON per Contenuto**: Flessibilità massima per informazioni aggiuntive e dinamiche senza modificare schema

4. **FK Logici (non DB)**: `patientId` e `therapeuticPlanId` sono string, permette riferimento tra sistemi diversi senza vincoli di integrità hard

5. **Timestamps Auto**: CreatedAt e UpdatedAt gestiti automaticamente da JPA `@PrePersist` e `@PreUpdate`

6. **Service Layer Pattern**: Separazione chiara tra Controller (API) e Service (business logic)

7. **Mapper Pattern**: Conversione Entity-DTO esplicita, evita esposizione di dettagli JPA nell'API

8. **Exception Handler Centralizzato**: `@RestControllerAdvice` per gestione coerente di errori

9. **Liquibase YAML**: Changelog in YAML (più leggibile di XML), versionato in git

---

## Prossimi Passi (Optional)

- [ ] Aggiungere autenticazione/autorizzazione (Spring Security)
- [ ] Implementare soft delete (flag is_deleted)
- [ ] Aggiungere audit trail (chi ha modificato, quando)
- [ ] Integrare con QTMCommonLib (DTOs comuni)
- [ ] Caching (Redis per query frequenti)
- [ ] Event sourcing per notifiche real-time
- [ ] API documentation (Swagger/OpenAPI)

---

**Ultima modifica**: 01/06/2026 09:20 UTC+2
**Versione**: 1.0.0
**Status**: ✅ Build Success | ✅ DB Schema Created | ✅ API Ready
