<?php session_start(); ?>
<?php

require("constantes.php");

if(!isset($_SESSION['admin'])){
header('Location: /web_adminlogin.php');
}else{
	if( isset($_SERVER['HTTP_REFERER']))
	{
	
	
if(isset($_POST['id']) && isset($_POST['b'])){	
    
    $id = $_POST['id'];
 
    $con = mysql_connect(constant("HOST"), constant("USER_NAME"), constant("PASSWORD"));

    if (!$con){
        die('Could not connect: ' . mysql_error());
    }
    
    mysql_select_db(constant("DATABASE"), $con);
    $id = mysql_real_escape_string($id);
    
    date_default_timezone_set('Asia/Calcutta');
    $dateupdate = new DateTime();
    $printdate = $dateupdate->format("Y-m-d H:i:s");
    
    if($_POST['b'] == 'b'){
 
	 $query = "UPDATE public_users SET user_status = -1, `user_status_changed_by`='".$_SESSION['username']."', `user_status_changed_date`='".$printdate."' WHERE id=$id";
         $result = mysql_query($query, $con);
    
    }else{
         $query = "UPDATE public_users SET user_status = 1, `user_status_changed_by`='".$_SESSION['username']."', `user_status_changed_date`='".$printdate."' WHERE id=$id";
         $result = mysql_query($query, $con);
    }
    
    mysql_close($con);
}
	}
	else{
		header('Location: /web_adminlogin.php');
		}
}
?>

