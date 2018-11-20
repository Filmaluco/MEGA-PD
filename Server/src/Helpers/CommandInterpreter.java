package Helpers;

import java.util.Scanner;

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
    Scanner scanner = new Scanner(System.in);
    String lastCommand;

    private final String SHUTDOWN = "shutdown";

    //Public variables
    public enum Commands{
        SHUTDOWN, NOT_DEFINED
    }



    //----------------------------------------------------------------------------------------------
    //      GETTERS
    //----------------------------------------------------------------------------------------------


    public boolean isAlive() {
        return status;
    }

    //----------------------------------------------------------------------------------------------
    //      SETTERS
    //----------------------------------------------------------------------------------------------

    //----------------------------------------------------------------------------------------------
    //      Methods
    //----------------------------------------------------------------------------------------------

    // Public Methods ------------------------------------------------------------------------------

    /**
     * starts the core of the class
     */
    public Commands read() {

        lastCommand = scanner.nextLine();

        if (lastCommand.equalsIgnoreCase(SHUTDOWN)){
            status = false;
            return Commands.SHUTDOWN;
        }
        return Commands.NOT_DEFINED;
    }

    // Private Methods -----------------------------------------------------------------------------

}
