package com.team4.cryptme.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.team4.cryptme.FilePicker.FilePicker;
import com.team4.cryptme.FilePicker.IconFilePicker;
import com.team4.cryptme.FilePicker.ListFilePicker;
import com.team4.cryptme.GlobalDocumentFileStateHolder;
import com.team4.cryptme.R;
import com.team4.cryptme.SettingsHelper;

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