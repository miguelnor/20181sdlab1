package application;

import application.configuration.ApplicationProperties;

import java.util.logging.Logger;

public class Main {

    static String port;
    private static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public static void main(String args[])
    {
        port = ApplicationProperties.getInstance().loadProperties().getProperty("site.port");
        logger.info("Porta: "+port);
    }


}
