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

all: run

# Qbicc
QBICC_HOME := /opt/qbicc-qbicc
QBICC_VERSION := 0.1.0-SNAPSHOT

qbicc_jars = $(shell find $(QBICC_HOME)/main/target/libs -type f -name '*.jar')
qbicc_jar_main := $(QBICC_HOME)/main/target/qbicc-main-$(QBICC_VERSION).jar
qbicc_jar_runtime_api := $(QBICC_HOME)/runtime/api/target/qbicc-runtime-api-$(QBICC_VERSION).jar
qbicc_jar_runtime_unwind := $(QBICC_HOME)/runtime/unwind/target/qbicc-runtime-unwind-$(QBICC_VERSION).jar
qbicc_jar_runtime_main := $(QBICC_HOME)/runtime/main/target/qbicc-runtime-main-$(QBICC_VERSION).jar
qbicc_jar_runtime_gc_nogc := $(QBICC_HOME)/runtime/gc/nogc/target/qbicc-runtime-gc-nogc-$(QBICC_VERSION).jar
qbicc_jar_runtime_posix := $(QBICC_HOME)/runtime/posix/target/qbicc-runtime-posix-$(QBICC_VERSION).jar
qbicc_sources := $(shell find $(QBICC_HOME)/ -type f -name '*.java')

qbicc_cl_home = /opt/qbicc-class-library

noop=
space = $(noop) $(noop)

qbicc_cp_compile := $(qbicc_jar_runtime_api)
qbicc_cp_runtime = $(qbicc_jar_main):$(subst $(space),:,$(qbicc_jars))

qbicc_cp_boot_jars += $(qbicc_cp_compile)
qbicc_cp_boot_jars += $(qbicc_jar_runtime_unwind)
qbicc_cp_boot_jars += $(qbicc_jar_runtime_main)
qbicc_cp_boot_jars += $(qbicc_jar_runtime_gc_nogc)
qbicc_cp_boot_jars += $(qbicc_jar_runtime_posix)
qbicc_cp_boot = $(subst $(space),:,$(qbicc_cp_boot_jars))

$(qbicc_jar_runtime_api): $(qbicc_sources)
> cd $(QBICC_HOME)
> mvn -DskipTests=true install
> mvn dependency:copy-dependencies -DoutputDirectory=target/libs
> touch $@

qbicc-rt-jar:
> cd $(qbicc_cl_home)
> mvn install
> touch $@
.PHONY: qbicc-rt-jar

clean-qbicc:
> cd $(QBICC_HOME)
> mvn clean
> cd $(qbicc_cl_home)
> mvn clean
.PHONY: clean-qbicc

# Samples
BYTEMAN_HOME := /opt/byteman

byteman_jar := $(BYTEMAN_HOME)/lib/byteman.jar
main := org.example.ea.Main
name := ea-samples
qoccido_dir := /tmp/qoccido/$(notdir $(shell pwd))
resources_dir := src/main/resources
target_dir := target

sources := $(shell find src -type f -name '*')
sources += pom.xml

jar := $(target_dir)/$(name).jar
program := $(qoccido_dir)/a.out
graphs_dir := $(target_dir)/graphs

java += java
java += -Xss16m # Big enough thread stack size to avoid https://github.com/quarkuscc/qcc/issues/262
ifdef DEBUG
  java += -agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=*:4004
endif

ifdef VERBOSE
  java += -Dorg.jboss.byteman.verbose=true
endif

print = echo '$(1)'
comma := ,

EA ?= true

ifeq ($(EA),true)
  ifdef VERIFY
    byteman_scripts := $(comma)script:$(resources_dir)/ea/verify.btm
  else
    byteman_scripts := $(comma)script:$(resources_dir)/ea/logging.btm
  endif
else
  byteman_scripts := $(comma)script:$(resources_dir)/main/logging.btm
endif

run: $(qoccido_dir) $(jar)
> $(java) \
>  -javaagent:$(byteman_jar)=boot:$(byteman_jar)$(byteman_scripts) \
>  -classpath $(qbicc_cp_runtime) \
>  org.qbicc.main.Main \
>  --boot-path-append-file $(jar) \
>  --output-path $(qoccido_dir) \
>  --gen-graph \
>  $(main) \
>  | tee target/console.log
> $(program)
.PHONY: run

$(qoccido_dir):
> mkdir -p $@

run-jar: $(jar)
> java -jar $(jar)
.PHONY: run-jar

CLASS ?= EASample_01_Basic

run-class: $(jar)
> java \
> -XX:-TieredCompilation \
> -Xbatch \
> -Xcomp \
> -XX:+UnlockDiagnosticVMOptions \
> -XX:+PrintEscapeAnalysis \
> -XX:+PrintEliminateAllocations \
> -cp target/ea-samples.jar \
> org.example.ea.samples.$(CLASS)

