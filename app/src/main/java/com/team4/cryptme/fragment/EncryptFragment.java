package com.team4.cryptme.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.team4.cryptme.CryptoThread;
import com.team4.cryptme.R;
import com.team4.cryptme.activity.HomeActivity;

import co.infinum.goldfinger.Goldfinger;
import me.aflak.libraries.callback.FingerprintDialogCallback;
import me.aflak.libraries.dialog.FingerprintDialog;

public class EncryptFragment extends Fragment {

    private Context context;
    private Goldfinger goldfinger;

    private TextView fileName;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private CheckBox deleteInputFileCheckbox;

    public EncryptFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_encrypt, container, false);

        ImageView upload_file_bg = root.findViewById(R.id.upload_file_bg);
        fileName = root.findViewById(R.id.fileName);
        passwordEditText = root.findViewById(R.id.enterPassword);
        confirmPasswordEditText = root.findViewById(R.id.confirmPassword);
        deleteInputFileCheckbox = root.findViewById(R.id.deleteInputFileCheckbox);
        Button encryptWithDeviceIdButton = root.findViewById(R.id.encryptWithDeviceIdButton);

        Glide.with(context).load(R.drawable.upload_file_bg).into(upload_file_bg);

        if (goldfinger.hasFingerprintHardware() && goldfinger.hasEnrolledFingerprint()) {
            encryptWithDeviceIdButton.setText("Encrypt with fingerprint");
        }

        root.findViewById(R.id.uploadAFile).setOnClickListener(v -> {

            DocumentFile initialFolder = DocumentFile.fromFile(Environment.getExternalStorageDirectory());
            ((HomeActivity) context).pickFile(false, initialFolder, null, CryptoThread.OPERATION_TYPE_ENCRYPTION);

        });

        root.findViewById(R.id.encryptButton).setOnClickListener(v -> {

            String password = passwordEditText.getText().toString().trim();
            String confirmPassword = confirmPasswordEditText.getText().toString().trim();

            if (fileName.getText().toString().equals("Choose a file")) {
                Toast.makeText(context, "No file selected", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(password)) {
                passwordEditText.setError("This field is required");
            } else if (TextUtils.isEmpty(confirmPassword)) {
                confirmPasswordEditText.setError("This field is required");
            } else if (!password.equals(confirmPassword)) {
                confirmPasswordEditText.setError("Passwords do not match");
            } else {

                ((HomeActivity) context).actionButtonPressed(
                        password,
                        CryptoThread.OPERATION_TYPE_ENCRYPTION,
                        deleteInputFileCheckbox.isChecked()
                );

                Toast.makeText(context, "Encryption started", Toast.LENGTH_SHORT).show();
                reset();
            }
        });

        encryptWithDeviceIdButton.setOnClickListener(v -> {

            if (fileName.getText().toString().equals("Choose a file")) {
                Toast.makeText(context, "No file selected", Toast.LENGTH_SHORT).show();
            } else {

                if (goldfinger.hasFingerprintHardware() && goldfinger.hasEnrolledFingerprint()) {

                    FingerprintDialog.initialize(getActivity())
                            .title("Validate fingerprint")
                            .message("Place your finger on the fingerprint sensor")
                            .callback(new FingerprintDialogCallback() {
                                @Override
                                public void onAuthenticationSucceeded() {
                                    encryptWithDeviceID();
                                }

                                @Override
                                public void onAuthenticationCancel() {
                                    Toast.makeText(context, "Fingerprint authentication failed", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .show();

                } else {
                    encryptWithDeviceID();
                }

            }

        });

        return root;
    }

    private void encryptWithDeviceID() {

        String password = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

        ((HomeActivity) context).actionButtonPressed(
                password,
                CryptoThread.OPERATION_TYPE_ENCRYPTION,
                deleteInputFileCheckbox.isChecked()
        );

        Toast.makeText(context, "Encryption started", Toast.LENGTH_SHORT).show();
        reset();
    }

    private void reset() {
        fileName.setText("Choose a file");
        passwordEditText.setText("");
        confirmPasswordEditText.setText("");
    }

    public void setFileName(String fileName) {
        if (this.fileName != null) {
            this.fileName.setText(fileName);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        this.goldfinger = new Goldfinger.Builder(context).build();
    }
}