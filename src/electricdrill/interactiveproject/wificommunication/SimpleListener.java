package electricdrill.interactiveproject.wificommunication;

import com.illposed.osc.OSCListener;
import com.illposed.osc.OSCMessage;

import java.util.Date;

public class SimpleListener implements OSCListener {

    private boolean messageRecieved = false;
    private Date recievedTimestamp = null;

    public Date getRecievedTimestamp() { return recievedTimestamp; }

    public boolean isMessageRecieved() { return messageRecieved; }

    @Override
    public void acceptMessage(Date time, OSCMessage message) {
        messageRecieved = true;
        recievedTimestamp = time;
    }

}
