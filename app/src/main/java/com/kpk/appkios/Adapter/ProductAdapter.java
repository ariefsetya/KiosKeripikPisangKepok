package com.kpk.appkios.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.kpk.appkios.AppConfig.AppController;
import com.kpk.appkios.AppConfig.Connector;
import com.kpk.appkios.Model.Products;
import com.kpk.appkios.R;

import io.realm.RealmList;

/**
 * Created by AnonymousX on 09/12/2016.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    RealmList<Products> products = new RealmList<>();
    Context ctx ;
    private ListenerInterface myListenerInterface;
    public ProductAdapter(Context ctx, ListenerInterface myListenerInterface){
        this.ctx = ctx;
        this.myListenerInterface = myListenerInterface;
    }

    public void updateAdapter(RealmList<Products> products){
        this.products = products;
        this.notifyDataSetChanged();
    }

    @Override
    public ProductAdapter.ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product,parent,false);
        ProductViewHolder pvh = new ProductViewHolder(vItem);
        return pvh;
    }

    public interface ListenerInterface {
        void onOrderTapped(Products products);
    }

    @Override
    public void onBindViewHolder(final ProductAdapter.ProductViewHolder holder, int position) {
        Products product = products.get(position);
        holder.productName.setText(product.getNama());
//        holder.productWeight.setText(product.getBerat()+product.getUnit());
        if (product.getGambar()!= null) {
            Glide.with(ctx).load(Connector.getInstance().imageURI+product.getGambar()).asBitmap()
                    .into(new BitmapImageViewTarget(holder.productImage){
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(ctx.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    holder.productImage.setImageDrawable(circularBitmapDrawable);
                    holder.progressBar.setVisibility(View.GONE);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if(products==null) {
            return 0;
        }else{
            return products.size();
        }
    }
    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout productLinearLayout;
        ProgressBar progressBar;
        ImageView productImage;
        TextView productName;//,productWeight;
        ProductViewHolder(View itemView) {
            super(itemView);
            productLinearLayout = (RelativeLayout)itemView.findViewById(R.id.productLinearLayout);
            productImage = (ImageView)itemView.findViewById(R.id.productImage);
            progressBar = (ProgressBar)itemView.findViewById(R.id.progressBar);
            productName = (TextView)itemView.findViewById(R.id.productName);
//            productWeight = (TextView)itemView.findViewById(R.id.productWeight);
        }
    }
}