$(jar): $(sources) $(qbicc_jar_runtime_api)
> mvn package -Dea=$(EA)
> touch $(jar)

clean:
> rm -drf $(target_dir)
> rm -drf $(qoccido_dir)
.PHONY: clean

graphs:
> rm -drf $(graphs_dir)
> mkdir -p $(graphs_dir)
> cp -r $(qoccido_dir)/org/example/ea/samples/EASample* $(graphs_dir)
> cp -r $(qoccido_dir)/org/qbicc/runtime/main/Main* $(graphs_dir)
> find $(graphs_dir) -depth -name '*$$*' -execdir bash -c 'mv -- "$$1" "$${1//\$$/_}"' bash {} \;
> find $(graphs_dir) -iname '*.dot' -exec bash -c 'dot -Tsvg "{}" > "{}".svg' \;
.PHONY: graphs

open_graphs_dir := /opt/qbicc-graphs

open-graphs-all: open-graphs-1 open-graphs-2 open-graphs-3 open-graphs-4 open-graphs-5 open-graphs-6
.PHONY: open-graphs-all

open-graphs-1:
> open $(open_graphs_dir)/EASample_01_Basic/methods/sample.id2/ea-intra.dot.svg
> open $(open_graphs_dir)/EASample_01_Basic/methods/sample.id2/ea-inter.dot.svg
.PHONY: open-graphs-1

open-graphs-2:
> open $(open_graphs_dir)/EASample_02_StaticAssignment/methods/sample.id2/ea-intra.dot.svg
> open $(open_graphs_dir)/EASample_02_StaticAssignment/methods/sample.id2/ea-inter.dot.svg
.PHONY: open-graphs

open-graphs-3:
> open $(open_graphs_dir)/EASample_03_ParameterEscape/methods/sample.id2/ea-intra.dot.svg
> open $(open_graphs_dir)/EASample_03_ParameterEscape/methods/sample.id2/ea-inter.dot.svg
> open $(open_graphs_dir)/EASample_03_ParameterEscape/methods/sample2.id3/ea-intra.dot.svg
> open $(open_graphs_dir)/EASample_03_ParameterEscape/methods/sample2.id3/ea-inter.dot.svg
> open $(open_graphs_dir)/EASample_03_ParameterEscape/methods/sampleViaAux.id4/ea-intra.dot.svg
> open $(open_graphs_dir)/EASample_03_ParameterEscape/methods/sampleViaAux.id4/ea-inter.dot.svg
> open $(open_graphs_dir)/EASample_03_ParameterEscape/methods/sampleViaAux2.id5/ea-intra.dot.svg
> open $(open_graphs_dir)/EASample_03_ParameterEscape/methods/sampleViaAux2.id5/ea-inter.dot.svg
.PHONY: open-graphs-3

open-graphs-4:
> open $(open_graphs_dir)/EASample_04_ReturnEscape/methods/sample.id2/ea-intra.dot.svg
> open $(open_graphs_dir)/EASample_04_ReturnEscape/methods/sample.id2/ea-inter.dot.svg
> open $(open_graphs_dir)/EASample_04_ReturnEscape/methods/sample2.id3/ea-intra.dot.svg
> open $(open_graphs_dir)/EASample_04_ReturnEscape/methods/sample2.id3/ea-inter.dot.svg
.PHONY: open-graphs-4

open-graphs-5:
> open $(open_graphs_dir)/EASample_05_Throw/methods/sampleThrow.id5/ea-intra.dot.svg
> open $(open_graphs_dir)/EASample_05_Throw/methods/sampleThrow.id5/ea-inter.dot.svg
.PHONY: open-graphs-5

open-graphs-6:
> open $(open_graphs_dir)/EASample_06_ParameterToStatic/methods/sample.id2/ea-intra.dot.svg
> open $(open_graphs_dir)/EASample_06_ParameterToStatic/methods/sample.id2/ea-inter.dot.svg
> open $(open_graphs_dir)/EASample_06_ParameterToStatic/methods/sample2.id3/ea-intra.dot.svg
> open $(open_graphs_dir)/EASample_06_ParameterToStatic/methods/sample2.id3/ea-inter.dot.svg
.PHONY: open-graphs-6

open-graphs-main:
> open $(open_graphs_dir)/Main/functions/main/ea-intra.dot.svg
> open $(open_graphs_dir)/Main/functions/main/ea-inter.dot.svg
.PHONY: open-graphs-main

graphs-sample:
> open $(open_graphs_dir)/EASample_$(SAMPLE)*/methods/*/ea-*.dot.svg
.PHONY: graphs-sample
