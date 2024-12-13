import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import serverstreaming.ServerStreamingGrpc;
import serverstreaming.Serverstreaming;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class client {

    public static void main(String[] args) throws InterruptedException {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();
        ServerStreamingGrpc.ServerStreamingStub stub = ServerStreamingGrpc.newStub(channel);
        CountDownLatch latch = new CountDownLatch(1);
        Serverstreaming.Number request = Serverstreaming.Number.newBuilder()
                .setValue(5)
                .build();

        stub.getServerResponse(request, new StreamObserver<>() {
            @Override
            public void onNext(Serverstreaming.Message message) {
                System.out.println("[server to client] " + message.getMessage());
            }

            @Override
            public void onError(Throwable t) {}

            @Override
            public void onCompleted() {}
        });

        latch.await(1, TimeUnit.MINUTES);channel.shutdown();
    }
}
