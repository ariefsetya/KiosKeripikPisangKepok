package com.kpk.appkios.Model;

import org.json.JSONObject;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by AnonymousX on 09/12/2016.
 */

public class Products extends RealmObject {
    @PrimaryKey // Name is not nullable
    private String id;
    private String nama;
    private String harga;
    private String gambar;
    private String berat;
    private String unit;
    private String komposisi;

    public Products(){
    }

    public Products(JSONObject jsonObj) {
        try {
            if (jsonObj.has("id")){
                setId(jsonObj.getString("id"));
            }
            if (jsonObj.has("ingredients")){
                setKomposisi(jsonObj.getString("ingredients"));
            }
            if (jsonObj.has("display_name")){
                setNama(jsonObj.getString("display_name"));
            }
            if (jsonObj.has("price")){
                setHarga(jsonObj.getString("price"));
            }
            if (jsonObj.has("weight")){
                setBerat(jsonObj.getString("weight"));
            }
            if (jsonObj.has("units")){
                setUnit(jsonObj.getString("units"));
            }
            if (jsonObj.has("image")){
                setGambar(jsonObj.getString("image"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public String getBerat() {
        return berat;
    }

    public void setBerat(String berat) {
        this.berat = berat;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getKomposisi() {
        return komposisi;
    }

    public void setKomposisi(String komposisi) {
        this.komposisi = komposisi;
    }
}
