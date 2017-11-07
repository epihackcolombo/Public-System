<?PHP 

	require "constantes.php";

	$_host = HOST;
	$_user_name = USER_NAME;
	$_password = PASSWORD;
	$_db = DATABASE;
	
	function connect_db(){
	
		global $_host, $_user_name, $_password, $_db;

		$con = new mysqli($_host, $_user_name, $_password, $_db);

    		if ($con ->connect_errno) {
        		printf("Connect failed");
    		} 
		
		return $con;
	
	}	
	
?>