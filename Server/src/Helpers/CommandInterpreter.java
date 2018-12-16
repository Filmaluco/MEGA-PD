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
    private Scanner scanner = new Scanner(System.in);

    private final String SHUTDOWN = "shutdown";

    //Public variables

    /**
     * Acceptable commands
     */
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
    //      Methods
    //----------------------------------------------------------------------------------------------
    // Public Methods ------------------------------------------------------------------------------

    /**
     * Returns a enumeration with the acceptable commands
     */
    public Commands read() {

        String lastCommand = scanner.nextLine();

        if (lastCommand.equalsIgnoreCase(SHUTDOWN)){
            status = false;
            return Commands.SHUTDOWN;
        }
        return Commands.NOT_DEFINED;
    }

    // Private Methods -----------------------------------------------------------------------------

}
