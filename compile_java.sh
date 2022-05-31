#!/bin/bash

mkdir result
mkdir result/settings

cp -R settings result/settings

find ./src/jooom/ -name "*.java" > source.txt

javac -d result @source.txt
