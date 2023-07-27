package com.himedia.androidshoppingmall;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.himedia.androidshoppingmall.Data.PreferenceManager;
import com.himedia.androidshoppingmall.Request.LoginRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private Animation logoAni; // 위치 이동
    private Animation loginFormAni; // 투명도 조절
    private ImageView imgView;
    private LinearLayout loginForm;
    private PreferenceManager pManager;

    private EditText id;
    private EditText pw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        pManager = new PreferenceManager();  // 로그인후 토큰이 만들어져 자동 로그인 기능

        imgView = findViewById(R.id.logoImg);
        loginForm = findViewById(R.id.loginfrom_view);
        id = findViewById(R.id.idEt);
        pw = findViewById(R.id.pwEt);

        loginCheck();
        startAnime();
    }

    private void startAnime() {
        logoAni = new TranslateAnimation(0, 0, 0, -250); // from 어디서 to 어디까지 이동할건지. 가운데를 중심으로 위, 왼쪽: - 아래, 오른쪽: +
        logoAni.setDuration(2000); // 지속시간
        logoAni.setFillAfter(true); // 이동 후 이동한 자리에 남아있을건지
        logoAni.setStartOffset(1500); // 딜레이
        logoAni.setInterpolator(new AccelerateDecelerateInterpolator()); // interpolator 설정. AccelerteDecelerate : 시작지점에 가속했다 종료시점에 감속

        loginFormAni = new AlphaAnimation(0, 1);
        loginFormAni.setDuration(1000);
        loginFormAni.setStartOffset(3000);

        imgView.setAnimation(logoAni); // 애니메이션 세팅
        loginForm.setAnimation(loginFormAni);
    }

    public void loginCheck() {
        String loginId = pManager.getString(this, "member_id");

        if(loginId.length() != 0) { // preference가 비어있지 않으면 바로 Main실행.
            startMainActivity();
       }
    }

    public void onLogin(View v) {
        // 로그인 id, pw 확인 후 일치 확인, 이후 main Activity로 이동
        String idValue = String.valueOf(id.getText());
        String pwValue = String.valueOf(pw.getText());

        if(!accountCheck(idValue,pwValue)) {
            return;
        }

        login(idValue,pwValue);

        setEmptyEt();
    }

    public void onRegister(View v) {
        // register Activity로 이동
        setEmptyEt();

        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private boolean accountCheck(String idValue, String pwValue) {
        //db에 접근해서 id, pw 확인 후 일치 시 true, 불일치시 false return.

        if( idValue.equals(""))
        {
            Toast.makeText(this, "아이디를 입력하세요.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if( pwValue.equals(""))
        {
            Toast.makeText(this, "비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void setEmptyEt() {
        id.setText("");
        pw.setText("");
    }

    public void login(String member_id, String member_pw) {

            Response.Listener<String> responseListener = new Response.Listener<String>()
            {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        //Register.php에서 $response["success"] = true; 값을 받음
                        boolean success = jsonObject.getBoolean("success");  // true, false 가져옴

                        if ( success )
                        {
                            String member_id = jsonObject.getString("member_id");
                            String member_pw = jsonObject.getString("member_pw");
                            
                            Toast.makeText(getApplicationContext(),"환영합니다.",Toast.LENGTH_SHORT).show();
                            pManager.setString(getApplicationContext(), "member_id", String.valueOf(member_id));
                            //  LoginActivity ->  MainActivity 로 넘어가게 한다.
                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                            startActivity(intent);
                        }
                        else
                        {
                            pManager.clear(getApplicationContext()); // 로컬 쿠키 삭제
                            Toast.makeText(getApplicationContext(),"로그인에 실패하였습니다.",Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            };
            LoginRequest loginRequest = new LoginRequest(member_id, member_pw, responseListener);
            RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
            queue.add(loginRequest);
    }

}