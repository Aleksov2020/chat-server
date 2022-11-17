#!/usr/bin/env bash

mvn clean package

echo 'Copy files'

scp -i ~/.ssh/id_rsa C:/serverChat/bes-proebov/serverChat/target/ServerChat-0.0.1-SNAPSHOT.jar root@194.67.105.170:/root

echo "Bye"