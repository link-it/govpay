<p align="center">
<img src="https://www.link.it/wp-content/uploads/2025/01/logo-govpay.svg" alt="GovPay Logo" width="200"/>
</p>

# GovPay - Porta di accesso al sistema pagoPA

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=link-it_govpay&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=link-it_govpay)
[![Docker Hub](https://img.shields.io/docker/v/linkitaly/govpay?label=Docker%20Hub&sort=semver)](https://hub.docker.com/r/linkitaly/govpay)
[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://raw.githubusercontent.com/link-it/govpay/master/LICENSE)
[![Documentation](https://img.shields.io/badge/docs-latest-brightgreen.svg)](https://govpay.readthedocs.io/it/master/)

## Descrizione

GovPay è una piattaforma completa per l'integrazione di Enti Pubblici, Intermediari e Partner tecnologici alle componenti centrali del progetto pagoPA.

Fornisce:
- **Gestione Archivio Pagamenti in Attesa (APA)**
- **API REST** per integrazione con sistemi verticali
- **Portale di Backoffice** per configurazione e monitoraggio
- **Portale Cittadino** per pagamenti online
- **Integrazione nativa con pagoPA** (SANP 3.x, GPD, ACA)
- **Supporto multi-database** (PostgreSQL, MySQL/MariaDB, Oracle, SQL Server, HSQL)

## Requisiti

- **Database**: PostgreSQL, MySQL/MariaDB, Oracle o SQL Server
- **Java**: 21 (incluso nell'immagine Docker)
- **Risorse minime**: 2GB RAM, 2 CPU cores

## Quick Start

### 1. Pull dell'immagine

```bash
docker pull linkitaly/govpay:latest
```

### 2. Avvio con Docker Run

```bash
docker run -d \
  --name govpay \
  -p 8080:8080 \
  -e GOVPAY_DB_TYPE=postgresql \
  -e GOVPAY_DB_SERVER=postgres:5432 \
  -e GOVPAY_DB_NAME=govpay \
  -e GOVPAY_DB_USER=govpay \
  -e GOVPAY_DB_PASSWORD=govpay \
  -v /opt/govpay/log:/var/log/govpay \
  -v /etc/govpay:/etc/govpay \
  linkitaly/govpay:latest
```

L'applicazione sarà disponibile su:
- **Console Backoffice**: http://localhost:8080/govpay-backoffice-console
- **API Backoffice**: http://localhost:8080/govpay-api-backoffice
- **API Pagamento**: http://localhost:8080/govpay-api-pagamento
- **API PagoPA**: http://localhost:8080/govpay-api-pagopa

### 3. Docker Compose (consigliato)

```yaml
version: '3.8'

services:
  postgres:
    image: postgres:16-alpine
    environment:
      POSTGRES_DB: govpay
      POSTGRES_USER: govpay
      POSTGRES_PASSWORD: govpay
    volumes:
      - govpay-db:/var/lib/postgresql/data
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U govpay"]
      interval: 10s
      timeout: 5s
      retries: 5

  govpay:
    image: linkitaly/govpay:latest
    depends_on:
      postgres:
        condition: service_healthy
    ports:
      - "8080:8080"
    environment:
      # Database
      GOVPAY_DB_TYPE: postgresql
      GOVPAY_DB_SERVER: postgres:5432
      GOVPAY_DB_NAME: govpay
      GOVPAY_DB_USER: govpay
      GOVPAY_DB_PASSWORD: govpay

      # Logging
      GOVPAY_LOG_LEVEL: INFO

      # JVM Memory
      JAVA_OPTS: "-Xms512m -Xmx2g"
    volumes:
      - /var/log/govpay:/var/log/govpay
      - /etc/govpay:/etc/govpay
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:8080/govpay-api-backoffice/rs/basic/v1/info"]
      interval: 30s
      timeout: 10s
      retries: 3
      start_period: 120s

volumes:
  govpay-db:
```

Avvio dello stack:

```bash
docker-compose up -d
```

## Tag disponibili

- `latest`
- `3.8.1`, `3.8.1_postgres`, `3.8.1_mariadb`
- `3.8.0`, `3.8.0_postgres`, `3.8.0_mariadb`
- `3.7.8`, `3.7.8_postgres`, `3.7.8_mariadb`
- `3.7.7`, `3.7.7_postgres`, `3.7.7_mariadb`
- `3.7.6`, `3.7.6_postgres`, `3.7.6_mariadb`
- `3.7.5`, `3.7.5_postgres`, `3.7.5_mariadb`
- `3.7.4`, `3.7.4_postgres`, `3.7.4_mariadb`

Elenco completo: https://hub.docker.com/r/linkitaly/govpay/tags

## Configurazione

### Variabili d'Ambiente

#### Database

| Variabile | Descrizione | Default |
|-----------|-------------|---------|
| `GOVPAY_DB_TYPE` | Tipo database: `postgresql`, `mysql`, `mariadb`, `oracle`, `sqlserver` | `postgresql` |
| `GOVPAY_DB_SERVER` | Host e porta del database (`host:port`) | `localhost:5432` |
| `GOVPAY_DB_NAME` | Nome del database | `govpay` |
| `GOVPAY_DB_USER` | Username database | `govpay` |
| `GOVPAY_DB_PASSWORD` | Password database | - |

#### Logging

| Variabile | Descrizione | Default |
|-----------|-------------|---------|
| `GOVPAY_LOG_LEVEL` | Livello log: `TRACE`, `DEBUG`, `INFO`, `WARN`, `ERROR` | `INFO` |
| `GOVPAY_LOG_SQL` | Abilita log SQL | `false` |

#### JVM

| Variabile | Descrizione | Default |
|-----------|-------------|---------|
| `JAVA_OPTS` | Opzioni JVM | `-Xms512m -Xmx1g` |

#### Servizi pagoPA

| Variabile | Descrizione | Default |
|-----------|-------------|---------|
| `GOVPAY_PAGOPA_ENV` | Ambiente pagoPA: `prod`, `uat`, `dev` | `uat` |

### Volumi

| Path Container | Descrizione |
|----------------|-------------|
| `/var/log/govpay` | Directory dei log applicativi |
| `/etc/govpay` | Directory configurazione (`govpay.properties`) |
| `/usr/local/tomcat/conf/Catalina/localhost` | Context descriptor Tomcat |
| `/opt/govpay/upload` | Directory upload tracciati CSV |

### Porte

| Porta | Servizio |
|-------|----------|
| `8080` | HTTP Tomcat (tutte le API e console) |
| `8443` | HTTPS Tomcat (se configurato) |

## Configurazione Avanzata

### File govpay.properties

Per personalizzare la configurazione oltre le variabili d'ambiente, montare un file `govpay.properties`:

```bash
docker run -d \
  --name govpay \
  -v /percorso/locale/govpay.properties:/etc/govpay/govpay.properties \
  linkitaly/govpay:latest
```

Esempio `govpay.properties`:

```properties
# Configurazione Cluster
it.govpay.cluster.id=nodo1

# Configurazione Batch
it.govpay.batch.acquisizioneRendicontazioni.cron=0 */10 * * * ?
it.govpay.batch.recuperoRPTPendenti.cron=0 0 * * * ?

# Configurazione Email
it.govpay.email.host=smtp.example.com
it.govpay.email.port=587
it.govpay.email.username=govpay@example.com
it.govpay.email.password=password

# Configurazione AppIO
it.govpay.appio.enabled=true
it.govpay.appio.subscriptionKey=YOUR_SUBSCRIPTION_KEY
```

### Autenticazione

GovPay supporta multiple modalità di autenticazione:
- **Basic Authentication** (default)
- **LDAP**
- **SSL Client Certificate**
- **Header-based**
- **OAuth2 / OIDC**
- **SPID** (per cittadini)

Configurare l'autenticazione tramite:
1. Variabili d'ambiente (per configurazioni semplici)
2. File `govpay.properties` (per configurazioni avanzate)
3. Spring Security XML (per personalizzazioni complete)

### Esempio con SSL

```bash
docker run -d \
  --name govpay \
  -p 8443:8443 \
  -v /percorso/certificati:/opt/govpay/ssl \
  -e GOVPAY_SSL_ENABLED=true \
  -e GOVPAY_SSL_KEYSTORE=/opt/govpay/ssl/keystore.jks \
  -e GOVPAY_SSL_KEYSTORE_PASSWORD=changeit \
  linkitaly/govpay:latest
```

## Inizializzazione Database

Al primo avvio, GovPay richiede un database vuoto. Gli script SQL per l'inizializzazione sono inclusi nell'immagine:

```bash
# Per PostgreSQL
docker exec -i govpay cat /opt/govpay/sql/postgresql/gov_pay.sql | \
  psql -h localhost -U govpay -d govpay

# Per MySQL
docker exec -i govpay cat /opt/govpay/sql/mysql/gov_pay.sql | \
  mysql -h localhost -u govpay -p govpay
```

## Health Check

L'immagine include endpoint per verificare lo stato dell'applicazione:

```bash
# Health check applicazione
curl http://localhost:8080/govpay-api-backoffice/rs/basic/v1/info

# Health check database
curl http://localhost:8080/govpay-api-backoffice/rs/basic/v1/info
```

Risposta di esempio:

```json
{
  "versione": "3.8.2",
  "ambiente": "PROD",
  "build": "2025-12-10"
}
```

## Backup e Manutenzione

### Backup Database

```bash
# PostgreSQL
docker exec postgres pg_dump -U govpay govpay > backup_govpay_$(date +%Y%m%d).sql

# MySQL
docker exec govpay-mysql mysqldump -u govpay -p govpay > backup_govpay_$(date +%Y%m%d).sql
```

### Backup Configurazione

```bash
docker cp govpay:/etc/govpay ./backup_config_$(date +%Y%m%d)
```

### Aggiornamento Versione

```bash
# 1. Backup database
docker exec postgres pg_dump -U govpay govpay > backup_pre_upgrade.sql

# 2. Stop container
docker-compose down

# 3. Pull nuova versione
docker pull linkitaly/govpay:3.8.2

# 4. Aggiorna docker-compose.yml con nuova versione

# 5. Riavvio (applica patch SQL automaticamente)
docker-compose up -d

# 6. Verifica log
docker-compose logs -f govpay
```

## Troubleshooting

### Container non si avvia

```bash
# Verifica log
docker logs govpay

# Verifica connessione database
docker exec govpay cat /etc/govpay/govpay.properties | grep datasource
```

### Problemi di performance

```bash
# Aumenta memoria JVM
docker run -e JAVA_OPTS="-Xms1g -Xmx4g" linkitaly/govpay:latest

# Verifica utilizzo risorse
docker stats govpay
```

### Reset completo

```bash
# ATTENZIONE: Cancella tutti i dati
docker-compose down -v
docker volume rm govpay-db
docker-compose up -d
```

## Architettura Multi-Tier

### Deployment con Load Balancer

```yaml
version: '3.8'

services:
  nginx:
    image: nginx:alpine
    ports:
      - "80:80"
      - "443:443"
    volumes:
      - ./nginx.conf:/etc/nginx/nginx.conf
    depends_on:
      - govpay-1
      - govpay-2

  govpay-1:
    image: linkitaly/govpay:latest
    environment:
      GOVPAY_CLUSTER_ID: node1
      # ... altre configurazioni

  govpay-2:
    image: linkitaly/govpay:latest
    environment:
      GOVPAY_CLUSTER_ID: node2
      # ... altre configurazioni

  postgres:
    image: postgres:16-alpine
    # ... configurazione database
```

## Link Utili

- **Documentazione Completa**: https://govpay.readthedocs.io/
- **Repository GitHub**: https://github.com/link-it/govpay
- **Segnalazioni**: https://github.com/link-it/govpay/issues
- **Documentazione pagoPA**: https://docs.pagopa.it/
- **Docker Hub**: https://hub.docker.com/r/linkitaly/govpay

## Supporto

Per supporto e domande:
- **Issues GitHub**: https://github.com/link-it/govpay/issues
- **Discussioni**: https://github.com/link-it/govpay/discussions

## Licenza

Questo progetto è rilasciato sotto licenza GPL-3.0. Vedere il file [LICENSE](https://github.com/link-it/govpay/blob/master/LICENSE) per i dettagli.

Copyright (c) 2014-2025 [Link.it srl](http://www.link.it)
