# Schema e patch SQL: nomi dei dialetti fra i repository

Questo documento registra come si chiamano i dialetti di database nei repository GovPay e
come vanno messi in corrispondenza. Serve a chi scrive strumenti che raccolgono script SQL da
piu' repository: la corrispondenza non e' sempre per nome uguale.

Nessun repository va rinominato per allinearsi: i nomi delle directory coincidono con valori
di configurazione presenti in installazioni gia' distribuite, e cambiarli imporrebbe una
modifica di configurazione ai deployment esistenti. La differenza si risolve in lettura,
con la tabella qui sotto.

## Nome canonico

Il nome canonico di ciascun dialetto e' quello usato da questo repository, e coincide con i
valori accettati da `govpay-docker/build_image.sh` e dall'installer
(`setup/core/installer/setup/antinstall-config.xml`):

| Canonico     | DBMS                        |
|--------------|-----------------------------|
| `postgresql` | PostgreSQL                  |
| `oracle`     | Oracle                      |
| `mysql`      | MySQL / MariaDB             |
| `sqlserver`  | SQL Server                  |
| `hsql`       | HyperSQL, sviluppo e test   |

`mariadb` non e' un dialetto a se': usa gli script `mysql`. Gli script di inizializzazione dei
batch applicano questa equivalenza esplicitamente.

## Corrispondenza per repository

Directory dei dialetti effettivamente presenti, sotto la radice SQL indicata.

| Repository               | Radice SQL                    | Dialetti                                                | Note                     |
|--------------------------|-------------------------------|---------------------------------------------------------|--------------------------|
| `govpay`                 | `src/main/resources/db/sql`   | postgresql, oracle, mysql, sqlserver, `hsql`            | riferimento              |
| `govpay-console-api`     | `src/main/resources/db/sql`   | postgresql, oracle, mysql, sqlserver, `hsql`            | allineato                |
| `govpay-aca-batch`       | `src/main/resources/sql`      | postgresql, oracle, mysql, sqlserver, `hsql`            | allineato                |
| `govpay-fdr-batch`       | `src/main/resources/sql`      | postgresql, oracle, mysql, sqlserver, **`hsqldb`**      | vedi deroga              |
| `govpay-iban-batch`      | `src/main/resources/sql`      | postgresql, oracle, mysql, sqlserver, **`hsqldb`**      | vedi deroga              |
| `govpay-maggioli-jppa`   | `src/main/resources/sql`      | postgresql, oracle, mysql, sqlserver, **`hsqldb`**      | vedi deroga              |
| `govpay-notify-batch`    | `src/main/resources/sql`      | postgresql, oracle, mysql, sqlserver, **`hsqldb`**      | vedi deroga              |
| `govpay-rt-batch`        | `src/main/resources/sql`      | postgresql, oracle, mysql, sqlserver, **`hsqldb`**      | vedi deroga              |
| `govpay-tracciati-batch` | `src/main/resources/sql`      | postgresql, oracle, mysql, sqlserver, **`hsqldb`**      | vedi deroga              |

Due radici SQL diverse: `db/sql` in questo repository e in `govpay-console-api`,
`sql` nei batch. Anche questa differenza va gestita in lettura.

## Deroga: hsqldb

Sei repository usano `hsqldb` dove il canonico e' `hsql`. La deroga e' mantenuta
deliberatamente e non va corretta con una rinomina, perche' il nome della directory non e'
solo un nome: in `docker/commons/init_<modulo>_db.sh` la directory viene scelta con

```sh
SQL_DIR="${GOVPAY_DB_TYPE}"
[ "${GOVPAY_DB_TYPE}" == "mariadb" ] && SQL_DIR="mysql"
```

cioe' la directory **e'** il valore della variabile d'ambiente. Rinominarla richiederebbe di
cambiare `GOVPAY_DB_TYPE` in tutti i deployment che valgono `hsqldb`, oppure di introdurre un
alias in sei script: in entrambi i casi si toccherebbe materiale gia' distribuito per una
differenza che riguarda solo il dialetto di sviluppo e test.

