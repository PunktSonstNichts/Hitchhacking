<?php

header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");

include_once "loader.php";

$requestsSource = $_SERVER['HTTP_REFERER'];
if ($_SERVER['REQUEST_METHOD'] === 'POST' || true) {
    if ($_POST["api"] == API || true) {
	$f = "matchUsers";
	$email = $_POST["email"];
	$password = $_POST["password"];
	
	$user_id = $_POST["user_id"];
	$potential_users = $_POST["potential_users"];
	
	$email = "a.j@gmx.de";
	$password = "123456";
	$user_id = "1";
	$potential_users = "1|2"; //TODO possible error with |
	
	$myUsers = new cUsers();
	$returnData = null;
	if ($f == "loginUsers") {
	    $returnData = $myUsers->loginUser($email, $password);
	}
	else if ($f == "matchUsers") {
	    $returnData = $myUsers->matchUsers($user_id, $potential_users);
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