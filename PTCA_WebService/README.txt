Additional installs to put into Maven before running the project:

sqljdbc41.jar:mvn install:install-file -Dfile=sqljdbc41.jar -Dpackaging=jar -DgroupId=com.microsoft.sqlserver -DartifactId=sqljdbc41 -Dversion=4.1

The project can then be run with the maven command:
mvn spring-boot:run