package com.himedia.androidshoppingmall;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.himedia.androidshoppingmall.Fragment.CartFragment;
import com.himedia.androidshoppingmall.Fragment.HomeFragment;
import com.himedia.androidshoppingmall.Fragment.MyFragment;
import com.himedia.androidshoppingmall.Fragment.ShopFragment;

public class MainActivity extends AppCompatActivity {
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private HomeFragment homeFragment = new HomeFragment();
    private ShopFragment shopFragment = new ShopFragment();
    private CartFragment cartFragment = new CartFragment();
    private MyFragment myFragment = new MyFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 첫 화면 지정
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout, homeFragment).commitAllowingStateLoss();

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation_view);
        bottomNav.setOnNavigationItemSelectedListener(new ItemSelectListener());
    }

    private class ItemSelectListener implements BottomNavigationView.OnNavigationItemSelectedListener{

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            switch (menuItem.getItemId()) {
                case R.id.nav_home:
                    transaction.replace(R.id.frameLayout, homeFragment).commitAllowingStateLoss();
                    break;

                case R.id.nav_shop:
                    transaction.replace(R.id.frameLayout, shopFragment).commitAllowingStateLoss();
                    break;

                case R.id.nav_cart:
                    transaction.replace(R.id.frameLayout, cartFragment).commitAllowingStateLoss();
                    break;

                case R.id.nav_my:
                    transaction.replace(R.id.frameLayout, myFragment).commitAllowingStateLoss();
                    break;
            }
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        ActivityCompat.finishAffinity(this); // app 종료
    }
}
