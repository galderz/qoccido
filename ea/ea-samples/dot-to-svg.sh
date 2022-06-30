#!/usr/bin/env bash

graphs_dir=target/graphs

find ${graphs_dir} -depth -name '*$*' -execdir bash -c 'mv -- "$1" "${1//\$/_}"' bash {} \;
find ${graphs_dir} -iname '*.dot' -exec bash -c 'dot -Tsvg "{}" > "{}".svg' \;

# Debug individual file names being processed with:
#find ${graphs_dir} -iname '*.dot' -exec bash -c 'echo "{}" && dot -Tsvg "{}" > "{}".svg' \;
