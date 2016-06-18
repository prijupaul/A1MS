package a1ms.uk.a1ms.contacts;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import java.util.SortedMap;
import java.util.TreeMap;

import a1ms.uk.a1ms.A1MSApplication;
import a1ms.uk.a1ms.dto.Contacts;
import a1ms.uk.a1ms.util.PermissionRequestManager;

/**
 * Created by priju.jacobpaul on 24/05/16.
 */
public class FetchContactsLoader implements Loader.OnLoadCompleteListener<Cursor>, Loader.OnLoadCanceledListener<Cursor>{


    private CursorLoader mCursorLoader;
    private Context mContext;
    private FetchContactsLoaderListener mContactsLoadListener;

    private String[] PROJECTION_DP_PHOTO_EMAIL = new String[] { ContactsContract.RawContacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.Contacts.PHOTO_ID,
            ContactsContract.CommonDataKinds.Email.DATA,
            ContactsContract.CommonDataKinds.Photo.CONTACT_ID };

    private String[] PROJECTION_DP_PHOTO_SMS = new String[] {
            ContactsContract.RawContacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.Contacts.PHOTO_ID,
            ContactsContract.CommonDataKinds.Phone.DATA,
            ContactsContract.CommonDataKinds.Email.DATA,
            ContactsContract.CommonDataKinds.Photo.CONTACT_ID };

    String orderEmail = "CASE WHEN "
            + ContactsContract.Contacts.DISPLAY_NAME_PRIMARY
            + " NOT LIKE '%@%' THEN 1 ELSE 2 END, "
            + ContactsContract.Contacts.DISPLAY_NAME
            + ", "
            + ContactsContract.CommonDataKinds.Email.DATA
            + " COLLATE NOCASE";

    String orderSMS = "CASE WHEN "
            + ContactsContract.Contacts.DISPLAY_NAME_PRIMARY
            + " NOT LIKE '%@%' THEN 1 ELSE 2 END, "
            + ContactsContract.Contacts.DISPLAY_NAME
            + ", "
            + ContactsContract.CommonDataKinds.Phone.DATA
            + " COLLATE NOCASE";

    String filterEmail = ContactsContract.CommonDataKinds.Email.DATA + " NOT LIKE ''";
//    String filterSMS = ContactsContract.CommonDataKinds.Phone.DATA + " NOT LIKE ''";
    String filterSMS = ContactsContract.Data.HAS_PHONE_NUMBER + "!=0 " +
                        "AND (" + ContactsContract.Data.MIMETYPE + "=? OR " + ContactsContract.Data.MIMETYPE + "=?)";

    String filterName = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC";

    String[]selection = {
            ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE,
            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
    };

    public FetchContactsLoader(Context context, FetchContactsLoaderListener fetchContactsLoaderListener){
        mContext = context;
        mContactsLoadListener = fetchContactsLoaderListener;
    }

    private boolean hasPermission(){

        A1MSApplication application = (A1MSApplication)mContext;
        Activity activity = application.getCurrentActivity();
        if(activity == null){
            return false;
        }

        return PermissionRequestManager.checkPermission(activity, android.Manifest.permission.READ_CONTACTS);

    }

    public void loadContactsWithSMS(){

        A1MSApplication application = (A1MSApplication)mContext;
        Activity activity = application.getCurrentActivity();

        if(!hasPermission()){
            return;
        }

        mCursorLoader = new CursorLoader(mContext, ContactsContract.Data.CONTENT_URI,
                null,
                filterSMS,selection, ContactsContract.Data.DISPLAY_NAME);

        mCursorLoader.registerListener(1,this);
        mCursorLoader.registerOnLoadCanceledListener(this);


        if ((mCursorLoader != null) && !mCursorLoader.isStarted()) {
            mCursorLoader.startLoading();
        }


//        else {
//            PermissionRequestManager.checkAndRequestPermission(activity,
//                    android.Manifest.permission.READ_CONTACTS,
//                    activity.getResources().getInteger(R.integer.MY_PERMISSIONS_REQUEST_READ_CONTACTS));
//        }
    }

//    public void loadContactsWithEmail(){
//
//
//        A1MSApplication application = (A1MSApplication)mContext;
//        Activity activity = application.getCurrentActivity();
//
//        mCursorLoader = new CursorLoader(mContext, ContactsContract.CommonDataKinds.Email.CONTENT_URI,PROJECTION_DP_PHOTO_EMAIL,
//                filterEmail,null,orderEmail);
//        mCursorLoader.registerListener(1,this);
//        mCursorLoader.registerOnLoadCanceledListener(this);
//
//        if(PermissionRequestManager.checkPermission(application.getCurrentActivity(), android.Manifest.permission.READ_CONTACTS)) {
//            if ((mCursorLoader != null) && !mCursorLoader.isStarted()) {
//                mCursorLoader.startLoading();
//            }
//        }
//        else {
//            PermissionRequestManager.checkAndRequestPermission(activity,
//                    android.Manifest.permission.READ_CONTACTS,
//                    activity.getResources().getInteger(R.integer.MY_PERMISSIONS_REQUEST_READ_CONTACTS));
//        }
//    }

