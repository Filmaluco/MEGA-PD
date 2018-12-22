package Helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * This class is meant to encapsulate the server commands
 *
 * @author FilipeA
 * @version 1.2
 */
public class CommandInterpreter{

    //--------------------------------------------------------------------------------------------------------------
    // Variables
    //--------------------------------------------------------------------------------------------------------------
    //Private variables
    private boolean status = true;
    private Scanner scanner = new Scanner(System.in);

    private final String SHUTDOWN = "shutdown";
    private final String ADD = "add";
    private final String HELP = "help";

    private int argC =-1;
    private List<String> commandArguments;

    //Public variables

    /**
     * Acceptable commands
     */
    public enum Commands{
        SHUTDOWN, NOT_DEFINED, ADD, HELP
    }

    //----------------------------------------------------------------------------------------------
    //      CONSTRUCTOR
    //----------------------------------------------------------------------------------------------


    public CommandInterpreter() {
        commandArguments = new ArrayList<>();
    }

    //----------------------------------------------------------------------------------------------
    //      GETTERS
    //----------------------------------------------------------------------------------------------
    public boolean isAlive() {
        return status;
    }

    public boolean hasArguments(){
        return  argC+1 <= commandArguments.size();
    }

    public String nextArgument(){
        return commandArguments.get(argC--);
    }

    //----------------------------------------------------------------------------------------------
    //      Methods
    //----------------------------------------------------------------------------------------------
    // Public Methods ------------------------------------------------------------------------------

    /**
     * Returns a enumeration with the acceptable commands
     */
    public Commands read() {

        String lastCommand = scanner.nextLine();
        argC = -1;
        commandArguments.clear();
        StringTokenizer st = new StringTokenizer(lastCommand);

        if (lastCommand.equalsIgnoreCase(SHUTDOWN)){
            status = false;
            return Commands.SHUTDOWN;
        }

        if (lastCommand.equalsIgnoreCase(HELP)){
            return Commands.HELP;
        }

        if(st.nextToken().contains(ADD)){
            while (st.hasMoreTokens()){
                argC++;
                commandArguments.add(st.nextToken());
            }
            return Commands.ADD;
        }

        return Commands.NOT_DEFINED;
    }

    // Private Methods -----------------------------------------------------------------------------

}
