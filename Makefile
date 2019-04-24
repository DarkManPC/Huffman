all: compile run 

compile: Main.java
		javac -g -d classes Main.java
		
run:
		java -classpath ./classes Main