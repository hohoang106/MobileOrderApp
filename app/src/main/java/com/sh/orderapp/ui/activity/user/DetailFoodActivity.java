package com.sh.orderapp.ui.activity.user;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.sh.orderapp.adapter.FoodCommentAdapter;
import com.sh.orderapp.model.FoodCommentModel;
import com.sh.orderapp.model.FoodModel;
import com.sh.orderapp.model.UserModel;
import com.sh.orderapp.utils.Const;
import com.sh.orderapp.utils.MySharedPreferences;
import com.sh.orderapp.utils.StringFormatUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DetailFoodActivity extends AppCompatActivity implements View.OnClickListener {
    private ProgressDialog progressDialog;

    private Button btnComment;
    private ImageView imvFood;
    private EditText edtComment;
    private TextView tvNameFood, tvPriceFood;

    private RecyclerView rcvComment;
    private List<FoodCommentModel> commentModelList;
    private FoodCommentAdapter foodCommentAdapter;

    private FoodModel foodModel;
    private UserModel userComment;
    private MySharedPreferences preferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_food);
        initViews();
        initData();
        initAdapter();
        clearData();
    }

    private void initViews() {
        btnComment = this.findViewById(R.id.btnCommentDetailFood);
        imvFood = this.findViewById(R.id.imvDetailFood);
        edtComment = this.findViewById(R.id.edtCommentDetailFood);
        tvNameFood = this.findViewById(R.id.tvNameDetailFood);
        tvPriceFood = this.findViewById(R.id.tvPriceDetailFood);

        btnComment.setOnClickListener(this);

        rcvComment = this.findViewById(R.id.rcvCommentDetailFood);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rcvComment.setLayoutManager(layoutManager);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please waiting...");
        progressDialog.setCanceledOnTouchOutside(false);
    }

    private void initData() {
        Bundle mBundle = getIntent().getExtras();
        if (mBundle != null) {
            foodModel = mBundle.getParcelable(Const.FOOD_SELECT);
            tvNameFood.setText(foodModel.getName());
            tvPriceFood.setText(StringFormatUtils.convertToStringMoneyVND(foodModel.getPrice()));
            Picasso.with(this).load(foodModel.getImage())
                    .placeholder(R.drawable.c1).error(R.drawable.c1).into(imvFood);
        } else {
            tvNameFood.setText("Chưa có dữ liệu");
            tvPriceFood.setText("Chưa có dữ liệu");
        }

        preferences = new MySharedPreferences(this);
        userComment = preferences.getUser(Const.USER_LOGIN);
    }

    private void initAdapter() {
        commentModelList = new ArrayList<>();
        foodCommentAdapter = new FoodCommentAdapter(this, commentModelList);
        rcvComment.setAdapter(foodCommentAdapter);

        buildAllCommentFood();
    }

    private void buildAllCommentFood() {
        showProgressDialog();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference drinks = database.getReference("CSO").child(Const.FIREBASE_URI.COMMENT_FOOD_REF);
        drinks.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    commentModelList.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        FoodCommentModel model = ds.getValue(FoodCommentModel.class);
                        if (model != null && model.getFoodModel().getId().equalsIgnoreCase(foodModel.getId())) {
                            commentModelList.add(model);
                        }
                    }
                    foodCommentAdapter.notifyDataSetChanged();
                    hiddenProgressDialog();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        hiddenProgressDialog();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnCommentDetailFood) {
            String content = edtComment.getText().toString();
            if (content.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            } else {
                FoodCommentModel model = new FoodCommentModel();
                model.setFoodModel(foodModel);
                model.setContent(content.trim());
                model.setUserCommentModel(userComment);
                model.setId(UUID.randomUUID().toString());
                model.setCreatedDate(StringFormatUtils.getCurrentDateStrFull());

                FirebaseDatabase.getInstance().getReference().child("CSO").child(Const.FIREBASE_URI.COMMENT_FOOD_REF)
                        .push().setValue(model);
                buildAllCommentFood();
                clearData();
                Toast.makeText(this, "Đánh giá thành công", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void clearData() {
        edtComment.setText("");
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
}
