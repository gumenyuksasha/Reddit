package com.olhu.redditclient.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class HomeInstanceState implements Parcelable {
    public static final String TAG = "HomeInstanceState";
    public static final Creator<HomeInstanceState> CREATOR = new Creator<HomeInstanceState>() {
        @Override
        public HomeInstanceState createFromParcel(Parcel in) {
            return new HomeInstanceState(in);
        }

        @Override
        public HomeInstanceState[] newArray(int size) {
            return new HomeInstanceState[size];
        }
    };
    private int currentPage;
    private List<Topic> topics;

    private HomeInstanceState(Parcel in) {
        currentPage = in.readInt();
        topics = in.createTypedArrayList(Topic.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flag) {
        parcel.writeInt(currentPage);
        parcel.writeList(topics);
    }
}
