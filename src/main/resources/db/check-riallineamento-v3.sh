#!/bin/bash
#
# Verifica che la sezione "Migrazione dei dati al modello v3" sia identica in
# sql/<dialetto>/patch/3.11.0.sql e in sql/<dialetto>/migrazione/riallineamento-v3.sql
# (Issue #901).
#
# La migrazione gira due volte: con la patch, all'aggiornamento alla 3.11.0, e
# con lo script di riallineamento, al passaggio all'API v3. Il testo e' lo stesso
# e si mantiene nella patch: dopo ogni modifica va ricopiato nel riallineamento,
# dal marcatore fino alla fine del file. Questo controllo segnala le copie
# rimaste indietro.
#
# Uso:
#   ./check-riallineamento-v3.sh
#
set -euo pipefail

BASEDIR="$(cd "$(dirname "$0")" && pwd)"       # src/main/resources/db
MARCATORE='^-- Migrazione dei dati al modello v3$'
DIALETTI=(postgresql oracle mysql sqlserver hsql)

# Dalla riga di '=' che precede il marcatore fino alla fine del file.
function sezione() {
  awk -v m="${MARCATORE}" '
    { righe[NR] = $0 }
    $0 ~ m && inizio == 0 { inizio = NR - 1 }
    END { if (inizio > 0) for (i = inizio; i <= NR; i++) print righe[i] }
  ' "$1"
}

esito=0
for d in "${DIALETTI[@]}"; do
  patch="${BASEDIR}/sql/${d}/patch/3.11.0.sql"
  riall="${BASEDIR}/sql/${d}/migrazione/riallineamento-v3.sql"
  if [[ -z "$(sezione "${patch}")" ]]; then
    echo "KO  ${d}: marcatore assente in ${patch#${BASEDIR}/}"
    esito=1
  elif diff -q <(sezione "${patch}") <(sezione "${riall}") > /dev/null; then
    echo "OK  ${d}"
  else
    echo "KO  ${d}: ${riall#${BASEDIR}/} non e' allineato a ${patch#${BASEDIR}/}"
    diff <(sezione "${patch}") <(sezione "${riall}") | head -20
    esito=1
  fi
done
exit ${esito}
