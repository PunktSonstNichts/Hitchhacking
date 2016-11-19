<?php

header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");

include_once "loader.php";

$requestsSource = $_SERVER['HTTP_REFERER'];
if ($_SERVER['REQUEST_METHOD'] === 'POST' || true) {
    if ($_POST["api"] == API || true) {
	$apiKey = $_POST["api"];
	$hike_id = $_POST["hike_id"];
	
	$arr["user_id"] = $_POST["user_id"];
	$arr["matched_driver_id"] = 0;
	$arr["matching_state"] = 0;
	$arr["needed_seats"] = $_POST["needed_seats"];
	$arr["security_mode"] =  $_POST["security_mode"];
	$arr["start_timestamp"] = $_POST["start_timestamp"];
	$arr["last_fetch_timestamp"] = 0;
	$arr["accepting_timestamp"] = 0;
	$arr["start_timestamp_server"] = time();
	$arr["delete_timestamp"] = $_POST["delete_timestamp"];
	$arr["delete_timestamp_server"] = time();
	$arr["destination_lat"] = $_POST["destination_lat"];
	$arr["destination_lon"] = $_POST["destination_lon"];
	$arr["destination_heading"] = $_POST["destination_heading"];
	$arr["destination_name"] = $_POST["destination_name"];
	$arr["current_lat"] = $_POST["current_lat"];
	$arr["current_lon"] = $_POST["current_lon"];
	$arr["current_name"] = $_POST["current_name"];

	/*
	 * TESTDATA
	 * 
	 */
	  $f = "deleteHike";
	  $hike_id = 2;
	  $arr["user_id"] = 3;
	  $arr["matched_driver_id"] = 0;
	  $arr["matching_state"] = 0;
	  $arr["needed_seats"] = 1;
	  $arr["security_mode"] = 0;
	  $arr["start_timestamp"] = 12312312324;
	  $arr["last_fetch_timestamp"] = 1479570588;
	  $arr["accepting_timestamp"] = 0;
	  $arr["start_timestamp_server"] = time();
	  $arr["delete_timestamp"] = 24355345345;
	  $arr["delete_timestamp_server"] = time();
	  $arr["destination_lat"] = 234.234;
	  $arr["destination_lon"] = 234.234;
	  $arr["destination_heading"] = 234.234;
	  $arr["destination_name"] = "london";
	  $arr["current_lat"] = 345.34;
	  $arr["current_lon"] = 345.35;
	  $arr["current_name"] = "pittsburg"; /**/

	$myHikes = new cHikes();
	$returnData = null;

	if ($f == "pushHike") {
	    $returnData = $myHikes->pushHike($arr);
	} else if ($f == "upgradeHike") {
	    $updateArr["last_fetch_timestamp"] = $arr["last_fetch_timestamp"];
	    $returnData = $myHikes->upgradeHike($hike_id, $updateArr);
	} else if ($f == "deleteHike") {
	    $deleteArr["delete_timestamp"] = $arr["delete_timestamp"];
	    $deleteArr["delete_timestamp_server"] = $arr["delete_timestamp_server"];
	    $returnData = $myHikes->deleteHike($hike_id, $deleteArr);
	} else if ($f == "deleteHike") {
	    $deleteArr["delete_timestamp"] = $arr["delete_timestamp"];
	    $deleteArr["delete_timestamp_server"] = $arr["delete_timestamp_server"];
	    $returnData = $myHikes->deleteHike($hike_id, $deleteArr);
	}
	else {
	    /*
	     * Function can not be found
	     */
	}


	echo json_encode($returnData);
    }
}
?>