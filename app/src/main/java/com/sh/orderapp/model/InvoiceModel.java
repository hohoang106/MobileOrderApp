package com.sh.orderapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceModel implements Serializable, Parcelable {
    private int total;
    private String date;
    private String month;
    private String yearly;
    private UserModel user;
    private String payMethod;
    private String dateRutGon;
    private List<FoodModel> listFoods;
    private UserModel restaurantModel;

    protected InvoiceModel(Parcel in) {
        total = in.readInt();
        date = in.readString();
        month = in.readString();
        yearly = in.readString();
        user = in.readParcelable(UserModel.class.getClassLoader());
        payMethod = in.readString();
        dateRutGon = in.readString();
        listFoods = in.createTypedArrayList(FoodModel.CREATOR);
        restaurantModel = in.readParcelable(UserModel.class.getClassLoader());
    }

    public static final Creator<InvoiceModel> CREATOR = new Creator<InvoiceModel>() {
        @Override
        public InvoiceModel createFromParcel(Parcel in) {
            return new InvoiceModel(in);
        }

        @Override
        public InvoiceModel[] newArray(int size) {
            return new InvoiceModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(total);
        dest.writeString(date);
        dest.writeString(month);
        dest.writeString(yearly);
        dest.writeParcelable(user, flags);
        dest.writeString(payMethod);
        dest.writeString(dateRutGon);
        dest.writeTypedList(listFoods);
        dest.writeParcelable(restaurantModel, flags);
    }
}
