package com.himedia.androidshoppingmall.Recycler;
/*
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.himedia.androidshoppingmall.Data.ProductBean;
import com.himedia.androidshoppingmall.R;

import java.util.ArrayList;

public class ShopRecyclerAdapter extends RecyclerView.Adapter<ShopRecyclerAdapter.ViewHolder> {

    private ArrayList<ProductBean> data;
    private ItemClickListener listener;
    ArrayList<ProductBean> items = new ArrayList();

    public ShopRecyclerAdapter(ArrayList<ProductBean> data, ItemClickListener listener) {
        this.data = data;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ShopRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.activity_shop_fragment, viewGroup, false);


        return new ProductAdapter.ViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopRecyclerAdapter.ViewHolder viewHolder, int position) {
        ProductBean item = items.get(position);
        viewHolder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(ProductBean item) {
        items.add(item);
    }

    public void setItems(ArrayList<ProductBean> items) {
        this.items = items;
    }

    public ProductBean getItem(int position) {
        return items.get(position);
    }

    public void setItem(int position, ProductBean item) {
        items.set(position, item);
    }

    public void setOnItemClickListener(OnShopItemClickListener listener) {
        this.listener = listener;
    });

    public void onItemClick(ShopRecyclerAdapter.ViewHolder holder, View view, int position) {
        if (listener != null) {
            listener.onItemClick(holder, view, position);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView1;
        TextView textView2;
        TextView textView3;
        TextView textView4;

        ImageView imageView1;

        public ViewHolder(View itemView, OnCartItemClickListener listener) {    // final OnProdu ....
            super(itemView);

            textView1 = itemView.findViewById(R.id.textView1);
            textView2 = itemView.findViewById(R.id.textView2);
            textView3 = itemView.findViewById(R.id.textView3);
            textView4 = itemView.findViewById(R.id.textView4);

            imageView1 = itemView.findViewById(R.id.imageView1);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

                    if (listener != null) {
                        //           listener.onItemClick(ShopRecyclerAdapter.ViewHolder.this, view, position);
                    }
                }
            });
        }

        public void setItem(ProductBean item) {
            textView1.setText(String.valueOf(item.getGoods_id())); // goods_id
            textView2.setText(item.getGoods_title());                // goods_title
            textView4.setText(String.valueOf(item.getGoods_price()));     // goods_price
            Glide.with(itemView.getContext()).load(item.getImageRes()).into(imageView1);
            //imageView1.setImageBitmap(.parseInt(String.valueOf(item.getImageRes())));
            //imageView1.setImageResource(Integer.parseInt(String.valueOf(item.getImageRes())));
        }

    }

}

 */

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.himedia.androidshoppingmall.Data.ProductBean;
import com.himedia.androidshoppingmall.R;

import java.util.ArrayList;

public class ShopRecyclerAdapter extends RecyclerView.Adapter<ShopRecyclerAdapter.ViewHolder> {
    ArrayList<ProductBean> items = new ArrayList();

    OnProductItemClickListener listener;

    public ShopRecyclerAdapter(ArrayList<ProductBean> items, ItemClickListener listener){
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_item_card, viewGroup, false);
        return new ViewHolder(view, i);
    }


    public void onBindViewHolder(@NonNull ShopRecyclerAdapter.ViewHolder viewHolder, int position) {
        ProductBean item = items.get(position);
        viewHolder.setItem(item);
    }

    @Override
    public int getItemCount() {
        if(items == null)
            return 0;
        else
            return items.size();
    }

    public void updateData(ArrayList<ProductBean> data){
        this.items = data;
        notifyDataSetChanged();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView1;
        TextView textView2;
        TextView textView3;
        TextView textView4;

        ImageView imageView1;

        public ViewHolder(View itemView, int listener) {    // final OnProdu ....
            super(itemView);

            textView1 = itemView.findViewById(R.id.textView1);
            textView2 = itemView.findViewById(R.id.textView2);
            textView3 = itemView.findViewById(R.id.textView3);
            textView4 = itemView.findViewById(R.id.textView4);

            imageView1 = itemView.findViewById(R.id.imageView1);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
/* 오류로 주석처리 by Dean
                    if (listener != null) {
                        listener.OnItemClick(ProductAdapter.ViewHolder.this, view, position);
                    }
                    
 */
                }
            });
        }

        public void setItem(ProductBean item) {
            textView1.setText(String.valueOf(item.getGoods_id())); // goods_id
            textView2.setText(item.getGoods_title());                // goods_title
            textView4.setText(String.valueOf(item.getGoods_price()));     // goods_price
            Glide.with(itemView.getContext()).load(item.getImageRes()).into(imageView1);
            //imageView1.setImageBitmap(.parseInt(String.valueOf(item.getImageRes())));
            //imageView1.setImageResource(Integer.parseInt(String.valueOf(item.getImageRes())));
        }

    }
}