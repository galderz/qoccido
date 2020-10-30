main = Hello

sourcefiles = \
$(main).java

exec = $(shell echo $(main) | tr A-Z a-z)
jar = $(exec).jar

classfiles  = $(sourcefiles:.java=.class)

all: $(classfiles) jar run-jar qcc

%.class: %.java
	javac -d . -classpath . $<

clean:
	rm -f *.class || true
	rm -f ${exec} || true

jar:
	jar cvfm $(jar) Manifest.txt $(classfiles)

run-jar:
	java -jar $(jar)

qcc:
	jbang ./qcc.java

qcc-debug:
	jbang --debug ./qcc.java
