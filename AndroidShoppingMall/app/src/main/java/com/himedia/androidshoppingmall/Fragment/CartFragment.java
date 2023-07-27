package com.himedia.androidshoppingmall.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.himedia.androidshoppingmall.Data.PreferenceManager;
import com.himedia.androidshoppingmall.Data.ProductBean;
import com.himedia.androidshoppingmall.R;
import com.himedia.androidshoppingmall.Recycler.CartRecycleAdapter;
import com.himedia.androidshoppingmall.Recycler.OnCartItemClickListener;

import java.util.ArrayList;
public class CartFragment extends Fragment {
    private static final String SERVER_URL = "http://3.37.214.236:8080";
    private View view;
    private Bundle bundle;

    RecyclerView recyclerView;
    CartRecycleAdapter cartRecycleAdapter;
    private PreferenceManager pManager;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pManager = new PreferenceManager();  // 로그인후 토큰이 만들어져 자동 로그인 기능

        if (savedInstanceState != null) {
            bundle = savedInstanceState.getBundle("data");
        } else {
            bundle = getArguments();
        }
    }

    // 메인. 슬라이드 형식 화면 절반치 광고, 아래에 상품 6개 정도 보여주기
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_cart_fragment, container, false);

        showCartList();
        return view;
    }

    public void showCartList()
    {
        String member_id = pManager.getString(getContext(), "member_id");
        Bundle args = getArguments();
        ArrayList<String[]> data = (ArrayList<String[]>) args.getSerializable("data");


        String imgUrlPath = SERVER_URL + "/resources/image/file_repo/";
        recyclerView = view.findViewById(R.id.recyclerView);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(layoutManager);
        cartRecycleAdapter = new CartRecycleAdapter();

        for (int i = 0; i < data.size(); i++) {
            String[] row = data.get(i);
            String imgUrl = imgUrlPath + row[0] + "/" + row[3];
            cartRecycleAdapter.addItem(new ProductBean(row[0], row[1], row[2], imgUrl));
        }

        recyclerView.setAdapter(cartRecycleAdapter);

        cartRecycleAdapter.setListener(new OnCartItemClickListener() {
            @Override
            public void onItemClick(CartRecycleAdapter.ViewHolder viewHolder, View view, int position) {
                ProductBean item = cartRecycleAdapter.getItem(position);
                Toast.makeText(getContext(), "선택된 제품 : " + item.getGoods_title(), Toast.LENGTH_LONG).show();
                String goods_id = String.valueOf(item.getGoods_id());

                //showProductDetail(goods_id); /* 상품상세페이지 *///      ProductDetailFragment 참조 필요
            }
        });
    }
/*
    private void showProductDetail(String goods_id) {
        Bundle args = getArguments();
        ArrayList<String[]> data = (ArrayList<String[]>) args.getSerializable("data");
        String imgUrlPath = SERVER_URL + "/resources/image/file_repo/";
        pManager = new PreferenceManager();  // 로그인후 토큰이 만들어져 자동 로그인 기능

        String member_id = pManager.getString(getContext(), "member_id");

        // 수정 시작
        imageViewOrign = view.findViewById(R.id.imageViewOrign);   // 제조사
        imageView1 = view.findViewById(R.id.imageView1);
        imageView2 = view.findViewById(R.id.imageView2);
        imageView3 = view.findViewById(R.id.imageView3);
        imageView4 = view.findViewById(R.id.imageView4);

        TextView textView2 = view.findViewById(R.id.textView2);
        TextView textView4 = view.findViewById(R.id.textView4);

        btnPayment = view.findViewById(R.id.btnPayment); // 결제하기
        btnCart = view.findViewById(R.id.btnCart);   // 장바구니 넣기
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(member_id.length() != 0) {
                    String goods_id = data.get(0)[0];  //EditText에 현재 입력되어 있는 값을 가져온다.

                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                //Register.php에서 $response["success"] = true; 값을 받음
                                boolean success = jsonObject.getBoolean("success");  // true, false 가져옴

                                if (success) {
                                    Toast.makeText(getContext(), "장바구니에 담았습니다.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getContext(), "장바구니에 담는과정에서 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    };

                    CartRequest cartRequest = new CartRequest(goods_id, member_id, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(getContext());
                    queue.add(cartRequest);
                }
                else{
                    Toast.makeText(getContext(), "서버와 연결이 끝어졌습니다. 다시 로그인 해주세요.", Toast.LENGTH_SHORT).show();
                    //  Fragment -> Activity 이동 : MyFragment ->  MainActivity 로 넘어가게 한다.
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
        //장바구니에 담기 시작

        //    recyclerView = view.findViewById(R.id.recyclerView);

        //    GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        //    recyclerView.setLayoutManager(layoutManager);  // 오류
        //    productDetailAdapter = new ProductDetailAdapter();


        for (int i = 0; i < data.size(); i++) {
            String[] row = data.get(i);
            String imgUrl  = imgUrlPath + row[0] + "/" + row[3];

            textView2.setText(row[1]);
            textView4.setText(row[2]);

            switch( i )
            {
                case 0:
                    Glide.with(view.getContext()).load(imgUrl).into(imageView1);
                    break;
                case 1:
                    Glide.with(view.getContext()).load(imgUrl).into(imageView2);
                    break;
                case 2:
                    Glide.with(view.getContext()).load(imgUrl).into(imageView3);
                    break;
                case 3:
                    Glide.with(view.getContext()).load(imgUrl).into(imageView4);
                    break;
            }
            //       productDetailAdapter.addItem(new ProductDetailBean(imgUrl));
            imgUrl = SERVER_URL + "/resources/image/file_repo/500/원산지.png";
            Glide.with(view.getContext()).load(imgUrl).into(imageViewOrign);
        }
        // 폼 교체 반복되는 이미지(상품 상세페이지 )
        // 폼 교체 반복 종료
    }
    */
}
