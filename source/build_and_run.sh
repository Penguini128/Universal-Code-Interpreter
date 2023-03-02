javac -d bin src/*.java
cd ..
jar cvfe program.jar Main -C source/bin .
sh start.sh