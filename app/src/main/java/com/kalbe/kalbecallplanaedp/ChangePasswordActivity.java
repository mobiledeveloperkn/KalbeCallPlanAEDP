package com.kalbe.kalbecallplanaedp;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kalbe.kalbecallplanaedp.BL.clsHelperBL;
import com.kalbe.kalbecallplanaedp.BL.clsMainBL;
import com.kalbe.kalbecallplanaedp.Common.clsToken;
import com.kalbe.kalbecallplanaedp.Common.mUserLogin;
import com.kalbe.kalbecallplanaedp.Data.VolleyResponseListener;
import com.kalbe.kalbecallplanaedp.Data.clsHardCode;
import com.kalbe.kalbecallplanaedp.Repo.clsTokenRepo;
import com.kalbe.kalbecallplanaedp.Repo.mUserLoginRepo;
import com.kalbe.kalbecallplanaedp.ResponseDataJson.PushLogError.PushLogError;
import com.kalbe.kalbecallplanaedp.ResponseDataJson.loginMobileApps.LoginMobileApps;
import com.kalbe.kalbecallplanaedp.Utils.AuthenticatorUtil;
import com.kalbe.mobiledevknlibs.ToastAndSnackBar.ToastCustom;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.List;

import static com.oktaviani.dewi.mylibrary.authenticator.AccountGeneral.ARG_AUTH_TYPE;
import static com.oktaviani.dewi.mylibrary.authenticator.AccountGeneral.ARG_IS_ADDING_NEW_ACCOUNT;
import static com.oktaviani.dewi.mylibrary.authenticator.AccountGeneral.PARAM_USER_PASS;

public class ChangePasswordActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextInputEditText et_current_pw, et_new_pw, et_confirm_pw;
    Button btn_change_pw;
    clsTokenRepo tokenRepo;
    List<clsToken> dataToken;
    private Gson gson;
    mUserLogin dtLogin;
    private AccountManager mAccountManager;
    boolean isOnCreate = false;
    private String i_View ="Fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.green_300));
        }
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Change Password");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mAccountManager = AccountManager.get(getBaseContext());
        et_current_pw = (TextInputEditText)findViewById(R.id.et_current_pw);
        et_new_pw = (TextInputEditText)findViewById(R.id.et_new_pw);
        et_confirm_pw = (TextInputEditText)findViewById(R.id.et_confirm_pw);
        btn_change_pw = (Button)findViewById(R.id.btn_change_pw);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();

        dtLogin = new clsMainBL().getUserLogin(getApplicationContext());


