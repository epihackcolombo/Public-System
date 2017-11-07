<?php  

require_once 'global_variables.php';
include 'Security.php';


function httpGet($url)
{
    $ch = curl_init();  
 
    curl_setopt($ch,CURLOPT_URL,$url);
    curl_setopt($ch,CURLOPT_RETURNTRANSFER,true);
	//curl_setopt($ch,CURLOPT_HEADER, false); 
 
    $output=curl_exec($ch);
 
    curl_close($ch);
    return $output;
}

try {

    $data = file_get_contents('php://input');

    $username = "";
    $strfrom = "";
    $strto = "";

    if (is_object(json_decode($data))) {

        $json = json_decode($data);
        $FBI = new CI_Security();

        if (property_exists($json, 'username')) {
            $username = $json->{'username'};
            $username = $FBI->xss_clean($username);
        }

        if (property_exists($json, 'strfrom')) {
            $strfrom = $json->{'strfrom'};
            $strfrom = $FBI->xss_clean($strfrom);
        }

        if (property_exists($json, 'strto')) {
            $strto = $json->{'strto'};
            $strto = $FBI->xss_clean($strto);
        }

        if (!empty($strfrom) && !empty($strto)) {

			//Server url to CMC-PHI_Server, and get-parameters. token is from server random-user-token
			$get_url = "http://172.21.171.247:8084/ReqWatcher?req_hotspots={'token':'81DC9BDB52D04DC20036DBD8313ED055C87146AD709ECA447DF91DBF6D7D6F27','from':'".$strfrom."','to':'".$strto."'}";
			echo httpGet($get_url);
        }        
        else {
            echo "{'circles': []}";
        }
    }
} catch (Exception $e) {
    die(INT_ERROR_SERVER); //nothing to do
}
?>