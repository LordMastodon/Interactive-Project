package electricdrill.interactiveproject.wificommunication;

import com.illposed.osc.OSCPort;
import com.illposed.osc.OSCPortIn;
import com.illposed.osc.OSCPortOut;
import org.junit.*;
import org.junit.rules.ExpectedException;

public class Port {

    private static final long WAIT_FOR_SOCKET_CLOSE = 30;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private OSCPortOut sender;
    private OSCPortIn reciever;

    @Before
    public void setUp() throws Exception {
        sender = new OSCPortOut();
        reciever = new OSCPortIn(OSCPort.defaultSCOSCPort());
    }

    @After
    public void tearDown() throws Exception {
        reciever.close();
        sender.close();

        Thread.sleep(WAIT_FOR_SOCKET_CLOSE);
    }

    @Test
    public void testSocketClose() throws Exception {
        reciever.close();
        sender.close();

        Thread.sleep(WAIT_FOR_SOCKET_CLOSE);

        sender = new OSCPortOut();
        reciever = new OSCPortIn(OSCPort.defaultSCOSCPort());
    }

    @Test
    public void testSocketAutoClose() throws Exception {
        reciever = null;
        sender = null;

        System.gc();
        Thread.sleep(WAIT_FOR_SOCKET_CLOSE);

        sender = new OSCPortOut();
        reciever = new OSCPortIn(OSCPort.defaultSCOSCPort());
    }

    @Test
    public void testPorts() throws Exception {
        Assert.assertEquals("Bad default SuperCollider OSC port",
                57110, OSCPort.defaultSCOSCPort());
        Assert.assertEquals("Bad default SuperCollider Language OSC port",
                );
    }

}
