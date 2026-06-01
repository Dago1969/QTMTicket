# QTM Ticket Service

Servizio centralizzato per la gestione dei ticket all'interno dell'ecosistema QTM.

## Caratteristiche

- **Database Centralizzato**: Database SQLite `qtmticket.db` utilizzato da tutti i realm
- **Ticket Management**: CRUD completo con gestione dello stato
- **Riferimenti Multipli**: Supporto per realm, progetto, paziente, piano terapeutico
- **Tipologie Flessibili**: Enum di tipologie (BUG, FEATURE, SUPPORT, ISSUE, ENHANCEMENT, DOCUMENTATION)
- **Contenuto JSON**: Campo JSON per gestione dinamica del contenuto
- **Liquibase**: Gestione del database con changelog YAML
- **API REST**: Endpoint CRUD completi con ricerca e filtri

## Struttura

```
src/main/java/com/qtm/ticket/
├── entity/           # JPA Entity
├── dto/             # Data Transfer Object
├── controller/      # REST Controllers
├── service/         # Business Logic
├── repository/      # Data Access Layer
├── mapper/          # Entity-DTO Mapping
└── exception/       # Custom Exceptions
```

## API Endpoints

### CRUD Operations

- `POST /tickets` - Crea un nuovo ticket
- `GET /tickets/{id}` - Recupera un ticket per ID
- `PUT /tickets/{id}` - Aggiorna un ticket
- `DELETE /tickets/{id}` - Elimina un ticket

### Ricerca e Filtri

- `GET /tickets/by-realm/{realm}` - Ticket per realm (paginato)
- `GET /tickets/by-project/{project}` - Ticket per progetto (paginato)
- `GET /tickets/by-patient/{patientId}` - Ticket per paziente (paginato)
- `GET /tickets/by-plan/{therapeuticPlanId}` - Ticket per piano terapeutico (paginato)
- `GET /tickets/by-status/{status}` - Ticket per stato (paginato)
- `GET /tickets/search` - Ricerca con filtri multipli
- `GET /tickets/patient/{patientId}/open` - Ticket aperti per paziente

### Stato

- `PATCH /tickets/{id}/status/{newStatus}` - Cambia lo stato di un ticket

## Modello Dati

### Ticket Entity

| Campo | Tipo | Obbligatorio | Note |
|-------|------|-------------|-------|
| id | BIGINT | Sì | Primary Key, auto-increment |
| realm | VARCHAR(100) | Sì | Realm di provenienza |
| project | VARCHAR(100) | Sì | Progetto di provenienza |
| patient_id | VARCHAR(100) | Sì | ID del paziente |
| therapeutic_plan_id | VARCHAR(100) | No | ID del piano terapeutico |
| ticket_type | VARCHAR(50) | Sì | Enum: BUG, FEATURE, SUPPORT, ISSUE, ENHANCEMENT, DOCUMENTATION |
| status | VARCHAR(50) | Sì | Enum: OPEN, IN_PROGRESS, ON_HOLD, CLOSED, REJECTED, REOPENED |
| title | VARCHAR(500) | Sì | Titolo/Oggetto del ticket |
| description | TEXT | No | Descrizione dettagliata |
| content_json | TEXT | No | Contenuto JSON per gestione dinamica |
| created_at | DATETIME | Sì | Data di creazione (auto) |
| updated_at | DATETIME | Sì | Data ultimo aggiornamento (auto) |

## Build e Deploy

### Compilazione

```bash
mvn clean install
```

### Esecuzione

```bash
mvn spring-boot:run
```

Il servizio sarà disponibile su `http://localhost:8086/api/ticket`

## Database

Il database SQLite viene creato automaticamente in `./data/qtmticket.db`

Le migrations vengono gestite da Liquibase tramite i changelog in `src/main/resources/db/changelog/`

## Logging

- **Level**: INFO (DEBUG per `com.qtm.ticket`)
- **Logger**: SLF4J + Spring Boot Logging
- Non è consentito l'uso di `System.out.println()`

## Eccezioni

- `TicketNotFoundException`: Ticket non trovato (HTTP 404)
- `TicketValidationException`: Validazione fallita (HTTP 400)
- Exception Handler centralizzato con `@RestControllerAdvice`

## Testing

Test di integrazione in `src/test/java/com/qtm/ticket/`

Esecuzione:
```bash
mvn test
```
