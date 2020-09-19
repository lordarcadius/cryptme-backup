package com.hexoncode.cryptit.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ShareCompat;
import androidx.core.view.GravityCompat;
import androidx.documentfile.provider.DocumentFile;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.hexoncode.cryptit.BuildConfig;
import com.hexoncode.cryptit.CryptoService;
import com.hexoncode.cryptit.CryptoThread;
import com.hexoncode.cryptit.FilePicker.FilePicker;
import com.hexoncode.cryptit.FilePicker.IconFilePicker;
import com.hexoncode.cryptit.FilePicker.ListFilePicker;
import com.hexoncode.cryptit.GlobalDocumentFileStateHolder;
import com.hexoncode.cryptit.SettingsHelper;
import com.hexoncode.cryptit.fragment.DecryptFragment;
import com.hexoncode.cryptit.fragment.EncryptFragment;
import com.hexoncode.cryptit.fragment.ExploreFragment;
import com.hexoncode.cryptit.R;
import com.hexoncode.cryptit.util.LoadingDialog;
import com.hexoncode.cryptit.util.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, CryptoThread.ProgressDisplayer {

    private boolean isFragmentAdded = false;
    private DocumentFile inputFileParentDirectory = null;
    String inputFileName;
    private DocumentFile outputFileParentDirectory = null;
    String outputFileName;

    public TextView toolbarTitle;
    public Toolbar toolbar;
    public ImageView option;
    SharedPreferences prefs;
    public FrameLayout frameLayout;
    public BottomNavigationView bottomNavigationView;
    private DrawerLayout drawer;
    private boolean currentOperation;
    private LoadingDialog loadingDialog;
    private ViewPager viewPager;

    private EncryptFragment encryptFragment;
    private DecryptFragment decryptFragment;
    private ExploreFragment exploreFragment;

    private static final String PROGRESS_DISPLAYER_ID = "com.divyanshu.cryptit.MainActivityFragment.PROGRESS_DISPLAYER_ID";
    private static final String MAINACITIVITYFRAGMENT_ON_TOP_KEY = "com.divyanshu.cryptit.MainActivity.MAINACTIVITYFRAGMENT_ON_TOP_KEY";
    private static final String MAINACTIVITYFRAGMENT_TAG = "com.divyanshu.cryptit.MainActivity.MAINACTIVITYFRAGMENT_TAG";
    private static final String FILEPICKERFRAGMENT_TAG = "com.divyanshu.cryptit.MainActivity.FILEPICKERFRAGMENT_TAG";
    private static final String TITLE_KEY = "com.divyanshu.cryptit.MainActivity.TITLE_KEY";
    private static final String[] PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final int RC_PERMISSIONS = 8008;
    private static final String SUPPORT_EMAIL = "android-support@hexoncode.com";

    @Override
    public void update(boolean operationType, int progress, int completedMessageStringId, int minutesToCompletion, int secondsToCompletion) {

        final Context context = getBaseContext();
        if (context != null) {
            new Handler(context.getMainLooper()).post(() -> {

                if (progress != 100) {

                    if (!HomeActivity.this.isFinishing()) {

                        if (!loadingDialog.isShowing()) {
                            loadingDialog.showLoading();
                        }

                    }
                } else {
                    if (!HomeActivity.this.isFinishing()) {
                        loadingDialog.hideLoading();
                        if (exploreFragment != null) {
                            exploreFragment.loadData();
                        }
                    }
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getColor(R.color.colorPrimaryDark));
        window.setNavigationBarColor(getColor(R.color.colorPrimaryDark));

        setContentView(R.layout.activity_home);
        prefs = getApplicationContext().getSharedPreferences("APP_PREFS", MODE_PRIVATE);

        //to prevent direct activity launch
        String deviceId = getIntent().getStringExtra("deviceId");
        if (deviceId == null || !deviceId.equals(Utils.getDeviceId(this))) {
            finish();
            return;
        }

        initView();

        CryptoThread.registerForProgressUpdate(PROGRESS_DISPLAYER_ID, this);

        //Check if there is an operation in progress. If there is, get an update show the progress bar and cancel button immediately, rather than waiting for CryptoThread to push an update.
        if (CryptoThread.operationInProgress) {
            update(CryptoThread.getCurrentOperationType(), CryptoThread.getProgressUpdate(), CryptoThread.getCompletedMessageStringId(), -1, -1);
        }

        if (!permissionsGranted()) {
            requestPermissions(PERMISSIONS, RC_PERMISSIONS);
        }

    }

    private boolean permissionsGranted() {
        return checkSelfPermission(PERMISSIONS[0]) == PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(PERMISSIONS[1]) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == RC_PERMISSIONS) {

            if (!permissionsGranted()) {

                Toast.makeText(this, "Permissions denied", Toast.LENGTH_SHORT).show();

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("We need storage permissions to get the app working. Please grant using the permission dialog or manually from settings.")
                        .setPositiveButton("Grant", ((dialogInterface, i) -> {
                            dialogInterface.dismiss();
                            requestPermissions(PERMISSIONS, RC_PERMISSIONS);
                        }))
                        .setCancelable(false)
                        .show();

            } else exploreFragment.loadData();

        }

    }

    public void initView() {
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigation = findViewById(R.id.nav_view);
        navigation.setNavigationItemSelectedListener(this);
        toolbar = findViewById(R.id.toolbar);
        toolbarTitle = findViewById(R.id.toolbarTitle);
        option = findViewById(R.id.option);
        frameLayout = findViewById(R.id.frameLayout);
        bottomNavigationView = findViewById(R.id.bottomNavView);
        setSupportActionBar(toolbar);

        List<Fragment> fragments = new ArrayList<>();

        encryptFragment = new EncryptFragment();
        decryptFragment = new DecryptFragment();
        exploreFragment = new ExploreFragment();

        fragments.add(encryptFragment);
        fragments.add(decryptFragment);
        fragments.add(exploreFragment);

        viewPager = findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(fragments.size());
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, fragments));

        toolbarTitle.setText("Encrypt");

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                switch (position) {

                    case 0:
                        bottomNavigationView.setSelectedItemId(R.id.encrypt_view);
                        break;
                    case 1:
                        bottomNavigationView.setSelectedItemId(R.id.decrypt_view);
                        break;
                    case 2:
                        bottomNavigationView.setSelectedItemId(R.id.explorer_view);
                        break;

                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (isFragmentAdded) {
                    onBackPressed();
                }

                switch (item.getItemId()) {
                    case R.id.encrypt_view:
                        viewPager.setCurrentItem(0);
                        toolbarTitle.setText("Encrypt");

                        break;
                    case R.id.decrypt_view:
                        viewPager.setCurrentItem(1);
                        toolbarTitle.setText("Decrypt");
                        break;
                    case R.id.explorer_view:
                        viewPager.setCurrentItem(2);
                        toolbarTitle.setText("Explorer");
                        break;
                }
                return true;
            }

            ;
        });

        findViewById(R.id.option).setOnClickListener(v -> drawer.openDrawer(GravityCompat.END));
        loadingDialog = new LoadingDialog(this);
    }

    public void pickFile(boolean isOutput, DocumentFile initialFolder, String defaultOutputFilename, boolean currentOperation) {
        this.currentOperation = currentOperation;
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
        GlobalDocumentFileStateHolder.setInitialFilePickerDirectory(initialFolder);
        args.putString(FilePicker.DEFAULT_OUTPUT_FILENAME_KEY, defaultOutputFilename);
        filePicker.setArguments(args);
        displaySecondaryFragmentScreen(filePicker, title, FILEPICKERFRAGMENT_TAG);
    }

    public void displaySecondaryFragmentScreen(Fragment fragment, String title, String tag) {
        isFragmentAdded = true;
        findViewById(R.id.option).setVisibility(View.GONE);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        attachFragment(fragment, true, tag);
        if (title != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    private void attachFragment(Fragment fragment, boolean addToBackStack, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(null);
//            fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, android.R.anim.fade_out, android.R.anim.slide_in_left, android.R.anim.fade_out);
        } else {
//            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.slide_in_left, android.R.anim.fade_out);
        }
        fragmentTransaction.replace(R.id.fragment_holder, fragment, tag);
        fragmentTransaction.commit();
    }

    public void filePicked(DocumentFile fileParentDirectory, String filename, boolean isOutput) {
        setFile(fileParentDirectory, filename, isOutput);
        if (currentOperation == CryptoThread.OPERATION_TYPE_ENCRYPTION) {
            encryptFragment.setFileName(filename);
        } else if (currentOperation == CryptoThread.OPERATION_TYPE_DECRYPTION) {
            decryptFragment.setFileName(filename);
        }

//        getMainActivityFragment().setFile(fileParentDirectory, filename, isOutput);
    }

    public void setFile(DocumentFile file, String filename, boolean isOutput) {

        if (isOutput) {
            outputFileParentDirectory = file;
            outputFileName = filename;
        } else {

            inputFileParentDirectory = file;
            inputFileName = filename;
        }
       /* if (context != null) {
            updateFileUI(isOutput);
        }*/
    }

    public void actionButtonPressed(String password, boolean operation, boolean deleteInputFile) {
//        if (isValidElsePrintErrors()) {
        //Can't use getContext() or getActivity(). See comment on this.onAttach(Context)
        Intent intent = new Intent(HomeActivity.this, CryptoService.class);

        File encryptedFilesDirectory = new File(Environment.getExternalStorageDirectory() + File.separator + "Crypt-It" + File.separator + "Encrypted");
        File decryptedFilesDirectory = new File(Environment.getExternalStorageDirectory() + File.separator + "Crypt-It" + File.separator + "Decrypted");

        if (!encryptedFilesDirectory.exists()) encryptedFilesDirectory.mkdirs();
        if (!decryptedFilesDirectory.exists()) decryptedFilesDirectory.mkdirs();

        DocumentFile outputFileParentDirectory;
        String outputFileName;

        if (operation == CryptoThread.OPERATION_TYPE_ENCRYPTION) {
            outputFileParentDirectory = DocumentFile.fromFile(encryptedFilesDirectory);
            outputFileName = inputFileName + ".aes";
        } else {
            outputFileParentDirectory = DocumentFile.fromFile(decryptedFilesDirectory);
            outputFileName = inputFileName.replace(".aes", "");
        }

        intent.putExtra(CryptoService.INPUT_FILE_NAME_EXTRA_KEY, inputFileName);
        intent.putExtra(CryptoService.OUTPUT_FILE_NAME_EXTRA_KEY, outputFileName);
        GlobalDocumentFileStateHolder.setInputFileParentDirectory(inputFileParentDirectory);
        GlobalDocumentFileStateHolder.setOutputFileParentDirectory(outputFileParentDirectory);
        intent.putExtra(CryptoService.INPUT_FILENAME_KEY, inputFileName);
        intent.putExtra(CryptoService.OUTPUT_FILENAME_KEY, outputFileName);
        intent.putExtra(CryptoService.PASSWORD, password);
        intent.putExtra(CryptoService.VERSION_EXTRA_KEY, SettingsHelper.getAESCryptVersion(getBaseContext()));
        intent.putExtra(CryptoService.OPERATION_TYPE_EXTRA_KEY, operation);
        intent.putExtra(CryptoService.DELETE_INPUT_FILE_KEY, deleteInputFile);

        startService(intent);

        //unset files
        outputFileParentDirectory = null;
        outputFileName = null;
        inputFileParentDirectory = null;
        inputFileName = null;

            /*if(IS_USING_FP){
                Toast.makeText(getActivity(), "Using fingerprint over password!", Toast.LENGTH_SHORT).show();
                IS_USING_FP = false;
                SharedPreferences prefs = getActivity().getSharedPreferences("try", 0);
                if(prefs.getString("filepass", null) == null){
                    String newpass = random();
                    passwordEditText.setText(newpass);
                    prefs.edit().putString("filepass", newpass).apply();
                    MainActivityFragment.password = newpass.toCharArray();
                } else {
                    String thepass = prefs.getString("filepass", "try");
                    passwordEditText.setText(thepass);
                    MainActivityFragment.password = thepass.toCharArray();
                }
            } else {
                MainActivityFragment.password = passwordEditText.getText().toString().toCharArray();
            }

            if(!passwordEditText.getText().toString().isEmpty()){
                context.startService(intent);
            } else {
                Toast.makeText(getActivity(), "Neither fingerprint nor password given.", Toast.LENGTH_SHORT).show();
            }*/
//        }
    }

    public void removeFragment() {

        try {

            findViewById(R.id.option).setVisibility(View.VISIBLE);
            getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentByTag(FILEPICKERFRAGMENT_TAG));
            isFragmentAdded = false;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class ViewPagerAdapter extends FragmentStatePagerAdapter {

        private List<Fragment> fragments;

        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior, List<Fragment> fragments) {
            super(fm, behavior);
            this.fragments = fragments;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        drawer.closeDrawer(GravityCompat.END);

        switch (item.getItemId()) {

            /*case R.id.settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;*/

            case R.id.about:
                Intent aboutIntent = new Intent(this, AboutUs.class);
                startActivity(aboutIntent);
                break;

            case R.id.contact:
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", SUPPORT_EMAIL, null));
                intent.putExtra(Intent.EXTRA_SUBJECT, "Regarding " + getString(R.string.app_name) + " v" + BuildConfig.VERSION_NAME);
                String content = "";
                intent.putExtra(Intent.EXTRA_TEXT, content);
                startActivity(Intent.createChooser(intent, "Send via"));
                break;

            case R.id.rate:
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
                break;

            case R.id.invite:
                invite();
                break;

            case R.id.faq:
                Intent faqIntent = new Intent(this, FaqActivity.class);
                startActivity(faqIntent);
                break;


        }

        return false;
    }

    private void invite() {

        String message = "I am using Crypt-It to secure my data from malicious apps with military-grade encryption algorithms. Download it today from the Google Play Store.\n\nhttp://play.google.com/store/apps/details?id=" + getPackageName();

        ShareCompat.IntentBuilder
                .from(this)
                .setText(message)
                .setType("text/plain")
                .setChooserTitle("Invite using")
                .startChooser();
    }

    @Override
    public void onBackPressed() {
        if (isFragmentAdded) {
            removeFragment();
            super.onBackPressed();
        } else if (viewPager != null && viewPager.getCurrentItem() != 0) {
            viewPager.setCurrentItem(0);
        } else super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}