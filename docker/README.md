# Immagini Docker di GovPay

Questa directory contiene tutto il necessario per costruire le immagini Docker di
GovPay. Prima era mantenuta nel repository separato `govpay-docker` (branch
`gp_newgen`) e le immagini venivano prodotte a mano; ora il contesto di build vive
qui, accanto al codice che ci finisce dentro, e la pipeline lo usa direttamente.

## Contenuto

| Percorso | Cosa e' |
|---|---|
| `build-images.sh` | Punto di ingresso usato dalla pipeline: costruisce e pubblica un insieme di immagini |
| `build_image.sh` | Costruisce **una** immagine. Portato da `govpay-docker`, invocato da `build-images.sh` |
| `govpay/Dockerfile.daFile` | Stage installer, a partire da un installer `.tgz` locale |
| `govpay/Dockerfile.github` | Stage installer, scaricando l'installer da una release GitHub |
| `govpay/tomcat11/Dockerfile.govpay` | Immagine finale su Tomcat 11 (default) |
| `govpay/wildfly25/` | Immagine finale su WildFly 25 |
| `commons/` | Script di entrypoint, configurazione datasource e init, per application server |
| `console/README.md` | Esternalizzazione di `Config.js` della console, agganciata all'avvio |
| `DOCKERHUB.md` | Descrizione pubblicata sulla pagina Docker Hub |

`buildcontext/` e `compose/` sono prodotti dagli script e sono ignorati da git.

## Insiemi di immagini

`build-images.sh` produce due insiemi, corrispondenti ai due stage della pipeline:

| Insieme | Repository | Immagini | Tag prodotti |
|---|---|---|---|
| `dev` | `linkitaly/govpay-dev` | postgresql | `linkitaly/govpay-dev:3.10.0-SNAPSHOT_postgres` |
| `release` | `linkitaly/govpay` | postgresql, oracle, mariadb, mysql, senza db | `linkitaly/govpay:3.10.0_postgres`, `..._oracle`, `..._mariadb`, `..._mysql`, `linkitaly/govpay:3.10.0` e con `--latest` anche `:latest` |

Le immagini di sviluppo e quelle di release stanno in **due repository distinti**,
quindi i tag non possono collidere e la versione compare nel tag cosi' com'e',
senza marcatori aggiuntivi. Il repository di default dipende dall'insieme, cosi'
un'invocazione manuale che dimentica `--image-base` non pubblica per sbaglio
un'immagine di sviluppo fra le release.

I suffissi per database sono quelli gia' in uso su Docker Hub: l'immagine senza
database non ha suffisso, quella PostgreSQL usa `_postgres` (non `_postgresql`),
le altre usano il nome del database. La convenzione e' implementata in
`build-images.sh` e replicata in `build_image.sh`, che la applica quando `-t` non
e' indicato.

## Uso nella pipeline

I job sono `docker_dev` e `docker_release` in
`.github/workflows/maven.yml`, la pipeline GitHub Actions. Il `Jenkinsfile` non
costruisce immagini: la sua testsuite di integrazione resta il gate sulla
qualita' del prodotto, la pubblicazione delle immagini vive su GitHub accanto
alla creazione della release.

Il discriminante e' il **riferimento git in costruzione**:

| Evento | Job | Risultato |
|---|---|---|
| push su `master` o su un branch `*.x` | `docker_dev` | immagine postgres in `linkitaly/govpay-dev` |
| push di un tag | `docker_release` | cinque immagini in `linkitaly/govpay` e `:latest` |
| push su un altro branch, pull request | nessuno | niente immagini |

Un push ordinario, anche su `master`, produce quindi un'immagine di **sviluppo**:
solo un tag produce le immagini stabili. E' il motivo per cui le due destinazioni
sono repository distinti.

`docker_release` usa una **matrice con un database per esecutore**. Cinque
immagini finali piu' i rispettivi stage installer non stanno nel disco di un solo
runner GitHub, che ne ha una quindicina di GB liberi contro i circa cinque per
immagine; in parallelo, inoltre, il tempo di un rilascio resta quello di una
singola immagine. Il tag `:latest` e' derivato dall'immagine senza database,
quindi lo aggiunge soltanto la voce `hsql` della matrice.

