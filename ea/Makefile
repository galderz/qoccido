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

ASYNC_HOME ?= /opt/async-profiler
JAVA_HOME ?= /opt/adopt-17
MAVEN_HOME := /opt/maven

java = $(JAVA_HOME)/bin/java
mvn += JAVA_HOME=$(JAVA_HOME)
mvn += $(MAVEN_HOME)/bin/mvn

all: gen test
.PHONY: all

test:
> cd ea-jmh
> $(mvn) verify
.PHONY: test

gen:
> cd ea-samples
> EVENTS=true make
.PHONY: gen

bench:
> $(java) -jar ea-jmh/target/benchmarks.jar
.PHONY: bench

async:
> LD_LIBRARY_PATH=$(ASYNC_HOME)/build $(java) -jar ea-jmh/target/benchmarks.jar -prof async:output=flamegraph -i 1 -wi 1 -f 1
.PHONY: async
