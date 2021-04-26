package com.sh.orderapp.ui.activity.user;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sh.orderapp.R;
import com.sh.orderapp.model.NewsModel;
import com.sh.orderapp.utils.Const;

public class DetailNewsActivity extends AppCompatActivity {

    private TextView tvTitle, tvContent;

    private NewsModel newsModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_news);
        initData();
        initView();
    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            newsModel = bundle.getParcelable(Const.NEW_SELECTED);
        }
    }

    private void initView() {
        tvTitle = this.findViewById(R.id.tvTitleNewsItemDetail);
        tvContent = this.findViewById(R.id.tvContentNewsItemDetail);

        tvTitle.setText(newsModel.getName());
        tvContent.setText(newsModel.getContent());
    }
}
