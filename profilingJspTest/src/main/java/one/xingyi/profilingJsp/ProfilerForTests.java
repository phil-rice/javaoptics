package one.xingyi.profilingJsp;

import one.xingyi.interfaces.INanoTime;
import one.xingyi.profile.IProfile;

public class ProfilerForTests {
   public  static IProfile profiler = IProfile.root("myJmxName", INanoTime.testNanoTime());
}
