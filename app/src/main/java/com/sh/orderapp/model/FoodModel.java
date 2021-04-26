package com.sh.orderapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FoodModel implements Serializable, Parcelable {

    private String id;
    private String name;
    private String image;
    private int nowQty;
    private int price;
    private UserModel userModel;

    protected FoodModel(Parcel in) {
        id = in.readString();
        name = in.readString();
        image = in.readString();
        nowQty = in.readInt();
        price = in.readInt();
        userModel = in.readParcelable(UserModel.class.getClassLoader());
    }

    public static final Creator<FoodModel> CREATOR = new Creator<FoodModel>() {
        @Override
        public FoodModel createFromParcel(Parcel in) {
            return new FoodModel(in);
        }

        @Override
        public FoodModel[] newArray(int size) {
            return new FoodModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(image);
        dest.writeInt(nowQty);
        dest.writeInt(price);
        dest.writeParcelable(userModel, flags);
    }
}
