package com.kpk.appkios.Model;

import org.json.JSONObject;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by AnonymousX on 06/12/2016.
 */
public class User extends RealmObject {
    @PrimaryKey // Name is not nullable
    private String id;
    private String email;
    private String name;
    private String key;
    private String phone;
    public User(){

    }

    public User(JSONObject jsonObj) {
        try {
            if (jsonObj.has("id")){
                setId(jsonObj.getString("id"));
            }
            if (jsonObj.has("email")){
                setEmail(jsonObj.getString("email"));
            }
            if (jsonObj.has("name")){
                setName(jsonObj.getString("name"));
            }
            if (jsonObj.has("phone")){
                setPhone(jsonObj.getString("phone"));
            }
            if (jsonObj.has("key")){
                setPhone(jsonObj.getString("key"));
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
