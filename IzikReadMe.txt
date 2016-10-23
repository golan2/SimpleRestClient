

This is a simple rest client that opens a few threads that send rest requests.
The URLs we invoke are for tomcat example pages.

maven-assembly-plugin
=====================
This is a mvn plugin that will create a JAR that contains all the dependencies.
This way we don't need to worry about bringing all the JARs we need and run the java with -cp and so on.

Note: it only adds dependencies; you need to build your jar first.

Example:
mvn package
mvn org.apache.maven.plugins:maven-assembly-plugin:2.2-beta-5:single
