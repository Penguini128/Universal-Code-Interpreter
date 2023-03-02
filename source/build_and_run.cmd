javac -d bin src\*.java
cd ..
jar cvfe program.jar Main -C source\bin .
java -jar program.jar