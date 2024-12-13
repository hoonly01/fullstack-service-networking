import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import serverstreaming.ServerStreamingGrpc;
import serverstreaming.Serverstreaming;

import java.io.IOException;

public class server {

    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(50051)
                .addService(new ServerStreamingService())
                .build();

        System.out.println("Starting server. Listening on port 50051.");
        server.start();
        server.awaitTermination();
    }

    static class ServerStreamingService extends ServerStreamingGrpc.ServerStreamingImplBase {
        @Override
        public void getServerResponse(Serverstreaming.Number request, StreamObserver<Serverstreaming.Message> responseObserver) {
            int numberOfMessages = request.getValue();

            System.out.println("Server processing gRPC server-streaming: " + numberOfMessages);

            for (int i = 1; i <= numberOfMessages; i++) {
                Serverstreaming.Message message = Serverstreaming.Message.newBuilder()
                        .setMessage("Message #" + i)
                        .build();

                responseObserver.onNext(message);
            }
            responseObserver.onCompleted();
        }
    }
}
