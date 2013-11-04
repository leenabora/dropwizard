#!/bin/bash -e

gradle clean build fatJar

cd stockservice-service/build/libs

ymlConfigFile="$HOME/projects/dropwizard/stockService.yml"

java -jar stockservice-service-fat.jar server $ymlConfigFile
