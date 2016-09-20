package uk.com.a1ms.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.widget.TextView;

import uk.com.a1ms.A1MSApplication;
import uk.com.a1ms.R;
import uk.com.a1ms.db.A1MSUsersFieldsDataSource;
import uk.com.a1ms.db.dto.A1MSGroup;

/**
 * Created by priju.jacobpaul on 16/09/16.
 */
public class GroupNameChangeFragment extends BaseFragment {



    private A1MSGroup mGroupDetails;
    private A1MSUsersFieldsDataSource a1MSUsersFieldsDataSource;
    private GroupNameChangeFragmentListener infoFragmentListener;
    private EditText mETGroupName;
    private TextView mTVCounter;
    private String originalName;
    private MenuItem renameMenuItem;

    public interface GroupNameChangeFragmentListener{
        void onNameChange(String groupName);
    }

    public static GroupNameChangeFragment getInstance(A1MSGroup a1MSGroup, GroupNameChangeFragmentListener listener){

        GroupNameChangeFragment fragment = new GroupNameChangeFragment();
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
        return inflater.inflate(R.layout.group_change_name_fragment,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setCollapsible(false);
        toolbar.setTitle(getString(R.string.group_name));

        originalName = mGroupDetails.getGroupName();
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mETGroupName = (EditText)view.findViewById(R.id.edittext_group_change_name);
        mTVCounter = (TextView)view.findViewById(R.id.textview_counters);
        mETGroupName.setText(mGroupDetails.getGroupName());
        mETGroupName.requestFocus();
        mETGroupName.selectAll();

        mETGroupName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                changeTextLength(editable);
            }
        });

    }

    private void changeTextLength(Editable editable){
        int maxLength = getResources().getInteger(R.integer.group_name_max_length);
        mTVCounter.setText(String.valueOf(maxLength - editable.length()));
        if(originalName.compareTo(editable.toString()) == 0){
            renameMenuItem.setEnabled(false);
        }
        else {
            renameMenuItem.setEnabled(true);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_group_changename,menu);
        renameMenuItem = menu.findItem(R.id.action_rename_groups);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                getActivity().onBackPressed();
                return true;
            }
            case R.id.action_rename_groups:{
                if(infoFragmentListener != null){
                    infoFragmentListener.onNameChange(mETGroupName.getText().toString());
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
