package uk.com.a1ms.network.dto;

/**
 * Created by priju.jacobpaul on 5/08/16.
 */
public class MsgDetail
{
    private String connectionId;

    private String welcome;

    private String data;

    public String getConnectionId ()
    {
        return connectionId;
    }

    public void setConnectionId (String connectionId)
    {
        this.connectionId = connectionId;
    }

    public String getWelcome ()
    {
        return welcome;
    }

    public void setWelcome (String welcome)
    {
        this.welcome = welcome;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString()
    {
        return "MsgDetail [connectionId = "+connectionId+", welcome = "+welcome+"]";
    }
}