### Regola per gli strumenti

Chi raccoglie script SQL da piu' repository normalizza il nome in lettura:

| Trovato nel repository | Dialetto canonico |
|------------------------|-------------------|
| `hsql`                 | `hsql`            |
| `hsqldb`               | `hsql`            |
| `mariadb`              | `mysql`           |

La normalizzazione vale solo per la lettura. Nessuno strumento deve riscrivere i nomi nei
repository di origine.

## Nomi dei file, non allineati

Oltre al dialetto differiscono i nomi dei file con la stessa funzione. Anche questi vanno
risolti in lettura, non rinominati:

| Funzione                          | Nomi in uso                                  |
|-----------------------------------|----------------------------------------------|
| creazione schema del modulo       | `create-db.sql`, `create.sql`                |
| svuotamento dati                  | `delete.sql`, `delete-db.sql`                |
| eliminazione schema               | `drop.sql`, `drop-db.sql`                    |
| schema metadati Spring Batch      | `spring-batch/schema-<vendor>.sql`           |
| pulizia dati Spring Batch         | `spring-batch-cleanup.sql`                   |

Le prime tre voci non hanno un nome unico fra i repository; le ultime due si. Un raccoglitore
deve quindi cercare per funzione con piu' nomi ammessi, non per nome esatto, e non deve
assumere che i file esistano: la presenza varia per modulo.

Lo schema dei metadati di Spring Batch non e' piu' mantenuto nei repository dei batch: il
profilo maven `dist`, definito in `govpay-bom`, lo estrae verbatim da `spring-batch-core` e lo
deposita in `sql/spring-batch/` dentro `sql.zip`, quindi in `/opt/sql/spring-batch`
dell'immagine. Due conseguenze per chi legge quello SQL:

- i nomi dei vendor sono quelli di upstream, quindi `schema-hsqldb.sql` e
  `schema-mariadb.sql`, e non esiste `schema-hsql.sql`: la normalizzazione dei dialetti serve
  anche qui;
- accanto agli `schema-<vendor>.sql` arrivano, perche' il profilo estrae `schema-*.sql` e
  `migration/**` senza distinguere, anche gli `schema-drop-<vendor>.sql` e l'albero
  `migration/`. Non vanno inclusi in uno script di installazione.

I repository passati al profilo `dist` portano inoltre un `sql/VERSION` con le versioni di
progetto, `spring-batch` e `govpay-common` con cui lo SQL e' stato prodotto. Con lo schema
preso da upstream, due componenti hanno lo stesso schema dei metadati se e solo se dichiarano
la stessa versione della libreria: e' il modo piu' diretto per verificarlo.

Fino a quando tutte le immagini non sono ricostruite restano in circolazione artefatti con la
forma precedente, `<dialetto>/tabelle_batch-create.sql` accanto a
`<dialetto>/tabelle_batch-drop.sql`. Un raccoglitore deve accettare entrambe le forme e
preferire quella upstream.

| Repository               | creazione                       | svuotamento      | eliminazione   |
|--------------------------|---------------------------------|------------------|----------------|
| `govpay-aca-batch`       | `create.sql` e `create-db.sql`  | assente          | assente        |
| `govpay-fdr-batch`       | `create.sql`                    | `delete.sql`     | `drop.sql`     |
| `govpay-iban-batch`      | `create-db.sql`                 | `delete.sql`     | `drop.sql`     |
| `govpay-maggioli-jppa`   | `create-db.sql`                 | `delete-db.sql`  | `drop-db.sql`  |
| `govpay-rt-batch`        | `create-db.sql`                 | assente          | assente        |
| `govpay-tracciati-batch` | assente                         | assente          | assente        |
| `govpay-notify-batch`    | assente                         | assente          | assente        |

