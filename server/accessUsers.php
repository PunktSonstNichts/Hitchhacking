<?php

header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");

include_once "loader.php";

$requestsSource = $_SERVER['HTTP_REFERER'];
if ($_SERVER['REQUEST_METHOD'] === 'POST' || true) {
    if ($_POST["api"] == API || true) {
	$api = "0210b46f9282682b590b8985fea833f4";//$_POST["api"]
	$f = "loginUser";
	$email = $_POST["email"];
	$password = $_POST["password"];
	
	$potential_users = $_POST["potential_users"];
	
	$email = "a.j@gmx.de";
	$password = "123456";
	$potential_users = "2|3"; //TODO possible error with |
	
	$myUsers = new cUsers();
	$returnData = null;
	if ($f == "loginUser") {
	    $returnData = $myUsers->loginUser($email, $password);
	}
	else if ($f == "matchUsers") {
	    $returnData = $myUsers->matchUsers($api, $potential_users);
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