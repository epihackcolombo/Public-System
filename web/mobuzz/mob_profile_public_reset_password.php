<?php

require 'db_connection.php';
require 'global_variables.php';
include 'Security.php';

$data = file_get_contents('php://input');

$user = "";
$h_user = "";
$email = "";

$FBI=new CI_Security();

$json = json_decode($data);

if (property_exists($json, 'user')) {
	$user = $json->{'user'};
	$user=$FBI->xss_clean($user);
}

if (property_exists($json, 'h_user')) {
	$h_user = $json->{'h_user'};
	$h_user=$FBI->xss_clean($h_user);
}

if (property_exists($json, 'email')) {
	$email = $json->{'email'};
	$email=$FBI->xss_clean($email);
}

$con = connect_db();

if (mysqli_connect_errno()) {
    echo DB_ERROR_CONN_JSON;
}

$user = $con->real_escape_string($user);
$h_user = $con->real_escape_string($h_user);
$email = $con->real_escape_string($email);

$result = mysqli_query($con, "SELECT email FROM public_users WHERE username ='$user'");

$s_email = '';

while ($row = mysqli_fetch_array($result)) {
    $s_email = $row['email'];
}

if ($s_email == $email) {

    mysqli_query($con, "UPDATE public_users SET password = '$h_user' WHERE username = '$user'");

    echo PROF_RESET_SUCCESS;
} else {
    echo PROF_RESET_ERROR;
}
?>