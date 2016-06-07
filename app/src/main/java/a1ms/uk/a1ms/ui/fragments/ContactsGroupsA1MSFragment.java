package a1ms.uk.a1ms.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import a1ms.uk.a1ms.R;
import a1ms.uk.a1ms.adapters.ContactsGroupsA1MSAdapter;
import a1ms.uk.a1ms.db.A1MSUsersFieldsDataSource;
import a1ms.uk.a1ms.db.dto.A1MSUser;

/**
 * Created by priju.jacobpaul on 27/05/16.
 */
public class ContactsGroupsA1MSFragment extends BaseFragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private A1MSUsersFieldsDataSource mDataSource;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mDataSource = new A1MSUsersFieldsDataSource(context);
        mDataSource.open();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.contactsgroups_a1ms,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.a1ms_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setNestedScrollingEnabled(false);

    }

    @Override
    public void onResume() {
        super.onResume();

        List<A1MSUser> a1MSUsers = mDataSource.getAllA1MSUsers();
        mAdapter = new ContactsGroupsA1MSAdapter(a1MSUsers);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mDataSource.close();
    }

    public void setGlobalCheckBoxStatusChange(boolean statusChange){

        ((ContactsGroupsA1MSAdapter)mAdapter).setGlobalCheckBoxStatusChange(statusChange);
        mAdapter.notifyDataSetChanged();
    }
}
