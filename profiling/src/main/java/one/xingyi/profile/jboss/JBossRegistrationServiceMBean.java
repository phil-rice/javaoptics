package one.xingyi.profile.jboss;

import javax.management.ObjectName;
import java.util.List;

public interface JBossRegistrationServiceMBean {
    List<ObjectName> getRegisteredMBeans();

}
