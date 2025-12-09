import dev.restate.sdk.endpoint.Endpoint;
import dev.restate.sdk.http.vertx.RestateHttpServer;
import my.example.Greeter;
import my.example.SimpleSaga;

public class Main {
    public static void main(String[] args) {
        RestateHttpServer.listen(Endpoint.bind(new Greeter()).bind(new SimpleSaga()));
    }
}