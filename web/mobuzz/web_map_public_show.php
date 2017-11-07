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
                            <div class="widget widget-nopad">
                                <div class="widget-header"> <i class="icon-list-alt"></i>
                                    <h3> Generate Clusters</h3>
                                </div>
                                <!-- /widget-header -->
                                <div class="widget-content">
                                    <table style="width: 100%;" border="0">
                                        <tr>
                                            <td style="width: 230px; background-color:#889CC3; vertical-align:text-top;">

                                                <div>
                                                    <table style="width: 100%; height: 100%;" border="0">
                                                        <tr>
                                                            <td colspan="2"><b><u>Hotspot creation parameters:</u></b></td>
                                                        </tr>  
                                                        <tr>
                                                            <td style="width: 100%;">Danger radius (m):</td>
                                                            <td><input type="text" id="danger_zone" name="danger_zone" style="width:80px;" value="100"/></td>
                                                        </tr>    
                                                        <tr>
                                                            <td colspan="2"><b><u>Cluster creation parameters:</u></b></td>
                                                        </tr>  
                                                        <tr>
                                                            <td>Maximum adjacent distance (m):</td>
                                                            <td><input type="text" id="cluster_radius" name="cluster_radius" style="width:80px;" value="1000"/></td>
                                                        </tr>  
                                                        <tr>
                                                            <td>Minimum density:</td>
                                                            <td><input type="text" id="cluster_density" name="cluster_density" style="width:80px;" value="5"/></td>
                                                        </tr>  
                                                        <tr>
                                                            <td colspan="2"><b><u>Data selection parameters:</u></b></td>
                                                        </tr>                             
                                                        <tr>
                                                            <td>Year:</td>
                                                            <td><input type="text" id="year" name="year" style="width:80px;" value="2014"/></td>
                                                        </tr>                            
                                                        <tr>
                                                            <td>Week of Year:</td>
                                                            <td><input type="text" id="week_of_year" name="week_of_year" style="width:80px;"/></td>
                                                        </tr>
                                                        <tr>
                                                            <td colspan="2" style="text-align: right"><button  class="btn btn-medium btn-success" onClick="initData()">Load Data</button></td>
                                                        </tr>
                                                    </table>
                                                </div>

                                                <div id="clusterinfo" style="text-align: center;">
                                                    <b><u>Clusters:</u></b>
                                                    <div id="maptag" style="overflow-x: hidden; height: 622px; width:100%;">
                                                        <table id="table" border="0"><thead id="table-head"><tr><th></th></tr></thead><tbody id="table-body"></tbody></table>
                                                    </div>
                                                    <button class="btn btn-medium btn-success" onClick="saveCluster()" style="margin-bottom:1px; margin-top: 2px;">Save Cluster</button>
                                                    <button class="btn btn-medium btn-success" onClick="loadCluster()" style="margin-bottom:1px; margin-top: 2px;">Load Cluster</button>                        
                                                </div>

                                            </td>
                                            <td>
                                                <div id="map"></div>
                                                <div id="msg"></div>
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

        <script src="js/jquery-1.7.2.min.js"></script> 
        <script src="js/excanvas.min.js"></script> 
        <script src="js/bootstrap.js"></script>
        <script src="http://maps.googleapis.com/maps/api/js?sensor=false" type="text/javascript"></script>
        <script type="text/javascript" src="js/gmap3.js"></script>
        <script type="text/javascript">

                var jHotspots = null;
                var sql_success = "SQLSuccess";
                var sql_error = "SQLError";
                var sql_dberror = "DBError";
                var sql_nodata = "SQL_no_data";
                var hotspot_def_color_rgb = "#F5A9A9";


                $(function() {

                    document.getElementById('clusterinfo').style.visibility = 'hidden';

                    $('#map').gmap3({
                        map: {
                            options: {
                                center: [6.911640, 79.87],
                                zoom: 13
                            }
                        }
                    });

                    $("#map").gmap3({
                        kmllayer: {
                            options: {
                                url: "http://202.129.235.206/mobuzz/kml/cmcregions.kml",
                                opts: {
                                    suppressInfoWindows: true
                                }
                            },
                            events: {
                            }
                        }

                    });

                });

                function initData()
                {

                    var danger_zone = document.getElementById('danger_zone').value;
                    var cluster_radius = document.getElementById('cluster_radius').value;
                    var cluster_density = document.getElementById('cluster_density').value;
                    var year = document.getElementById('year').value;
                    var week_of_year = document.getElementById('week_of_year').value;

                    clearall(function() {
                        defHotspots(parseInt(year), parseInt(week_of_year));
                        ckbPtmline(parseInt(year), parseInt(week_of_year), parseInt(danger_zone), parseInt(cluster_radius), parseInt(cluster_density)); //(year, week_of_year, danger_zone, cluster_radius, cluster_density)
                    });

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

                function defHotspots(year, week_of_year)
                {

                    getbkenddata("map_public_marker_locations.php", year + "$" + week_of_year, function(jData) {

                        var jMarkers;
                        try {
                            var jLocatins = jData.split("$");
                            jMarkers = eval('(' + jLocatins[2] + ')');
                        } catch (e) {
                            if (e instanceof SyntaxError) {
                                //alert(e.message);
                            }
                        }

                        if (!$.isArray(jMarkers) || !jMarkers.length)
                        {

                            cleartag('MARKER', 'hp_def', function() {
                                popupMessage("SQL_no_data");
                            });

                        }
                        else {

                            cleartag('MARKER', 'hp_def', function() {

                                $.each(jMarkers, function(i, item) {

                                    $("#map").gmap3({

                                        marker: {
                                            latLng: [item.lat, item.lng],
                                            data: item.dbid,
                                            events:{
                                               click: function(marker, event, context) {

                                                   var datacont = context.data;

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
                                                               content: "<div style='width:85px;'>DB Item: "+datacont+"</div>"
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

                                    });

                                });

                                $("#map").gmap3("autofit");

                            });

                        }
                    });

                }


                function ckbPtmline(year, week_of_year, danger_zone, cluster_radius, cluster_density)
                {

                    getbkenddata("map_public_clusters_manage.php", year + "$" + week_of_year + "$" + danger_zone + "$" + cluster_radius + "$" + cluster_density, function(jData) {

                        try
                        {
                            if (jData.indexOf("SQL_no_data") > -1)
                            {
                                popupMessage("SQL_no_data");
                            }
                            else
                            {
                                document.getElementById('maptag').innerHTML = '<table id="table"><thead id="table-head"><tr><th></th></tr></thead><tbody id="table-body"></tbody></table>';
                                jHotspots = eval('(' + jData + ')');
                                for (i in jHotspots)
                                {
                                    creatckbox(jHotspots[i]["tag"]);
                                }

                                checkstatus();
                            }

                        } catch (e) {
                            if (e instanceof SyntaxError) {
                                //alert(e.message);
                            }
                        }

                    });
                    document.getElementById('clusterinfo').style.visibility = 'visible';

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


                function creatckbox(nametag)
                {

                    var table = document.getElementById("table");
                    var row = document.createElement("tr");
                    var cell = document.createElement("td");
                    var label = document.createElement("label");
                    var description = document.createTextNode("Cluster No: " + (nametag + 1));
                    var checkbox = document.createElement("input");
                    checkbox.class = "hotspot";
                    checkbox.type = "checkbox";
                    checkbox.name = "hotspot"+nametag;
                    checkbox.id = "hotspot"+nametag;
                    checkbox.value = nametag;
                    checkbox.onclick = new Function("checkstatusitem (this);");
                    checkbox.checked = true;
                    label.appendChild(checkbox);
                    label.appendChild(description);
                    cell.appendChild(label);
                    row.appendChild(cell);
                    table.appendChild(row);

                }

                var selectedcircle;

                function checkstatus() {

                    $('#map').gmap3({clear: {name: 'infowindow', all: true}});

                    cleartype('CIRCLE', function() {

                        $('input[type=checkbox]').each(function() {

                            if (this.checked)
                            {
                                var i = this.value;

                                $("#map").gmap3({
                                    circle: {
                                        tag: "hpc_" + jHotspots[i]["tag"],
                                        data: jHotspots[i]["data"],
                                        options: {
                                            center: [jHotspots[i]["lat"], jHotspots[i]["lng"]],
                                            radius: jHotspots[i]["rad"],
                                            fillColor: hotspot_def_color_rgb, //jHotspots[i]["fill"],
                                            strokeColor: hotspot_def_color_rgb, //jHotspots[i]["stroke"],
                                            fillOpacity: 0.5,
                                            strokeOpacity: 0.5,
                                            strokeWeight: 1,
                                            editable: true,
                                            draggable: true,
                                        },
                                        events: {
                                            click: function(marker, event, context) {

                                                var datacont = context.data.split(",");

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

                                                selectedcircle = marker;

                                                $("#map").gmap3({
                                                    infowindow: {
                                                        latLng: [parseFloat(datacont[0]), parseFloat(datacont[1])],
                                                        options: {
                                                            content: '<table style="width:100px; height:80px;"><tr><td>Cluster No: ' + (parseInt(datacont[2]) + 1) + '</td></tr><tr><td><button type="button" style="background-color: #F5A9A9" onclick="selectedcircle.setOptions({strokeColor: \'#F5A9A9\', fillColor: \'#F5A9A9\'})">&nbsp;</button><button type="button" style="background-color: #FA5858" onclick="selectedcircle.setOptions({strokeColor: \'#FA5858\', fillColor: \'#FA5858\'})">&nbsp;</button><button type="button" style="background-color: #FF0000" onclick="selectedcircle.setOptions({strokeColor: \'#FF0000\', fillColor: \'#FF0000\'})">&nbsp;</button><button type="button" style="background-color: #DF0101" onclick="selectedcircle.setOptions({strokeColor: \'#DF0101\', fillColor: \'#DF0101\'})">&nbsp;</button><button type="button" style="background-color: #B40404" onclick="selectedcircle.setOptions({strokeColor: \'#B40404\', fillColor: \'#B40404\'})">&nbsp;</button></td></tr><tr><td><input type="checkbox" name="cluster" value="cluster" onclick="clusterclick( this,'+ parseInt(datacont[2]) +')" checked> Show<br></td></tr></table>'
                                                        },
                                                        events: {
                                                            closeclick: function(infowindow) {
                                                            }
                                                        }
                                                    }
                                                });
                                            }

                                        }
                                    },
                                    callback: function() {

                                    }

                                });
                            }
                        });
                    });

                }

                function clusterclick(checkbox, clusterid)
                {

                    var checkbxvalue = document.getElementById("hotspot"+clusterid);

                    var circle = $("#map").gmap3({
                        get: {
                            name: "circle",
                            tag: "hpc_" + clusterid,
                            all: false
                        }
                    });

                    if (checkbox.checked)
                    {
                        circle.setMap($("#map").gmap3("get"));
                        checkbxvalue.checked = true;
                    }
                    else
                    {
                        circle.setMap(null);
                        checkbxvalue.checked = false;
                    }

                    //alert(checkvalue);
                }

                function checkstatusitem(checkbox) {

                    var circle = $("#map").gmap3({
                        get: {
                            name: "circle",
                            tag: "hpc_" + checkbox.value,
                            all: false
                        }
                    });

                    if (checkbox.checked)
                    {
                        circle.setMap($("#map").gmap3("get"));
                    }
                    else
                    {
                        circle.setMap(null);
                    }

                }

                function saveCluster()
                {
                    var year = document.getElementById('year').value;
                    var week_of_year = document.getElementById('week_of_year').value;

                    getbkenddata("util_public_time.php", year + "$" + week_of_year, function(jData) {


                        var todate = "";
                        var fromdate = "";
                        var fromyear = 0;
                        var fromyearweek = 0;

                        try {
                            var jDates = jData.split("$");
                            todate = jDates[0];
                            fromdate = jDates[1];
                            fromyear = parseInt(jDates[2]);
                            fromyearweek = parseInt(jDates[3]);
                        } catch (e) {
                            if (e instanceof SyntaxError) {
                                //alert(e.message);
                            }
                        }

                        if (fromyear > 0 && fromyearweek > 0)
                        {

                            var checkString = "[";
                            var isFirst = true;

                            $('input[type=checkbox]').each(function() {

                                if (this.checked)
                                {
                                    var j = this.value;

                                    var markers = $("#map").gmap3({
                                        get: {
                                            name: "circle",
                                            tag: "hpc_" + j,
                                            all: true
                                        }
                                    });

                                    if (markers.length>0)
                                    {
										//add transparency to android-circles
										var fcolor = markers[0].get('fillColor');
										fcolor = fcolor.replace("#", "#7F");
									
                                        if (isFirst)
                                        {
                                            isFirst = false;
                                            checkString += '{"lat":' + parseFloat(markers[0].getCenter().lat()).toFixed(6) + ', "lng":' + parseFloat(markers[0].getCenter().lng()).toFixed(6) + ', "rad":' + parseInt(markers[0].getRadius()) + ', "color":"' + fcolor + '","fromdate":"' + fromdate + '","todate":"' + todate + '","toyear":' + year + ',"toweek":' + week_of_year + '}';
                                        }
                                        else
                                        {
                                            checkString += ',{"lat":' + parseFloat(markers[0].getCenter().lat()).toFixed(6) + ', "lng":' + parseFloat(markers[0].getCenter().lng()).toFixed(6) + ', "rad":' + parseInt(markers[0].getRadius()) + ', "color":"' + fcolor + '"}';
                                        }
                                    }

                                }
                            });
                            checkString += "]";

                            //save the selection to the database
                            if (checkString.length >= 4)
                            {
                                var danger_zone = document.getElementById('danger_zone').value;
                                var cluster_radius = document.getElementById('cluster_radius').value;
                                var cluster_density = document.getElementById('cluster_density').value;
                                var post_string = "year=" + year + "&week_of_year=" + week_of_year + "&danger_zone=" + danger_zone + "&cluster_radius=" + cluster_radius + "&cluster_density=" + cluster_density + "&checkString=" + checkString;

                                getbkenddata_post("map_public_clusters_save.php", post_string, function(jData) {
                                    popupMessage(jData);
                                });
                            }
                            else
                            {
                                alert(" No data to save! ");
                            }

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

                function loadCluster()
                {
                    var year = document.getElementById('year').value;
                    var week_of_year = document.getElementById('week_of_year').value;

                    getbkenddata("map_public_clusters_locations.php", year + "$" + week_of_year, function(jData) {

                        var jCircles;
                        try
                        {
                            jCircles = eval('(' + jData + ')');
                        } catch (e) {
                            if (e instanceof SyntaxError) {
                                //alert(e.message);
                            }
                        }

                        if (!$.isArray(jCircles) || !jCircles.length)
                        {
                            clearall(function() {
                                popupMessage("SQL_no_data");
                            });
                        }
                        else {

                            clearall(function() {

                                $.each(jCircles, function(i, item) {

                                    $("#map").gmap3({
                                        circle: {
                                            tag: 'hp_clust',
                                            options: {
                                                center: [item.lat, item.lng],
                                                radius: item.rad,
                                                fillColor: item.color,
                                                strokeColor: item.color,
                                                fillOpacity: 0.5,
                                                strokeOpacity: 0.5,
                                                strokeWeight: 1
                                            }
                                        }
                                    });
                                });

                                document.getElementById('maptag').innerHTML = '<table id="table"><thead id="table-head"><tr><th></th></tr></thead><tbody id="table-body"></tbody></table>';

                            });
                        }

                    });

                }

        </script> 

        <script src="js/base.js"></script>           

    </body>

</html>