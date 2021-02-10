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

gen-sources := $(shell find src/ -type f -name '*.java')
gen-jar := target/qoccinero.jar
gen-target := target/generated-sources

gen-main-name := Qoccido
gen-java-file-names += $(gen-main-name)
gen-java-file-names += Double_doubleToRawLongBits
gen-java-file-names += Float_floatToRawIntBits
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
javacmd += -Xss4m
javadebugcmd += $(javacmd)
javadebugcmd += -agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=*:4004

qcc-jar-api := $(QCC_HOME)/runtime/api/target/qcc-runtime-api-1.0.0-SNAPSHOT.jar
qcc-jar-main := $(QCC_HOME)/main/target/qcc-main-1.0.0-SNAPSHOT.jar
qcc-jar-unwind := $(QCC_HOME)/runtime/unwind/target/qcc-runtime-unwind-1.0.0-SNAPSHOT.jar
qcc-sources := $(shell find $(QCC_HOME)/ -type f -name '*.java')
compile-classpath := $(qcc-jar-api)
boot-classpath := $(compile-classpath):$(qcc-jar-unwind)

noop=
space = $(noop) $(noop)

run: $(program)
> $(program)
.PHONY: run

run-classpath:
> $(eval dependencies := $(shell find $(QCC_HOME)/main/target/libs -type f -name '*.jar'))
> $(eval runcp := $(qcc-jar-main):$(subst $(space),:,$(dependencies)))
.PHONY: run-classpath

$(program): $(gen-jar-path) run-classpath
> $(call run,$(javacmd))

$(gen-jar-path): $(gen-class-files)
> cd $(gen-target) && jar cvfe $(gen-jar-file) $(gen-main-name) $(notdir $(gen-class-files))

$(gen-class-files): $(gen-java-files) $(qcc-jar-api)
> javac -d $(gen-target) -classpath $(gen-target):$(compile-classpath) $<

$(gen-java-files): $(gen-sources)
> mvn compile exec:java -Dexec.mainClass=qoccinero.Qoccinero

runjar: $(gen-jar-path)
> java -jar $<
.PHONY: run-jar

clean:
> mvn clean
> rm -drf $(targetdir)
> cd $(QCC_HOME) && mvn clean
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
  -classpath $(runcp) \
  cc.quarkus.qcc.main.Main \
  --boot-module-path $(gen-jar-path):/opt/qccrt/qccrt-java.base-11.0.1-SNAPSHOT.jar:$(boot-classpath) \
  --output-path $(targetdir) \
  $(gen-main-name)
endef
