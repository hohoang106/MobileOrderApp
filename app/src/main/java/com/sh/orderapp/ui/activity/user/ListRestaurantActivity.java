package com.sh.orderapp.ui.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

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
import com.sh.orderapp.adapter.RestaurantAdapter;
import com.sh.orderapp.model.UserModel;
import com.sh.orderapp.utils.Const;
import com.sh.orderapp.utils.MySharedPreferences;

import java.util.ArrayList;
import java.util.List;

public class ListRestaurantActivity extends AppCompatActivity {
    private EditText edtSearch;
    private RecyclerView rcvRestaurant;
    private RestaurantAdapter adapter;
    private List<UserModel> listRestaurants;
    private MySharedPreferences preferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);
        initView();
        buildAllNews();
    }

    private void initView() {
        edtSearch = this.findViewById(R.id.edtSearchRestaurant);
        rcvRestaurant = this.findViewById(R.id.rcvRestaurant);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rcvRestaurant.setLayoutManager(layoutManager);

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void buildAllNews() {
        preferences = new MySharedPreferences(this);
        listRestaurants = new ArrayList<>();
        final DatabaseReference drinks = FirebaseDatabase.getInstance().getReference("CSO").child(Const.FIREBASE_URI.USERS_REF);
        drinks.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    listRestaurants = new ArrayList<>();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        UserModel model = ds.getValue(UserModel.class);
                        if (model != null && Const.USER_TYPE.RESTAURANT_MANAGE.equalsIgnoreCase(model.getPermission())) {
                            listRestaurants.add(model);
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

    private void loadNews() {
        rcvRestaurant.setAdapter(null);
        adapter = new RestaurantAdapter(this, listRestaurants, userModel -> {
            preferences.putUser(Const.RESTAURANT_SELECTED, userModel);
            Intent intent = new Intent(ListRestaurantActivity.this, UserMenuActivity.class);
            startActivity(intent);
        });
        rcvRestaurant.setAdapter(adapter);
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        edtSearch.setText("");
    }

}
