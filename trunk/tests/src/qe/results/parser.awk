#$ awk -f parser.awk -v session=es -v size=100 -v len=1000 -v inners=50 -v outers=10 test.txt 
#$ awk -f parser.awk session=es inners=50 outers=10 test.txt 

# ** awk indicies start at 1. **

BEGIN { # "{" has to be on this line.
	if (session == "" || size == "" || len == "" || inners == "" || outers == "") {
		print "usage: awk -f parser.awk -v session=x -v inners=y -v outers=z [file]"
		exit 1
	}

	#print session " " size " " len " " inners " " outers

	#file = "qe-" session "_" size "_" len ".csv"
	
	#print file
	
	gsq_i = 1
	bq_i = 1
	ssq_i = 1
	qecq_i = 1
	qe_i = 1
	
	doParse = "false"
	
	fail = "false"
}

/LocalRun:/ {
	lheader = $1

	split($2, array, "=") 
	lsession = array[2]

	split($3, array, "=") 
	lchan = array[2]

	split($4, array, "=") 
	lsize = array[2]

	split($5, array, "=") 
	llen = array[2]

	split($6, array, "=") 
	louter = array[2]
	
	if (lsession == session && lsize == size && llen == len)
	{
		#print lheader " " lsession " " lchan " " lsize " " llen " " louter	
		
		doParse = "true"
	}
	else
	{
		doParse = "false"
	}
}

/QELocalRun:/ {
	lheader = $1

	split($2, array, "=") 
	lsession = array[2]

	lchan = "qe"

	split($3, array, "=") 
	lsize = array[2]

	split($4, array, "=") 
	llen = array[2]

	split($5, array, "=") 
	louter = array[2]
	
	if (lsession == session && lsize == size && llen == len)
	{
		#print lheader " " lsession " " lchan " " lsize " " llen " " louter	
		
		doParse = "true"
	}
	else
	{
		doParse = "false"
	}
}

/Run time:/ { 
	duration = $3

	#print duration
	
	if (doParse == "true")
	{
		if (lchan == "gsq")
		{
			gsq[gsq_i++] = duration
		}
		else if (lchan == "bq")
		{
			bq[bq_i++] = duration
		}
		else if (lchan == "ssq")
		{
			ssq[ssq_i++] = duration
		}	
		else if (lchan == "qecq")
		{
			qecq[qecq_i++] = duration
		}
		else if (lchan == "qe")
		{
			qe[qe_i++] = duration
		}
		else
		{
			fail = "true"
		
			print "Unknown channel flag: " lchan
			exit 1
		}
	}	
	
	#print duration
	
	#print trial " ; " count " ; " duration >> file
}

END {
	if (fail == "false") {
		drop = 5 # Drop the first x values from each outer run.
		count = 1
	
		#for (v in gsq) { # Unordered.
		for (o = 1; o <= outers; o++) {
			for (i = 1; i <= inners; i++) {
				if (i > drop) {
					{printf "%s,", gsq[count]}
					{printf "%s,", bq[count]}
					{printf "%s,", ssq[count]}
					{printf "%s,", qecq[count]}
					{printf "%s", qe[count]}

					print ""
				}

				count++
			}
		}
	}
}
