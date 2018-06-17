package com.example.sudhaker_ftc_test;

import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sudhaker_ftc_test.apputils.ValidateEmailAddress;
import com.example.sudhaker_ftc_test.database.App_DataSource;
import com.example.sudhaker_ftc_test.database.User;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText et_Name, et_EmailId, et_Password, et_ConfirmPassword;
    Button btn_SignUp, btn_Login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        findViewsByIds();
    }

    private void findViewsByIds() {
        et_Name = (EditText) findViewById(R.id.et_Name);
        et_EmailId = (EditText) findViewById(R.id.et_EmailId);
        et_Password = (EditText) findViewById(R.id.et_Password);
        et_ConfirmPassword = (EditText) findViewById(R.id.et_ConfirmPassword);
        btn_SignUp = (Button) findViewById(R.id.btn_SignUp);
        btn_Login = (Button) findViewById(R.id.btn_Login);
        btn_Login.setOnClickListener(this);
        btn_SignUp.setOnClickListener(this);

    }

    private void signUp() {
        if (validateUserInputs()) {


            App_DataSource app_dataSource = App_DataSource.getAppDataSource(getApplicationContext());
            app_dataSource.openDb();
            if (app_dataSource.userAllreadyExist(et_EmailId.getText().toString())) {
                Toast.makeText(this, R.string.user_AllreadyExist, Toast.LENGTH_SHORT).show();
                app_dataSource.closeDb();
                return;
            }
            ContentValues values = new ContentValues();
            values.put(User.USER_NAME, et_Name.getText().toString());
            values.put(User.USER_EMAIL, et_EmailId.getText().toString());
            values.put(User.USER_PASSWORD, et_Password.getText().toString());
            long userCount = app_dataSource.insertIntoUserTable(values);
            app_dataSource.closeDb();
            if (userCount > 0) {
                ///Signup Success
                Intent iAppointment = new Intent(getApplicationContext(), AppointmentListActivity.class);
                startActivity(iAppointment);
                finish();

            }
        }


    }

    private boolean validateUserInputs() {
        if (et_Name.getText().toString().matches("")) {
            Toast.makeText(this, R.string.enterNameFirst, Toast.LENGTH_SHORT).show();
            return false;
        }
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
        if (et_Password.getText().toString().length() < 6) {
            Toast.makeText(this, R.string.min_pass, Toast.LENGTH_SHORT).show();
            return false;

        }

        if (et_ConfirmPassword.getText().toString().matches("")) {
            Toast.makeText(this, R.string.enter_confirmPass, Toast.LENGTH_SHORT).show();
            return false;

        }
        if (!et_Password.getText().toString().equals(et_ConfirmPassword.getText().toString())) {
            Toast.makeText(this, R.string.pass_confrim_notMatch, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void login() {
        /*Navigate To Login Activity*/
        Intent iLogin = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(iLogin);
        finish();
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

        }

    }

}
