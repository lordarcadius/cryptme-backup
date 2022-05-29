package com.team4.cryptme.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;

public class Utils {

    public static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }

    /*private String convertToHex(byte[] data) {
        StringBuilder buf = new StringBuilder();
        for (byte b : data) {
            int halfbyte = (b >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                buf.append((0 <= halfbyte) && (halfbyte <= 9) ? (char) ('0' + halfbyte) : (char) ('a' + (halfbyte - 10)));
                halfbyte = b & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }

    private String sha1(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] textBytes = text.getBytes(StandardCharsets.UTF_8);
        md.update(textBytes, 0, textBytes.length);
        byte[] sha1hash = md.digest();
        return convertToHex(sha1hash);
    }*/

    /*public static void saveLicense(Context context) {
        try {
            String licenseText = BuildConfig.APPLICATION_ID + "," + getDeviceId(context);
            licenseText = sha1(licenseText);
            byte[] data = licenseText.getBytes(StandardCharsets.UTF_8);
            String base64 = Base64.encodeToString(data, Base64.DEFAULT);

            SharedPreferences preferences = context.getSharedPreferences("app", Context.MODE_PRIVATE);
            preferences.edit().putString("base", base64).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean verifyLocalLicense(Context context) {

        try {
            String licenseText = BuildConfig.APPLICATION_ID + "," + getDeviceId(context);
            licenseText = sha1(licenseText);
            byte[] data = licenseText.getBytes(StandardCharsets.UTF_8);
            String base64 = Base64.encodeToString(data, Base64.DEFAULT);

            SharedPreferences preferences = context.getSharedPreferences("app", Context.MODE_PRIVATE);
            String license = preferences.getString("base", null);
            if (license == null) return false;
            else return license.equals(base64);

        } catch (Exception e) {
            return false;
        }
    }*/
}
