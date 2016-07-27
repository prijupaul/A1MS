package uk.com.a1ms.ui.uiutil;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import uk.com.a1ms.R;

/**
 * Created by priju.jacobpaul on 26/07/16.
 */
public class ImagePickerBottomSheet extends BaseBottomSheet {


    public static ImagePickerBottomSheet newInstance() {
        ImagePickerBottomSheet fragment = new ImagePickerBottomSheet();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set Theme_A1MS_BottomSheetDialog to replace the default
        // behavior_peekHeight, so that content of this bottom sheet will be shown
        // completely
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_A1MS_BottomSheetDialog);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottomsheet_pick_camera_gallery, container, false);
        LinearLayout linearLayoutGallery = (LinearLayout) view.findViewById(R.id.ll_camera_holder);
        LinearLayout linearLayoutCamera = (LinearLayout) view.findViewById(R.id.ll_gallery_holder);
        linearLayoutGallery.setOnClickListener(this);
        linearLayoutCamera.setOnClickListener(this);
        return view;
    }


}
