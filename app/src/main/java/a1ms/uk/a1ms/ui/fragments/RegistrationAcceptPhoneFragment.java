package a1ms.uk.a1ms.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import a1ms.uk.a1ms.R;
import a1ms.uk.a1ms.util.AndroidUtils;
import a1ms.uk.a1ms.util.BuildUtils;
import a1ms.uk.a1ms.util.PhoneConfigUtils;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnRegoAcceptPhoneFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RegistrationAcceptPhoneFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegistrationAcceptPhoneFragment extends BaseFragment {

    private OnRegoAcceptPhoneFragmentInteractionListener mListener;
    private EditText mETCountryCode;
    private EditText mETPhoneNumber;
    private TextView mTVCountry;
    private MenuItem mMenuNext;

    public RegistrationAcceptPhoneFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment BlankFragment.
     */

    public static RegistrationAcceptPhoneFragment newInstance() {
        RegistrationAcceptPhoneFragment fragment = new RegistrationAcceptPhoneFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.registration_add_phone, container, false);

        mTVCountry = (TextView)view.findViewById(R.id.textview_country);
        mETCountryCode = (EditText)view.findViewById(R.id.edittext_country_code);
        mETPhoneNumber = (EditText)view.findViewById(R.id.edittext_enter_phone);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mTVCountry.setText(PhoneConfigUtils.getCountryLocale());
        mETCountryCode.setText( PhoneConfigUtils.getCountryPhoneCode(getActivity()));

        mETPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable editable) {

                if((editable.toString().length() >= BuildUtils.getMaxPhoneNumberDigits() &&
                        (mETCountryCode.getText().length() >= 1))){
                    if(mMenuNext != null) {
                        mMenuNext.setEnabled(true);
                    }
                }
                else if(mMenuNext != null){
                    mMenuNext.setEnabled(false);
                }
            }
        });

        mETCountryCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if((editable.toString().length() >= 1 &&
                        (mETPhoneNumber.getText().length() >= BuildUtils.getMaxPhoneNumberDigits()))){
                    if(mMenuNext != null) {
                        mMenuNext.setEnabled(true);
                    }
                }
                else if(mMenuNext != null){
                    mMenuNext.setEnabled(false);
                }
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnRegoAcceptPhoneFragmentInteractionListener) {
            mListener = (OnRegoAcceptPhoneFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu( menu, inflater );
        inflater.inflate(R.menu.menu_phone_registration,menu);
        mMenuNext = menu.findItem(R.id.action_send_activation_code);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.action_send_activation_code){
            if(item.isEnabled()){

                // hide keyboard
                AndroidUtils.hideKeyboard(mETCountryCode);
                AndroidUtils.hideKeyboard(mETPhoneNumber);
                disableInput();
                mListener.onNextPressed(mETCountryCode.getText().toString(),mETPhoneNumber.getText().toString());
                return true;
            }
        }
        return false;
    }


    private void disableInput(){
        mMenuNext.setEnabled(false);
        mETCountryCode.setEnabled(false);
        mETPhoneNumber.setEnabled(false);
    }

     public interface OnRegoAcceptPhoneFragmentInteractionListener {
        void onNextPressed(String countryCode, String phoneNo);
    }
}
