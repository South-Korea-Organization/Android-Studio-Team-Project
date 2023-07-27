package com.himedia.androidshoppingmall;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.himedia.androidshoppingmall.Data.PreferenceManager;
import com.himedia.androidshoppingmall.Fragment.CartFragment;
import com.himedia.androidshoppingmall.Fragment.HomeFragment;
import com.himedia.androidshoppingmall.Fragment.MyFragment;
import com.himedia.androidshoppingmall.Fragment.ShopFragment;
import com.himedia.androidshoppingmall.Request.CartListRequest;
import com.himedia.androidshoppingmall.Request.HomeRequest;
import com.himedia.androidshoppingmall.Request.MyPageRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private HomeFragment homeFragment = new HomeFragment();
    private ShopFragment shopFragment = new ShopFragment();
    private CartFragment cartFragment = new CartFragment();
    private MyFragment myFragment = new MyFragment();

    Toolbar toolbar;
    private PreferenceManager pManager;  // 로그인 로컬 쿠키 처리

    @Override
    public <T extends View> T findViewById(int id) {
        return super.findViewById(id);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pManager = new PreferenceManager();   // 로그인 세션 로컬 처리

        //바로가기 메뉴 시작 by Dean(23.07.13)
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(new NavigationItemSelectListener()); //바로가기 메뉴 이동 (메뉴수정 필요 Exam09 참조) by Dean

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);

        showMainProduct ("bestseller");  // 베스트 셀러 상품
        /*
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout, homeFragment).commitAllowingStateLoss();
        */

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationItemSelectListener());
    }

    private class NavigationItemSelectListener implements NavigationView.OnNavigationItemSelectedListener{     // BottomNavigationView.OnNavigationItemSelectedListener{
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            String member_id = pManager.getString(getApplicationContext(), "member_id");
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            DrawerLayout drawer = findViewById(R.id.drawer_layout);  // 바로가기메뉴 클릭시 창닫기 by Dean 230714

            switch (menuItem.getItemId()) {
                case R.id.nav_0:
                    showMainProduct ("bestseller");  // 베스트 셀러 상품
                    //transaction.replace(R.id.frameLayout, homeFragment).commitAllowingStateLoss();
                    toolbar.setTitle("홈페이지");
                    drawer.closeDrawers();   // 바로가기 메뉴 창 닫기
                    break;
                case R.id.nav_1:
                    transaction.replace(R.id.frameLayout, shopFragment).commit();
                    toolbar.setTitle("쇼핑 홈");
                    drawer.closeDrawers();
                    break;

                case R.id.nav_2:
                    showMyCart(member_id);
                  //  transaction.replace(R.id.frameLayout, cartFragment).commit();
                    toolbar.setTitle("장바구니");
                    drawer.closeDrawers();
                    break;

                case R.id.nav_3:
                    showMyPage(member_id);
                    toolbar.setTitle("마이페이지");
                    drawer.closeDrawers();
                    break;
            }
            return true;
        }
    }


    private class BottomNavigationItemSelectListener implements BottomNavigationView.OnNavigationItemSelectedListener{
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            String member_id = pManager.getString(getApplicationContext(), "member_id");

            switch (menuItem.getItemId()) {
                case R.id.nav_home:
                    showMainProduct ("bestseller");  // 베스트 셀러 상품
              //      transaction.replace(R.id.frameLayout, homeFragment).commitAllowingStateLoss();
                    toolbar.setTitle("홈페이지");
                    break;

                case R.id.nav_shop:
                    transaction.replace(R.id.frameLayout, shopFragment).commitAllowingStateLoss();
                    toolbar.setTitle("쇼핑 홈");
                    break;

                case R.id.nav_cart:
                    showMyCart(member_id);
                    //transaction.replace(R.id.frameLayout, cartFragment).commitAllowingStateLoss();
                    toolbar.setTitle("장바구니");
                    break;

                case R.id.nav_my:
                    showMyPage(member_id);
                    toolbar.setTitle("마이페이지");
                    break;
            }
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        ActivityCompat.finishAffinity(this); // app 종료
    }

    public void showMainProduct(String goodsStatus) {
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

                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    HomeFragment homeFragment = new HomeFragment();
                    homeFragment.setArguments(bundle);
                    transaction.replace(R.id.frameLayout, homeFragment).commit();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        HomeRequest homeRequest = new HomeRequest(goodsStatus, responseListener);
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        queue.add(homeRequest);
    }

    public void showMyPage(String member_id) {
        //EditText에 현재 입력되어 있는 값을 가져온다.
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // activity/fragment -> fragment 값 전달
                try {
                    Bundle bundle = new Bundle();

                    JSONObject jsonObject = new JSONObject(response);
                    //Register.php에서 $response["success"] = true; 값을 받음
                    boolean success = jsonObject.getBoolean("success");  // true, false 가져옴

                    if ( success ) {
                        String member_id = jsonObject.getString("member_id");       // 회원ID
                        String member_pw = jsonObject.getString("member_pw");    // 회원비밀번호
                        String member_name = jsonObject.getString("member_name");    // 회원명
                        String email1 = jsonObject.getString("email1");  // 이메일1
                        String email2 = jsonObject.getString("email2");  // 이메일1
                        String member_gender = jsonObject.getString("member_gender");        // 성별:남(101), 여(102)
                        String hp1 = jsonObject.getString("hp1");  // 휴대폰11
                        String hp2 = jsonObject.getString("hp2");  // 휴대폰12
                        String hp3 = jsonObject.getString("hp3");  // 휴대폰13

                        bundle.putString("member_id", member_id);
                        bundle.putString("member_pw", member_pw);
                        bundle.putString("member_name", member_name);
                        bundle.putString("email1", email1);
                        bundle.putString("email2", email2);
                        bundle.putString("member_gender", member_gender);
                        bundle.putString("hp1", hp1);
                        bundle.putString("hp2", hp2);
                        bundle.putString("hp3", hp3);

                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        MyFragment myFragment = new MyFragment();
                        myFragment.setArguments(bundle);
                        transaction.replace(R.id.frameLayout, myFragment).commit();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"마이페이지 접속에 실패하였습니다.",Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        MyPageRequest myPageRequest = new MyPageRequest(member_id, responseListener);
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        queue.add(myPageRequest);
    }

    public void showMyCart(String member_id) {
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
                        boolean success = jsonObject.getBoolean("success");  // true/false

                        if ( success ) {
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

                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    CartFragment cartFragment = new CartFragment();
                    cartFragment.setArguments(bundle);
                    transaction.replace(R.id.frameLayout, cartFragment).commit();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        CartListRequest cartListRequest = new CartListRequest(member_id, responseListener);
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        queue.add(cartListRequest);
    }

}
