graphID <- function (sess_l, msg_size) {
	paste("len", sess_l, "_size", msg_size, sep="")
}

loadCsv <- function(filename) {
	csv <- read.csv(filename, header=F)
	data <- list()

	for (msg_size in unique(csv[[1]])) {
		forSize <- subset(csv, V1 %in% msg_size)
		for (sess_len in unique(forSize[[2]])) {
			id <- graphID(sess_len, msg_size)
			data[[id]] <- list()
			forLen <- subset(forSize, V2 %in% sess_len)
			for (nClients in forLen[[3]]) {
				data[[id]][[toString(nClients)]] <- t(as.vector(
					subset(forLen, V3 %in% nClients)[4:length(forLen)]
				))
			}
		}
	}
	data
}

loadAll <- function() {
	je <- loadCsv("je.csv")
	se <- loadCsv("se.csv")
	st <- loadCsv("st.csv")
	jt <- loadCsv("jt.csv")

	data <- list()
	for (n in names(je)) {
		forName <- list()
		forName$JE <- je[[n]]
		forName$SE <- se[[n]]
		forName$JT <- jt[[n]]
		forName$ST <- st[[n]]
		data[[n]] <- forName
	}
	
	data
}

m <- function(data) {
	sapply(data, mean) / 1e6
}

s <- function(data) {
	sapply(data, sd) / 1e6
}

clients = c(10,100,300,500,700,900)

drawMean <- function(data, params, main="") {
	d <- data[[params]]
	yRange <- range(m(d$SE),m(d$ST),m(d$JE),m(d$JT))
	myPlot(clients[1:length(d$ST)], m(d$ST), lab="Mean response time (ms)", main=main, yl=yRange, xl=range(clients))
	axis(1, at=clients, labels=clients)
	myLines(clients[1:length(d$JE)], m(d$JE), "green")
	myLines(clients[1:length(d$JT)], m(d$JT), "black")
	myLines(clients[1:length(d$SE)], m(d$SE), "red")
}

myLines <- function (x, y, col) {
	lines(x, y, type="o", pch=20, col=col)
}

myPlot <- function(x,y,xl,yl,lab,main) {
	plot(x, y, 
		type="o", ylab=lab, xlab=main, pch=20, sub=main,
		bty="n", ylim=yl, xlim=xl, xaxt="n", col="blue")
}

myLegend <- function(x,y) {
	legend(x, y, legend=c("ST", "JE", "JT", "SE"), col=c("blue", "green", "black", "red"), lty = 1, pch=20, bty="n", xpd=NA)
}

drawSd <- function(data, params, main="") {
	d <- data[[params]]
	yRange <- range(s(d$SE),s(d$ST),s(d$JE),s(d$JT))
	myPlot(clients[1:length(d$ST)], s(d$ST), main=main,
		lab="Response time standard deviation (ms)", yl=yRange, xl=range(clients))
	axis(1, at=clients, labels=clients)
	myLines(clients[1:length(d$JE)], s(d$JE), "green")
	myLines(clients[1:length(d$JT)], s(d$JT), "black")
	myLines(clients[1:length(d$SE)], s(d$SE), "red")
}

data <- loadAll()
par(mfrow=c(2,3), oma=c(0,0,2,0), mar=c(4,4,0,2))
drawMean(data, "len1_size100", main="Session length: 1, Message size: 100")
drawMean(data, "len10_size100", main="Session length: 10, Message size: 100")
drawMean(data, "len100_size100", main="Session length: 100, Message size: 100")
drawMean(data, "len1_size1024", main="Session length: 1, Message size: 1024")
drawMean(data, "len10_size1024", main="Session length: 10, Message size: 1024")
drawMean(data, "len100_size1024", main="Session length: 100, Message size: 1024")
#mtext("Number of clients", 3, outer=T)
myLegend(-2900, y=46000)

dev.new()
par(mfrow=c(2,3), oma=c(0,0,2,0), mar=c(4,4,0,2))
drawSd(data, "len1_size100", main="Session length: 1, Message size: 100")
drawSd(data, "len10_size100", main="Session length: 10, Message size: 100")
drawSd(data, "len100_size100", main="Session length: 100, Message size: 100")
drawSd(data, "len1_size1024", main="Session length: 1, Message size: 1024")
drawSd(data, "len10_size1024", main="Session length: 10, Message size: 1024")
drawSd(data, "len100_size1024", main="Session length: 100, Message size: 1024")
myLegend(-2900, y=9500)
