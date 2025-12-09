import dev.restate.sdk.fake.ContextExpectations;
import dev.restate.sdk.fake.FakeContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SimpleSagaTest {

    @Test
    @DisplayName("When ctx.run fails, the exception is propagated")
    public void verifyException() {
        SimpleSaga simpleSaga = new SimpleSaga();

        // The no-arg constructor of FakeContext will, by default, execute all ctx.run
        // In case of a failure, the exception is propagated as is.
        FakeContext context = new FakeContext();

        IllegalStateException actual = assertThrows(IllegalStateException.class, () -> simpleSaga.doSaga(context));
        assertEquals("error when running do", actual.getMessage());
    }

    @Test
    @DisplayName("When ctx.run is not retried, the error gets converted to TerminalException")
    public void verifySagaLogic() {
        SimpleSaga simpleSaga = new SimpleSaga();

        FakeContext context = new FakeContext(
                new ContextExpectations()
                        // ContextExpectations.dontRetryRun will try to execute the ctx.run
                        // and in case of a failure, will convert its error to terminal
                        .dontRetryRun("do")
        );

        String result = simpleSaga.doSaga(context);
        assertEquals("undone", result);
    }

}
