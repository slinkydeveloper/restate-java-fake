import dev.restate.sdk.Context;
import dev.restate.sdk.annotation.Handler;
import dev.restate.sdk.annotation.Service;
import dev.restate.sdk.common.RetryPolicy;
import dev.restate.sdk.common.TerminalException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service
public class SimpleSaga {

    private static final Logger LOG = LogManager.getLogger(SimpleSaga.class);

    @Handler
    public String doSaga(Context ctx) {
        String result;
        try {
            result = ctx.run("do", String.class,
                    // Set a retry policy
                    RetryPolicy.defaultPolicy().setMaxAttempts(10),
                    () -> {
                        // Let's assume this always fails
                        LOG.info("Doing work");
                        throw new IllegalStateException("error when running do");
                    });
        } catch (TerminalException e) {
            // When TerminalException thrown, undo with another ctx.run
            result = ctx.run("undo", String.class, () -> {
                LOG.info("Undoing previous work");
                return "undone";
            });
        }

        return result;
    }

}
