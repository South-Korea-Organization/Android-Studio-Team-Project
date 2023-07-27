package com.himedia.androidshoppingmall.Fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.himedia.androidshoppingmall.Data.ProductBean;
import com.himedia.androidshoppingmall.R;
import com.himedia.androidshoppingmall.Recycler.SelectRecyclerAdapter;
import com.himedia.androidshoppingmall.Recycler.ShopRecyclerAdapter;

import java.util.ArrayList;

public class ShopFragment extends Fragment implements AdapterView.OnItemClickListener {
    private static final String SERVER_URL = "http://3.37.214.236:8080";
    private static final String TYPE_TOP = "Top";
    private static final String TYPE_BOTTOM = "Bottom";
    private static final String TYPE_ACC = "Acc";

    private View view;
    private RecyclerView recyclerView;
    private SelectRecyclerAdapter tAdapter;
    private String[] tData;
    private ShopRecyclerAdapter pAdapter;
    private ArrayList<ProductBean> pData;

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
   //        tAdapter = new SelectRecyclerAdapter(tData,listener);  // 주석처리 by Dean 오류
        recyclerView.setAdapter(tAdapter);
    }

    private void showProduct() {
//        dbHelper = ProductDBHelper.getInstance(getContext());
//        pData = dbHelper.getAllProduct();

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView = view.findViewById(R.id.productRecycler);
        recyclerView.setLayoutManager(layoutManager);
    //    pAdapter = new ShopRecyclerAdapter(pData, );
        recyclerView.setAdapter(pAdapter);
    }

    private void showProduct(String type) {
        pData.clear();
  //      pData = dbHelper.getProductbyType(type);
  //      pAdapter.updateData(pData);
    }

    public void onItemClick(View v, int position) {
        String type = String.valueOf(((TextView)(v.findViewById(R.id.typeSelectTv))).getText());

        if(type.equals(TYPE_TOP)) {
            showProduct(type);
        } else if(type.equals(TYPE_BOTTOM)) {
            showProduct(type);
        } else if(type.equals(TYPE_ACC)) {
            showProduct(type);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //
    }

}
