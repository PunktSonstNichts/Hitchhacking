<?php

include_once("../loader.php");

set_time_limit(0);

class driverThread extends Thread {

  private $client;
  private $connected;
  private $first_lat;
  private $first_long;
  private $bearing;
  private $match = false;

  private $myHikes;

  public function __counstruct($client) {
    $this->client = $client;
    $connected = 1;
    $myHikes = new cHikes();
  }

  public function run() {
      if(!$match) {
        $msg = socket_read($client, 10000, PHP_NORMAL_READ) or die("Could not read message");
        if($index == 0) {
          list($first_lat, $first_long) = split(";", $msg);
        } else if($index > 0) {
          list($lat, $long) = split(";", $msg);
          $bearing = getMiddleValue($bearing, $getBearing($first_lat, $first_long, $lat, $long));
          $hikers[][] = $myHikes->getHikerRequests($lat, $long);
          for($i = 0; $i < count($hikers); $i++) {
            if(abs($hikers[$i]["destination_heading"] - $bearing) < 40) {
              // MATCH
            }
          }
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

 private function getMiddleValue($value1, $value2) {
   return ($value1 + $value2) / 2;
 }


}

 ?>
