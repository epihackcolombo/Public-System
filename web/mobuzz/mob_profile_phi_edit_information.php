<?php

require 'db_connection.php';
require 'mob_profile_phi_validate.php';
include 'Security.php';

$data = file_get_contents('php://input');

$json = json_decode($data);

$FBI=new CI_Security();


$user = $json->{'user'};
$user=$FBI->xss_clean($user);

$password = $json->{'password'};
$password=$FBI->xss_clean($password);

$name = $json->{'name'};
$name=$FBI->xss_clean($name);

$email = $json->{'email'};
$email=$FBI->xss_clean($email);

$residence = $json->{'residence'};
$residence=$FBI->xss_clean($residence);

$language = $json->{'language'};
$language=$FBI->xss_clean($language);

$m_mobile = $json->{'p_mobile'};
$m_mobile=$FBI->xss_clean($m_mobile);

$newpassword = $json->{'newpassword'};
$newpassword=$FBI->xss_clean($newpassword);

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
        echo "Failed to connect to MySQL: " . mysqli_connect_error();
    }

    $user = $con->real_escape_string($user);
    $password=   $con->real_escape_string($password);
    $name=   $con->real_escape_string($name);
    $email=   $con->real_escape_string($email);
    $residence=   $con->real_escape_string($residence);
    $language=   $con->real_escape_string($language);
    $m_mobile=   $con->real_escape_string($m_mobile);
    $newpassword=   $con->real_escape_string($newpassword);
    $time_stamp=   $con->real_escape_string($time_stamp);
    $uudid=   $con->real_escape_string($uudid);

    $result = mysqli_query($con, "SELECT password FROM phi_users WHERE username='$user'");

    $s_password;

    while ($row = mysqli_fetch_array($result)) {
        $s_password = $row['password'];
    }

    if ($s_password == $password) {
        mysqli_query($con, "UPDATE phi_users SET email='$email', full_name = '$name', language = '$language', contact_no = '$m_mobile' WHERE username='$user'");


        if ($newpassword != null) {
            mysqli_query($con, "UPDATE phi_users SET password = '$newpassword' WHERE username='$user'");
        }

        echo "edited,success,";
    } else {

        echo "edited,Invalid Password,";
    }
    
    $con->close();
}
?>