package com.himedia.androidshoppingmall.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import com.himedia.androidshoppingmall.Data.ProductBean;
import com.himedia.androidshoppingmall.Data.ProductDBHelper;
import com.himedia.androidshoppingmall.R;
import com.himedia.androidshoppingmall.Recycler.CartRecyclerAdapter;
import com.himedia.androidshoppingmall.Recycler.ItemClickListener;

public class CartFragment extends Fragment implements ItemClickListener {
    private View view;
    private RecyclerView recyclerView;
    private CartRecyclerAdapter adapter;
    private ArrayList<ProductBean> data;
    private ProductDBHelper dbHelper;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_cart_fragment, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showProduct();
    }

    private void showProduct() {
        dbHelper = ProductDBHelper.getInstance(getContext());
        data = dbHelper.getAllProduct();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CartRecyclerAdapter(data, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View v, int position) {

    }
}
