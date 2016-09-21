package uk.com.a1ms.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.TextView;

import java.util.ArrayList;

import uk.com.a1ms.A1MSApplication;
import uk.com.a1ms.R;
import uk.com.a1ms.adapters.GroupsInfoAdapter;
import uk.com.a1ms.db.A1MSUsersFieldsDataSource;
import uk.com.a1ms.db.dto.A1MSGroup;
import uk.com.a1ms.db.dto.A1MSUser;
import uk.com.a1ms.dialogutil.DialogCallBackListener;
import uk.com.a1ms.dialogutil.DialogUtil;
import uk.com.a1ms.util.SharedPreferenceManager;

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
    private TextView mTvGroupName;
    private TextView mMembersCount;
    private Button mAddMembers;

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
        toolbar.setTitle(mGroupDetails.getGroupName());

        mMembersList = mGroupDetails.getMembersList();
        mMembersA1MSUsersList = a1MSUsersFieldsDataSource.getA1MSUsersDetails(mMembersList);

        mMembersA1MSUsersList.add(createYourUser());
        mAdapter = new GroupsInfoAdapter(mGroupDetails,mMembersA1MSUsersList);
        mGroupMembersRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        mTvGroupName.setText(mGroupDetails.getGroupName());

    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mGroupMembersRecyclerView = (RecyclerView)view.findViewById(R.id.group_members_recycler_view);
        mButtonExitGroup = (Button)view.findViewById(R.id.button_exitgroup);
        mTvGroupName = (TextView)view.findViewById(R.id.tv_group_name);
        mMembersCount = (TextView)view.findViewById(R.id.tv_members_counter);

        mMembersCount.setText(mGroupDetails.getMembersList().size() + "/" + getResources().getInteger(R.integer.group_max_members_size) );

        mAddMembers = (Button)view.findViewById(R.id.button_add_members);

        mLayoutManager = new LinearLayoutManager(getContext());
        mGroupMembersRecyclerView.setLayoutManager(mLayoutManager);
        mGroupMembersRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mButtonExitGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(infoFragmentListener != null){
                    DialogUtil.showYESNODialog(getActivity(),
                            getString(R.string.dialog_exit_group),
                            getString(R.string.exit_group_message),
                            getString(R.string.dialog_yes),
                            getString(R.string.dialog_cancel),
                            new DialogCallBackListener() {
                                @Override
                                public void run() {
                                    infoFragmentListener.onExitGroup();
                                }
                            },
                            null,
                            true
                    );

                }
            }
        });

        mTvGroupName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                GroupNameChangeFragment groupNameChangeFragment = GroupNameChangeFragment.getInstance(mGroupDetails,(GroupNameChangeFragment.GroupNameChangeFragmentListener) getActivity());
                android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().replace(R.id.message_framelayout_holder,groupNameChangeFragment,
                        groupNameChangeFragment.getClass().getSimpleName());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        mAddMembers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                GroupAddUserFragment groupAddUserFragment = GroupAddUserFragment.getInstance(mGroupDetails,(GroupAddUserFragment.GroupAddUserFragmentListener) getActivity());
                android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().replace(R.id.message_framelayout_holder,groupAddUserFragment,
                        groupAddUserFragment.getClass().getSimpleName());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
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

    private A1MSUser createYourUser(){
        A1MSUser a1MSUser = new A1MSUser();
        a1MSUser.setUserId(SharedPreferenceManager.getUserId(getActivity()));
        a1MSUser.setName("You");
        a1MSUser.setEchomate(false);
        return a1MSUser;
    }
}
