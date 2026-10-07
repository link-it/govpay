# Svecchiamento del database GovPay

`svecchiamento-db.sh` cancella dal database di un'installazione GovPay i dati che hanno superato
il loro periodo di conservazione: giornale degli eventi, tracciati, metadati dei batch, pendenze
scadute, flussi di rendicontazione con le pendenze che rendicontano, pendenze pagate.

Lo svecchiamento è diviso in **sezioni**, una per area di dati. Ogni sezione è uno script SQL a sé,
in `sql/<dialetto>/svecchiamento/<sezione>.sql`, eseguibile anche da solo con il client del
database. Il comando compone le sezioni richieste, con la retention passata da fuori, e le esegue
con il client nativo del database.

> **La cancellazione è definitiva.** Prima della prima esecuzione fare un backup del database, e
> provare con `--dry-run` su una copia per vedere quanti dati vengono cancellati e in quanto tempo.

## Uso

```
./svecchiamento-db.sh <tipoDB> [opzioni]
```

`tipoDB` è l'unico argomento obbligatorio. Senza altre opzioni il comando esegue tutte le sezioni,
con la retention di default di ciascuna, chiedendo conferma prima di procedere.

```bash
cd src/main/resources/db

# Tutte le sezioni, con la retention di default
./svecchiamento-db.sh postgresql --host db.example.it --db govpay --user govpay

# Simulazione: esegue tutto e chiude ogni sezione con ROLLBACK
./svecchiamento-db.sh postgresql --host db.example.it --db govpay --user govpay --dry-run

# Solo flussi e pendenze pagate, salvando prima su file RT e flussi
./svecchiamento-db.sh postgresql --sezioni flussi_rendicontazione,pendenze_pagate \
    --export-rt /backup/rt --export-fr /backup/fr

# Compone lo script senza toccare il database, per rileggerlo o applicarlo a parte
./svecchiamento-db.sh oracle --solo-sql

# Esecuzione pianificata, senza conferma, con la connessione dalle variabili d'ambiente
GOVPAY_DB_SERVER=db.example.it:5432 GOVPAY_DB_NAME=govpay GOVPAY_DB_USER=govpay \
GOVPAY_DB_PASSWORD=... ./svecchiamento-db.sh postgresql -y
```

## Sezioni

Le sezioni vengono eseguite sempre in quest'ordine, comunque le si scriva in `--sezioni`.

| Sezione | Cosa cancella | Retention di default |
|---|---|---|
| `eventi` | Il giornale degli eventi, per età (`eventi.data`). | 3 mesi |
| `tracciati` | I tracciati di caricamento completati, con le operazioni e gli eventi collegati. | 1 mese |
| `spring-batch` | I metadati delle esecuzioni dei batch (`BATCH_*`), per data di fine, inizio o creazione dell'esecuzione. | 3 mesi |
| `pendenze_scadute_non_pagate` | Le pendenze `NON_ESEGUITO` scadute (`data_scadenza`), senza pagamenti né rendicontazioni, con tutto ciò che le riferisce. Restano i documenti. | 12 mesi |
| `flussi_rendicontazione` | I flussi di rendicontazione per `data_ora_flusso`, con rendicontazioni, eventi e incassi, e le pendenze che rendicontano, qualunque sia il loro stato. | 24 mesi |
| `pendenze_pagate` | Le pendenze pagate con ricevuta (`data_pagamento`) e mai rendicontate, con RPT, pagamenti e tutto ciò che le riferisce. | 24 mesi |

### flussi_rendicontazione

Per ogni flusso più vecchio della soglia cancella:

- gli eventi del flusso e le sue rendicontazioni;
- le pendenze che le rendicontazioni riferiscono, sulla voce o sul pagamento, **qualunque sia lo
  stato**: anche quelle non ancora riconciliate e quelle pagate senza RT. Di ogni pendenza viene
  cancellata tutta la struttura: pagamenti, RPT, voci, notifiche, notifiche App IO, promemoria,
  operazioni, stampe, allegati;
- l'incasso che riconcilia il flusso, con i suoi eventi;
- il flusso.

