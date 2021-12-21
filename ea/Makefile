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

all: gen test
.PHONY: all

test:
> cd ea-jmh
> mvn verify
.PHONY: test

gen:
> cd ea-samples
> EVENTS=true make
.PHONY: gen

bench:
> java -jar ea-jmh/target/benchmarks.jar
.PHONY: bench