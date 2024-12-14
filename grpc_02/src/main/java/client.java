import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import bidirectional.BidirectionalGrpc;
import bidirectional.BidirectionalOuterClass;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class client {

    public static void main(String[] args) throws Exception {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();

        BidirectionalGrpc.BidirectionalStub stub = BidirectionalGrpc.newStub(channel);

        CountDownLatch latch = new CountDownLatch(1);

        StreamObserver<BidirectionalOuterClass.Message> requestObserver = stub.getServerResponse(new StreamObserver<>() {
            @Override
            public void onNext(BidirectionalOuterClass.Message message) {
                System.out.println("[server to client]: " + message.getMessage());
            }

            @Override
            public void onError(Throwable t) {}

            @Override
            public void onCompleted() {}
        });

        for (int i = 1; i <= 5; i++) {
            BidirectionalOuterClass.Message message = BidirectionalOuterClass.Message.newBuilder()
                    .setMessage("Message #" + i)
                    .build();
            requestObserver.onNext(message);
        }

        requestObserver.onCompleted();
        latch.await(1, TimeUnit.MINUTES);
        channel.shutdown();
    }
}
