##
# Set the current directory to the csv directory.
##


##
# Global parameters.
##
PREFIX  = 'bmark2-'
MODES   = c('NOALIASf', 'NOALIASm', 'ORDINARYf', 'ORDINARYm')
SIZES   = c('0', '1024')
LENGTHS = c('1', '10', '100', '1000')


##
# Load data from csv files.
##
load_all <- function() 
{
	data <- list()
	for (mode in MODES) 
	{
		data[[mode]] <- list()
		for (size in SIZES)
		{
			data[[mode]][[size]] <- list()
			for (length in LENGTHS)
			{	
				tmp <- load_csv(mode, size, length)
				data[[mode]][[size]][[length]] <- tmp
			}
		}
	}
	data
}

load_csv <- function(mode, size, length) 
{
	res <- read.csv(paste(PREFIX, mode, "-size_", size, "-len_", length, ".csv", sep=""), head=FALSE, sep=",")
	res
}


##
# Global parameters for graph plotting.
##
PLOT_MODES = c('NOALIASf', 'ORDINARYf', 'NOALIASm', 'ORDINARYm')
#PLOT_MODES   = c('NOALIASf', 'NOALIASm', 'ORDINARYf', 'ORDINARYm')
PLOT_COLOURS = c('red', 'blue', 'black', 'green')


##
# Line plot.
##
line_plot <- function(data, size) 
{
	first = TRUE
	#for (mode in PLOT_MODES) 
	for (i in c(1:length(PLOT_MODES)))
	{
		if (first)
		{
			first = FALSE
			plot(LENGTHS, unlist(sapply(data[[PLOT_MODES[[i]]]][[size]], mean)), type="o", col=PLOT_COLOURS[[i]])
		}
		else
		{
			lines(LENGTHS, unlist(sapply(data[[PLOT_MODES[[i]]]][[size]], mean)), col=PLOT_COLOURS[[i]])
		}
	}
}


##
# Single bar plot.
##
bar_plot <- function(data, size, length) 
{
	tmp <- list()
	for (mode in PLOT_MODES)
	{
		tmp[[mode]] <- c(mean(data[[mode]][[size]][[length]]))
	}
	barplot(unlist(tmp), names.arg=PLOT_MODES) #, axis.lty=1)
	tmp
}

   
##
# Error bars for bar plots.
##
error_bars <- function(x, y, upper, lower=upper, length=0.1, ...)
{
	if (length(x) != length(y) | length(y) != length(lower) | length(lower) != length(upper))
	{
		stop("vectors must be same length: ")
	}
	arrows(x,y+upper, x, y-lower, angle=90, code=3, length=length, ...)
}


##
# Bar plot all. Can be refactored a lot using matrices.
##
bar_plot_all <- function(data, size, level=0) 
{
	tmp <- list()
	for (mode in PLOT_MODES)
	{
		tmp[[mode]] <- c()
		for (length in LENGTHS)
		{
			tmp[[mode]] <- c(tmp[[mode]], mean(data[[mode]][[size]][[length]]))
		}
	}
	foo <- list()
	lowers <- list()
	uppers <- list()
	if (level != 0)
	{
		bar <- 1
		for (length in LENGTHS)
		{
			for (mode in PLOT_MODES)
			{
				ci <- conf_int(data, mode, size, length, level)
				#lowers <- c(lowers, ci$lower)
				#uppers <- c(uppers, ci$upper)
				lowers <- c(lowers, ci)
				foo <- c(foo, tmp[[mode]][[bar]])
			}
			bar <- bar + 1
		}
	}
	#res <- as.matrix(tmp[[PLOT_MODES[[1]]]])
	res <- matrix(0, length(tmp[[1]]), 0) # as.matrix does not work directly on tmp
	#for (i in c(2:length(PLOT_MODES)))
	for (mode in PLOT_MODES)
	{
		#res <- cbind(res, tmp[[PLOT_MODES[[i]]]]) 
		res <- cbind(res, tmp[[mode]]) 
	}
	bp <- barplot(t(res), beside=TRUE) #, axis.lty=1)
	if (level != 0)
	{
		error_bars(bp, unlist(foo), unlist(lowers)) #, unlist(uppers)) 
	}
	res
}


##
# Confidence interval. 0 < level < 1
##
conf_int <- function(data, mode, size, length, level)
{
	d <- data[[mode]][[size]][[length]]
	m <- mean(d)
	s <- sd(d)
	n <- nrow(d)
	q <- qnorm(1 - ((1 - level) / 2))
	error <- q * s / sqrt(n)
	#list(lower=m - error, upper=m + error)
	error
}


##
# ANOVA
##
my_anova <- function(data, size, length)
{
	rows <- nrow(data[[1]][[size]][[length]]) # Assume the same for all param. combinations
	means <- list()
	for (mode in MODES)
	{
		means[[mode]] <- mean(data[[mode]][[size]][[length]])
	}
	meanmean <- mean(unlist(means))
	ssa <- 0
	#for (mode in MODES)
	for (mode in names(means))
	{
		tmp <- means[[mode]] - meanmean
		ssa <- ssa + (tmp * tmp)
	}
	ssa <- rows * ssa
	sse <- 0
	for (mode in MODES)
	{
		for (i in c(1:rows))
		{
			v <- data[[mode]][[size]][[length]]$V1[[i]] # FIXME: V1 is hacky (use row/colnames)
			tmp <- v - means[[mode]]
			sse <- sse + (tmp * tmp)
		}
	}
	#means
	list(ssa=ssa, sse=sse)
}


##
# The main function (not really needed, user can call load_all directly).
##
main <- function()
{
	data <- load_all()
	data
}


##
# Call main function.
##
#main()

