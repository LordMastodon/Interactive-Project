package electricdrill.interactiveproject.wificommunication;

import com.illposed.osc.*;
import org.junit.*;
import org.junit.rules.ExpectedException;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class PortSuper {

    private static final long WAIT_FOR_SOCKET_CLOSE = 30;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private PortOut sender;
    private OSCPortIn reciever;

    @Before
    public void setUp() throws Exception {
        sender = new PortOut();
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

        sender = new PortOut();
        reciever = new OSCPortIn(OSCPort.defaultSCOSCPort());
    }

    @Test
    public void testSocketAutoClose() throws Exception {
        reciever = null;
        sender = null;

        System.gc();
        Thread.sleep(WAIT_FOR_SOCKET_CLOSE);

        sender = new PortOut();
        reciever = new OSCPortIn(OSCPort.defaultSCOSCPort());
    }

    @Test
    public void testPorts() throws Exception {
        Assert.assertEquals("Bad default SuperCollider OSC port",
                57110, OSCPort.defaultSCOSCPort());
        Assert.assertEquals("Bad default SuperCollider Language OSC port",
                57120, OSCPort.defaultSCOSCPort());
        Assert.assertEquals("Bad default port with ctor()",
                57110, sender.getPort());

        sender.close();
        sender = new PortOut(InetAddress.getLocalHost());
        Assert.assertEquals("Bad default port with ctor(address)",
                57110, sender.getPort());

        sender.close();
        sender = new PortOut(InetAddress.getLocalHost(), 12345);
        Assert.assertEquals("Bad port with ctor(address, port)",
                12345, sender.getPort());
    }

    @Test
    public void testStart() throws Exception {
        OSCMessage msg = new OSCMessage("/sc/stop");
        sender.send(msg);
    }

    @Test
    public void testMessageWithArgs() throws Exception {
        List<Object> args = new ArrayList<Object>(2);
        args.add(3);
        args.add("hello");
        OSCMessage msg = new OSCMessage("/foo/bar", args);
        sender.send(msg);
    }

    @Test
    public void testMessageWithNullAddress() throws Exception {
        OSCMessage msg = new OSCMessage(null);
        expectedException.expect(NullPointerException.class);
        sender.send(msg);
    }

    @Test
    public void testBundle() throws Exception {
        List<Object> args = new ArrayList<Object>(2);
        args.add(3);
        args.add("hello");
        List<OSCPacket> msgs = new ArrayList<OSCPacket>(1);
        msgs.add(new OSCMessage("/foo/bar", args));
        OSCBundle bundle = new OSCBundle(msgs);
        sender.send(bundle);
    }

    @Test
    public void testRecieving() throws Exception {
        OSCMessage msg = new OSCMessage("/message/recieving");
        SimpleListener listener = new SimpleListener();
        reciever.addListener("/message/recieving", listener);
        reciever.startListening();
        sender.send(msg);
        Thread.sleep(100);
        reciever.stopListening();
        if (!listener.isMessageRecieved()) {
            Assert.fail("Message was not recieved");
        }
    }

    @Test
    public void testBundleRecieving() throws Exception {
        OSCBundle bundle = new OSCBundle();
        bundle.addPacket(new OSCMessage("/bundle/recieving"));
        SimpleListener listener = new SimpleListener();
        reciever.addListener("/bundle/recieving", listener);
        reciever.startListening();
        sender.send(bundle);
        Thread.sleep(100);
        reciever.stopListening();
        if (!listener.isMessageRecieved()) {
            Assert.fail("Message was not recieved");
        }

        if (!listener.getRecievedTimestamp().equals(bundle.getTimestamp())) {
            Assert.fail("Message should have timestamp " + bundle.getTimestamp()
                + " but has " + listener.getRecievedTimestamp());
        }
    }

}
