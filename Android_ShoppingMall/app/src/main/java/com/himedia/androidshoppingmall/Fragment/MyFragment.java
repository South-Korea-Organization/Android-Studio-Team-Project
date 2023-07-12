package com.himedia.androidshoppingmall.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import com.himedia.androidshoppingmall.Data.PreferenceManager;
import com.himedia.androidshoppingmall.Data.UserBean;
import com.himedia.androidshoppingmall.Data.UserDBHelper;
import com.himedia.androidshoppingmall.MainActivity;
import com.himedia.androidshoppingmall.R;

public class MyFragment extends Fragment {
    // 이름, 아이디, 이메일, 성별, 나이 표시해주기
    // 정보수정? 시간 남으면 하기 -> 정보수정 글씨를 누르면 팝업화면으로 수정하고 확인하면 refresh
    private TextView userName;
    private TextView userName2;
    private TextView userId;
    private TextView userEmail;
    private TextView userGender;
    private TextView userYears;
    private TextView logout;
    private UserDBHelper dbHelper;
    private PreferenceManager pManager;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_my_fragment, container, false);

        pManager = new PreferenceManager();
        userName = view.findViewById(R.id.usernameTv);
        userName2 = view.findViewById(R.id.usernameTv2);
        userId = view.findViewById(R.id.useridTv);
        userEmail = view.findViewById(R.id.useremailTv);
        userGender = view.findViewById(R.id.usergenderTv);
        userYears = view.findViewById(R.id.useryearsTv);
        logout = view.findViewById(R.id.logoutTv);

        dbHelper = UserDBHelper.getInstance(getContext());
        ArrayList<UserBean> userArr = dbHelper.getUserById(pManager.getString(getContext(), "user_id"));

        userName.setText(userArr.get(0).getName());
        userName2.setText(userArr.get(0).getName());
        userId.setText(userArr.get(0).getId());
        userEmail.setText(userArr.get(0).getEmail());
        userGender.setText(userArr.get(0).getGender());
        userYears.setText(userArr.get(0).getYears());

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        // 시간남으면 회원탈퇴 구현
        return view;
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
        builder.show();
    }
}