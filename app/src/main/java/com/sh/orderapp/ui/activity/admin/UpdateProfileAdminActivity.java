package com.sh.orderapp.ui.activity.admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sh.orderapp.R;
import com.sh.orderapp.model.UserModel;
import com.sh.orderapp.utils.Const;
import com.sh.orderapp.utils.MySharedPreferences;

public class UpdateProfileAdminActivity extends AppCompatActivity {

    private EditText edtUsername, edtEmail, edtFullName, edtAddress, edtPhoneNumber;
    private Button btnUpdate;

    private UserModel userModel;
    private MySharedPreferences preferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile_user);
        initData();
        initView();
    }

    private void initData() {
        preferences = new MySharedPreferences(this);
        userModel = preferences.getUser(Const.USER_LOGIN);
    }

    private void initView() {
        edtUsername = this.findViewById(R.id.edtUserNameUpdateProfile);
        edtEmail = this.findViewById(R.id.edtEmailUpdateProfile);
        edtFullName = this.findViewById(R.id.edtFullNameUpdateProfile);
        edtAddress = this.findViewById(R.id.edtAddressUpdateProfile);
        edtPhoneNumber = this.findViewById(R.id.edtPhoneUpdateProfile);
        btnUpdate = this.findViewById(R.id.btnUpdateProfileUser);

        edtUsername.setText(userModel.getUsername());
        edtEmail.setText(userModel.getEmail());
        edtFullName.setText(userModel.getFullName());
        edtAddress.setText(userModel.getAddress());
        edtPhoneNumber.setText(userModel.getPhone());


        btnUpdate.setOnClickListener(v -> {

            String email = edtEmail.getText().toString();
            String fullName = edtFullName.getText().toString();
            String address = edtAddress.getText().toString();
            String phoneNumber = edtPhoneNumber.getText().toString();

            if (email.isEmpty() || fullName.isEmpty() || address.isEmpty() || phoneNumber.isEmpty()) {
                Toast.makeText(this, "Please fill all data", Toast.LENGTH_SHORT).show();
            } else {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference users = database.getReference("CSO").child(Const.FIREBASE_URI.USERS_REF).child(userModel.getUsername());
                users.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            userModel = dataSnapshot.getValue(UserModel.class);
                            assert userModel != null;
                            userModel.setEmail(email);
                            userModel.setFullName(fullName);
                            userModel.setAddress(address);
                            userModel.setPhone(phoneNumber);

                            DatabaseReference changePassword = database.getReference("CSO").child(Const.FIREBASE_URI.USERS_REF);
                            changePassword.child(userModel.getUsername()).setValue(userModel);
                            preferences.putUser(Const.USER_LOGIN, userModel);
                            Toast.makeText(UpdateProfileAdminActivity.this, "Update profile restaurant successful!", Toast.LENGTH_LONG).show();

                            Intent mIntent = new Intent(UpdateProfileAdminActivity.this, MenuAdminActivity.class);
                            startActivity(mIntent);
                            finish();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(UpdateProfileAdminActivity.this, "Error, please call admin", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
