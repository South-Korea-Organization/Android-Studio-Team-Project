package com.himedia.androidshoppingmall.Recycler;

import android.view.View;

public interface ItemClickListener extends OnProductItemClickListener {
    @Override
    void onItemClick(ProductAdapter.ViewHolder holder, View view, int position);
}
