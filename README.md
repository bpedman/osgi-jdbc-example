osgi-jdbc-example
=================

Example Declarative Service using a JDBC DataSourceFactory

Testing out with Karaf
----------------------

- Install a properly OSGi-wrapped PostgreSQL database driver
- Install the OSGi Enterprise bundle: `install -s mvn:org.osgi/org.osgi.enterprise/4.2.0`
- Install the PostgreSQL DataSourceFactory implementation: `install -s mvn:org.ops4j.pax.jdbc/pax-jdbc-postgresql`
- Install the SCR declarative services feature: `features:install scr`
- Install this bundle
- Create a config file for the service in the etc/ directory called ServiceWithDbAccess.cfg with properties
    - dbname = The database name
    - user = The database user
    - password = The password for the user
    - server = The database host
    - port = The port the database is listening on
- After the config file is created the service will automagically get activated by the service component runtime in karaf
