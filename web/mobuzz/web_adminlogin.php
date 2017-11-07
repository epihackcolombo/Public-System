
<?php
session_start();

if (isset($_SESSION['admin'])) {
    header('Location: web_tcg_cpanel.php');
}
?>

<!DOCTYPE html>
<html lang="en">

    <head>
        <meta charset="utf-8">
        <title>Login - Dengue Breeding Site Reporting Application</title>

        <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
        <meta name="apple-mobile-web-app-capable" content="yes"> 

        <link href="css/bootstrap.min.css" rel="stylesheet" type="text/css" />
        <link href="css/bootstrap-responsive.min.css" rel="stylesheet" type="text/css" />

        <link href="css/font-awesome.css" rel="stylesheet">
        <link href="http://fonts.googleapis.com/css?family=Open+Sans:400italic,600italic,400,600" rel="stylesheet">

        <link href="css/style.css" rel="stylesheet" type="text/css">
        <link href="css/pages/signin.css" rel="stylesheet" type="text/css">

    </head>
    <body class="login_page">

        <div class="navbar navbar-fixed-top">

            <div class="navbar-inner">

                <div class="container">

                    <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </a>

                    <a class="brand" href="#">
                        Mo-Buzz Dengue Management Console	
                    </a>	


                    <div class="nav-collapse">
                        <ul class="nav pull-right">

                            <li class="">						

                            </li>

                            <li class="">						

                            </li>
                        </ul>

                    </div><!--/.nav-collapse -->	

                </div> <!-- /container -->

            </div> <!-- /navbar-inner -->

        </div> <!-- /navbar -->
        <?php
        require("constantes.php");


        $_table = "admin_users";

        if (isset($_GET['logout'])) {
            session_destroy();
            header('Location: web_adminlogin.php');
        }

        if (isset($_POST['submit']) && !empty($_POST["user"]) && !empty($_POST["pwd"])) {

            $user = $_POST['user'];
            $pass = md5($_POST["pwd"]);

            $mysqli = new mysqli(constant("HOST"), constant("USER_NAME"), constant("PASSWORD"), constant("DATABASE")); //$_host, $_user_name, $_password, $_db);
            $user = $mysqli->real_escape_string($user);
            $sql = "SELECT *
                    FROM `admin_users`
                    WHERE (user_status = 1) AND (username = '" . $user . "')";

            //echo $sql;
            $res = $mysqli->query($sql);

            if ($res) {
                $row = mysqli_fetch_array($res);
                if (strcmp($pass, $row['password']) == 0) {
                    $_SESSION['admin'] = "1";
                    $_SESSION['username'] = $row['username'];
                    $_SESSION['full_name'] = $row['full_name'];
                    $mysqli->close();
                    header('Location: web_tcg_cpanel.php');
                } else //account error
                {
                    echo "<div class='account-container'>
	
                            <div class='content clearfix'>

                                    <form action='web_adminlogin.php' method='post' name='input'>

                                            <h1>Member Login</h1>		

                                            <div class='login-fields'>

                                                    <p>Please provide your details</p>

                                                    <div class='field'>
                                                            <label for='username'>Username</label>
                                                            <input type='text' id='username' name='user' value='' placeholder='Username' class='login username-field' />
                                                    </div> <!-- /field -->

                                                    <div class='field'>
                                                            <label for='password'>Password:</label>
                                                            <input type='password' id='password' name='pwd' value='' placeholder='Password' class='login password-field'/>
                                                    </div> <!-- /password -->

                                            </div> <!-- /login-fields -->

                                            <div class='login-actions'>

                                                    <span class='login-checkbox'>
                                                            <input id='Field' name='Field' type='checkbox' class='field login-checkbox' value='First Choice' tabindex='4' />
                                                            <label class='choice' for='Field'>Keep me signed in</label>
                                                    </span>

                                                    <input type='submit' class='button btn btn-success btn-large' name='submit' value='submit'>	

                                            </div> <!-- .actions -->

                                    </form>

                            </div> <!-- /content -->

                    </div>";

                    echo "<script> alert('Your username or password may be incorrect! Or related account may not be an active-account.');</script>";
                }
            } else //db error
            { 
                echo"<div class='account-container'>
	
                    <div class='content clearfix'>

                            <form action='web_adminlogin.php' method='post' name='input'>

                                    <h1>Member Login</h1>		

                                    <div class='login-fields'>

                                            <p>Please provide your details</p>

                                            <div class='field'>
                                                    <label for='username'>Username</label>
                                                    <input type='text' id='username' name='user' value='' placeholder='Username' class='login username-field' />
                                            </div> <!-- /field -->

                                            <div class='field'>
                                                    <label for='password'>Password:</label>
                                                    <input type='password' id='password' name='pwd' value='' placeholder='Password' class='login password-field'/>
                                            </div> <!-- /password -->

                                    </div> <!-- /login-fields -->

                                    <div class='login-actions'>

                                            <span class='login-checkbox'>
                                                    <input id='Field' name='Field' type='checkbox' class='field login-checkbox' value='First Choice' tabindex='4' />
                                                    <label class='choice' for='Field'>Keep me signed in</label>
                                            </span>

                                            <input type='submit' class='button btn btn-success btn-large' name='submit' value='submit'>				
                                    </div> <!-- .actions -->



                            </form>

                    </div> <!-- /content -->

            </div>";

            echo "<script> alert('Oops! Unable to connect. Please retry in a few minutes ...');</script>";
                
            }

            $mysqli->close();
        } else { //if username password are empty - form load section
            echo "<div class='account-container'>
	
                        <div class='content clearfix'>

                                <form action='web_adminlogin.php' method='post' name='input'>

                                        <h1>Member Login</h1>		

                                        <div class='login-fields'>

                                                <p>Please provide your details</p>

                                                <div class='field'>
                                                        <label for='username'>Username</label>
                                                        <input type='text' id='username' name='user' value='' placeholder='Username' class='login username-field' />
                                                </div> <!-- /field -->

                                                <div class='field'>
                                                        <label for='password'>Password:</label>
                                                        <input type='password' id='password' name='pwd' value='' placeholder='Password' class='login password-field'/>
                                                </div> <!-- /password -->

                                        </div> <!-- /login-fields -->

                                        <div class='login-actions'>

                                                <span class='login-checkbox'>
                                                        <input id='Field' name='Field' type='checkbox' class='field login-checkbox' value='First Choice' tabindex='4' />
                                                        <label class='choice' for='Field'>Keep me signed in</label>
                                                </span>

                                                <input type='submit' class='button btn btn-success btn-large' name='submit' value='submit'>				
                                        </div> <!-- .actions -->



                                </form>

                        </div> <!-- /content -->

                </div>";

        }
        ?>




        <div class="login-extra">

        </div> <!-- /login-extra -->


        <script src="js/jquery-1.7.2.min.js"></script>
        <script src="js/bootstrap.js"></script>

        <script src="js/signin.js"></script>

    </body>

</html>
