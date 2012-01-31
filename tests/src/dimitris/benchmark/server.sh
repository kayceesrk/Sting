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
CLASS="sessionj.benchmark.$1.ServerRunner"
if [ "$1" = "TJava" ] || [ "$1" = "EJava" ]; then
    CLASS="ServerRunner"
fi
NEGOTIATION=-Dsessionj.transports.negotiation=m 
sessionj -ea -Xmx1024m $NEGOTIATION -Djava.util.logging.config.file=../logging.properties -cp classes $TRANSPORTS $CLASS $VERSION 2000 $CLIENTS 10000
