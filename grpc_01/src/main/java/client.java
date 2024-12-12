// Java gRPC Client Implementation
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class client {

    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();

        MyServiceGrpc.MyServiceBlockingStub stub = MyServiceGrpc.newBlockingStub(channel);

        HelloGrpc.MyNumber request = HelloGrpc.MyNumber.newBuilder()
                .setValue(4)
                .build();

        HelloGrpc.MyNumber response = stub.myFunction(request);
        System.out.println("Received response: " + response.getValue());

        channel.shutdown();
    }
}
