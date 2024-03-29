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

QCC_HOME := /opt/qcc-qcc

currentdir := $(notdir $(shell pwd))
targetdir := /tmp/qoccido/$(currentdir)
bitcode := output.ll
bitcodepath := $(targetdir)/example/$(bitcode)
program := $(targetdir)/a.out
javafiles := $(shell find . -type f -name '*.java')
classfiles := $(patsubst %.java,%.class,$(javafiles))

javacmd := java
javadebugcmd += $(javacmd)
javadebugcmd += -agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=*:4004

qcc-repo := $(HOME)/.m2/repository/cc/quarkus
qcc-jar-api := $(qcc-repo)/qcc-runtime-api/1.0.0-SNAPSHOT/qcc-runtime-api-1.0.0-SNAPSHOT.jar
qcc-jar-main := $(qcc-repo)/qcc-main/1.0.0-SNAPSHOT/qcc-main-1.0.0-SNAPSHOT.jar
qcc-sources := $(shell find $(QCC_HOME)/ -type f -name '*.java')
compilecp := $(qcc-jar-api)
dependencies := $(shell find $(QCC_HOME)/main/target/libs -type f -name '*.jar')

noop=
space = $(noop) $(noop)
runcp := $(qcc-jar-main):$(subst $(space),:,$(dependencies))

run: $(program) copy-bc
> $(program)
.PHONY: run

$(program): example.jar
> $(call run,$(javacmd))

example.jar: $(classfiles)
> jar cvfe $@ example $^

%.class: %.java $(qcc-jar-api)
> javac -d . -classpath .:$(compilecp) $<

runjar: example.jar
> java -jar $<
.PHONY: run-jar

clean:
> rm -f *.class
> rm -f *.jar
> rm -f output.ll
> rm -drf $(targetdir)
> cd $(QCC_HOME)
> mvn clean
.PHONY: clean

debug:
> $(call run,$(javadebugcmd))
.PHONY: debug

$(qcc-jar-api): $(qcc-sources)
> cd $(QCC_HOME)
> mvn -DskipTests=true install
> mvn dependency:copy-dependencies -DoutputDirectory=target/libs

runbc: $(bitcode)
> lli $(bitcode)
.PHONY: run-bc

copy-bc:
> cp -f $(bitcodepath) .
.PHONY: copy-bc

$(bitcode): $(program) copy-bc

define run
$(1) \
  -classpath $(runcp) \
  cc.quarkus.qcc.main.Main \
  --boot-module-path example.jar:/opt/qccrt/qccrt-java.base-11.0.1-SNAPSHOT.jar:$(compilecp) \
  --output-path $(targetdir) \
  example
endef