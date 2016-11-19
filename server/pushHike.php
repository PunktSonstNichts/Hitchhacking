<?php

header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");

include_once "loader.php";

$requestsSource = $_SERVER['HTTP_REFERER'];
if ($_SERVER['REQUEST_METHOD'] === 'POST' || true) {
    if ($_POST["api"] == API || true) {
	$apiKey = $_POST["api"];
	$arr["user_id"] = $_POST["user_id"];
	$arr["matching_state"] = 0;
	$arr["start_timestamp"] = $_POST["start_timestamp"];
	$arr["last_fetch_timestamp"] = 0;
	$arr["accepting_timestamp"] = 0;
	$arr["destination_lat"] = $_POST["destination_lat"];
	$arr["destination_lon"] = $_POST["destination_lon"];
	$arr["destination_heading"] = $_POST["destination_heading"];
	$arr["destination_name"] = $_POST["destination_name"];
	$arr["current_lat"] = $_POST["current_lat"];
	$arr["current_lon"] = $_POST["current_lon"];
	$arr["current_name"] = $_POST["current_name"];
	
	$arr["user_id"] = 2;
	$arr["matching_state"] = 0;
	$arr["start_timestamp"] = 2344324;
	$arr["last_fetch_timestamp"] = 0;
	$arr["accepting_timestamp"] = 0;
	$arr["destination_lat"] = 234.234;
	$arr["destination_lon"] = 234.234;
	$arr["destination_heading"] = 234.234;
	$arr["destination_name"] = "london";
	$arr["current_lat"] = 345.34;
	$arr["current_lon"] = 345.35;
	$arr["current_name"] = "pittsburg";

    $myHikes = new cHikes();
    $returnData = null;
    $returnData = $myHikes->pushHike($arr);
    echo json_encode($returnData);
    }
}
?>