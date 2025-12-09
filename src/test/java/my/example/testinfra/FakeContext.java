package my.example.testinfra;

import dev.restate.common.Request;
import dev.restate.common.function.ThrowingSupplier;
import dev.restate.sdk.*;
import dev.restate.sdk.common.*;
import dev.restate.sdk.endpoint.definition.HandlerContext;
import dev.restate.serde.TypeTag;

import java.time.Duration;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.Executor;

/**
 * Fake {@link Context}, see {@link ContextExpectations} for more details.
 * <p>
 * You can pass this to Restate handlers.
 *
 * @see ContextExpectations
 */
public class FakeContext implements Context, ObjectContext, SharedObjectContext, WorkflowContext, SharedWorkflowContext {

    private final WorkflowContext inner;

    /**
     * Fake {@link Context} with default expectations.
     *
     * @see ContextExpectations
     */
    public FakeContext() {
        this(new ContextExpectations());
    }

    /**
     * Fake {@link Context}, see {@link ContextExpectations} for more details.
     *
     * @see ContextExpectations
     */
    public FakeContext(ContextExpectations expectations) {
        // The mocking works as follows:

        // FakeContext -- calls --> ContextImpl -- calls --> FakeHandlerContext
        // * FakeHandlerContext implements the mocking behavior
        // * ContextImpl is the real implementation of Context, which delegates all the "restate" operations to the HandlerContext implementation
        // * FakeContext is just a nice wrapper around ContextImpl for ease of use.

        FakeHandlerContext fakeHandlerContext = new FakeHandlerContext(expectations);
        this.inner = createContextImpl(fakeHandlerContext, expectations);
    }

    // -- Delegate to internal interface mock

    @Override
    public HandlerRequest request() {
        return inner.request();
    }

    @Override
    public <T, R> CallDurableFuture<R> call(Request<T, R> request) {
        return inner.call(request);
    }

    @Override
    public <T, R> InvocationHandle<R> send(Request<T, R> request, Duration duration) {
        return inner.send(request, duration);
    }

    @Override
    public <R> InvocationHandle<R> invocationHandle(String s, TypeTag<R> typeTag) {
        return inner.invocationHandle(s, typeTag);
    }

    @Override
    public DurableFuture<Void> timer(String s, Duration duration) {
        return inner.timer(s, duration);
    }

    @Override
    public <T> DurableFuture<T> runAsync(String s, TypeTag<T> typeTag, RetryPolicy retryPolicy, ThrowingSupplier<T> throwingSupplier) throws TerminalException {
        return inner.runAsync(s, typeTag, retryPolicy, throwingSupplier);
    }

    @Override
    public <T> Awakeable<T> awakeable(TypeTag<T> typeTag) {
        return inner.awakeable(typeTag);
    }

    @Override
    public AwakeableHandle awakeableHandle(String s) {
        return inner.awakeableHandle(s);
    }

    @Override
    public RestateRandom random() {
        return inner.random();
    }

    @Override
    public void clear(StateKey<?> stateKey) {
        inner.clear(stateKey);
    }

    @Override
    public void clearAll() {
        inner.clearAll();
    }

    @Override
    public <T> void set(StateKey<T> stateKey, T t) {
        inner.set(stateKey, t);
    }

    @Override
    public <T> DurablePromise<T> promise(DurablePromiseKey<T> durablePromiseKey) {
        return inner.promise(durablePromiseKey);
    }

    @Override
    public <T> DurablePromiseHandle<T> promiseHandle(DurablePromiseKey<T> durablePromiseKey) {
        return inner.promiseHandle(durablePromiseKey);
    }

    @Override
    public String key() {
        return inner.key();
    }

    @Override
    public <T> Optional<T> get(StateKey<T> stateKey) {
        return inner.get(stateKey);
    }

    @Override
    public Collection<String> stateKeys() {
        return inner.stateKeys();
    }

    private static WorkflowContext createContextImpl(HandlerContext fakeHandlerContext, ContextExpectations expectations)  {
       try {
           // Some reflections to call the private constructor of ContextImpl
           Class<?> contextImplClass = Class.forName("dev.restate.sdk.ContextImpl");
           var constructor = contextImplClass.getDeclaredConstructor(
                   HandlerContext.class,
                   Executor.class,
                   dev.restate.serde.SerdeFactory.class
           );
           constructor.setAccessible(true);
           return (WorkflowContext) constructor.newInstance(
                   fakeHandlerContext,
                   (Executor) Runnable::run,
                   expectations.serdeFactory()
           );
       } catch (Exception e) {
           throw new RuntimeException("Failed to instantiate ContextImpl", e);
       }
    }
}
