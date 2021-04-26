package com.sh.orderapp.ui.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.FirebaseDatabase;
import com.sh.orderapp.R;
import com.sh.orderapp.adapter.ConfirmOrderAdapter;
import com.sh.orderapp.model.FoodModel;
import com.sh.orderapp.model.InvoiceModel;
import com.sh.orderapp.model.UserModel;
import com.sh.orderapp.utils.Const;
import com.sh.orderapp.utils.MySharedPreferences;
import com.sh.orderapp.utils.StringFormatUtils;

import java.util.ArrayList;
import java.util.List;

import static com.sh.orderapp.utils.Common.getDecimalFormattedString;


public class ConfirmOrderActivity extends AppCompatActivity {
    private ListView lvFood;
    private ConfirmOrderAdapter adapter;
    private ArrayList<FoodModel> listFoodOrders;
    private TextView tvOrderTotal;
    private Spinner spnPayMethod;

    private int orderTotal = 0;
    private String payMethod;
    private UserModel userModel;
    private UserModel restaurantModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);
        MySharedPreferences preferences = new MySharedPreferences(this);
        userModel = preferences.getUser(Const.USER_LOGIN);
        restaurantModel = preferences.getUser(Const.RESTAURANT_SELECTED);
        mainLoad();
    }

    public void addControls() {
        try {
            lvFood = findViewById(R.id.lvFoodConfirmOrder);
            tvOrderTotal = findViewById(R.id.tvTotalPriceConfirmOrder);
            spnPayMethod = this.findViewById(R.id.spnPayMethodConfirmOrder);

            Intent intent = getIntent();
            ArrayList<FoodModel> drinkList = (ArrayList<FoodModel>) intent.getSerializableExtra("ORDERS");
            listFoodOrders = new ArrayList<>();

            assert drinkList != null;
            for (int i = 0; i < drinkList.size(); i++) {
                if (drinkList.get(i).getNowQty() > 0) {
                    listFoodOrders.add(drinkList.get(i));
                }
            }

            List<String> listPayMethod = new ArrayList<>();
            listPayMethod.add("Payment on delivery");
            listPayMethod.add("Payment by for MoMo");
            listPayMethod.add("Pament by Viettel Pay");

            ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listPayMethod);
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spnPayMethod.setAdapter(aa);
            spnPayMethod.setSelection(0);
            payMethod = listPayMethod.get(0);

            spnPayMethod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    payMethod = (String) spnPayMethod.getSelectedItem();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void mainLoad() {
        addControls();
        showDataToListView();
    }

    public void showDataToListView() {
        try {
            lvFood.setAdapter(null);
            adapter = new ConfirmOrderAdapter(this, R.layout.item_food_cart, listFoodOrders, foodModel -> {
                listFoodOrders.remove(foodModel);
                adapter.notifyDataSetChanged();
                calculatorTotalPrice();
            });
            this.lvFood.setAdapter(adapter);
            calculatorTotalPrice();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void calculatorTotalPrice() {
        orderTotal = 0;
        for (int i = 0; i < listFoodOrders.size(); i++) {
            orderTotal += listFoodOrders.get(i).getNowQty() * listFoodOrders.get(i).getPrice();
        }
        tvOrderTotal.setText(getDecimalFormattedString(String.valueOf(orderTotal)));
    }

    private void setDataOrder() {
        try {
            String dateCurrent = StringFormatUtils.getCurrentDateStr();
            String[] strings = dateCurrent.split("/");

            InvoiceModel invoiceModel = InvoiceModel.builder()
                    .dateRutGon(StringFormatUtils.getCurrentDateNotTimeStr())
                    .date(StringFormatUtils.getCurrentDateStr())
                    .yearly(String.valueOf(strings[2]))
                    .month(String.valueOf(strings[1]))
                    .restaurantModel(restaurantModel)
                    .listFoods(listFoodOrders)
                    .payMethod(payMethod)
                    .total(orderTotal)
                    .user(userModel)
                    .build();

            FirebaseDatabase.getInstance().getReference().child("CSO").child(Const.FIREBASE_URI.INVOICE_USER_REF)
                    .child(userModel.getUsername())
                    .push()
                    .setValue(invoiceModel);

            FirebaseDatabase.getInstance().getReference().child("CSO").child(Const.FIREBASE_URI.INVOICE_ADMIN_REF)
                    .push()
                    .setValue(invoiceModel);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void OrderNow(View view) {
        try {
            setDataOrder();
            Intent intent = new Intent(ConfirmOrderActivity.this, OrderCompleteActivity.class);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
