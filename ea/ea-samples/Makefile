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
JAVA_HOME := /opt/java-17
MAVEN_HOME := /opt/maven
QBICC_HOME := /opt/qbicc-qbicc
QBICC_VERSION := 0.3.0-SNAPSHOT

mvn += JAVA_HOME=$(JAVA_HOME)
mvn += $(MAVEN_HOME)/bin/mvn

qbicc_jars = $(shell find $(QBICC_HOME)/main/target/lib -type f -name '*.jar')
qbicc_jar_main := $(QBICC_HOME)/main/target/qbicc-main-$(QBICC_VERSION).jar
qbicc_jar_runtime_api := $(QBICC_HOME)/runtime/api/target/qbicc-runtime-api-$(QBICC_VERSION).jar
qbicc_jar_runtime_unwind := $(QBICC_HOME)/runtime/unwind/target/qbicc-runtime-unwind-$(QBICC_VERSION).jar
qbicc_jar_runtime_main := $(QBICC_HOME)/runtime/main/target/qbicc-runtime-main-$(QBICC_VERSION).jar
qbicc_jar_runtime_gc_nogc := $(QBICC_HOME)/runtime/gc/nogc/target/qbicc-runtime-gc-nogc-$(QBICC_VERSION).jar
qbicc_jar_runtime_posix := $(QBICC_HOME)/runtime/posix/target/qbicc-runtime-posix-$(QBICC_VERSION).jar
qbicc_sources = $(shell find $(QBICC_HOME)/ -type f -name '*.java')
qbicc_sources += $(shell find $(QBICC_HOME)/ -type f -name 'pom.xml')

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
> $(mvn) -DskipTests=true install
> touch $@

$(qbicc_jar_main): $(qbicc_jar_runtime_api)
> cd $(QBICC_HOME)
> $(mvn) dependency:copy-dependencies \
>   --projects main \
>   -DoutputDirectory=target/lib
> touch $@

qbicc-rt-jar:
> cd $(qbicc_cl_home)
> $(mvn) install
.PHONY: qbicc-rt-jar

clean-qbicc:
> cd $(QBICC_HOME)
> $(mvn) clean
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

java += $(JAVA_HOME)/bin/java
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

qbicc_params += --gen-graph
ifeq ($(EA),true)
  ifdef VERIFY
    byteman_scripts := $(comma)script:$(resources_dir)/ea/verify.btm
    classpath = $(qbicc_cp_runtime)
  else ifdef EVENTS
    byteman_scripts := $(comma)script:$(resources_dir)/ea/events.btm
    classpath = $(qbicc_cp_runtime):$(jar):target/lib/javapoet-1.13.0.jar
  else
    byteman_scripts := $(comma)script:$(resources_dir)/ea/logging.btm
    classpath = $(qbicc_cp_runtime)
  endif
else
  byteman_scripts := $(comma)script:$(resources_dir)/main/logging.btm
  classpath = $(qbicc_cp_runtime)
  qbicc_params += --no-escape-analysis
endif

run: $(qoccido_dir) $(qbicc_jar_main) $(jar)
> CC=clang $(java) \
>  -javaagent:$(byteman_jar)=boot:$(byteman_jar)$(byteman_scripts) \
>  -classpath $(classpath) \
>  org.qbicc.main.Main \
>  --boot-path-append-file $(jar) \
>  --output-path $(qoccido_dir) \
>  $(qbicc_params) \
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
> $(java) \
> -XX:-TieredCompilation \
> -Xbatch \
> -Xcomp \
> -XX:+UnlockDiagnosticVMOptions \
> -XX:+PrintEscapeAnalysis \
> -XX:+PrintEliminateAllocations \
> -cp target/ea-samples.jar \
> org.example.ea.samples.$(CLASS)

$(jar): $(sources) $(qbicc_jar_runtime_api)
> $(mvn) install dependency:copy-dependencies -Dea=$(EA) -DoutputDirectory=target/lib -DincludeArtifactIds=javapoet
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

open:
> open $(open_graphs_dir)/EASample_$(SAMPLE)*/methods/*/ea-*.dot.svg
.PHONY: open
