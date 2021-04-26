package com.sh.orderapp.ui.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.util.Strings;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sh.orderapp.R;
import com.sh.orderapp.model.UserModel;
import com.sh.orderapp.utils.Common;
import com.sh.orderapp.utils.Const;


public class ChangePasswordUserActivity extends AppCompatActivity {
    private EditText edOldPass, edNewPass, edRepeatPass;
    private ProgressBar progressBar;
    private UserModel clsUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_change_password);
        addControls();
    }

    public void addControls() {
        edOldPass = findViewById(R.id.a5_change_edOldPass);
        edNewPass = findViewById(R.id.a5_change_edNewPass);
        edRepeatPass = findViewById(R.id.a5_change_edRepeat);
        progressBar = findViewById(R.id.a2_progressBar);
    }

    public void onChangePass(View view) {
        try {
            progressBar.setVisibility(View.VISIBLE);
            final String oldPass = edOldPass.getText().toString();
            final String newPass = edNewPass.getText().toString();
            final String repeatPass = edRepeatPass.getText().toString();
            // Check valid
            if (Strings.isEmptyOrWhitespace(oldPass)) {
                edOldPass.setError(("Please enter your old password"));
                edOldPass.requestFocus();
                progressBar.setVisibility(View.GONE);
                return;
            } else if (Strings.isEmptyOrWhitespace(newPass)) {
                edNewPass.setError(("Please enter your new password"));
                edNewPass.requestFocus();
                progressBar.setVisibility(View.GONE);
                return;
            } else if (!newPass.equals(repeatPass)) {
                edRepeatPass.setError(("New password not match"));
                edRepeatPass.requestFocus();
                progressBar.setVisibility(View.GONE);
                return;
            }

            // Check vaild user registered
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference users = database.getReference("CSO").child(Const.FIREBASE_URI.USERS_REF).child(Common.userLogin);
            users.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        if (dataSnapshot.getValue() != null) {
                            clsUser = new UserModel();
                            clsUser = dataSnapshot.getValue(UserModel.class);
                            assert clsUser != null;
                            String oldPasswordDB = clsUser.getPassword();
                            if (oldPasswordDB.equals(oldPass)) {
                                DatabaseReference changePassword = database.getReference("CSO").child(Const.FIREBASE_URI.USERS_REF);
                                changePassword.child(Common.userLogin).child("password").setValue(newPass);
                                Toast.makeText(ChangePasswordUserActivity.this, "Change password successful!", Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);

                                Intent mIntent = new Intent(ChangePasswordUserActivity.this, UserMenuActivity.class);
                                startActivity(mIntent);
                                finish();
                            } else {
                                Toast.makeText(ChangePasswordUserActivity.this, "Old password is wrong. Please try again!", Toast.LENGTH_LONG).show();
                                edOldPass.requestFocus();
                                progressBar.setVisibility(View.GONE);
                            }
                        } else {
                            Toast.makeText(ChangePasswordUserActivity.this, "User not registered. Please contact admin", Toast.LENGTH_LONG).show();
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
