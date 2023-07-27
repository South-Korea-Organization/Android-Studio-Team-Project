<?php     
    $con = mysqli_connect("localhost", "gdkgate", "kgd951753#", "gdkgate") or die("MySqlDB 접속 실패 !!");
 //   $con = mysqli_connect("db-instance-001.cfaktkh1zamw.ap-northeast-2.rds.amazonaws.com", "admin", "admin001", "scott") or die("MySqlDB 접속 실패 !!");
    mysqli_query($con,'SET NAMES utf8');

    
    $member_id = isset($_POST["member_id"]) ? $_POST["member_id"] : "";
    $member_pw = isset($_POST["member_pw"]) ? $_POST["member_pw"] : "";
    $member_name = isset($_POST["member_name"]) ? $_POST["member_name"] : "";
    $email1 = isset($_POST["email1"]) ? $_POST["email1"] : "";
    $email2 = isset($_POST["email2"]) ? $_POST["email2"] : "";
    $member_gender = isset($_POST["member_gender"]) ? $_POST["member_gender"] : "";
    $hp1 = isset($_POST["hp1"]) ? $_POST["hp1"] : "";
    $hp2 = isset($_POST["hp2"]) ? $_POST["hp2"] : "";
    $hp3 = isset($_POST["hp3"]) ? $_POST["hp3"] : "";
    
    /*
    $member_id = isset($_GET["member_id"]) ? $_GET["member_id"] : "";
    $member_pw = isset($_GET["member_pw"]) ? $_GET["member_pw"] : "";
    $member_name = isset($_GET["member_name"]) ? $_GET["member_name"] : "";
    $email1 = isset($_GET["email1"]) ? $_GET["email1"] : "";
    $email2 = isset($_GET["email2"]) ? $_GET["email2"] : "";
    $member_gender = isset($_GET["member_gender"]) ? $_GET["member_gender"] : "";
    $hp1 = isset($_GET["hp1"]) ? $_GET["hp1"] : "";
    $hp2 = isset($_GET["hp2"]) ? $_GET["hp2"] : "";
    $hp3 = isset($_GET["hp3"]) ? $_GET["hp3"] : "";
    */

    $statement = mysqli_prepare($con, "INSERT INTO t_shopping_member (member_id,member_pw,member_name,email1,email2,member_gender,hp1,hp2,hp3) VALUES (?,?,?,?,?,?,?,?,?)");
    mysqli_stmt_bind_param($statement, "sssssssss", $member_id, $member_pw, $member_name, $email1,$email2,$member_gender,$hp1,$hp2,$hp3); // sssi는 string 3개 int 1개
    mysqli_stmt_execute($statement);
    
    mysqli_close( $con );            

    // Check if the query was successful
    if (mysqli_stmt_affected_rows($statement) > 0) {
        $response["success"] = true;
        $response["member_id"] = $member_id;
    } else {
        $response["success"] = false;      
    }

    echo json_encode($response);
?>