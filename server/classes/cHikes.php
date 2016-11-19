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

    public function pushHike() {
	$this->db->query("INSERT ");
    }

   
   