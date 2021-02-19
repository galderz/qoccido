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

gen-main-name := Qoccido
gen-jar := target/qoccinero.jar

MVN_JAVA_HOME := /opt/java-java
QCC_HOME := /opt/qcc-qcc

gen-sources := $(shell find src/ -type f -name '*.java')
gen-target := target/generated-sources

gen-java-file-names += $(gen-main-name)
gen-java-files := $(patsubst %,$(gen-target)/%.java,$(gen-java-file-names))
gen-class-files := $(patsubst %.java,%.class,$(gen-java-files))

gen-jar-file := qoccido.jar
gen-jar-path := $(gen-target)/$(gen-jar-file)

currentdir := $(notdir $(shell pwd))
targetdir := /tmp/qoccido/$(currentdir)
bitcode := output.ll
program := $(targetdir)/a.out

javacmd += java
# Big enough thread stack size to avoid https://github.com/quarkuscc/qcc/issues/262
javacmd += -Xss16m
javadebugcmd += $(javacmd)
javadebugcmd += -agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=*:4004

mvncmd += JAVA_HOME=$(MVN_JAVA_HOME)
mvncmd += mvn

qcc-jar-api := $(QCC_HOME)/runtime/api/target/qcc-runtime-api-1.0.0-SNAPSHOT.jar
qcc-jar-main := $(QCC_HOME)/main/target/qcc-main-1.0.0-SNAPSHOT.jar
qcc-jar-runtime-unwind := $(QCC_HOME)/runtime/unwind/target/qcc-runtime-unwind-1.0.0-SNAPSHOT.jar
qcc-jar-runtime-main := $(QCC_HOME)/runtime/main/target/qcc-runtime-main-1.0.0-SNAPSHOT.jar
qcc-jar-runtime-gc-nogc := $(QCC_HOME)/runtime/gc/nogc/target/qcc-runtime-gc-nogc-1.0.0-SNAPSHOT.jar
qcc-sources := $(shell find $(QCC_HOME)/ -type f -name '*.java')
qcc-cp-compile := $(qcc-jar-api)
qcc-boot-jars += $(qcc-cp-compile)
qcc-boot-jars += $(qcc-jar-runtime-unwind)
qcc-boot-jars += $(qcc-jar-runtime-main)
qcc-boot-jars += $(qcc-jar-runtime-gc-nogc)
qcc-cp-boot = $(subst $(space),:,$(qcc-boot-jars))
qcc-runtime-jars = $(shell find $(QCC_HOME)/main/target/libs -type f -name '*.jar')
qcc-cp-runtime = $(qcc-jar-main):$(subst $(space),:,$(qcc-runtime-jars))

noop=
space = $(noop) $(noop)

run: $(program)
> $(program)
.PHONY: run

$(program): $(gen-jar-path)
> $(call run,$(javacmd))

$(gen-jar-path): $(gen-class-files)
> cd $(gen-target) && jar cvfe $(gen-jar-file) $(gen-main-name) $(notdir $(gen-class-files))

$(gen-class-files): $(gen-java-files) $(qcc-jar-api)
> javac -d $(gen-target) -classpath $(gen-target):$(qcc-cp-compile) $<

$(gen-java-files): $(gen-sources)
> $(mvncmd) compile exec:java -Dexec.mainClass=qoccinero.Qoccinero

runjar: $(gen-jar-path)
> java -jar $<
.PHONY: run-jar

clean:
> $(mvncmd) clean
> rm -drf $(targetdir)
> cd $(QCC_HOME) && $(mvncmd) clean
.PHONY: clean

debug:
> $(call run,$(javadebugcmd))
.PHONY: debug

$(qcc-jar-api): $(qcc-sources)
> cd $(QCC_HOME)
> $(mvncmd) -DskipTests=true install
> $(mvncmd) dependency:copy-dependencies -DoutputDirectory=target/libs

define run
$(1) \
  -classpath $(qcc-cp-runtime) \
  cc.quarkus.qcc.main.Main \
  --boot-module-path $(gen-jar-path):/opt/qccrt/qccrt-java.base-11.0.1-SNAPSHOT.jar:$(qcc-cp-boot) \
  --output-path $(targetdir) \
  $(gen-main-name)
endef
