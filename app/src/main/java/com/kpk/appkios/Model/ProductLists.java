package com.kpk.appkios.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by AnonymousX on 09/12/2016.
 */

public class ProductLists extends RealmObject {

    @PrimaryKey // Name is not nullable
    private String id;
    private RealmList<Products> listProduct = new RealmList<>();
    public ProductLists(){

    }

    public ProductLists(JSONArray jsonArray){
        setId("0");
        try{
            RealmList<Products> products = new RealmList<>();
            for(int i = 0;i<jsonArray.length();i++){
                JSONObject productJSONObject = jsonArray.getJSONObject(i);
                Products product = new Products(productJSONObject);
                products.add(product);
            }
            setListProduct(products);
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public RealmList<Products> getListProduct() {
        return listProduct;
    }

    public void setListProduct(RealmList<Products> listProduct) {
        this.listProduct = listProduct;
    }
}
