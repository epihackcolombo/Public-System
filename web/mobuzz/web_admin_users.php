<?php session_start(); 
date_default_timezone_set('UTC');
include 'Security.php';
$CI = new CI_Security();
?>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <title>Public Users - Dengue Breeding Site Reporting Application</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
        <meta name="apple-mobile-web-app-capable" content="yes">
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="css/bootstrap-responsive.min.css" rel="stylesheet">
        <link href="http://fonts.googleapis.com/css?family=Open+Sans:400italic,600italic,400,600"
              rel="stylesheet">
        <link href="css/font-awesome.css" rel="stylesheet">
        <link href="css/style.css" rel="stylesheet">
        <link href="css/pages/dashboard.css" rel="stylesheet">
        <!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
        <!--[if lt IE 9]>
              <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
            <![endif]-->
    </head>
    <body>
        <?php
        require("constantes.php");

        if (!isset($_SESSION['admin'])) {
            header('Location: web_adminlogin.php');
        }
        ?>
        
        <?php

            $con = mysql_connect(constant("HOST"), constant("USER_NAME"), constant("PASSWORD"));

            if (!$con) {
                die('Could not connect: ' . mysql_error());
            }

            mysql_select_db(constant("DATABASE"), $con);


            if (isset($_POST['firstname']) && isset($_POST['admin_username']) && isset($_POST['admin_password'])) {

                $query1 = "SELECT
                            admin_users.id
                            FROM
                            admin_users
                            WHERE
                            admin_users.username = '" . mysql_real_escape_string($_POST['admin_username']) . "'";

                $result = mysql_query($query1, $con);
                $row = mysql_fetch_array($result, MYSQL_ASSOC);

                if ($row == 0) {
                    $query = "INSERT INTO admin_users (admin_users.full_name,
                                admin_users.username,
                                admin_users.`password`)
                                VALUES ('" . $CI->xss_clean(mysql_real_escape_string($_POST['firstname'])) . "','" . $CI->xss_clean(mysql_real_escape_string($_POST['admin_username'])) . "','" . md5($_POST['admin_password']) . "')";

                    mysql_query($query, $con);

                    echo '<script>alert("New admin-user is added to the system.")</script>';
                } else {
                    echo '<script>alert("Username is already taken. Please choose a different username!")</script>';
                }
            }

            //---------------- update user ----------------//

            if(isset($_POST['adminquery']))
            {

                if(isset($_POST['npassword']) && strlen($_POST['npassword'])>=6)
                {
                    $sqladminupdate = "UPDATE `mobuzz_db`.`admin_users` SET `admin_users`.`full_name`='".$CI->xss_clean(mysql_real_escape_string($_POST['nfname']))."', `password` = '".md5($_POST['npassword'])."' WHERE `admin_users`.`id`=".$_POST['adminquery'];
                }
                else {

                   $sqladminupdate = "UPDATE `mobuzz_db`.`admin_users` SET `admin_users`.`full_name`='".$CI->xss_clean(mysql_real_escape_string($_POST['nfname']))."' WHERE `admin_users`.`id`=".$_POST['adminquery'];
                }

                  mysql_query($sqladminupdate, $con);
            }
            else {
                //echo "no query!!!";
            }

            $currentadminpass =  "";
            $currentadminfname =  "";
            $currentadminid=-1;
            
            $sqladminselect = "SELECT id, full_name, password FROM mobuzz_db.admin_users where username='".$_SESSION['username']."'";
            $resultselect = mysql_query($sqladminselect, $con);
            $row = mysql_fetch_array($resultselect, MYSQL_ASSOC);
            
            $currentadminid = $row['id'];
            $currentadminfname = $row['full_name'];
            $currentadminpass = $row['password'];

        ?> 


        <div class="navbar navbar-fixed-top">
            <div class="navbar-inner">
                <div class="container"> <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse"><span
                            class="icon-bar"></span><span class="icon-bar"></span><span class="icon-bar"></span> </a><a class="brand" href="web_tcg_cpanel.php">Mo-Buzz Dengue Management Console</a>
                    <div class="nav-collapse">
                        <ul class="nav pull-right">
                            <li class="dropdown"><a href="#" class="dropdown-toggle" data-toggle="dropdown"><span style="color:#333;font-size:14px;"><i
                                            class="icon-user"></i>&nbsp;<strong>Hi&nbsp;&nbsp;</strong><?php echo $_SESSION['username'] ?></span><b class="caret"></b></a>
                                <ul class="dropdown-menu">
                                    <li><a href="web_adminlogin.php?logout=logout">Logout</a></li>
                                </ul>
                            </li>
                        </ul>
                        <!-- <form class="navbar-search pull-right">
                           <input type="text" class="search-query" placeholder="Search">
                         </form> -->
                    </div>
                    <!--/.nav-collapse --> 
                </div>
                <!-- /container --> 
            </div>
            <!-- /navbar-inner --> 
        </div>
        <!-- /navbar -->
        <div class="subnavbar">
            <div class="subnavbar-inner">
                <div class="container">
                    <ul class="mainnav" style="margin-right: -200px;">
                        <li><a href="web_tcg_cpanel.php"><i class="icon-dashboard"></i><span>Dashboard</span> </a> </li>
                        <li><a href="web_requests.php"><i class="icon-share"></i><span>Complaints</span> </a> </li>
                        <li ><a href="web_phi.php"><i class="icon-user-md"></i><span>PHI</span> </a></li>
                        <li><a href="web_map.php"><i class="icon-map-marker"></i><span>Map</span> </a> </li>
                        <li class="dropdown"><a href="javascript:;" class="dropdown-toggle" data-toggle="dropdown"> <i class="icon-long-arrow-down"></i><span>Clusters</span> <b class="caret"></b></a>
                            <ul class="dropdown-menu">
                                <li><a href="web_map_public_show.php"><i class="icon-map-marker"></i><span>&nbsp;Generate clusters</span> </a></li>
                                <li><a href="web_map_public_show_data.php"><i class="icon-map-marker"></i><span>&nbsp;Cluster data</span> </a>   </li> 
                            </ul></li>
                        <li><a href="web_public_users.php"><i class="icon-group"></i><span>Public Users</span> </a> </li>
                        <li><a href="web_requests_byuser.php"><i class="icon-search"></i><span>Search</span> </a> </li>
                        <li><a href="web_gallery.php"><i class="icon-th-large"></i><span>Gallery</span> </a> </li>
                        <li><a href="web_download.php"><i class="icon-download-alt"></i><span>Downloads</span> </a> </li>
                        <li><a href="web_moh_details.php"><i class="icon-home"></i><span>MOH</span> </a> </li>
                        <li  class="active"><a href="web_admin_users.php"><i class="icon-briefcase"></i><span>Administrator</span> </a> </li> 
                        <li><a href="#adminModal" data-toggle="modal"><i class="icon-plus-sign"></i><span>New Administrator</span> </a> </li> 
                        
                    </ul>
                    <!-- Modal -->

                    <div id="adminModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="adminModalLabel" aria-hidden="true">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                            <h3 id="adminModalLabel">Add new admin</h3>
                        </div>

                        <div class="modal-body">

                            <form id="InfroText" method="POST">

                                <input type="hidden" name="InfroText" value="1">

                                <table>
                                    <tbody><tr><td>Full name</td><td><input type="text" name="firstname" id="title" style="width:300px"><span class="hide help-inline">This is required! Your full name must be at least 5 characters.</span></td></tr>
                                        <tr><td>Username</td><td><input type="text" name="admin_username"  id="title1" style="width:300px"><span class="hide help-inline">This is required! Username must be at least 6 characters long.</span></td></tr>
                                        <tr><td>Password</td><td><input type="password" name="admin_password" id="title2" style="width:300px"><span class="hide help-inline">This is required! Your password must be at least 6 characters long.</span></td></tr>
                                        <tr><td>Confirm Password</td><td><input type="password" name="admin_password2" id="title3" style="width:300px"><span class="hide help-inline">This is required, or not matching with the password.</span></td></tr>
                                    </tbody>
                                </table>
                            </form>
                        </div>
                        <div class="modal-footer">
                            <button class="btn" data-dismiss="modal" aria-hidden="true">Close</button>
                            <button class="btn btn-primary" data-dismiss="modal" id="InfroTextSubmit">Save changes</button>
                        </div>
                    </div>



                </div> <!-- /controls -->	
            </div> <!-- /control-group -->

        </div>
        <!-- /container --> 
    </div>
    <!-- /subnavbar-inner --> 
