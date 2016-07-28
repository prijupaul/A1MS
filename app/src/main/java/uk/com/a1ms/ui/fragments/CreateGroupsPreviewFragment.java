package uk.com.a1ms.ui.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.futuremind.recyclerviewfastscroll.FastScroller;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.util.List;

import uk.com.a1ms.R;
import uk.com.a1ms.adapters.CreateGroupsAdapter;
import uk.com.a1ms.databinding.CreategroupsGroupnameBinding;
import uk.com.a1ms.db.dto.A1MSUser;
import uk.com.a1ms.dialogutil.DialogCallBackListener;
import uk.com.a1ms.dialogutil.DialogUtil;
import uk.com.a1ms.ui.uiutil.BottomSheetHelper;
import uk.com.a1ms.ui.uiutil.ImagePickerBottomSheet;
import uk.com.a1ms.ui.uiutil.OnActionCallback;
import uk.com.a1ms.util.FileUtil;
import uk.com.a1ms.util.ImagePresenter;
import uk.com.a1ms.util.PermissionRequestManager;

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
    private ImageView mGroupsIconPreview;
    private CreategroupsGroupnameBinding binding;
    private ImagePresenter imagePresenter;

    private String mCurrentGroupProfilePhotoPath;

    private final int CHOOSE_CAMERA_REQUEST_CODE = 991;
    private final int CHOOSE_GALLERY_REQUEST_CODE = 992;

    public interface GroupPreviewListener {
        void onGroupCreated(String groupName, List<A1MSUser> a1MSUsers);
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
//        return inflater.inflate(R.layout.creategroups_groupname, container, false);
        binding = DataBindingUtil.inflate(inflater, R.layout.creategroups_groupname, container, false);
        imagePresenter = new ImagePresenter();
        binding.setFragment(this);
        binding.setImagepresenter(imagePresenter);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.a1msusers_recycler_view);
        mFastScroller = (FastScroller) view.findViewById(R.id.fastscroll);
        mEditTextGroupName = (EditText) view.findViewById(R.id.edittext_groupname);
        mGroupsIconPreview = (ImageView) view.findViewById(R.id.imageview_groupicon_preview);

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
                if (mMenuNext == null) {
                    return;
                }

                if (editable.length() > 0) {
                    mMenuNext.setEnabled(true);
                } else {
                    mMenuNext.setEnabled(false);
                }
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();

        if (mAdapter == null) {
            mAdapter = new CreateGroupsAdapter(mA1MSUsers, false, this);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
            mFastScroller.setRecyclerView(mRecyclerView);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        BottomSheetHelper.dismiss(getFragmentManager());
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
                mGroupPreviewListener.onGroupCreated(mEditTextGroupName.getText().toString(), mA1MSUsers);
                return true;
            }
        }

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return false;
    }


    @Override
    public void onClick(View view, boolean isChecked, int position) {

    }

    /**
     * Invoked from the xml file
     *
     * @param view
     */
    public void onGroupIconClicked(View view) {

        if(requestPermissions()){
            showBottomSheets();
        }
    }

    public void showBottomSheets(){

        ImagePickerBottomSheet imagePickerBottomSheet = ImagePickerBottomSheet.newInstance();
        imagePickerBottomSheet.setActionCallback(new OnActionCallback() {
            @Override
            public void onAction(int id) {

                BottomSheetHelper.dismiss(getFragmentManager());

                if (id == R.id.ll_camera_holder) {
                    showCamera();
                } else if (id == R.id.ll_gallery_holder) {
                    showGallery();
                }
            }
        });

        BottomSheetHelper.showBottomSheet(getFragmentManager(), imagePickerBottomSheet);
    }

    private boolean requestPermissions(){

        final String[] permissionsRequired = new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE
        };

        if (!PermissionRequestManager.checkPermissions(getActivity(),permissionsRequired)) {


            boolean shouldRequestPermissionRationale;
            shouldRequestPermissionRationale = ( ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),Manifest.permission.CAMERA) |
                    ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),Manifest.permission.READ_EXTERNAL_STORAGE));

            if (shouldRequestPermissionRationale) {

                DialogUtil.showOKDialog(getActivity(),
                        getActivity().getString(R.string.permission_title),
                        getActivity().getString(R.string.permission_camera),
                        getActivity().getString(android.R.string.ok),
                        new DialogCallBackListener() {
                            @Override
                            public void run() {
                                PermissionRequestManager.requestPermission(getActivity(), permissionsRequired, 1001);
                                return;
                            }
                        }, false);
            } else {
                PermissionRequestManager.requestPermission(getActivity(), permissionsRequired, 1001);
            }

            return false;

        }
        return true;
    }

    private void showGallery() {
        mCurrentGroupProfilePhotoPath = null;
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, CHOOSE_GALLERY_REQUEST_CODE);

    }

    private void showCamera() {

        // create Intent to take a picture and return control to the calling application

        FileUtil.createMediaFolder(getActivity());

        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            Toast.makeText(getContext(), "not mounted", Toast.LENGTH_LONG).show();
            return;

        }

        Uri fileUri = Uri.fromFile(FileUtil.generateGroupProfilePhotos()); // create a file to save the image
        mCurrentGroupProfilePhotoPath = fileUri.getPath();

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name

        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {

            startActivityForResult(intent, CHOOSE_CAMERA_REQUEST_CODE);

        }
    }


    @Override
    public boolean onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() != 0) {
            getFragmentManager().popBackStack();
            return true;
        }
        return super.onBackPressed();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("code", "" + requestCode);

        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == CHOOSE_CAMERA_REQUEST_CODE) {
                Bundle extras = data.getExtras();
                if (extras != null) {
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    imagePresenter.setImageBitmap(imageBitmap);
                }

                Uri imageUri = Uri.fromFile(new File(mCurrentGroupProfilePhotoPath));
                cropImage(imageUri);

            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == getActivity().RESULT_OK) {
                    Uri resultUri = result.getUri();
                    imagePresenter.fitImageOnView(mGroupsIconPreview, resultUri.getPath());
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                }
            } else if (requestCode == CHOOSE_GALLERY_REQUEST_CODE) {
                Uri selectedImage = data.getData();

                if(mCurrentGroupProfilePhotoPath == null){
                    Uri fileUri = Uri.fromFile(FileUtil.generateGroupProfilePhotos()); // create a file to save the image
                    mCurrentGroupProfilePhotoPath = fileUri.getPath();
                }
                cropImage(selectedImage);
            }

        }
    }

    private void cropImage(Uri imageUri){
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setCropShape(CropImageView.CropShape.RECTANGLE)
                .setOutputUri(Uri.fromFile(new File(mCurrentGroupProfilePhotoPath)))
                .setRequestedSize(mGroupsIconPreview.getWidth(), mGroupsIconPreview.getHeight())
                .start(getActivity().getApplicationContext(), this);
    }
}
