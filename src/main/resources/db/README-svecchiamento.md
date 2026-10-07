# Svecchiamento del database GovPay

`svecchiamento-db.sh` cancella dal database di un'installazione GovPay i dati che hanno superato
il loro periodo di conservazione. È l'unico strumento di svecchiamento del repository.

Due regole:

- **giornale degli eventi e metadati dei batch** si cancellano e basta;
- **tutto il resto** (tracciati, audit, pendenze, flussi di rendicontazione, incassi, documenti)
  viene prima copiato in **tabelle di archivio giornaliere** `<tabella>_AAAAMMGG`, da salvare ed
  eliminare a cura del DBA (vedi [Archivio](#archivio)). Il backup è uno al giorno: un secondo lancio
  nello stesso giorno salta le sezioni con archivio.

Le anagrafiche (domini, applicazioni, operatori, tipi pendenza, configurazione, ...) non vengono mai
toccate.

> **La cancellazione è definitiva**, salvo quanto copiato nelle tabelle di archivio. Prima della
> prima esecuzione fare un backup del database, e provare con `--dry-run` su una copia per vedere
> quanti dati vengono cancellati e in quanto tempo.

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

# Solo eventi e metadati dei batch, con 15 giorni di retention
./svecchiamento-db.sh postgresql --sezioni eventi,spring-batch \
    --retention-eventi 15 --retention-spring-batch 15

# Compone lo script senza toccare il database, per rileggerlo o applicarlo a parte
./svecchiamento-db.sh oracle --solo-sql

# Esecuzione pianificata, ogni notte, senza conferma, con la connessione dalle variabili d'ambiente
GOVPAY_DB_SERVER=db.example.it:5432 GOVPAY_DB_NAME=govpay GOVPAY_DB_USER=govpay \
GOVPAY_DB_PASSWORD=... ./svecchiamento-db.sh postgresql -y
```

## Sezioni

Le sezioni vengono eseguite sempre in quest'ordine, comunque le si scriva in `--sezioni`. Le
retention sono in **giorni**.

| Sezione | Cosa cancella | Riferimento | Retention di default | Archivio |
|---|---|---|---|---|
| `eventi` | Il giornale degli eventi. | `eventi.data` | 30 | no |
| `tracciati` | I tracciati di caricamento completati, con le loro operazioni e i loro eventi. | `data_completamento` | 30 | sì |
| `spring-batch` | I metadati delle esecuzioni dei batch (`BATCH_*`). | fine, inizio o creazione dell'esecuzione | 30 | no |
| `tracciati_notifica_pagamenti` | I tracciati di notifica dei pagamenti (`trac_notif_pag`). | `data_creazione` | 30 | sì |
| `audit` | L'audit delle operazioni degli operatori (`gp_audit`). | `data` | 365 | sì |
| `pendenze_scadute_non_pagate` | Le pendenze `NON_ESEGUITO` scadute, senza pagamenti né rendicontazioni, con tutto ciò che le riferisce. | `data_scadenza` | 365 | sì |
| `pendenze_annullate` | Le pendenze `ANNULLATO`, senza pagamenti né rendicontazioni, con tutto ciò che le riferisce. | `data_ora_ultimo_aggiornamento` | 365 | sì |
| `flussi_rendicontazione` | I flussi di rendicontazione, con rendicontazioni, eventi e incassi, e le pendenze che rendicontano, qualunque sia il loro stato. | `data_ora_flusso` | 730 | sì |
| `pendenze_pagate` | Le pendenze pagate con ricevuta e mai rendicontate, con RPT, pagamenti e tutto ciò che le riferisce. | `data_pagamento` | 730 | sì |
| `documenti_orfani` | I documenti non più referenziati da pendenze, promemoria o stampe. | nessuno | — | sì |

"Tutto ciò che riferisce una pendenza" sono voci, RPT (con l'XML di RPT e RT), pagamenti,
notifiche, notifiche App IO, promemoria, stampe, operazioni e allegati.

### pendenze_annullate

GovPay non registra la data dell'annullamento, e l'annullamento da API o da tracciato non aggiorna
`data_ora_ultimo_aggiornamento`. Il riferimento è comunque quella colonna, la migliore disponibile:
una pendenza vecchia annullata di recente può essere svecchiata al primo giro. Resta nell'archivio.

### flussi_rendicontazione

Per ogni flusso più vecchio della soglia cancella:

- gli eventi del flusso e le sue rendicontazioni;
- le pendenze che le rendicontazioni riferiscono, sulla voce o sul pagamento, **qualunque sia lo
  stato**: anche quelle non ancora riconciliate e quelle pagate senza RT;
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

### documenti_orfani

Un documento raggruppa più pendenze, e le sezioni sulle pendenze non lo cancellano: potrebbe averne
altre. Questa sezione, che va per ultima, cancella quelli rimasti senza pendenze, promemoria e
stampe. Non ha retention: un documento non ha una data. Un documento è creato nella stessa
transazione della sua prima pendenza, quindi non è mai visibile orfano mentre nasce.

### spring-batch

Non c'è filtro sullo stato delle esecuzioni: è una scelta degli script di `govpay-common`, da cui
la sezione deriva, e serve a bonificare le esecuzioni rimaste appese. Un job ancora in corso,
creato prima della soglia, verrebbe cancellato.

## Archivio

Tutte le sezioni tranne `eventi` e `spring-batch` copiano le righe che cancellano in tabelle di
archivio, nello stesso schema, con il nome della tabella d'origine e la data dell'esecuzione:

```
versamenti_20261007   rpt_20261007   pagamenti_20261007   fr_20261007   gp_audit_20261007   ...
```

| Sezione | Tabelle archiviate |
|---|---|
| `tracciati` | `tracciati`, `operazioni` |
| `tracciati_notifica_pagamenti` | `trac_notif_pag` |
| `audit` | `gp_audit` |
| `pendenze_scadute_non_pagate`, `pendenze_annullate` | `versamenti`, `singoli_versamenti`, `rpt`, `notifiche`, `notifiche_app_io`, `promemoria`, `operazioni`, `stampe`, `allegati` |
| `flussi_rendicontazione` | `fr`, `rendicontazioni`, `incassi`, `pagamenti` e le tabelle delle pendenze, come sopra |
| `pendenze_pagate` | `pagamenti` e le tabelle delle pendenze, come sopra |
| `documenti_orfani` | `documenti` |

La tabella `eventi` non viene archiviata, neanche per gli eventi cancellati insieme a tracciati e
flussi.

- **Un backup al giorno.** Prima di eseguire, il comando controlla se nel database esiste già anche
  una sola tabella di archivio con la data di oggi. Se c'è, lo svecchiamento con archivio di oggi è
  già stato fatto, e **le sezioni con archivio vengono saltate**; `eventi` e `spring-batch`
  girano comunque. Il controllo vale anche per `--dry-run`; con `--solo-sql` non si può fare, e il
  comando lo segnala.
- **Dopo un'esecuzione fallita**, le tabelle di archivio di oggi possono esistere già: per riprovare
  in giornata il DBA deve salvarle ed eliminarle. In alternativa si riprova il giorno dopo.
- Le tabelle vengono **create all'inizio di ogni sezione**, con le colonne della tabella d'origine e
  senza chiavi esterne. Su MySQL sono create con `CREATE TABLE ... LIKE`, che copia anche indici e
  chiave primaria (`CREATE TABLE ... SELECT` non è ammessa con GTID attivo); sugli altri database
  nascono senza vincoli né indici.
- Le righe vengono copiate **nella stessa transazione della cancellazione**, con lo stesso criterio
  delle `DELETE` o sugli stessi id: l'archivio contiene esattamente le righe cancellate. Se la
  sezione fallisce e viene annullata, l'archivio resta come prima.
- **PostgreSQL:** `allegati.raw_contenuto`, `tracciati.zip_stampe` e `trac_notif_pag.raw_contenuto`
  sono large object, che la cancellazione elimina. Il loro contenuto viene copiato nella colonna
  aggiuntiva `<colonna>_dati` (`bytea`) dell'archivio; la colonna originale conserva solo l'OID, ormai
  non valido.
- **Simulazione:** con `--dry-run` le tabelle si chiamano `<tabella>_simAAAAMMGG`, restano vuote
  perché le transazioni vengono annullate, e vengono eliminate alla fine.
- Eseguito da solo, uno script di sezione archivia nelle tabelle `<tabella>_aaaammgg`: il suffisso
  è un segnaposto che `svecchiamento-db.sh` sostituisce con la data.

**Salvare ed eliminare le tabelle di archivio è compito del DBA.** Fino ad allora i dati svecchiati
continuano a occupare spazio nel database. Per trovarle, per esempio su PostgreSQL:

```sql
SELECT table_name FROM information_schema.tables WHERE table_name ~ '_[0-9]{8}$';
```

Per ripristinare i dati si reinseriscono le righe nelle tabelle d'origine nell'ordine delle chiavi
esterne: `documenti`, `incassi`, `fr`, `tracciati`, `versamenti`, `singoli_versamenti`, `rpt`,
`stampe`, `allegati`, `pagamenti`, `rendicontazioni`, `operazioni`, `notifiche`, `notifiche_app_io`,
`promemoria`; `trac_notif_pag` e `gp_audit` in qualsiasi momento. Su PostgreSQL i large object
vanno ricreati dalla colonna `_dati`, aggiornando l'OID.

## Opzioni

### Argomento

| Argomento | Obbligatorio | Descrizione |
|---|---|---|
| `<tipoDB>` | sì | Dialetto del database: `postgresql`, `oracle`, `mysql`, `sqlserver`, `hsql`. Sono accettati gli alias `mariadb` (= `mysql`) e `hsqldb` (= `hsql`). |

### Sezioni e retention

| Opzione | Descrizione |
|---|---|
| `--sezioni <lista>` | Sezioni da eseguire, separate da virgola. Default: tutte. |
| `--retention-<sezione> <giorni>` | Retention in giorni di una sezione, con il nome come in `--sezioni`: `--retention-eventi`, `--retention-tracciati`, `--retention-spring-batch`, `--retention-tracciati_notifica_pagamenti`, `--retention-audit`, `--retention-pendenze_scadute_non_pagate`, `--retention-pendenze_annullate`, `--retention-flussi_rendicontazione`, `--retention-pendenze_pagate`. `documenti_orfani` non ha retention. La sezione deve essere tra quelle eseguite. Senza l'opzione vale il default scritto nello script della sezione. |

### Retention minima e `--force`

Per `flussi_rendicontazione` e `pendenze_pagate` la retention minima è di **730 giorni**. Una
retention inferiore, passata con l'opzione o scritta nello script della sezione, viene rifiutata a
meno di indicare anche:

| Opzione | Effetto |
|---|---|
| `--dry-run` | Simula, senza cancellare nulla. |
| `--force` | Cancella davvero. |

Il rifiuto vale anche con `--solo-sql`, perché lo script composto, eseguito, cancellerebbe quei dati.

```bash
# rifiutato
./svecchiamento-db.sh postgresql --sezioni pendenze_pagate --retention-pendenze_pagate 365
# ammessi
./svecchiamento-db.sh postgresql --sezioni pendenze_pagate --retention-pendenze_pagate 365 --dry-run
./svecchiamento-db.sh postgresql --sezioni pendenze_pagate --retention-pendenze_pagate 365 --force
```

### Connessione

Ogni parametro si passa con l'opzione o con la variabile d'ambiente corrispondente. Sono le stesse
variabili usate dai container GovPay, quindi un ambiente già configurato per quelli vale anche qui.
Servono per eseguire e per il controllo dell'archivio di oggi: con `--solo-sql` non sono necessari.

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
| `--dry-run` | Simulazione: esegue le sezioni sul database e chiude ognuna con `ROLLBACK` invece di `COMMIT`. Riporta le righe che ogni `DELETE` cancellerebbe e non modifica i dati. Le tabelle di archivio sono create con il suffisso `simAAAAMMGG` ed eliminate alla fine. Non chiede conferma. Come nell'esecuzione vera, le righe interessate restano bloccate finché la sezione è aperta. |
| `--solo-sql` | Compone lo script e si ferma, senza connettersi al database. |
| `--force` | Vedi [Retention minima](#retention-minima-e---force). |
| `-y`, `--si` | Non chiede conferma. È la forma da usare in un'esecuzione pianificata: senza terminale la conferma non è possibile e il comando si ferma. |
| `--out <dir>` | Directory in cui scrivere lo script composto. Default: `target/svecchiamento-sql/` del repository. |
| `-h`, `--help` | Mostra l'aiuto. |

## Requisiti

### Macchina che esegue il comando

| Requisito | Note |
|---|---|
| Bash 4 o successivo | Usa array associativi. Va bene anche Bash 4.2 (RHEL 7). |
| GNU sed, grep, awk; `mktemp`, `date`, `paste` | `sed -i` è usato nella forma GNU: su macOS (BSD sed) non funziona. |
| La directory `src/main/resources/db/` del repository | Il comando cerca le sezioni in `sql/<dialetto>/svecchiamento/` accanto a sé. |
| Il client nativo del database | Vedi sotto. Con `--solo-sql` non serve. |

| Database | Client | Note |
|---|---|---|
| PostgreSQL | `psql` | |
| MySQL / MariaDB | `mysql` | |
| Oracle | `sqlplus` | |
| SQL Server | `sqlcmd` | Non ancora provato su un'istanza reale. |
| HSQLDB | `java`, `sqltool.jar`, driver JDBC | Indicati con `GOVPAY_SQLTOOL_JAR` (default `/opt/hsqldb-<versione>/hsqldb/lib/sqltool.jar`) e `GOVPAY_DS_JDBC_LIBS` (default `/opt/jdbc-drivers`). |

### Spazio nel database

Le tabelle di archivio stanno nello stesso database: finché il DBA non le salva e le elimina, i dati
svecchiati occupano spazio come prima. La prima esecuzione su un'installazione mai svecchiata può
copiare molti dati; per dividerla, eseguire su più giorni con retention decrescenti.

### Utente del database

Il modo più semplice è usare l'utente con cui gira GovPay, proprietario dello schema. Altrimenti
servono:

- `DELETE` sulle tabelle delle sezioni eseguite, e `UPDATE` su `pagamenti` per
  `flussi_rendicontazione`;
- la creazione di tabelle nello schema (`CREATE TABLE`), per le tabelle di archivio, e
  `INSERT` e `SELECT` su di esse; con `--dry-run` anche `DROP TABLE`;
- la lettura del catalogo (`information_schema`, `user_tables`, `sys.tables`) per il controllo
  dell'archivio di oggi;
- la possibilità di creare tabelle temporanee (su MySQL il privilegio `CREATE TEMPORARY TABLES`);
- su PostgreSQL, la proprietà dei large object, per leggerli (`lo_get`) e cancellarli
  (`lo_unlink`), e delle tabelle, per `VACUUM`.

## Comportamento

- **Ogni sezione è una transazione a sé.** Se una fallisce, quelle completate prima restano
  applicate e il comando lo segnala.
- **Prima di eseguire, il comando chiede conferma** indicando utente, host e database, salvo con
  `-y` o `--dry-run`.
- **Lo script composto** resta nella directory di `--out`: è quello effettivamente eseguito, e
  documenta sezioni e retention usate.
- **Oracle e HSQLDB:** la soglia di data è calcolata una volta per sezione, e gli id da cancellare
  sono raccolti una volta sola, così che archiviazione e cancellazione riguardino le stesse righe
  anche se l'esecuzione passa la mezzanotte.

## Funzionamento interno

Note per chi modifica il comando o gli script delle sezioni.

**Sezioni.** Ogni sezione è uno script SQL a sé in `sql/<dialetto>/svecchiamento/<sezione>.sql`,
eseguibile da solo con il client del database, con il proprio parametro di retention scritto
dentro. Il comando le compone in un unico script e lo esegue.

**Retention.** Il default di ogni sezione sta nel suo script e non è duplicato nel comando, che si
limita a leggerlo per riportarlo. Con `--retention-<sezione>` la riga del parametro viene
sostituita nell'idioma del dialetto (`\set`, `DEFINE`, `SET @`, `DECLARE @`, e su HSQLDB i letterali
`- N DAY`, perché HSQLDB non ha variabili negli script). La sostituzione è verificata due volte: che
la riga da sostituire ci sia, e che il valore chiesto compaia nel risultato. Uno script che
cambiasse idioma, senza il controllo, si eseguirebbe ignorando l'opzione.

**Archivio.** Una sezione archivia se il suo script contiene il segnaposto `_aaaammgg`: l'elenco
non è duplicato nel comando, che sostituisce il segnaposto con la data (o con `simAAAAMMGG` in
simulazione) e ricava dagli script i nomi delle tabelle da controllare.

**Simulazione.** Il comando sostituisce l'unico `COMMIT` che chiude ogni sezione con un
`ROLLBACK`, e verifica che ce ne sia esattamente uno: una simulazione che committasse sarebbe
l'errore peggiore possibile. Le righe cancellate le riportano psql e sqlcmd da soli, sqlplus con
`SET FEEDBACK ON`; su MySQL e HSQLDB il comando aggiunge dopo ogni `DELETE` una `SELECT` di
`ROW_COUNT()` o `DIAGNOSTICS(ROW_COUNT)`. Su PostgreSQL il `VACUUM ANALYZE` non viene eseguito, e su
HSQLDB ogni sezione si apre con `SET AUTOCOMMIT FALSE`.

**Client.** È quello nativo del dialetto, perché gli script usano i comandi del client per i
parametri e i messaggi. Ciascuno è invocato in modo da uscire con codice non nullo al primo errore:
`ON_ERROR_STOP` su psql, `-b` su sqlcmd, `WHENEVER SQLERROR EXIT SQL.SQLCODE ROLLBACK` su sqlplus,
dove il `ROLLBACK` serve perché l'uscita di sqlplus, per default, fa `COMMIT`. Su SQL Server le
sezioni sono separate da `GO`, perché ciascuna dichiara le proprie variabili.

**Metadati Spring Batch.** `spring-batch.sql` replica il corpo degli script di svecchiamento dei
metadati di `govpay-common`, che restano la versione di riferimento: se cambia la struttura delle
tabelle `BATCH_*` vanno allineati.
