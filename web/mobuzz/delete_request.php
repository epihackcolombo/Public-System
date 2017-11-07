<?php session_start(); ?>

<?php

require("constantes.php");

if(!isset($_SESSION['admin'])){
   header('Location: /web_adminlogin.php');  
}
else{
		if( isset($_SERVER['HTTP_REFERER'])){
	
if(isset($_POST['id'])){	
    $id = $_POST['id'];

    $con = mysql_connect(constant("HOST"), constant("USER_NAME"), constant("PASSWORD"));

    if (!$con){
        die('Could not connect: ' . mysql_error());
    }

    mysql_select_db(constant("DATABASE"), $con);
	$id = mysql_real_escape_string($id);
    $query = " UPDATE `public_data` SET `verified`='-2' WHERE (`id`='".$id."') ";

    if(!mysql_query($query, $con)){
        die('Could not delete data: ' . mysql_error());
    }
	else{ echo '1111';}

    mysql_close($con);
}

}else
{
	 header('Location: /web_adminlogin.php');  
}
}
?>