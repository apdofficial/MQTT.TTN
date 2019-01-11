package Protocol;

import Database.DBAccessor;
import Database.DBmanager;

import java.util.ArrayList;

/**
 * DeviceNames class.
 *
 * Class that retrieves DeviceNames from the database.
 *
 * @author Group 3
 * @version 0.1
 */

public class DeviceNames implements Command {

    //method process the command and return result
    public ArrayList<String> process(String request) {
        ArrayList<String> result = new ArrayList<>();

        if (!request.trim().equalsIgnoreCase(this.getDescription())){
            return result;
        }

        try {
            DBmanager dbManager = new DBmanager();
            dbManager.makeConnection();
            DBAccessor DBAccessor = new DBAccessor(dbManager);
            result = DBAccessor.getHardwareNr();
            if(result.isEmpty()) {
                throw new Exception("Empty result");
            }
        }
        catch(Exception E){
            E.printStackTrace();
        }
        return result;
    }

    @Override
    public String getDescription() {
        return "DeviceNames";
    }
    @Override
    public String getCommand() {
        return "DeviceNames";
    }
}
