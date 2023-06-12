package one.xingyi.optics.annotations.test.seperationOfStructure;

import java.util.Objects;

public final class ChassisTroncon implements IMeansOfTransport {
    private final String pfId;
    public ChassisTroncon(String pfId) {this.pfId = pfId;}


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
    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ChassisTroncon) obj;
        return Objects.equals(this.pfId, that.pfId);
    }
    @Override
    public int hashCode() {
        return Objects.hash(pfId);
    }
    @Override
    public String toString() {
        return "ChassisTroncon[" +
                "pfId=" + pfId + ']';
    }

}
