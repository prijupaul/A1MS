package a1ms.uk.a1ms.contacts;

import java.util.SortedMap;

import a1ms.uk.a1ms.dto.Contacts;

/**
 * Created by priju.jacobpaul on 28/05/16.
 */
public interface FetchContactsHandlerListener {
        void onContactsLoadComplete(SortedMap<String, Contacts> contactsList);
}
