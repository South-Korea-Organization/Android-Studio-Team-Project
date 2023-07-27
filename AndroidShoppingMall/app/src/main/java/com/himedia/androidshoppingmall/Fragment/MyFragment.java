package com.himedia.androidshoppingmall.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.himedia.androidshoppingmall.Data.PreferenceManager;
import com.himedia.androidshoppingmall.Data.UserBean;
import com.himedia.androidshoppingmall.LoginActivity;
import com.himedia.androidshoppingmall.MainActivity;
import com.himedia.androidshoppingmall.R;
import com.himedia.androidshoppingmall.Request.ModifyMemberRequest;
import com.himedia.androidshoppingmall.Request.WithdrawalRequest;

import org.json.JSONException;
import org.json.JSONObject;
public class MyFragment extends Fragment {
    // 이름, 아이디, 이메일, 성별, 나이 표시해주기
    // 정보수정? 시간 남으면 하기 -> 정보수정 글씨를 누르면 팝업화면으로 수정하고 확인하면 refresh
    private TextView userId;    //아이디
    private TextView userPw,userChkPw;    // 비밀번호, 비번확인
    private TextView userName;  //회원명    
    private TextView userEmail1,userEmail2;   //이메일
    private RadioGroup userGender;    // 성별 남(101), 여자(102)
    private RadioButton maleRb,femaleRb;    // 남, 여
    private TextView userHp1,userHp2,userHp3;    // 휴대폰 번호

    private TextView logout,cancel,modifyOk;
    private TextView withdrawal;    // 회원 탈퇴

    private String selectGender = "";

    // 회원탈퇴용
    private String memberId = "";
    private String memberPw = "";
    private PreferenceManager pManager;  // 로그인 로컬 쿠키 처리

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_my_fragment, container, false);

        pManager = new PreferenceManager();

        userId = view.findViewById(R.id.idEt);
        userPw = view.findViewById(R.id.pwEt);

        userChkPw = view.findViewById(R.id.pwChkEt);
        userName = view.findViewById(R.id.nameEt);

        userEmail1 = view.findViewById(R.id.email1Et);
        userEmail2 = view.findViewById(R.id.email2Et);

        userGender = view.findViewById(R.id.genderRg);  // 성별

        int maleRadioButtonId = R.id.maleRb;
        int femaleRadioButtonId = R.id.femaleRb;
        maleRb = view.findViewById(maleRadioButtonId);
        femaleRb = view.findViewById(femaleRadioButtonId);

        RadioGroup.OnCheckedChangeListener listener = new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == maleRb.getId()) {
                    selectGender = "101";              //String.valueOf(male.getText());   by Dean 수정 기존 남자 값이 들어가는데 코드로 들어가게 수정
                } else if (checkedId == femaleRb.getId()) {
                    selectGender = "102";             // String.valueOf(female.getText());
                }
            }
        };
        userGender.setOnCheckedChangeListener(listener);

        userHp1 = view.findViewById(R.id.hp1Et);
        userHp2 = view.findViewById(R.id.hp2Et);
        userHp3 = view.findViewById(R.id.hp3Et);

        withdrawal = view.findViewById(R.id.withdrawalTv);   // 회원탈퇴
        logout = view.findViewById(R.id.logoutTv);
        cancel = view.findViewById(R.id.cancelTv);
        modifyOk   = view.findViewById(R.id.modifyOkTv);

        // Activity/Fragment -> Fragment 보낸 인자 받기
        Bundle extra = this.getArguments();
        if(extra != null) {
            extra = getArguments();

            String member_id = extra.getString("member_id");
            String member_pw = extra.getString("member_pw");

            // 회원탈퇴용 id, pw 저장
            memberId = member_id;
            memberPw = member_pw;

            String member_name = extra.getString("member_name");

            String email1 = extra.getString("email1");
            String email2 = extra.getString("email2");
            String member_gender = extra.getString("member_gender");

            String hp1 = extra.getString("hp1");
            String hp2 = extra.getString("hp2");
            String hp3 = extra.getString("hp3");

            userId.setText(member_id);
            userPw.setText(member_pw);
            userChkPw.setText(member_pw);
            userName.setText(member_name);
            userEmail1.setText(email1);
            userEmail2.setText(email2);
         //   userGender.setText(member_gender);
            userHp1.setText(hp1);
            userHp2.setText(hp2);
            userHp3.setText(hp3);

            if(member_gender.equals("101")) {
                maleRb.setChecked(true);          //String.valueOf(male.getText());   by Dean 수정 기존 남자 값이 들어가는데 코드로 들어가게 수정
            } else if (member_gender.equals("102")) {
                femaleRb.setChecked(true);
            }
            // 성별을 체크하기 수정중
        }

        modifyOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { showDialogModifyMember();  }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        withdrawal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogWithdrawal();
            }
        });

        // 시간남으면 회원탈퇴 구현
        return view;
    }

    private void showDialogModifyMember() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("알림");
        builder.setMessage("정말 회원수정 하시겠습니까?");
        builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {

                // DB에 값 집어넣기
                UserBean userBean = new UserBean();

                userBean.setId(String.valueOf(userId.getText()));
                userBean.setPassword(String.valueOf(userPw.getText()));
                userBean.setName(String.valueOf(userName.getText()));
                userBean.setEmail1(String.valueOf(userEmail1.getText()));
                userBean.setEmail2(String.valueOf(userEmail2.getText()));
                userBean.setGender(String.valueOf(selectGender));
                userBean.setHp1(String.valueOf(userHp1.getText()));
                userBean.setHp2(String.valueOf(userHp2.getText()));
                userBean.setHp3(String.valueOf(userHp3.getText()));
                //   userBean.setYears(String.valueOf(selectYears));

                ModifyMember(userBean);
            }
        });
        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        builder.show();
    }

    private void showDialogWithdrawal() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("알림");
        builder.setMessage("정말 회원탈퇴 하시겠습니까?");
        builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
                pManager.clear(getContext());

                Withdrawal(memberId, memberPw);
            }
        });
        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        builder.show();
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("알림");
        builder.setMessage("정말 로그아웃 하시겠습니까?");
        builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
                pManager.clear(getContext());
                ((MainActivity)getActivity()).finish();
            }
        });
        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        builder.show();
    }

    //회원탈퇴
    public void Withdrawal( String member_id, String member_pw ) {
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
                        pManager.clear(getContext());
                        Toast.makeText(getContext(),"회원을 탈퇴하였습니다.",Toast.LENGTH_SHORT).show();
                        //  Fragment -> Activity 이동 : MyFragment ->  MainActivity 로 넘어가게 한다.
                        Intent intent = new Intent(getContext(), LoginActivity.class);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(getContext(),"회원탈퇴가 실패하였습니다.",Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        WithdrawalRequest withdrawalRequest = new WithdrawalRequest(member_id,member_pw, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(withdrawalRequest);
    }

    //회원수정
    public void ModifyMember( UserBean userBean) {
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
                        Toast.makeText(getContext(),"회원을 수정하였습니다.",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getContext(),"회원수정이 실패하였습니다.",Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        ModifyMemberRequest modifyMemberRequest = new ModifyMemberRequest(userBean, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(modifyMemberRequest);
    }
}