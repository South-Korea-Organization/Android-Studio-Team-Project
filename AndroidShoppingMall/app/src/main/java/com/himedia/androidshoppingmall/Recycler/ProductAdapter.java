package com.himedia.androidshoppingmall.Recycler;

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

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder>
                            implements OnProductItemClickListener {
    ArrayList<ProductBean> items = new ArrayList();

    OnProductItemClickListener listener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.product_item, viewGroup, false);

        return new ViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
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

    public void setOnItemClickListener(OnProductItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onItemClick(ViewHolder holder, View view, int position) {
        if (listener != null) {
            listener.onItemClick(holder, view, position);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView1;
        TextView textView2;
        TextView textView3;
        TextView textView4;

        ImageView imageView1;

        public ViewHolder(View itemView, OnProductItemClickListener listener) {    // final OnProdu ....
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
                        listener.onItemClick(ViewHolder.this, view, position);
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
