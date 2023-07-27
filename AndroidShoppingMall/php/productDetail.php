<?php
    $con = mysqli_connect("db-instance-001.cfaktkh1zamw.ap-northeast-2.rds.amazonaws.com", "admin", "admin001", "scott") or die("MySqlDB 접속 실패 !!");
    mysqli_query($con,'SET NAMES utf8');

    $goods_id = isset($_POST["goods_id"]) ? $_POST["goods_id"] : "";
   // $goods_id = isset($_GET["goods_id"]) ? $_GET["goods_id"] : "";

    $sql  = "select t.* from( select g.goods_id,g.goods_title, g.goods_price,d.fileName from t_shopping_goods g, t_goods_detail_image d where g.goods_id=d.goods_id and g.goods_del_yn='N' and g.goods_id=? order by g.goods_id ) t; ";   
   
    $statement = mysqli_prepare($con, $sql);
    mysqli_stmt_bind_param($statement, "s", $goods_id);  // ss 는 string string,  si는 string int 의미임  
    mysqli_stmt_execute($statement);
   
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $goods_id, $goods_title, $goods_price, $fileName);

    $result = array();
    $response = array();
    if ( $statement == "") $response["success"] = false;
 
    while(mysqli_stmt_fetch($statement)) {
        $result["success"] = true;
        $result["goods_id"] = $goods_id;
        $result["goods_title"] = $goods_title;
        $result["goods_price"] = $goods_price;   
        $result["fileName"] = $fileName;   
        $response[] = $result;          
    }

    echo json_encode($response);
?>