</div>
<!-- /subnavbar -->
<div class="main">
    <div class="main-inner">
        <div class="container">
            <div class="row">
                <div class="span12">
                    
                    <div class="widget widget-table action-table">
                        <div class="widget-header"> <i class="icon-briefcase"></i>
                            <h3><?php echo $_SESSION['username'] ?>'s detail</h3>
                        </div>
                        <!-- /widget-header -->
                        <div class="widget-content">
                            
                            <div style="margin-left:12px; margin-top:10px; margin-bottom:15px;">

                                <form id="adminupdate" method="POST">
                                
                                    <table>
                                        <tr>
                                            <td><h3>UserName </h3></p></td>
                                            <td><h3> : <span style="color:#FF7F74;"><?php echo $_SESSION['username']; ?></span></h3></p></td>
                                        </tr>
                                        <tr>
                                            <td><h3>Name </h3></p></td>
                                            <td><h3> : <span style="color:#FF7F74;"><input type="text" id="nfname" name="nfname" value='<?php echo $currentadminfname; ?>'></span></h3></p></td>
                                        </tr>
                                        <tr>
                                            <td><h3>New Password </h3></p></td>
                                            <td><h3> : <span style="color:#FF7F74;"><input type="password" id="npassword" name="npassword"></span></h3></p></td>
                                        </tr>
                                        <tr>
                                            <td><h3>Confirm New Password </h3></p></td>
                                            <td><h3> : <span style="color:#FF7F74;"><input type="password" id="ncpassword" name="ncpassword"></span></h3></p></td>
                                        </tr>
                                        <tr>
                                            <td><h3>Current Password </h3></p></td>
                                            <td><h3> : <span style="color:#FF7F74;"><input type="password" id="cpassword" name="cpassword"></span></h3></p></td>
                                        </tr>
                                        <tr>
                                            <td><input type='button' class='btn btn-small btn-warning' id="submitadminbutton" value=' Save Changes '/></td>
                                            <td><input type="hidden" id="adminquery" name="adminquery" value=""></td>
                                        </tr>
                                    </table>
                                    
                                </form>

                            </div>

                        </div>
                    </div>
                    
                    
                    <div class="widget widget-table action-table">
                        <div class="widget-header"> <i class="icon-briefcase"></i>
                            <h3> Admin user's detail</h3>
                        </div>
                        <!-- /widget-header -->
                        <div class="widget-content">

                            <?php
                            $con = mysql_connect(constant("HOST"), constant("USER_NAME"), constant("PASSWORD"));

                            if (!$con) {
                                die('Could not connect: ' . mysql_error());
                            }

                            mysql_select_db(constant("DATABASE"), $con);

                            $query = "SELECT * FROM admin_users WHERE user_status > -2  ORDER BY admin_users.username ASC";

                            $comments = mysql_query($query, $con);
                            
                            
                            ?>   
                            <table class="table table-striped table-bordered">
                                <thead>
                                    <tr>
                                        <th> </th>
                                        <th> Username</th>
                                        <th> Full Name</th>
                                        <th> Status &nbsp;&nbsp; > ></th>
                                        <th> Changed By</th>
                                        <th> Changed Date</th>
                                    </tr>
                                </thead>
                                <tbody>

                                    <?php
                                    
                                    $x = 0;
                                    while ($row = mysql_fetch_array($comments, MYSQL_ASSOC)) {
                                        $x = $x + 1;
                                        echo "<tr>";
                                        echo "<td>" . $x . "</td>";
                                        echo "<td>" . $row['username'] . "</td>";
                                        echo "<td>" . $row['full_name'] . "</td>";

                                        if ($row['user_status'] == '1') {
                                            echo "<td><div align='center'><input type='button' class='btn btn-small btn-danger' value='Block' onclick='block(" . $row['id'] . ");' /></div></td>";
                                        } else {
                                            echo "<td><div align='center'><input type='button' class='btn btn-small btn-success' value='Unblock' onclick='unblock(" . $row['id'] . ");' /></div></td>";
                                        }

                                        echo "<td>" . $row['user_status_changed_by'] . "</td>";
                                        echo "<td>" . $row['user_status_changed_date'] . "</td>";
                                        echo "</tr>";

                                    }
                                    echo "</tbody></table>";
                                    mysql_close($con);
                                    ?>


                                    </div>
                                    <!-- /widget-content --> 

                                    </div>
                                    </div>
                                </div>
                                <!-- /widget -->

                            </div>
                            <!-- /span12 -->

                        </div>
                        <!-- /row --> 
                    </div>
                    <!-- /container --> 
                </div>
                <!-- /main-inner --> 
            </div>
            <!-- /main -->
            <div class="footer">
                <div class="footer-inner">
                    <div class="container">
                        <div class="row">
                            <div class="span12"><span style="text-align:center">  &copy; 2014 All Rights Reserved. </span></div>
                            <!-- /span12 --> 
                        </div>
                        <!-- /row --> 
                    </div>
                    <!-- /container --> 
                </div>
                <!-- /footer-inner --> 
            </div>
            <!-- /footer --> 
            <!-- Le javascript
            ================================================== --> 
            <!-- Placed at the end of the document so the pages load faster --> 
            <script src="js/jquery-1.7.2.min.js"></script> 
            <script src="js/excanvas.min.js"></script> 
            <script src="js/bootstrap.js"></script>
            <script src="js/base.js"></script> 
            <script src="http://crypto-js.googlecode.com/svn/tags/3.1.2/build/rollups/md5.js"></script>


            <script>

                function block(id) {

                    $.post("block_admin.php", {id: id, b: "b"})
                            .done(function(data) {
                                //$.get("block_user.php?id=" + id + "&b=b");
                                alert("User  Blocked!");
                                window.location = "web_admin_users.php";
                            });

                }
                
                function unblock(id) {
                    $.post("block_admin.php", {id: id, b: "u"})
                            .done(function(data) {
                                //$.get("block_user.php?id=" + id + "&b=u");

                                alert("User  Unblocked!");
                                window.location = "web_admin_users.php";
                            });

                }

            </script><!-- /Calendar -->
            <script>
                $(document).ready(function() {
                    $('#InfroTextSubmit').click(function() {

                        if ( ($('#title').val() === "") || ($('#title').val().length < 5)) { //admin full name

                            $('#title').next('.help-inline').show();
                            return false;

                        } else if ( ($('#title1').val() === "") || ($('#title1').val().length < 6)  ) { //username
                            $('#title').next('.help-inline').hide();
                            $('#title1').next('.help-inline').show();
                            return false;

                        } else if ( ($('#title2').val() === "")  || ($('#title2').val().length < 6) ) {//password
                            $('#title').next('.help-inline').hide();
                            $('#title1').next('.help-inline').hide();
                            $('#title2').next('.help-inline').show();
                            return false;
                        }
                        else if ($('#title3').val() === "" || ($('#title3').val() != $('#title2').val())) {//conform password
                            $('#title').next('.help-inline').hide();
                            $('#title1').next('.help-inline').hide();
                            $('#title2').next('.help-inline').hide();
                            $('#title3').next('.help-inline').show();
                            return false;
                        }

                        else {
                            // submit the form here
                            $('#InfroText').submit();

                            return true;
                        }

                    });
                    

                    $('#submitadminbutton').click(function() {
                        
                        var admin_name = document.getElementById('nfname').value;
                        var admin_new_password = document.getElementById('npassword').value;
                        var admin_cnew_password = document.getElementById('ncpassword').value;
                        var admin_current_password = document.getElementById('cpassword').value;
                        
                        var sqlupdate=-1;
                    
                        if( (admin_current_password == null) || (admin_current_password.length == 0))
                        {
                            alert('Current password is required!');
                        }
                        else
                        {
                            //alert(admin_name+" "+admin_new_password+" "+admin_cnew_password+" "+admin_current_password);

                            if( ((admin_new_password != null) && (admin_new_password.length > 0)) || ((admin_cnew_password != null) && (admin_cnew_password.length > 0)) )// if new password entered
                            {

                                if(admin_new_password.length < 6)
                                {
                                    alert("New password is too short! Password should be at least 6 characters.");
                                }
                                else if(admin_new_password!=admin_cnew_password)
                                {
                                    alert("Your new password and confirm password do not match!");
                                }
                                else
                                {
                                    sqlupdate = <?php echo $currentadminid ?>;
                                }

                            }
                            else // only name
                            {
                                sqlupdate = <?php echo $currentadminid ?>;
                            }


                            if( sqlupdate > -1 )
                            {
                                  if(CryptoJS.MD5(admin_current_password)=='<?php echo $currentadminpass; ?>')
                                  {

                                      document.getElementById('adminquery').value = sqlupdate;
                                      $('#adminupdate').submit();
                                  }
                                  else
                                  {
                                      alert("Incorrect password.");
                                  }

                            }

                        }
                    });

                });
        </script>
    </body>
</html>
