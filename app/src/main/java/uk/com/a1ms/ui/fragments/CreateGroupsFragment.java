package uk.com.a1ms.ui.fragments;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.futuremind.recyclerviewfastscroll.FastScroller;

import java.util.ArrayList;
import java.util.List;

import uk.com.a1ms.R;
import uk.com.a1ms.adapters.CreateGroupsAdapter;
import uk.com.a1ms.db.A1MSUsersFieldsDataSource;
import uk.com.a1ms.db.dto.A1MSUser;
import uk.com.a1ms.util.BuildUtils;
import uk.com.a1ms.util.NotificationController;

/**
 * Created by priju.jacobpaul on 13/07/16.
 */
public class CreateGroupsFragment extends BaseFragment implements CreateGroupsAdapter.CreateGroupsAdapterListener, NotificationController.NotificationListener{

    private RecyclerView mRecyclerView;
    private FastScroller mFastScroller;
    private Toolbar mToolbar;
    private A1MSUsersFieldsDataSource mUsersDataSource;
    private CreateGroupsAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private HorizontalScrollView mScrollView;
    private LinearLayout mLLPreviewHolder;
    private MenuItem mMenuNext;
    private CreateGroupsFragmentListener mCreateGroupsFragmentListener;

    List<A1MSUser> mSelectedA1MSUsers = new ArrayList<>();
    List<A1MSUser> mA1MSUsers;


    public interface CreateGroupsFragmentListener{
        void onGroupMembersSelected(List<A1MSUser> selectedA1MSUsers);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public static CreateGroupsFragment newInstance(CreateGroupsFragmentListener createGroupsFragmentListener){

        CreateGroupsFragment fragment = new CreateGroupsFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        fragment.mCreateGroupsFragmentListener = createGroupsFragmentListener;
        return fragment;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        NotificationController.getInstance().addObserver(this,NotificationController.didOpenDatabase);
        mUsersDataSource = new A1MSUsersFieldsDataSource(context);
        mUsersDataSource.open();
        mA1MSUsers = mUsersDataSource.getAllA1MSUsers(true,false);
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

        if(mAdapter == null) {
            mAdapter = new CreateGroupsAdapter(mA1MSUsers, true,this);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
            mFastScroller.setRecyclerView(mRecyclerView);
        }

        if(((AppCompatActivity)getActivity()).getSupportActionBar() != null){
            mToolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
            mToolbar.setSubtitle(String.format("(%d/100)",mSelectedA1MSUsers.size()));
        }
    }

    @Override
    public void onNotificationReceived(int id, Object... args) {

        if(id == NotificationController.didOpenDatabase){
            mA1MSUsers = mUsersDataSource.getAllA1MSUsers(true,false);
            if(mAdapter == null) {
                mAdapter = new CreateGroupsAdapter(mA1MSUsers, true,this);
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                mFastScroller.setRecyclerView(mRecyclerView);
            }

            NotificationController.getInstance().removeObserver(this,NotificationController.didOpenDatabase);
        }

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mUsersDataSource.close();
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
                mCreateGroupsFragmentListener.onGroupMembersSelected(mSelectedA1MSUsers);
                return true;
            }
        }

        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return false;
    }


    @Override
    public void onClick(View view, boolean isChecked,int position) {

        A1MSUser a1MSUser = mA1MSUsers.get(position);
        if(isChecked) {
            addContact(a1MSUser);
        }
        else {
            removeContact(a1MSUser);
        }
    }

    @Override
    public boolean onBackPressed() {
        getActivity().finish();
        return super.onBackPressed();
    }

    private void addContact(final A1MSUser a1MSUser) {

        if(mSelectedA1MSUsers.size() == BuildUtils.getMaxGroupsMemberSize()){
            Toast.makeText(getActivity(),getString(R.string.max_group_size_reached),Toast.LENGTH_LONG).show();
            return;
        }

        View view = getActivity().getLayoutInflater().inflate(R.layout.creategroups_memberpreview, null);

        Bitmap contact = BitmapFactory.decodeResource(getResources(), R.drawable.contact);
        SpannableStringBuilder ssb1 = new SpannableStringBuilder(" " + a1MSUser.getName());
        ssb1.setSpan(new ImageSpan(getActivity(), contact), 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

        TextView textView = (TextView) view.findViewById(R.id.tv_name);
        textView.setText(ssb1, TextView.BufferType.SPANNABLE);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(5, 20, 15, 0);
        view.setTag(a1MSUser.getMobile());

        mLLPreviewHolder.addView(view, layoutParams);

        performFullScrollRight();

        Animation animation = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.slide_right_in);
        animation.setStartOffset(0);
        view.startAnimation(animation);

        ImageView delete = (ImageView)view.findViewById(R.id.imageView_delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeContact(a1MSUser);
            }
        });

        mSelectedA1MSUsers.add(a1MSUser);

        if(mSelectedA1MSUsers.size() > 1) {
            mMenuNext.setEnabled(true);
        }

        mToolbar.setSubtitle(String.format("(%d/100)",mSelectedA1MSUsers.size()));

    }

    private void removeContact(A1MSUser a1MSUser){

        View viewToRemove = mLLPreviewHolder.findViewWithTag(a1MSUser.getMobile());
        mLLPreviewHolder.removeView(viewToRemove);

        Animation animation = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.slide_left_out);
        animation.setStartOffset(0);
        viewToRemove.startAnimation(animation);

        performFullScrollRight();

        mAdapter.setCheckBoxStatusChange(a1MSUser,false);

        mSelectedA1MSUsers.remove(a1MSUser);

        if(mSelectedA1MSUsers.size() == 0){
            mMenuNext.setEnabled(false);
        }

        mToolbar.setSubtitle(String.format("(%d/100)",mSelectedA1MSUsers.size()));
    }

    private void performFullScrollRight(){
        mScrollView.postDelayed(new Runnable() {
            public void run() {
                ViewTreeObserver viewTreeObserver = mScrollView.getViewTreeObserver();
                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener(){
                    @Override
                    public void onGlobalLayout() {
                        mScrollView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        mScrollView.scrollTo(mLLPreviewHolder.getWidth() + 30, 0);
                    }
                });
            }
        }, 100L);
    }
}
