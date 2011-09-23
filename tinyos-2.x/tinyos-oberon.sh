#! /usr/bin/env bash
# Here we setup the environment
# variables needed by the tinyos 
# make system

echo "Setting up for TinyOS 2.1.1"
export TOSROOT=
export TOSDIR=
export MAKERULES=

TOSROOT="/cygdrive/c/Dropbox/Plum/Code/PLUM2.0/tinyos-2.x"
TOSDIR="$TOSROOT/tos"
CLASSPATH=$CLASSPATH:$TOSROOT/support/sdk/java/tinyos.jar:.
MAKERULES="$TOSROOT/support/make/Makerules"

export TOSROOT
export TOSDIR
export CLASSPATH
export MAKERULES

