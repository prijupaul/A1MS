package uk.com.a1ms.network.dto;

/**
 * Created by priju.jacobpaul on 5/08/16.
 */
public class MessageResponseDetails {

    private enum MSG_TYPE{
        MSG_TYPE_NONE,
        MSG_TYPE_ON_OPEN_CONNECTION
    }

    private String msgType;

    private MsgDetail msg;

    private MSG_TYPE msgTypeEnum;

    public String getMsgType ()
    {
        return msgType;
    }

    public void setMsgType (String msgType)
    {
        this.msgType = msgType;
    }

    public MsgDetail getMsg ()
    {
        return msg;
    }

    public void setMsg (MsgDetail msg)
    {
        this.msg = msg;
    }

    public boolean canIgnore(){

        if( (getMsgType()!=null) && getMsgType().contains("onOpenConnection")){
            return true;
        }
        return false;
    }

    @Override
    public String toString()
    {
        return "MessageResponseDetails [msgType = "+msgType+", msg = "+msg+"]";
    }
}
