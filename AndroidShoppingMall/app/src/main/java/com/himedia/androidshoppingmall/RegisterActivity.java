package com.himedia.androidshoppingmall;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.himedia.androidshoppingmall.Data.PreferenceManager;
import com.himedia.androidshoppingmall.Data.UserBean;
import com.himedia.androidshoppingmall.Request.CheckDuplicateIdRequest;
import com.himedia.androidshoppingmall.Request.RegisterRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {
    private PreferenceManager pManager;
    private static final String EMPTY_STRING = "";

 //   private Spinner yearsSpinner;
    private ArrayAdapter adapter;

    private EditText name;
    private EditText email1,email2;
    private EditText id;
    private EditText pw;
    private EditText pwChk;
    private RadioGroup gender;
    private RadioButton male;
    private RadioButton female;
    private EditText hp1,hp2,hp3;

  //  private String selectYears = "";
    private String selectGender = "";
    
    boolean checkDuplicateIDYesNo = false;  // 충복 ID check 여부

    private TextView okTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        pManager = new PreferenceManager();  // 로그인후 토큰이 만들어져 자동 로그인 기능

        id = findViewById(R.id.idEt); // DB에 중복되는 id값 있는지 확인
        pw = findViewById(R.id.pwEt);
        pwChk = findViewById(R.id.pwChkEt); // pwEt와 값이 같은지 확인
        name = findViewById(R.id.nameEt);
        email1 = findViewById(R.id.email1Et);
        email2 = findViewById(R.id.email2Et);
        gender = findViewById(R.id.genderRg);
        male = findViewById(R.id.maleRb);
        female = findViewById(R.id.femaleRb);

        RadioGroup.OnCheckedChangeListener listener = new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == male.getId()) {
                    selectGender = "101";              //String.valueOf(male.getText());   by Dean 수정 기존 남자 값이 들어가는데 코드로 들어가게 수정
                } else if (checkedId == female.getId()) {
                    selectGender = "102";             // String.valueOf(female.getText());
                }
            }
        };
        gender.setOnCheckedChangeListener(listener);

        hp1 = findViewById(R.id.hp1Et);
        hp2 = findViewById(R.id.hp2Et);
        hp3 = findViewById(R.id.hp3Et);        

        /*
        yearsSpinner = (Spinner) findViewById(R.id.yearsSppiner);
        adapter = ArrayAdapter.createFromResource(this, R.array.yearsArray, android.R.layout.simple_spinner_dropdown_item);
        yearsSpinner.setAdapter(adapter);

        yearsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectYears = yearsSpinner.getSelectedItem().toString();
                if(selectYears.equals("선택")) {
                    selectYears = EMPTY_STRING;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
         */
    }

    public void OnClickCheckDuplicateID( View view )
    {
        //EditText에 현재 입력되어 있는 값을 가져온다.
        String member_id = id.getText().toString();

        if ( member_id.equals(""))
        {
            Toast.makeText(getApplicationContext(),"아이디를 입력하고 ID체크를 하세요.",Toast.LENGTH_SHORT).show();
            return;
        }

        Response.Listener<String> responseListenerCheckID = new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    //Register.php에서 $response["success"] = true; 값을 받음
                    boolean success = jsonObject.getBoolean("success");  // true, false 가져옴

                    if ( success )
                    {
                        Toast.makeText(getApplicationContext(),"중복된 ID입니다 ",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        checkDuplicateIDYesNo = true;
                        Toast.makeText(getApplicationContext(),"사용할 수 있는 ID 입니다.",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        CheckDuplicateIdRequest checkDuplicateIdRequest = new CheckDuplicateIdRequest(member_id,responseListenerCheckID);
        RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
        queue.add(checkDuplicateIdRequest);
    }

    public void onOkBtnClick(View v) {
        if(isEmpty()) { // 비어있는 칸 없는지 확인. 있으면 true 반환
           return;
        }
        else if(checkDuplicateIDYesNo == false ) { // 중복 ID값 체크여부
            Toast.makeText(this,"중복ID를 체크후 회원가입 하세요.",Toast.LENGTH_SHORT).show();
            return;
        }
        else if(!isCorreectPw()) { // PW, PwChk 값 같은지 확인. 같으면 true 반환
            Toast.makeText(this, "비밀번호와 비밀번호 확인의 값이 다릅니다. 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        // Email 포맷 체크, %@% 아니면 DB에 안들어감.

        // DB에 값 집어넣기
        UserBean userBean = new UserBean();

        userBean.setName(String.valueOf(name.getText()));
        userBean.setEmail1(String.valueOf(email1.getText()));
        userBean.setEmail2(String.valueOf(email2.getText()));
        userBean.setId(String.valueOf(id.getText()));
        userBean.setPassword(String.valueOf(pw.getText()));
        userBean.setGender(String.valueOf(selectGender));
        userBean.setHp1(String.valueOf(hp1.getText()));
        userBean.setHp2(String.valueOf(hp2.getText()));
        userBean.setHp3(String.valueOf(hp3.getText()));
        //   userBean.setYears(String.valueOf(selectYears));

        registerMember(userBean);
    }

    public void onCancelBtnClick(View v) {
        // 진짜 뒤로갈건지 확인하기
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("알림");
        builder.setMessage("회원가입을 취소하시겠습니까?");
        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        builder.setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {@Override public void onClick(DialogInterface dialog, int which) { }});
        builder.show();
    }

    /*
    private boolean isDuplicateId() {
        String dbId = dbHelper.getUserId(String.valueOf(id.getText()));

        if(dbId.isEmpty()) {
            return false;
        }
        return true;
    }
*/
    private boolean isCorreectPw() {
        String pwValue = String.valueOf(pw.getText());
        String pwChkValue = String.valueOf(pwChk.getText());

        if(pwValue.equals(pwChkValue)) {
            return true;
        }

        pw.setText("");
        pwChk.setText("");
        moveFocus(pw);
        return false;
    }

    private boolean isEmpty() {
        if(TextUtils.isEmpty(name.getText())) {
            showDialog("이름", name);
            return true;
        }
        else if(TextUtils.isEmpty(email1.getText())) {
            showDialog("이메일", email1);
            return true;
        }
        else if(TextUtils.isEmpty(email2.getText())) {
            showDialog("이메일", email2);
            return true;
        }
        else if(TextUtils.isEmpty(id.getText())) {
            showDialog("아이디", id);
            return true;
        }
        else if(TextUtils.isEmpty(pw.getText())) {
            showDialog("비밀번호", pw);
            return true;
        }
        else if(TextUtils.isEmpty(pwChk.getText())) {
            showDialog("비밀번호 확인", pwChk);
            return true;
        }
        else if(TextUtils.isEmpty(selectGender)) {
            showDialog("성별");
            return true;
        }
        /*
        else if(TextUtils.isEmpty(selectYears)) {
            showDialog("연령");
            return true;
        }
        */
        else if(TextUtils.isEmpty(hp1.getText())) {
            showDialog("HP1", hp1);
            return true;
        }
        else if(TextUtils.isEmpty(hp2.getText())) {
            showDialog("HP2", hp2);
            return true;
        }
        else if(TextUtils.isEmpty(hp3.getText())) {
            showDialog("HP3", hp3);
            return true;
        }

        return false;
    }

    private void showDialog(String str) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("알림");
        builder.setMessage( str + "을 선택해주세요.");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) { }});
        builder.show();
    }

    private void showDialog(String str, final EditText et) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("알림");
        builder.setMessage( str + "을(를) 기입해주세요.");

        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        moveFocus(et);
                    }
                });
        builder.show();
    }

    private void moveFocus(EditText et) {
        // 해당 position으로 focus 이동 후 키보드 올리기
        et.requestFocus();
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    public void registerMember(UserBean userBean) {
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
                        String member_id = jsonObject.getString ("member_id");
                        pManager.setString(getApplicationContext(), "member_id", String.valueOf(member_id));
                        Toast.makeText(getApplicationContext(),"환영합니다.",Toast.LENGTH_SHORT).show();
                        //  RegisterActivity ->  MainActivity 로 넘어가게 한다.
                        Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"회원가입에 실패하였습니다.",Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        RegisterRequest registerRequest = new RegisterRequest(userBean, responseListener);
        RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
        queue.add(registerRequest);
    }
}
