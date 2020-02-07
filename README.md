# ArepClientServerChallenge01

# Client Server

This application implements a web server created with java that supports multiple consecutive requests and returns the requested files, including html pages and images.

The application is deployed here: [Heroku deployment](https://clientserver01.herokuapp.com/index.html)

The web server has these resources:

* /index.html
* /java.png
* /spark.png
* /spring.png


[![CircleCI](https://circleci.com/gh/swilsonmelo/ArepClientServerChallenge01.svg?style=svg)](https://circleci.com/gh/swilsonmelo/ArepClientServerChallenge01)

## Running locally

Compile first time the project after downloaded.

    mvn clean install
    mvn package

Make unit tests.

    mvn test

To run the whole project.

    mvn exec:java -D "exec.mainClass"="edu.escuelaing.arep.App"        

    To test the server from the localhost open in the browser the directory: ./LocalResources/index.html

## Generate documentation.

In order to obtain the documentation of the project, you must execute the command:

    mvn javadoc:jar

An HTML documentation will be generated in /target/site/apidocs/index.html.

## Built With

* [Maven](https://maven.apache.org/) - Dependency Management.
* [Heroku](https://www.heroku.com/) - A cloud platform that lets companies build, deliver, monitor and scale apps
* [CircleCi](https://circleci.com/) - Cloud-native continuous integration

## Author

* **Willson Sneitder Melo Merchan** - Escuela Colombiana de Ingeniería Julio Garavito.

## License

* This project is under GNU General Public License - see [LICENSE](https://github.com/swilsonmelo/ArepClientServerChallenge01/blob/master/LICENSE) to more info.