//        et_current_pw.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (et_new_pw.getText().length()<6&&et_new_pw.getText().length()>0){
//                    ToastCustom.showToasty(ChangePasswordActivity.this,"Passwords must be at least 6 characters long",4);
//                }else if (!et_new_pw.getText().toString().equals(et_confirm_pw.getText().toString())){
//                    ToastCustom.showToasty(ChangePasswordActivity.this,"Passwords do not match",4);
//                }
//            }
//        });
//
//        et_new_pw.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//               if (et_new_pw.getText().length()>6&&!et_new_pw.getText().toString().equals(et_confirm_pw.getText().toString())){
//                    ToastCustom.showToasty(ChangePasswordActivity.this,"Passwords do not match",4);
//                }
//            }
//        });
//
//        et_confirm_pw.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (et_new_pw.getText().length()<6&&et_new_pw.getText().length()>0){
//                    ToastCustom.showToasty(ChangePasswordActivity.this,"Passwords must be at least 6 characters long",4);
//                }
//            }
//        });
        et_current_pw.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    if (et_new_pw.getText().length()<6&&et_new_pw.getText().length()>0){
                        ToastCustom.showToasty(ChangePasswordActivity.this,"New passwords must be at least 6 characters long",4);
                    }else if (!et_new_pw.getText().toString().equals(et_confirm_pw.getText().toString())){
                        ToastCustom.showToasty(ChangePasswordActivity.this,"New passwords do not match",4);
                    }
            }
        });

        et_new_pw.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    if (et_new_pw.getText().length()>6&&!et_new_pw.getText().toString().equals(et_confirm_pw.getText().toString())){
                        ToastCustom.showToasty(ChangePasswordActivity.this,"New passwords do not match",4);
                    }
            }
        });

        et_confirm_pw.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    if (et_new_pw.getText().length()<6&&et_new_pw.getText().length()>0){
                        ToastCustom.showToasty(ChangePasswordActivity.this,"New passwords must be at least 6 characters long",4);
                    }
            }
        });

        btn_change_pw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isExist = false;
                Account[] accounts = new AuthenticatorUtil().countingAccount(mAccountManager);
                if (accounts!=null)
                if (accounts.length>0){
                    for (int i=0; i<accounts.length; i++){
                        if (accounts[i].name.equals(dtLogin.getTxtUserName().toUpperCase().toString())){
                            isExist = true;
                        }
                    }
                }

                if (isExist){
                    if (et_current_pw.getText().length()==0){
                        ToastCustom.showToasty(ChangePasswordActivity.this,"Please fill current password",4);
                    }else if (et_new_pw.getText().length()<6){
                        ToastCustom.showToasty(ChangePasswordActivity.this,"New passwords must be at least 6 characters long",4);
                    }else if (!et_new_pw.getText().toString().equals(et_confirm_pw.getText().toString())){
                        ToastCustom.showToasty(ChangePasswordActivity.this,"New passwords do not match",4);
                    }else if (et_new_pw.getText().toString().equals(et_confirm_pw.getText().toString())&&et_current_pw.getText().length()>0){
                        ChangePassword();
                    }
                }else {
                    onBackPressed();
                    ToastCustom.showToasty(ChangePasswordActivity.this,"Your Account Manager has been deleted",4);
                }
            }
        });
    }

    private void ChangePassword() {
        String strLinkAPI = new clsHardCode().linkChangePassword;
        JSONObject resJson = new JSONObject();
        JSONObject jData = new JSONObject();

        try {
            jData.put("username",dtLogin.getTxtUserName() );
            jData.put("intRoleId", dtLogin.getIntRoleID());
            jData.put("oldPassword", et_current_pw.getText().toString());
            jData.put("newPassword", et_new_pw.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            tokenRepo = new clsTokenRepo(getApplicationContext());
            dataToken = (List<clsToken>) tokenRepo.findAll();
            resJson.put("data", jData);
            resJson.put("device_info", new clsHardCode().pDeviceInfo());
            resJson.put("txtRefreshToken", dataToken.get(0).txtRefreshToken.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        final String mRequestBody = resJson.toString();
        new clsHelperBL().volleyLogin(ChangePasswordActivity.this, strLinkAPI, mRequestBody, "Please Wait....", new VolleyResponseListener() {
            @Override
            public void onError(String message) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response, Boolean status, String strErrorMsg) {
                Intent res = null;
                if (response != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        PushLogError model = gson.fromJson(jsonObject.toString(), PushLogError.class);
                        boolean txtStatus = model.getResult().isStatus();
                        String txtMessage = model.getResult().getMessage();
                        String txtMethode_name = model.getResult().getMethodName();
                        Account[] accounts = new AuthenticatorUtil().countingAccount(mAccountManager);

                        if (txtStatus == true){
                            for (int i=0; i<accounts.length; i++){
                                if (accounts[i].name.equals(dtLogin.getTxtUserName().toUpperCase().toString())){
                                    mAccountManager.setPassword(accounts[i], et_new_pw.getText().toString());
                                }
                            }
                            onBackPressed();
                            ToastCustom.showToasty(ChangePasswordActivity.this,"Success Change Password",1);

                        } else {
                            ToastCustom.showToasty(ChangePasswordActivity.this,txtMessage,4);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent parentIntent = NavUtils.getParentActivityIntent(this);
        parentIntent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(parentIntent);
        finish();
    }
}
