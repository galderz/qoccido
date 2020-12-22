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
QCC_CL_HOME := $(HOME)/1/qcc-class-library

all: hello
.PHONY: all

hello:
> $(call submake,hello)
.PHONY: hello

clean:
> $(call submake,hello,clean)

define submake
cd ${1}
make ${2}
endef

refresh:
> $(call refresh,$(QCC_HOME))
> $(call refresh,$(QCC_CL_HOME))

define refresh
cd ${1}
git checkout master
git pull
mvn clean install
endef
