package com.team4.cryptme.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.team4.cryptme.GridSpacingItemDecoration;
import com.team4.cryptme.R;
import com.team4.cryptme.data.Donation;
import com.team4.cryptme.util.Publisher;
import com.team4.cryptme.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class DonationActivity extends AppCompatActivity implements BillingProcessor.IBillingHandler {

    private BillingProcessor bp;
    private boolean readyToPurchase = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getColor(R.color.colorPrimary));
        window.setNavigationBarColor(getColor(R.color.colorPrimary));

        bp = BillingProcessor.newBillingProcessor(this, Publisher.LICENSE_KEY, this);
        bp.initialize();

        List<Donation> donations = new ArrayList<>();
        donations.add(new Donation("donate_11", "\u20b911"));
        donations.add(new Donation("donate_21", "\u20b921"));
        donations.add(new Donation("donate_51", "\u20b951"));
        donations.add(new Donation("donate_101", "\u20b9101"));
        donations.add(new Donation("donate_201", "\u20b9201"));
        donations.add(new Donation("donate_501", "\u20b9501"));
        donations.add(new Donation("donate_1001", "\u20b91001"));
        donations.add(new Donation("donate_2001", "\u20b92001"));

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        int spacing = (int) Utils.pxFromDp(DonationActivity.this, 32);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, spacing, true));

        recyclerView.setAdapter(new DonationAdapter(donations));
    }

    @Override
    public void onBillingInitialized() {
        readyToPurchase = true;
    }

    @Override
    public void onProductPurchased(@NonNull String productId, TransactionDetails details) {
        bp.consumePurchase(productId);
        sayThanks();
    }

    @Override
    public void onBillingError(int errorCode, Throwable error) {
        Toast.makeText(this, "Purchased cancelled or failed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!bp.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onDestroy() {
        if (bp != null) {
            bp.release();
        }
        super.onDestroy();
    }

    private void sayThanks() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thank you ❤")
                .setMessage("Your contribution means a lot and will help us grow. Thank you for donating. You can also help us by telling your friends about this app and inviting them for a download.")
                .setPositiveButton("Invite friends", ((dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    invite();
                }))
                .show();

    }

    private void invite() {

        String message = "I am using Crypt-Me to secure my data from malicious apps with military-grade encryption algorithms. Download it today from the Google Play Store.\n\nhttp://play.google.com/store/apps/details?id=" + getPackageName();

        ShareCompat.IntentBuilder
                .from(this)
                .setText(message)
                .setType("text/plain")
                .setChooserTitle("Invite using")
                .startChooser();
    }

    class DonationAdapter extends RecyclerView.Adapter<DonationAdapter.ViewHolder> {

        private List<Donation> donations;

        public DonationAdapter(List<Donation> donations) {
            this.donations = donations;
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView amount;
            /*RadioButton radio;*/
            View view;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                amount = itemView.findViewById(R.id.amount);
                /*radio = itemView.findViewById(R.id.radio);*/
                view = itemView.findViewById(R.id.view);
            }
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_donate, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.amount.setText(donations.get(holder.getAdapterPosition()).getName());
            holder.view.setOnClickListener(v -> {

                if (readyToPurchase) {
                    bp.purchase(DonationActivity.this, donations.get(holder.getAdapterPosition()).getSkuId());
                } else {
                    Toast.makeText(DonationActivity.this, "Loading purchase data, please try again in a while", Toast.LENGTH_SHORT).show();
                }

            });
        }

        @Override
        public int getItemCount() {
            return donations.size();
        }
    }

}