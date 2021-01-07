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

currentdir = $(notdir $(shell pwd))
targetdir = /tmp/qoccido/$(currentdir)
bitcode = output.ll
bitcodepath = $(targetdir)/example/Example/$(bitcode)
program = $(targetdir)/a.out
javafiles := example/Example.java
classfiles := $(patsubst %.java,%.class,$(javafiles))

qccrepo := $(HOME)/.m2/repository/cc/quarkus
compilecp := $(qccrepo)/qcc-runtime-api/1.0.0-SNAPSHOT/qcc-runtime-api-1.0.0-SNAPSHOT.jar
mainjar := $(qccrepo)/qcc-main/1.0.0-SNAPSHOT/qcc-main-1.0.0-SNAPSHOT.jar
dependencies = $(shell find $(QCC_HOME)/main/target/libs -type f -name '*.jar')

noop=
space = $(noop) $(noop)
runcp := $(mainjar):$(subst $(space),:,$(dependencies))

debug := -agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=*:4004

run: $(program)
> $(program)
.PHONY: run

$(program): example.jar
> $(call run)

example.jar: $(classfiles)
> jar cvfe $@ example.Example $^

%.class: %.java
> javac -d . -classpath $(compilecp) $<

runjar: example.jar
> java -jar $<
.PHONY: run-jar

clean:
> rm -f example/*.class
> rm -f *.jar
> rm -f output.ll
> rm -drf $(targetdir)
.PHONY: clean

debug:
> $(call run,$(debug))
.PHONY: debug

qcc:
> cd $(QCC_HOME)
> mvn -DskipTests=true clean install
> mvn dependency:copy-dependencies -DoutputDirectory=target/libs
.PHONY: qcc

runbc: $(bitcode)
> lli $(bitcode)
.PHONY: run-bc

$(bitcode): $(program)
> cp -f $(bitcodepath) .

define run
java \
  $(1) \
  -classpath $(runcp) \
  cc.quarkus.qcc.main.Main \
  --boot-module-path example.jar:/opt/qccrt/qccrt-java.base-11.0.1-SNAPSHOT.jar \
  --output-path $(targetdir) \
  example.Example
endef