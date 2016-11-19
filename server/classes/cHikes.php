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
	$this->db->insert("hikes", $insertArr);
    }
    
    public function upgradeHike($hike_id, $upgradeArr) {
	$this->db->where("hike_id", $hike_id);
	$this->db->update("hikes", $upgradeArr);
    }
    
    public function deleteHike($hike_id, $deleteArr) {
	$this->db->where("hike_id", $hike_id);
	$this->db->update("hikes", $deleteArr);
    }
}

   
   