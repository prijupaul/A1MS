package uk.com.a1ms.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.CheckBox;
import android.widget.TextView;

import com.futuremind.recyclerviewfastscroll.FastScroller;

import java.util.ArrayList;
import java.util.List;

import uk.com.a1ms.R;
import uk.com.a1ms.adapters.ContactsGroupsA1MSAdapter;
import uk.com.a1ms.db.A1MSUsersFieldsDataSource;
import uk.com.a1ms.db.dto.A1MSUser;
import uk.com.a1ms.dialogutil.DialogCallBackListener;
import uk.com.a1ms.dialogutil.DialogUtil;
import uk.com.a1ms.util.NotificationController;

/**
 * Created by priju.jacobpaul on 27/05/16.
 */
public class ContactsGroupsA1MSFragment extends BaseFragment implements ContactsGroupsA1MSAdapter.ContactsGroupsA1MSAdapterListener,
        NotificationController.NotificationListener
{

    private RecyclerView mRecyclerView;
    private FastScroller mFastScroller;
    private ContactsGroupsA1MSAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private A1MSUsersFieldsDataSource mDataSource;
    private Toolbar mToolbar;
    protected TextView mTextViewCounter;
    private int selectionCounter;
    private boolean mIsInActionMode = false;
    List<A1MSUser> mSelectedA1MSUsers = new ArrayList<>();
    ArrayList<Integer> mSelectedItemPosition = new ArrayList<>();
    List<A1MSUser> mA1MSUsers;
    FloatingActionButton mFab;
    boolean mIsGroupsShown;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        setHasOptionsMenu(true);
        NotificationController.getInstance().addObserver(this,NotificationController.didOpenDatabase);
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
        mFastScroller = (FastScroller) view.findViewById(R.id.fastscroll);
        mFab = (FloatingActionButton) getActivity().findViewById(R.id.fab);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
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

        if(((AppCompatActivity)getActivity()).getSupportActionBar() != null){
            mToolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
            mTextViewCounter = (TextView)getActivity().findViewById(R.id.textview_countertext);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
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
    public void onLongClick(View view, int position) {
        if(mToolbar != null){

            mToolbar.getMenu().clear();
            mToolbar.inflateMenu(R.menu.menu_contacts_contextual);
            mTextViewCounter.setVisibility(View.VISIBLE);
            mTextViewCounter.setText("1 Item Selected");
            selectionCounter++;
            ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

            setIsInActionMode(true);

            mSelectedA1MSUsers.add(mA1MSUsers.get(position));
            mSelectedItemPosition.add(position);
        }
    }

    @Override
    public void onPrepareSelection(View view, int position) {

        CheckBox checkBox = (CheckBox)view.findViewById(R.id.checkbox_imageview_icon);
        if(checkBox.isChecked()){
            mSelectedA1MSUsers.add(mA1MSUsers.get(position));
            mSelectedItemPosition.add(position);
            selectionCounter++;
        }
        else {
            int indexOfUser = mSelectedItemPosition.indexOf(position);
            mSelectedA1MSUsers.remove(indexOfUser);
            mSelectedItemPosition.remove(indexOfUser);
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu( menu, inflater );
        inflater.inflate(R.menu.menu_main_groups_contacts,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_add_contact: {
                Intent intent = new Intent(Intent.ACTION_INSERT, ContactsContract.Contacts.CONTENT_URI);
                startActivity(intent);
                return true;
            }
            case R.id.action_contacts_groups:{

                if(mIsGroupsShown){
                    if(mAdapter != null) {
                        mIsGroupsShown = false;
                        mA1MSUsers = mDataSource.getAllA1MSUsers(false,true);
                        mAdapter.setDataSet(mA1MSUsers);
                        mAdapter.notifyDataSetChanged();
                        item.setIcon(getResources().getDrawable(R.drawable.contacts_group));
                        item.setTitle(getString(R.string.hint_show_groups));
                    }
                }
                else {
                    if(mAdapter != null) {
                        mIsGroupsShown = true;
                        mA1MSUsers = mDataSource.getAllA1MSGroups();
                        mAdapter.setDataSet(mA1MSUsers);
                        mAdapter.notifyDataSetChanged();
                        item.setIcon(getResources().getDrawable(R.drawable.contact));
                        item.setTitle(getString(R.string.hint_show_contacts));
                    }
                }

                return true;
            }
        }
        return false;
    }

    private void clearActionMode(){

        mTextViewCounter.setVisibility(View.GONE);
        mToolbar.getMenu().clear();
        mToolbar.inflateMenu(R.menu.menu_main_groups_contacts);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(true);
        mAdapter.resetContextMenu();
        selectionCounter = 0;

        // Clear the selected item arrays
        mSelectedA1MSUsers.clear();
        mSelectedItemPosition.clear();
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

    public void deleteSelectedItems(){

        if(mSelectedItemPosition.size() == 0){
            return;
        }

        DialogUtil.showYESNODialog(getActivity(), "", getString(R.string.hint_delete) + "?",
                getString(R.string.dialog_delete), getString(R.string.dialog_cancel), new DialogCallBackListener() {
                    @Override
                    public void run() {
                        mDataSource.deleteA1MSUsers(mSelectedA1MSUsers);
                        mA1MSUsers = mDataSource.getAllA1MSUsers(false,true);
                        mAdapter.setDataSet(mA1MSUsers);
                        mAdapter.notifyDataSetChanged();
                        mSelectedItemPosition.clear();
                        mSelectedA1MSUsers.clear();
                        onBackPressed();
                    }
                },null,true);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(!isVisibleToUser){
            if(IsInActionMode()){
                onBackPressed();
            }
        }
        else {
            if(mAdapter != null) {
                mAdapter.setDataSet(mA1MSUsers);
                    mAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onNotificationReceived(int id, Object... args) {
        if(id == NotificationController.didOpenDatabase){
            if(mAdapter == null) {
                mA1MSUsers = mDataSource.getAllA1MSUsers(false,true);
                mAdapter = new ContactsGroupsA1MSAdapter(mA1MSUsers, this);
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                mFastScroller.setRecyclerView(mRecyclerView);
            }
            NotificationController.getInstance().removeObserver(this,NotificationController.didOpenDatabase);
        }
    }
}
