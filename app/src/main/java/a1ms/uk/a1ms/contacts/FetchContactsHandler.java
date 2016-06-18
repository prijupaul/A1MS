package a1ms.uk.a1ms.contacts;

import android.content.Context;

import java.util.SortedMap;

import a1ms.uk.a1ms.dto.Contacts;
import a1ms.uk.a1ms.util.NotificationController;

/**
 * Created by priju.jacobpaul on 30/05/16.
 */
public class FetchContactsHandler implements FetchContactsLoaderListener{

    private FetchContactsLoader mContactsLoader;
    private static FetchContactsHandler mContactsHandler;
    private Context mContext;
    private SortedMap<String, Contacts> mContactsWithEmailPhone;
    private FetchContactsHandlerListener mListener;
    private FetchContactsHandler(){

    }

    public static FetchContactsHandler getInstance(Context context){
        if(mContactsHandler == null){
            mContactsHandler = new FetchContactsHandler();
            mContactsHandler.initLoader(context);
        }

        return mContactsHandler;
    }

    private void initLoader(Context context){
        mContext = context;
        mContactsLoader = new FetchContactsLoader(mContext,this);

    }

    public  void getContactsWithSMSPhone(FetchContactsHandlerListener listener){
        mListener = listener;
        if(mContactsWithEmailPhone == null) {
            mContactsLoader.loadContactsWithSMS();
        }
        else {
//            listener.onContactsLoadComplete(mContactsWithEmailPhone);
            NotificationController.getInstance().postNotificationName(NotificationController.contactsDidLoaded,mContactsWithEmailPhone);
        }
    }

    public SortedMap<String, Contacts> getLoadedContacts(){
        return mContactsWithEmailPhone;
    }


//    public  void getContactsWithEmailPhone(){
//        mContactsLoader.loadContactsWithEmail();
//    }

    @Override
    public void onContactsLoadComplete(SortedMap<String, Contacts> contactsList) {
        mContactsWithEmailPhone  = contactsList;
        NotificationController.getInstance().postNotificationName(NotificationController.contactsDidLoaded,mContactsWithEmailPhone);

//        if(mListener != null){
//            mListener.onContactsLoadComplete(mContactsWithEmailPhone);
//        }
    }
}
