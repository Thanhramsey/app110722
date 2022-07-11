package com.vnpt.staffhddt;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.vnpt.common.Common;
import com.vnpt.utils.StoreSharePreferences;

public class ChoosePrinterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_printer);
        boolean check = StoreSharePreferences.getInstance(this).loadBooleandSavedPreferences(Common.KEY_CONFIG_PRINTER);
        if (check) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    public void chooseEr58(View view) {
        Intent intent = new Intent(ChoosePrinterActivity.this, LoginActivity.class);
        StoreSharePreferences.getInstance(this).saveIntPreferences(Common.KEY_PRINTER, Common.PRINT_ER_58);
        StoreSharePreferences.getInstance(this).saveBooleanPreferences(Common.KEY_CONFIG_PRINTER, true);
        startActivity(intent);
        finish();
    }

    public void choosePos58(View view) {
        Intent intent = new Intent(ChoosePrinterActivity.this, LoginActivity.class);
        StoreSharePreferences.getInstance(this).saveIntPreferences(Common.KEY_PRINTER, Common.PRINT_POS_58);
        StoreSharePreferences.getInstance(this).saveBooleanPreferences(Common.KEY_CONFIG_PRINTER, true);
        startActivity(intent);
        finish();
    }
}