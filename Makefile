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
> $(call pull,$(QBICC_HOME))

define pull
cd ${1}
git checkout main
git pull
endef

install-qbicc:
> cd $(QBICC_HOME)
> mvn clean install -DskipTests

test-qbicc:
> cd $(QBICC_HOME)
> mvn install

copy-dependencies:
> cd $(QBICC_HOME)
> mvn -pl main dependency:copy-dependencies -DoutputDirectory=\$${project.build.directory}/libs

refresh: pull install-qbicc copy-dependencies

ea-examples += ea-01
ea-examples += ea-02
ea-examples += ea-03
ea-examples += ea-04
ea-examples += ea-05

sync-ea-files:
> for ex1 in $(ea-examples) ; do \
>     for ex2 in $(ea-examples) ; do \
>         rsync -auvh $$ex1/Makefile $$ex2/Makefile ; \
>         rsync -auvh $$ex1/cg.btm $$ex2/cg.btm ; \
>         rsync -auvh $$ex1/logging.btm $$ex2/logging.btm ; \
>     done \
> done
.PHONY: sync-ea-files