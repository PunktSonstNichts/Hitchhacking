<?php

set_time_limit(0);

// class driverThread extends Thread {
//
//   private $client;
//   private $connected;
//
//   public function __counstruct($client) {
//     $this->client = $client;
//     $connected = 1;
//   }
//
//   public function run() {
//     $output = "Test";
//     while($connected == 1) {
//       socket_write($client, output."\n", strlen($output) + 1) or die("Could not write output\n");
//     }
//
//   }
//
//
// }

phpinfo();

$host = "127.0.0.1";
$port = 1234;

$socket = socket_create(AF_INET, SOCK_STREAM, SOL_TCP) or die("Could not create a new socket\n");
$result = socket_bind($socket, $host, $port) or die("Could not bind to socket\n");
$result = socket_listen($socket, SOMAXCONN);

echo "Socket gestartet";
while(true) {
  $newClient = socket_accept($socket) or die("Could not accept incoming connection\n");
    echo "Driver connected";
    $output = "Test";
    socket_write($newClient, $output."\n", strlen($output) + 1) or die("Could not write output\n");

    // $driver = new driverThread($newClient);
    // $driver->start();
}
socket_close($socket);
 ?>
