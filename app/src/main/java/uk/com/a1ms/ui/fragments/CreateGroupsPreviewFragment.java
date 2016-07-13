package uk.com.a1ms.ui.fragments;

import android.content.Context;
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
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.futuremind.recyclerviewfastscroll.FastScroller;

import java.util.ArrayList;
import java.util.List;

import uk.com.a1ms.R;
import uk.com.a1ms.adapters.CreateGroupsAdapter;
import uk.com.a1ms.db.A1MSUsersFieldsDataSource;
import uk.com.a1ms.db.dto.A1MSUser;

/**
 * Created by priju.jacobpaul on 13/07/16.
 */
public class CreateGroupsPreviewFragment extends BaseFragment implements CreateGroupsAdapter.CreateGroupsAdapterListener{

    private RecyclerView mRecyclerView;
    private FastScroller mFastScroller;
    private Toolbar mToolbar;
    private A1MSUsersFieldsDataSource mDataSource;
    private CreateGroupsAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private HorizontalScrollView mScrollView;
    private LinearLayout mLLPreviewHolder;
    private MenuItem mMenuNext;

    List<A1MSUser> mSelectedA1MSUsers = new ArrayList<>();
    List<A1MSUser> mA1MSUsers;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public static CreateGroupsPreviewFragment newInstance(){

        CreateGroupsPreviewFragment fragment = new CreateGroupsPreviewFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mDataSource = new A1MSUsersFieldsDataSource(context);
        mDataSource.open();
        mA1MSUsers = mDataSource.getAllA1MSUsers(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.creategroups_fragment,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.a1msusers_recycler_view);
        mFastScroller = (FastScroller) view.findViewById(R.id.fastscroll);
        mLLPreviewHolder = (LinearLayout) view.findViewById(R.id.linearlayout_previewholder);
        mScrollView = (HorizontalScrollView)view.findViewById(R.id.scrollview);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setNestedScrollingEnabled(false);
    }

    @Override
    public void onResume() {
        super.onResume();

        if(mAdapter == null) {

            mAdapter = new CreateGroupsAdapter(mA1MSUsers, this);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
            mFastScroller.setRecyclerView(mRecyclerView);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mDataSource.close();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu( menu, inflater );
        inflater.inflate(R.menu.menu_create_group,menu);
        mMenuNext = menu.findItem(R.id.action_create_groups);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.action_create_groups){
            if(item.isEnabled()){
                
                return true;
            }
        }
        return false;
    }


    @Override
    public void onClick(View view, boolean isChecked,int position) {

    }

    @Override
    public boolean onBackPressed() {
        getActivity().finish();
        return super.onBackPressed();
    }

}
