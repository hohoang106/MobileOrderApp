package com.sh.orderapp.ui.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sh.orderapp.R;
import com.sh.orderapp.model.UserModel;
import com.sh.orderapp.ui.activity.admin.MenuAdminActivity;
import com.sh.orderapp.ui.activity.admin.SuperAdminManageActivity;
import com.sh.orderapp.utils.Common;
import com.sh.orderapp.utils.Const;
import com.sh.orderapp.utils.MySharedPreferences;
import com.sh.orderapp.utils.StringFormatUtils;

public class LoginActivity extends AppCompatActivity {
    //Declare
    private EditText edUser, edPass;
    private ProgressBar progressBar;
    private TextView tvRegisterAccount;
    private UserModel clsUser;
    private MySharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        preferences = new MySharedPreferences(this);
        addControls();
    }

    public void addControls() {
        //Mapping
        edUser = findViewById(R.id.a1_login_edUserName);
        edPass = findViewById(R.id.a1_login_edPass);
        progressBar = findViewById(R.id.a2_progressBar);
        tvRegisterAccount = this.findViewById(R.id.tvRegisterAccount);
    }

    public void onRegister(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    public void onLogin(View view) {
        try {
            progressBar.setVisibility(View.VISIBLE);
            final String userName = edUser.getText().toString().trim();
            final String passWord = edPass.getText().toString().trim();
            // Check valid
            if (StringFormatUtils.isNullOrEmpty(userName)) {
                edUser.setError(("Please enter your username"));
                edUser.requestFocus();
                progressBar.setVisibility(View.GONE);
                return;
            } else if (StringFormatUtils.isNullOrEmpty(passWord)) {
                edPass.setError(("Please enter your password"));
                edPass.requestFocus();
                progressBar.setVisibility(View.GONE);
                return;
            }
            //connect to Firebase
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference users = database.getReference("CSO").child(Const.FIREBASE_URI.USERS_REF).child(userName);
            users.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        if (dataSnapshot.getValue() != null) {
                            clsUser = new UserModel();
                            clsUser = dataSnapshot.getValue(UserModel.class);
                            assert clsUser != null;
                            String pass = clsUser.getPassword();
                            if (pass.equals(passWord)) {
                                Common.userLogin = userName;
                                preferences.putUser(Const.USER_LOGIN, clsUser);

                                Intent intent;
                                if (Const.USER_STATUS.APPROVE.equals(clsUser.getStatus())) {
                                    if (clsUser.getPermission().equals(Const.USER_TYPE.RESTAURANT_MANAGE)) {
                                        intent = new Intent(LoginActivity.this, MenuAdminActivity.class);
                                    } else if (clsUser.getPermission().equals(Const.USER_TYPE.USER)) {
                                        intent = new Intent(LoginActivity.this, ListRestaurantActivity.class);
                                    } else {
                                        intent = new Intent(LoginActivity.this, SuperAdminManageActivity.class);
                                    }
                                    startActivity(intent);
                                    progressBar.setVisibility(View.GONE);
                                    finish();
                                } else {
                                    Toast.makeText(LoginActivity.this, "Please contact admin to active account!!!", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                }
                            } else {
                                Toast.makeText(LoginActivity.this, "Wrong password. Please try again!", Toast.LENGTH_LONG).show();
                                edPass.requestFocus();
                                progressBar.setVisibility(View.GONE);
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "User not registered. Please contact admin", Toast.LENGTH_LONG).show();
                            edUser.requestFocus();
                            progressBar.setVisibility(View.GONE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
