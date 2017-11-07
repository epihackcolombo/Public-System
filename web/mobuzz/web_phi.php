<?php
session_start();
include 'Security.php';
$FBI = new CI_Security()
?>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <title>PHI List - Mo-Buzz Dengue Management Console</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
        <meta name="apple-mobile-web-app-capable" content="yes">
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="css/bootstrap-responsive.min.css" rel="stylesheet">
        <link href="http://fonts.googleapis.com/css?family=Open+Sans:400italic,600italic,400,600" rel="stylesheet">
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

        <div class="navbar navbar-fixed-top">
            <div class="navbar-inner">
                <div class="container"> <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse"><span
                            class="icon-bar"></span><span class="icon-bar"></span><span class="icon-bar"></span> </a><a class="brand" href="tcg_cpanel.php">Mo-Buzz Dengue Management Console</a>
                    <div class="nav-collapse">
                        <ul class="nav pull-right">
                            <li class="dropdown"><a href="#" class="dropdown-toggle" data-toggle="dropdown"><span style="color:#333;font-size:14px;"><i
                                            class="icon-user"></i>&nbsp;<strong>Hi&nbsp;&nbsp;</strong><?php echo $_SESSION['username'] ?></span> <b class="caret"></b></a>
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
                    <ul class="mainnav">
                        <li><a href="web_tcg_cpanel.php"><i class="icon-dashboard"></i><span>Dashboard</span> </a> </li>
                        <li><a href="web_requests.php"><i class="icon-share"></i><span>Complaints</span> </a> </li> 
                        <li class="active"><a href="web_phi.php"><i class="icon-user-md"></i><span>PHI</span> </a></li>
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
                        <li><a href="web_admin_users.php"><i class="icon-briefcase"></i><span>Administrator</span> </a> </li> 
                        <li> </li>
                    </ul>
                    <!-- Modal -->

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
                    <div class="widget-table action-table">
                        <div class="widget-header"> <i class="icon-list-alt"></i>
                            <h3> PHI officer's detail</h3>
                        </div>
                        <!-- /widget-header -->
                        <div class="widget-content">

                            <?php
//$con = mysql_connect("mysql5.000webhost.com","a3035840_pubapp","manju104");
                            $con = mysql_connect(constant("HOST"), constant("USER_NAME"), constant("PASSWORD"));

                            if (!$con) {
                                die('Could not connect: ' . mysql_error());
                            }

//mysql_select_db("a3035840_pubapp",$con);
                            mysql_select_db(constant("DATABASE"), $con);

                            $query = "SELECT *
FROM phi_users
ORDER BY
phi_users.`username` ASC
";
//echo $query;

                            $comments = mysql_query($query, $con);
                            ?>    

                            <table class="table table-striped table-bordered">
                                <thead>
                                    <tr>
                                        <th>  </th>
                                        <th> Username</th>
                                        <th> Full name</th>
										<th> Registered on</th>
                                        <th> Email </th>
                                        <th> Phone number </th>
                                        <th> Ward </th>

                                        <th class="td-actions"> Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr>
                                        <?php
                                        $p = 0;
                                        while ($row = mysql_fetch_array($comments, MYSQL_ASSOC)) {
                                            $p = $p + 1;
                                            echo "<tr>";
                                            echo "<td>" . $p . "</td>";
                                            echo "<td>" . $FBI->xss_clean($row["username"]) . "</td>";
                                            echo "<td>" . $FBI->xss_clean($row["full_name"]) . "</td>";
											echo "<td>" . $FBI->xss_clean($row["time_stamp"]) . "</td>";
                                            echo "<td>" . $FBI->xss_clean($row["email"]) . "</td>";
                                            echo "<td>" . $FBI->xss_clean($row["contact_no"]) . "</td>";
                                            echo "<td style='max-width: 200px;
word-wrap: break-word;'>" . $FBI->xss_clean($row["ward"]) . "</td>";
                                            echo " <td class='td-actions'><a href='web_assign_wards.php?id=" . $row["id"] . "' class='btn btn-small btn-success'><i class='btn-icon-only icon-ok'> </i>Change</a></td>";
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
                                </script>

                                <script src="js/base.js"></script> 

                                </body>
                                </html>
