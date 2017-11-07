<?php

require 'db_connection.php';
require 'mob_profile_phi_validate.php';
require 'global_variables.php';
include 'Security.php';


function get_data($user, $strfrom, $strto)
{

    $mysqli = connect_database();

    if (mysqli_connect_errno()) {
        close_db($mysqli);
        return DB_ERROR_CONN_JLIST;
    }

    $user = $mysqli->real_escape_string($user);
    $strfrom = $mysqli->real_escape_string($strfrom);
    $strto = $mysqli->real_escape_string($strto);

    $sql_select_wards = "SELECT * FROM phi_users WHERE username='$user'";

    $comments = $mysqli->query($sql_select_wards);

    if ($comments == false) {
        close_db($mysqli);
        return DB_ERROR_SELECT_JLIST;
    }

    if ($comments->num_rows <= 0) {
        close_db($mysqli);
        return DB_ERROR_NODATA_JLIST;
    } else {

        $row = $comments->fetch_array(MYSQLI_ASSOC);

        $ward_data = $row['ward'];
        $ward_no = explode(",", $ward_data);

        if (!empty($ward_no)) {

            $sql_condition = "WHERE (verified=-1) AND (date_time BETWEEN '$strfrom' AND '$strto') AND (";

            for ($x = 0; $x < sizeof($ward_no); $x++) {

                if ($x < 1) {
                    $sql_condition .= "ward=$ward_no[$x]";
                } else {
                    $sql_condition .= " OR ward=$ward_no[$x]";
                }
            }

            $sql_condition .= ")";
                        
            if ( (strpos($sql_condition, '=)') > 0) OR (strpos($sql_condition, '*') > 0) )
            {
                return LOGIN_USER_ERROR_REAUTH_JSON;
            }
            
            $sql_ward_data = "SELECT * FROM public_data $sql_condition";

            $result = mysqli_query($mysqli, $sql_ward_data);

            $jresult = "[";

            while ($row2 = mysqli_fetch_array($result)) {
				
				if (empty($row2['img_path'])) { // no image
                    $jresult .= "{ 'id':'$row2[id]' , 'image':0, 'imagepath':'', 'gps':'$row2[gps]', 'username':'$row2[username]', 'address':'$row2[address]', 'date':'$row2[date_time]', 'ward':'$row2[ward]', 'remarks':'$row2[remarks]', 'verified':$row2[verified], 'assessment':'$row2[assessment]', 'cmcmessage':'$row2[cmcmessage]'},";
                } else { //has image
                    $jresult .= "{ 'id':'$row2[id]' , 'image':1, 'imagepath':'".strrchr($row2['img_path'],"/")."', 'gps':'$row2[gps]' , 'username':'$row2[username]', 'address':'$row2[address]', 'date':'$row2[date_time]', 'ward':'$row2[ward]', 'remarks':'$row2[remarks]', 'verified':$row2[verified], 'assessment':'$row2[assessment]', 'cmcmessage':'$row2[cmcmessage]'},";
                }
            }

            if (strlen($jresult) > 8) { // 8 is to make sure it gone in to loop

                $jresult = substr($jresult, 0, -1) . "]";

                close_db($mysqli);
                return $jresult;
            }
        }

        close_db($mysqli);
        return "";
    }
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
    $strfrom = "";
    $strto = "";

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

        if (property_exists($json, 'strfrom')) {
            $strfrom = $json->{'strfrom'};
            $strfrom = $FBI->xss_clean($strfrom);
        }

        if (property_exists($json, 'strto')) {
            $strto = $json->{'strto'};
            $strto = $FBI->xss_clean($strto);
        }


        $isvaliduser = user_validate($user, $time_stamp, $uudid);

        if (!empty($isvaliduser)) {

            echo $isvaliduser;
        } else {

            if (!empty($strfrom) && !empty($strto)) {
                echo get_data($user, $strfrom, $strto);
            }
        }
    }
} catch (Exception $e) {
    die(INT_ERROR_SERVER); //nothing to do
}
?>