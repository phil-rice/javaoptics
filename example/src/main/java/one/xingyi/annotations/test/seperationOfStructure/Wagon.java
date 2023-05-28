package one.xingyi.annotations.test.seperationOfStructure;

import one.xingyi.annotations.optics.Optics;

@Optics
public record Wagon(String pfId) implements IMeansOfTransport {

    @Override
    public void doSomething() {

    }

    @Override
    public void doSomethingElse() {

    }
}
