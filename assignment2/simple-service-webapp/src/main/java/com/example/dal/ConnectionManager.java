package com.example.dal;

/**
 * Created by ChangLiu on 10/25/18.
 */

import com.mchange.v2.c3p0.ComboPooledDataSource;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Use ConnectionManager to connect to your database instance.
 *
 * ConnectionManager uses the MySQL Connector/J driver to connect to your local MySQL instance.
 *
 * In our example, we will create a DAO (data access object) java class to interact with
 * each MySQL table. The DAO java classes will use ConnectionManager to open and close
 * connections.
 *
 * Instructions:
 * 1. Install MySQL Community Server. During installation, you will need to set up a user,
 * password, and port. Keep track of these values.
 * 2. Download and install Connector/J: http://dev.mysql.com/downloads/connector/j/
 * 3. Add the Connector/J JAR to your buildpath. This allows your application to use the
 * Connector/J library. You can add the JAR using either of the following methods:
 *   A. When creating a new Java project, on the "Java Settings" page, go to the
 *   "Libraries" tab.
 *   Click on the "Add External JARs" button.
 *   Navigate to the Connector/J JAR. On Windows, this looks something like:
 *   C:\Program Files (x86)\MySQL\Connector.J 5.1\mysql-connector-java-5.1.34-bin.jar
 *   B. If you already have a Java project created, then go to your project properties.
 *   Click on the "Java Build Path" option.
 *   Click on the "Libraries" tab, click on the "Add External Jars" button, and
 *   navigate to the Connector/J JAR.
 * 4. Update the "private final" variables below.
 */
public class ConnectionManager {

    // User to connect to your database instance. By default, this is "root2".
    private final static String MYSQL_USERNAME = "mysqladmin";
    // Password for the user.
    private final static String MYSQL_PASSWORD = "mysqlpassword";
    // URI to your database server. If running on the same machine, then this is "localhost".
    private final static String AWS_MYSQL_HOSTNAME = "mysql-instance1.cpxpvza17dep.us-west-2.rds.amazonaws.com";
    // Port to your database server. By default, this is 3307.
    private final static int AWS_MYSQL_PORT = 3306;
    // Name of the MySQL schema that contains your tables.
    private final static String AWS_MYSQL_SCHEMA = "bsds";

    private static DataSource dataSource = setupDataSource();

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    private static DataSource setupDataSource() {
        ComboPooledDataSource cpds = new ComboPooledDataSource();
        try {
            cpds.setDriverClass("com.mysql.jdbc.Driver");
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
        cpds.setJdbcUrl("jdbc:mysql://" + AWS_MYSQL_HOSTNAME + ":" + AWS_MYSQL_PORT + "/" + AWS_MYSQL_SCHEMA);
        cpds.setUser(MYSQL_USERNAME);
        cpds.setPassword(MYSQL_PASSWORD);
        cpds.setMinPoolSize(1);
        cpds.setAcquireIncrement(5);
        cpds.setMaxPoolSize(60);
        return cpds;
    }
}
