<?php     
    $con = mysqli_connect("localhost", "gdkgate", "kgd951753#", "gdkgate") or die("MySqlDB 접속 실패 !!");
    //$con = mysqli_connect("db-instance-001.cfaktkh1zamw.ap-northeast-2.rds.amazonaws.com", "admin", "admin001", "scott") or die("MySqlDB 접속 실패 !!");
    mysqli_query($con,'SET NAMES utf8');

    $goods_id = isset($_POST["goods_id"]) ? $_POST["goods_id"] : "";  
    $member_id = isset($_POST["member_id"]) ? $_POST["member_id"] : "";  
/*   
    $goods_id = isset($_GET["goods_id"]) ? $_GET["goods_id"] : "";  
    $member_id = isset($_GET["member_id"]) ? $_GET["member_id"] : ""; 
*/  
    $statement = mysqli_prepare($con, "INSERT INTO t_shopping_cart (goods_id,member_id) VALUES (?,?)");
    mysqli_stmt_bind_param($statement, "is", $goods_id, $member_id); // sssi는 string 3개 int 1개
    mysqli_stmt_execute($statement);

    mysqli_close( $con );            

    // Check if the query was successful
    if (mysqli_stmt_affected_rows($statement) > 0) {
        $response["success"] = true;     
    } else {
        $response["success"] = false;      
    }

    echo json_encode($response);

    /*
         추가 아래 추가하지않으면 키값도 인서트해야됨
         ALTER TABLE t_shopping_cart MODIFY CART_ID BIGINT NOT NULL AUTO_INCREMENT; 
    */
?>