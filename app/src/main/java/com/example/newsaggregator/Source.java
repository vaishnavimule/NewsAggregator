package com.example.newsaggregator;

import android.os.Parcel;
import android.os.Parcelable;

public class Source implements Parcelable {
    public String id;
    public String name;
    public String category;

    public Source(String id, String name, String category) {
        this.id = id;
        this.name = name;
        this.category = category;
    }

    protected Source(Parcel in) {
        id = in.readString();
        name = in.readString();
        category = in.readString();
    }

    public static final Creator<Source> CREATOR = new Creator<Source>() {
        @Override
        public Source createFromParcel(Parcel in) {
            return new Source(in);
        }

        @Override
        public Source[] newArray(int size) {
            return new Source[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(category);
    }
}
