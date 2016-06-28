package uk.com.a1ms.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

/**
 * Created by priju.jacobpaul on 27/05/16.
 */
public class BaseFragment<T> extends Fragment {

    T mCallback;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (T)getParentFragment();
        }
        catch (ClassCastException e){
            throw new ClassCastException(context.toString() + " must implement " + getClass().getName() + ".OnEventListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }


    public boolean onBackPressed(){
        return false;
    }
}
