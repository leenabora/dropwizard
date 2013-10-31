#!/bin/bash -e
gradle fatJar
cd dev/build/libs
java -jar dev-fat.jar server
