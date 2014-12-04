The connection string and default image path need to be updated before building.
in PTClientAssist.java change the DB_Connection_String and Default_Image to the appropreate location.

Additional installs to put into Maven before running the project:

Forms-1.3.0.jar:mvn install:install-file -Dfile=forms-1.3.0.jar -Dpackaging=jar -DgroupId=com.jgoodies.forms -DartifactId=forms13 -Dversion=1.3
jnlp.jar:mvn install:install-file -Dfile=jnlp.jar -Dpackaging=jar -DgroupId=com.javax.jnlp -DartifactId=jnlp1 -Dversion=1.0
sqljdbc41.jar:mvn install:install-file -Dfile=sqljdbc41.jar -Dpackaging=jar -DgroupId=com.microsoft.sqlserver -DartifactId=sqljdbc41 -Dversion=4.1

The project can then be run with the maven command:
mvn spring-boot:run

The password for the Ninja login is: password