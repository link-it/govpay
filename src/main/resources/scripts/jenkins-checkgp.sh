NOW=$(date +%s)
TIMEOUT=$((NOW+120))
PS=-1
OFF=0
while [ ! ${PS} -eq 1 ]
do
        NOW=$(date +%s)
        if [ ${NOW} -gt ${TIMEOUT} ]
        then
                echo "Timeout."
                break;
        fi
        sleep 1
        /usr/bin/curl --noproxy 127.0.0.1 -w "\n\nRETURNCODE:%{http_code}" http://127.0.0.1:8080/govpay/backend/api/backoffice/ >/tmp/sonda.log 2>&1
        PS=`grep "RETURNCODE:200" /tmp/sonda.log|wc -l`
done

if [ ${NOW} -gt ${TIMEOUT} ]
then
        exit 2
else
        exit 0
fi
