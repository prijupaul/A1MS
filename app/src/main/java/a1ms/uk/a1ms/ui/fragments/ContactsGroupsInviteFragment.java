package a1ms.uk.a1ms.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;

import com.futuremind.recyclerviewfastscroll.FastScroller;

import java.util.SortedMap;

import a1ms.uk.a1ms.A1MSApplication;
import a1ms.uk.a1ms.R;
import a1ms.uk.a1ms.adapters.ContactsGroupsInviteAdapter;
import a1ms.uk.a1ms.contacts.FetchContactsHandler;
import a1ms.uk.a1ms.dto.Contacts;
import a1ms.uk.a1ms.util.NotificationController;

/**
 * Created by priju.jacobpaul on 27/05/16.
 */
public class ContactsGroupsInviteFragment extends BaseFragment implements  NotificationController.NotificationListener{

    private RecyclerView mRecyclerView;
    private FastScroller mFastScroller;
    private ContactsGroupsInviteAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private LinearLayout mLLNoContactsHolder;
    private FloatingActionButton mFab;

    private SortedMap<String,Contacts> mContactsList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.contactsgroups_a1ms,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = (RecyclerView)view.findViewById(R.id.a1ms_recycler_view);
        mFastScroller = (FastScroller) view.findViewById(R.id.fastscroll);
        mFab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE){
                    mFab.show();
                }
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 ||dy<0 && mFab.isShown()) {
                    mFab.hide();
                }

            }
        });

        mLLNoContactsHolder = (LinearLayout)view.findViewById(R.id.ll_no_contacts);
        fetchContacts();
    }


    public void setContactList(SortedMap<String,Contacts> contactList){
        mContactsList = contactList;

        if(mAdapter == null) {
            mAdapter = new ContactsGroupsInviteAdapter(contactList);
        }

        mAdapter.setDataSet(mContactsList);

        if((mRecyclerView != null)){
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }


        if((mContactsList == null) || mContactsList.size() == 0){
            if(mRecyclerView != null) {
                mRecyclerView.setVisibility(View.GONE);
            }
            if(mLLNoContactsHolder != null) {
                mLLNoContactsHolder.setVisibility(View.VISIBLE);
            }
        }
        else {
            if(mRecyclerView != null) {
                mRecyclerView.setVisibility(View.VISIBLE);
            }
            if(mLLNoContactsHolder != null) {
                mLLNoContactsHolder.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        NotificationController.getInstance().addObserver(this,NotificationController.contactsDidLoaded);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        NotificationController.getInstance().removeObserver(this,NotificationController.contactsDidLoaded);
    }



    public void fetchContacts(){
        FetchContactsHandler.getInstance(A1MSApplication.applicationContext).getContactsWithSMSPhone(null);
    }

    public void setGlobalCheckBoxStatusChange(boolean statusChange){

        ((ContactsGroupsInviteAdapter)mAdapter).setGlobalCheckBoxStatusChange(statusChange);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onNotificationReceived(int id, Object... args) {

        if(id == NotificationController.contactsDidLoaded){
            setContactList(FetchContactsHandler.getInstance(A1MSApplication.applicationContext).getLoadedContacts());
            mFastScroller.setRecyclerView(mRecyclerView);
        }
    }
}
