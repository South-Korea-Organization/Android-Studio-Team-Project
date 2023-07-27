<?php
    $con = mysqli_connect("localhost", "gdkgate", "kgd951753#", "gdkgate") or die("MySqlDB 접속 실패 !!");	// dothome.co.kr
//    $con = mysqli_connect("db-instance-001.cfaktkh1zamw.ap-northeast-2.rds.amazonaws.com", "admin", "admin001", "scott") or die("MySqlDB 접속 실패 !!");
    mysqli_query($con,'SET NAMES utf8');

    $member_id = isset($_POST["member_id"]) ? $_POST["member_id"] : "";
    $member_pw = isset($_POST["member_pw"]) ? $_POST["member_pw"] : "";
/*
    $member_id = isset($_GET["member_id"]) ? $_GET["member_id"] : "";
    $member_pw = isset($_GET["member_pw"]) ? $_GET["member_pw"] : "";
*/  
    $statement = mysqli_prepare($con, "SELECT member_id,member_pw,member_name,del_yn,joindate FROM t_shopping_member WHERE DEL_YN='N' AND MEMBER_ID = ? AND MEMBER_PW = ?");
    mysqli_stmt_bind_param($statement, "ss", $member_id, $member_pw);  // ss 는 string string,  si는 string int 의미임  
    mysqli_stmt_execute($statement);

    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $member_id, $member_pw, $member_name, $del_yn, $joinDate);

    $response = array();
    $response["success"] = false;
 
    while(mysqli_stmt_fetch($statement)) {
        $response["success"] = true;
        $response["member_id"] = $member_id;
        $response["member_pw"] = $member_pw;
        $response["member_name"] = $member_name;   
        $response["del_yn"] = $del_yn;      
        $response["joinDate"] = $joinDate;   
    }

    echo json_encode($response);
?>