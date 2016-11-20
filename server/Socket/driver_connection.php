<?php

include_once("../loader.php");

set_time_limit(0);

class driverThread {

  private $client;
  private $connected;
  private $first_lat;
  private $first_long;
  private $bearing;
  private $index = 0;
  private $match = false;

  private $myHikes;

  public function __counstruct($client) {
    $this->client = $client;
    $connected = 1;
    $myHikes = new cHikes();
  }

  public function run() {
    $output = "Test";
    while($connected == 1) {
      if(!$match) {
        $msg = socket_read($client, 10000, PHP_NORMAL_READ) or die("Could not read message");
        if($index == 0) {
          list($first_lat, $first_long) = split(";", $msg);
        } else if($index > 0) {
          list($lat, $long) = split(";", $msg);
          $bearing = getMiddleValue($bearing, $getBearing($first_lat, $first_long, $lat, $long), $index); //use index as count
          $hikers[][] = $myHikes->getHikerRequests($lat, $long);
          for($i = 0; $i < count($hikers); $i++) {
            if(abs($hikers[$i]["destination_heading"] - $bearing) < 40) {
              // MATCH
            }
          }
        }
        $index++;
      }
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

 private function getMiddleValue($prevbearing, $currbearing, ++$count) {
   return (($count-1)$prevbearing + 1 * $currbearing) / $count;
 }


}

public Socket {

  $host = "127.0.0.1";
  $port = 1234;
  $socket;
  $result;

public function __construct() {
  $socket = socket_create(AF_INET, SOCK_STREAM, SOL_TCP) or die("Could not create a new socket\n");
  $result = socket_bind($socket, $host, $port) or die("Could not bind to socket\n");
  $result = socket_listen($socket, SOMAXCONN) or die("Could not start listening");
}

public function run() {
  while(true) {
    $newClient = socket_accept($socket) or die("Could not accept incoming connection\n");
    echo "Driver connected";
    $output = "Test";
    socket_write($newClient, $output."\n", strlen($output) + 1) or die("Could not write output\n");

    // $driver = new driverThread($newClient);
    // $driver->start();
  }
}

}
?>
