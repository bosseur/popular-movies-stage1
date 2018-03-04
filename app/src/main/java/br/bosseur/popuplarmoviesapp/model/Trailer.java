package br.bosseur.popuplarmoviesapp.model;

/**
 * Created by ekoetsier on 28/02/2018.
 */

public class Trailer {
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
}
