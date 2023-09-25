package one.xingyi.profile;

import one.xingyi.interfaces.INanoTime;
import one.xingyi.interfaces.RunnableWithExceptionE;
import one.xingyi.interfaces.SupplierWithExceptionE;
import one.xingyi.profile.pathmap.IPathMap;

import java.util.function.Consumer;

public interface IProfile {
    /**
     * Starts a profiling tree. Almost always you will want to register this as an MBean. This hasn't been done automatically incase you want to do something else with it.
     * This will almost always be a 'final static' field, and is often defined in a class made by the user called 'Profile' or 'Profiler'
     *
     *
     * <p>
     * Normally this profiling tree is 'global' and the profiler not actually used. Usually a child would be made for each class that is being profiled
     * <br />
     * <p>Example</p>
     * static final IProfile main = IProfile.root("one.xingyi.profile", INanoTime.testNanoTime()).registerMBean();
     *
     * @param jmxName  is often a package name. Like 'org.example'. Just has to be unique. It is used to register MBeans
     * @param nanoTime is the clock to use. Often INanoTime.realNanoTime. For tests consider INanoTime.testNanoTime()
     */
    static IProfile root(String jmxName, INanoTime nanoTime) {return new Profile(jmxName, nanoTime, IPathMap.make(ProfileBuckets::create), e ->{});}
    /**
     * Create a child of this profile. There are three main reasons for doing this: <ul>
     * <li>In a class, with the name of the class as the 'name'. This would often be registered as an mbean (call .registerMBean)</li>
     * <li>For a particular method, with the name of the method as the 'name'. Unless the class is a 'god class' with lots of 'entry level' methods in it this wouldn't normally be registered as a MBean</li>
     * <li> Sometimes methods are huge... 100s of lines. We can profile separate parts of this, and we can give those a name. Often this is a clue you should refactor the method!</li>
     * </ul>
     */
    IProfile child(String name);
    /**
     * Time how longs it takes to run the supplier. The result of the supplier is returned.
     */
    <T, E extends Exception> T profile(SupplierWithExceptionE<T, E> fn) throws E;
    /**
     * Time how longs it takes to run the supplier. This is identically to child(name).profile(supplier). The result of the supplier is returned.
     */
    default <T, E extends Exception> T profile(String name, SupplierWithExceptionE<T, E> fn) throws E {return child(name).profile(fn);}
    /**
     * Time how longs it takes to run the runnable.
     */
    <E extends Exception> void run(RunnableWithExceptionE<E> fn) throws E;
    /**
     * Time how longs it takes to run the runnable. This is identically to child(name).run(runnable).
     */
    default <E extends Exception> void run(String name, RunnableWithExceptionE<E> fn) throws E {child(name).run(fn);}
    /**
     * The profiler is registered as a MBean. If you call it twice it will throw exceptions, so usually this is done to 'static final' profile
     */
    IProfile registerMBean();
    IProfile withErrorHandler(Consumer<Exception> errorHandler);

    /**
     * Useful when debugging or manually you want to dump the results of the profiler
     */
    static String jsonFor(IProfile profile) {
        if (!(profile instanceof Profile))
            throw new RuntimeException("Not a Profile. Class is " + profile.getClass().getName());
        Profile p = (Profile) profile;
        return p.json();
    }
}

