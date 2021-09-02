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

QBICC_HOME := /opt/qbicc-qbicc
BYTEMAN_HOME := /opt/byteman
VERSION := 0.1.0-SNAPSHOT

noop=
space = $(noop) $(noop)

qbicc_sources := $(shell find $(QBICC_HOME)/ -type f -name '*.java')

qbicc_jar_main := $(QBICC_HOME)/main/target/qbicc-main-$(VERSION).jar
qbicc_jar_runtime_api := $(QBICC_HOME)/runtime/api/target/qbicc-runtime-api-$(VERSION).jar
qbicc_jar_runtime_unwind := $(QBICC_HOME)/runtime/unwind/target/qbicc-runtime-unwind-$(VERSION).jar
qbicc_jar_runtime_main := $(QBICC_HOME)/runtime/main/target/qbicc-runtime-main-$(VERSION).jar
qbicc_jar_runtime_gc_nogc := $(QBICC_HOME)/runtime/gc/nogc/target/qbicc-runtime-gc-nogc-$(VERSION).jar
qbicc_jar_runtime_posix := $(QBICC_HOME)/runtime/posix/target/qbicc-runtime-posix-$(VERSION).jar

qbicc_cp_compile := $(qbicc_jar_runtime_api)

qbicc_cp_boot_jars += $(qbicc_cp_compile)
qbicc_cp_boot_jars += $(qbicc_jar_runtime_unwind)
qbicc_cp_boot_jars += $(qbicc_jar_runtime_main)
qbicc_cp_boot_jars += $(qbicc_jar_runtime_gc_nogc)
qbicc_cp_boot_jars += $(qbicc_jar_runtime_posix)
qbicc_cp_boot = $(subst $(space),:,$(qbicc_cp_boot_jars))

qbicc_jars = $(shell find $(QBICC_HOME)/main/target/libs -type f -name '*.jar')
qbicc_cp_runtime = $(qbicc_jar_main):$(subst $(space),:,$(qbicc_jars))

print = echo '$(1)'
comma := ,

byteman_jar := $(BYTEMAN_HOME)/lib/byteman.jar

main := org.example.ea.Main
name := ea-samples
target_dir := target
qbicc_graphs_dir := $(target_dir)/qbicc-graphs
connection_graphs_dir := $(target_dir)/connection-graphs
jar := $(target_dir)/$(name).jar
sources := $(shell find src -type f -name '*')

qoccido_dir := /tmp/qoccido/$(notdir $(shell pwd))
program := $(qoccido_dir)/a.out

java += java
# Big enough thread stack size to avoid https://github.com/quarkuscc/qcc/issues/262
java += -Xss16m
java_debug += $(java)
java_debug += -agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=*:4004

ifdef VERBOSE
  java += -Dorg.jboss.byteman.verbose=true
endif

ifdef VERIFY
  byteman_scripts := $(comma)script:src/main/resources/verify.btm
else
  byteman_scripts := $(comma)script:src/main/resources/nodes.btm$(comma)script:src/main/resources/logging.btm$(comma)script:src/main/resources/cg-intra.btm
endif

run: $(jar)
> rm -f target/verify.log
> rm -drf $(connection_graphs_dir)
> mkdir -p target/connection-graphs/intra-procedure
> $(java) \
>  -Dorg.jboss.byteman.transform.all=true \
>  -javaagent:$(byteman_jar)=boot:$(byteman_jar)$(byteman_scripts) \
>  -classpath $(qbicc_cp_runtime) \
>  org.qbicc.main.Main \
>  --boot-module-path $(jar):/opt/qbicc-rt/qbicc-rt-java.base-11.0.1-SNAPSHOT.jar:$(qbicc_cp_boot) \
>  --output-path $(qoccido_dir) \
>  --gen-graph \
>  $(main) \
>  | tee target/console.log
> $(program)
> test -f target/verify.log && cat target/verify.log || true
.PHONY: run

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
> mvn package
> touch $(jar)

$(qbicc_jar_runtime_api): $(qbicc_sources)
> cd $(QBICC_HOME)
> mvn -DskipTests=true install
> mvn dependency:copy-dependencies -DoutputDirectory=target/libs

clean:
> rm -drf $(target_dir)
.PHONY: clean

clean-all: clean
> cd $(QBICC_HOME)
> mvn clean
.PHONY: clean-all

graphs: qbicc-graphs connection-graphs
.PHONY: graphs

connection-graphs:
> find $(connection_graphs_dir) -iname '*.dot' -exec bash -c 'dot -Tsvg "{}" > "{}".svg' \;
.PHONY: connection-graphs

qbicc-graphs:
> rm -drf $(qbicc_graphs_dir)
> mkdir -p $(qbicc_graphs_dir)
> cp -r $(qoccido_dir)/org/example/ea/samples/EASample* $(qbicc_graphs_dir)
> cp -r $(qoccido_dir)/org/qbicc/runtime/main/Main* $(qbicc_graphs_dir)
> find $(qbicc_graphs_dir) -depth -name '*$$*' -execdir bash -c 'mv -- "$$1" "$${1//\$$/_}"' bash {} \;
> find $(qbicc_graphs_dir) -iname '*.dot' -exec bash -c 'dot -Tsvg "{}" > "{}".svg' \;
.PHONY: qbicc-graphs

# TODO add a target to run one of ea examples with java with options such as: -XX:+UnlockDiagnosticVMOptions -XX:+PrintEscapeAnalysis -XX:+PrintEliminateAllocations
