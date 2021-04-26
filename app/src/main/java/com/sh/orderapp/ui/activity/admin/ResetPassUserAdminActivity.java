package com.sh.orderapp.ui.activity.admin;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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
import com.sh.orderapp.utils.Const;

import java.util.ArrayList;

import static com.sh.orderapp.utils.Common.PASS_DEFAULT;


public class ResetPassUserAdminActivity extends AppCompatActivity {
    private Spinner spUserID;
    private ArrayList<UserModel> listUser = new ArrayList<>();
    private ArrayList<String> listUserID = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_menu_user_reset_pass);
        addControls();
        getDataFireBase();
    }

    public void addControls() {
        spUserID = findViewById(R.id.a10_reset_edUserID);
    }

    public void getDataFireBase() {
        try {
            // Get list from Firebase
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference users = database.getReference("CSO").child(Const.FIREBASE_URI.USERS_REF);
            users.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        listUser.clear();
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            UserModel user = ds.getValue(UserModel.class);
                            listUser.add(user);
                        }
                        setDataSpinner();
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

    public void setDataSpinner() {
        for (int i = 0; i < listUser.size(); i++) {
            listUserID.add(listUser.get(i).getUsername());
        }

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listUserID);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spUserID.setAdapter(adapter);

    }

    public void onCancel(View view) {
        ResetPassUserAdminActivity.this.finish();
    }

    public void onResetPassword(View view) {
        resetPassword();
    }

    private void resetPassword() {
        try {
            final String userID = spUserID.getSelectedItem().toString();
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference userMaster = database.getReference("CSO").child(Const.FIREBASE_URI.USERS_REF).child(userID);
            userMaster.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        DatabaseReference user = database.getReference("CSO").child(Const.FIREBASE_URI.USERS_REF);
                        user.child(userID).child("password").setValue(PASS_DEFAULT);
                        Toast.makeText(ResetPassUserAdminActivity.this, "Reset password successful", Toast.LENGTH_SHORT).show();
                        ResetPassUserAdminActivity.this.finish();
                    } else {
                        Toast.makeText(ResetPassUserAdminActivity.this, "Wrong UserID, please try again!", Toast.LENGTH_LONG).show();
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