    @Override
    public void onLoadComplete(Loader<Cursor> loader, Cursor data) {
        SortedMap<String,Contacts> contactsList = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

        if (data.moveToFirst()) {
            do {
                // names comes in hand sometimes
                String name = data.getString(data.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
                String mimeType = data.getString(data.getColumnIndex(ContactsContract.Data.MIMETYPE));
                String emailType = data.getString(data.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));

                Contacts contacts;
                if(contactsList.containsKey(name)){
                    contacts = contactsList.get(name);
                }
                else {
                    contacts = new Contacts();
                    contacts.setContactName(name);
                }

                if(mimeType.equals(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)) {
                    int type = data.getInt(data.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));

                    switch (type){
                        case ContactsContract.CommonDataKinds.Phone.TYPE_WORK_MOBILE:{
                            contacts.setContactWorkMobile(data.getString(data.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA1) ));
                            break;
                        }
                        case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:{
                            contacts.setContactPhoneHome(data.getString(data.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA1) ));
                            break;
                        }
                        case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:{
                            contacts.setContactPhoneMobile(data.getString(data.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA1) ));
                            break;
                        }
                        case ContactsContract.CommonDataKinds.Phone.TYPE_MAIN:{
                            contacts.setContactMain(data.getString(data.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA1) ));
                            break;
                        }
                        case ContactsContract.CommonDataKinds.Phone.TYPE_CUSTOM:{
                            contacts.setContactPhoneCustom(data.getString(data.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA1) ));
                            break;
                        }
                        case ContactsContract.CommonDataKinds.Phone.TYPE_COMPANY_MAIN:{
                            contacts.setContactCompanyMain(data.getString(data.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA1) ));
                            break;
                        }
                    }
                }

                if(mimeType.equals(ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)) {
                    int type = data.getInt(data.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));

                    if (emailType != null)
                        switch (type){
                            case ContactsContract.CommonDataKinds.Email.TYPE_CUSTOM:{
                                contacts.setContactEmailCustom(data.getString(data.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA1) ));
                                break;
                            }
                            case ContactsContract.CommonDataKinds.Email.TYPE_HOME:{
                                contacts.setContactEmailHome(data.getString(data.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA1) ));
                                break;
                            }
                            case ContactsContract.CommonDataKinds.Email.TYPE_WORK:{
                                contacts.setContactEmailWork(data.getString(data.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA1) ));
                                break;
                            }
                            case ContactsContract.CommonDataKinds.Email.TYPE_OTHER:{
                                contacts.setContactEmailOther(data.getString(data.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA1) ));
                                break;
                            }
                            case ContactsContract.CommonDataKinds.Email.TYPE_MOBILE:{
                                contacts.setContactEmailMobile(data.getString(data.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA1) ));
                                break;
                            }
                        }
                }

                contactsList.put(contacts.getContactName(),contacts);
            } while (data.moveToNext());
        }

        data.close();

        if(mContactsLoadListener != null){
            mContactsLoadListener.onContactsLoadComplete(contactsList);
        }
    }

    @Override
    public void onLoadCanceled(Loader<Cursor> loader) {
        loader.abandon();
    }

    public void closeLoader(){

        if(mCursorLoader != null){
            mCursorLoader.unregisterListener(this);
            mCursorLoader.unregisterOnLoadCanceledListener(this);
            mCursorLoader.cancelLoad();
            mCursorLoader.stopLoading();
        }
    }
}
