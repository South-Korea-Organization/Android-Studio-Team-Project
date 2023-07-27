package com.himedia.androidshoppingmall.Recycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.himedia.androidshoppingmall.Data.ProductDetailBean;
import com.himedia.androidshoppingmall.R;

import java.util.ArrayList;

public class ProductDetailAdapter extends RecyclerView.Adapter<ProductDetailAdapter.ViewHolder>
        implements OnProductItemClickListener {

    ArrayList<ProductDetailBean> items = new ArrayList();

    OnProductItemClickListener listener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        View itemView = inflater.inflate(R.layout.fragment_product_details, viewGroup, false);

        return new ViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        ProductDetailBean item = items.get(position);
        viewHolder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(ProductDetailBean item) {
        items.add(item);
    }

    public void setItems(ArrayList<ProductDetailBean> items) {
        this.items = items;
    }

    public ProductDetailBean getItem(int position) {
        return items.get(position);
    }

    public void setItem(int position, ProductDetailBean item) {
        items.set(position, item);
    }

    public void onProductDetailItemClick(int position) {
        // Do something when the item is clicked
    }

    @Override
    public void onItemClick(ProductAdapter.ViewHolder holder, View view, int position) {

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView1;

        public ViewHolder(View itemView, final OnProductItemClickListener listener) {
            super(itemView);

            imageView1 = itemView.findViewById(R.id.imageView1);
            // Set the listener for the item click
        }

        public void setItem(ProductDetailBean item) {
            Glide.with(itemView.getContext()).load(item.getImageRes()).into(imageView1);
        }

    }

}