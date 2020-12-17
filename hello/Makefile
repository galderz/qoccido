javafiles := hello.java
classfiles := $(patsubst %.java,%.class,$(javafiles))

classpath := $(HOME)/.m2/repository/cc/quarkus/qcc-runtime-api/1.0.0-SNAPSHOT/qcc-runtime-api-1.0.0-SNAPSHOT.jar

run: a.out
	./a.out
.PHONY: run

a.out: hello.jar
	jbang ../qcc.java

hello.jar: $(classfiles)
	jar cvfm $@ Manifest.txt $^

%.class: %.java
	javac -d . -classpath $(classpath) $<

run-jar: hello.jar
	java -jar $<
.PHONY: run-jar

clean:
	rm -f *.class
	rm -f *.jar
	rm -drf hello
	rm -f a.out
.PHONY: clean
