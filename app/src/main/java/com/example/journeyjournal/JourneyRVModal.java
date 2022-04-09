package com.example.journeyjournal;

import android.os.Parcel;
import android.os.Parcelable;

import kotlin.reflect.KParameter;

public class JourneyRVModal implements Parcelable {
//    declare a variable
    private String placeName;
    private String placeDescription;
    private String journeyId;
    private String journeyImage = "";

//    create a empty constructor
    public JourneyRVModal(){
//        Empty constructor
    };

    public JourneyRVModal(String placeName, String placeDescription, String journeyId, String journeyImage) {
        this.placeName = placeName;
        this.placeDescription = placeDescription;
        this.journeyId = journeyId;
        this.journeyImage = journeyImage;
    }

    //    Insert all the value pass as Parameter to declare variable
    public JourneyRVModal(String placeName, String placeDescription, String journeyId  ) {
        this.placeName = placeName;
        this.placeDescription = placeDescription;
        this.journeyId = journeyId;
    }
    //get the value from user as Parameter and read the value and store in firebase
    protected JourneyRVModal(Parcel in) {
        placeName = in.readString();
        placeDescription = in.readString();
        journeyId = in.readString();
        journeyImage = in.readString();
    }

    public static final Creator<JourneyRVModal> CREATOR = new Creator<JourneyRVModal>() {
        @Override
        public JourneyRVModal createFromParcel(Parcel in) {
            return new JourneyRVModal(in);
        }

        @Override
        public JourneyRVModal[] newArray(int size) {
            return new JourneyRVModal[size];
        }
    };

//    return a variable where value is insert
    public String getPlaceName() {
        return placeName;
    }
    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

//    return a variable where value is insert
    public String getPlaceDescription() {
        return placeDescription;
    }
    public void setPlaceDescription(String placeDescription) {
        this.placeDescription = placeDescription;
    }
//    return a variable where value is insert
    public String getJourneyId() {
        return journeyId;
    }
    public void setJourneyId(String journeyId) {
        this.journeyId = journeyId;
    }

    public String getJourneyImage() {
        return journeyImage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(placeName);
        parcel.writeString(placeDescription);
        parcel.writeString(journeyId);
        parcel.writeString(journeyImage);
    }
}
