package com.oleg.hubal.creditcardedittext;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int[] pattern = new int[] {3, 1, 3, 2, 3};

        CreditCardEditText creditCardEditText = (CreditCardEditText) findViewById(R.id.ccet_credit_card);
        creditCardEditText.setTextPattern(pattern);
    }
}
