package com.example.user.memory_game;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginRegisterDialog extends DialogFragment {

    private String action;
    private TextView lblTitle;
    private EditText txtUserName;
    private EditText txtPassword;
    private Button btnOk;
    private Button btnCancel;
    private LoginOrRegisterListener listener;



    public void setAction(String action){
        this.action = action;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_register_dialog, container, false);
        lblTitle = view.findViewById(R.id.lblTitle);
        txtUserName = view.findViewById(R.id.txtUserName);
        txtPassword = view.findViewById(R.id.txtPassword);
        btnOk = view.findViewById(R.id.btnOk);
        btnCancel = view.findViewById(R.id.btnCancel);
        lblTitle.setText(action);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = txtUserName.getText().toString();
                String password = txtPassword.getText().toString();
                if (userName.isEmpty() || password.isEmpty()){
                    Toast.makeText(getContext() , "username and password are required", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (listener != null)
                    listener.onOk(userName, password);
                dismiss();

            }
        });
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        return view;
    }
    public void setListener(LoginOrRegisterListener listener){
        this.listener = listener;
    }
interface LoginOrRegisterListener{
        void onOk(String userName, String password);
}

}