`govpay-tracciati-batch` e `govpay-notify-batch` non definiscono struttura propria: la sola
struttura che portano e' quella dei metadati di Spring Batch, che dal passaggio al profilo
`dist` non e' piu' nel loro albero sorgente ma viene estratta dalla libreria in fase di
packaging. `govpay-aca-batch` contiene entrambi i nomi per la creazione e definisce una vista
sulle tabelle del core anziche' tabelle proprie.

## Raccolta dello SQL di un rilascio

`collect-release-sql.sh` compone lo SQL di un rilascio in un unico script per
dialetto, che la pipeline allega alla GitHub Release del core come
`govpay-sql-<tag>.zip`.

Le versioni dei componenti stanno in `release-components.env`, versionato: va
aggiornato nello stesso commit che porta la versione del core, cosi' la
composizione di ogni rilascio resta registrata in git e rivedibile in una pull
request. Una versione vuota significa "non fa parte di questo rilascio".

### Rilascio o sviluppo

Il valore di ciascun componente puo' essere di tre forme, perche' i casi
sono diversi:

| Valore | Da dove prende lo SQL | Quando |
|---|---|---|
| `<tag>` | asset **`sql.zip`** della GitHub Release | rilascio: l'artefatto pubblicato non cambia piu' |
| `image:<rif>` | `/opt/sql` dell'**immagine docker** | sviluppo: e' la forma da preferire |
| `branch:<nome>` | archivio del branch, l'albero sorgente | quando non c'e' ne' rilascio ne' immagine |

In pratica sul branch di sviluppo del core i valori sono `image:<tag>` delle
immagini dev, e nel commit che prepara il rilascio diventano i tag dei rilasci.

`image:` e' preferibile a `branch:` in sviluppo per due ragioni: lo SQL e'
quello che accompagna il binario effettivamente in esecuzione, e la versione e'
esplicita nel tag invece di essere lo stato mutevole di un branch. Un
riferimento senza `/` viene espanso come `linkitaly/govpay-<nome>-dev:<tag>`,
la stessa convenzione usata per le immagini dev del core; il prefisso si cambia
con `DOCKER_DEV_PREFIX` e il percorso interno con `DOCKER_SQL_PATH`.

Uno script che contiene componenti presi da un branch **non e' riproducibile**,
e lo dichiara nella propria intestazione. Anche la provenienza da immagine e'
annotata, con il riferimento completo.

Nessuna forma richiede autenticazione verso GitHub: i repository
`link-it/govpay*` sono pubblici, quindi bastano `curl`, `unzip` e `tar`, e
`curl` non serve affatto se tutti i componenti arrivano da immagini. `GH_TOKEN`,
se presente nell'ambiente, alza soltanto il limite di richieste dell'API
GitHub, che in anonimo e' di 60 all'ora per indirizzo IP.

Non tutte le immagini contengono lo SQL: quella di `console-api` ha `/opt/sql`
vuota, perche' il suo Dockerfile non lo copia. In quel caso il componente viene
saltato con una nota, come per un rilascio senza `sql.zip`.

Un riferimento inesistente, tag o branch, e' un **errore**: tipicamente una
versione sbagliata nel file, e proseguire produrrebbe uno script a cui manca un
componente senza che si veda. Un rilascio che esiste ma non ha `sql.zip` e'
invece legittimo: il componente viene saltato, annotandolo nel riepilogo e
nell'intestazione dello script prodotto.

Ordine di concatenazione, che non e' arbitrario:

1. **il core**, perche' definisce le tabelle su cui gli altri poggiano — per
   esempio `create-db.sql` di `aca-batch` crea viste su `versamenti`;
2. **le tabelle di Spring Batch**, una volta sola;
3. **i componenti**, in ordine alfabetico.

Quattro modalita':

| `--mode` | Sezione core | Usata da |
|---|---|---|
| `install` | baseline `gov_pay.sql` | rilascio, installazione da zero |
| `upgrade` | `patch/<versione>.sql` | rilascio, aggiornamento di un'installazione |
| `both` | entrambe, in due script | il job `release` della pipeline GitHub |
| `componenti` | **nessuna** | `jenkins.install.sh`, dove il core lo applica l'installer |

