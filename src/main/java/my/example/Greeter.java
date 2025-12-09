package my.example;

import dev.restate.sdk.Context;
import dev.restate.sdk.annotation.Handler;
import dev.restate.sdk.annotation.Service;

@Service
public class Greeter {

    @Handler
    public String greet(Context ctx, String name) {
        return "You said hi to " + name + " with random id " + ctx.random().nextUUID() + "!";
    }
}
