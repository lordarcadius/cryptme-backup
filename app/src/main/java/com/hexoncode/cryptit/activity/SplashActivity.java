package com.hexoncode.cryptit.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.vending.licensing.AESObfuscator;
import com.google.android.vending.licensing.LicenseChecker;
import com.google.android.vending.licensing.LicenseCheckerCallback;
import com.google.android.vending.licensing.Policy;
import com.google.android.vending.licensing.ServerManagedPolicy;
import com.hexoncode.cryptit.OnBoardingActivity;
import com.hexoncode.cryptit.lvl.Api;
import com.hexoncode.cryptit.R;
import com.hexoncode.cryptit.lvl.SecureUtils;

import java.util.HashMap;
import java.util.Map;

public class SplashActivity extends AppCompatActivity {

    private LicenseChecker mChecker;

    public TextView madeInIndia;

    private static final String BASE64_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhldAdldxgORRWk+B9tLv7ITmKoAe3PHivthDZMsG7Aqrc+S9/3qF5qNB+/Hm/SpzmPQSdDz6NmRv65Rpqm45iFdWZU4TSBVL6MWam4TYQE9rgCzUHVhYjuPe1kAKV6kKP8jaSq3wT6UGznB2itNgeHPEqDmLDO6Y25oPnC4e8suXdVPcmmDbl4uo96pa2YSwHeZzff5HqAaNqxVf01PSJPodcaTNGOJcGiP8EGtHR29QlFIaYPM5aPj5yVLdJ7Tbw4DCRKLCc4bdCnkhLJ6/hOVGagrBhqBy9SYfQoIPxI8eb0Q6JxEXGesWbgeVj6W28e2YhBI6dIW7xvVcY12RRQIDAQAB";
    private static final byte[] SALT = new byte[]{95, 91, 13, 67, 29, 35, 42, 13, 54, 76, 19, 41, 86, 82, 72, 53, 61, 81, 39, 12};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        madeInIndia = findViewById(R.id.madeInIndia);
        madeInIndia.setText(Html.fromHtml("<font color=#ffffff>Made in India</font><font color=#40ACFF> © </font><font color=#ffffff>2020</font>"));

//        checkLicense();

        new Handler().postDelayed(() -> {

            startActivity(new Intent(this, OnBoardingActivity.class));
            finish();

        }, 1200);
    }

    public void checkLicense(){
        Context AppContext=getApplicationContext();
        String sPackageName=AppContext.getPackageName();
        LicenseCheckerCallback mLicenseCheckerCallback = new MyLicenseCheckerCallback2();
        mChecker = new LicenseChecker(this, new ServerManagedPolicy(AppContext, new AESObfuscator(SALT, sPackageName, getDeviceId())),BASE64_PUBLIC_KEY);
        mChecker.checkAccess(mLicenseCheckerCallback);
    }
    private class MyLicenseCheckerCallback2 implements LicenseCheckerCallback {
        public void allow(final int reason) {

            Log.d("responseLicense", "allow");

            final Map<String, String> params = new HashMap<>();

            params.put("deviceid", getDeviceId());
            params.put("responsedata", mChecker.getSignedData());
            params.put("signature", mChecker.getSignature());
            params.put("sha", SecureUtils.getAppSignature(SplashActivity.this));

            StringRequest setupRequest = new StringRequest(Request.Method.POST, Api.VERIFY_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(final String response) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("responseLicense", response);
                        }
                    });

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(final VolleyError error) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("responseLicense", error.toString());
                        }
                    });

                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    return params;
                }
            };

            Volley.newRequestQueue(SplashActivity.this).add(setupRequest);

        }
        public void dontAllow(final int reason) {

            Log.d("responseLicense", "dontAllow");

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

//                    result.setText("dontAllow: " + reason + "\n"); todo
                }
            });

            if (reason == Policy.RETRY) {
//                Log.d(TAG, "dontAllow: " + "dialog_retry");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        result.append("dontAllow: " + "dialog_retry"); todo
                    }
                });

            } else {
//                Log.d(TAG, "dontAllow: " + "dialog_got_to_market");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        result.append("dontAllow: " + "dialog_got_to_market"); todo
                    }
                });

            }
        }
        @Override
        public void applicationError(final int errorCode) {
//            Log.d(TAG, "applicationError: " + errorCode);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    result.setText("applicationError: " + errorCode); todo
                }
            });
        }
    }

    private String getDeviceId() {
        return Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mChecker != null) {
            mChecker.onDestroy();
        }
    }
}
