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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnRegoActivationFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RegistrationAcceptActivationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegistrationAcceptActivationFragment extends BaseFragment {

    private OnRegoActivationFragmentInteractionListener mListener;
    private EditText mETActivationCode;
    private MenuItem mMenuNext;
    private TextView mTVActivationCodeSend;
    private static final String PHONE_NUMBER = "phone";

    public RegistrationAcceptActivationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment BlankFragment.
     */

    public static RegistrationAcceptActivationFragment newInstance(String phoneNumber) {
        RegistrationAcceptActivationFragment fragment = new RegistrationAcceptActivationFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PHONE_NUMBER,phoneNumber);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.registration_type_activationcode, container, false);
        mETActivationCode = (EditText)view.findViewById(R.id.edittext_activationcode);
        mTVActivationCodeSend = (TextView)view.findViewById(R.id.textview_activationcode_hint);

        Bundle bundle = getArguments();
        String string = String.format(getResources().getString(R.string.hint_enter_phone_activation_code_wait),
                bundle.getString(PHONE_NUMBER));
        mTVActivationCodeSend.setText(string);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        mETActivationCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable editable) {

                if((editable.toString().length() == 4 )){
                    mMenuNext.setEnabled(true);
                }
                else {
                    mMenuNext.setEnabled(false);
                }
            }
        });


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        setHasOptionsMenu(true);
        if (context instanceof OnRegoActivationFragmentInteractionListener) {
            mListener = (OnRegoActivationFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnRegoActivationFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_phone_registration,menu);
        mMenuNext = menu.findItem(R.id.action_send_activation_code);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.action_send_activation_code){
            if(item.isEnabled()){
                mListener.onSendActivationCode(mETActivationCode.getText().toString());
                return true;
            }
        }
        return false;
    }

     public interface OnRegoActivationFragmentInteractionListener {
        void onSendActivationCode(String activationCode);
    }
}
