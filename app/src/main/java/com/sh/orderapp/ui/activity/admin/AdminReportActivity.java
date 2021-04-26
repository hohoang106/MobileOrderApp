package com.sh.orderapp.ui.activity.admin;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sh.orderapp.R;
import com.sh.orderapp.model.InvoiceModel;
import com.sh.orderapp.model.UserModel;
import com.sh.orderapp.utils.Const;
import com.sh.orderapp.utils.MySharedPreferences;
import com.sh.orderapp.utils.StringFormatUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AdminReportActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner spinner;
    private TextView tvQuantityBill, tvTotal;

    private ArrayList<InvoiceModel> lstInvoices;

    private UserModel restaurantModel;
    private MySharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_report);
        initView();
        initData();
        addTypeReport();
    }

    private void initView() {
        spinner = findViewById(R.id.spnTypeReport);
        tvTotal = findViewById(R.id.tvTotalReport);
        tvQuantityBill = findViewById(R.id.tvQuantityReport);
        spinner.setOnItemSelectedListener(this);
    }

    private void initData() {
        preferences = new MySharedPreferences(this);
        restaurantModel = preferences.getUser(Const.USER_LOGIN);
    }

    public void addTypeReport() {
        try {
            lstInvoices = new ArrayList<>();
            List<String> per = new ArrayList<>();
            per.add("Daily");
            per.add("Monthly");
            per.add("Yearly");

            ArrayAdapter arrayAdapter = new ArrayAdapter<>(AdminReportActivity.this, android.R.layout.simple_list_item_1, per);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(arrayAdapter);
            spinner.setSelection(0);
            getDataReport(per.get(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getDataReport(String reportType) {
        final DatabaseReference database = FirebaseDatabase.getInstance()
                .getReference("CSO").child(Const.FIREBASE_URI.INVOICE_ADMIN_REF);
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    List<InvoiceModel> data = new ArrayList<>();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        InvoiceModel invoiceModel = ds.getValue(InvoiceModel.class);
                        if (invoiceModel != null && restaurantModel.getUsername().equalsIgnoreCase(invoiceModel.getRestaurantModel().getUsername())) {
                            data.add(invoiceModel);
                        }
                    }
                    if (!data.isEmpty()) {
                        int total = 0;
                        int quantity = 0;
                        String dateCurrent = StringFormatUtils.getCurrentDateStr();
                        String[] strings = dateCurrent.split("/");
                        lstInvoices.clear();
                        if ("Daily".equalsIgnoreCase(reportType)) {
                            String dateCompare = StringFormatUtils.getCurrentDateNotTimeStr();
                            lstInvoices.addAll(data.stream().filter(obj -> dateCompare.equalsIgnoreCase(obj.getDateRutGon()))
                                    .collect(Collectors.toList()));
                        } else if ("Monthly".equalsIgnoreCase(reportType)) {
                            lstInvoices.addAll(data.stream().filter(obj -> obj.getMonth().equalsIgnoreCase(String.valueOf(strings[1])))
                                    .collect(Collectors.toList()));
                        } else {
                            lstInvoices.addAll(data.stream().filter(obj -> obj.getYearly().equalsIgnoreCase(String.valueOf(strings[2])))
                                    .collect(Collectors.toList()));
                        }
                        if (!lstInvoices.isEmpty()) {
                            total = lstInvoices.stream().mapToInt(InvoiceModel::getTotal).sum();
                            quantity = lstInvoices.size();
                        }
                        tvQuantityBill.setText(String.valueOf(quantity));
                        tvTotal.setText(StringFormatUtils.convertToStringMoneyVND(total));
                    } else {
                        tvQuantityBill.setText(String.valueOf(0));
                        tvTotal.setText(StringFormatUtils.convertToStringMoneyVND(0));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String reportType = (String) spinner.getSelectedItem();
        getDataReport(reportType);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}