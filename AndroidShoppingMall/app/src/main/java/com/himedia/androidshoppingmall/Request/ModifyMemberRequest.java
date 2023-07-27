package com.himedia.androidshoppingmall.Request;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.himedia.androidshoppingmall.Data.UserBean;

import java.util.HashMap;
import java.util.Map;

public class ModifyMemberRequest extends StringRequest {
    // 서버 URL 설정 ( PHP 파일 연동 ) 회원탈퇴

    //final static private String URL = "http://3.37.214.236/android_001/ModifyMember.php";   //실서버
    private static final String URL ="http://gdkgate.dothome.co.kr/android_001/ModifyMember.php";

    private Map<String, String> map;

    public ModifyMemberRequest(UserBean userBean, Response.Listener<String> listener)
    {
        super(Method.POST, URL, listener, null);
        map = new HashMap<>();

        map.put("member_id", userBean.getId());
        map.put("member_pw", userBean.getPassword());
        map.put("member_name", userBean.getName());
        map.put("email1", userBean.getEmail1());
        map.put("email2", userBean.getEmail2());
        map.put("member_gender",userBean.getGender());
        map.put("hp1",userBean.getHp1());
        map.put("hp2",userBean.getHp2());
        map.put("hp3",userBean.getHp3());
    }

    @Override
    protected  Map<String, String> getParams() throws AuthFailureError
    {
        return map;
    }
}
