package one.xingyi.annotations.test.seperationOfStructure;

public record ChassisTroncon(String pfId) implements IMeansOfTransport {


    @Override
    public String pfId() {
        return null;
    }

    @Override
    public void doSomething() {

    }

    @Override
    public void doSomethingElse() {

    }
}
