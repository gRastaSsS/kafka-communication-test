# Kafka Communication Task

The project consists of the following components:

* [`Wage Service`] - a scalable REST API service, that has only 
one purpose - publishing wage requests to kafka. 
* [`Kafka Connect Service`] - an image of scalable [Kafka Connect] data streaming tool, 
enriched with a custom transformer library for tax calculation ([`connect-transformers`]) and MySQL driver. 
* [`Connectors Registry Service`] - service that is used for submitting ETL 
tasks to Kafka Connect at the startup. It exposes a draft of API
 (on top of [Kafka Connect REST Interface]) that can
 be extended to support runtime tasks submission. 
* Example Zookeeper / Kafka / MySQL instances.

The project is built using [Gradle]. Reusable build logic is implemented as Gradle
plugins that can be found in [`buildSrc`] directory.

[Gradle]: https://docs.gradle.org/current/userguide/what_is_gradle.html
[`buildSrc`]: buildSrc
[`Wage Service`]: wage-service
[`Kafka Connect Service`]: kafka-connect-service
[`Connectors Registry Service`]: connectors-registry-service
[`connect-transformers`]: kafka-connect-service/connect-transformers
[Kafka Connect]: https://docs.confluent.io/platform/current/connect/index.html
[Kafka Connect REST Interface]: https://docs.confluent.io/platform/current/connect/references/restapi.html

## Manually building and running services

I use [JIB] plugin for Gradle to build Docker images.

To build Docker images and use them locally, use the following command:
```
./gradlew jibDockerBuild
```
This command will build images for all the services and save them to your
local Docker installation, so you can inspect or run the image as any other
local container.  (You need to have Docker installed locally).

After building Docker images using [JIB], use docker-compose to launch services.
You can propagate your config file as follows.  
```
docker-compose --env-file ./config/.env.dev up --scale kafka-connect=N --scale wage-service=M 
```
Please note that other containers (zookeeper, kafka, db, connectors-registry-service)
are not scalable for this task. 

[JIB]: https://cloud.google.com/java/getting-started/jib

## Manual testing using Swagger UI

Swagger UI for wage-service should be available at 
[http://localhost:{port}/swagger-ui.html](http://localhost:{port}/swagger-ui.html)
after launch, where port ranges from 8090 to 8099 (depending on number of instances).

## Future plans

* I decided to use [Kafka Connect] for this task, since it provides an easy out-of-the
box solution to transfer a bunch of records from Kafka to another storage and apply some
simple mappings such as tax calculation. 
If, however, we plan to make this project support some aggregations or complex calculations 
over records, then I would suggest to look at more mature system ([Spark Streaming] as an example).
* One obvious bottleneck in our solution is a single MySQL instance. 
Although scalability is usually quite a troublesome task for SQL databases, 
we can still try to build and maintain a MySQL Cluster. 
As an alternative, I would consider using some NoSQL solution instead, 
as a potential candidate with naturally simpler scaling and streaming integration options. 
It's hard to tell which solution is better without knowing business requirements.
* Make the project production-ready: externalize the configuration, ensure load balancing and service discovery, 
enable monitoring and logging, write documentation, etc. 
Probably move to Kubernetes or other container management platform. 

[Spark Streaming]: https://spark.apache.org/docs/latest/streaming-programming-guide.html