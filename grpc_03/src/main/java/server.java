import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import clientstreaming.ClientStreamingGrpc;
import clientstreaming.Clientstreaming;

public class server {

    public static void main(String[] args) throws Exception {
        Server server = ServerBuilder.forPort(50051)
                .addService(new ClientStreamingServiceImpl())
                .build();

        System.out.println("Starting server. Listening on port 50051.");
        server.start();
        server.awaitTermination();
    }

    static class ClientStreamingServiceImpl extends ClientStreamingGrpc.ClientStreamingImplBase {

        @Override
        public StreamObserver<Clientstreaming.Message> getServerResponse(
                StreamObserver<Clientstreaming.Number> responseObserver) {
            return new StreamObserver<>() {
                int messageCount = 0;

                @Override
                public void onNext(Clientstreaming.Message message) {
                    System.out.println("[client to server] " + message.getMessage());
                    messageCount++;
                }

                @Override
                public void onError(Throwable t) {}

                @Override
                public void onCompleted() {
                    Clientstreaming.Number response = Clientstreaming.Number.newBuilder()
                            .setValue(messageCount)
                            .build();
                    responseObserver.onNext(response);
                    responseObserver.onCompleted();
                }
            };
        }
    }
}
