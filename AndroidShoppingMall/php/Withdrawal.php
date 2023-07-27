<?php     
    $con = mysqli_connect("localhost", "gdkgate", "kgd951753#", "gdkgate") or die("MySqlDB 접속 실패 !!");
 //   $con = mysqli_connect("db-instance-001.cfaktkh1zamw.ap-northeast-2.rds.amazonaws.com", "admin", "admin001", "scott") or die("MySqlDB 접속 실패 !!");
    mysqli_query($con,'SET NAMES utf8');

    $member_id = isset($_POST["member_id"]) ? $_POST["member_id"] : "";
    $member_pw = isset($_POST["member_pw"]) ? $_POST["member_pw"] : "";    
    
    /*
    $member_id = isset($_GET["member_id"]) ? $_GET["member_id"] : "";
    $member_pw = isset($_GET["member_pw"]) ? $_GET["member_pw"] : "";    
    */

    $statement = mysqli_prepare($con, "UPDATE t_shopping_member SET del_yn = 'Y' Where member_id=? and member_pw=?");
    mysqli_stmt_bind_param($statement, "ss", $member_id, $member_pw); // sssi는 string 3개 int 1개  datetime 은 d
    mysqli_stmt_execute($statement);
    
    mysqli_close( $con );            

    // Check if the query was successful
    if (mysqli_stmt_affected_rows($statement) > 0) {
        $response["success"] = true;     
    } else {
        $response["success"] = false;      
    }

    echo json_encode($response);
?>