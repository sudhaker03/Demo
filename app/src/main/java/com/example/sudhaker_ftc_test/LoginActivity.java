package com.example.sudhaker_ftc_test;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sudhaker_ftc_test.apputils.App_SharedPreferenceConstant;
import com.example.sudhaker_ftc_test.apputils.ValidateEmailAddress;
import com.example.sudhaker_ftc_test.database.App_DataSource;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private EditText et_EmailId, et_Password;
    private CheckBox cb_RememberMe;
    private Button btn_Login, btn_SignUp, btn_ForgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViewsByIds();
        setRemembermeData();
    }

    private void setRemembermeData() {
        SharedPreferences sharedPref = getSharedPreferences(App_SharedPreferenceConstant.app_PrefName, MODE_PRIVATE);
        if (!sharedPref.getString(App_SharedPreferenceConstant.user_EmailId, "").equals("") && !sharedPref.getString(App_SharedPreferenceConstant.user_Password, "").equals("")) {
            et_EmailId.setText(sharedPref.getString(App_SharedPreferenceConstant.user_EmailId, ""));
            et_Password.setText(sharedPref.getString(App_SharedPreferenceConstant.user_Password, ""));
            cb_RememberMe.setChecked(true);
        }
    }

    private void findViewsByIds() {
        et_EmailId = (EditText) findViewById(R.id.et_EmailId);
        et_Password = (EditText) findViewById(R.id.et_Password);
        cb_RememberMe = (CheckBox) findViewById(R.id.cb_RememberMe);
        btn_Login = (Button) findViewById(R.id.btn_Login);
        btn_SignUp = (Button) findViewById(R.id.btn_SignUp);
        btn_ForgotPassword = (Button) findViewById(R.id.btn_ForgotPassword);
        btn_Login.setOnClickListener(this);
        btn_SignUp.setOnClickListener(this);
        btn_ForgotPassword.setOnClickListener(this);
        cb_RememberMe.setOnCheckedChangeListener(this);
    }

    private void signUp() {
 /*Navigate To Sign Up Activity*/
        Intent iSignUp = new Intent(getApplicationContext(), SignUpActivity.class);
        startActivity(iSignUp);
        finish();

    }

    private void forgotPassword() {
         /*Navigate To Forgot Password Activity*/
        Intent iSignUp = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
        startActivity(iSignUp);

    }

    private void login() {
        if (validateUserInputs()) {

            App_DataSource app_dataSource = App_DataSource.getAppDataSource(getApplicationContext());
            app_dataSource.openDb();
            if (app_dataSource.isUserAvailable(et_EmailId.getText().toString(), et_Password.getText().toString())) {
                ///Login Success
                Intent iAppointment = new Intent(getApplicationContext(), AppointmentListActivity.class);
                startActivity(iAppointment);
                SharedPreferences app_UserPref = getSharedPreferences(App_SharedPreferenceConstant.app_PrefName, MODE_PRIVATE);
                SharedPreferences.Editor editor = app_UserPref.edit();
                if (cb_RememberMe.isChecked()) {
                    editor.putString(App_SharedPreferenceConstant.user_EmailId, et_EmailId.getText().toString());
                    editor.putString(App_SharedPreferenceConstant.user_Password, et_Password.getText().toString());
                    editor.commit();
                } else {
                    editor.clear();
                    editor.commit();
                }
                finish();

            } else {
                if (app_dataSource.userAllreadyExist(et_EmailId.getText().toString())) {
                    Toast.makeText(this, R.string.passwordNotMatch, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, R.string.email_id_or_passwordNotMatch, Toast.LENGTH_SHORT).show();
                }


            }
            app_dataSource.closeDb();
        }
    }

    private boolean validateUserInputs() {

        if (et_EmailId.getText().toString().matches("")) {
            Toast.makeText(this, R.string.enter_email, Toast.LENGTH_SHORT).show();
            return false;

        }
        if (!ValidateEmailAddress.isValidEmail(et_EmailId.getText().toString())) {
            Toast.makeText(this, R.string.valid_emial, Toast.LENGTH_SHORT).show();
            return false;

        }
        if (et_Password.getText().toString().matches("")) {
            Toast.makeText(this, R.string.enter_Password, Toast.LENGTH_SHORT).show();
            return false;

        }

        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_Login:
                login();
                break;
            case R.id.btn_SignUp:
                signUp();
                break;
            case R.id.btn_ForgotPassword:
                forgotPassword();
                break;

        }

    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

        SharedPreferences app_UserPref = getSharedPreferences(App_SharedPreferenceConstant.app_PrefName, MODE_PRIVATE);
        SharedPreferences.Editor editor = app_UserPref.edit();
        if (cb_RememberMe.isChecked()) {
            editor.putString(App_SharedPreferenceConstant.user_EmailId, et_EmailId.getText().toString());
            editor.putString(App_SharedPreferenceConstant.user_Password, et_Password.getText().toString());
            editor.commit();
        } else {
            editor.clear();
            editor.commit();
        }
    }
}
