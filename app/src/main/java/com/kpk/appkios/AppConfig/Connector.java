package com.kpk.appkios.AppConfig;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.kpk.appkios.BuildConfig;
import com.kpk.appkios.Model.ProductLists;
import com.kpk.appkios.Model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;

/**
 * Created by AnonymousX on 04/12/2016.
 */

public class Connector {

    private static Connector ourInstance = new Connector();

    public static Connector getInstance() {
        return ourInstance;
    }

    public String url = "http://192.168.1.125:8000/api";
    public String imageURI = "http://192.168.1.125:8000/images/";

    public static String eventSignInWithEmail = "SignInWithEmail";
    public static String eventProfile = "Profile";
    public static String eventProductList = "ProductList";
    public static String message = "Message";
    public static String responseNoInternet = "noInternet";
    public static String responseSuccess = "Success";
    public static String responseFail = "Fail";
    public static String responseInvalidToken = "422 Invalid Token";
    public static String responseInvalidEmailOrPassword = "403 Forbidden";
    public static String responseInvalidFormat = "422";

    Realm realm;
    private RequestQueue mRequestQueue;
    public static final String TAG = Connector.class.getSimpleName();

    private Connector() {
        realm = Realm.getDefaultInstance();
    }
    public boolean hasInternet()
    {
        try
        {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager)AppController.getInstance().getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        catch (Exception e)
        {
            return false;
        }
    }
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(AppController.getContext());
        }

        return mRequestQueue;
    }
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public void signinWithEmail(final String email, String password){
        if(!hasInternet()){
            Intent intent = new Intent(eventSignInWithEmail);
            intent.putExtra(message, responseNoInternet);
            LocalBroadcastManager.getInstance(AppController.getInstance().getContext()).sendBroadcast(intent);
            return;
        }
        JSONObject params = new JSONObject();
        try {
            params.put("email", email);
            params.put("password", password);
//            if(AppController.getInstance().key != null) {
//                params.put("hfPushID", AppController.getInstance().key);
//            }
//            params.put("version", BuildConfig.VERSION_NAME);
//            params.put("build",BuildConfig.VERSION_CODE);
        }catch (Exception e){

        }


        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url + "/auth/login",params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("TAGA",response.toString());
                        try {
                            if (response.getString("status").equals("ok")){
                                User user = new User(response.getJSONObject("auth"));
                                realm.beginTransaction();
                                realm.copyToRealmOrUpdate(user);
                                realm.commitTransaction();

                                AppController.getInstance().currentUser = user;
                                AppController.getInstance().setKey(response.getString("token"));
                                Intent intent = new Intent(eventSignInWithEmail);
                                intent.putExtra(message, responseSuccess);
                                LocalBroadcastManager.getInstance(AppController.getInstance().getContext()).sendBroadcast(intent);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse nr = error.networkResponse;
                Intent intent = new Intent(eventSignInWithEmail);
                if(nr!=null){
                    switch (nr.statusCode){
                        case 422:intent.putExtra(message, responseInvalidFormat);break;
                        case 403:intent.putExtra(message, responseInvalidEmailOrPassword);break;
                        default:intent.putExtra(message, responseFail);break;
                    }
                }else{
                    intent.putExtra(message, responseFail);
                }
                LocalBroadcastManager.getInstance(AppController.getInstance().getContext()).sendBroadcast(intent);
            }
        });

        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        addToRequestQueue(request,"signinWithEmail");
    }

    public void profile(){
        if(!hasInternet()){
            Intent intent = new Intent(eventProfile);
            intent.putExtra(message, responseNoInternet);
            LocalBroadcastManager.getInstance(AppController.getInstance().getContext()).sendBroadcast(intent);
            return;
        }
        JSONObject params = new JSONObject();
        try {

        }catch (Exception e){

        }


        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url + "/kios/profile?token="+AppController.getInstance().getKey(),params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("status").equals("ok")){
                                User user = new User(response.getJSONObject("auth"));
                                realm.beginTransaction();
                                realm.copyToRealmOrUpdate(user);
                                realm.commitTransaction();

                                AppController.getInstance().currentUser = user;
                                Intent intent = new Intent(eventProfile);
                                intent.putExtra(message, responseSuccess);
                                LocalBroadcastManager.getInstance(AppController.getInstance().getContext()).sendBroadcast(intent);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse nr = error.networkResponse;
                Intent intent = new Intent(eventProfile);
                if(nr!=null){
                    switch (nr.statusCode){
                        case 400:intent.putExtra(message, responseInvalidToken);break;
                        default:intent.putExtra(message, responseFail);break;
                    }
                }else{
                    intent.putExtra(message, responseFail);
                }
                LocalBroadcastManager.getInstance(AppController.getInstance().getContext()).sendBroadcast(intent);
            }
        });

        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        addToRequestQueue(request,"eventProfile");
    }

    public void productList(){
        if(!hasInternet()){
            Intent intent = new Intent(eventProductList);
            intent.putExtra(message, responseNoInternet);
            LocalBroadcastManager.getInstance(AppController.getInstance().getContext()).sendBroadcast(intent);
            return;
        }
        JSONObject params = new JSONObject();
        try {

        }catch (Exception e){

        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url + "/products?token="+AppController.getInstance().getKey(),params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("status").equals("ok")){
                                ProductLists productLists = new ProductLists(response.getJSONArray("data"));
                                realm.beginTransaction();
                                realm.copyToRealmOrUpdate(productLists);
                                realm.commitTransaction();
                                Intent intent = new Intent(eventProductList);
                                intent.putExtra(message, responseSuccess);
                                LocalBroadcastManager.getInstance(AppController.getInstance().getContext()).sendBroadcast(intent);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse nr = error.networkResponse;
                Intent intent = new Intent(eventProductList);
                if(nr!=null){
                    switch (nr.statusCode){
                        case 400:intent.putExtra(message, responseInvalidToken);break;
                        default:intent.putExtra(message, responseFail);break;
                    }
                }else{
                    intent.putExtra(message, responseFail);
                }
                LocalBroadcastManager.getInstance(AppController.getInstance().getContext()).sendBroadcast(intent);
            }
        });

        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        addToRequestQueue(request,"eventProductList");
    }
}
