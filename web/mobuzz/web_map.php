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
        <title>DengueHotspots - Dengue Breeding Site Reporting Application</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
        <meta name="apple-mobile-web-app-capable" content="yes">
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="css/bootstrap-responsive.min.css" rel="stylesheet">
        <link href="http://fonts.googleapis.com/css?family=Open+Sans:400italic,600italic,400,600"
              rel="stylesheet">
        <link href="css/font-awesome.css" rel="stylesheet">
        <link href="css/style.css" rel="stylesheet">
        
        <link href="css/pages/dashboard.css" rel="stylesheet">
        <link href="http://netdna.bootstrapcdn.com/font-awesome/4.0.3/css/font-awesome.min.css" rel="stylesheet">
        <link rel="stylesheet" href="datepicker/daterangepicker-bs3.css" />
        
        <style>

            body{
                text-align:left;
            }
            #map{
                margin: 0px;
                width: 100%;
                height: 980px;
            }
            #maptag{
                margin: 0px;
                width: 200px;
            }
            label input{ display:inline !important; margin-right:8px !important;}

        </style>
    </head>

    <body>
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
                        <li ><a href="web_tcg_cpanel.php"><i class="icon-dashboard"></i><span>Dashboard</span> </a> </li>
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
 
                                    <!-- __________________________________________________________________________ -->                                

                                    <div class="row">

                                        <div class="span8">
                                            <!-- /widget -->
                                            <div class="widget">
                                                <div class="widget-content">

                                                    <h1>Pick a Time Range</h1><br>

                                                    <form action="web_map.php" method="post" id="request_form">

                                                        <div class="input-prepend input-group">
                                                            &nbsp;
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

                                                            <input name="ss_start_date"  id="ss_start_date" type="hidden" value="" />
                                                            <input name="ss_end_date"  id="ss_end_date" type="hidden" value="" />
                                                            
                                                            <input type="text" style="width:50%" name="reservation" id="reservation" class="form-control" value="<?php echo $temp_date;?>" />


                                                            <select id="mySelect" onchange="refreshMapData(this)">
                                                                <option value="-2">All</option>
                                                                <option value="0">Not Verified</option>
                                                                <option value="1">Accepted </option>
                                                                <option value="101">Accepted (PHI)</option>
                                                                <option value="-1">Rejected </option>
                                                            </select>

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
                                                <div class="stats-box-all-info">
                                                        <?php echo "<h2><i class='icon-time' style='color:#3C3 ;'></i>    Today is  " . date("Y-m-d")."</h2>";?>
                                                </div>
                                            </div>
                                        </div>

                                        <div style="margin-top: -1.5em; ">
                                            <div style="float:left; margin-right: 25px;">
                                                 <div id="mkr1Nv"></div>
                                                 <div id="mkr2Nv"></div>
                                            </div>
                                            <div style="float:left; margin-right: 35px;">
                                                <div id="mkr1A"></div>
                                                <div id="mkr2A"></div>
                                            </div>
                                            <div style="float:left; margin-right: 25px;">
                                                <div id="mkr1Ap"></div>
                                                <div id="mkr2Ap"></div>
                                            </div>
                                            <div style="float:left;">
                                                <div id="mkr1R"></div>
                                                <div id="mkr2R"></div>
                                            </div>

                                        </div>
				
                                        </div> <!-- /span8-->

                                    </div>

                                    <!-- __________________________________________________________________________ -->                                

                            <div class="widget widget-nopad">

                                <div class="widget-header"> <i class="icon-list-alt"></i>
                                    <h3> Breeding-Site Complaints</h3>
                                </div>
       
                                <!-- /widget-header -->
                                <div class="widget-content">
                                    <table style="width: 100%;" border="0">
                                        <tr>
                                            <td>
                                                <div id="map"></div>
                                            </td>
                                        </tr>
                                    </table>
                                </div>
                                <!-- /widget-content --> 
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

