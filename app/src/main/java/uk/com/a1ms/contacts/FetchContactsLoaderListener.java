package uk.com.a1ms.contacts;

import java.util.SortedMap;

import uk.com.a1ms.dto.Contacts;

/**
 * Created by priju.jacobpaul on 28/05/16.
 */
public interface FetchContactsLoaderListener {
        void onContactsLoadComplete(SortedMap<String,Contacts> contactsList);
}
