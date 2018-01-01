<p align="left">
  <img width= "15%" src="https://github.com/AbdullahAsendar/GraphJ/blob/master/src/main/resources/images/GraphJ.png"/>
</p>


[![Base](https://img.shields.io/badge/Base-JavaFX-blue.svg)](http://www.oracle.com/technetwork/java/javafx/overview/index.html)
[![Framework](https://img.shields.io/badge/Framework-Spring-orange.svg)](https://spring.io/)
[![RDB](https://img.shields.io/badge/Rational-MySQL-blue.svg)](https://www.mysql.com/)
[![Graph](https://img.shields.io/badge/Graph-Neo4J-blue.svg)](https://neo4j.com/)

# GraphJ
A tool that reads a provided MySQL database and converts it to Neo4J graph database.

## Demo Video

<a style="float:right" href="https://youtu.be/Trh9YuNlcao" target="_blank">
  <img alt="Demo Video" src="https://github.com/AbdullahAsendar/GraphJ/blob/master/src/main/resources/images/GraphJ_screen_shot.png" />
</a>

## Prerequisites

In order to run GraphJ, you need to install:

 - An IDE, ex [Eclipse](http://www.eclipse.org/downloads/eclipse-packages/)
 - [JDK 1.8 (minimum)](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
 - [Lombok](https://projectlombok.org/download.html)
 - [MySQL Server](https://www.mysql.com/)
 - [Neo4J Server](https://neo4j.com/)

## Installing

 - Checkout using [EGit](http://www.eclipse.org/egit/download/) connector  (in order to import as maven project).
 - Make sure to have lombok installed on your IDE.
 - Configure and you are good to go.

## Configuration

Define the following environment variables:

Variable        | Data Type     | Value                                        | Default Value
-------------   | ------------- | -------------                                | -------------
MYSQL_HOST      | String        | The host to connect                          | localhost
MYSQL_PORT      | Integer       | The port to connect                          | 3306
MYSQL_DB        | String        | Default database name                        | null
MYSQL_USER      | String        | User name of a MySQL user                    | root
MYSQL_PASS      | String        | Password of the MySQL user                   | root
NEO4J_DB        | String        | Path to Neo4J database                       | null
QUERY_LIMIT     | Integer       | The limitation on the MySQL queries          | 1000



## Build 
[![Maven](https://img.shields.io/badge/Apache-Maven-blue.svg)](https://maven.apache.org/)

To build the project run as maven build with goals: `clean install`

