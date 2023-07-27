<?php
   $con = mysqli_connect("localhost", "gdkgate", "kgd951753#", "gdkgate") or die("MySqlDB 접속 실패 !!");	// dothome.co.kr
   // $con = mysqli_connect("db-instance-001.cfaktkh1zamw.ap-northeast-2.rds.amazonaws.com", "admin", "admin001", "scott") or die("MySqlDB 접속 실패 !!");
    mysqli_query($con,'SET NAMES utf8');

    $member_id = isset($_POST["member_id"]) ? $_POST["member_id"] : "";
   // $member_id = isset($_GET["member_id"]) ? $_GET["member_id"] : "";
   
    $statement = mysqli_prepare($con, "SELECT member_id,member_pw,member_name,email1,email2,member_gender,hp1,hp2,hp3 FROM t_shopping_member WHERE DEL_YN='N' AND MEMBER_ID = ?");
    mysqli_stmt_bind_param($statement, "s", $member_id);  // ss 는 string string,  si는 string int , string datetime 는  sd   
    mysqli_stmt_execute($statement);

    mysqli_stmt_store_result($statement);  // 남성:101, 여성:102
    mysqli_stmt_bind_result($statement, $member_id, $member_pw, $member_name, $email1,$email2, $member_gender, $hp1,$hp2,$hp3);

    $response = array();
    $response["success"] = false;
 
    while(mysqli_stmt_fetch($statement)) {
        $response["success"] = true;
        $response["member_id"] = $member_id;
        $response["member_pw"] = $member_pw;
        $response["member_name"] = $member_name;   
        $response["email1"] = $email1;      
        $response["email2"] = $email2;  
        $response["member_gender"] = $member_gender;  
        $response["hp1"] = $hp1;   
        $response["hp2"] = $hp2;   
        $response["hp3"] = $hp3;   
    }

    echo json_encode($response);
?>