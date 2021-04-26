package com.sh.orderapp.ui.activity.admin;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.BaseMultiplePermissionsListener;
import com.sh.orderapp.R;
import com.sh.orderapp.adapter.FoodManageAdapter;
import com.sh.orderapp.model.FoodModel;
import com.sh.orderapp.model.UserModel;
import com.sh.orderapp.utils.Const;
import com.sh.orderapp.utils.MySharedPreferences;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AdminFoodManagementActivity extends AppCompatActivity implements View.OnClickListener {
    private FloatingActionButton fabFood;
    private RecyclerView listViewFood;
    private List<FoodModel> listFoods;
    private ProgressDialog progressDialog;

    private View viewDialog;
    private AlertDialog foodDialog;
    private ImageView imvFoodDlg;
    private Button btnCancelDlg, btnAgreeDlg;
    private EditText edtNameFoodDlg, edtPriceFoodDlg;

    private Uri filePath;
    private FoodModel foodSelected;

    private MySharedPreferences preferences;
    private UserModel restaurantModel;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_food_management);
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
        fabFood = this.findViewById(R.id.fabFood);
        listViewFood = this.findViewById(R.id.rcvFood);
        LinearLayoutManager layoutManagerHot = new LinearLayoutManager(AdminFoodManagementActivity.this);
        layoutManagerHot.setOrientation(RecyclerView.VERTICAL);
        listViewFood.setLayoutManager(layoutManagerHot);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please waiting...");
        progressDialog.setCanceledOnTouchOutside(false);

        fabFood.setOnClickListener(this);
    }

    private void buildAllFoods() {
        try {
            // Get list from Firebase
            listFoods = new ArrayList<>();
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference drinks = database.getReference("CSO").child(Const.FIREBASE_URI.FOOD_REF);
            drinks.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        listFoods.clear();
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            FoodModel foodModel = ds.getValue(FoodModel.class);
                            if (foodModel != null && foodModel.getUserModel().getUsername().equalsIgnoreCase(restaurantModel.getUsername())) {
                                listFoods.add(foodModel);
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
            listViewFood.setAdapter(null);
            FoodManageAdapter detailsAdapter = new FoodManageAdapter(this, listFoods, new FoodManageAdapter.OnItemFoodClickListener() {
                @Override
                public void onClickUpdateFood(FoodModel foodModel) {
                    foodSelected = foodModel;
                    imvFoodDlg.setEnabled(false);
                    btnAgreeDlg.setText("Update");
                    edtNameFoodDlg.setText(foodSelected.getName());
                    edtNameFoodDlg.setSelection(foodSelected.getName().length());
                    edtPriceFoodDlg.setText(String.valueOf(foodSelected.getPrice()));
                    Picasso.with(AdminFoodManagementActivity.this).load(foodSelected.getImage()).placeholder(R.drawable.c1)
                            .error(R.drawable.c1).into(imvFoodDlg);
                    showFoodDialog();
                }

                @Override
                public void onClickDeleteFood(FoodModel foodModel) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(AdminFoodManagementActivity.this);
                    alert.setTitle("Delete Food");
                    alert.setMessage("Are you sure delete this food?");
                    alert.setPositiveButton(android.R.string.yes, (dialog, which) -> {
                        FirebaseDatabase.getInstance().getReference("CSO").child(Const.FIREBASE_URI.FOOD_REF).child(foodModel.getId()).removeValue();
                        Toast.makeText(AdminFoodManagementActivity.this, "Delete food successfully", Toast.LENGTH_SHORT).show();
                        buildAllFoods();
                    });
                    alert.setNegativeButton(android.R.string.no, (dialog, which) -> dialog.cancel());
                    alert.show();
                }
            });
            this.listViewFood.setAdapter(detailsAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initFoodDialog() {
        foodDialog = new AlertDialog.Builder(AdminFoodManagementActivity.this, R.style.CustomAlertDialog).create();
        viewDialog = getLayoutInflater().inflate(R.layout.dialog_modify_food, null);

        imvFoodDlg = viewDialog.findViewById(R.id.imvImageFoodDlg);
        edtNameFoodDlg = viewDialog.findViewById(R.id.edtNameFoodDlg);
        edtPriceFoodDlg = viewDialog.findViewById(R.id.edtPriceFoodDlg);
        btnCancelDlg = viewDialog.findViewById(R.id.btnCancelFoodDlg);
        btnAgreeDlg = viewDialog.findViewById(R.id.btnAgreeFoodDlg);
        foodDialog.setView(viewDialog);

        btnCancelDlg.setOnClickListener(this);
        btnAgreeDlg.setOnClickListener(this);
        imvFoodDlg.setOnClickListener(this);

        btnCancelDlg.setTransformationMethod(null);
        btnAgreeDlg.setTransformationMethod(null);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fabFood: {
                onClickFabFood();
                break;
            }
            case R.id.imvImageFoodDlg: {
                onClickImageViewDialog();
                break;
            }
            case R.id.btnCancelFoodDlg: {
                onClickCancelDialog();
                break;
            }
            case R.id.btnAgreeFoodDlg: {
                onClickAgreeDialog();
                break;
            }
        }
    }

    private void onClickImageViewDialog() {
        checkPermission();
    }

    private void onClickFabFood() {
        clearDataDialog();
        showFoodDialog();
    }

    private void onClickCancelDialog() {
        clearDataDialog();
        hiddenFoodDialog();
    }

    private void onClickAgreeDialog() {
        String nameFood = edtNameFoodDlg.getText().toString();
        String priceFood = edtPriceFoodDlg.getText().toString();
        if (nameFood.isEmpty() || priceFood.isEmpty()) {
            Toast.makeText(this, "Please fill all data", Toast.LENGTH_SHORT).show();
        } else {
            showProgressDialog();
            final FoodModel foodModel = FoodModel.builder()
                    .name(nameFood).nowQty(0)
                    .price(Integer.parseInt(priceFood))
                    .build();
            if (foodSelected != null) {
                foodModel.setId(foodSelected.getId());
                foodModel.setImage(foodSelected.getImage());
                foodModel.setUserModel(foodSelected.getUserModel());
            } else {
                foodModel.setId(UUID.randomUUID().toString());
                foodModel.setUserModel(restaurantModel);
            }

            final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("CSO").child(Const.FIREBASE_URI.FOOD_REF);
            if (foodSelected == null) {
                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            //Save image to storage
                            StorageReference fileUpload = FirebaseStorage.getInstance().getReference().child("icon/" + foodModel.getId());
                            fileUpload.putFile(filePath)
                                    .addOnSuccessListener(taskSnapshot -> fileUpload.getDownloadUrl().addOnSuccessListener(uri -> {
                                        String url = uri.toString();
                                        foodModel.setImage(url);
                                        mDatabase.child(foodModel.getId()).setValue(foodModel);
                                        Toast.makeText(AdminFoodManagementActivity.this, "Add new food successful", Toast.LENGTH_SHORT).show();
                                        hiddenProgressDialog();
                                        hiddenFoodDialog();
                                        foodSelected = null;
                                    }))
                                    .addOnFailureListener(e -> {
                                        hiddenProgressDialog();
                                        Toast.makeText(AdminFoodManagementActivity.this, "Error, please check again", Toast.LENGTH_SHORT).show();
                                    });
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
                            mDatabase.child(foodModel.getId()).setValue(foodModel);
                            Toast.makeText(AdminFoodManagementActivity.this, "Update food successful", Toast.LENGTH_SHORT).show();
                            hiddenProgressDialog();
                            hiddenFoodDialog();
                            foodSelected = null;
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

    private void checkPermission() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new BaseMultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        super.onPermissionsChecked(report);
                        choseFiles();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        super.onPermissionRationaleShouldBeShown(permissions, token);
                    }
                }).check();
    }

    private void choseFiles() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Chọn ảnh"), 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imvFoodDlg.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void clearDataDialog() {
        filePath = null;
        foodSelected = null;
        imvFoodDlg.setEnabled(true);
        btnAgreeDlg.setText("Insert");
        edtNameFoodDlg.setText("");
        edtPriceFoodDlg.setText("");
        edtNameFoodDlg.requestFocus();
        imvFoodDlg.setImageResource(R.drawable.camera_picture);
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
        if (foodDialog != null && !foodDialog.isShowing()) {
            foodDialog.show();
        }
    }

    private void hiddenFoodDialog() {
        if (foodDialog != null && foodDialog.isShowing()) {
            foodDialog.dismiss();
        }
    }
}
