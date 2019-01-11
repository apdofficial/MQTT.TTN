package Protocol;


import java.util.ArrayList;
import java.util.List;

/**
 * CommandProcessor class.
 *
 * Class that processes the commands.
 *
 * @author Group 3
 * @version 0.1
 */

public class CommandProcessor {
    private List<Command> commands;

    public CommandProcessor() { commands =  new ArrayList<>();}

    //method returns list with description of commands in Protocol
    public List<String> displayProtocol(){
        List<String> result = new ArrayList<>();
        for(Command o : this.commands) result.add(o.getDescription());
        return result;
    }

    //method adding command into Protocol
    public boolean addCommand(Command operation) {
        for (Command command : commands){
            if (command.getCommand().equals(operation.getCommand())){
                return false;
            }
        }

        commands.add(operation);
        return true;
    }

    //method process the request and returns the result:
    public String processRequest(String request) {
        //chain of responsibility
        for (Command command : commands){
            try {
                ArrayList<String> result = command.process(request);
                if (!result.isEmpty())
                    return result.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return "Command not supported";
    }

}


