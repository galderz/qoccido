javafiles := Hello.java
classfiles := $(patsubst %.java,%.class,$(javafiles))

qcc: run-jar
	jbang ./qcc.java
.PHONY: qcc

run-jar: hello.jar
	java -jar $<
.PHONY: run

hello.jar: $(classfiles)
	jar cvfm $@ Manifest.txt $^

%.class: %.java
	javac -d . -classpath . $<

clean:
	rm -f *.class
	rm -f *.jar
.PHONY: clean
