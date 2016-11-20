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
	$this->db->where("email", $email);
	$this->db->where("password", $password);

	$user = $this->db->get("users", 1); //LIMIT 1 for safety
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
		$this->db->update("users", $updateArr);

		$this->db->where("api", $api);
		$user = $this->db->get("users", 1);
	    }
	}
	return $user;
    }

    public function matchUsers($api, $potential_users) {
	$potential_users_arr = explode("|", $potential_users);
	foreach ($potential_users_arr as $p_u) {
	    $this->db->where("u.id", $p_u, "=", "OR");
	}
	$this->db->join("cities c", "u.city_living_in=c.city_id", "LEFT");
	$this->db->join("tags t_1", "u.tag_1=t_1.tag_id", "LEFT");
	$this->db->join("tags t_2", "u.tag_2=t_2.tag_id", "LEFT");
	$this->db->join("tags t_3", "u.tag_3=t_3.tag_id", "LEFT");
	$userList = $this->db->query("SELECT *, t_1.tag_name AS tag_name_1, t_1.x AS x_1, t_1.y AS y_1, t_2.tag_name AS tag_name_2, t_2.x AS x_2, t_2.y AS y_2, t_3.tag_name AS tag_name_3, t_3.x AS x_3, t_3.y AS y_3 FROM users as u");

	$this->db->where("u.api", $api, "=");
	$this->db->join("cities c", "u.city_living_in=c.city_id", "LEFT");
	$this->db->join("tags t_1", "u.tag_1=t_1.tag_id", "LEFT");
	$this->db->join("tags t_2", "u.tag_2=t_2.tag_id", "LEFT");
	$this->db->join("tags t_3", "u.tag_3=t_3.tag_id", "LEFT");
	$mainUser = $this->db->query("SELECT *, t_1.tag_name AS tag_name_1, t_1.x AS x_1, t_1.y AS y_1, t_2.tag_name AS tag_name_2, t_2.x AS x_2, t_2.y AS y_2, t_3.tag_name AS tag_name_3, t_3.x AS x_3, t_3.y AS y_3 FROM users as u");

	return $this->calculateUserDistance($mainUser, $userList);
    }

    private function calculateUserDistance($mainUser, $userList) {
	var_dump($userList);
	foreach ($userList as $u) {
	    $tempDistance = array();

	    for ($i = 1; $i < 4; $i++) {
		$tempDistance[$i] = $this->distance(array($mainUser["x_" . $i], $mainUser["y_" . $i]), array($u["x_" . $i], $u["y_" . $i]));
	    }
	    $minDistance["a" . $u['id']] = min(array($tempDistance[1], $tempDistance[2], $tempDistance[3]));
	}
	asort($minDistance);
	return $minDistance;
    }

    private function distance($vector1, $vector2) {
	$n = count($vector1);
	$sum = 0;
	for ($i = 0; $i < $n; $i++) {
	    $sum += ($vector1[$i] - $vector2[$i]) * ($vector1[$i] - $vector2[$i]);
	}
	return sqrt($sum);
    }

}
