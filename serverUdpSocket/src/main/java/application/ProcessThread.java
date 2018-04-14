package application;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class ProcessThread extends Thread {

    final String CREATE = "1",
    READ = "2",
    UPDATE = "3",
    DELETE = "4";

    private String logFile;
    private Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private Map<BigInteger,String> mapa = new HashMap<>();

    public ProcessThread(String logFile) {
        this.logFile = logFile;
    }

    @Override
    public void run() {
        processLogFile();
        while(true){
            Process process = Server.processQueue.poll();
            if(process != null){
                String[] splited = process.getRequest().split("'");
                String CRUD = splited[0];
                BigInteger key = new BigInteger(splited[1]);
                String value = splited[2];
                if(CRUD != READ)
                    processCUD(CRUD,key,value);
                else {
                    mapRead(key);
                }
            }
        }
    }

    private boolean processCUD(String crud, BigInteger key, String value) {
        switch (crud){
            case CREATE: return mapCreate(key,value);
            case UPDATE: return mapUpdate(key,value);
            case DELETE: return mapDelete(key);
            default: return false;
        }
    }

    private boolean mapDelete(BigInteger key) {
        mapa.remove(key);
        return true;
    }

    private boolean mapUpdate(BigInteger key, String value) {
        if(!mapa.containsKey(key))
            return false;
        mapa.replace(key,value);
        return true;
    }

    private String mapRead(BigInteger key) {
        return mapa.get(key);
    }

    private boolean mapCreate(BigInteger key, String value) {
        mapa.put(key,value);
        return true;
    }

    //TODO ao inicializar, processar o log antes de ir para fila
    private void processLogFile(){

    }

}
