package de.sidanner.newsalarm;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by simon on 3/27/16.
 */
public class AlarmSettings implements Parcelable {

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public AlarmSettings createFromParcel(Parcel in) {
            return new AlarmSettings(in);
        }

        public AlarmSettings[] newArray(int size) {
            return new AlarmSettings[size];
        }
    };
    public ArrayList<Podcast> toListen;


    public AlarmSettings(Collection<Podcast> toListenPodcasts) {
        toListen = new ArrayList<>(toListenPodcasts);
    }

    public AlarmSettings(Parcel in) {
        toListen = new ArrayList<>();
        in.readList(toListen, Podcast.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(toListen);
    }
}
