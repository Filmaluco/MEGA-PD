package Helpers;

import java.util.Scanner;

enum Commands{
    EXIT, NOT_DEFINED;
}

/**
 * This class is meant to encapsulate the server commands
 *
 * @author FilipeA
 * @version 1.0
 */
public class CommandInterpreter{

    //--------------------------------------------------------------------------------------------------------------
    // Variables
    //--------------------------------------------------------------------------------------------------------------
    //Private variables
    private boolean status = true;
    Scanner scanner = new Scanner(System.in);
    String lastCommand;

    private final String EXIT = "exit";

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

        if (lastCommand.equalsIgnoreCase(EXIT)){
            status = false;
            return Commands.EXIT;
        }
        return Commands.NOT_DEFINED;
    }

    // Private Methods -----------------------------------------------------------------------------

}
