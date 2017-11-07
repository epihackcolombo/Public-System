<?php
session_start();
date_default_timezone_set('UTC');
include 'Security.php';
$CI = new CI_Security();
?>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <title>Mo-Buzz Dengue Management Console</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
        <meta name="apple-mobile-web-app-capable" content="yes">
        <link href="css/bootstrap.min.css" rel="stylesheet">

        <link href="css/bootstrap-responsive.min.css" rel="stylesheet">
        <link href="http://fonts.googleapis.com/css?family=Open+Sans:400italic,600italic,400,600"
              rel="stylesheet">
        <link href="css/font-awesome.css" rel="stylesheet">
        <link href="css/style.css" rel="stylesheet">
        <link href="css/pages/dashboard.css" rel="stylesheet">
        <link href="css/pages/reports.css" rel="stylesheet">
        <!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
        <!--[if lt IE 9]>
              <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
            <![endif]-->

        <link href="http://netdna.bootstrapcdn.com/font-awesome/4.0.3/css/font-awesome.min.css" rel="stylesheet">
        <link rel="stylesheet" href="datepicker/daterangepicker-bs3.css" />

    </head>
    <body>
        <?php
        require("constantes.php");

        if (!isset($_SESSION['admin'])) {
            header('Location: web_adminlogin.php');
        }

        //set date for the page
        date_default_timezone_set('Asia/Kolkata');
        if (isset($_POST['ss_end_date'])) {
            $ss_end_date = date("Y-m-d", strtotime($_POST['ss_end_date']));
        } else {
            $ss_end_date = date("Y-m-d", strtotime("today"));
        }

        $con = mysql_connect(constant("HOST"), constant("USER_NAME"), constant("PASSWORD"));

        if (!$con) {
            die('Could not connect: ' . mysql_error());
        }

        mysql_select_db(constant("DATABASE"), $con);

        
        
        $query = "SELECT
                  Count(public_users.id) as total
                  FROM `public_users`
                  WHERE
                  public_users.user_status = 1
                  AND
                  registered_date_time <= '".$ss_end_date." 23:59:59'";

        $total_active_users = 0;
        $total_active_users_array = mysql_query($query, $con);
        while ($row = mysql_fetch_array($total_active_users_array, MYSQL_ASSOC)) {
            $total_active_users = $row['total'];
        }

        
        $query = "SELECT id
                  FROM `public_data`
                  WHERE
                  public_data.verified = 1
                  AND
                  date_time <= '".$ss_end_date." 23:59:59'";
        $verify_array = mysql_query($query, $con);
        $verify = mysql_num_rows($verify_array);

        
        $query = "SELECT id
                  FROM `public_data`
                  WHERE
                  public_data.verified = 101
                  AND
                  date_time <= '".$ss_end_date." 23:59:59'";
        $verifyphi_array = mysql_query($query, $con);
        $verifyphi = mysql_num_rows($verifyphi_array);


        $query = "SELECT id
                  FROM `public_data`
                  WHERE
                  public_data.verified = 0
                  AND
                  date_time <= '".$ss_end_date." 23:59:59'";
        $pending_array = mysql_query($query, $con);
        $pending = mysql_num_rows($pending_array);

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
                    <ul class="mainnav">
                        <li class="active"><a href="web_tcg_cpanel.php"><i class="icon-dashboard"></i><span>Dashboard</span> </a> </li>
                        <li><a href="web_requests.php"><i class="icon-share"></i><span>Complaints</span> </a> </li>
                        <li><a href="web_phi.php"><i class="icon-user-md"></i><span>PHI</span> </a></li>
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

                    <div id="myModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                            <h3 id="myModalLabel">Add new admin</h3>
                        </div>

                        <div class="modal-body">

                            <form id="InfroText" method="POST">

                                <input type="hidden" name="InfroText" value="1">

                                <table>
                                    <tbody><tr><td>Full name</td><td><input type="text" name="firstname" id="title" style="width:300px"><span class="hide help-inline">This is required</span></td></tr>
                                        <tr><td>Username</td><td><input type="text" name="admin_username"  id="title1" style="width:300px"><span class="hide help-inline">This is required</span></td></tr>
                                        <tr><td>Password</td><td><input type="password" name="admin_password" id="title2" style="width:300px"><span class="hide help-inline">This is required</span></td></tr>
                                        <tr><td>Confirm Password</td><td><input type="password" name="admin_password2" id="title3" style="width:300px"><span class="hide help-inline">This is required or not match</span></td></tr>

                                    </tbody></table>
                            </form>
                        </div>
                        <div class="modal-footer">
                            <button class="btn" data-dismiss="modal" aria-hidden="true">Close</button>
                            <button class="btn btn-primary" data-dismiss="modal" id="InfroTextSubmit">Save changes</button>
                        </div>
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
            
            <!-- Date functions --> 
            <!-- __________________________________________________________________________ -->                                
            
            <div class="row">

                <div class="span8">
                    <!-- /widget -->
                    <div class="widget">
                        <div class="widget-content">

                            <h1>Pick a Date</h1><br>

                            <form action="web_tcg_cpanel.php" method="post" id="request_form">

                                <div class="input-prepend input-group">
                                    <span class="add-on input-group-addon" ><i class="glyphicon glyphicon-calendar fa fa-calendar"></i></span>

                                    <input name="ss_start_date"  id="ss_start_date" type="hidden" value="" />
                                    <input name="ss_end_date"  id="ss_end_date" type="hidden" value="" />
                                    
                                    <input type="text" style="width:50%" name="reservation" id="reservation" class="form-control" value="<?php echo date("F j, Y", strtotime($ss_end_date));?>" />
                                </div>

                            </form>

                        </div> <!-- /widget-content -->
                    </div> <!-- /widget -->
              </div>
              <!-- /span8-->

              <div class="span4">
                <!-- /widget -->
                <div class="widget">
                    <div class="widget-content"> 
                        <div class="stats-box-all-info" style="margin-top: 0px; margin-bottom: 0px;">
                                <?php echo "<h2><i class='icon-time' style='color:#3C3 ;'></i>    Today is  " . date("Y-m-d")."</h2>";?>
                        </div>
                    </div>
                </div>

                </div> <!-- /span8-->

            </div>

            <!-- __________________________________________________________________________ -->      

            
            <div class="row">
                <div class="span12">
                    <div class="widget widget-nopad">
                        <div class="widget-header"> <i class="icon-list-alt"></i>
                            <h3> Today's Statistics</h3>
                        </div>
                        <!-- /widget-header -->
                        <div class="widget-content">
                            <div class="widget big-stats-container">
                                <div class="widget-content">
                                    <h6 class="bigstats">CURRENT STATUS OF DENGUE BREEDING SITE COMPLAINTS</h6>
                                    <div id="big_stats" class="cf">
                                        <div class="stat" title="Total users "><h3>Total Active <br>Public Users</h3><div class="stats-box-all-info"> <i class="icon-user" style="color:#936" ></i> <?php echo $total_active_users; ?></div> </div>
                                        <!-- .stat -->

                                        <div class="stat" title="Total complaints"><h3>Total Accepted Complaints<br>(Public | PHI)</h3><div class="stats-box-all-info"><i class="icon-thumbs-up" style="color:#0033FF;"></i> <?php echo $verify; ?> | <?php echo $verifyphi; ?></div> </div>
                                        <!-- .stat -->

                                        <div class="stat" title="Total pending complaints(for validation)"><h3>Total Pending Public Complaints <br> (for validation)</h3><div class="stats-box-all-info"><i class="icon-thumbs-down" style="color:#F00;"></i> <?php
                                                echo $pending;
                                                if ($pending == '0' && $verify == '0') {
                                                    $pending = 1;
                                                }
                                                ?></div></div>
                                        <!-- .stat -->

                                        <div class="stat" title="Total valid complaints"> <h3>Total Attempted <br>Public Complaints</h3><div class="stats-box-all-info"><i class="icon-align-justify" style="color:#FF00CC;"></i> <?php echo round(($verify / ($pending + $verify)) * 100); ?> %</div> </div>
                                        <!-- .stat --> 
                                    </div>
                                </div>
                                <!-- /widget-content --> 

                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="span6">
                    <!-- /widget -->
                    <div class="widget widget-nopad">
                        <div class="widget-header"> <i class="icon-list-alt"></i>
                            <h3>Annual Statistics</h3>
                        </div>
                        <!-- /widget-header -->
                        <div class="widget-content">
                            <div id="chart_div" style="width:100%; height:475px;"></div>
                        </div>
                        <!-- /widget-content --> 
                    </div>
                    <!-- /widget -->
                </div>
                <!-- /span5 -->
                <div class="span6">

                    <div class="widget">
                        <div class="widget-header"> <i class="icon-signal"></i>
                            <h3>Last Month Statistics</h3>
                        </div>
                        <!-- /widget-header -->
                        <div class="widget-content">
                         <!--  <canvas id="pie-chart" class="chart-holder" height="250" width="538"> </canvas>--> 
                            <div id="piechart" style="width: 100%; height: 440px;"></div>

                            <!-- /pie-chart --> 
                        </div>
                        <!-- /widget-content --> 
                    </div>
                    <!-- /widget -->

                </div>
                <!-- /span6 --> 

            </div>
            <!-- /row --> 
            <div class="row">
                <div class="span12">
                    <div class="widget">

                        <div class="widget-header">
                            <i class="icon-list-alt"></i>
                            <h3>Ward-wise Public Complaints</h3>
                        </div> <!-- /widget-header -->

                        <div class="widget-content">
                            <div id="chart_phirepwardall" style="width: 100%; height: 800px;"></div>

                        </div> <!-- /widget-content -->

                    </div> <!-- /widget --> 
                </div><!-- /span6 --> 
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
<style>
    #request_form .input-prepend .add-on, .input-prepend .btn { margin-top: -9px;}
    .stats-box-all-info i {
        font-size:40px;
    }   
