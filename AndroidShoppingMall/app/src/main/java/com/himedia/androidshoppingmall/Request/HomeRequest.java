package com.himedia.androidshoppingmall.Request;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class HomeRequest extends StringRequest {
    // 서버 URL 설정 ( PHP 파일 연동 )
 //    final static private String URL = "http://3.37.214.236/android_001/main.php";   //실서버
    final static private String URL = "http://gdkgate.dothome.co.kr/android_001/main.php";      //테스트서버

    private Map<String, String> map;

    public HomeRequest(String goodsStatus, Response.Listener<String> listener)
    {
        super(Method.POST, URL, listener, null);
        map = new HashMap<>();
        map.put("goodsStatus", goodsStatus);
    }

    @Override
    protected  Map<String, String> getParams() throws AuthFailureError
    {
        return map;
    }
}
