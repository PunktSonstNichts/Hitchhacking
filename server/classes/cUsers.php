<?php

class cUsers {

    private $db;
    private $connection_host = CONNECTION_HOST;
    private $username = USERNAME;
    private $password = PASSWORD;
    private $nameOfDB = DB;

    /*
     * PUBLIC FUNCTIONS
     */

    public function cUsers() {
	$this->db = new cDB();
	$this->db->__construct($this->connection_host, $this->username, $this->password, $this->nameOfDB);
	$this->db->connect();
	if (!$this->db) {
	    die("Database error");
	}
    }

    public function loginUser($email, $password) {
	$this->db->join("users_access u_a", "u_a.id=u.id", "LEFT");
	$this->db->where("u_a.email", $email);
	$this->db->where("u_a.password", $password);

	$user = array();
	$user = $this->db->get("users_access u", 1); //LIMIT 1 for safety
	if (!empty($user)) {
	    $suceed_or_fail = true;
	    $api = md5(uniqid($your_user_login, true));
	    if ($api == false || !$suceed_or_fail) {
		/*
		 * Fails
		 */
	    } else {
		$updateArr["api"] = $api;
		$this->db->where("id", $user[0]["id"]);
		$this->db->update("users_access", $updateArr);
		
		$this->db->where("api", $api);
		$user = $this->db->get("users_access", 1);
		
	    }
	}
	return $user;
    }

}
