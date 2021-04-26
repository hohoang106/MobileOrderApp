package com.sh.orderapp.ui.activity.admin;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sh.orderapp.R;
import com.sh.orderapp.adapter.AccountAdapter;
import com.sh.orderapp.model.UserModel;
import com.sh.orderapp.utils.Const;

import java.util.ArrayList;
import java.util.List;

public class SuperAdminManageActivity extends AppCompatActivity {

    private List<UserModel> listUsers;
    private AccountAdapter accountAdapter;
    private RecyclerView rcvAccount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_admin_manage);
        initViews();
        buildAllAccount();
    }

    private void initViews() {
        rcvAccount = this.findViewById(R.id.rcvAccount);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rcvAccount.setLayoutManager(layoutManager);
    }

    private void loadNews() {
        rcvAccount.setAdapter(null);
        accountAdapter = new AccountAdapter(this, listUsers, userModel -> {
            Bundle bundle = new Bundle();
            bundle.putParcelable(Const.ACCOUNT_SELECTED, userModel);
            Intent intent = new Intent(SuperAdminManageActivity.this, DetailAccountSuperAdminActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        });
        rcvAccount.setAdapter(accountAdapter);
    }

    private void buildAllAccount() {
        listUsers = new ArrayList<>();
        final DatabaseReference drinks = FirebaseDatabase.getInstance().getReference("CSO").child(Const.FIREBASE_URI.USERS_REF);
        drinks.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    listUsers = new ArrayList<>();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        UserModel model = ds.getValue(UserModel.class);
                        if (model.getPermission().equals(Const.USER_TYPE.RESTAURANT_MANAGE)) {
                            listUsers.add(model);
                        }
                    }
                    loadNews();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