L'installer non viene ricostruito dai job docker: e' l'artefatto `govpay-installer`
prodotto dal job `build`, che esegue `prepareSetup.sh tomcat` e lo carica solo
quando i job docker gireranno davvero, per non pagare centinaia di MB di
artefatto su ogni branch di lavoro. La versione e' ricavata dal nome
dell'archivio, che e' la fonte autorevole per entrambe le cose e permette ai job
docker di non installare ne' Java ne' Maven.

Il login al registry e' fatto dai job, non dallo script, e richiede due
impostazioni sul repository GitHub:

| Nome | Tipo | Contenuto |
|---|---|---|
| `DOCKERHUB_USERNAME` | variabile | utente Docker Hub |
| `DOCKERHUB_TOKEN` | segreto | token di accesso con permesso di scrittura |

Opzionalmente `DOCKER_IMAGE_BASE` e `DOCKER_IMAGE_BASE_DEV` come variabili, per
spostare i repository di destinazione senza modificare il workflow.

## Uso manuale

Dopo un `mvn install` che abbia prodotto l'installer:

```bash
# insieme di sviluppo, senza pubblicare
./docker/build-images.sh \
  --version 3.10.0-SNAPSHOT \
  --installer src/main/resources/setup/target/govpay-installer-3.10.0-SNAPSHOT.tgz \
  --set dev

# insieme di release su un registry locale, con anteprima dei comandi
REGISTRY_PREFIX=localhost ./docker/build-images.sh \
  --version 3.10.0 \
  --installer src/main/resources/setup/target/govpay-installer-3.10.0.tgz \
  --set release --dry-run
```

`--dry-run` stampa i comandi senza eseguirli. `DOCKER_BIN` permette di usare un
wrapper, per esempio `DOCKER_BIN="sudo docker"` dove il demone richiede sudo.

Per una singola immagine si usa direttamente `build_image.sh`, invocabile da
qualsiasi directory:

```bash
./docker/build_image.sh -t linkitaly/govpay:3.10.0_oracle -v 3.10.0 \
  -l src/main/resources/setup/target/govpay-installer-3.10.0.tgz -d oracle
./docker/build_image.sh -h   # elenco completo delle opzioni
```

Entrambi gli script si spostano da soli nel contesto di build, dopo aver reso
assoluti i percorsi che gli si passano. E' una differenza rispetto alla versione
in `govpay-docker`, dove `build_image.sh` funziona solo se lanciato dalla propria
directory: la pipeline lo invoca dalla radice del workspace e passa l'installer
con un percorso relativo a quella.

## Limiti noti, ereditati da govpay-docker

- L'opzione `-j` di `build_image.sh` (installer scaricato dalla pipeline Jenkins)
  fa riferimento a `govpay/Dockerfile.jenkins`, che non esiste ne' qui ne' nel
  repository di origine: e' inutilizzabile. Nella pipeline non serve, perche'
  l'installer e' un file locale e si passa con `-l`.
- Le opzioni `-a aca` e `-a gde` fanno riferimento a `Dockerfile.govpay_aca` e
  `Dockerfile.govpay_gde`, anch'essi assenti nel repository di origine.
- Il blocco finale di `build_image.sh` genera un `compose/docker-compose.yaml` di
  esempio che richiede il driver JDBC copiato a mano. E' comodo in locale ed
  innocuo in pipeline, dove il file non viene usato.
- Ogni immagine richiede circa 5 GB fra stage installer e immagine finale. Sui
  runner GitHub il problema e' risolto dalla matrice, un database per esecutore
  effimero. In locale la pulizia e' a carico di chi lancia il build: gli script
  non rimuovono nulla, per non cancellare immagini estranee.

## Rapporto con govpay-docker

`govpay-docker` resta il posto in cui vivono le personalizzazioni per singolo
cliente, i compose di consegna e il materiale OpenShift. Quello che e' stato
portato qui e' solo il contesto di build dell'immagine di prodotto. Le modifiche
a entrypoint, configurazione dell'application server o Dockerfile vanno fatte qui
e, se servono anche la', riportate a mano: i due alberi non sono sincronizzati.
