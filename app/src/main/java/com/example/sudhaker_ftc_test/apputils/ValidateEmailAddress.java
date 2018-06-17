package com.example.sudhaker_ftc_test.apputils;

/**
 * Created by sudhaker on 1/7/2018.
 */
public class ValidateEmailAddress {

    ////Validation of Email Addres used at various places in Application
    public static boolean isValidEmail(CharSequence email)
    {
        if(email==null)
        {
            return false;
        }
        else
        {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
    }
}
