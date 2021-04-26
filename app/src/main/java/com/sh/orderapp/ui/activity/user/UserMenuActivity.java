package com.sh.orderapp.ui.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
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
import com.sh.orderapp.adapter.FoodAdapter;
import com.sh.orderapp.model.FoodModel;
import com.sh.orderapp.model.UserModel;
import com.sh.orderapp.utils.Const;
import com.sh.orderapp.utils.MySharedPreferences;

import java.util.ArrayList;

import static com.sh.orderapp.utils.Common.flagTableList;


public class UserMenuActivity extends AppCompatActivity {

    private EditText edtSearch;
    private TextView textView7;

    private ListView lvFood;
    private ArrayList<FoodModel> listFoods;
    private FoodAdapter detailsAdapter;

    private MySharedPreferences preferences;
    private UserModel restaurantModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_menu);
        preferences = new MySharedPreferences(this);
        restaurantModel = preferences.getUser(Const.RESTAURANT_SELECTED);
        mainLoad();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void addControls() {
        textView7 = this.findViewById(R.id.textView7);
        edtSearch = findViewById(R.id.edtSearchFoodMenuUser);
        lvFood = findViewById(R.id.a3_listView);

        if (restaurantModel != null && restaurantModel.getFullName() != null) {
            textView7.setText(restaurantModel.getFullName());
        }
        getDataFireBase();

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                detailsAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        lvFood.setOnItemClickListener((parent, view, position, id) -> {
            Bundle mBundle = new Bundle();
            mBundle.putParcelable(Const.FOOD_SELECT, listFoods.get(position));

            Intent mIntent = new Intent(UserMenuActivity.this, DetailFoodActivity.class);
            mIntent.putExtras(mBundle);
            startActivity(mIntent);
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        edtSearch.setText("");
    }

    public void mainLoad() {
        addControls();
    }

    public void onInvoice(View view) {
        flagTableList = true; // invoice
        Intent intent = new Intent(UserMenuActivity.this, InvoiceActivity.class);
        startActivity(intent);
    }

    public void onUserAccount(View view) {
        Intent intent = new Intent(UserMenuActivity.this, UserInfoActivity.class);
        startActivity(intent);
    }

    public void onUserNews(View view) {
        Intent intent = new Intent(UserMenuActivity.this, NewsActivity.class);
        startActivity(intent);
    }


    public void getDataFireBase() {
        try {
            listFoods = new ArrayList<>();

            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference drinks = database.getReference("CSO").child(Const.FIREBASE_URI.FOOD_REF);
            drinks.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        listFoods.clear();
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            FoodModel model = ds.getValue(FoodModel.class);
                            if (model != null && model.getUserModel() != null && restaurantModel.getUsername().equalsIgnoreCase(model.getUserModel().getUsername())) {
                                listFoods.add(model);
                            }
                        }
                        loadListFoods();
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

    public void loadListFoods() {
        try {
            lvFood.setAdapter(null);
            detailsAdapter = new FoodAdapter(this, R.layout.item_food, listFoods);
            this.lvFood.setAdapter(detailsAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void AddOrder(View view) {
        try {
            int orderTotal = 0;
            for (int i = 0; i < listFoods.size(); i++) {
                orderTotal += listFoods.get(i).getNowQty() * listFoods.get(i).getPrice();
            }
            if (orderTotal > 0) {
                Intent intent = new Intent(UserMenuActivity.this, ConfirmOrderActivity.class);
                intent.putExtra("ORDERS", listFoods);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Please select food", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
