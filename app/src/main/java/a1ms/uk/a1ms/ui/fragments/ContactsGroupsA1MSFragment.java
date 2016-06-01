package a1ms.uk.a1ms.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;

import a1ms.uk.a1ms.R;
import a1ms.uk.a1ms.adapters.ContactsGroupsA1msAdapter;
import a1ms.uk.a1ms.contacts.FetchContactsHandler;
import a1ms.uk.a1ms.contacts.FetchContactsHandlerListener;
import a1ms.uk.a1ms.dto.Contacts;

/**
 * Created by priju.jacobpaul on 27/05/16.
 */
public class ContactsGroupsA1MSFragment extends BaseFragment implements FetchContactsHandlerListener {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.contactsgroups_a1ms,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = (RecyclerView)view.findViewById(R.id.a1ms_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setNestedScrollingEnabled(false);
        }

    public void setContactList(HashMap<String,Contacts> contactList){
        if(mRecyclerView == null){
            return;
        }

        mAdapter = new ContactsGroupsA1msAdapter(contactList);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fetchContacts();
    }

    @Override
    public void onContactsLoadComplete(HashMap<String, Contacts> contactsList) {
        setContactList(contactsList);
    }

    public void fetchContacts(){
        FetchContactsHandler.getInstance(getActivity().getApplicationContext())
                .getContactsWithSMSPhone(this);
    }
}
