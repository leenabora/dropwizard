#!/bin/bash -e

gradle fatJar

cd stockservice-service/build/libs

java -jar stockservice-service-fat.jar server
