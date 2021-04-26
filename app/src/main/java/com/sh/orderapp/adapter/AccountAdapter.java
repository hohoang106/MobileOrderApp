package com.sh.orderapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sh.orderapp.R;
import com.sh.orderapp.model.UserModel;
import com.sh.orderapp.utils.Const;

import java.util.List;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.AccountViewHolder> {
    private final Context mContext;
    private final List<UserModel> lstUsers;
    private final OnAccountItemClickListener listener;

    public AccountAdapter(Context mContext, List<UserModel> lstUsers, OnAccountItemClickListener listener) {
        this.mContext = mContext;
        this.lstUsers = lstUsers;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AccountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_account_super_admin, parent, false);
        return new AccountViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountViewHolder holder, int position) {
        UserModel userModel = lstUsers.get(position);
        holder.tvName.setText("Restaurant name: " + userModel.getFullName());
        holder.tvAddress.setText("Address: " + userModel.getAddress());
        holder.tvStatus.setText("Status: " + userModel.getStatus());
        holder.tvPhone.setText("Phone: " + userModel.getPhone());

        if (Const.USER_STATUS.APPROVE.equals(userModel.getStatus())) {
            holder.llStatus.setBackgroundColor(mContext.getResources().getColor(R.color.lime_green));
        } else if (Const.USER_STATUS.WAIT.equals(userModel.getStatus())) {
            holder.llStatus.setBackgroundColor(mContext.getResources().getColor(R.color.yellow));
        } else {
            holder.llStatus.setBackgroundColor(mContext.getResources().getColor(R.color.red));
        }
        holder.bind(userModel, listener);
    }

    @Override
    public int getItemCount() {
        if (lstUsers != null) {
            return lstUsers.size();
        } else {
            return 0;
        }
    }

    public static class AccountViewHolder extends RecyclerView.ViewHolder {
        protected LinearLayout llStatus;
        protected TextView tvName, tvAddress, tvStatus, tvPhone;


        public AccountViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvFullNameItemAccount);
            tvAddress = itemView.findViewById(R.id.tvAddressItemAccount);
            tvStatus = itemView.findViewById(R.id.tvStatusItemAccount);
            tvPhone = itemView.findViewById(R.id.tvPhoneItemAccount);
            llStatus = itemView.findViewById(R.id.llStatusItemAccount);
        }

        public void bind(final UserModel userModel, final OnAccountItemClickListener listener) {
            itemView.setOnClickListener(v -> listener.onClickItem(userModel));
        }

    }

    public interface OnAccountItemClickListener {
        void onClickItem(UserModel userModel);
    }

}
