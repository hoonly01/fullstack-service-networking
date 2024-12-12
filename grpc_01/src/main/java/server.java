// Java gRPC Server Implementation
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

public class server {

    public static void main(String[] args) throws Exception {
        Server server = ServerBuilder.forPort(50051)
                .addService(new MyServiceImpl())
                .build();

        System.out.println("Starting server. Listening on port 50051.");
        server.start();
        server.awaitTermination();
    }

    static class MyServiceImpl extends MyServiceGrpc.MyServiceImplBase {

        @Override
        public void myFunction(HelloGrpc.MyNumber request, StreamObserver<HelloGrpc.MyNumber> responseObserver) {
            int inputNumber = request.getValue();
            System.out.println("gRPC result: " + inputNumber);

            int result = hello_grpc.myFunc(inputNumber);
            HelloGrpc.MyNumber response = HelloGrpc.MyNumber.newBuilder()
                    .setValue(result)
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }
}
