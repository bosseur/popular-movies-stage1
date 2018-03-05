package br.bosseur.popuplarmoviesapp.model;


import android.os.Parcel;
import android.os.Parcelable;

public class Review implements Parcelable {

  public static final Parcelable.Creator<Review> CREATOR
      = new Parcelable.Creator<Review>() {

    public Review createFromParcel(Parcel in) {
      return new Review(in);
    }

    public Review[] newArray(int size) {
      return new Review[size];
    }
  };


  public Review(String author, String content, String id, String url) {
    this.author = author;
    this.content = content;
    this.id = id;
    this.url = url;
  }

  private String author;
  private String content;
  private String id;
  private String url;

  public Review(Parcel in) {
    this.author = in.readString();
    this.content = in.readString();
    this.id = in.readString();
    this.url = in.readString();
  }

  public String getAuthor() {
    return author;
  }

  public String getContent() {
    return content;
  }

  public String getId() {
    return id;
  }

  public String getUrl() {
    return url;
  }

  @Override
  public String toString() {
    return "Review{" +
        "author='" + author + '\'' +
        ", content='" + content + '\'' +
        ", id='" + id + '\'' +
        ", url='" + url + '\'' +
        '}';
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel parcel, int i) {
    parcel.writeString(this.author);
    parcel.writeString(this.content);
    parcel.writeString(this.id);
    parcel.writeString(this.url);
  }
}
