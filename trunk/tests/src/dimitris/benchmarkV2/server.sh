DIR=`find . -depth 1 -type d -not -name classes`
if [ -z "$1" ]; then
    echo Please choose one of $DIR
    exit
fi

VERSION=Simple
if [ ! -z "$2" ]; then
    VERSION=$2
fi
CLIENTS=100
if [ ! -z "$3" ]; then
    CLIENTS=$3
fi

TRANSPORTS=
if [ "$1" = "SJE" ]; then
    TRANSPORTS="-Dsessionj.transports.session=a"
fi
sessionj -Djava.util.logging.config.file=../logging.properties -cp classes $TRANSPORTS sessionj.benchmarkV2.$1.ServerRunner $VERSION 2000 $CLIENTS
