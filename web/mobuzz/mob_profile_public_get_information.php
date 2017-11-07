<?php

require 'db_connection.php';
require 'profile_public_validate.php';
require 'global_variables.php';
include 'Security.php';


$data = file_get_contents('php://input');

$json = json_decode($data);

$FBI=new CI_Security();

$user = $json->{'user'};
$user=$FBI->xss_clean($user);

$time_stamp = $json->{'time_stamp'};
$time_stamp=$FBI->xss_clean($time_stamp);

$uudid = $json->{'uudid'};
$uudid=$FBI->xss_clean($uudid);

$isvaliduser = user_validate($user, $time_stamp, $uudid);

if (!empty($isvaliduser)) {
    
    echo $isvaliduser;
} else {

    $con = connect_db();

    if (mysqli_connect_errno()) {
        echo DB_ERROR_CONN_JSON;
    }

    $user=   $con->real_escape_string($user);
    $time_stamp=   $con->real_escape_string($time_stamp);
    $time_stamp=   $con->real_escape_string($time_stamp);

    $result = mysqli_query($con, "SELECT * FROM public_users WHERE username='$user'");

    while ($row = mysqli_fetch_array($result)) {
        echo "profiledata," . $row['full_name'] . ", " . $row['email'] . ", " . $row['contact_no'] . ", " . $row['gender'] . ", " . $row['residence'] . ", " . $row['language'] . ", ";
    }
    
    $con->close();
}

?>