La modalita' `componenti` esiste perche' `prepareSetup.sh` copia
`src/main/resources/db/sql/*` dentro l'installer: `dist/sql/gov_pay.sql` e' lo
stesso file che il raccoglitore userebbe come sezione core, e applicarli
entrambi duplicherebbe lo schema. Lasciando il core all'installer, inoltre, la
testsuite installa dall'artefatto prodotto e non dai sorgenti.

In `jenkins.install.sh` questa modalita' sostituisce i file che erano copiati a
mano in `/etc/govpay/docker/<versione>/sql/` — `batch-aca.sql`, `batch-fdr.sql`
e `tabelle_batch-create.sql` — che stavano fuori da git, su una macchina.

### Lo schema dei metadati di Spring Batch

Concatenarlo ingenuamente lo emetterebbe una volta per componente e il secondo
`CREATE TABLE` farebbe fallire lo script, quindi viene deduplicato. Il confronto
e' sul contenuto normalizzato, perche' alcune copie differiscono solo per il
newline finale e sarebbe rumore.

Il raccoglitore lo cerca in due forme, in questo ordine:

1. `sql/spring-batch/schema-<vendor>.sql`, estratto da `spring-batch-core` dal
   profilo `dist`;
2. `sql/<dialetto>/tabelle_batch-create.sql`, la copia che i repository
   mantenevano a mano.

La prima ha la precedenza, perche' e' la sorgente autorevole: un componente che
avesse entrambe usa quella. La seconda resta supportata finche' sono in
circolazione immagini anteriori al passaggio.

Con lo schema preso da upstream la deriva fra i componenti non e' piu' possibile
per costruzione, ma due componenti possono ancora pinnare versioni diverse della
libreria attraverso `govpay-bom`, e in quel caso gli schemi divergono di nuovo.
Il raccoglitore lo rileva su due piani: confronta le versioni dichiarate in
`sql/VERSION`, e confronta il contenuto. Include la variante di **maggioranza** e
segnala le altre, sia a schermo sia come commento nello script prodotto: dare a
un batch lo schema di un'altra versione del framework non puo' essere una scelta
silenziosa. Quando le due varianti hanno **origine** diversa — una upstream e una
copia locale — il commento lo dice esplicitamente, perche' in quel caso il
problema non e' il framework ma un'immagine da ricostruire.

Se **nessun** componente fornisce lo schema, il raccoglitore lo segnala a schermo
e nello script invece di omettere la sezione in silenzio. Il caso tipico e' un
componente indicato con `branch:<nome>`: da quando lo schema e' prodotto in fase
di packaging, nell'albero sorgente non c'e' piu', quindi da un branch non si
ottiene. Per i batch servono `image:<tag>` o il tag di un rilascio.

### File inclusi ed esclusi

Inclusi: `create-db.sql`, `create.sql` o `console-api-schema.sql` — cercati per
funzione, perche' i nomi non sono uniformi — e un solo schema dei metadati di
Spring Batch. Esclusi sempre, perche' in uno script di installazione
distruggerebbero dati o non c'entrano: `delete*.sql`, `drop*.sql`,
`tabelle_batch-drop.sql`, `spring-batch-cleanup.sql`,
`spring-batch-6.0-migration.sql`, `utils.sql`, e fra i file che arrivano da
upstream in `sql/spring-batch` gli `schema-drop-<vendor>.sql` e l'albero
`migration/**`.

La ricerca e' per file e non per directory: in almeno un repository esiste una
`hsql/` accanto alla `hsqldb/` tracciata, e fissare la directory per nome faceva
sparire il contenuto vero.

## Patch di aggiornamento fra due versioni

`build-upgrade-sql.sh` compone in un unico script tutte le patch necessarie a
portare un'installazione da una versione a un'altra:

```console
./build-upgrade-sql.sh <tipoDB> <versioneDA> <versioneA> [opzioni]
./build-upgrade-sql.sh postgresql 3.8.2 3.10.0
```

