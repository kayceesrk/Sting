##
# Set the current directory to the csv directory.
# FIXME: can factor a lot of this out with bmark1 to a common script
require(tikzDevice)  # Load the tikzDevice package


##
# Global parameters.
##
PREFIX  = 'bmark2-'
MODES   = c('NOALIASf', 'NOALIASm', 'ORDINARYf', 'ORDINARYm')
SIZES   = c('1', '2', '4', '8') # Tree depths.
LENGTHS = c('1', '10', '100', '1000')


##
# Global parameters for graph plotting.
##
PLOT_MODES = c('NOALIASf', 'ORDINARYf', 'NOALIASm', 'ORDINARYm')
#PLOT_MODES   = c('NOALIASf', 'NOALIASm', 'ORDINARYf', 'ORDINARYm')
#PLOT_COLOURS = c('red', 'blue', 'black', 'green')


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
# Organise the data for the thesis figures.
#
thesis_data <- function(data, scale=1)
{
	res <- list()
	for (size in SIZES)
	{
		for (length in LENGTHS)
		{
			graph <- matrix(nrow=length(PLOT_MODES), ncol=0)
			tmp <- NULL
			for (mode in PLOT_MODES)
			{
				tmp <- c(tmp, mean(data[[mode]][[size]][[length]]) / scale)
			}
			graph <- cbind(graph, tmp)
			colnames(graph) <- paste("Length", length)  # If no xlab is set on the plot, this seems to get used by default (but formatted as an axis name)
			rownames(graph) <- PLOT_MODES
			res[[size]][[length]] <- graph
		}
	}
	res
}

##
# Plot a single chart.
#
single_chart <- function(data, size, length, scale=1, units='nanos', level=0, doylab=T, doleg=F)
{
	res <- thesis_data(data, scale)
	tmp <- get_errors(data, res, size, length, scale, level)
	yvalues <- tmp[['yvalues']]
	errors <- tmp[['errors']]
	#x <- paste('Message Size ', size, ' B') 
	#title <- paste('Length ', length)
	xlab <- paste('Length ', length)
	ylab <- NULL
	legend <- NULL
	args <- NULL # For legend
	if (doylab == T)
	{
		ylab <- paste('Session duration (', units, ')', sep='')
	}
	if (doleg == T)
	{
		legend <- c(expression('NA'[sm]), expression('O'[sm]), expression('NA'[d]), expression('O'[d]))
		args <- list(x='topleft', bty='n', cex=1.2)  # For legend
	}
	#bp <- barplot(res[[size]][[length]], beside=TRUE, ylab=ylab, names.arg=NULL, legend.text=legend, args.legend=args)
	if (doleg == T)
	{
		ylim <- c(0, (max(res[[size]][[length]]) * 1.6))  # Needs a bit extra, tikzDevice seems to push the legend down to the level of the y-axis
	}
	else
	{
		ylim <- c(0, (max(res[[size]][[length]]) * 1.13))
	}
	bp <- barplot(res[[size]][[length]], beside=TRUE, ylab=ylab, names.arg=NULL, legend.text=legend, args.legend=args, ylim=ylim)
	if (doleg)
	{
		#legend(...)
	}
	if (level != 0)
	{
		error_bars(bp, unlist(yvalues), unlist(errors)) 
	}
	bp
}

get_errors <- function(data, res, size, length, scale, level)
{
	yvalues <- list()  # The height at which to draw each arrow bar
	errors <- list()   # The size of the arrow bar (in one direction)
	if (level != 0)
	{
		i <- 1  # Index for mode values inside each graph matrix
		for (mode in PLOT_MODES)
		{
			ci <- conf_int(data, mode, size, length, scale, level)
			errors <- c(errors, ci)
			yvalues <- c(yvalues, res[[size]][[length]][[i]])
			i <- i + 1
		}
	}
	list(yvalues=yvalues, errors=errors)
}


##
# Plot all charts for one message size in a row.
#
charts_for_size <- function(data, size, scale=1, level=0, units='nanos', doleg=T)
{
	par(mfrow=c(1,4))
	doylab <- T
	#doleg <- T
	for (length in LENGTHS)
	{
		if (length == LENGTHS[[length(LENGTHS)]])
		{
			#doleg <- T
		}
		single_chart(data, size, length, scale, units, level, doylab, doleg)
		doylab <- F
		doleg <- F
	}
}


##
#
#
test_tikz <- function(size='1', doleg=T)
{
	par(cex.lab=1.3, cex.axis=1.2)
	data <- load_all()
	charts_for_size(data, size, 1000000, 0.95, 'millis', doleg)
}

print_tikz <- function(justlegrow=F)
{
	# width, height: inches
	width <- 6.1
	height <- 2.55

	if (justlegrow == T)  # Only do chart row for size '1'
	{
		tmp <- paste('benchmark2-1.tex')
		tikz(tmp, standAlone=FALSE, width, height)

		test_tikz('1', T)

		dev.off() # Close the device
	}
	else
	{
		doleg <- T
		for (size in SIZES)
		{
			# The following will create normal.tex in the working
			# directory the first time this is run it may take a long time because the
			#	process of calulating string widths for proper placement is
			#	computationally intensive, the results will get cached for the current R
			# session or will get permenantly cached if you set
			# options( tikzMetricsDictionary='/path/to/dictionary' ) which will be
			# created if it does not exist.  Also if the flag standAlone is not set to
			# TRUE then a file is created which can be included with \include{}
			tmp <- paste('benchmark2-', size, '.tex', sep='')
			tikz(tmp, standAlone=FALSE, width, height)

			test_tikz(size, doleg)
			doleg <- F

			dev.off() # Close the device
			#tools::texi2dvi('benchmark1.tex', pdf=T) # Compile the tex file
			#system(paste(getOption('pdfviewer'),'normal.pdf')) # View it
		}
	}
}	


##
# Error bars for bar plots.
##
error_bars <- function(x, y, upper, lower=upper, length=0.05, ...)
{
	if (length(x) != length(y) | length(y) != length(lower) | length(lower) != length(upper))
	{
		stop("vectors must be same length: ")
	}
	arrows(x,y+upper, x, y-lower, angle=90, code=3, length=length, ...)
}


##
# Confidence interval. 0 < level < 1
##
conf_int <- function(data, mode, size, length, scale, level)
{
	d <- data[[mode]][[size]][[length]]
	m <- mean(d)
	s <- sd(d)
	n <- nrow(d)
	q <- qnorm(1 - ((1 - level) / 2))
	error <- q * s / sqrt(n)
	#list(lower=m - error, upper=m + error)
	error / scale
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

