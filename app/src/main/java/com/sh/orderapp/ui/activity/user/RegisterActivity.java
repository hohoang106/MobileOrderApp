package com.sh.orderapp.ui.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    private Button btnRegister;
    private EditText edtUsername, edtPassword, edtFullName, edtPhoneNumber, edtEmail, edtAddress;
    private Spinner spnTypeUser;

    private String typeUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }

    private void initView() {
        edtUsername = this.findViewById(R.id.edtUsernameRegister);
        edtPassword = this.findViewById(R.id.edtPasswordRegister);
        edtFullName = this.findViewById(R.id.edtFullNameRegister);
        edtPhoneNumber = this.findViewById(R.id.edtPhoneNumberRegister);
        edtEmail = this.findViewById(R.id.edtEmailRegister);
        edtAddress = this.findViewById(R.id.edtAddressRegister);
        btnRegister = this.findViewById(R.id.btnRegister);
        spnTypeUser = findViewById(R.id.spnTypeUser);


        List<String> list = new ArrayList<>();
        list.add(Const.USER_TYPE.USER);
        list.add(Const.USER_TYPE.RESTAURANT_MANAGE);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnTypeUser.setAdapter(dataAdapter);
        typeUser = list.get(0);

        spnTypeUser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                typeUser = list.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnRegister.setOnClickListener(v -> {
            String username = edtUsername.getText().toString();
            String password = edtPassword.getText().toString();
            String fullName = edtFullName.getText().toString();
            String phoneNumber = edtPhoneNumber.getText().toString();
            String email = edtEmail.getText().toString();
            String address = edtAddress.getText().toString();


            if (username.isEmpty() || password.isEmpty() || fullName.isEmpty() || phoneNumber.isEmpty() || email.isEmpty() || address.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "Please fill all information", Toast.LENGTH_SHORT).show();
            } else {

                String status = Const.USER_STATUS.APPROVE;
                if (Const.USER_TYPE.RESTAURANT_MANAGE.equals(typeUser)) {
                    status = Const.USER_STATUS.WAIT;
                }

                final UserModel userCreated = UserModel.builder()
                        .username(username.trim().toLowerCase())
                        .permission(typeUser)
                        .password(password)
                        .fullName(fullName)
                        .phone(phoneNumber)
                        .address(address)
                        .email(email)
                        .status(status)
                        .build();
                final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("CSO").child(Const.FIREBASE_URI.USERS_REF);
                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.child(userCreated.getUsername()).exists()) {
                            Toast.makeText(RegisterActivity.this, "User is existed", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        mDatabase.child(userCreated.getUsername()).setValue(userCreated);
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        finish();
                        startActivity(intent);
                        Toast.makeText(RegisterActivity.this, "Register account successfully", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });
    }
}
