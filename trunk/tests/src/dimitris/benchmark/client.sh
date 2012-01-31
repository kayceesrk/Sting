DIR=`find . -depth 1 -type d -not -name classes`
if [ -z "$1" ]; then
    echo Please choose one of $DIR
    exit
fi

THREADS=1
if [ ! -z "$2" ]; then
    THREADS=$2
fi
VERSION=Simple
if [ ! -z "$3" ]; then
    VERSION=$3
fi
CLIENTS_PERTHREAD=10
if [ ! -z "$4" ]; then
    CLIENTS_PERTHREAD=$4
fi

NEGOTIATION_TR="-Dsessionj.transports.negotiation=m"
SESSION_TR="-Dsessionj.transports.session=s"
if [ "$1" = "SJE" ]; then
    SESSION_TR="-Dsessionj.transports.session=a"
fi
CLASS="sessionj.benchmark.$1.ClientRunner"
if [ "$1" = "TJava" ] || [ "$1" = "EJava" ]; then
    CLASS="ClientRunner"
fi
echo sessionj -Djava.util.logging.config.file=../logging.properties -cp classes $NEGOTIATION_TR $SESSION_TR $CLASS $VERSION 2000 localhost $THREADS $CLIENTS_PERTHREAD
sessionj -Djava.util.logging.config.file=../logging.properties -cp classes $NEGOTIATION_TR $SESSION_TR $CLASS $VERSION 2000 localhost $THREADS $CLIENTS_PERTHREAD
