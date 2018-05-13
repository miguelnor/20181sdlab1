package application;


import io.grpc.project.api.*;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import java.io.IOException;
import java.util.logging.Logger;

public class ProcessServer {
    private static final Logger logger = Logger.getLogger(ProcessServer.class.getName());

    private final int port;
    private final Server server;

    public ProcessServer(int port){
        this(ServerBuilder.forPort(port), port);
    }

    public ProcessServer(ServerBuilder<?> serverBuilder, int port){
        this.port = port;
        this.server = serverBuilder.addService(new ProcessService()).build();
    }

    public void start() throws IOException{
        server.start();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                // Use stderr here since the logger may has been reset by its JVM shutdown hook.
                System.err.println("*** shutting down gRPC server since JVM is shutting down");
                ProcessServer.this.stop();
                System.err.println("*** server shut down");
            }
        });
    }

    public void stop(){
        if(server != null){
            server.shutdown();
        }
    }

    public void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    private static class ProcessService extends ProcessGrpc.ProcessImplBase{

        @Override
        public void sendProcess(Request request, StreamObserver<Reply> responseObserver) {
            Reply reply = Reply.newBuilder().setMessage("Teste de retorno").build();
            responseObserver.onNext(reply);
            responseObserver.onCompleted();
        }
    }

}
