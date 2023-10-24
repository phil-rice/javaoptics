package one.xingyi.profile.jboss;

import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

public class MBeanRegistry {
    private static List<String> mbeanNames = Collections.synchronizedList(new ArrayList<>());

    public static void registerMBean(String mbeanName) {
        mbeanNames.add(mbeanName);
    }

    public static List<String> getRegisteredMBeans() {
        return Collections.unmodifiableList(mbeanNames);
    }
}
