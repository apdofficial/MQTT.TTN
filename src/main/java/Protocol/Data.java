package Protocol;

import Database.DBAccessor;
import Database.DBmanager;

import java.util.ArrayList;

/**
 * Data class.
 *
 * Class that retrieves Data (data sent retrieved from TTN) from the database.
 *
 * @author Group 3
 * @version 0.1
 */

public class Data implements Command {

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
            result = DBAccessor.getDeviceData();
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
        return "Data";
    }
    @Override
    public String getCommand() {
        return "Data";
    }
}
