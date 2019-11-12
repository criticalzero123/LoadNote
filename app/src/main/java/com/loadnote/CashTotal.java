package com.loadnote;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CashTotal extends AppCompatActivity {

    private TextView totalPay;
    private final static String KEY = "com.loadnote.COLLECTED";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_total);

        totalPay = findViewById(R.id.total_cash);

        SharedPreferences payment = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor paymentEditor = payment.edit();

        paymentEditor.apply();

        String total = payment.getString(KEY, ""+0);
        totalPay.setText(total);
    }
}
