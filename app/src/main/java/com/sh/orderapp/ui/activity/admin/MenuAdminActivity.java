package com.sh.orderapp.ui.activity.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.sh.orderapp.R;
import com.sh.orderapp.ui.activity.user.LoginActivity;
import com.sh.orderapp.ui.activity.user.UpdateProfileUserActivity;
import com.sh.orderapp.ui.activity.user.UserInfoActivity;
import com.sh.orderapp.utils.MySharedPreferences;

public class MenuAdminActivity extends AppCompatActivity {

    MySharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_menu);
        preferences = new MySharedPreferences(this);
    }

    public void onFoodManagement(View view) {
        Intent intent = new Intent(MenuAdminActivity.this, AdminFoodManagementActivity.class);
        startActivity(intent);
    }

    public void onBillManagement(View view) {
        Intent intent = new Intent(MenuAdminActivity.this, AdminBillManagementActivity.class);
        startActivity(intent);
    }

    public void onReportManagement(View view) {
        Intent intent = new Intent(MenuAdminActivity.this, AdminReportActivity.class);
        startActivity(intent);
    }

    public void onLogoutAdmin(View view) {
        preferences.clearAllData();

        Intent intent = new Intent(MenuAdminActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void onNewsManagement(View view) {
        Intent intent = new Intent(MenuAdminActivity.this, AdminNewsManagementActivity.class);
        startActivity(intent);
    }

    public void onProfileAdminManagement(View view) {
        Intent intent = new Intent(MenuAdminActivity.this, UserInfoActivity.class);
        startActivity(intent);
    }

}