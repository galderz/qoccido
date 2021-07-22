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
qbicc_jar_runtime_deserialization := $(QBICC_HOME)/runtime/deserialization/target/qbicc-runtime-deserialization-$(VERSION).jar

qbicc_cp_compile := $(qbicc_jar_runtime_api)

qbicc_cp_boot_jars += $(qbicc_cp_compile)
qbicc_cp_boot_jars += $(qbicc_jar_runtime_unwind)
qbicc_cp_boot_jars += $(qbicc_jar_runtime_main)
qbicc_cp_boot_jars += $(qbicc_jar_runtime_gc_nogc)
qbicc_cp_boot_jars += $(qbicc_jar_runtime_posix)
qbicc_cp_boot_jars += $(qbicc_jar_runtime_deserialization)
qbicc_cp_boot = $(subst $(space),:,$(qbicc_cp_boot_jars))

qbicc_jars = $(shell find $(QBICC_HOME)/main/target/libs -type f -name '*.jar')
qbicc_cp_runtime = $(qbicc_jar_main):$(subst $(space),:,$(qbicc_jars))

byteman_jar := $(BYTEMAN_HOME)/lib/byteman.jar

main := org.example.ea.Main
name := ea-samples
target_dir := target
graphs_dir := $(target_dir)/graphs
jar := $(target_dir)/$(name).jar
java_sources := $(shell find src -type f -name '*.java')

qoccido_dir := /tmp/qoccido/$(notdir $(shell pwd))
program := $(qoccido_dir)/a.out

java += java
# Big enough thread stack size to avoid https://github.com/quarkuscc/qcc/issues/262
java += -Xss16m
java_debug += $(java)
java_debug += -agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=*:4004
java_verbose += $(java)
java_verbose += -Dorg.jboss.byteman.verbose=true

run: $(program)
> $(program)
.PHONY: run

$(program): $(jar)
> $(call run-qbicc,$(java))

verbose: $(jar)
> $(call run-qbicc,$(java_verbose))
.PHONY: verbose

$(jar): $(java_sources) $(qbicc_jar_runtime_api)
> mvn package

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

graphs: $(graphs_dir)
> cp -r $(qoccido_dir)/org/example/ea/samples/EASample* $<
> find $< -depth -name '*$$*' -execdir bash -c 'mv -- "$$1" "$${1//\$$/_}"' bash {} \;
> find $< -iname '*.dot' -exec bash -c 'dot -Tsvg "{}" > "{}".svg' \;
.PHONY: graphs

$(graphs_dir):
> mkdir -p $@

define run-qbicc
mkdir -p target/graphs/intra-procedure
mkdir -p target/graphs/inter-procedure
$(1) \
  -Dorg.jboss.byteman.transform.all=true \
  -javaagent:$(byteman_jar)=boot:$(byteman_jar),script:src/main/resources/logging.btm,script:src/main/resources/cg-intra.btm \
  -classpath $(qbicc_cp_runtime) \
  org.qbicc.main.Main \
  --boot-module-path $(jar):/opt/qccrt/qbicc-rt-java.base-11.0.1-SNAPSHOT.jar:$(qbicc_cp_boot) \
  --output-path $(qoccido_dir) \
  --gen-graph \
  $(main) \
  | tee target/console.log
endef