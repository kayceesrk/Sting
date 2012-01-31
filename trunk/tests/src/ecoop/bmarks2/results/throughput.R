#load("SMTPthroughput10-10000.rdata")
source("loadThroughputCsv.R")

remove_zeroes <- function(X) {
	newX <- c()
	for (e in X) {
		if (e != 0) {
			newX <- c(newX, e)
		}
	}
	newX
}

mean_nozeroes <- function(frame) {
	result <- c()
	for (series in frame) {
		result <- c(result, mean(remove_zeroes(series)))
	}
	result
}

sd_nozeroes <- function(frame) {
	result <- c()
	for (series in frame) {
		result <- c(result, sd(remove_zeroes(series)))
	}
	result
}

meanST <- mean_nozeroes(tputST)
sdST <- sd_nozeroes(tputST)

meanSE <- mean_nozeroes(tputSE)
sdSE <- sd_nozeroes(tputSE)
clientsSE <- c(10, 100, 300, 500, 700, 900)
clientsST <- c(10, 100, 300, 500, 700, 900)

yMarks = round(c(meanSE, meanST), 1)

out.range <- c(295, 345)
breakp <- out.range[1]
yMarksOrig <- yMarks
delta <- diff(out.range)
yMarks[yMarks>breakp] <- yMarks[yMarks>breakp] - delta
yMarksOrigCulled=sort(yMarksOrig)[-2][-2][-6][-6]#[-7]
yMarksCulled=sort(yMarks)[-2][-2][-6][-6]#[-7]
yrangeMeanOrig<-c(yMarksOrigCulled[1], range(yMarksOrigCulled)[2])

library(gplots)
library(plotrix)

fudge = 40
yrangeMeanDiff<-c(yrangeMeanOrig[1], yrangeMeanOrig[2]-delta)
yrangeMean<-c(yrangeMeanOrig[1]-11, yrangeMeanOrig[2]+5-delta)
xrange <- range(c(clientsSE, clientsST))

gplots::plotCI(x=clientsSE, y=meanSE - delta, uiw=sdSE, xlim=xrange, type="o", pch=20, gap=0, ylim=yrangeMean, col="blue", xlab="", ylab="", xaxt="n", yaxt="n", bty="n", sfrac=0.005)
gplots::plotCI(add=T, x=clientsST, y=meanST, xlim=xrange, uiw=sdST, type="p", pch=20, gap=0, ylim=yrangeMean, col="red", xlab="", ylab="", xaxt="n", sfrac=0.005)
lines(x=clientsST, y=meanST, lty=2, col="red")
axis(side=2, at=yrangeMeanDiff, labels=F, tick=T, lwd.ticks=0)
axis(side=1, at=xrange, labels=F, tick=T, lwd.ticks=0)
rug(yMarks, side=2, ticksize=-0.02)
axis.break(2, breakpos=breakp)
text(par("usr")[1]-12, yMarksCulled, adj=1, labels=yMarksOrigCulled, xpd=T, cex=0.6)

rug(clientsSE, side=1, ticksize=-0.02)
yLab <- rep(par("usr")[3] - 6, times=length(clientsSE))
#yLab <- yLab + 14*c(0,1,0,1,0,1,0,0,0)
text(clientsSE, yLab, srt=90, adj=0.6, labels=clientsSE, xpd=T, cex=0.6)

#title(main="SMTP macro-benchmark: Throughput", xlab="Number of clients", ylab="Throughput (msg / s)")
title(xlab="Number of clients", ylab="Throughput (msg / s)", line=2, cex.lab=0.7)
legend(list(x=10,y=250), legend=c("SE", "ST"), col=c("blue", "red"), lty = c(1,2), pch=20, bty="n", cex=0.8)

