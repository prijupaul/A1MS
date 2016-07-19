package uk.com.a1ms.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.futuremind.recyclerviewfastscroll.FastScroller;

import java.util.List;

import uk.com.a1ms.R;
import uk.com.a1ms.adapters.CreateGroupsAdapter;
import uk.com.a1ms.db.dto.A1MSUser;

/**
 * Created by priju.jacobpaul on 13/07/16.
 */
public class CreateGroupsPreviewFragment extends BaseFragment implements CreateGroupsAdapter.CreateGroupsAdapterListener {

    private RecyclerView mRecyclerView;
    private FastScroller mFastScroller;
    private Toolbar mToolbar;

    private CreateGroupsAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private MenuItem mMenuNext;
    private EditText mEditTextGroupName;
    private List<A1MSUser> mA1MSUsers;
    private GroupPreviewListener mGroupPreviewListener;

    public interface GroupPreviewListener{
        void onGroupCreated(String groupName,List<A1MSUser> a1MSUsers);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public static CreateGroupsPreviewFragment newInstance(List<A1MSUser> a1MSUsers, GroupPreviewListener listener) {

        CreateGroupsPreviewFragment fragment = new CreateGroupsPreviewFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        fragment.mA1MSUsers = a1MSUsers;
        fragment.mGroupPreviewListener = listener;
        return fragment;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.creategroups_groupname, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.a1msusers_recycler_view);
        mFastScroller = (FastScroller) view.findViewById(R.id.fastscroll);
        mEditTextGroupName = (EditText) view.findViewById(R.id.edittext_groupname);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setNestedScrollingEnabled(false);

        mEditTextGroupName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length() > 0) {
                    mMenuNext.setEnabled(true);
                }
                else {
                    mMenuNext.setEnabled(false);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mAdapter == null) {
            mAdapter = new CreateGroupsAdapter(mA1MSUsers,false, this);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
            mFastScroller.setRecyclerView(mRecyclerView);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_create_group, menu);
        mMenuNext = menu.findItem(R.id.action_create_groups);
        mMenuNext.setTitle(R.string.hint_create_group);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_create_groups) {
            if (item.isEnabled()) {
                mGroupPreviewListener.onGroupCreated(mEditTextGroupName.getText().toString(),mA1MSUsers);
                return true;
            }
        }

        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return false;
    }


    @Override
    public void onClick(View view, boolean isChecked, int position) {

    }

    @Override
    public boolean onBackPressed() {
        if(getFragmentManager().getBackStackEntryCount() != 0){
            getFragmentManager().popBackStack();
            return true;
        }
        return super.onBackPressed();
    }

}
