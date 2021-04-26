package com.sh.orderapp.ui.activity.admin;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sh.orderapp.R;
import com.sh.orderapp.adapter.InvoiceManagementAdapter;
import com.sh.orderapp.model.InvoiceModel;
import com.sh.orderapp.model.UserModel;
import com.sh.orderapp.utils.Const;
import com.sh.orderapp.utils.MySharedPreferences;
import com.sh.orderapp.utils.StringFormatUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AdminBillManagementActivity extends AppCompatActivity {

    private TextView tvDateSearch, tvTotalMoney, tvQuantity;
    private RecyclerView rcvInvoice;

    private InvoiceManagementAdapter adapter;
    private List<InvoiceModel> lstInvoiceModels;

    private String dateSearch;
    private UserModel userModel;
    private MySharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_bill_management);
        initView();
        initAdapter();
        initData();
        setDataRecycleView();
    }

    private void initData() {
        dateSearch = StringFormatUtils.getCurrentDateStr();
        tvDateSearch.setText(StringFormatUtils.getCurrentDateStr());

        preferences = new MySharedPreferences(this);
        userModel = preferences.getUser(Const.USER_LOGIN);

        tvDateSearch.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (view1, year1, monthOfYear, dayOfMonth) -> {
                        String date = (dayOfMonth < 10 ? ("0" + dayOfMonth) : dayOfMonth)
                                + "/" + ((monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : (monthOfYear + 1)) + "/" + year1;
                        tvDateSearch.setText(date);
                        dateSearch = date;
                        setDataRecycleView();
                    }, year, month, day);
            datePickerDialog.show();
        });
    }

    private void initView() {
        rcvInvoice = this.findViewById(R.id.rcvBillManagement);
        tvDateSearch = this.findViewById(R.id.tvDateBillManagement);
        tvQuantity = this.findViewById(R.id.tvQuantityBillManagement);
        tvTotalMoney = this.findViewById(R.id.tvTotalMoneyBillManagement);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rcvInvoice.setLayoutManager(layoutManager);
    }

    private void initAdapter() {
        lstInvoiceModels = new ArrayList<>();
        adapter = new InvoiceManagementAdapter(this, lstInvoiceModels, invoiceModel -> {
            Bundle mBundle = new Bundle();
            mBundle.putParcelable(Const.BILL_SELECT, invoiceModel);
            Intent mIntent = new Intent(AdminBillManagementActivity.this, AdminBillDetailManagementActivity.class);
            mIntent.putExtras(mBundle);
            startActivity(mIntent);
        });
        rcvInvoice.setAdapter(adapter);
    }

    private void setDataRecycleView() {
        try {
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference users = database.getReference("CSO")
                    .child(Const.FIREBASE_URI.INVOICE_ADMIN_REF);
            users.addListenerForSingleValueEvent(new ValueEventListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    lstInvoiceModels.clear();
                    if (dataSnapshot.getValue() != null) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            InvoiceModel invoiceModel = ds.getValue(InvoiceModel.class);
                            if (invoiceModel != null && dateSearch.equalsIgnoreCase(invoiceModel.getDate())) {
                                if (userModel != null && userModel.getUsername().equalsIgnoreCase(invoiceModel.getRestaurantModel().getUsername()))
                                    lstInvoiceModels.add(invoiceModel);
                            }
                        }
                        if (lstInvoiceModels.size() == 0) {
                            tvQuantity.setText(String.valueOf(0));
                            tvTotalMoney.setText(StringFormatUtils.convertToStringMoneyVND(0));
                            Toast.makeText(AdminBillManagementActivity.this, "No bill yet", Toast.LENGTH_SHORT).show();
                        } else {
                            tvTotalMoney.setText(StringFormatUtils.convertToStringMoneyVND(calculatorTotalMoney(lstInvoiceModels)));
                            tvQuantity.setText(String.valueOf(lstInvoiceModels.size()));
                        }
                    } else {
                        tvQuantity.setText(String.valueOf(0));
                        tvTotalMoney.setText(StringFormatUtils.convertToStringMoneyVND(0));
                        Toast.makeText(AdminBillManagementActivity.this, "No bill yet", Toast.LENGTH_SHORT).show();
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(AdminBillManagementActivity.this, "An error occurred, check again", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private int calculatorTotalMoney(List<InvoiceModel> list) {
        int total = 0;
        if (list.size() > 0) {
            total = list.stream().mapToInt(InvoiceModel::getTotal).sum();
        }
        return total;
    }


}