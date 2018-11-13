package Core;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.*;

/**
 * This class must be initialized once with Log.initLog() before being used.
 * After being initialized, in order to be used all that has to be done is include this file and call the desired method
 * <br>
 * <b>Usage Example: </b><br>
 * <pre>
 *     import [path].Log;
 * {@code
 *      void method(){
 *          //produce a information log
 *          Log.i(message);
 *      }
 * }
 * </pre>
 *  All the logs created by this class are stored in the directory <i>Logs</i> of the project in which the class was created. <br>
 *  @author Vascoide
 *  @version 0.0.1
 */

public final class Log {
    public static final Logger LOGGER = Logger.getLogger(Log.class.getName());
    static Date date = null;
    static SimpleDateFormat dateFormat = null;
    static File file = null;
  //  static Handler consoleHandler = null;   // DEBUG
    static Handler fileHandler = null;
    static Formatter simpleFormatter = null;
    static boolean status = true;

    /**
     * Initializes the --- in order to save the file log file in the <i>MMM d, yyyy 'at' h-mm-ss a</i> format.
     */
    public static void initLog(){
        try{
            date = new Date();
            dateFormat = new SimpleDateFormat("MMM d, yyyy 'at' h-mm-ss a");
            file = new File("src/Logs/" + dateFormat.format(date) + ".log");
            file.createNewFile();
            //consoleHandler = new ConsoleHandler();
            fileHandler = new FileHandler(file.getPath());
            simpleFormatter = new SimpleFormatter();
            //LOGGER.addHandler(consoleHandler);
            LOGGER.addHandler(fileHandler);
            fileHandler.setFormatter(simpleFormatter);
            //consoleHandler.setLevel(Level.ALL);
            fileHandler.setLevel(Level.ALL);
            LOGGER.setLevel(Level.ALL);
        }catch(IOException e){
            System.out.println(e);
            status = false;
        }
    }

    /**
     * Issues a WARNING level message.
     * @param s message to put on the log
     */
    public static void w(String s){
        if(!status) return;
        LOGGER.warning("{" + s + "}");
    }

    /**
     * Issues a INFO level message
     * @param s message to put on the log
     */
    public static void i(String s){
        if(!status) return;
        LOGGER.info("{" + s + "}");
    }
}