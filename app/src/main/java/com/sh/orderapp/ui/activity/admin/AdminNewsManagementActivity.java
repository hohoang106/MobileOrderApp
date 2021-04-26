package com.sh.orderapp.ui.activity.admin;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sh.orderapp.R;
import com.sh.orderapp.adapter.NewsManagementAdapter;
import com.sh.orderapp.model.NewsModel;
import com.sh.orderapp.model.UserModel;
import com.sh.orderapp.utils.Const;
import com.sh.orderapp.utils.MySharedPreferences;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AdminNewsManagementActivity extends AppCompatActivity implements View.OnClickListener {

    private FloatingActionButton fabNews;
    private RecyclerView rcvNews;
    private List<NewsModel> listNews;
    private ProgressDialog progressDialog;

    private View viewDialog;
    private AlertDialog newsDialog;
    private Button btnCancelDlg, btnAgreeDlg;
    private EditText edtNameDlg, edtDescDlg, edtContentDlg;

    private NewsModel newsSelected;

    private MySharedPreferences preferences;
    private UserModel restaurantModel;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_news_management);
        initView();
        initData();
        buildAllFoods();
        initFoodDialog();
    }

    private void initData() {
        preferences = new MySharedPreferences(this);
        restaurantModel = preferences.getUser(Const.USER_LOGIN);
    }

    private void initView() {
        fabNews = this.findViewById(R.id.fabNewsManagement);
        rcvNews = this.findViewById(R.id.rcvNewsManagement);
        LinearLayoutManager layoutManagerHot = new LinearLayoutManager(AdminNewsManagementActivity.this);
        layoutManagerHot.setOrientation(RecyclerView.VERTICAL);
        rcvNews.setLayoutManager(layoutManagerHot);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please waiting...");
        progressDialog.setCanceledOnTouchOutside(false);

        fabNews.setOnClickListener(this);
    }

    private void buildAllFoods() {
        try {
            // Get list from Firebase
            listNews = new ArrayList<>();
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference drinks = database.getReference("CSO").child(Const.FIREBASE_URI.NEWS_REF);
            drinks.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        listNews.clear();
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            NewsModel model = ds.getValue(NewsModel.class);
                            if (model != null && model.getUserModel().getUsername().equalsIgnoreCase(restaurantModel.getUsername())) {
                                listNews.add(model);
                            }
                        }
                        loadFood();
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


    public void loadFood() {
        try {
            rcvNews.setAdapter(null);
            NewsManagementAdapter detailsAdapter = new NewsManagementAdapter(this, listNews, new NewsManagementAdapter.OnItemNewsClickListener() {
                @Override
                public void onClickUpdateNews(NewsModel newsModel) {
                    newsSelected = newsModel;
                    btnAgreeDlg.setText("Update");
                    edtNameDlg.setText(newsSelected.getName());
                    edtNameDlg.setSelection(newsSelected.getName().length());
                    edtDescDlg.setText(newsSelected.getDescription());
                    edtContentDlg.setText(newsSelected.getContent());
                    showFoodDialog();
                }

                @Override
                public void onClickDeleteNews(NewsModel newsModel) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(AdminNewsManagementActivity.this);
                    alert.setTitle("Delete News");
                    alert.setMessage("Are you sure delete this news?");
                    alert.setPositiveButton(android.R.string.yes, (dialog, which) -> {
                        FirebaseDatabase.getInstance().getReference("CSO").child(Const.FIREBASE_URI.NEWS_REF).child(newsModel.getId()).removeValue();
                        Toast.makeText(AdminNewsManagementActivity.this, "Delete news successfully", Toast.LENGTH_SHORT).show();
                        buildAllFoods();
                    });
                    alert.setNegativeButton(android.R.string.no, (dialog, which) -> dialog.cancel());
                    alert.show();
                }
            });
            this.rcvNews.setAdapter(detailsAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initFoodDialog() {
        newsDialog = new AlertDialog.Builder(AdminNewsManagementActivity.this, R.style.CustomAlertDialog).create();
        viewDialog = getLayoutInflater().inflate(R.layout.dialog_news, null);

        edtNameDlg = viewDialog.findViewById(R.id.edtNameNewsDlg);
        edtDescDlg = viewDialog.findViewById(R.id.edtDescNewsDlg);
        edtContentDlg = viewDialog.findViewById(R.id.edtContentNewsDlg);
        btnCancelDlg = viewDialog.findViewById(R.id.btnCancelNewsDlg);
        btnAgreeDlg = viewDialog.findViewById(R.id.btnAgreeNewsDlg);
        newsDialog.setView(viewDialog);

        btnCancelDlg.setOnClickListener(this);
        btnAgreeDlg.setOnClickListener(this);

        btnCancelDlg.setTransformationMethod(null);
        btnAgreeDlg.setTransformationMethod(null);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fabNewsManagement: {
                onClickFabNews();
                break;
            }
            case R.id.btnCancelNewsDlg: {
                onClickCancelDialog();
                break;
            }
            case R.id.btnAgreeNewsDlg: {
                onClickAgreeDialog();
                break;
            }
        }
    }

    private void onClickFabNews() {
        clearDataDialog();
        showFoodDialog();
    }

    private void onClickCancelDialog() {
        clearDataDialog();
        hiddenFoodDialog();
    }

    private void onClickAgreeDialog() {
        String nameNews = edtNameDlg.getText().toString();
        String descNews = edtDescDlg.getText().toString();
        String contentNews = edtContentDlg.getText().toString();
        if (nameNews.isEmpty() || descNews.isEmpty() || contentNews.isEmpty()) {
            Toast.makeText(this, "Please fill all data", Toast.LENGTH_SHORT).show();
        } else {
            showProgressDialog();
            final NewsModel newsModel = NewsModel.builder()
                    .name(nameNews)
                    .description(descNews)
                    .content(contentNews)
                    .build();
            if (newsSelected != null) {
                newsModel.setId(newsSelected.getId());
                newsModel.setUserModel(newsSelected.getUserModel());
            } else {
                newsModel.setId(UUID.randomUUID().toString());
                newsModel.setUserModel(restaurantModel);
            }

            final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("CSO").child(Const.FIREBASE_URI.NEWS_REF);
            if (newsSelected == null) {
                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            mDatabase.child(newsModel.getId()).setValue(newsModel);
                            Toast.makeText(AdminNewsManagementActivity.this, "Add news successful", Toast.LENGTH_SHORT).show();
                            hiddenProgressDialog();
                            hiddenFoodDialog();
                            newsSelected = null;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            } else {
                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            mDatabase.child(newsModel.getId()).setValue(newsModel);
                            Toast.makeText(AdminNewsManagementActivity.this, "Update news successful", Toast.LENGTH_SHORT).show();
                            hiddenProgressDialog();
                            hiddenFoodDialog();
                            newsSelected = null;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }
    }

    private void clearDataDialog() {
        newsSelected = null;
        btnAgreeDlg.setText("Insert");
        edtNameDlg.setText("");
        edtDescDlg.setText("");
        edtContentDlg.setText("");
        edtNameDlg.requestFocus();
    }

    private void showProgressDialog() {
        if (progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    private void hiddenProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void showFoodDialog() {
        if (newsDialog != null && !newsDialog.isShowing()) {
            newsDialog.show();
        }
    }

    private void hiddenFoodDialog() {
        if (newsDialog != null && newsDialog.isShowing()) {
            newsDialog.dismiss();
        }
    }
}
