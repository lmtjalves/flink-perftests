# flink-perftests

A set of tools to do performance tests on Apache Flink (with multiple applications in parallel), and for reporting.

This repository contains four main components:

## Ingnitor

* Starts/Stops all the machines/services in your performance environment.
* Clean up the state of the environment (logs from old executions).

## Injector

* Uses [gateling.io](http://gatling.io) to inject data from a file datasource into [Apache Kafka](https://kafka.apache.org), in order to be processed by the applications.
* The injection rate per application can be configured to change over time.
* Simulates applications being deployed and canceled in Apache Flink.

## Reporter

* Collects logs from the machines available in your performance environment.
* Launches [Apache Zeppelin](http://zeppelin.apache.org) using [Docker](https://www.docker.com) and allows you to analyse all the information gathered during the execution.

## runtest.sh

* Coordinates the components above in order to do the full performance test cicle at once.
