<?php
require("util_manage_time.php");

try {

    //GET parameters
    $data = filter_input(INPUT_GET, 'q');
    $param = explode("$", $data);

    if (count($param) >= 2 and $param[0] > 0 and $param[1] > 0) {
	
		$datearray = getTimeStatus($param[0], $param[1]);
		echo $datearray[0].'$'.$datearray[1].'$'.$datearray[4].'$'.$datearray[5];//toDate, fromDate, fromYear, fromWeek
    }

} catch (Exception $e) {
    die(""); //nothing to do
}

?>
