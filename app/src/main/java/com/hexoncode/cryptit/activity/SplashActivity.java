package com.hexoncode.cryptit.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import com.hexoncode.cryptit.lvl.Api;
import com.hexoncode.cryptit.R;
import com.hexoncode.cryptit.lvl.SecureUtils;
import com.hexoncode.cryptit.util.Publisher;
import com.hexoncode.cryptit.util.Utils;
import com.pddstudio.preferences.encrypted.EncryptedPreferences;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SplashActivity extends AppCompatActivity {

    private LicenseChecker mChecker;

    public TextView madeInIndia;

    private static final byte[] SALT = new byte[]{95, 91, 13, 67, 29, 35, 42, 13, 54, 76, 19, 41, 86, 82, 72, 53, 61, 81, 39, 12};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getColor(R.color.colorPrimaryDark));
        window.setNavigationBarColor(getColor(R.color.colorPrimaryDark));

        setContentView(R.layout.activity_splash);
        madeInIndia = findViewById(R.id.madeInIndia);
        madeInIndia.setText("Made with ❤ in India by Hexoncode");

        init();
        new Handler().postDelayed(() -> runOnUiThread(() -> findViewById(R.id.progressBar).setVisibility(View.VISIBLE)), 1500);
    }

    private void init() {

        EncryptedPreferences encryptedPreferences = new EncryptedPreferences.Builder(SplashActivity.this).withEncryptionPassword(Utils.getDeviceId(SplashActivity.this)).build();
        String deviceId = encryptedPreferences.getString("device", null);

        if (deviceId == null || !deviceId.equals(Utils.getDeviceId(SplashActivity.this))) {

            if (Utils.isNetworkAvailable(this)) {
                checkLicense();
            } else {
                showNoInternetDialog();
            }

        } else {
            launchHome();
        }

    }

    private void showNoInternetDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Connection error")
                .setMessage("We need internet connection to verify the integrity of application for your security. Please check your internet connection or try again later.")
                .setPositiveButton("Retry", ((dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    init();
                }))
                .setNegativeButton("Exit", ((dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    finish();
                }))
                .setCancelable(false)
                .show();

    }

    public void checkLicense() {

            Context AppContext = getApplicationContext();
            String sPackageName = AppContext.getPackageName();
            LicenseCheckerCallback mLicenseCheckerCallback = new MyLicenseCheckerCallback2();
            mChecker = new LicenseChecker(this, new ServerManagedPolicy(AppContext, new AESObfuscator(SALT, sPackageName, Utils.getDeviceId(this))), Publisher.LICENSE_KEY);
            mChecker.checkAccess(mLicenseCheckerCallback);
    }

    private class MyLicenseCheckerCallback2 implements LicenseCheckerCallback {

        public void allow(final int reason) {

            final Map<String, String> params = new HashMap<>();

            params.put("deviceid", Utils.getDeviceId(SplashActivity.this));
            params.put("responsedata", mChecker.getSignedData());
            params.put("signature", mChecker.getSignature());
            params.put("sha", SecureUtils.getAppSignature(SplashActivity.this));

            StringRequest setupRequest = new StringRequest(Request.Method.POST, Api.VERIFY_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(final String response) {

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.has("deviceid")) {
                            String deviceId = jsonObject.getString("deviceid");
                            if (deviceId.equals(Utils.getDeviceId(SplashActivity.this))) {

                                EncryptedPreferences encryptedPreferences = new EncryptedPreferences.Builder(SplashActivity.this).withEncryptionPassword(deviceId).build();
                                encryptedPreferences.edit().putString("device", deviceId).apply();

                                launchHome();
                            } else {
                                showPreventDialog("0x2");
                            }
                        } else if (jsonObject.has("error")) {
                            showPreventDialog("0x3");
                        } else {
                            showPreventDialog("0x4");
                        }
                    } catch (Exception e) {
                        showPreventDialog("JS");
                    }

                }
            }, error -> runOnUiThread(() -> showNoInternetDialog())) {
                @Override
                protected Map<String, String> getParams() {
                    return params;
                }
            };

            Volley.newRequestQueue(SplashActivity.this).add(setupRequest);

        }

        public void dontAllow(final int reason) {
            runOnUiThread(() -> showPreventDialog(String.valueOf(reason)));
        }

        @Override
        public void applicationError(final int errorCode) {

            runOnUiThread(() -> showPreventDialog(String.valueOf(errorCode)));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mChecker != null) {
            mChecker.onDestroy();
        }
    }

    private void launchHome() {

        SharedPreferences preferences = getSharedPreferences("app", Context.MODE_PRIVATE);
        boolean onboarded = preferences.getBoolean("onboarding", false);
        if (!onboarded) {
            startActivity(new Intent(this, OnboardingActivity.class)
                    .putExtra("deviceId", Utils.getDeviceId(this)));
        } else {
            startActivity(new Intent(this, HomeActivity.class)
                    .putExtra("deviceId", Utils.getDeviceId(this)));
        }
        finish();
    }

    private void showPreventDialog(String reason) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error " + reason)
                .setMessage("We are unable to verify the legitimacy of this copy of application. Please download the application from the Google Play Store.")
                .setPositiveButton("Download", ((dialogInterface, i) -> {

                    Uri uri = Uri.parse("market://details?id=" + getPackageName());
                    Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                    goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                            Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                            Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    try {
                        startActivity(goToMarket);
                    } catch (ActivityNotFoundException e) {
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                    }

                }))
                .setCancelable(false)
                .show();
    }

}
