package one.xingyi.profilingJsp;

import one.xingyi.helpers.StringHelper;
import one.xingyi.profile.IProfile;
import one.xingyi.profile.ProfileMBean;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProfilerTagIntTest {

    private static Server server;

    @BeforeAll
    public static void startJetty() throws Exception {
        server = new Server(8055);

        WebAppContext context = new WebAppContext();
        context.setDescriptor("src/test/webapp/WEB-INF/web.xml");
        context.setResourceBase("src/test/webapp");
        context.setContextPath("/");
        context.setParentLoaderPriority(true);
        context.setClassLoader(Thread.currentThread().getContextClassLoader());
        context.setAttribute("org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern", ".*/profiling-.*\\.jar$");
        server.setHandler(context);

        // Start the server
        server.start();
        ((ProfileMBean) ProfilerForTests.profiler).clear();
        if (!server.isStarted()) {
            throw new RuntimeException("Jetty server did not start");
        }
    }

    @AfterAll
    public static void stopJetty() throws Exception {
        server.stop();
    }


    @Test
    public void testProfileTag() throws Exception {
//        System.out.println(System.getProperty("java.class.path"));
        URL url = new URL("http://localhost:8055/profileTest.jsp");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        assertEquals(HttpURLConnection.HTTP_OK, connection.getResponseCode());
        String json = IProfile.jsonFor(ProfilerForTests.profiler);
        assertEquals("{'':{'count':0,'avg':0,'total':0,'<10ms':{'count':0,'total':0,'avg':0,'snapshot':0},'<100ms':{'count':0,'total':0,'avg':0,'snapshot':0},'<1s:':{'count':0,'total':0,'avg':0,'snapshot':0},'<10s':{'count':0,'total':0,'avg':0,'snapshot':0},'rest':{'count':0,'total':0,'avg':0,'snapshot':0}}," +
                        "'profileTag':{'count':1,'avg':1,'total':1,'<10ms':{'count':1,'total':1,'avg':1,'snapshot':1},'<100ms':{'count':0,'total':0,'avg':0,'snapshot':0},'<1s:':{'count':0,'total':0,'avg':0,'snapshot':0},'<10s':{'count':0,'total':0,'avg':0,'snapshot':0},'rest':{'count':0,'total':0,'avg':0,'snapshot':0}}," +
                        "'startEndProfileTag':{'count':1,'avg':1,'total':1,'<10ms':{'count':1,'total':1,'avg':1,'snapshot':1},'<100ms':{'count':0,'total':0,'avg':0,'snapshot':0},'<1s:':{'count':0,'total':0,'avg':0,'snapshot':0},'<10s':{'count':0,'total':0,'avg':0,'snapshot':0},'rest':{'count':0,'total':0,'avg':0,'snapshot':0}}}",
                StringHelper.toSingleQuotes(StringHelper.removeWhiteSpace(json)));
    }

}
