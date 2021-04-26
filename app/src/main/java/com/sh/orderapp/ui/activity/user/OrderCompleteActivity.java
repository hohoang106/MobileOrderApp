package com.sh.orderapp.ui.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.sh.orderapp.R;

public class OrderCompleteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_complete);
    }

    public void backHome(View view) {
        Intent intent = new Intent(OrderCompleteActivity.this, UserMenuActivity.class);
        startActivity(intent);
        finish();
    }
}
