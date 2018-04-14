package application;

import application.configuration.ApplicationProperties;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    private static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public static void main(String args[])
    {

        logger.setLevel(Level.FINE);
        String port = ApplicationProperties.getInstance().loadProperties().getProperty("server.port");
        logger.info("Server port: "+port);

        for(int i=0;i<4;i++){
            new ThreadResults("Thread "+i).start();
        }
    }
}
