package com.example.sudhaker_ftc_test;

import android.content.ContentValues;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sudhaker_ftc_test.apputils.ValidateEmailAddress;
import com.example.sudhaker_ftc_test.database.App_DataSource;
import com.example.sudhaker_ftc_test.database.User;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText et_EmailId, et_NewPassword, et_ConfirmNewPassword;
    Button btn_ResetPasswod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        findViewsByIds();
    }

    private void findViewsByIds() {
        et_EmailId = (EditText) findViewById(R.id.et_EmailId);
        et_NewPassword = (EditText) findViewById(R.id.et_NewPassword);
        et_ConfirmNewPassword = (EditText) findViewById(R.id.et_ConfirmNewPassword);
        btn_ResetPasswod = (Button) findViewById(R.id.btn_ResetPasswod);
        btn_ResetPasswod.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_ResetPasswod:
                resetPassWord();
                break;
        }
    }

    private void resetPassWord() {
        if (validateUserInputs()) {
            ContentValues values = new ContentValues();
            values.put(User.USER_EMAIL, et_NewPassword.getText().toString());
            App_DataSource app_dataSource = App_DataSource.getAppDataSource(getApplicationContext());
            app_dataSource.openDb();
            int updateCount = app_dataSource.resetPassword(et_EmailId.getText().toString(), values);
            app_dataSource.closeDb();
            if (updateCount > 0) {
                Toast.makeText(this, R.string.passUpdated, Toast.LENGTH_SHORT).show();
                onBackPressed();
            } else {
                Toast.makeText(this, "Email id not found ", Toast.LENGTH_SHORT).show();
            }

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
        if (et_NewPassword.getText().toString().matches("")) {
            Toast.makeText(this, R.string.enter_Password, Toast.LENGTH_SHORT).show();
            return false;

        }
        if (et_ConfirmNewPassword.getText().toString().matches("")) {
            Toast.makeText(this, R.string.enter_confirmPass, Toast.LENGTH_SHORT).show();
            return false;

        }
        if (!et_NewPassword.getText().toString().equals(et_ConfirmNewPassword.getText().toString())) {
            Toast.makeText(this, R.string.pass_confrim_notMatch, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
