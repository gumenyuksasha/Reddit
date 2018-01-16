package com.olhu.redditclient.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class Topic implements Parcelable {
    public static final Creator<Topic> CREATOR = new Creator<Topic>() {
        @Override
        public Topic createFromParcel(Parcel in) {
            return new Topic(in);
        }

        @Override
        public Topic[] newArray(int size) {
            return new Topic[size];
        }
    };
    private static final long HOURS_SPAN = 60 * 60 * 1000L;
    private String title;
    private String author;
    private String name;
    private int commentsNumber;
    private Image sourceImage;
    private Image thumbnail;
    private long createdUTC;

    private Topic(Parcel in) {
        title = in.readString();
        author = in.readString();
        name = in.readString();
        commentsNumber = in.readInt();
        createdUTC = in.readLong();
        sourceImage = in.readParcelable(Image.class.getClassLoader());
        thumbnail = in.readParcelable(Image.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(author);
        dest.writeString(name);
        dest.writeInt(commentsNumber);
        dest.writeLong(createdUTC);
        dest.writeParcelable(sourceImage, flags);
        dest.writeParcelable(thumbnail, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public long getHoursElapsed() {
        return (Calendar.getInstance().getTimeInMillis() - createdUTC) / HOURS_SPAN;
    }

}
