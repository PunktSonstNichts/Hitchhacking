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

    public function pushHike($insertArr) {
	$result = $this->db->insert("hikes", $insertArr);
	if (!empty($result)) {
	    return "success";
	}
    }

    public function upgradeHike($hike_id, $upgradeArr) {
	$this->db->where("hike_id", $hike_id);
	$result = $this->db->update("hikes", $upgradeArr);
	if (!empty($result)) {
	    return "success";
	}
    }

    public function deleteMatching($hike_id, $deleteArr) {
	$this->db->where("hike_id", $hike_id);
	$result = $this->db->update("matchings", $deleteArr);

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
		    ." HAVING distance < 0.6
		    ORDER BY distance DESC");
    }

}
