The web-service uses Microsoft SQL server as it's database. Please run the
Table Creation.sql script in the main folder to setup the database.

The connection string and default image path need to be updated before building.
in Application.java change the DB_Connection_String to the appropreate server.
The login should be the same after running DB setup and initialization scripts.

Additional installs to put into Maven before running the project:

sqljdbc41.jar:mvn install:install-file -Dfile=sqljdbc41.jar -Dpackaging=jar -DgroupId=com.microsoft.sqlserver -DartifactId=sqljdbc41 -Dversion=4.1

The project can then be run with the maven command:
mvn spring-boot:run