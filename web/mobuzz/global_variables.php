<?PHP 

define("DB_ERROR_CONN","db_error_conn");
define("DB_ERROR_SELECT","db_error_conn");
define("DB_ERROR_UPDATE","db_error_conn");
define("DB_ERROR_INSERT","db_error_conn");

define("DB_ERROR_CONN_JSON","{'status':'error_db_connect', 'language':''}");
define("DB_ERROR_SELECT_JSON","{'status':'error_db_connect', 'language':''}");
define("DB_ERROR_INSERT_JSON","{'status':'error_db_connect', 'language':''}");
define("DB_ERROR_UPDATE_JSON","{'status':'error_db_connect', 'language':''}");
define("DB_ERROR_PARAM_JSON","{'status':'error_db_params', 'language':''}");
define("DB_ERROR_NODATA_JSON","{'status':'sql_no_data', 'language':''}");

define("DB_ERROR_CONN_JLIST","[{'DBError': 'Database connection failed'}]");
define("DB_ERROR_SELECT_JLIST","[{'SQLError': 'SQL select failed'}]");
define("DB_ERROR_NODATA_JLIST","[{'SQLSuccess': 'SQL_no_data'}]");

define("INT_ERROR_SERVER","internal_error");


define("LOGOUT_DB_ERROR_CONN","logout,dberror,");
define("LOGOUT_AUTH_ERROR","logout,autherror,");
define("LOGOUT_DB_ERROR_UPDATE","logout,dberror,");
define("LOGOUT_SUCCESS","logout,ok,");
define("LOGOUT_ERROR","logout,error,");

define("LOGIN_DB_ERROR_UPDATE_JSON","{'status':'error_db_update', 'language':''}");
define("LOGIN_USER_ERROR_AUTH_JSON","{'status':'authentication_failed', 'language':''}");
define("LOGIN_USER_ERROR_BLOCK_JSON","{'status':'authentication_blocked', 'language':''}");
define("LOGIN_USER_ERROR_REAUTH_JSON","{'status':'authentication_required', 'language':''}");
define("LOGIN_USER_ERROR_WARD_JSON","{'status':'ward_required', 'language':''}");

define("PROF_EDIT_SUCCESS","edited,success,");
define("PROF_EDIT_ERROR_AUTH","edited,Invalid Password,");

define("PROF_REG_ERROR_AUTH_DUP","username_exists_db");
define("PROF_REG_SUCCESS","ok");

define("PROF_RESET_SUCCESS","ok,");
define("PROF_RESET_ERROR","no,");

define("MAXMISUSE",3);
define("MAX_THUMB_DIMENSION",400);
define("THUMB_EXT","_thumb.jpg");

?>