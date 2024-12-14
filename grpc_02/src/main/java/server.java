import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import bidirectional.BidirectionalGrpc;
import bidirectional.BidirectionalOuterClass;

public class server {

    public static void main(String[] args) throws Exception {
        Server server = ServerBuilder.forPort(50051)
                .addService(new BidirectionalServiceImpl())
                .build();
        System.out.println("Starting server. Listening on port 50051.");
        server.start();
        server.awaitTermination();
    }

    static class BidirectionalServiceImpl extends BidirectionalGrpc.BidirectionalImplBase {

        @Override
        public StreamObserver<BidirectionalOuterClass.Message> getServerResponse(
                StreamObserver<BidirectionalOuterClass.Message> responseObserver) {
            return new StreamObserver<>() {
                @Override
                public void onNext(BidirectionalOuterClass.Message message) {
                    System.out.println("[client to server]: " + message.getMessage());
                    BidirectionalOuterClass.Message response = BidirectionalOuterClass.Message.newBuilder()
                            .setMessage("Server received: " + message.getMessage())
                            .build();
                    responseObserver.onNext(response);
                }

                @Override
                public void onError(Throwable t) {
                    System.err.println("Error occurred: " + t.getMessage());
                }
                @Override
                public void onCompleted() {
                    responseObserver.onCompleted();
                }
            };
        }
    }
}
