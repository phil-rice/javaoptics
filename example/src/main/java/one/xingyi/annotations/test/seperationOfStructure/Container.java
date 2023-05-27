package one.xingyi.annotations.test.seperationOfStructure;

public record Container(String pfId) implements IMeansOfTransport {


    @Override
    public void doSomething() {

    }

    @Override
    public void doSomethingElse() {

    }
}
