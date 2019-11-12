package com.loadnote;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class CashTotal extends AppCompatActivity {

    private TextView totalPay;
    private Button cashIn;
    private final static String KEY = "com.loadnote.COLLECTED";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_total);

        totalPay = findViewById(R.id.total_cash);
        cashIn = findViewById(R.id.cash_in);

        SharedPreferences payment = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor paymentEditor = payment.edit();

        paymentEditor.apply();

        String total = payment.getString(KEY, ""+0);
        totalPay.setText(total);

        cashIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SweetAlertDialog alertDialog = new SweetAlertDialog(CashTotal.this, SweetAlertDialog.WARNING_TYPE);
                alertDialog.setCanceledOnTouchOutside(false);


                alertDialog.setTitleText("Did you just cash-in?")
                        .setConfirmText("Yes")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                paymentEditor.putString(KEY, "0");
                                totalPay.setText("0");
                                paymentEditor.apply();

                                Toast.makeText(CashTotal.this, "Cashed in", Toast.LENGTH_SHORT).show();
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .setCancelButton("No", new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                Toast.makeText(CashTotal.this, "Cancelled", Toast.LENGTH_SHORT).show();
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .show();
            }
        });
    }
}
