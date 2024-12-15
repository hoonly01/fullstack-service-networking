import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import clientstreaming.ClientStreamingGrpc;
import clientstreaming.Clientstreaming;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class client {

    public static void main(String[] args) throws Exception {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();

        ClientStreamingGrpc.ClientStreamingStub stub = ClientStreamingGrpc.newStub(channel);

        CountDownLatch latch = new CountDownLatch(1);

        StreamObserver<Clientstreaming.Message> requestObserver = stub.getServerResponse(new StreamObserver<>() {
            @Override
            public void onNext(Clientstreaming.Number response) {
                System.out.println("[server to client] " + response.getValue());
            }

            @Override
            public void onError(Throwable t) {
                System.err.println("Error occurred: " + t.getMessage());
                latch.countDown();
            }

            @Override
            public void onCompleted() {
                latch.countDown();
            }
        });

        for (int i = 1; i <= 5; i++) {
            Clientstreaming.Message message = Clientstreaming.Message.newBuilder()
                    .setMessage("Message #" + i)
                    .build();
            requestObserver.onNext(message);
            Thread.sleep(500);
        }

        requestObserver.onCompleted();
        latch.await(1, TimeUnit.MINUTES);
        channel.shutdown();
    }
}