<style>
.input-prepend .add-on, .input-prepend .btn { margin-top: -9px;}
.stats-box-all-info i {
   font-size:40px;
}
</style>        

    <script src="js/jquery-1.7.2.min.js"></script> 
    <script src="js/excanvas.min.js"></script> 
    <script src="js/bootstrap.js"></script>
    <script src="http://maps.googleapis.com/maps/api/js?sensor=false" type="text/javascript"></script>
    <script type="text/javascript" src="js/gmap3.js"></script>

    <script type="text/javascript" src="datepicker/moment.js"></script>
    <script type="text/javascript" src="datepicker/daterangepicker.js"></script>

    <script type="text/javascript">

        var jHotspots = null;
        var sql_success = "SQLSuccess";
        var sql_error = "SQLError";
        var sql_dberror = "DBError";
        var sql_nodata = "SQL_no_data";
        
        var dbfromdate = '2014-01-01' ;
        var dbtodate = '2014-02-01';

        dbfromdate = '<?php echo date("Y-m-d", strtotime($ss_start_date)) ;?>';
        dbtodate = '<?php echo date("Y-m-d", strtotime($ss_end_date)) ;?>';

        $(function() {

            $('#map').gmap3({
                map: {
                    options: {
                        center: [6.911640, 79.87],
                        zoom: 13
                    }
                },
                kmllayer:{
                      options:{
                            url: "http://202.129.235.206/mobuzz/kml/cmcregions.kml",
                            opts:{
                              suppressInfoWindows: true
                            }
                      },
                      events:{
                      }
                    }
            });

            $('#reservation').daterangepicker({format: 'MMMM D ,YYYY'}, function(start, end, label) {

                document.getElementById("ss_start_date").value = start.format('YYYY-MM-DD');
                document.getElementById("ss_end_date").value = end.format('YYYY-MM-DD');
                
                document.getElementById('request_form').submit();
            });
            
                        
            clearall(function() {

                defHotspots(dbfromdate, dbtodate, "0", "./img/logo_icon_marker_public_new.png");
                defHotspots(dbfromdate, dbtodate, "1", "./img/logo_icon_marker_public_acc.png");
                defHotspots(dbfromdate, dbtodate, "101", "./img/logo_icon_marker_phi_acc.png");
                defHotspots(dbfromdate, dbtodate, "-1", "./img/logo_icon_marker_public_reg.png");
            });

        });
        
        
        function refreshMapData(selectvalue)
        {
            //Clear html table data
            document.getElementById("mkr1Nv").innerHTML = "";
            document.getElementById("mkr1A").innerHTML = "";
            document.getElementById("mkr1Ap").innerHTML = "";
            document.getElementById("mkr1R").innerHTML = "";
            
            document.getElementById("mkr2Nv").innerHTML = "";
            document.getElementById("mkr2A").innerHTML = "";
            document.getElementById("mkr2Ap").innerHTML = "";
            document.getElementById("mkr2R").innerHTML = "";
            
            
            if(selectvalue.value==-2)
            {
                clearall(function() {
                    defHotspots(dbfromdate, dbtodate, "0", "./img/logo_icon_marker_public_new.png");
                    defHotspots(dbfromdate, dbtodate, "1", "./img/logo_icon_marker_public_acc.png");
                    defHotspots(dbfromdate, dbtodate, "101", "./img/logo_icon_marker_phi_acc.png");
                    defHotspots(dbfromdate, dbtodate, "-1", "./img/logo_icon_marker_public_reg.png");
                });
            }
            else if(selectvalue.value==0)
            {
                clearall(function() {
                    defHotspots(dbfromdate, dbtodate, "0", "./img/logo_icon_marker_public_new.png");
                });
            }
            else if(selectvalue.value==1)
            {
                clearall(function() {
                    defHotspots(dbfromdate, dbtodate, "1", "./img/logo_icon_marker_public_acc.png");
                });
            }
            else if(selectvalue.value==101)
            {
                clearall(function() {
                    defHotspots(dbfromdate, dbtodate, "101", "./img/logo_icon_marker_phi_acc.png");
                });
            }
            else if(selectvalue.value==-1)
            {
                clearall(function() {
                    defHotspots(dbfromdate, dbtodate, "-1", "./img/logo_icon_marker_public_reg.png");
                });
            }
        }
        

        function initData()
        {
        }

        function clearall(callback)
        {
            $('#map').gmap3({clear: {name: 'circle', all: true}});
            $('#map').gmap3({clear: {name: 'marker', all: true}});
            $('#map').gmap3({clear: {name: 'infowindow', all: true}});

            callback();
        }

        //clear google map tags
        function cleartype(type, callback)
        {
            switch (type) {
                case 'MARKER':
                    $('#map').gmap3({
                        clear: {
                            name: 'marker',
                            all: true
                        }
                    });
                    break;
                case 'CIRCLE':
                    $('#map').gmap3({
                        clear: {
                            name: 'circle',
                            all: true
                        }
                    });
                    break;
                default:
                    break;
            }

            callback();
        }

        //clear google map tags
        function cleartag(type, strtag, callback)
        {
            $('#map').gmap3({action: 'clear', tag: strtag});
            callback();
        }

        function defHotspots(fromdate, todate, reporttype, markericon)
        {

            getbkenddata("map_public_report_locations.php", fromdate+" 00:00:00$"+todate+" 23:59:59$"+reporttype, function(jData) {

                var jMarkers;
                try {
                    jMarkers = eval('(' + jData + ')');
                } catch (e) {
                    if (e instanceof SyntaxError) {
                        //alert(e.message);
                    }
                }

                if(!$.isArray(jMarkers) || !jMarkers.length)
                {

                    cleartag('MARKER', 'hp_def', function() {
                        //Nothing to do
                        
                        if(reporttype=="0")
                        {
                            //alert("Not Verified: No data to show! ");
                            document.getElementById("mkr1Nv").innerHTML = "<b>Not Varified</b>";
                            document.getElementById("mkr2Nv").innerHTML = "<img src='./img/logo_icon_marker_public_new.png' style='height:40px;'> 0";
                        }
                        else if(reporttype=="1")
                        {
                            //alert("Accepted: No data to show! ");
                            document.getElementById("mkr1A").innerHTML = "<b>Accepted</b>";
                            document.getElementById("mkr2A").innerHTML = "<img src='./img/logo_icon_marker_public_acc.png' style='height:40px;'> 0";                           
                        }
                        else if(reporttype=="101")
                        {
                            //alert("Accepted (PHI): No data to show! ");
                            document.getElementById("mkr1Ap").innerHTML = "<b>Accepted (PHI)</b>";
                            document.getElementById("mkr2Ap").innerHTML = "<img src='./img/logo_icon_marker_phi_acc.png' style='height:40px;'> 0";                            
                        }
                        else if(reporttype=="-1")
                        {
                            //alert("Rejected: No data to show! ");
                            document.getElementById("mkr1R").innerHTML = "<b>Rejected</b>";
                            document.getElementById("mkr2R").innerHTML = "<img src='./img/logo_icon_marker_public_reg.png' style='height:40px;'> 0";                            
                        }

                    });

                }
                else {
                    
                    if(reporttype=="0")
                    {
                        //alert("Not Verified: No data to show! ");
                        document.getElementById("mkr1Nv").innerHTML = "<b>Not Varified</b>";
                        document.getElementById("mkr2Nv").innerHTML = "<img src='./img/logo_icon_marker_public_new.png' style='height:40px;'> "+jMarkers.length+"";
                    }
                    else if(reporttype=="1")
                    {
                        //alert("Accepted: No data to show! ");
                        document.getElementById("mkr1A").innerHTML = "<b>Accepted</b>";
                        document.getElementById("mkr2A").innerHTML = "<img src='./img/logo_icon_marker_public_acc.png' style='height:40px;'> "+jMarkers.length+"";                           
                    }
                    else if(reporttype=="101")
                    {
                        //alert("Accepted (PHI): No data to show! ");
                        document.getElementById("mkr1Ap").innerHTML = "<b>Accepted (PHI)</b>";
                        document.getElementById("mkr2Ap").innerHTML = "<img src='./img/logo_icon_marker_phi_acc.png' style='height:40px;'> "+jMarkers.length+"";                            
                    }
                    else if(reporttype=="-1")
                    {
                        //alert("Rejected: No data to show! ");
                        document.getElementById("mkr1R").innerHTML = "<b>Rejected</b>";
                        document.getElementById("mkr2R").innerHTML = "<img src='./img/logo_icon_marker_public_reg.png' style='height:40px;'> "+jMarkers.length+"";                            
                    }

                    cleartag('MARKER', 'hp_def', function() {

                        $.each(jMarkers, function(i, item) {

                            $("#map").gmap3({
                                marker: {
                                    latLng: [item.lat, item.lng],
                                    data: item.data,
                                    options:{icon: markericon,  draggable: true},
                                events:{
                                    click: function(marker, event, context) {

                                        var datacont = context.data.split("|");
                                        var messagebox = "";
                                        
                                        if(reporttype=="101")
                                        {
                                             messagebox = "<table><tr><th colspan='2'>Report Details</th></tr><tr><td>User:</td><td>"+datacont[0]+"</td></tr><tr><td>Date-Time:</td><td>"+datacont[1]+"</td></tr><tr><td>Ward:</td><td>"+datacont[2]+"</td></tr><tr><td>CMC-Message:  </td><td>"+datacont[3]+"</td></tr><tr><td class='td-actions'><a href='web_view.php?id="+datacont[6]+"' class='btn btn-small btn-success'>Edit</a></td><td></td></tr></table>";
                                        }
                                        else
                                        {
                                             messagebox = "<table><tr><th colspan='2'>Report Details</th></tr><tr><td>User:</td><td>"+datacont[0]+"</td></tr><tr><td>Date-Time:</td><td>"+datacont[1]+"</td></tr><tr><td>Ward:</td><td>"+datacont[2]+"</td></tr><tr><td>CMC-Message:  </td><td>"+datacont[3]+"</td></tr><tr><th colspan='2'>Verified Details</th></tr><tr><td>By:</td><td>"+datacont[4]+"</td></tr><tr><td>Date-Time:</td><td>"+datacont[5]+"</td></tr><tr><td class='td-actions'><a href='web_view.php?id="+datacont[6]+"' class='btn btn-small btn-success'>Edit</a></td><td></td></tr></table>";
                                        }
        
                                        var infowindow = $(this).gmap3({get: {name: "infowindow"}});

                                        if (infowindow)
                                        {
                                            infowindow.close();
                                            infowindow = $(this).gmap3({action: 'get', name: 'infowindow'});
                                        }
                                        else
                                        {
                                            infowindow = $(this).gmap3({action: 'get', name: 'infowindow'});
                                        }

                                        $("#map").gmap3({
                                            infowindow: {
                                                anchor: marker,
                                                options: {
                                                    content: messagebox
                                                },
                                                events: {
                                                    closeclick: function(infowindow) {
                                                    }
                                                }
                                            }
                                        });

                                    }
                                }
                                }
                            }, "autofit");

                        });

                    });

                }
            });

        }

        //Get data
        function getbkenddata(strUrl, dataString, callback)
        {
            $.ajax({
                type: "GET",
                url: strUrl,
                data: {'q': dataString},
                cache: false,
                success: function(jsonData)
                {
                    callback(jsonData);
                },
                error: function(msg)
                {
                    //alert(JSON.stringify(msg));
                }
            });
        }

        function getbkenddata_post(strUrl, jasonString, callback)
        {

            $.ajax({
                type: "POST",
                url: strUrl,
                data: jasonString,
                success: function(data, textStatus, jqXHR)
                {
                    callback(data);
                },
                error: function(jqXHR, textStatus, errorThrown)
                {
                    //alert(JSON.stringify(errorThrown));
                }
            });

        }

        function popupMessage(data)
        {
            if (data.indexOf(sql_nodata) > -1)
            {
                alert(" No data to show! ");
            }
            else if (data.indexOf(sql_success) > -1)
            {
                alert(" Request succeed! ");
            }
            else if (data.indexOf(sql_error) > -1)
            {
                alert(" Request failed, please try again later! ");
            }
            else if (data.indexOf(sql_dberror) > -1)
            {
                alert(" Request failed, Database error! ");
            }
        }

    </script> 

    <script src="js/base.js"></script>           

    </body>

</html>