<?php
session_start();
date_default_timezone_set('UTC');
if (!isset($_SESSION['admin'])) {
    header('Location: web_adminlogin.php');
}
?>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <title>Dashboard - Dengue Breeding Site Reporting Application</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
        <meta name="apple-mobile-web-app-capable" content="yes">
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="css/bootstrap-responsive.min.css" rel="stylesheet">
        <link href="http://fonts.googleapis.com/css?family=Open+Sans:400italic,600italic,400,600"
              rel="stylesheet">
        <link href="css/font-awesome.css" rel="stylesheet">
        <link href="css/style.css" rel="stylesheet">
        <link href="css/pages/dashboard.css" rel="stylesheet">
        <link rel="stylesheet" href="http://blueimp.github.io/Gallery/css/blueimp-gallery.min.css">
        <link rel="stylesheet" href="css/bootstrap-image-gallery.min.css">
        <link href="http://netdna.bootstrapcdn.com/font-awesome/4.0.3/css/font-awesome.min.css" rel="stylesheet">
        <link rel="stylesheet" href="datepicker/daterangepicker-bs3.css" />
        <!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
        <!--[if lt IE 9]>
              <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
            <![endif]-->
        <style>
            .modal-open{overflow:hidden}
            body.modal-open,.modal-open .navbar-fixed-top,.modal-open .navbar-fixed-bottom{margin-right:15px}
            .modal{position:fixed;top:0;right:0;bottom:0;left:0;z-index:1040;display:none;overflow:auto;overflow-y:scroll}
            .modal.fade .modal-dialog{-webkit-transform:translate(0,-25%);-ms-transform:translate(0,-25%);transform:translate(0,-25%);-webkit-transition:-webkit-transform .3s ease-out;-moz-transition:-moz-transform .3s ease-out;-o-transition:-o-transform .3s ease-out;transition:transform .3s ease-out}
            .modal.in .modal-dialog{-webkit-transform:translate(0,0);-ms-transform:translate(0,0);transform:translate(0,0)}
            .modal-dialog{z-index:1050;width:auto;padding:10px;margin-right:auto;margin-left:auto}
            .modal-content{position:relative;background-color:#fff;border:1px solid #999;border:1px solid rgba(0,0,0,0.2);border-radius:6px;outline:0;-webkit-box-shadow:0 3px 9px rgba(0,0,0,0.5);box-shadow:0 3px 9px rgba(0,0,0,0.5);background-clip:padding-box}
            .modal-backdrop{position:fixed;top:0;right:0;bottom:0;left:0;z-index:1030;background-color:#000}
            .modal-backdrop.fade{opacity:0;filter:alpha(opacity=0)}
            .modal-backdrop.in{opacity:.5;filter:alpha(opacity=50)}
            .blueimp-gallery .modal-dialog {
                max-width: 840px !important;}
            .modal-header{min-height:16.428571429px;padding:15px;border-bottom:1px solid #e5e5e5}
            .modal-header .close{margin-top:-2px}
            .modal-title{margin:0;line-height:1.428571429}
            .modal-body{position:relative;padding:20px}
            .modal-footer{padding:19px 20px 20px;margin-top:15px;text-align:right;border-top:1px solid #e5e5e5}
            .modal-footer:before,.modal-footer:after{display:table;content:" "}
            .modal-footer:after{clear:both}
            .modal-footer:before,.modal-footer:after{display:table;content:" "}
            .modal-footer:after{clear:both}
            .modal-footer .btn+.btn{margin-bottom:0;margin-left:5px}
            .modal-footer .btn-group .btn+.btn{margin-left:-1px}
            .modal-footer .btn-block+.btn-block{margin-left:0}
            @media screen and (min-width:768px){.modal-dialog{right:auto;left:50%;width:600px;padding-top:30px;padding-bottom:30px}
                                                .modal-content{-webkit-box-shadow:0 5px 15px rgba(0,0,0,0.5);box-shadow:0 5px 15px rgba(0,0,0,0.5)}
            }
        </style>


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
                        <li ><a href="web_requests.php"><i class="icon-share"></i><span>Complaints</span> </a> </li>
                        <li><a href="web_phi.php"><i class="icon-user-md"></i><span>PHI</span> </a></li>
                        <li><a href="web_map.php"><i class="icon-map-marker"></i><span>Map</span> </a> </li>
                        <li class="dropdown"><a href="javascript:;" class="dropdown-toggle" data-toggle="dropdown"> <i class="icon-long-arrow-down"></i><span>Clusters</span> <b class="caret"></b></a>
                            <ul class="dropdown-menu">
                                <li><a href="web_map_public_show.php"><i class="icon-map-marker"></i><span>&nbsp;Clusters spot map</span> </a></li>
                                <li><a href="web_map_public_show_data.php"><i class="icon-map-marker"></i><span>&nbsp;Cluster data</span> </a>   </li> 
                            </ul></li>
                        <li><a href="web_public_users.php"><i class="icon-group"></i><span>Public Users</span> </a> </li>
                        <li><a href="web_requests_byuser.php"><i class="icon-search"></i><span>Search</span> </a> </li>
                        <li class="active"><a href="web_gallery.php"><i class="icon-th-large"></i><span>Gallery</span> </a> </li>
                        <li><a href="web_download.php"><i class="icon-download-alt"></i><span>Downloads</span> </a> </li>
                        <li><a href="web_moh_details.php"><i class="icon-home"></i><span>MOH</span> </a> </li>
                        <li><a href="web_admin_users.php"><i class="icon-briefcase"></i><span>Administrator</span> </a> </li> 
                        <li> </li>
                    </ul>
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

                        <div class="span8">


                            <!-- /widget -->
                            <div class="widget">

                                <div class="widget-content">
                                    <h1>Pick a Time Range</h1><br>
                                    <form action="web_gallery.php" method="post" id="request_form">
                                        <div class="input-prepend input-group">
                                            <span class="add-on input-group-addon"><i class="glyphicon glyphicon-calendar fa fa-calendar"></i></span>

                                            <?php
                                            if (isset($_POST['ss_start_date'])) {
                                                $ss_start_date = date("m/d/Y", strtotime($_POST['ss_start_date']));
                                            } else {
                                                $ss_start_date = date("m/d/Y", strtotime("-1 month"));
                                            }
                                            if (isset($_POST['ss_end_date'])) {
                                                $ss_end_date = date("m/d/Y", strtotime($_POST['ss_end_date']));
                                            } else {
                                                $ss_end_date = date("m/d/Y", strtotime("today"));
                                            }

                                            $temp_date = date("F j, Y", strtotime($ss_start_date)) . ' - ' . date("F j, Y", strtotime($ss_end_date));
                                            ?>     

                                            <input type="text" style="width:50%" name="reservation" id="reservation" class="form-control" value="<?php echo $temp_date; ?>" />
                                            <select name="select" id="select" onChange="selected_submit()">
                                            <?php
                                            $query_select = (isset($_REQUEST['select']) ? $_REQUEST['select'] : null);

                                            if ($query_select == '') {
                                                echo ' <option value="">All</option>
         <option value="0">Not Verified</option>
         <option value="1">Accepted </option>
         <option value="101">Accepted (PHI)</option>
         <option value="-1">Rejected (Misuse)</option>
         ';
                                            }
                                            if ($query_select == '1') {
                                                echo '  <option value="1">Accepted </option>
         <option value="101">Accepted (PHI)</option>
         <option value="-1">Rejected (Misuse)</option>
         <option value="">All</option>
         <option value="0">Not Verified</option>
         ';
                                            }
                                            if ($query_select == '-1') {
                                                echo '  <option value="-1">Rejected (Misuse)</option>
         <option value="1">Accepted </option>
         <option value="101">Accepted (PHI)</option>
         <option value="0">Not Verified</option>
         <option value="">All</option>
         ';
                                            }
                                            if ($query_select == '0') {
                                                echo ' <option value="0">Not Verified</option>
         <option value="-1">Rejected (Misuse)</option>
         <option value="1">Accepted</option>
         <option value="101">Accepted (PHI)</option>
         <option value="">All</option>
         ';
                                            }
                                            if ($query_select == '101') {
                                                echo ' <option value="101">Accepted (PHI)</option>
         <option value="">All</option>
         <option value="0">Not Verified</option>
         <option value="-1">Rejected (Misuse)</option>
         <option value="1">Accepted</option>
         ';
                                            }
                                            ?>

                                            </select>
                                        </div>
                                        <input name="ss_start_date"  id="ss_start_date" type="hidden" value="<?php echo date("Y-m-d", strtotime($ss_start_date)); ?>" />
                                        <input name="ss_end_date"  id="ss_end_date" type="hidden" value="<?php echo date("Y-m-d", strtotime($ss_end_date)); ?>" />

                                    </form>
                                </div> <!-- /widget-content -->

                            </div> <!-- /widget -->
                        </div>
                        <!-- /span8-->

                        <div class="span4">


                            <!-- /widget -->
                            <div class="widget">

                                <div class="widget-content"> 

                                    <div class="stats-box-all-info">

<?php echo "<h2><i class='icon-time' style='color:#3C3 ;'></i>    Today is  " . date("Y-m-d") . "</h2>"; ?></div>
                                </div>
                            </div>
                        </div> <!-- /span8-->
                    </div>
                    <!-- /row --> 

                    <div class="row">

                        <div class="span12">


                            <!-- /widget -->
                            <div class="widget">

                                <div class="widget-content">
                                    <h1> Requests </h1><br>
                                    <div id="links">

                                            <?php
                                            if (isset($_POST['ss_start_date']) && isset($_POST['ss_end_date'])) {

                                                $start_date = new DateTime(date("Y-m-d", strtotime($_POST['ss_start_date'])));
                                                $start_date3 = date("Y-m-d", strtotime($_POST['ss_start_date']));

                                                $end_date = new DateTime(date("Y-m-d", strtotime($_POST['ss_end_date'])));
                                                $end_date3 = date("Y-m-d", strtotime($_POST['ss_end_date']));

                                            } else {

                                                $start_date = new DateTime(date("Y-m-d", strtotime("-1 month")));
                                                $end_date = new DateTime(date("Y-m-d", strtotime("today")));
                                                $start_date3 = date("Y-m-d", strtotime("-1 month"));
                                                $end_date3 = date("Y-m-d", strtotime("+24 hours"));
                                                
                                            }

                                            $con = mysql_connect(constant("HOST"), constant("USER_NAME"), constant("PASSWORD"));

                                            if (!$con) {
                                                die('Could not connect: ' . mysql_error());
                                            }

                                            mysql_select_db(constant("DATABASE"), $con);

                                            if (isset($_REQUEST['select'])) {
                                                if ($_REQUEST['select'] == '') {
                                                    $where = 'and  public_data.verified > -2';
                                                } else {
                                                    $where = "and  public_data.verified = " . $_REQUEST['select'] . "";
                                                }
                                            } else {
                                                $where = 'and  public_data.verified > -2';
                                            }

                                            $query = "SELECT
                                            public_data.id,
                                            public_data.username,
                                            public_data.verified,
                                            public_data.verified_by,
                                            public_data.verified_date_time,
                                            public_data.date_time,
                                            public_data.remarks,
                                            public_data.img_path
                                            FROM
                                            public_data
                                            WHERE
                                            public_data.date_time BETWEEN '" . $start_date3 . "' AND '" . $end_date3 . ' 23:59:59' . "'" . $where . " 
                                            ORDER BY
                                            public_data.date_time DESC";

                                            $comments = mysql_query($query, $con);

                                            while ($row = mysql_fetch_array($comments, MYSQL_ASSOC)) {

                                                $date = new DateTime(trim($row['date_time']));
                                                if ($row['verified'] != '-2') {

                                                    $picpath = $row['img_path'];

                                                    if(file_exists($picpath."_thumb.jpg"))
                                                    {
                                                        $picpath = $picpath."_thumb.jpg";
                                                    }
                                                    else if(file_exists($picpath))
                                                    {
                                                        //file exist
                                                    }
                                                    else
                                                    {
                                                        // image is not available, skip
                                                        continue;
                                                    }
                                                    
                                                    $actual_path = substr($row['img_path'], 1);
                                                    
                                                    if ($picpath != '') {
                                                        echo '<a href="http://'.HOST_IP.'/mobuzz' . $actual_path . '" title="Reported by ' . $row['username'] . ' on ' . $row['date_time'] . '" data-gallery><img src="' . $picpath . '" width="75" style="height:75px;"></a>';
                                                    }
                                                }
                                            }

                                            echo " </div>";
                                            mysql_close($con);
                                            ?>
                                        <div id="blueimp-gallery" class="blueimp-gallery">
                                            <!-- The container for the modal slides -->
                                            <div class="slides"></div>
                                            <!-- Controls for the borderless lightbox -->
                                            <h3 class="title"></h3>
                                            <a class="prev">‹</a>
                                            <a class="next">›</a>
                                            <a class="close">×</a>
                                            <a class="play-pause"></a>
                                            <ol class="indicator"></ol>
                                            <!-- The modal dialog, which will be used to wrap the lightbox content -->
                                            <div class="modal fade">
                                                <div class="modal-dialog">
                                                    <div class="modal-content">
                                                        <div class="modal-header">
                                                            <button type="button" class="close" aria-hidden="true">&times;</button>
                                                            <h4 class="modal-title"></h4>
                                                        </div>
                                                        <div class="modal-body next"></div>
                                                        <div class="modal-footer">
                                                            <button type="button" class="btn btn-default pull-left prev">
                                                                <i class="glyphicon glyphicon-chevron-left"></i>
                                                                Previous
                                                            </button>
                                                            <button type="button" class="btn btn-primary next">
                                                                Next
                                                                <i class="glyphicon glyphicon-chevron-right"></i>
                                                            </button>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>

                                    </div> <!-- /widget-content -->

                                </div> <!-- /widget -->
                            </div>
                            <!-- /span6 --> 
                        </div>
                        <!-- /row --> 


                    </div>
                    <!-- /container --> 
                </div>
                <!-- /main-inner --> 
            </div></div>
        <!-- /main -->
        <div class="footer">
            <div class="footer-inner">
                <div class="container">
                    <div class="row">
                        <div class="span12"><span style="text-align:center"> &copy; 2014 All Rights Reserved.</span> </div>
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

        <style>
            #request_form .input-prepend .add-on, .input-prepend .btn { margin-top: -9px;}
            .stats-box-all-info i {
                font-size:40px;
            }
        </style>
        <script src="js/jquery-1.7.2.min.js"></script> 
        <script src="js/excanvas.min.js"></script> 
        <script src="js/bootstrap.js"></script>
        <script src="http://blueimp.github.io/Gallery/js/jquery.blueimp-gallery.min.js"></script>
        <script src="js/bootstrap-image-gallery.min.js"></script> 
        <script type="text/javascript" src="datepicker/moment.js"></script>
        <script type="text/javascript" src="datepicker/daterangepicker.js"></script>


        <script src="js/base.js"></script> 
        <script>
                   $(document).ready(function() {

                       $('#reservation').daterangepicker({format: 'MMMM D ,YYYY'}, function(start, end, label) {


                           $('#ss_end_date').val(end.format('YYYY-MM-DD'));
                           $('#ss_start_date').val(start.format('YYYY-MM-DD'));
                           document.getElementById('request_form').submit();
                       });

                   });


                   function selected_submit() {
                       //	console.log(	$('#reservation').getDates());

                       $('#ss_end_date').val();
                       $('#ss_start_date').val();
                       // alert(start);
                       document.getElementById('request_form').submit();
                   }

        </script><!-- date picker -->
    </body>
</html>
