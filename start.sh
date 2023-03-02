if [ -e program.jar ]
then
	java -jar program.jar
else
	cd source
	javac -d bin src/*.java
	cd ..
	jar cfe program.jar Main -C source/bin .
	java -jar program.jar
fi