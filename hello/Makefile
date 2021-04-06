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

gen-main-name := example.Example
gen-jar-path := example.jar

currentdir := $(notdir $(shell pwd))
targetdir := /tmp/qoccido/$(currentdir)
bitcode := output.ll
program := $(targetdir)/a.out
javafiles := $(shell find . -type f -name '*.java')
classfiles := $(patsubst %.java,%.class,$(javafiles))

javacmd := java
javadebugcmd += $(javacmd)
javadebugcmd += -agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=*:4004

qcc-jar-api := $(QCC_HOME)/runtime/api/target/qcc-runtime-api-1.0.0-SNAPSHOT.jar
qcc-jar-main := $(QCC_HOME)/main/target/qcc-main-1.0.0-SNAPSHOT.jar
qcc-jar-runtime-unwind := $(QCC_HOME)/runtime/unwind/target/qcc-runtime-unwind-1.0.0-SNAPSHOT.jar
qcc-jar-runtime-main := $(QCC_HOME)/runtime/main/target/qcc-runtime-main-1.0.0-SNAPSHOT.jar
qcc-jar-runtime-gc-nogc := $(QCC_HOME)/runtime/gc/nogc/target/qcc-runtime-gc-nogc-1.0.0-SNAPSHOT.jar
qcc-jar-runtime-posix := $(QCC_HOME)/runtime/posix/target/qcc-runtime-posix-1.0.0-SNAPSHOT.jar

qcc-sources := $(shell find $(QCC_HOME)/ -type f -name '*.java')
qcc-cp-compile := $(qcc-jar-api)
qcc-boot-jars += $(qcc-cp-compile)
qcc-boot-jars += $(qcc-jar-runtime-unwind)
qcc-boot-jars += $(qcc-jar-runtime-main)
qcc-boot-jars += $(qcc-jar-runtime-gc-nogc)
qcc-boot-jars += $(qcc-jar-runtime-gc-nogc)
qcc-boot-jars += $(qcc-jar-runtime-posix)

noop=
space = $(noop) $(noop)

run: $(program)
> $(program)
.PHONY: run

run-classpath:
> $(eval dependencies := $(shell find $(QCC_HOME)/main/target/libs -type f -name '*.jar'))
> $(eval qcc-cp-runtime := $(qcc-jar-main):$(subst $(space),:,$(dependencies)))
.PHONY: run-classpath

$(program): example.jar run-classpath
> $(eval qcc-cp-boot := $(subst $(space),:,$(qcc-boot-jars)))
> $(call run,$(javacmd))

example.jar: $(classfiles)
> jar cvfe $@ example $^

%.class: %.java $(qcc-jar-api)
> javac -d . -classpath .:$(qcc-cp-compile) $<

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

debug: run-classpath
> $(call run,$(javadebugcmd))
.PHONY: debug

$(qcc-jar-api): $(qcc-sources)
> cd $(QCC_HOME)
> mvn -DskipTests=true install
> mvn dependency:copy-dependencies -DoutputDirectory=target/libs

define run
$(1) \
  -classpath $(qcc-cp-runtime) \
  cc.quarkus.qcc.main.Main \
  --boot-module-path $(gen-jar-path):/opt/qccrt/qccrt-java.base-11.0.1-SNAPSHOT.jar:$(qcc-cp-boot) \
  --output-path $(targetdir) \
  $(gen-main-name)
endef