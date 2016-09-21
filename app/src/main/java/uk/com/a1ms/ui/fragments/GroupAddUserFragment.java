package uk.com.a1ms.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import java.util.ArrayList;
import java.util.List;

import uk.com.a1ms.R;
import uk.com.a1ms.adapters.GroupsAddUserAdapter;
import uk.com.a1ms.db.A1MSGroupsFieldsDataSource;
import uk.com.a1ms.db.A1MSUsersFieldsDataSource;
import uk.com.a1ms.db.dto.A1MSGroup;
import uk.com.a1ms.db.dto.A1MSUser;

/**
 * Created by priju.jacobpaul on 21/09/16.
 */
public class GroupAddUserFragment extends BaseFragment implements GroupsAddUserAdapter.GroupsAddUserListener {

    public interface GroupAddUserFragmentListener{
        void usersAdded(ArrayList<A1MSUser> users);
    }

    private A1MSGroup mGroupDetails;
    private GroupAddUserFragmentListener addUserFragmentListener;
    private A1MSUsersFieldsDataSource a1MSUsersFieldsDataSource;
    private A1MSGroupsFieldsDataSource a1MSGroupsFieldsDataSource;
    private List<A1MSUser> allA1MSUsersList;
    private ArrayList<String> currentMembersInGroup;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private GroupsAddUserAdapter mAdapter;
    private MenuItem mAddUsersMenu;
    private ArrayList<A1MSUser> mNewUsers = new ArrayList<>();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        a1MSGroupsFieldsDataSource = new A1MSGroupsFieldsDataSource(getContext());
        a1MSUsersFieldsDataSource = new A1MSUsersFieldsDataSource(getContext());
        a1MSUsersFieldsDataSource.open();
        a1MSGroupsFieldsDataSource.open();

        allA1MSUsersList = a1MSUsersFieldsDataSource.getAllA1MSUsers(true,false);
        currentMembersInGroup = mGroupDetails.getMembersList();

        mAdapter = new GroupsAddUserAdapter(currentMembersInGroup,allA1MSUsersList,this);

        setHasOptionsMenu(true);

    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public boolean onBackPressed() {
        return super.onBackPressed();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.group_adduser_fragment,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = (RecyclerView)view.findViewById(R.id.a1ms_recycler_view);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);

    }

    public static GroupAddUserFragment getInstance(A1MSGroup a1MSGroup, GroupAddUserFragmentListener listener){

        GroupAddUserFragment fragment = new GroupAddUserFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("a1msGroup",a1MSGroup);
        fragment.setArguments(bundle);
        fragment.mGroupDetails = a1MSGroup;
        fragment.addUserFragmentListener = listener;
        return fragment;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_group_adduser,menu);
        mAddUsersMenu = menu.findItem(R.id.action_groups_adduser);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                getActivity().onBackPressed();
                return true;
            }
            case R.id.action_groups_adduser:{
                if(this.addUserFragmentListener != null){
                    this.addUserFragmentListener.usersAdded(mNewUsers);
                }
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClicked(View view, int position) {

        A1MSUser a1MSUser = allA1MSUsersList.get(position);
        if(((CheckBox)view).isChecked()){
            mNewUsers.add(a1MSUser);
        }
        else{
            mNewUsers.remove(a1MSUser);
        }

        if(mNewUsers.size() == 0){
            mAddUsersMenu.setEnabled(false);
        }
        else {
            mAddUsersMenu.setEnabled(true);
        }

    }
}
