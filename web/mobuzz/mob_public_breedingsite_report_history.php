<?php

require 'db_connection.php';
require 'profile_public_validate.php';
require 'global_variables.php';
include 'Security.php';

function get_data($user) {

    $con = connect_db();

    if (mysqli_connect_errno()) {
        //echo "Failed to connect to MySQL: " . mysqli_connect_error();
        echo DB_ERROR_CONN_JSON;
    }
    $user=   $con->real_escape_string($user);

    $result = mysqli_query($con, "SELECT * FROM public_data WHERE username='$user' ORDER BY id");

    $jresult = "[";

    while ($row = mysqli_fetch_array($result)) {

        if (empty($row['img_path'])) { // no image
            $jresult .= "{ 'id':'$row[id]' , 'image':0, 'imagepath':'', 'gps':'$row[gps]' , 'address':'$row[address]', 'date':'$row[date_time]', 'ward':'$row[ward]', 'remarks':'$row[remarks]', 'assessment':'$row[assessment]', 'cmcmessage':'$row[cmcmessage]'},";
        } else {//has image
            $jresult .= "{ 'id':'$row[id]' , 'image':1, 'imagepath':'".strrchr($row['img_path'],"/")."', 'gps':'$row[gps]' , 'address':'$row[address]', 'date':'$row[date_time]', 'ward':'$row[ward]', 'remarks':'$row[remarks]', 'assessment':'$row[assessment]', 'cmcmessage':'$row[cmcmessage]'},";
        }
    }

    $jresult .= "{ 'id':'', 'image':1, 'gps':'', 'address':'','date':'', 'ward':'', 'remarks':'', 'assessment':'', 'cmcmessage':''}]";

    return $jresult;
}

header('Content-Type: application/json');

try {

    $data = file_get_contents('php://input');

    $user = "";
    $time_stamp = "";
    $uudid = "";
	
	$FBI=new CI_Security();

    if (is_object(json_decode($data))) {

        $json = json_decode($data);


        if (property_exists($json, 'user')) {
            $user = $json->{'user'};
			$user=$FBI->xss_clean($user);
        }

        if (property_exists($json, 'time_stamp')) {
            $time_stamp = $json->{'time_stamp'};
			$time_stamp=$FBI->xss_clean($time_stamp);
        }

        if (property_exists($json, 'uudid')) {
            $uudid = $json->{'uudid'};
			$uudid=$FBI->xss_clean($uudid);
        }

        
        $isvaliduser = user_validate($user, $time_stamp, $uudid);

        if (!empty($isvaliduser)) {

            echo $isvaliduser;
        } else {

            $response = get_data($user);
            echo $response;
        }
        
    }
} catch (Exception $e) {
    die(INT_ERROR_SERVER); //nothing to do
}

?>