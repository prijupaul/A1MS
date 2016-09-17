package uk.com.a1ms.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.widget.Button;

import java.util.ArrayList;

import uk.com.a1ms.A1MSApplication;
import uk.com.a1ms.R;
import uk.com.a1ms.adapters.GroupsInfoAdapter;
import uk.com.a1ms.db.A1MSUsersFieldsDataSource;
import uk.com.a1ms.db.dto.A1MSGroup;
import uk.com.a1ms.db.dto.A1MSUser;

/**
 * Created by priju.jacobpaul on 16/09/16.
 */
public class GroupInfoFragment extends BaseFragment {

    private RecyclerView mGroupMembersRecyclerView;
    private Button mButtonExitGroup;
    private GroupsInfoAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private A1MSGroup mGroupDetails;
    private ArrayList<String> mMembersList;
    private ArrayList<A1MSUser> mMembersA1MSUsersList;
    private A1MSUsersFieldsDataSource a1MSUsersFieldsDataSource;
    private GroupInfoFragmentListener infoFragmentListener;

    public interface GroupInfoFragmentListener{
        void onExitGroup();
    }

    public static GroupInfoFragment getInstance(A1MSGroup a1MSGroup,GroupInfoFragmentListener listener){

        GroupInfoFragment fragment = new GroupInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("a1msGroup",a1MSGroup);
        fragment.setArguments(bundle);
        fragment.mGroupDetails = a1MSGroup;
        fragment.infoFragmentListener = listener;
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        a1MSUsersFieldsDataSource = new A1MSUsersFieldsDataSource(A1MSApplication.applicationContext);
        a1MSUsersFieldsDataSource.open();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.group_info_fragment,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setCollapsible(false);
        toolbar.setTitle(getString(R.string.toolbar_group_info));

        mMembersList = mGroupDetails.getMembersList();
        mMembersA1MSUsersList = a1MSUsersFieldsDataSource.getA1MSUsersDetails(mMembersList);

        mAdapter = new GroupsInfoAdapter(mGroupDetails,mMembersA1MSUsersList);
        mGroupMembersRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mGroupMembersRecyclerView = (RecyclerView)view.findViewById(R.id.group_members_recycler_view);
        mButtonExitGroup = (Button)view.findViewById(R.id.button_exitgroup);

        mLayoutManager = new LinearLayoutManager(getContext());
        mGroupMembersRecyclerView.setLayoutManager(mLayoutManager);
        mGroupMembersRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mButtonExitGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(infoFragmentListener != null){
                    infoFragmentListener.onExitGroup();
                }
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                getActivity().onBackPressed();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
