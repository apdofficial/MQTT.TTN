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

public class Temperature implements Command {

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
            result = DBAccessor.getTemperature();
            if(result.isEmpty()) {
                throw new Exception();
            }
        }
        catch(Exception E){
            System.out.println("Empty result for: Temperature");
        }
        return result;
    }

    @Override
    public String getDescription() {
        return "Temperature";
    }
    @Override
    public String getCommand() {
        return "Temperature";
    }
}
