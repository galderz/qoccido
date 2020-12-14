main = Hello

sourcefiles = \
$(main).java

exec = $(shell echo $(main) | tr A-Z a-z)
jar = $(exec).jar

classfiles  = $(sourcefiles:.java=.class)

all: $(classfiles) jar run-jar qcc

%.class: %.java
	javac -d target -classpath . $<

clean:
	rm -f *.class || true
	rm -f $(exec) || true
	rm -f $(jar) || true
	rm -drf target || true

jar:
	cd target \
		&& jar cvfm $(jar) ../Manifest.txt $(classfiles)

run-jar:
	java -jar target/$(jar)

qcc:
	jbang ./qcc.java

qcc-debug:
	jbang --debug ./qcc.java

qcc-remote-debug:
	jbang --debug=*:4004 ./qcc.java