</style>

<script src="js/jquery-1.7.2.min.js"></script> 
<script src="js/excanvas.min.js"></script> 
<script src="js/bootstrap.js"></script>

<script src="js/base.js"></script> 
<script type="text/javascript" src="https://www.google.com/jsapi"></script>

<script type="text/javascript" src="datepicker/moment.js"></script>
<script type="text/javascript" src="datepicker/daterangepicker.js"></script>


<script type="text/javascript">

    $(document).ready(function() {
        
        $('#reservation').daterangepicker({ singleDatePicker: true, showDropdowns: true, format: 'MMMM D ,YYYY'}, function(start, end, label) {
            
            document.getElementById("ss_end_date").value = end.format('YYYY-MM-DD');
            document.getElementById('request_form').submit();
        });
    });

    

    google.load("visualization", "1", {packages: ["corechart"]});
    google.setOnLoadCallback(drawChart);
    

    function drawChart() 
    {
        <?php
            //set up the date for the chart, if its not already set
            if (isset($_POST['ss_end_date'])) {
                $ss_end_date = date("Y-m-d", strtotime($_POST['ss_end_date']));
            } else {
                $ss_end_date = date("Y-m-d", strtotime("today"));
            }
        ?>
        
        drawChart_month_currentYear();
        drawChart_ward_previousMonth();
        darwChart_ward_currentMonth();
    }

    //--------------------------------------------------------------------------------
    //Draw month-wise data for the current month (bar-graph)
    function drawChart_month_currentYear()
    {
        <?php

        function report($month, $type) {
            
            //set up the date for the chart, if its not already set
            if (isset($_POST['ss_end_date'])) {
                $ss_end_date = date("Y-m-d", strtotime($_POST['ss_end_date']));
            } else {
                $ss_end_date = date("Y-m-d", strtotime("today"));
            }

            $con = mysql_connect(constant("HOST"), constant("USER_NAME"), constant("PASSWORD"));

            if (!$con) {
                die('Could not connect: ' . mysql_error());
            }

            mysql_select_db(constant("DATABASE"), $con);

            $query = "SELECT
            Count(public_data.id) as total,
            MONTH(public_data.date_time) as month,
            public_data.verified  as validity
            FROM `public_data`
            WHERE
            YEAR(public_data.date_time) = " . date("Y",strtotime($ss_end_date)) . " and 
            MONTH(public_data.date_time)=" . $month . " and  public_data.verified =" . $type . "
            ";
            //echo $query
            $result = mysql_query($query, $con);

            $row = mysql_fetch_assoc($result);
            return $row['total'];
        }
        ?>
        
        
        var data_cy = google.visualization.arrayToDataTable([
            ['Year', 'Pending requests', 'Attempted requests'],
            ['January', <?php echo report(1, 0) ?>, <?php echo report(1, 1) ?>],
            ['February', <?php echo report(2, 0) ?>, <?php echo report(2, 1) ?>],
            ['March', <?php echo report(3, 0) ?>, <?php echo report(3, 1) ?>],
            ['April', <?php echo report(4, 0) ?>, <?php echo report(4, 1) ?>],
            ['May', <?php echo report(5, 0) ?>, <?php echo report(5, 1) ?>],
            ['June', <?php echo report(6, 0) ?>, <?php echo report(6, 1) ?>],
            ['July', <?php echo report(7, 0) ?>, <?php echo report(7, 1) ?>],
            ['August', <?php echo report(8, 0) ?>, <?php echo report(8, 1) ?>],
            ['September', <?php echo report(9, 0) ?>, <?php echo report(9, 1) ?>],
            ['October', <?php echo report(10, 0) ?>, <?php echo report(10, 1) ?>],
            ['November', <?php echo report(11, 0) ?>, <?php echo report(11, 1) ?>],
            ['December', <?php echo report(12, 0) ?>, <?php echo report(12, 1) ?>]
        ]);

        var options_dcww = {
            title: '<?php echo date("Y",strtotime($ss_end_date)); ?> Year  Public Reports',
            width: 530,
            height: 470,
            chartArea: {left: 50, width: 550, top: 70, height: 300},
            hAxis: {title: 'Months', titleTextStyle: {color: 'black'}, direction: 1, slantedText: true, slantedTextAngle: 90},
            legend: {position: "top"},
            colors: ['#B6291E', '#468847']
        };

        var chart2 = new google.visualization.ColumnChart(document.getElementById('chart_div'));
        chart2.draw(data_cy, options_dcww);
    }
    
    
    //--------------------------------------------------------------------------------
    //Draw ward-wise data for the current month (bar-graph)
    function drawChart_ward_previousMonth() {

        var data_cww = google.visualization.arrayToDataTable
        ([ ['phireports', 'Pending requests', 'Attempted requests', {role: 'annotation'}],
            <?php

            function getrequrst($ward_id, $type) {

                //set up the date for the chart, if its not already set
                if (isset($_POST['ss_end_date'])) {
                    $ss_end_date = date("Y-m-d", strtotime($_POST['ss_end_date']));
                } else {
                    $ss_end_date = date("Y-m-d", strtotime("today"));
                }

                $con = mysql_connect(constant("HOST"), constant("USER_NAME"), constant("PASSWORD")); //Connection to get pie-chart data for the month
                $query = "SELECT
                        Count(public_data.id) AS `total`
                        FROM `public_data`
                        WHERE
                        YEAR(public_data.date_time)= " . date("Y",strtotime($ss_end_date)) . " AND
                        MONTH(public_data.date_time)= " . date("m",strtotime($ss_end_date)) . " AND
                        public_data.verified = " . $type . " AND
                        public_data.ward = " . $ward_id . "
                        GROUP BY public_data.ward";

                $result = mysql_query($query, $con);

                if (mysql_num_rows($result) == '0') {
                    $total = 0;
                }

                $total = 0;
                while ($row = mysql_fetch_assoc($result)) {
                    $total = $row['total'];
                }
                return $total;
            }

            $con = mysql_connect(constant("HOST"), constant("USER_NAME"), constant("PASSWORD")); //Connection to get ward-list, this is to find ward names
            $query = "SELECT wd.district_id, dis.district_name, wd.ward_no, wd.ward_name 
                        FROM mobuzz_db.cmc_ward wd
                        INNER JOIN mobuzz_db.cmc_district dis
                        ON wd.district_id = dis.district_id
                        ORDER BY wd.district_id , wd.ward_no ";
            $wardlist = mysql_query($query, $con);

            while ($row = mysql_fetch_array($wardlist, MYSQL_ASSOC)) {

                $ward_id = $row['ward_no'];

                if ($ward_id == 0) {
                    echo "  ['" . $row['ward_name'] . "', " . getrequrst($ward_id, 0) . ", " . getrequrst($ward_id, 1) . ", ''] ,";
                } else {
                    echo "  ['" . $row['ward_name'] . " (" . $row['ward_no'] .") [". $row['district_name'] . "]', " . getrequrst($ward_id, 0) . ", " . getrequrst($ward_id, 1) . ", ''] ,";
                }
            }
            ?>
        ]);

       var options_cww = {
            title: '<?php echo date("F",strtotime($ss_end_date)) ?>: Ward-wise Public Complaints',
            isStacked: true,
            width: 1100,
            height: 810,
            bar: {groupWidth: "90%"},
            colors: ['#B6291E', '#468847'],
            chartArea: {left: 50, width: 1100, top: 70, height: 500},
            hAxis: {title: 'CMC Wards', titleTextStyle: {color: 'black', fontSize:16}, direction: 1, slantedText: true, slantedTextAngle: 90, fontSize:10},
            legend: {position: 'top', maxLines: 3}
        };

        var chart_draw_phirepwardall = new google.visualization.ColumnChart(document.getElementById('chart_phirepwardall'));
        chart_draw_phirepwardall.draw(data_cww, options_cww);

    }
    
    
    //--------------------------------------------------------------------------------
    //Draw ward-wise char for the previous month (pie-chart)
    function darwChart_ward_currentMonth()
    {
            
        var data = google.visualization.arrayToDataTable([
            <?php
            
            //set up the date for the chart, if its not already set
            if (isset($_POST['ss_end_date'])) {
                $ss_end_date = date("Y-m-d", strtotime($_POST['ss_end_date']));
            } else {
                $ss_end_date = date("Y-m-d", strtotime("today"));
            }

            $firstday = date("Y-m-d",strtotime("-1 month",strtotime($ss_end_date)));

            $query = "SELECT
            COUNT(public_data.id) AS total,
            cmc_district.district_name,
            public_data.ward,
            cmc_ward.ward_name
            FROM public_data
            INNER JOIN cmc_ward
            ON public_data.ward = cmc_ward.ward_no
            INNER JOIN cmc_district
            ON
            cmc_ward.district_id = cmc_district.district_id
            WHERE 
            public_data.verified > -2 AND
            public_data.date_time BETWEEN '".$firstday." 23:59:59' AND '".$ss_end_date." 23:59:59'
            GROUP BY
            public_data.ward
            ORDER BY
            cmc_ward.district_id,
            cmc_ward.ward_no";

            //echo $query;

            $result = mysql_query($query, $con);

            $temp = " [' ', ' '],";

            if (mysql_num_rows($result) == 0) {

                $temp = " [' ', ' ']";
            }
            $x = 0;
            while ($row = mysql_fetch_assoc($result)) {

                if ($x == (mysql_num_rows($result) - 1)) {
                    if ($row["ward"] == '0') {
                        $temp = $temp . " ['". $row["ward_name"] . "', " . $row["total"] . "]";
                    } else {
                        $temp = $temp . " ['[".$row["district_name"]."] ". $row["ward_name"] . "', " . $row["total"] . "]";
                    }

                } else {
                    if ($row["ward"] == '0') {
                        $temp = $temp . " ['". $row["ward_name"] . "', " . $row["total"] . "],";
                    } else {
                        $temp = $temp . " ['[".$row["district_name"]."] ". $row["ward_name"] . "', " . $row["total"] . "],";
                    }
                }
                $x++;
            }

            echo $temp;
            ?>
        ]);

        var options = {
            title: 'Ward-wise Complaints in Month (from Current Day)',
            width: 550,
            height: 450,
            chartArea: {left: 20, width: 590, top: 20, height: 450},
            legend: {position: 'right'}
        };

        var chart = new google.visualization.PieChart(document.getElementById('piechart'));
        chart.draw(data, options);
    }
</script>

<script>
    $(document).ready(function() {
        $('#InfroTextSubmit').click(function() {


            if ($('#title').val() === "") {
                // invalid
                $('#title').next('.help-inline').show();
                return false;

            } else if ($('#title1').val() === "") {
                $('#title').next('.help-inline').hide();
                $('#title1').next('.help-inline').show();
                return false;

            } else if ($('#title2').val() === "") {
                $('#title').next('.help-inline').hide();
                $('#title1').next('.help-inline').hide();
                $('#title2').next('.help-inline').show();
                return false;
            }
            else if ($('#title3').val() === "" || ($('#title3').val() != $('#title2').val())) {
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
    });
</script>

</body>
</html>
