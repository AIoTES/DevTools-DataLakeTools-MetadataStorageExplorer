# DL-datamodel_workbench

ACTIVAGE datamodel workbench is a tool for exploring the data in Data Lake produced by IoT platforms. The datamodel workbench consists of three components, i.e. Datamodel Explorer, Storage Explorer and Metadata Explorer.

* The Datamodel Explorer is responsible for providing a graphical interface to the management of Models. It interacts with the [Metadata Strorage Server](https://git.activageproject.eu/Deployment/DT-AIOTES_docker/src/master/Metadata%20Storage%20server) through the APIs exposed by Metadata Storage Server. 

* The Storage Explorer provides a visual interface on top of the [Independent Data Storage](https://git.activageproject.eu/Data_Analytics/DL-Independent_data_storage) and the schema part of the [Query Execution Component](https://git.activageproject.eu/Data_Analytics/DL-Query_execution) for management of Databases, Tables and Schemas. 

* The Metadata Explorer component provides a graphical interface for management of Devices and Deployments. For this purpose, it interacts with the Metadata Storage Server through the APIs provided.


## Development

Before you can build this project, you must install and configure the following dependencies on your machine:

1. [Node.js][]: We use Node to run a development web server and build the project.
   Depending on your system, you can install Node either from source or as a pre-packaged bundle.
2. [Yarn][]: We use Yarn to manage Node dependencies.
   Depending on your system, you can install Yarn either from source or as a pre-packaged bundle.

After installing Node, you should be able to run the following command to install development tools.
You will only need to run this command when dependencies change in [package.json](package.json).

    yarn install

We use [Gulp][] as our build system. Install the Gulp command-line tool globally with:

    yarn global add gulp-cli

Run the following commands in two separate terminals to create a blissful development experience where your browser
auto-refreshes when files change on your hard drive.

    ./mvnw
    gulp

[Bower][] is used to manage CSS and JavaScript dependencies used in this application. You can upgrade dependencies by
specifying a newer version in [bower.json](bower.json). You can also run `bower update` and `bower install` to manage dependencies.
Add the `-h` flag on any command to see how you can use it. For example, `bower update -h`.




## Building for production

To optimize the test application for production, run:

    ./mvnw -Pprod clean package

This will concatenate and minify the client CSS and JavaScript files. It will also modify `index.html` so it references these new files.
To ensure everything worked, run:

    java -jar target/*.war

Then navigate to [http://localhost:8080](http://localhost:8080) in your browser.



## Testing

To launch your application's tests, run:

    ./mvnw clean test

### Client tests

Unit tests are run by [Karma][] and written with [Jasmine][]. They're located in [src/test/javascript/](src/test/javascript/) and can be run with:

    gulp test

UI end-to-end tests are powered by [Protractor][], which is built on top of WebDriverJS. They're located in [src/test/javascript/e2e](src/test/javascript/e2e)
and can be run by starting Spring Boot in one terminal (`./mvnw spring-boot:run`) and running the tests (`gulp itest`) in a second one.
### Other tests

Performance tests are run by [Gatling][] and written in Scala. They're located in [src/test/gatling](src/test/gatling) and can be run with:

    ./mvnw gatling:execute

For more information, refer to the [Running tests page][].

## Using Docker to simplify development (optional)

First build the workbench component using the following command:

    ./mvnw -DskipTests=true -Pdev clean package

Second step is to build docker image. To build docker image, run the following command:

    docker build --no-cache -t docker-activage.satrd.es/dl-datamodel-workbench target

Third step is to run the docker image using the following command. For this step you will need `docker-env` file available in [src/main/docker](src/main/docker). Below command should be run from the directory where the `docker-env` file exists.

    docker run -d -t --env-file docker-env -p 4590:8080 docker-activage.satrd.es/dl-datamodel-workbench:latest

The Datamodel Workbench can be accessed using the following URL:

[http://localhost:4590/datamodel-workbench/](http://localhost:4590/datamodel-workbench/)

Last step is to push docker image to the activage docker registry (docker-activage.satrd.es). Use the following command:

    docker push docker-activage.satrd.es/dl-datamodel-workbench

<!--- You can use Docker to improve your development experience. A number of docker-compose configuration are available in the [src/main/docker](src/main/docker) folder to launch required third party services.

For example, to start a mysql database in a docker container, run:

    docker-compose -f src/main/docker/mysql.yml up -d

To stop it and remove the container, run:

    docker-compose -f src/main/docker/mysql.yml down

You can also fully dockerize your application and all the services that it depends on.
To achieve this, first build a docker image of your app by running:

    ./mvnw verify -Pprod dockerfile:build

Then run:

    docker-compose -f src/main/docker/app.yml up -d

For more information refer to [Using Docker and Docker-Compose][]

## Continuous Integration (optional) -->

## Integration with other Components

The Datamodel Workbench is dependent on three components i.e. Metadata Storage Server, Independent Data Storage and Query Execution Component. It will communicate with these components if all are deployed at the following URLs:

[Metadata Strorage Server:](https://git.activageproject.eu/Deployment/DT-AIOTES_docker/src/master/Metadata%20Storage%20server)

    http://localhost:8081/

[Independent Data Storage:](https://git.activageproject.eu/Data_Analytics/DL-Independent_data_storage)

    http://localhost:4567/

[Query Execution Component:](https://git.activageproject.eu/Data_Analytics/DL-Query_execution)

    http://localhost:4570/