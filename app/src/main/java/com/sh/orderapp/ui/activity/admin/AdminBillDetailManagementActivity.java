package com.sh.orderapp.ui.activity.admin;

import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sh.orderapp.R;
import com.sh.orderapp.adapter.BillDetailManagementAdapter;
import com.sh.orderapp.model.FoodModel;
import com.sh.orderapp.model.InvoiceModel;
import com.sh.orderapp.utils.Const;

import java.util.ArrayList;
import java.util.List;

public class AdminBillDetailManagementActivity extends AppCompatActivity {
    private ListView rcvBillDetail;
    private List<FoodModel> listFoods;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_bill_detail_management);
        initView();
        initData();
        initAdapter();
    }


    private void initData() {
        Bundle mBundle = getIntent().getExtras();
        if (mBundle != null) {
            InvoiceModel invoiceModel = mBundle.getParcelable(Const.BILL_SELECT);
            listFoods = invoiceModel.getListFoods();
        } else {
            listFoods = new ArrayList<>();
        }
    }

    private void initView() {
        rcvBillDetail = this.findViewById(R.id.rcvBillDetailManagement);
    }

    private void initAdapter() {
        BillDetailManagementAdapter adapter = new BillDetailManagementAdapter(this, R.layout.item_food_cart, listFoods);
        rcvBillDetail.setAdapter(adapter);
    }

}
