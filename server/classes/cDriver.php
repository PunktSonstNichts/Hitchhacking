<?php

class cDriver {

    private $db;
    private $connection_host = CONNECTION_HOST;
    private $username = USERNAME;
    private $password = PASSWORD;
    private $nameOfDB = DB;

    /*
     * PUBLIC FUNCTIONS
     */

    public function cDriver() {
	$this->db = new cDB();
	$this->db->__construct($this->connection_host, $this->username, $this->password, $this->nameOfDB);
	$this->db->connect();
	if (!$this->db) {
	    die("Database error");
	}
    }

    public function pushDriver($id, $first_lat, $first_long) {

    }

    public function pushLocation($id, $lat, $long) {
      $this->db->where("driverid", $id);

      $driver = $this->db->get("drivers", 1);
      if(!empty($driver)) {
        // TODO $bearing = $this->getMiddleValue($bearing, $this->getBearing($first_lat, $first_long, $lat, $long), $dataIndex);
        // TODO Compare with hikes
        // TODO Send Answer Matching
      }
    }

    private function getBearing($lat1, $lon1, $lat2, $lon2) {
     //difference in longitudinal coordinates
     $dLon = deg2rad($lon2) - deg2rad($lon1);

     //difference in the phi of latitudinal coordinates
     $dPhi = log(tan(deg2rad($lat2) / 2 + pi() / 4) / tan(deg2rad($lat1) / 2 + pi() / 4));

     //we need to recalculate $dLon if it is greater than pi
     if(abs($dLon) > pi()) {
        if($dLon > 0) {
           $dLon = (2 * pi() - $dLon) * -1;
        }
        else {
           $dLon = 2 * pi() + $dLon;
        }
     }
     //return the angle, normalized
     return (rad2deg(atan2($dLon, $dPhi)) + 360) % 360;
   }

   private function getMiddleValue($prevbearing, $currbearing, $count) {
     return ((($count-1)*$prevbearing + 1 * $currbearing) / $count);
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

    public function editUserProfile($api, $profileArr) {
	$this->db->where("api", $api);
	$result = $this->db->update($profileArr);
	if (!empty($result)) {
	    return "success";
	}
    }


}
