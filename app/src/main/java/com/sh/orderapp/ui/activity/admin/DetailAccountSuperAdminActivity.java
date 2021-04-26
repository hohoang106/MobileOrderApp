package com.sh.orderapp.ui.activity.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.FirebaseDatabase;
import com.sh.orderapp.R;
import com.sh.orderapp.model.UserModel;
import com.sh.orderapp.utils.Const;

public class DetailAccountSuperAdminActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvName, tvPhone, tvAddress, tvStatus;
    private Button btnDelete, btnApprove;

    private UserModel userModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_admin_detail_account);
        initViews();
        initData();
    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            userModel = bundle.getParcelable(Const.ACCOUNT_SELECTED);
        }

        tvName.setText(userModel.getFullName());
        tvPhone.setText(userModel.getPhone());
        tvAddress.setText(userModel.getAddress());
        tvStatus.setText(userModel.getStatus());

    }

    private void initViews() {
        tvName = this.findViewById(R.id.tvFullNameAccount);
        tvPhone = this.findViewById(R.id.tvPhoneAccount);
        tvAddress = this.findViewById(R.id.tvAddressAccount);
        tvStatus = this.findViewById(R.id.tvStatusAccount);
        btnDelete = this.findViewById(R.id.btnDeleteAccount);
        btnApprove = this.findViewById(R.id.btnApproveAccount);

        btnDelete.setOnClickListener(this);
        btnApprove.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnDeleteAccount: {
                onClickDeleteAccount();
                break;
            }
            case R.id.btnApproveAccount: {
                onClickApproveAccount();
                break;
            }
        }
    }

    private void onClickDeleteAccount() {
        FirebaseDatabase.getInstance().getReference("CSO").child(Const.FIREBASE_URI.USERS_REF).child(userModel.getUsername()).removeValue();
        Toast.makeText(this, "Delete account successfully", Toast.LENGTH_SHORT).show();

        Intent mIntent = new Intent(DetailAccountSuperAdminActivity.this, SuperAdminManageActivity.class);
        startActivity(mIntent);
        finish();
    }

    private void onClickApproveAccount() {
        userModel.setStatus(Const.USER_STATUS.APPROVE);
        FirebaseDatabase.getInstance().getReference("CSO").child(Const.FIREBASE_URI.USERS_REF).child(userModel.getUsername()).setValue(userModel);
        Toast.makeText(this, "Approve account successfully", Toast.LENGTH_SHORT).show();

        Intent mIntent = new Intent(DetailAccountSuperAdminActivity.this, SuperAdminManageActivity.class);
        startActivity(mIntent);
        finish();
    }
}
