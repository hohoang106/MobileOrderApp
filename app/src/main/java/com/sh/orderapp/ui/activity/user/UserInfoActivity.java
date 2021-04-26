package com.sh.orderapp.ui.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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
import com.sh.orderapp.ui.activity.admin.UpdateProfileAdminActivity;
import com.sh.orderapp.utils.Const;
import com.sh.orderapp.utils.MySharedPreferences;

import static com.sh.orderapp.utils.Common.userLogin;


public class UserInfoActivity extends AppCompatActivity {

    public UserModel clsUser;
    private TextView fullName, fullname2, gender, email, address, username, accountType, changepass, tvUpdateProfile, editInfo;
    private ImageView imgUser;

    private MySharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_infor);
        mainLoad();
        preferences = new MySharedPreferences(this);
    }

    public void addControls() {
        try {
            fullName = findViewById(R.id.a2_tvFullname);
            fullname2 = findViewById(R.id.a2_tvFullname2);
            gender = findViewById(R.id.a2_tvGender);
            email = findViewById(R.id.a2_tvEmail);
            address = findViewById(R.id.a2_tvAddress);
            username = findViewById(R.id.a2_tvPhoneNumber);
            accountType = findViewById(R.id.a2_tvAccountType);
            changepass = findViewById(R.id.a2_tvChangePass);
            tvUpdateProfile = findViewById(R.id.a2_tvLogout);
            editInfo = findViewById(R.id.a2_tvUpdate);
            imgUser = findViewById(R.id.a2_imgUser);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void mainLoad() {
        try {
            addControls();
            getDataUserLogin();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getDataToView() {
        try {
            fullName.setText(clsUser.getFullName());
            fullname2.setText(clsUser.getFullName());
            gender.setText(clsUser.getPhone());
            email.setText(clsUser.getEmail());
            address.setText(clsUser.getAddress());
            accountType.setText(clsUser.getPermission());
            username.setText(userLogin);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getDataUserLogin() {
        try {
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference users = database.getReference("CSO").child(Const.FIREBASE_URI.USERS_REF).child(userLogin);
            users.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        if (dataSnapshot.getValue() != null) {
                            clsUser = new UserModel();
                            clsUser = dataSnapshot.getValue(UserModel.class);
                            getDataToView();
                        } else {
                            Toast.makeText(UserInfoActivity.this, "Can't get user info. Please contact admin", Toast.LENGTH_LONG).show();
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

    public void onChangePassword(View view) {
        try {
            Intent intent = new Intent(UserInfoActivity.this, ChangePasswordUserActivity.class);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onUpdateProfile(View view) {
        Intent intent;
        if (clsUser.getPermission().equalsIgnoreCase(Const.USER_TYPE.USER)) {
            intent = new Intent(UserInfoActivity.this, UpdateProfileUserActivity.class);
        } else {
            intent = new Intent(UserInfoActivity.this, UpdateProfileAdminActivity.class);
        }
        startActivity(intent);
        finish();
    }

    public void onLogout(View view) {
        preferences.clearAllData();

        Intent intent = new Intent(UserInfoActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
