<?php

require 'db_connection.php';
require 'mob_profile_phi_validate.php';
require 'global_variables.php';
include 'Security.php';


function get_data($id, $ward, $cmcmessage, $misusecounter, $blockuser, $user, $publicuser, $status)
{

    $mysqli = connect_database();

    if (mysqli_connect_errno()) {
        return DB_ERROR_CONN;
    }

    $id = $mysqli->real_escape_string($id);
    $ward = $mysqli->real_escape_string($ward);
    $cmcmessage = $mysqli->real_escape_string($cmcmessage);
    $misusecounter = $mysqli->real_escape_string($misusecounter); //Client is sending whether public-user had misused the app
    $blockuser = $mysqli->real_escape_string($blockuser);
    $user = $mysqli->real_escape_string($user);
    $publicuser = $mysqli->real_escape_string($publicuser);

    date_default_timezone_set('Asia/Kolkata');
    $date = new DateTime();
    $printdate = $date->format("Y-m-d H:i:s");

    if ($status < 0) //didn't change the cmcmessage
    {
        $sql_update_report = "UPDATE public_data SET ward='$ward', verified_by='$user', verified_date_time='$printdate' WHERE id='$id'";

        if (!$mysqli->query($sql_update_report)) {
            close_db($mysqli);
            return DB_ERROR_UPDATE;
        }
    }
    else if ($misusecounter > 0) {

        //update user
        $sql_select = "SELECT user_misuse_counter FROM public_users WHERE username='$publicuser'";

        $comments = $mysqli->query($sql_select);

        if ($comments == false) {
            close_db($mysqli);
            return DB_ERROR_SELECT;
        }

        $row = $comments->fetch_array(MYSQLI_ASSOC);

        $user_misuse_counter = $row['user_misuse_counter'];
        $user_misuse_counter += 1;
		
		$sql_update_user = "";
		
		if($user_misuse_counter > MAXMISUSE)
		{
			$blockuser=1;
			$cmcmessage="Your account has been auto blocked.";
			
			$sql_update_user = "UPDATE public_users SET user_status=-1, user_misuse_counter=$user_misuse_counter, user_status_changed_by='$user', user_status_changed_date='$printdate'  WHERE username='$publicuser'";
		}
		else
		{
			$sql_update_user = "UPDATE public_users SET user_misuse_counter='$user_misuse_counter' WHERE username='$publicuser'";
		}

		if (!$mysqli->query($sql_update_user)) {
			close_db($mysqli);
			return DB_ERROR_UPDATE;
		}
		 //update report status
		$sql_update_report = "UPDATE public_data SET ward='$ward', cmcmessage='$cmcmessage', verified=-1, verified_by='$user', verified_date_time='$printdate' WHERE id='$id'";

		if (!$mysqli->query($sql_update_report)) {
			close_db($mysqli);
			return DB_ERROR_UPDATE;
		}

    } else {
	
		//Note: status = 8, is rejected based on lack of information. It's not a misuse of the app

		$sql_update_report = "";
		
		if($status == 8)
		{
			$sql_update_report = "UPDATE public_data SET ward='$ward', cmcmessage='$cmcmessage', verified=-1, verified_by='$user', verified_date_time='$printdate' WHERE id='$id'";
		}
		else
		{
			$sql_update_report = "UPDATE public_data SET ward='$ward', cmcmessage='$cmcmessage', verified=1, verified_by='$user', verified_date_time='$printdate' WHERE id='$id'";
		}

        if (!$mysqli->query($sql_update_report)) {
            close_db($mysqli);
            return DB_ERROR_UPDATE;
        }
    }


    close_db($mysqli);

    return "action#ok";
}

function connect_database()
{
    $con = connect_db();
    return $con;
}

function close_db($con)
{
    $con->close();
}

try {

    $data = file_get_contents('php://input');

    $user = "";
    $time_stamp = "";
    $uudid = "";

    $id = "";
    $ward = "";
    $cmcmessage = "";
    $misusecounter = ""; //Not from the database. Form the app. It's [0] or [1]
    $blockuser = "";
    $publicuser = "";
    $status = "";


    if (is_object(json_decode($data))) {

        $json = json_decode($data);
        $FBI = new CI_Security();


        if (property_exists($json, 'user')) {
            $user = $json->{'user'};
            $user = $FBI->xss_clean($user);
        }

        if (property_exists($json, 'time_stamp')) {
            $time_stamp = $json->{'time_stamp'};
            $time_stamp = $FBI->xss_clean($time_stamp);
        }

        if (property_exists($json, 'uudid')) {
            $uudid = $json->{'uudid'};
            $uudid = $FBI->xss_clean($uudid);
        }


        if (property_exists($json, 'id')) {
            $id = $json->{'id'};
            $id = $FBI->xss_clean($id);
        }

        if (property_exists($json, 'ward')) {
            $ward = $json->{'ward'};
            $ward = $FBI->xss_clean($ward);
        }

        if (property_exists($json, 'cmcmessage')) {
            $cmcmessage = $json->{'cmcmessage'};
            $cmcmessage = $FBI->xss_clean($cmcmessage);
        }

        if (property_exists($json, 'misusecounter')) {
            $misusecounter = $json->{'misusecounter'};
            $misusecounter = $FBI->xss_clean($misusecounter);
        }

        if (property_exists($json, 'blockuser')) {
            $blockuser = $json->{'blockuser'};
            $blockuser = $FBI->xss_clean($blockuser);
        }

        if (property_exists($json, 'publicuser')) {
            $publicuser = $json->{'publicuser'};
            $publicuser = $FBI->xss_clean($publicuser);
        }
		
		if (property_exists($json, 'status')) {
            $status = $json->{'status'};
			$status = $FBI->xss_clean($status);
        }


        $isvaliduser = user_validate($user, $time_stamp, $uudid);

        if (!empty($isvaliduser)) {

            echo $isvaliduser;
        } else {

            $response = get_data($id, $ward, $cmcmessage, $misusecounter, $blockuser, $user, $publicuser, $status);

            echo $response;
        }
    }
} catch (Exception $e) {
    die("internal_error"); //nothing to do
}
?>