package com.sh.orderapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sh.orderapp.R;
import com.sh.orderapp.model.NewsModel;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.TripViewHolder> {
    private final Context mContext;
    private final List<NewsModel> lstNews;
    private final OnNewsItemClickListener listener;

    public NewsAdapter(Context mContext, List<NewsModel> lstNews, OnNewsItemClickListener listener) {
        this.mContext = mContext;
        this.lstNews = lstNews;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_news, parent, false);
        return new TripViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripViewHolder holder, int position) {
        NewsModel NewsModel = lstNews.get(position);
        holder.tvName.setText(NewsModel.getName());
        holder.tvDescription.setText(NewsModel.getDescription());
        holder.bind(NewsModel, listener);
    }

    @Override
    public int getItemCount() {
        if (lstNews != null) {
            return lstNews.size();
        } else {
            return 0;
        }
    }

    public static class TripViewHolder extends RecyclerView.ViewHolder {
        protected TextView tvName, tvDescription;

        public TripViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvNameNewsItem);
            tvDescription = itemView.findViewById(R.id.tvDescriptionNewsItem);
        }

        public void bind(final NewsModel newsModel, final OnNewsItemClickListener listener) {
            itemView.setOnClickListener(v -> listener.onClickItem(newsModel));
        }

    }

    public interface OnNewsItemClickListener {
        void onClickItem(NewsModel newsModel);
    }

}
