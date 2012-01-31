DIRS="EJava SJE SJthread TJava"
for dir in $DIRS; do
    if [ -e $dir/ClientRunner.sj ]; then 
        sessionjc -sourcepath $dir -d classes $dir/ServerRunner.sj
        sessionjc -sourcepath $dir -d classes $dir/ClientRunner.sj 
    else
        javac -sourcepath $dir -d classes $dir/ClientRunner.java $dir/ServerRunner.java
    fi
done
