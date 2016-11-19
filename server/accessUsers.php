<?php

header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");

include_once "loader.php";

$requestsSource = $_SERVER['HTTP_REFERER'];
if ($_SERVER['REQUEST_METHOD'] === 'POST' || true) {
    if ($_POST["api"] == API || true) {
	$f = "loginUsers";
	$email = $_POST["email"];
	$password = $_POST["password"];
	
	$email = "a.j@gmx.de";
	$password = "123456";
	
	$myUsers = new cUsers();
	$returnData = null;
	if ($f == "loginUsers") {
	    $returnData = $myUsers->loginUser($email, $password);
	}
	/*if ($f == "matchUsers") {
	    $returnData = $myUsers->loginUser($user_id);
	}*/
	else {
	    /*
	     * Function can not be found
	     */
	}


	echo json_encode($returnData);
    }
}
?>