SHELL := bash
.ONESHELL:
.SHELLFLAGS := -eu -o pipefail -c
.DELETE_ON_ERROR:
MAKEFLAGS += --warn-undefined-variables
MAKEFLAGS += --no-builtin-rules

ifeq ($(origin .RECIPEPREFIX), undefined)
  $(error This Make does not support .RECIPEPREFIX. Please use GNU Make 4.0 or later)
endif
.RECIPEPREFIX = >

QCC_HOME := $(HOME)/1/qcc-qcc

javafiles := main.java
classfiles := $(patsubst %.java,%.class,$(javafiles))

classpath := $(HOME)/.m2/repository/cc/quarkus/qcc-runtime-api/1.0.0-SNAPSHOT/qcc-runtime-api-1.0.0-SNAPSHOT.jar

run: a.out
> ./a.out
.PHONY: run

a.out: main.jar
> jbang ../qcc.java

main.jar: $(classfiles)
> jar cvfe $@ main $^

%.class: %.java
> javac -d . -classpath $(classpath) $<

run-jar: main.jar
> java -jar $<
.PHONY: run-jar

clean:
> rm -f *.class
> rm -f *.jar
> rm -drf main
> rm -f a.out
.PHONY: clean

debug:
> jbang --debug=*:4004 ../qcc.java
.PHONY: debug

qcc:
> cd $(QCC_HOME)
> mvn -DskipTests=true clean install
.PHONY: qcc
