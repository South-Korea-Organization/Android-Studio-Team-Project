package com.himedia.androidshoppingmall.Fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import com.himedia.androidshoppingmall.Data.ProductBean;
import com.himedia.androidshoppingmall.Data.ProductDBHelper;
import com.himedia.androidshoppingmall.R;
import com.himedia.androidshoppingmall.Recycler.ItemClickListener;
import com.himedia.androidshoppingmall.Recycler.SelectRecyclerAdapter;
import com.himedia.androidshoppingmall.Recycler.ShopRecyclerAdapter;

public class ShopFragment extends Fragment implements ItemClickListener {
    private static final String TYPE_001 = "화환";
    private static final String TYPE_002 = "관상식물";
    private static final String TYPE_003 = "기능성식물";

    private static final String TYPE_004 = "꽃배달서비스";

    private static final String TYPE_005 = "부가제품";

    private View view;
    private RecyclerView recyclerView;
    private SelectRecyclerAdapter tAdapter;
    private String[] tData;
    private ShopRecyclerAdapter pAdapter;
    private ArrayList<ProductBean> pData;
    private ProductDBHelper dbHelper;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_shop_fragment, container, false);

        showTypeSelecter();
        showProduct();

        return view;
    }

    private void showTypeSelecter() {
        tData = getContext().getResources().getStringArray(R.array.type);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView = view.findViewById(R.id.typeSelectRecycler);
        recyclerView.setLayoutManager(layoutManager);
        tAdapter = new SelectRecyclerAdapter(tData, this);
        recyclerView.setAdapter(tAdapter);
    }

    private void showProduct() {
        dbHelper = ProductDBHelper.getInstance(getContext());
        pData = dbHelper.getAllProduct();

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView = view.findViewById(R.id.productRecycler);
        recyclerView.setLayoutManager(layoutManager);
        pAdapter = new ShopRecyclerAdapter(pData, this);
        recyclerView.setAdapter(pAdapter);
    }

    private void showProduct(String type) {
        pData.clear();
        pData = dbHelper.getProductbyType(type);
        pAdapter.updateData(pData);
    }

    @Override
    public void onItemClick(View v, int position) {
        String type = String.valueOf(((TextView)(v.findViewById(R.id.typeSelectTv))).getText());

        if(type.equals(TYPE_001)) {
            showProduct(type);
        } else if(type.equals(TYPE_002)) {
            showProduct(type);
        } else if(type.equals(TYPE_003)) {
            showProduct(type);
        } else if(type.equals(TYPE_004)) {
            showProduct(type);
        } else if(type.equals(TYPE_005)) {
            showProduct(type);
        }
    }
}