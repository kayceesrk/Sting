#$ awk -f results.awk -v version=SE smtp-se-i50o10.txt

BEGIN {
    # First runs
    clients[0] = 10
    clients[1] = 100
    clients[2] = 300
    clients[3] = 500
    clients[4] = 700
    clients[5] = 900

    # SE High-clients run
    #clients[0] = 10000
    #clients[1] = 2000
    #clients[2] = 5000

    c = -1
    if (version == "") {
        print "usage: awk -f results.awk -v version=[SE | ST] file"
        exit 1
    }
    fileBase = "throughput-" version
}
/Parameters/ {
    split($5, array, "=")
    trial = array[2]
    if (trial > 10) {
        file = "/dev/null"
    } else if (trial == 0) {
        c += 1
        file = fileBase "-" clients[c] "clients.csv"
        print "trial ; count ; duration" >> file
    }
}
/Count duration/ { duration = $4 }
/Total count/ { 
    count = $4
    print trial " ; " count " ; " duration >> file
}
