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
javafiles := example/Example.java
classfiles := $(patsubst %.java,%.class,$(javafiles))

compileclasspath := $(HOME)/.m2/repository/cc/quarkus/qcc-runtime-api/1.0.0-SNAPSHOT/qcc-runtime-api-1.0.0-SNAPSHOT.jar

jandex = $(HOME)/.m2/repository/org/jboss/jandex/2.1.3.Final/jandex-2.1.3.Final.jar

jbosslogging = $(HOME)/.m2/repository/org/jboss/logging/jboss-logging/3.4.1.Final/jboss-logging-3.4.1.Final.jar

asmrepo = $(HOME)/.m2/repository/org/ow2/asm
asm := $\
$(asmrepo)/asm/7.2/asm-7.2.jar:$\
$(asmrepo)/asm-tree/7.2/asm-tree-7.2.jar:$\
$(asmrepo)/asm-analysis/7.2/asm-analysis-7.2.jar:

smallryerepo = $(HOME)/.m2/repository/io/smallrye
smallrye := $\
$(smallryerepo)/common/smallrye-common-constraint/1.4.1/smallrye-common-constraint-1.4.1.jar:$\
$(smallryerepo)/common/smallrye-common-function/1.4.1/smallrye-common-function-1.4.1.jar:$\
$(smallryerepo)/common/smallrye-common-version/1.4.1/smallrye-common-version-1.4.1.jar

qccrepo := $(HOME)/.m2/repository/cc/quarkus
qcc := $\
$(qccrepo)/qcc-main/1.0.0-SNAPSHOT/qcc-main-1.0.0-SNAPSHOT.jar:$\
$(qccrepo)/qcc-driver/1.0.0-SNAPSHOT/qcc-driver-1.0.0-SNAPSHOT.jar:$\
$(qccrepo)/qcc-machine-file-object/1.0.0-SNAPSHOT/qcc-machine-file-object-1.0.0-SNAPSHOT.jar:$\
$(qccrepo)/qcc-machine-arch/1.0.0-SNAPSHOT/qcc-machine-arch-1.0.0-SNAPSHOT.jar:$\
$(qccrepo)/qcc-machine-tool-api/1.0.0-SNAPSHOT/qcc-machine-tool-api-1.0.0-SNAPSHOT.jar:$\
$(qccrepo)/qcc-machine-probe/1.0.0-SNAPSHOT/qcc-machine-probe-1.0.0-SNAPSHOT.jar:$\
$(qccrepo)/qcc-compiler/1.0.0-SNAPSHOT/qcc-compiler-1.0.0-SNAPSHOT.jar:$\
$(qccrepo)/qcc-machine-file-elf/1.0.0-SNAPSHOT/qcc-machine-file-elf-1.0.0-SNAPSHOT.jar:$\
$(qccrepo)/qcc-machine-file-bin/1.0.0-SNAPSHOT/qcc-machine-file-bin-1.0.0-SNAPSHOT.jar:$\
$(qccrepo)/qcc-machine-file-macho/1.0.0-SNAPSHOT/qcc-machine-file-macho-1.0.0-SNAPSHOT.jar:$\
$(qccrepo)/qcc-plugin-constants/1.0.0-SNAPSHOT/qcc-plugin-constants-1.0.0-SNAPSHOT.jar:$\
$(qccrepo)/qcc-plugin-conversion/1.0.0-SNAPSHOT/qcc-plugin-conversion-1.0.0-SNAPSHOT.jar:$\
$(qccrepo)/qcc-plugin-dot/1.0.0-SNAPSHOT/qcc-plugin-dot-1.0.0-SNAPSHOT.jar:$\
$(qccrepo)/qcc-plugin-linker/1.0.0-SNAPSHOT/qcc-plugin-linker-1.0.0-SNAPSHOT.jar:$\
$(qccrepo)/qcc-plugin-llvm/1.0.0-SNAPSHOT/qcc-plugin-llvm-1.0.0-SNAPSHOT.jar:$\
$(qccrepo)/qcc-machine-llvm/1.0.0-SNAPSHOT/qcc-machine-llvm-1.0.0-SNAPSHOT.jar:$\
$(qccrepo)/qcc-plugin-lowering/1.0.0-SNAPSHOT/qcc-plugin-lowering-1.0.0-SNAPSHOT.jar:$\
$(qccrepo)/qcc-plugin-main-method/1.0.0-SNAPSHOT/qcc-plugin-main-method-1.0.0-SNAPSHOT.jar:$\
$(qccrepo)/qcc-plugin-intrinsics/1.0.0-SNAPSHOT/qcc-plugin-intrinsics-1.0.0-SNAPSHOT.jar:$\
$(qccrepo)/qcc-plugin-native/1.0.0-SNAPSHOT/qcc-plugin-native-1.0.0-SNAPSHOT.jar:$\
$(qccrepo)/qcc-runtime-api/1.0.0-SNAPSHOT/qcc-runtime-api-1.0.0-SNAPSHOT.jar:$\
$(qccrepo)/qcc-plugin-optimization/1.0.0-SNAPSHOT/qcc-plugin-optimization-1.0.0-SNAPSHOT.jar:$\
$(qccrepo)/qcc-plugin-patcher/1.0.0-SNAPSHOT/qcc-plugin-patcher-1.0.0-SNAPSHOT.jar:$\
$(qccrepo)/qcc-plugin-reachability/1.0.0-SNAPSHOT/qcc-plugin-reachability-1.0.0-SNAPSHOT.jar:$\
$(qccrepo)/qcc-plugin-try-catch/1.0.0-SNAPSHOT/qcc-plugin-try-catch-1.0.0-SNAPSHOT.jar:$\
$(qccrepo)/qcc-plugin-verification/1.0.0-SNAPSHOT/qcc-plugin-verification-1.0.0-SNAPSHOT.jar:$\
$(qccrepo)/qcc-machine-tool-gnu/1.0.0-SNAPSHOT/qcc-machine-tool-gnu-1.0.0-SNAPSHOT.jar:$\
$(qccrepo)/qcc-machine-tool-clang/1.0.0-SNAPSHOT/qcc-machine-tool-clang-1.0.0-SNAPSHOT.jar:$\
$(qccrepo)/qcc-machine-tool-llvm/1.0.0-SNAPSHOT/qcc-machine-tool-llvm-1.0.0-SNAPSHOT.jar:$\
$(compileclasspath)

debug := -agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=*:4004

run: a.out
> $(targetdir)/a.out
.PHONY: run

a.out: example.jar
> java -classpath $(asm):$(jandex):$(jbosslogging):$(smallrye):$(qcc) cc.quarkus.qcc.main.Main --boot-module-path example.jar:/opt/qccrt/qccrt-java.base-11.0.1-SNAPSHOT.jar --output-path $(targetdir) example.Example

example.jar: $(classfiles)
> jar cvfe $@ example.Example $^

%.class: %.java
> javac -d . -classpath $(compileclasspath) $<

run-jar: example.jar
> java -jar $<
.PHONY: run-jar

clean:
> rm -f example/*.class
> rm -f *.jar
> rm -drf $(targetdir)
.PHONY: clean

debug:
> java $(debug) -classpath $(asm):$(smallrye):$(classpath):$(qcc) cc.quarkus.qcc.main.Main --boot-module-path example.jar:/opt/qccrt/qccrt-java.base-11.0.1-SNAPSHOT.jar --output-path . example.Example
.PHONY: debug

qcc:
> cd $(QCC_HOME)
> mvn -DskipTests=true clean install
.PHONY: qcc
