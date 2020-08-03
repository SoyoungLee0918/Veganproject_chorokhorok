package org.techtown.veganproject.ui.map;

public class bookmark_data {


// 다이어리 기본 정보 관리



    //PK
    private int _id;
    private String name;
    private Double lat;
    private Double lon;
    public bookmark_data(){

    }

    public bookmark_data(String name, double lat, double lon) {
        this._id=_id;
        this.name=name;
        this.lat=lat;
        this.lon=lon;
    }

    /*public bookmark_data(String name, Double lat, Double lon){
        this.name=name;
        this.lat=lat;
        this.lon=lon;
    }*/

    public int get_ID(){ return _id; }
    public String getName(){ return name;}
    public Double getLat(){ return lat;}
    public Double getLon(){ return lon;}


    public void set_ID(int _id){ this._id = _id;}
    public void setName(String name){ this.name = name;}
    public void setLat(Double lat){this.lat = lat;}
    public void setLon(Double lon){this.lon = lon;}

}
