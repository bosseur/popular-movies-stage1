package br.bosseur.popuplarmoviesapp.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ekoetsier on 28/02/2018.
 */

public class Trailer implements Parcelable {

  public static final Parcelable.Creator<Trailer> CREATOR
      = new Parcelable.Creator<Trailer>() {

    public Trailer createFromParcel(Parcel in) {
      return new Trailer(in);
    }

    public Trailer[] newArray(int size) {
      return new Trailer[size];
    }
  };

  private String id;
  private String key;
  private String name;
  private String site;
  private int size;
  private String trailer;

  public Trailer(String id, String key, String name, String site, int size, String trailer) {
    this.id = id;
    this.key = key;
    this.name = name;
    this.site = site;
    this.size = size;
    this.trailer = trailer;
  }

  public Trailer(Parcel in) {
    this.id = in.readString();
    this.key = in.readString();
    this.name = in.readString();
    this.site = in.readString();
    this.size = in.readInt();
    this.trailer = in.readString();
  }

  public String getId() {
    return id;
  }

  public String getKey() {
    return key;
  }

  public String getName() {
    return name;
  }

  public String getSite() {
    return site;
  }

  public int getSize() {
    return size;
  }

  public String getTrailer() {
    return trailer;
  }

  @Override
  public String toString() {
    return "Trailer{" +
        "id='" + id + '\'' +
        ", key='" + key + '\'' +
        ", name='" + name + '\'' +
        ", site='" + site + '\'' +
        ", size=" + size +
        ", trailer='" + trailer + '\'' +
        '}';
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel parcel, int i) {
    parcel.writeString(this.id);
    parcel.writeString(this.key);
    parcel.writeString(this.name);
    parcel.writeString(this.site);
    parcel.writeInt(this.size);
    parcel.writeString(this.trailer);
  }
}
