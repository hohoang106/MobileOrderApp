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
public class UserModel implements Serializable, Parcelable {

    private String username;
    private String password;
    private String fullName;
    private String address;
    private String email;
    private String phone;
    private String linkImage;
    private String permission;
    private String status;

    protected UserModel(Parcel in) {
        username = in.readString();
        password = in.readString();
        fullName = in.readString();
        address = in.readString();
        email = in.readString();
        linkImage = in.readString();
        phone = in.readString();
        permission = in.readString();
        status = in.readString();
    }

    public static final Creator<UserModel> CREATOR = new Creator<UserModel>() {
        @Override
        public UserModel createFromParcel(Parcel in) {
            return new UserModel(in);
        }

        @Override
        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeString(password);
        dest.writeString(fullName);
        dest.writeString(address);
        dest.writeString(email);
        dest.writeString(phone);
        dest.writeString(linkImage);
        dest.writeString(permission);
        dest.writeString(status);
    }
}
