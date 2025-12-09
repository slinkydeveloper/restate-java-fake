# Restate Java SDK example unit tests

This project contains some examples on how to mock the `Context` to write unit tests.

To use the `FakeContext`, add the following dependency:

```xml
<dependency>
    <groupId>dev.restate</groupId>
    <artifactId>sdk-fake-api</artifactId>
    <version>${restate.version}</version>
    <scope>test</scope>
</dependency>
```

Example tests:

* [Greeter](./src/main/java/my/example/Greeter.java) and [GreeterTest](./src/test/java/my/example/GreeterTest.java) show a basic example.
* [SimpleSaga](./src/main/java/my/example/SimpleSaga.java) and [SimpleSagaTest](./src/test/java/my/example/SimpleSagaTest.java) show how to test a saga.
