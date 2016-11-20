<?php

header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");

include_once "loader.php";

$requestsSource = $_SERVER['HTTP_REFERER'];
if ($_SERVER['REQUEST_METHOD'] === 'POST' || true) {
    if ($_POST["api"] == API || true) {
	$apiKey = $_POST["api"];
	$hike_id = $_POST["hike_id"];
	
	$driver_geo["lat"] = $_POST["driver_lat"];
	$driver_geo["lon"] = $_POST["driver_lon"];
	
	$matchingArr["timestamp_hiker"] = $_POST["timestamp_hiker"];
	$matchingArr["timestamp_hiker_server"] = time();
	$matchingArr["timestamp_driver"] = $_POST["timestamp_driver"];
	$matchingArr["timestamp_driver_server"] = time();

	$arr["user_id"] = $_POST["user_id"];
	$arr["needed_seats"] = $_POST["needed_seats"];
	$arr["security_mode"] =  $_POST["security_mode"];
	$arr["start_timestamp"] = $_POST["start_timestamp"];
	$arr["last_fetch_timestamp"] = 0;
	$arr["start_timestamp_server"] = time();
	$arr["destination_lat"] = $_POST["destination_lat"];
	$arr["destination_lon"] = $_POST["destination_lon"];
	$arr["destination_heading"] = $_POST["destination_heading"];
	$arr["destination_name"] = $_POST["destination_name"];
	$arr["current_lat"] = $_POST["current_lat"];
	$arr["current_lon"] = $_POST["current_lon"];
	$arr["current_name"] = $_POST["current_name"];
	
	$driver_id = $_POST["matchedDrivers"];

	/*
	 * TESTDATA
	 * 
	 */
	  $f = "getHikerRequests";
	  $hike_id = 9;
	  
	  $driver_geo["lat"] = 52.375892;
	  $driver_geo["lon"] = 9.732010;
	  
	  $matchingArr["timestamp_hiker"] = 12313131;
	  $matchingArr["timestamp_hiker_server"] = time();
	  $matchingArr["timestamp_driver"] = 12313131;
	  $matchingArr["timestamp_driver_server"] = time();
	
	  $arr["user_id"] = 3;
	  $arr["needed_seats"] = 1;
	  $arr["security_mode"] = 0;
	  $arr["start_timestamp"] = 12312312324;
	  $arr["last_fetch_timestamp"] = 1479570588;
	  $arr["start_timestamp_server"] = time();
	  $arr["destination_lat"] = 234.234;
	  $arr["destination_lon"] = 234.234;
	  $arr["destination_heading"] = 234.234;
	  $arr["destination_name"] = "london";
	  $arr["current_lat"] = 345.34;
	  $arr["current_lon"] = 345.35;
	  $arr["current_name"] = "pittsburg"; /**/
	  
	$driver_id = "3";
	$matched_hikes = array(1,9,11);

	$myHikes = new cHikes();
	$returnData = null;

	if ($f == "pushHike") {
	    $returnData = $myHikes->pushHike($arr);
	} else if ($f == "upgradeHike") {
	    $updateArr["last_fetch_timestamp"] = $arr["last_fetch_timestamp"];
	    $returnData = $myHikes->hearbeatHike($hike_id, $updateArr);
	} 
	else if ($f == "acceptHikeHiker") {
	    $acceptArr["timestamp_hiker"] = $matchingArr["timestamp_hiker"];
	    $acceptArr["timestamp_hiker_server"] = $matchingArr["timestamp_hiker_server"];
	    $returnData = $myHikes->acceptMatching($hike_id, $acceptArr);
	}
	else if ($f == "acceptHikeDriver") {
	    $acceptArr["timestamp_driver"] = $matchingArr["timestamp_driver"];
	    $acceptArr["timestamp_driver_server"] = $matchingArr["timestamp_driver_server"];
	    $cancelArr["active"] = 0;
	    
	    $returnData = $myHikes->acceptMatching($hike_id, $acceptArr);
	}
	else if ($f == "cancelHikeDriver") {
	    $cancelArr["timestamp_driver"] = 0;
	    $cancelArr["timestamp_driver_server"] = 0;
	    $cancelArr["active"] = 0;
	    
	    $cancelHikeArr["match_proposed"] = 0;
	    $returnData = $myHikes->cancelMatching($hike_id, $cancelArr, $cancelHikeArr);
	}
	else if ($f == "cancelHikeHiker") {
	    $cancelArr["timestamp_hiker"] = 0;
	    $cancelArr["timestamp_hiker_server"] = 0;
	    $cancelArr["active"] = 0;
	    
	    $cancelHikeArr["match_proposed"] = 0;
	    $returnData = $myHikes->cancelMatching($hike_id, $cancelArr, $cancelHikeArr);
	}
	else if ($f == "getHikerRequests") {
	    $returnData = $myHikes->getHikerRequests($driver_geo["lat"], $driver_geo["lon"]);
	}
	else if ($f == "submitMatchings") {
	    $returnData = $myHikes->submitMatchings($driver_id, $matched_hikes);
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