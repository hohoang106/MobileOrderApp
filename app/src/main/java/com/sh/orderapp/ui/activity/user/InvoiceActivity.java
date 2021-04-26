package com.sh.orderapp.ui.activity.user;

import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sh.orderapp.R;
import com.sh.orderapp.adapter.InvoiceAdapter;
import com.sh.orderapp.model.InvoiceModel;
import com.sh.orderapp.model.UserModel;
import com.sh.orderapp.utils.Const;
import com.sh.orderapp.utils.MySharedPreferences;

import java.util.ArrayList;
import java.util.List;


public class InvoiceActivity extends AppCompatActivity {

    private ListView lvInvoice;
    private List<InvoiceModel> listInvoices;
    private InvoiceAdapter invoiceAdapter;

    private UserModel userModel;
    private MySharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);
        initView();
        initData();
        initAdapter();
    }

    private void initView() {
        lvInvoice = this.findViewById(R.id.lvInvoicePrivate);
    }

    private void initData() {
        listInvoices = new ArrayList<>();
        preferences = new MySharedPreferences(this);
        userModel = preferences.getUser(Const.USER_LOGIN);
        try {
            final DatabaseReference users = FirebaseDatabase.getInstance()
                    .getReference("CSO").child(Const.FIREBASE_URI.INVOICE_USER_REF).child(userModel.getUsername());
            users.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        listInvoices.clear();
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            InvoiceModel invoiceModel = ds.getValue(InvoiceModel.class);
                            if (invoiceModel != null && invoiceModel.getUser().getUsername().equalsIgnoreCase(userModel.getUsername())) {
                                listInvoices.add(invoiceModel);
                            }
                        }
                        initAdapter();
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

    private void initAdapter() {
        invoiceAdapter = new InvoiceAdapter(this, R.layout.item_invoice, listInvoices);
        lvInvoice.setAdapter(invoiceAdapter);
    }
}