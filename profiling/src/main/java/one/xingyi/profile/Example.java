package one.xingyi.profile;

import one.xingyi.interfaces.INanoTime;


public class Example {

    static final IProfile main = IProfile.root("one.xingyi.profile", INanoTime.testNanoTime()).registerMBean();
    static final IProfile here = main.child("here");
    static final IProfile p1 = here.child("one").registerMBean();
    static final IProfile p2 = here.child("two").registerMBean();
    public static void main(String[] args) throws InterruptedException {

        int count = 0;
        while (true) {
            Thread.sleep(1000);
            p1.run(() -> p1.run("a", () -> {}));

            p1.profile(() -> "");
            p2.run(() -> p2.run("b", () -> {}));
            p2.profile(() -> "");
            if (count++ % 10 == 0)
                System.out.println(IProfile.jsonFor(main));
        }
    }
}