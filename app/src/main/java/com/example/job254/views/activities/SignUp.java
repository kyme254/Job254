package com.example.job254.views.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import cn.pedant.SweetAlert.SweetAlertDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;
import com.example.job254.R;
import com.example.job254.database.FirebaseUtil;
import com.example.job254.views.activities.VerifyOTP;

public class SignUp extends AppCompatActivity {

    private Button callSignIn, signIn;
    private TextView wlcTxt, logoTxt;
    private ImageView logo;
    private TextInputLayout phoneNumber,fullName,email,password,confirmPassword;
    CountryCodePicker mCountryCodePicker;
    RadioGroup mRadioGroup;
    RadioButton selectedUserType;
    private static final String SHARED_PREFS = "com.pyropy.nodal.STATUS";
    private static final String REG_STATUS = "regstatus";
    String mUserType;
    private FirebaseUtil mUtil;
    public static boolean status = false;
    private String mUserPhone;
    private String mUserEmail;
    private String mUserFullname;
    private String mUserPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up);

        initComponents();
    }

    private void initComponents() {
        signIn = findViewById(R.id.signup_btn);
        wlcTxt = findViewById(R.id.wlc_txt);
        logoTxt = findViewById(R.id.logo_txt);
        logo = findViewById(R.id.app_dark_logo);
        mCountryCodePicker = findViewById(R.id.country_code_picker);
        phoneNumber = findViewById(R.id.phone);
        fullName = findViewById(R.id.full_name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        mRadioGroup = findViewById(R.id.user_type);
        confirmPassword = findViewById(R.id.confirm_password);
        mUtil = FirebaseUtil.getInstances(this);

        callSignIn = findViewById(R.id.to_login);
        addEventToCallSignInBtn();
        addEventToSignInBtn();
    }

    private void addEventToSignInBtn() {
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateUserType() | !validateEmail() | !validatePassword()){
                    return;
                }

                mUserFullname = fullName.getEditText().getText().toString().trim();
                mUserEmail = email.getEditText().getText().toString().trim();
                mUserPassword = password.getEditText().getText().toString().trim();
                int stdType = mRadioGroup.getCheckedRadioButtonId();
                if (stdType == R.id.student){mUserType = "Student";}
                if (stdType == R.id.instructor){mUserType = "Instructor";}
                if (stdType == R.id.freelancer){mUserType = "Freelancer";}
                if (stdType == R.id.buyer){mUserType = "Buyer";}

                String userEnteredPhone = phoneNumber.getEditText().getText().toString().trim();
                if (userEnteredPhone.charAt(0) == '0'){
                    userEnteredPhone = userEnteredPhone.substring(1);
                }
                mUserPhone = "+"+mCountryCodePicker.getFullNumber()+userEnteredPhone;

                checkUserDoesNotExistAndSave();
            }
        });
    }

    private void checkUserDoesNotExistAndSave() {
        String mailParts[] = mUserEmail.split("@");
        String mUserNode = mailParts[0];
        //Query checkUser = mUtil.mFirebaseDatabase.getReference("Users").orderByChild("phone").equalTo(mUserPhone);
        Query checkUser = mUtil.mFirebaseDatabase.getReference("Users").orderByKey().equalTo(mUserNode);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Toast.makeText(getApplicationContext(), "User already exists!", Toast.LENGTH_SHORT).show();
                }else{
                    Intent uIntent = new Intent(getApplicationContext(), VerifyOTP.class);
                    uIntent.putExtra("fullname", mUserFullname);
                    uIntent.putExtra("email", mUserEmail);
                    uIntent.putExtra("password", mUserPassword);
                    uIntent.putExtra("phoneNo", mUserPhone);
                    uIntent.putExtra("userType", mUserType);

                    startActivity(uIntent);
                    //saveRegistration();
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validatePassword() {
        String confirm = confirmPassword.getEditText().getText().toString();
        String pass = password.getEditText().getText().toString();
        if (!pass.isEmpty() | !confirm.isEmpty()){
            if (pass.matches(confirm)){
                confirmPassword.setError(null);
                confirmPassword.setErrorEnabled(false);
                return true;
            }else {
                confirmPassword.setError("password does not match");
                return false;
            }
        }else{
            confirmPassword.setError("ensure to fill both password fields");
            return false;
        }
    }

    private void saveRegistration() {
        SharedPreferences uSharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = uSharedPreferences.edit();
        edit.putString(REG_STATUS, "pending");
        edit.commit();
    }

    private boolean validateEmail() {
        String val = email.getEditText().getText().toString().trim();
        String checkEmail = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()){
            email.setError("Field cannot be empty");
            return false;
        }else if (!val.matches(checkEmail)){
            email.setError("Invalid Email!");
            return false;
        }else{
            email.setError(null);
            email.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateUserType() {
        if (mRadioGroup.getCheckedRadioButtonId()==-1){
            Toast.makeText(getApplicationContext(),"Please select a User type", Toast.LENGTH_SHORT).show();
            return false;
        }else{
            return true;
        }
    }

    private void addEventToCallSignInBtn() {
        callSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent uIntent = new Intent(SignUp.this,Login.class);

//                Pair[] uPairs = new Pair[4];
//                uPairs[0]  = new Pair<View, String>(logo,"logo_image");
//                uPairs[1]  = new Pair<View, String>(signIn,"user_btn");
//                uPairs[2]  = new Pair<View, String>(wlcTxt,"welcome_text");
//                uPairs[3]  = new Pair<View, String>(logoTxt,"logo_text");
//
//                ActivityOptions uOptions = ActivityOptions.makeSceneTransitionAnimation(SignUp.this,uPairs);
                //startActivity(uIntent, uOptions.toBundle());
                startActivity(uIntent);
            }
        });
    }
}