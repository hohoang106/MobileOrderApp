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
public class NewsModel implements Serializable, Parcelable {

    private String id;
    private String name;
    private String content;
    private String description;
    private UserModel userModel;


    protected NewsModel(Parcel in) {
        id = in.readString();
        name = in.readString();
        content = in.readString();
        description = in.readString();
        userModel = in.readParcelable(UserModel.class.getClassLoader());
    }

    public static final Creator<NewsModel> CREATOR = new Creator<NewsModel>() {
        @Override
        public NewsModel createFromParcel(Parcel in) {
            return new NewsModel(in);
        }

        @Override
        public NewsModel[] newArray(int size) {
            return new NewsModel[size];
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
        dest.writeString(content);
        dest.writeString(description);
        dest.writeParcelable(userModel, flags);
    }
}
