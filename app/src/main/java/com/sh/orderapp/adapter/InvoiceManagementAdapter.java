package com.sh.orderapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sh.orderapp.R;
import com.sh.orderapp.model.InvoiceModel;
import com.sh.orderapp.utils.StringFormatUtils;

import java.util.List;


public class InvoiceManagementAdapter extends RecyclerView.Adapter<InvoiceManagementAdapter.InvoiceManagementViewHolder> {
    private final Context mContext;
    private final List<InvoiceModel> lstInvoiceModels;
    private final OnBillItemClickListener listener;


    public InvoiceManagementAdapter(Context mContext, List<InvoiceModel> lstInvoiceModels, OnBillItemClickListener listener) {
        this.mContext = mContext;
        this.lstInvoiceModels = lstInvoiceModels;
        this.listener = listener;
    }

    @NonNull
    @Override
    public InvoiceManagementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_invoice_management, parent, false);
        return new InvoiceManagementViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InvoiceManagementViewHolder holder, final int position) {
        InvoiceModel model = lstInvoiceModels.get(position);
        holder.tvDateTime.setText("Time: " + model.getDate());
        holder.tvAddress.setText("Address: " + model.getUser().getAddress());
        holder.tvNameCustomer.setText("Orderer: " + model.getUser().getFullName());
        holder.tvTotalMoney.setText("Total: " + StringFormatUtils.convertToStringMoneyVND(model.getTotal()));

        holder.bind(model, listener);
    }

    @Override
    public int getItemCount() {
        return lstInvoiceModels.size();
    }

    public static class InvoiceManagementViewHolder extends RecyclerView.ViewHolder {
        TextView tvNameCustomer, tvAddress, tvDateTime, tvTotalMoney;

        public InvoiceManagementViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNameCustomer = itemView.findViewById(R.id.tvNameCustomerBillManagementItem);
            tvAddress = itemView.findViewById(R.id.tvAddressBillManagementItem);
            tvDateTime = itemView.findViewById(R.id.tvDateBillManagementItem);
            tvTotalMoney = itemView.findViewById(R.id.tvTotalBillManagementItem);
        }

        public void bind(final InvoiceModel invoiceModel, final OnBillItemClickListener listener) {
            itemView.setOnClickListener(v -> listener.onClickItem(invoiceModel));
        }

    }

    public interface OnBillItemClickListener {
        void onClickItem(InvoiceModel invoiceModel);
    }

}

