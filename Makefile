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
QCC_CL_HOME := /opt/qcc-class-library

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

pull:
> $(call pull,$(QCC_HOME))
> $(call pull,$(QCC_CL_HOME))

define pull
cd ${1}
git checkout main
git pull
endef

install-qcc:
> $(call install,$(QCC_HOME))

install-qcc-class-library:
> $(call install,$(QCC_CL_HOME))

copy-dependencies:
> cd $(QCC_HOME)
> mvn -pl main dependency:copy-dependencies -DoutputDirectory=\$${project.build.directory}/libs

refresh: pull install-qcc install-qcc-class-library copy-dependencies

define install
cd ${1}
mvn clean install
endef
