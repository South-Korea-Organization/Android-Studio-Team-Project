package com.himedia.androidshoppingmall.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.himedia.androidshoppingmall.Data.ProductBean;
import com.himedia.androidshoppingmall.R;
import com.himedia.androidshoppingmall.Recycler.OnProductItemClickListener;
import com.himedia.androidshoppingmall.Recycler.ProductAdapter;
import com.himedia.androidshoppingmall.Request.ProductDetailRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

//import com.himedia.androidshoppingmall.Recycler.ProductAdapter;
public class HomeFragment extends Fragment {
    private static final String SERVER_URL = "http://3.37.214.236:8080";
    private static final int INTERVAL_TIME = 3800;  // 메인 슬라이드 이미지 시간 간격 3.8초

    private View view;
    private ViewFlipper viewFlipper;
    private Bundle bundle;

    private ImageView imageView;  // URL 이미지 가져오는 용으로 사용(TEST)

    RecyclerView recyclerView;
    ProductAdapter productAdapter;
    
 //   ProductDetailsFragment productDetailsFragment;   // 상품 상세페이지 처리 by Dean
    ProductDetailRequest productDetailRequest;    // 상품상세설명 json으로 데이터 가져오는 기능

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            bundle = savedInstanceState.getBundle("data");
        } else {
            bundle = getArguments();
        }
    }

    // 메인. 슬라이드 형식 화면 절반치 광고, 아래에 상품 6개 정도 보여주기
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_home_fragment, container, false);
        viewFlipper = view.findViewById(R.id.imageSlide);

        imageView = view.findViewById(R.id.imageView);

        for ( int i = 1; i < 4; i++)
            flipperImagesFromUrl(i);   // 메인 슬라이드 광고 이미지 3개 보여주기

        showBestSeller(); // 베스트 셀러 상품 보여주기

        return view;
    }

    // URL 경로 이미지를 가져와서 출력하는 방식으로 변경 By Dean
    private void flipperImagesFromUrl(int idx) {
        String url = SERVER_URL + "/resources/image/banner"+idx+".png";  // banner2.png"
        ImageView imageView = new ImageView(getContext());
        Glide.with(this).load(url).into(imageView);

        viewFlipper.addView(imageView);
        viewFlipper.setFlipInterval(INTERVAL_TIME);
        viewFlipper.setAutoStart(true);

        viewFlipper.setInAnimation(getContext(), R.anim.slide_in_anim);
        viewFlipper.setOutAnimation(getContext(), R.anim.slide_out_anim);
    }

    //이전 버전 로컬경로에서 가져와서 이미지 출력방식 ( 현재 사용중지)
    private void flipperImages(int image) {
        ImageView imageView = new ImageView(getContext());
        imageView.setBackgroundResource(image);

        viewFlipper.addView(imageView);
        viewFlipper.setFlipInterval(INTERVAL_TIME);
        viewFlipper.setAutoStart(true);

        viewFlipper.setInAnimation(getContext(), R.anim.slide_in_anim);
        viewFlipper.setOutAnimation(getContext(), R.anim.slide_out_anim);
    }

    private void showBestSeller() {
        Bundle args = getArguments();
        ArrayList<String[]> data = (ArrayList<String[]>) args.getSerializable("data");

        String imgUrlPath = SERVER_URL + "/resources/image/file_repo/";
        recyclerView = view.findViewById(R.id.recyclerView);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        productAdapter = new ProductAdapter();

        for (int i = 0; i < data.size(); i++) {
            String[] row = data.get(i);
            String imgUrl  = imgUrlPath + row[0] + "/" + row[3];
            productAdapter.addItem(new ProductBean(row[0],row[1],row[2],imgUrl));
        }

        recyclerView.setAdapter(productAdapter);
        productAdapter.setOnItemClickListener(new OnProductItemClickListener() {
            @Override
            public void onItemClick(ProductAdapter.ViewHolder holder, View view, int position) {
                ProductBean item = productAdapter.getItem(position);
                Toast.makeText(getContext(), "선택된 제품 : " + item.getGoods_title(), Toast.LENGTH_LONG).show();
                String goods_id = String.valueOf (item.getGoods_id());

                showProductDetail(goods_id); /* 상품상세페이지 */
            }
        });
    }

    /* 상품상세페이지 */

    public void showProductDetail(String goods_id) {
        Bundle bundle = new Bundle();
        ArrayList<String[]> data = new ArrayList<>();
        //EditText에 현재 입력되어 있는 값을 가져온다.
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // activity/fragment -> fragment 값 전달
                try {
                    JSONArray jsonArray = new JSONArray(response);   // 여러개의 RECORDS 가져옴

                    for(int i = 0; i < jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String result = jsonObject.getString("success");  // true/false

                        if ( result == "true"  ) {
                            String goods_id = jsonObject.getString("goods_id");       // 상품코드
                            String goods_title = jsonObject.getString("goods_title");    // 상품명
                            String goods_price = jsonObject.getString("goods_price");  // 상품가격

                            // 숫자에 콤마 추가
                            String goods_price_comma = String.format(Locale.US, "%,d", Integer.parseInt(goods_price));

                            String fileName = jsonObject.getString("fileName");        // 상품이미지명

                            if (fileName != null) {
                                String[] row = new String[4];
                                row[0] = goods_id;
                                row[1] = goods_title;
                                row[2] = goods_price_comma;
                                row[3] = fileName;
                                data.add(row);
                            }

                        }
                    }
                    //   Toast.makeText(getApplicationContext(), "실서버 접속에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                    bundle.putStringArrayList("data", (ArrayList) data);

                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    ProductDetailsFragment productDetailsFragment = new ProductDetailsFragment();
                    productDetailsFragment.setArguments(bundle);
                    transaction.replace(R.id.frameLayout, productDetailsFragment).commit();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        ProductDetailRequest productDetailRequest  = new ProductDetailRequest(goods_id, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(productDetailRequest);
    }

}