# Console GovPay — Config.js esterno alla war

Meccanismo per servire il `Config.js` della console GovPay da un **file esterno alla war**,
sul volume `/etc/govpay`, senza ricostruire `govpay-console.war`.

> **Baked-in**: lo script sorgente è `commons/tomcat11/console-static-config.sh` e viene
> installato dall'immagine Tomcat 11 in `/docker-entrypoint-govpay.d/30-console-static-config.sh`
> (vedi `govpay/tomcat11/Dockerfile.govpay`). È quindi **attivo di default**: non serve montarlo
> a runtime. Disattivabile con `GOVPAY_CONSOLE_EXTERNAL_CONFIG=false`.

La dir esterna viene montata **dentro il context `/govpay-console`** (non un context separato),
così la URL resta sotto `/govpay-console/static/...`: compatibile con reverse proxy che già
inoltrano `/govpay-console/`, **senza regole proxy aggiuntive**.

Ad ogni avvio, prima di Tomcat:

1. **Seed Config.js** — se `/etc/govpay/static/govpay/web-console/assets/Config.js` non esiste,
   lo estrae da `govpay-console.war`; se esiste, usa quello del volume (le modifiche persistono).
2. **index.html** — estrae `index.html` dalla war e ne riscrive il tag
   `<script src="assets/Config.js">` in `<script src="static/govpay/web-console/assets/Config.js">`
   (src **relativo** → risolto dal browser sotto `<base href="/govpay-console/">` in
   `/govpay-console/static/...`). File derivato, rigenerato ad ogni avvio in
   `conf/govpay-console-override/` (fuori dal volume).
3. **Context console** — scrive `conf/Catalina/localhost/govpay-console.xml` che, sul context
   `/govpay-console`, aggiunge via `PreResources`:
   - `DirResourceSet` `base=/etc/govpay/static` → `webAppMount=/static` (serve la dir esterna
     sotto `/govpay-console/static/`, URL finale `/govpay-console/static/govpay/web-console/assets/Config.js`);
   - `FileResourceSet` che sovrappone l'`index.html` modificato (nessuna ricompattazione della war).

## Layout sul volume

```
/etc/govpay/
└── static/
    └── govpay/
        └── web-console/
            └── assets/
                └── Config.js      <-- editabile, persistente
```

## Uso con docker-compose

Basta un volume persistente su `/etc/govpay` (la hook è già nell'immagine):

```yaml
services:
  govpay:
    image: linkitaly/govpay:3.9.3        # adatta al tag/AS (tomcat11)
    volumes:
      - govpay_home:/etc/govpay          # persiste Config.js editato
    # environment:
    #   GOVPAY_CONSOLE_EXTERNAL_CONFIG: "false"   # per disattivare l'esternalizzazione
volumes:
  govpay_home:
```

## Note

- Il `Config.js` nel volume dev'essere il file **finale** (senza token `@...@`): bypassa l'installer.
- Gli `addScript('assets/config/...')` interni a Config.js restano relativi e sono serviti dalla
  war (context `/govpay-console`): invariati.
- Per rigenerare il Config.js dal war: cancellalo dal volume e riavvia il container.
- Per disattivare del tutto: `GOVPAY_CONSOLE_EXTERNAL_CONFIG=false` (la console torna a usare il
  Config.js interno alla war).
- Path validi per **immagine Tomcat 11** (`CATALINA_HOME=/usr/local/tomcat`,
  `GOVPAY_HOME=/etc/govpay`, `unzip` presente). Per wildfly25 andrebbe adattato/aggiunto a parte.
