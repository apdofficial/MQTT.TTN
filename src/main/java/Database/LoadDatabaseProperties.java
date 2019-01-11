package Database;

import java.io.FileInputStream;
import java.util.Properties;

/**
 * LoadDatabaseProperties class.
 *
 * Class that retrieves the properties for the running database from a properties file
 *
 * @author Group 3
 * @version 0.1
 */

public class LoadDatabaseProperties {
    private Properties props ;

    public LoadDatabaseProperties(String property_name)  {
        props = new Properties();
        FileInputStream in ;
        try {
            in = new FileInputStream(property_name);
            props.load(in);
            in.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getProperty(String code) {
        return  props.getProperty(code);
    }
}