Una pendenza **resta** se è rendicontata anche in un flusso più recente della soglia (una revoca,
un'altra revisione del flusso, un pagamento multibeneficiario) o se ha un pagamento più recente
della soglia. Perde soltanto le rendicontazioni dei flussi vecchi; i suoi pagamenti restano,
scollegati dall'incasso cancellato (`id_incasso` a NULL), e fino a quando anche l'altro flusso non
supera la soglia risultano tra i pagamenti non rendicontati. Succede solo alle pendenze a cavallo
della soglia.

### pendenze_pagate

Cancella le pendenze che soddisfano tutte queste condizioni:

- stato di pagamento eseguito: `ESEGUITO`, `PARZIALMENTE_ESEGUITO`, `ESEGUITO_ALTRO_CANALE`,
  `ESEGUITO_SENZA_RPT`, `INCASSATO`;
- `data_pagamento` più vecchia della soglia. È valorizzata all'elaborazione della RT, quindi solo
  per le pendenze pagate con ricevuta;
- nessun pagamento più recente della soglia;
- nessuna rendicontazione: le pendenze rendicontate le cancella `flussi_rendicontazione`.

## Opzioni

### Argomento

| Argomento | Obbligatorio | Descrizione |
|---|---|---|
| `<tipoDB>` | sì | Dialetto del database: `postgresql`, `oracle`, `mysql`, `sqlserver`, `hsql`. Sono accettati gli alias `mariadb` (= `mysql`) e `hsqldb` (= `hsql`). |

### Sezioni e retention

| Opzione | Descrizione |
|---|---|
| `--sezioni <lista>` | Sezioni da eseguire, separate da virgola. Default: tutte. |
| `--retention-<sezione> <mesi>` | Retention in mesi di una sezione, con il nome come in `--sezioni`: `--retention-eventi`, `--retention-tracciati`, `--retention-spring-batch`, `--retention-pendenze_scadute_non_pagate`, `--retention-flussi_rendicontazione`, `--retention-pendenze_pagate`. La sezione deve essere tra quelle eseguite. Senza l'opzione vale il default scritto nello script della sezione. |

### Retention minima e `--force`

Per `flussi_rendicontazione` e `pendenze_pagate` la retention minima è di **24 mesi**. Una
retention inferiore, passata con l'opzione o scritta nello script della sezione, viene rifiutata a
meno di indicare anche:

| Opzione | Effetto |
|---|---|
| `--dry-run` | Simula, senza cancellare nulla. |
| `--force` | Cancella davvero. |

Il rifiuto vale anche con `--solo-sql`, perché lo script composto, eseguito, cancellerebbe quei dati.

```bash
# rifiutato
./svecchiamento-db.sh postgresql --sezioni pendenze_pagate --retention-pendenze_pagate 12
# ammessi
./svecchiamento-db.sh postgresql --sezioni pendenze_pagate --retention-pendenze_pagate 12 --dry-run
./svecchiamento-db.sh postgresql --sezioni pendenze_pagate --retention-pendenze_pagate 12 --force
```

### Connessione

Ogni parametro si passa con l'opzione o con la variabile d'ambiente corrispondente. Sono le stesse
variabili usate dai container GovPay, quindi un ambiente già configurato per quelli vale anche qui.
Servono solo per eseguire: con `--solo-sql` non sono necessari.

| Opzione | Variabile d'ambiente | Obbligatorio | Descrizione |
|---|---|---|---|
| `--host <host>` | `GOVPAY_DB_SERVER` (`host[:porta]`) | sì | Host del database. |
| `--port <porta>` | `GOVPAY_DB_SERVER` | no | Porta. Default: 5432 PostgreSQL, 3306 MySQL, 1521 Oracle, 1433 SQL Server, 9001 HSQLDB. |
| `--db <nome>` | `GOVPAY_DB_NAME` | sì | Database; su Oracle il service name o il SID. |
| `--user <utente>` | `GOVPAY_DB_USER` | sì | Utente. |
| `--password <segreto>` | `GOVPAY_DB_PASSWORD` | no | Password. Preferire la variabile d'ambiente: l'opzione resta nella cronologia della shell. |
| `--oracle-conn <tipo>` | `GOVPAY_ORACLE_JDBC_URL_TYPE` | no | Solo Oracle: `servicename` (default) o `sid`. |

Su HSQLDB si usano anche `GOVPAY_SQLTOOL_JAR` (percorso di `sqltool.jar`) e
`GOVPAY_DS_JDBC_LIBS` (directory con il driver JDBC), vedi i requisiti.

### Esecuzione

| Opzione | Descrizione |
|---|---|
| `--dry-run` | Simulazione: esegue le sezioni sul database e chiude ognuna con `ROLLBACK` invece di `COMMIT`. Riporta le righe che ogni `DELETE` cancellerebbe e non modifica nulla. Non chiede conferma e non salva file di export. Come nell'esecuzione vera, le righe interessate restano bloccate finché la sezione è aperta. |
| `--solo-sql` | Compone lo script e si ferma, senza connettersi al database. |
| `--force` | Vedi [Retention minima](#retention-minima-e---force). |
| `-y`, `--si` | Non chiede conferma. È la forma da usare in un'esecuzione pianificata: senza terminale la conferma non è possibile e il comando si ferma. |
| `--out <dir>` | Directory in cui scrivere lo script composto. Default: `target/svecchiamento-sql/` del repository. |
| `-h`, `--help` | Mostra l'aiuto. |

### Salvataggio su file prima della cancellazione

| Opzione | Sezioni | Descrizione |
|---|---|---|
| `--export-rt <dir>` | `flussi_rendicontazione`, `pendenze_pagate` | Salva l'XML di ogni RT delle pendenze cancellate in `<dir>`, un file `<cod_dominio>_<iuv>_<ccp>.xml` per RT. |
| `--export-fr <dir>` | `flussi_rendicontazione` | Salva l'XML di ogni flusso cancellato in `<dir>`, un file `<cod_dominio>_<cod_flusso>_<data_ora_flusso>.xml` per flusso, con la data nel formato `AAAAMMGGhhmmss` (le revisioni di un flusso hanno lo stesso codice). |

Almeno una delle sezioni indicate deve essere tra quelle eseguite. Le due directory possono
coincidere. Come funziona:

- i documenti vengono letti **dentro la transazione della sezione e prima delle `DELETE`**: quelli
  salvati sono esattamente quelli cancellati, e al `COMMIT` sono già su disco;
- li scrive il client del database, sulla macchina dove gira il comando, in un file grezzo
  (`<tipo>-esportati-<data>-<sezione>.hex`) da cui il comando ricava poi gli XML. Riuscita la
  conversione, il file grezzo viene cancellato; se non riesce resta, e contiene tutti i documenti;
- i caratteri del nome diversi da lettere, cifre, `.`, `_` e `-` diventano `_`: il ccp `n/a` diventa
  `n_a`;
- un file già presente con lo stesso contenuto non viene riscritto, quindi rieseguire dopo un errore
  è sicuro. Un file presente con contenuto diverso non viene sovrascritto, e il nuovo prende un
  suffisso (`-2`, `-3`, ...);
- i flussi senza XML non producono file e vengono contati a parte. Il batch FdR non salva l'XML dei
  flussi, quindi ne sono privi quelli acquisiti dalla 3.10 in poi.

## Requisiti

### Macchina che esegue il comando

| Requisito | Note |
|---|---|
| Bash 4 o successivo | Usa array associativi. Va bene anche Bash 4.2 (RHEL 7). |
| GNU sed, grep, awk; `mktemp`, `date`, `tr` | `sed -i` è usato nella forma GNU: su macOS (BSD sed) non funziona. |
| `perl` | Solo con `--export-rt` o `--export-fr`, per ricavare gli XML dal file grezzo. |
| La directory `src/main/resources/db/` del repository | Il comando cerca le sezioni in `sql/<dialetto>/svecchiamento/` accanto a sé. |
| Il client nativo del database | Vedi sotto. Con `--solo-sql` non serve. |
| Spazio su disco per gli export | Il file grezzo è in esadecimale, il doppio dei documenti, e resta finché non è convertito: al picco serve circa il triplo della dimensione di RT e flussi esportati. |

| Database | Client | Note |
|---|---|---|
| PostgreSQL | `psql` | |
| MySQL / MariaDB | `mysql` | |
| Oracle | `sqlplus` | `--export-rt` e `--export-fr` richiedono Oracle 12c o successivo. |
| SQL Server | `sqlcmd` | |
| HSQLDB | `java`, `sqltool.jar`, driver JDBC | Indicati con `GOVPAY_SQLTOOL_JAR` (default `/opt/hsqldb-<versione>/hsqldb/lib/sqltool.jar`) e `GOVPAY_DS_JDBC_LIBS` (default `/opt/jdbc-drivers`). |

### Utente del database

Il modo più semplice è usare l'utente con cui gira GovPay, proprietario dello schema. Altrimenti
servono:

- `DELETE` sulle tabelle delle sezioni eseguite, e `UPDATE` su `pagamenti` per
  `flussi_rendicontazione`;
- la possibilità di creare tabelle temporanee (su MySQL il privilegio `CREATE TEMPORARY TABLES`);
- su PostgreSQL, la proprietà dei large object degli allegati, per `lo_unlink`, e delle tabelle, per
  `VACUUM`. Senza la prima la sezione fallisce e viene annullata; senza la seconda PostgreSQL salta
  il `VACUUM` con un avviso.

## Comportamento

- **Ogni sezione è una transazione a sé.** Se una fallisce, quelle completate prima restano
  applicate e il comando lo segnala. Corretta la causa si può rieseguire: le sezioni cancellano per
  data, quindi rieseguire non ha effetti aggiuntivi.
- **Prima di eseguire, il comando chiede conferma** indicando utente, host e database, salvo con
  `-y` o `--dry-run`.
- **Lo script composto** resta nella directory di `--out`: è quello effettivamente eseguito, e
  documenta sezioni e retention usate.
- **Oracle:** `flussi_rendicontazione` e `pendenze_pagate` girano in una transazione
  `SERIALIZABLE`. Se nel frattempo l'applicazione modifica una delle righe da cancellare, la sezione
  fallisce con `ORA-08177` e viene annullata: basta rieseguirla.
- **Transazioni grandi:** la prima esecuzione su un'installazione che non è mai stata svecchiata può
  cancellare molti dati in una sola transazione. Per dividerla basta eseguire più volte con
  retention decrescenti, per esempio `--retention-pendenze_pagate 120`, poi `96`, `72`, ... fino a
  `24`.
