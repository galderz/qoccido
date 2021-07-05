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
VERSION := 0.1.0-SNAPSHOT

BYTEMAN_HOME := /opt/byteman
byteman-jar := $(BYTEMAN_HOME)/lib/byteman.jar

gen-main-name := example
gen-jar-path := example.jar

currentdir := $(notdir $(shell pwd))
qoccidodir := /tmp/qoccido
targetdir := $(qoccidodir)/$(currentdir)
bitcode := output.ll
program := $(targetdir)/a.out
javafiles := $(shell find . -type f -name '*.java')
classfiles := $(patsubst %.java,%.class,$(javafiles))

javacmd += java
# Big enough thread stack size to avoid https://github.com/quarkuscc/qcc/issues/262
javacmd += -Xss16m
javadebugcmd += $(javacmd)
javadebugcmd += -agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=*:4004
javaverbosecmd += $(javacmd)
javaverbosecmd += -Dorg.jboss.byteman.verbose=true

qcc-jar-api := $(QCC_HOME)/runtime/api/target/qbicc-runtime-api-$(VERSION).jar
qcc-jar-main := $(QCC_HOME)/main/target/qbicc-main-$(VERSION).jar
qcc-jar-runtime-unwind := $(QCC_HOME)/runtime/unwind/target/qbicc-runtime-unwind-$(VERSION).jar
qcc-jar-runtime-main := $(QCC_HOME)/runtime/main/target/qbicc-runtime-main-$(VERSION).jar
qcc-jar-runtime-gc-nogc := $(QCC_HOME)/runtime/gc/nogc/target/qbicc-runtime-gc-nogc-$(VERSION).jar
qcc-jar-runtime-posix := $(QCC_HOME)/runtime/posix/target/qbicc-runtime-posix-$(VERSION).jar
qcc-jar-runtime-deserialization := $(QCC_HOME)/runtime/deserialization/target/qbicc-runtime-deserialization-$(VERSION).jar

qcc-sources := $(shell find $(QCC_HOME)/ -type f -name '*.java')
qcc-cp-compile := $(qcc-jar-api)
qcc-boot-jars += $(qcc-cp-compile)
qcc-boot-jars += $(qcc-jar-runtime-unwind)
qcc-boot-jars += $(qcc-jar-runtime-main)
qcc-boot-jars += $(qcc-jar-runtime-gc-nogc)
qcc-boot-jars += $(qcc-jar-runtime-posix)
qcc-boot-jars += $(qcc-jar-runtime-deserialization)

qcc-cp-boot = $(subst $(space),:,$(qcc-boot-jars))
qcc-runtime-jars = $(shell find $(QCC_HOME)/main/target/libs -type f -name '*.jar')
qcc-cp-runtime = $(qcc-jar-main):$(subst $(space),:,$(qcc-runtime-jars))

dotfiles := $(shell find . -type f -name '*.dot')
svgfiles := $(patsubst %.dot,%.svg,$(dotfiles))

noop=
space = $(noop) $(noop)

run: $(program)
> $(program)
.PHONY: run

$(program): $(gen-jar-path)
> $(call run,$(javacmd))

debug: $(gen-jar-path)
> $(call run,$(javadebugcmd))
.PHONY: debug

verbose: $(gen-jar-path)
> $(call run,$(javaverbosecmd))
.PHONY: verbose

$(gen-jar-path): $(classfiles)
> jar cvfe $@ example $^

%.class: %.java $(qcc-jar-api)
> javac -d . -classpath .:$(qcc-cp-compile) $<

runjar: example.jar
> java -jar $<
.PHONY: run-jar

clean: clean-dist
> cd $(QCC_HOME)
> mvn clean
.PHONY: clean

clean-dist:
> rm -f *.class
> rm -f *.jar
> rm -f output.ll
> rm -drf $(targetdir)
> rm -f *.dot
> rm -f *.svg
> rm -f .*.svg
> rm -f *.log
> rm -drf methods
.PHONY: clean-dist

$(qcc-jar-api): $(qcc-sources)
> cd $(QCC_HOME)
> mvn -DskipTests=true install
> mvn dependency:copy-dependencies -DoutputDirectory=target/libs

%.svg: %.dot
> dot -Tsvg $< > $@

graphs: $(svgfiles)
.PHONY: graphs

copy-graphs:
> cp -r $(targetdir)/example/methods .
.PHONY: copy-graphs

define run
$(1) \
  -Dorg.jboss.byteman.transform.all=true \
  -javaagent:$(byteman-jar)=boot:$(byteman-jar),script:logging.btm,script:cg.btm \
  -classpath $(qcc-cp-runtime) \
  org.qbicc.main.Main \
  --boot-module-path $(gen-jar-path):/opt/qccrt/qbicc-rt-java.base-11.0.1-SNAPSHOT.jar:$(qcc-cp-boot) \
  --output-path $(targetdir) \
  --gen-graph \
  $(gen-main-name) \
  | tee console.log
endef