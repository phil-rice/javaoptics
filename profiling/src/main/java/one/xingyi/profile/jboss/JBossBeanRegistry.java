package one.xingyi.profile.jboss;

import one.xingyi.helpers.StringHelper;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class JBossBeanRegistry {
    private static final ConcurrentHashMap<String, JBossBeanRegistry> SERVICES_BY_URL = new ConcurrentHashMap<>();

    private final List<ObjectName> mbeanNames = Collections.synchronizedList(new ArrayList<>());

    private JBossBeanRegistry() {
    }

    public static JBossBeanRegistry getInstance(String companyUrl) {
        return SERVICES_BY_URL.computeIfAbsent(companyUrl, url -> new JBossBeanRegistry());
    }


    public List<ObjectName> getRegisteredMBeans() {
        return Collections.unmodifiableList(mbeanNames);
    }


    public void register(String companyUrl, ObjectName mbeanName) {
        JBossBeanRegistry service = getInstance(companyUrl);
        service.mbeanNames.add(mbeanName);
    }

    public void unregister(String companyUrl, ObjectName mbeanName) {
        JBossBeanRegistry service = getInstance(companyUrl);
        service.mbeanNames.remove(mbeanName);
    }


}
