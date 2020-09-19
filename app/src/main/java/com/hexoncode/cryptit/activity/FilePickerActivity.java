package com.hexoncode.cryptit.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.hexoncode.cryptit.FilePicker.FilePicker;
import com.hexoncode.cryptit.FilePicker.IconFilePicker;
import com.hexoncode.cryptit.FilePicker.ListFilePicker;
import com.hexoncode.cryptit.GlobalDocumentFileStateHolder;
import com.hexoncode.cryptit.R;
import com.hexoncode.cryptit.SettingsHelper;

public class FilePickerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_picker);

        boolean isOutput = false;

        FilePicker filePicker = null;
        int filePickerType = SettingsHelper.getFilePickerType(this);
        if (filePickerType == SettingsHelper.FILE_ICON_VIEWER) {
            filePicker = new IconFilePicker();
        } else if (filePickerType == SettingsHelper.FILE_LIST_VIEWER) {
            filePicker = new ListFilePicker();
        }
        String title = isOutput ? getString(R.string.choose_output_file) : getString(R.string.choose_input_file);
        Bundle args = new Bundle();
        args.putBoolean(FilePicker.IS_OUTPUT_KEY, isOutput);
        GlobalDocumentFileStateHolder.setInitialFilePickerDirectory(null);
        args.putString(FilePicker.DEFAULT_OUTPUT_FILENAME_KEY, null);
        filePicker.setArguments(args);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, filePicker);
        fragmentTransaction.commit();

    }
}