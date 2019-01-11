package Database;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json .JSONException;
import org.json.JSONObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * DBAccessor class.
 *
 * Has multiple methods that can retrieve data from and store data into the database.
 *
 * @author Group 3
 * @version 0.1
 */


public class DBAccessor {
    private static DBAccessor uniqueInstance = null;
    private static Connection connection = null;
    private PreparedStatement preparedStatement = null;
    private int counter = 0;

    public DBAccessor(DBmanager db) { //precondition dbExisis()
        if ((connection = db.getConnection()) == null) //connect to the city db
            System.err.println(">>> DBAccessor: The database doesn't exist ...");
    }

    // apply singleton design pattern to CityDao
    public static synchronized DBAccessor getInstance(DBmanager db) {
        if (uniqueInstance == null) uniqueInstance = new DBAccessor(db);
        return uniqueInstance;
    }

    //Function that retrieves data from the database and returns it as type ArrayList
    public ArrayList<String> getDeviceData() throws Exception {
        ArrayList<String> deviceData = new ArrayList<String>();

        preparedStatement = connection.prepareStatement("SELECT * FROM Devicedata");
        ResultSet result = preparedStatement.executeQuery();

        while (result.next()){
            deviceData.add("HardwareID: "+result.getString("HardwareID"));
            deviceData.add("DataNr: "+ result.getInt("DataNr"));
            deviceData.add("Temperature: "+ result.getDouble("temperature"));
            deviceData.add("Luminosity: "+ result.getDouble("luminosity"));
            deviceData.add("Humidity: "+ result.getDouble("humidity"));
            deviceData.add("Pressure: "+ result.getDouble("pressure"));
        }
        preparedStatement.close();
        return deviceData;
    }

    //Return list of devices as ArrayList
    public ArrayList<String> getHardwareNr() throws Exception {
        ArrayList<String> deviceNames = new ArrayList<String>();
        preparedStatement = connection.prepareStatement("SELECT * FROM Device");
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next())
            deviceNames.add(resultSet.getString("HardwareID"));
        preparedStatement.close();
        return deviceNames;
    }

    //Takes a message of type MqttMessage and stores it in the database
    public void insertData(MqttMessage message) {

        try {
            this.insertDevice(message);
            JSONObject payload_fields = new JSONObject(message.toString()).getJSONObject("payload_fields");
            JSONObject hardware_serial_o = new JSONObject(message.toString());
            double temperature = payload_fields.getDouble("temperature");
            double luminosity = payload_fields.getDouble("luminosity");
            double humidity = payload_fields.getDouble("humidity");
            double pressure = payload_fields.getDouble("pressure");
            String hardwareID = hardware_serial_o.getString("hardware_serial");
            preparedStatement = connection.prepareStatement("insert into DeviceData VALUES (?, ?, ?, ?, ?,?,NOW())");
            preparedStatement.setString(1, hardwareID);
            preparedStatement.setInt(2, counter++);
            preparedStatement.setDouble(3, temperature);
            preparedStatement.setDouble(4, humidity);
            preparedStatement.setDouble(5, luminosity);
            preparedStatement.setDouble(6, pressure);
            preparedStatement.executeUpdate();

        } catch (SQLException | JSONException se) {
            se.printStackTrace();
        }

    }

    // Takes a devicename as type MqttMessage and stores it in the database
    public Boolean insertDevice(MqttMessage message) {
        boolean exits = false;
        try {
            JSONObject hardware_serial_o = new JSONObject(message.toString());
            String hardware_serial = hardware_serial_o.getString("hardware_serial");
            String dev_id = hardware_serial_o.getString("dev_id");
            ArrayList<String> devices = this.getHardwareNr();
            for (String device : devices) {
                if (device.equalsIgnoreCase(hardware_serial)) {
                    exits = true;
                    System.out.println("Did find device!");
                }
            }
            if (!exits) {
                preparedStatement = connection.prepareStatement("insert into Device VALUES (?, ?, ?)");
                preparedStatement.setString(1, hardware_serial);
                preparedStatement.setString(2, dev_id);
                preparedStatement.setString(3, "Enschede");
                preparedStatement.executeUpdate();
            }
        } catch (Exception se) {
            se.printStackTrace();
        }
        return exits;
    }

    //Function that retrieves data from the database and returns it as type ArrayList
    public ArrayList<String> getTemperature() throws Exception {
        ArrayList<String> result = new ArrayList<>();
        preparedStatement = connection.prepareStatement("SELECT Temperature FROM DeviceData ORDER BY DataNr DESC LIMIT 1");
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next())
            result.add(resultSet.getString("Temperature"));
        preparedStatement.close();
        return result;
    }

    public ArrayList<String> getHumidity() throws Exception {
        ArrayList<String> result = new ArrayList<>();
        preparedStatement = connection.prepareStatement("SELECT Humidity FROM DeviceData ORDER BY DataNr DESC LIMIT 1");
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next())
            result.add(resultSet.getString("Humidity"));
        preparedStatement.close();
        return result;
    }

    public ArrayList<String> getLuminosity() throws Exception {
        ArrayList<String> result = new ArrayList<>();
        preparedStatement = connection.prepareStatement("SELECT Luminosity FROM DeviceData ORDER BY DataNr DESC LIMIT 1");
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next())
            result.add(resultSet.getString("Luminosity"));
        preparedStatement.close();
        return result;
    }

    public ArrayList<String> getPressure() throws Exception {
        ArrayList<String> result = new ArrayList<>();
        preparedStatement = connection.prepareStatement("SELECT Pressure FROM DeviceData ORDER BY DataNr DESC LIMIT 1");
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next())
            result.add(resultSet.getString("Pressure"));
        preparedStatement.close();
        return result;
    }

}