Le patch del core sono nominate con la versione a cui **portano**, quindi
aggiornare da X ad A significa applicare, in ordine di versione, tutte le `v`
con `X < v <= A`. La versione di partenza e' esclusa: chi e' alla 3.8.2 ha gia'
applicato `3.8.2.sql`.

L'ordinamento e' per versione e non lessicografico, perche' `3.9 < 3.9.2 <
3.10.0` e `3.1-rc1 < 3.1.1`: con un ordinamento alfabetico la 3.10.0 finirebbe
prima della 3.9.

### Cosa include e cosa no

Vengono considerate solo le patch il cui nome e' una versione, nella forma
`<cifre>[.<cifre>...][-rc<n>]`. Il filtro non e' pedanteria: nella stessa
directory convivono file che versioni non sono, per esempio
`oracle/patch/3.8_aca.sql`, che e' la patch di un componente depositata fra
quelle del core e non e' collocabile in un intervallo di versioni. Questi file
vengono elencati fra quelli **non inclusi**, a schermo e nell'intestazione dello
script, invece di essere scartati in silenzio.

Restano fuori anche:

- `patch/clienti/`, specifiche di singole installazioni;
- le directory di patch cumulative preconfezionate come `patch/3.8.2_to_3.9.2/`,
  che sono un'alternativa a questo script, non un suo ingrediente.

### Patch dei componenti aggiuntivi

Dopo quelle del core lo script aggiunge le patch dei componenti del rilascio,
cercate in `<dialetto>/patch/` dentro lo SQL che accompagna ciascun componente.
Lo SQL e' ottenuto invocando `collect-release-sql.sh`, quindi valgono le stesse
tre sorgenti e le versioni dichiarate in `release-components.env`: non c'e' una
seconda implementazione della lettura da immagine o da rilascio.

Un componente senza patch non e' un errore e viene annotato. Alla data di
scrittura **nessuno degli otto componenti ne ha**: la ricerca e' predisposizione,
e la convenzione attesa e' la stessa del core, cioe' un file per versione sotto
`<dialetto>/patch/`.

Se la lettura dello SQL dei componenti non riesce — docker assente, immagine non
disponibile, rete — lo script **non** si ferma: le patch del core sono l'esito
essenziale, e il motivo del fallimento e' nel log indicato a schermo. Con
`--senza-componenti` la ricerca si salta del tutto.

### Ordine di applicazione

1. le patch del **core**, in ordine di versione;
2. le patch dei **componenti**, in ordine di versione, perche' poggiano su
   strutture che le prime possono creare o modificare.

L'intestazione dello script elenca le patch incluse nell'ordine in cui vanno
applicate, e avverte che non vanno riordinate.

### Controlli sugli estremi

Un intervallo vuoto o rovesciato e' quasi sempre un errore di invocazione, e
proseguire produrrebbe uno script vuoto che sembra valido: lo script si ferma se
`versioneDA` non precede `versioneA`, e se per `versioneA` non esiste una patch
nel dialetto indicato, elencando in quel caso le versioni disponibili. Se invece
e' `versioneDA` a non avere una patch, l'intervallo viene comunque calcolato per
confronto di versione ma con un avviso, perche' non e' verificabile da qui che
l'installazione sia davvero a quella versione.

## Copertura per dialetto

La presenza di un dialetto nell'elenco non implica che ogni patch esista per quel dialetto.
Nel solo `govpay`, le patch tracciate sono 17 per postgresql, oracle e mysql, 10 per sqlserver,
7 per hsql: i tre dialetti principali sono allineati, il divario e' su sqlserver e hsql.
Prima di considerare un dialetto supportato per una release va verificato che la patch di quella
release esista per tutti i dialetti dichiarati.

Il conteggio va fatto su `git ls-files` e non sul contenuto della directory: gli script preparati
per le consegne ai clienti non sono versionati e falserebbero il conto.
