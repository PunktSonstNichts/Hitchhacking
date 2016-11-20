<?php

class cHikes {

    private $db;
    private $connection_host = CONNECTION_HOST;
    private $username = USERNAME;
    private $password = PASSWORD;
    private $nameOfDB = DB;

    /*
     * PUBLIC FUNCTIONS
     */

    public function cHikes() {
	$this->db = new cDB();
	$this->db->__construct($this->connection_host, $this->username, $this->password, $this->nameOfDB);
	$this->db->connect();
	if (!$this->db) {
	    die("Database error");
	}
    }

    public function pushHike($api, $insertArr) {
	$this->db->where("api", $api);
	$user = $this->db->get("users", 1);
	$insertArr["user_id"] = $user[0]["id"];
	if(!empty($user)){
	$result = $this->db->insert("hikes", $insertArr);
	}
	if (!empty($result)) {
	    return "success";
	}
    }

    public function heartbeatHike($hike_id, $upgradeArr) {
	$this->db->where("hike_id", $hike_id);
	$result = $this->db->update("hikes", $upgradeArr);
	if (!empty($result)) {
	    return "success";
	}
    }

    public function cancelMatching($hike_id, $deleteArr, $deleteHikeArr) {
	$this->db->where("hike_id", $hike_id);
	$result = $this->db->update("matchings", $deleteArr);

	$this->db->where("hike_id", $hike_id);
	$result .= $this->db->update("hikes", $deleteHikeArr);

	if (!empty($result)) {
	    return "success";
	}
    }

    public function acceptMatching($hike_id, $acceptArr) {
	$this->db->where("hike_id", $hike_id);
	$result = $this->db->update("matchings", $acceptArr);

	if (!empty($result)) {
	    return "success";
	}
    }

    public function getHikerRequests($driver_lat, $driver_lon) {

	return $this->db->query("SELECT *, ( 3959 * acos( cos( radians(" . $driver_lat . ") ) * cos( radians( h.current_lat) ) 
		    * cos( radians(h.current_lon) - radians(" . $driver_lon . ")) + sin(radians(" . $driver_lat . ")) 
		    * sin( radians(h.current_lat)))) AS distance FROM hikes h"
			. " HAVING distance < 0.6
		    ORDER BY distance DESC");
    }

    public function submitMatchings($driver_id, $matched_hikes) {
	if (!empty($matched_hikes)) {

	    $insertArr = array();
	    $insertArr["matched_driver_id"] = $driver_id;

	    $result = "";
	    $priority = 1;
	    foreach ($matched_hikes as $m) {
		$matchedHikeArr["match_proposed"] = 1;
		$this->db->where("hike_id", $m);
		$result .= $this->db->update("hikes", $matchedHikeArr);


		$this->db->where("m.hike_id", $m);
		$exisiting_matches = $this->db->get("matchings m");

		if (empty($exisiting_matches)) {
		    $insertArr["active"] = 1;
		    $insertArr["priority"] = $priority;
		    $insertArr["hike_id"] = $m;
		    $result .= $this->db->insert("matchings", $insertArr);
		    $priority++;
		}
	    }
	}

	if (!empty($result)) {
	    return "success";
	}
    }

}
