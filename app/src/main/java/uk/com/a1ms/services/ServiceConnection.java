package uk.com.a1ms.services;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import uk.com.a1ms.A1MSApplication;
import uk.com.a1ms.messages.MessageImpl;

/**
 * Created by priju.jacobpaul on 7/10/2016.
 */

public class ServiceConnection implements android.content.ServiceConnection {

    private android.content.ServiceConnection mServiceConnection = this;
    private Messenger mIncomingMessenger = new Messenger(new MessageImpl.IncomingMessageHandler());
    private Messenger mServiceMessenger = null;

    private final String TAG = ServiceConnection.class.getSimpleName();

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        mServiceMessenger = new Messenger(iBinder);

    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        mIncomingMessenger = null;
    }

    public void doBindService(){

        Context context = A1MSApplication.applicationContext;
        Intent intent = new Intent(context,MessagingService.class);
        context.bindService(intent,mServiceConnection,Context.BIND_AUTO_CREATE);
        Log.d(TAG,"Binding to service");
    }

    public void doUnbindService(){
        Context context = A1MSApplication.applicationContext;
        context.unbindService(mServiceConnection);
    }

    public void sendEchoMessage(String message){
        sendUsingBundle(MessagingService.MSG_SEND_ECHO_MESSAGE,message);
    }

    public void sendPrivateMessage(String message){
        sendUsingBundle(MessagingService.MSG_SEND_PRIVATE_MESSAGE,message);
    }

    public void sendGroupMessage(String message){
        sendUsingBundle(MessagingService.MSG_SEND_GROUP_MESSAGE,message);
    }

    private void sendUsingBundle(int type, String message){

        if(mServiceMessenger == null){
            return;
        }

        try {
            Bundle bundle = new Bundle();

            bundle.putString("message", message);
            Message msg = new Message();
            msg.what = type;
            msg.setData(bundle);
            msg.replyTo = mIncomingMessenger;
            mServiceMessenger.send(msg);
        }
        catch (RemoteException e){
            e.printStackTrace();
        }
    }

}

