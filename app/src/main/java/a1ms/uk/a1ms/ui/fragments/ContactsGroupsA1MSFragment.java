package a1ms.uk.a1ms.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import a1ms.uk.a1ms.R;
import a1ms.uk.a1ms.adapters.ContactsGroupsA1MSAdapter;
import a1ms.uk.a1ms.db.A1MSUsersFieldsDataSource;
import a1ms.uk.a1ms.db.dto.A1MSUser;

/**
 * Created by priju.jacobpaul on 27/05/16.
 */
public class ContactsGroupsA1MSFragment extends BaseFragment implements ContactsGroupsA1MSAdapter.ContactsGroupsA1MSAdapterListener {

    private RecyclerView mRecyclerView;
    private ContactsGroupsA1MSAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private A1MSUsersFieldsDataSource mDataSource;
    private Toolbar mToolbar;
    protected TextView mTextViewCounter;
    private int selectionCounter;
    private boolean mIsInActionMode = false;

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

        if(((AppCompatActivity)getActivity()).getSupportActionBar() != null){
            mToolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
            mTextViewCounter = (TextView)getActivity().findViewById(R.id.textview_countertext);
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        List<A1MSUser> a1MSUsers = mDataSource.getAllA1MSUsers();
        mAdapter = new ContactsGroupsA1MSAdapter(a1MSUsers,this);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mDataSource.close();
    }

    public void setGlobalCheckBoxStatusChange(boolean statusChange){

        mAdapter.setGlobalCheckBoxStatusChange(statusChange);
        mAdapter.notifyDataSetChanged();
    }

    public boolean IsInActionMode() {
        return mIsInActionMode;
    }

    public void setIsInActionMode(boolean mIsInActionMode) {
        this.mIsInActionMode = mIsInActionMode;
    }

    @Override
    public void onLongClick(View view) {
        if(mToolbar != null){

            mToolbar.getMenu().clear();
            mToolbar.inflateMenu(R.menu.menu_contacts_contextual);
            mTextViewCounter.setVisibility(View.VISIBLE);
            mTextViewCounter.setText("1 Item Selected");
            selectionCounter++;
            ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

            setIsInActionMode(true);

        }
    }

    @Override
    public void onPrepareSelection(View view, int position) {

        CheckBox checkBox = (CheckBox)view.findViewById(R.id.checkbox_imageview_icon);
        if(checkBox.isChecked()){
            selectionCounter++;
        }
        else {
            selectionCounter--;
        }

        if(selectionCounter == 0){
            clearActionMode();

        }
        else{
            mTextViewCounter.setText(selectionCounter + " Item Selected");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void clearActionMode(){

        mTextViewCounter.setVisibility(View.GONE);
        mToolbar.getMenu().clear();
        mToolbar.inflateMenu(R.menu.menu_main_groups_contacts);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(true);
        mAdapter.resetContextMenu();
        selectionCounter = 0;
    }

    @Override
    public boolean onBackPressed() {
        if(IsInActionMode()){
            clearActionMode();
            mAdapter.resetContextMenu();
            setIsInActionMode(false);
            return true;
        }
        return false;
    }
}
