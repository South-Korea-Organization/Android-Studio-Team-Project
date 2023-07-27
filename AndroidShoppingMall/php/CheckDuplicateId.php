<?php
    $con = mysqli_connect("localhost", "gdkgate", "kgd951753#", "gdkgate") or die("MySqlDB 접속 실패 !!");	// dothome.co.kr
 //   $con = mysqli_connect("db-instance-001.cfaktkh1zamw.ap-northeast-2.rds.amazonaws.com", "admin", "admin001", "scott") or die("MySqlDB 접속 실패 !!");
    mysqli_query($con,'SET NAMES utf8');

    $member_id = isset($_POST["member_id"]) ? $_POST["member_id"] : "";  
  //  $member_id = isset($_GET["member_id"]) ? $_GET["member_id"] : "";
  
    
    $statement = mysqli_prepare($con, "SELECT member_id FROM t_shopping_member WHERE DEL_YN='N' AND MEMBER_ID = ?");
    mysqli_stmt_bind_param($statement, "s", $member_id);  // ss 는 string string,  si는 string int 의미임  
    mysqli_stmt_execute($statement);

    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $member_id);

    $response = array();   
 
    if (mysqli_stmt_fetch($statement))
    {
        $response["success"] = true;   // 중복 ID가 있다       
        $response["member_id"] = $member_id;   // 중복 ID 돌려주기          
    }
    else
    {
        $response["success"] = false;   // 중복 ID가 없다
    }

    echo json_encode($response);   
?>