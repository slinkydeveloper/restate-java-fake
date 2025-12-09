package my.example;

import dev.restate.sdk.fake.FakeContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GreeterTest {

    @Test
    @DisplayName("Greeter will greet")
    public void doTest() {
        Greeter greeter = new Greeter();

        // Create a mock context using FakeContext.
        // The no-arg constructor of FakeContext will, by default, execute all ctx.run
        // It will also set fixed seed for ctx.random()
        FakeContext context = new FakeContext();

        String response = greeter.greet(context, "Francesco");
        assertEquals("You said hi to Francesco with random id bb1ad573-19b8-9cd8-68fb-0e6f684df992!", response);
    }

}
