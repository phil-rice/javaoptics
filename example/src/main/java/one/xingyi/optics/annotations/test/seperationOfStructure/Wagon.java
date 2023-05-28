package one.xingyi.optics.annotations.test.seperationOfStructure;

import one.xingyi.optics.annotations.Optics;

@Optics
public record Wagon(String pfId) implements IMeansOfTransport {

    @Override
    public void doSomething() {

    }

    @Override
    public void doSomethingElse() {

    }
}
