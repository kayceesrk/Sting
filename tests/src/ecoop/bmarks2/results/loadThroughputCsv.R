readVersion <- function(version) {
	tputList <- list()
	for (nclients in c("10","100","300","500","700","900")) {
		filename <- paste("throughput-", version, "-", nclients, "clients.csv", sep="")
		csvarray <- read.csv(filename, sep=";")
		tput <- csvarray$count / (csvarray$duration / 1e9)
		tputList[[paste("X",nclients,sep="")]] <- tput
	}
	tputList
}

tputSE <- readVersion("SE")
tputST <- readVersion("ST")