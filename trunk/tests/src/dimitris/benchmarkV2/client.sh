DIR=`find . -depth 1 -type d -not -name classes`
if [ -z "$1" ]; then
    echo Please choose one of $DIR
    exit
fi

VERSION=Simple
if [ ! -z "$2" ]; then
    VERSION=$2
fi
THREADS=1
if [ ! -z "$3" ]; then
    THREADS=$3
fi
CLIENTS_PERTHREAD=10
if [ ! -z "$4" ]; then
    CLIENTS_PERTHREAD=$4
fi

TRANSPORTS=
if [ "$1" = "SJE" ]; then
    TRANSPORTS="-Dsessionj.transports.session=a"
fi
sessionj -Djava.util.logging.config.file=../logging.properties -cp classes $TRANSPORTS sessionj.benchmark.$1.ClientRunner $VERSION 2000 localhost $THREADS $CLIENTS_PERTHREAD
