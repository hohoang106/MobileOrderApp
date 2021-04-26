package com.sh.orderapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sh.orderapp.R;
import com.sh.orderapp.model.InvoiceModel;
import com.sh.orderapp.utils.StringFormatUtils;

import java.util.List;


public class InvoiceAdapter extends BaseAdapter {

    private final Context context;
    private final int layout;
    private final List<InvoiceModel> lstInvoices;

    public InvoiceAdapter(Context context, int layout, List<InvoiceModel> lstInvoices) {
        this.context = context;
        this.layout = layout;
        this.lstInvoices = lstInvoices;
    }

    @Override
    public int getCount() {
        return lstInvoices.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public static class ViewHolders {
        private TextView tvDate, tvTotal, tvPayMethod, tvNameRestaurant;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        try {
            InvoiceAdapter.ViewHolders viewHolders;
            if (view == null) {
                // Khai báo màn hình
                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                assert layoutInflater != null;
                view = layoutInflater.inflate(layout, null);

                // Ánh xạ đối tượng
                viewHolders = new InvoiceAdapter.ViewHolders();
                viewHolders.tvDate = view.findViewById(R.id.tvNgayInvoiceItem);
                viewHolders.tvTotal = view.findViewById(R.id.tvTotalInvoiceItem);
                viewHolders.tvPayMethod = view.findViewById(R.id.tvMethodPaymentInvoiceItem);
                viewHolders.tvNameRestaurant = view.findViewById(R.id.tvNameResInvoiceItem);


                view.setTag(viewHolders);
                view.setTag(R.id.tvNgayInvoiceItem, viewHolders.tvDate);
                view.setTag(R.id.tvTotalInvoiceItem, viewHolders.tvTotal);
                view.setTag(R.id.tvMethodPaymentInvoiceItem, viewHolders.tvPayMethod);
                view.setTag(R.id.tvNameResInvoiceItem, viewHolders.tvNameRestaurant);
            } else {
                viewHolders = (InvoiceAdapter.ViewHolders) view.getTag();
            }
            viewHolders.tvNameRestaurant.setText("Restaurant: " + lstInvoices.get(position).getRestaurantModel().getFullName());
            viewHolders.tvDate.setText("Date Time: " + lstInvoices.get(position).getDate());
            viewHolders.tvPayMethod.setText("Payment Method: " + lstInvoices.get(position).getPayMethod());
            viewHolders.tvTotal.setText("Total: " + StringFormatUtils.convertToStringMoneyVND(lstInvoices.get(position).getTotal()));

            return view;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
