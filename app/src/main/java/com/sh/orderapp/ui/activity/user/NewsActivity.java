package com.sh.orderapp.ui.activity.user;

import android.content.Intent;
import android.os.Bundle;

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
import com.sh.orderapp.adapter.NewsAdapter;
import com.sh.orderapp.model.NewsModel;
import com.sh.orderapp.model.UserModel;
import com.sh.orderapp.utils.Const;
import com.sh.orderapp.utils.MySharedPreferences;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity {

    private RecyclerView rcvNews;
    private NewsAdapter adapter;
    private List<NewsModel> listNews;

    private UserModel userModel;
    private UserModel restaurantModel;
    private MySharedPreferences preferences;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_news);
        initView();
        initData();
        buildAllNews();
    }

    private void initData() {
        preferences = new MySharedPreferences(this);
        userModel = preferences.getUser(Const.USER_LOGIN);
        restaurantModel = preferences.getUser(Const.RESTAURANT_SELECTED);
    }

    private void initView() {
        rcvNews = this.findViewById(R.id.rcvNews);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rcvNews.setLayoutManager(layoutManager);
    }

    private void buildAllNews() {
        listNews = new ArrayList<>();
        final DatabaseReference drinks = FirebaseDatabase.getInstance().getReference("CSO").child(Const.FIREBASE_URI.NEWS_REF);
        drinks.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    listNews = new ArrayList<>();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        NewsModel model = ds.getValue(NewsModel.class);
                        if (model != null && model.getUserModel().getUsername().equalsIgnoreCase(restaurantModel.getUsername())) {
                            listNews.add(model);
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
        rcvNews.setAdapter(null);
        adapter = new NewsAdapter(this, listNews, newsModel -> {
            Bundle bundle = new Bundle();
            bundle.putParcelable(Const.NEW_SELECTED, newsModel);
            Intent intent = new Intent(NewsActivity.this, DetailNewsActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        });
        rcvNews.setAdapter(adapter);
    }

}
