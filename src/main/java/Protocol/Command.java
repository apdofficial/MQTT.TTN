package Protocol;

import java.util.ArrayList;

public interface Command {
    ArrayList<String> process(String request) throws Exception;
    String getDescription();
    String getCommand();

}
