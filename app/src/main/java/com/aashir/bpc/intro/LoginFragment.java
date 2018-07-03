package com.aashir.bpc.intro;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.aashir.bpc.R;
import com.aashir.bpc.util.Utils;

public class LoginFragment extends Fragment {

    private TextInputLayout mInputUser;
    private TextInputLayout mInputPass;
    private IntroInterface mListener;
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getIntExtra("error", -1) > -1){
                if(intent.getIntExtra("error", -1) == 0) {
                    validate("Enter a Username", "Enter a Username");
                } else {
                    validate("Invalid Username", "Invalid Password");
                }
            }
        }
    };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (IntroInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement IntroInterface");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver, new IntentFilter("login"));
        View v = inflater.inflate(R.layout.intro_fragment_login, container, false);
        mInputUser = (TextInputLayout) v.findViewById(R.id.text_input_layout_user);
        mInputPass = (TextInputLayout) v.findViewById(R.id.text_input_layout_pass);

        mInputPass.getEditText().setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    validate("Enter a Username", "Enter a Password");
                }
                return false;
            }
        });

        return v;
    }

    public void validate(String usererror, String passerror) {
        if (mInputUser.getEditText().length() == 0) mInputUser.setError(usererror);
        if (mInputPass.getEditText().length() == 0) mInputPass.setError(passerror);
        if (mInputUser.getEditText().length() > 0 && mInputUser.getEditText().length() > 0) {
            if (Utils.gotInternet(getActivity())) {
                if (isAdded()) Toast.makeText(getActivity(), "Logging in...", Toast.LENGTH_SHORT).show();
                mListener.onLogin(mInputUser.getEditText().getText().toString(), mInputPass.getEditText().getText().toString());
            } else {
                if (isAdded())
                    Toast.makeText(getActivity(), "Failed to login\nCheck your internet connection", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
