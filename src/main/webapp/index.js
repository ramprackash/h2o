function setCookie(c_name,value,exdays) {
    var exdate=new Date();
    exdate.setDate(exdate.getDate() + exdays);
    var c_value=escape(value) + ((exdays==null) ? "" : "; expires="+exdate.toUTCString());
    document.cookie=c_name + "=" + c_value;
}

function getCookie(c_name) {
    var i,x,y,ARRcookies=document.cookie.split(";");
    for (i=0;i<ARRcookies.length;i++)
    {
      x=ARRcookies[i].substr(0,ARRcookies[i].indexOf("="));
      y=ARRcookies[i].substr(ARRcookies[i].indexOf("=")+1);
      x=x.replace(/^\s+|\s+$/g,"");
      if (x==c_name)
        {
        return unescape(y);
        }
      }
    return "";
}

// as a suggestion you could use the event listener to save the state when zoom changes or drag ends
function tilesLoaded() {
    google.maps.event.clearListeners(map, 'tilesloaded');
    google.maps.event.addListener(map, 'zoom_changed', saveMapState);
    google.maps.event.addListener(map, 'dragend', saveMapState);
}   


// functions below

function saveMapState() { 
    var mapZoom=map.getZoom(); 
    var mapCentre=map.getCenter(); 
    var mapLat=mapCentre.lat(); 
    var mapLng=mapCentre.lng(); 
    var cookiestring=mapLat+"_"+mapLng+"_"+mapZoom; 
    setCookie("myMapCookie",cookiestring, 30); 
} 


function loadMapState(map) { 
    var gotCookieString=getCookie("myMapCookie"); 
    var splitStr = gotCookieString.split("_");
    var savedMapLat = parseFloat(splitStr[0]);
    var savedMapLng = parseFloat(splitStr[1]);
    var savedMapZoom = parseFloat(splitStr[2]);
    if ((!isNaN(savedMapLat)) && (!isNaN(savedMapLng)) && (!isNaN(savedMapZoom))) {
        map.setCenter(new google.maps.LatLng(savedMapLat,savedMapLng));
        map.setZoom(savedMapZoom);
    }
}


function updateslider(year) {
    re_initialize(year, 1);
}

function dbRefresh() {

    var x;
    var r=confirm("The database refresh may take up to eight minutes. Thank you for your patience.");
    if (r==true)
    {
        $.getJSON('/stations?initdb=1', function(data) { });
        window.localStorage.setItem("testinit" , "1");
        x = "DB Refresh";
    }
    else
    {
        x = "DB Refresh";
    }
    document.getElementById("dbrefresh").innerHTML=x;

}

function markerchartinfo(map, marker, stn, id, data) {
        var dataTable = new google.visualization.DataTable();
        dataTable.addColumn('string', 'Year/Month');
        dataTable.addColumn('number', '> Monthly Avg');
        dataTable.addColumn({'type':'string', 'role':'tooltip', 'p': {'html':true}});
        dataTable.addColumn({'type':'string', 'role':'style'});
        dataTable.addColumn('number', '< Monthly Avg');

        for (var i = 0; i < data.length; i++) {
            if (data[i].month == "Jan") {
                if (data[i].level < parseFloat(stn.Jan)) {
                dataTable.addRow([data[i].month+' '+data[i].year, 
                    data[i].level, 
                    '<b>'+ data[i].month+' '+data[i].year+'<p>'+'Level: </b>'+data[i].level+'<p><b>Avg.  level for Jan: </b>'+stn.Jan,
                    'color: indianred', 0]);
                } else {
                dataTable.addRow([data[i].month+' '+data[i].year, 
                    data[i].level, 
                    '<b>'+ data[i].month+' '+data[i].year+'<p>'+'Level: </b>'+data[i].level+'<p><b>Avg.  level for Jan: </b>'+stn.Jan,
                    'color: cornflowerblue', 0]);
                }
            }
            if (data[i].month == "Feb") {
                if (data[i].level < parseFloat(stn.Feb)) {
                dataTable.addRow([data[i].month+' '+data[i].year, 
                    data[i].level, 
                    '<b>'+ data[i].month+' '+data[i].year+'<p>'+'Level: </b>'+data[i].level+'<p><b>Avg.  level for Feb: </b>'+stn.Feb,
                    'color: indianred', 0]);
                } else {
                dataTable.addRow([data[i].month+' '+data[i].year, 
                    data[i].level, 
                    '<b>'+ data[i].month+' '+data[i].year+'<p>'+'Level: </b>'+data[i].level+'<p><b>Avg.  level for Feb: </b>'+stn.Feb, 
                    'color: cornflowerblue', 0]);
                }
            }
            if (data[i].month == "Mar") {
                if (data[i].level < parseFloat(stn.Mar)) {
                dataTable.addRow([data[i].month+' '+data[i].year, 
                    data[i].level, 
                    '<b>'+ data[i].month+' '+data[i].year+'<p>'+'Level: </b>'+data[i].level+'<p><b>Avg.  level for Mar: </b>'+stn.Mar, 
                    'color: indianred', 0]);
                } else {
                dataTable.addRow([data[i].month+' '+data[i].year, 
                    data[i].level, 
                    '<b>'+ data[i].month+' '+data[i].year+'<p>'+'Level: </b>'+data[i].level+'<p><b>Avg.  level for Mar: </b>'+stn.Mar, 
                    'color: cornflowerblue', 0]);
                }
            }
            if (data[i].month == "Apr") {
                if (data[i].level < parseFloat(stn.Apr)) {
                dataTable.addRow([data[i].month+' '+data[i].year, 
                    data[i].level, 
                    '<b>'+ data[i].month+' '+data[i].year+'<p>'+'Level: </b>'+data[i].level+'<p><b>Avg.  level for Apr: </b>'+stn.Apr, 
                    'color: indianred', 0]);
                } else {
                dataTable.addRow([data[i].month+' '+data[i].year, 
                    data[i].level, 
                    '<b>'+ data[i].month+' '+data[i].year+'<p>'+'Level: </b>'+data[i].level+'<p><b>Avg.  level for Apr: </b>'+stn.Apr, 
                    'color: cornflowerblue', 0]);
                }
            }
            if (data[i].month == "May") {
                if (data[i].level < parseFloat(stn.May)) {
                dataTable.addRow([data[i].month+' '+data[i].year, 
                    data[i].level, 
                    '<b>'+ data[i].month+' '+data[i].year+'<p>'+'Level: </b>'+data[i].level+'<p><b>Avg.  level for May: </b>'+stn.May, 
                    'color: indianred', 0]);
                } else {
                dataTable.addRow([data[i].month+' '+data[i].year, 
                    data[i].level, 
                    '<b>'+ data[i].month+' '+data[i].year+'<p>'+'Level: </b>'+data[i].level+'<p><b>Avg.  level for May: </b>'+stn.May, 
                    'color: cornflowerblue', 0]);
                }
            }
            if (data[i].month == "Jun") {
                if (data[i].level < parseFloat(stn.Jun)) {
                dataTable.addRow([data[i].month+' '+data[i].year, 
                    data[i].level, 
                    '<b>'+ data[i].month+' '+data[i].year+'<p>'+'Level: </b>'+data[i].level+'<p><b>Avg.  level for Jun: </b>'+stn.Jun, 
                    'color: indianred', 0]);
                } else {
                dataTable.addRow([data[i].month+' '+data[i].year, 
                    data[i].level, 
                    '<b>'+ data[i].month+' '+data[i].year+'<p>'+'Level: </b>'+data[i].level+'<p><b>Avg.  level for Jun: </b>'+stn.Jun, 
                    'color: cornflowerblue', 0]);
                }
            }
            if (data[i].month == "Jul") {
                if (data[i].level < parseFloat(stn.Jul)) {
                dataTable.addRow([data[i].month+' '+data[i].year, 
                    data[i].level, 
                    '<b>'+ data[i].month+' '+data[i].year+'<p>'+'Level: </b>'+data[i].level+'<p><b>Avg.  level for Jul: </b>'+stn.Jul, 
                    'color: indianred', 0]);
                } else {
                dataTable.addRow([data[i].month+' '+data[i].year, 
                    data[i].level, 
                    '<b>'+ data[i].month+' '+data[i].year+'<p>'+'Level: </b>'+data[i].level+'<p><b>Avg.  level for Jul: </b>'+stn.Jul, 
                    'color: cornflowerblue', 0]);
                }
            }
            if (data[i].month == "Aug") {
                if (data[i].level < parseFloat(stn.Aug)) {
                dataTable.addRow([data[i].month+' '+data[i].year, 
                    data[i].level, 
                    '<b>'+ data[i].month+' '+data[i].year+'<p>'+'Level: </b>'+data[i].level+'<p><b>Avg.  level for Aug: </b>'+stn.Aug, 
                    'color: indianred', 0]);
                } else {
                dataTable.addRow([data[i].month+' '+data[i].year, 
                    data[i].level, 
                    '<b>'+ data[i].month+' '+data[i].year+'<p>'+'Level: </b>'+data[i].level+'<p><b>Avg.  level for Aug: </b>'+stn.Aug, 
                    'color: cornflowerblue', 0]);
                }
            }
            if (data[i].month == "Sep") {
                if (data[i].level < parseFloat(stn.Sep)) {
                dataTable.addRow([data[i].month+' '+data[i].year, 
                    data[i].level, 
                    '<b>'+ data[i].month+' '+data[i].year+'<p>'+'Level: </b>'+data[i].level+'<p><b>Avg.  level for Sep: </b>'+stn.Sep, 
                    'color: indianred', 0]);
                } else {
                dataTable.addRow([data[i].month+' '+data[i].year, 
                    data[i].level, 
                    '<b>'+ data[i].month+' '+data[i].year+'<p>'+'Level: </b>'+data[i].level+'<p><b>Avg.  level for Sep: </b>'+stn.Sep, 
                    'color: cornflowerblue', 0]);
                }
            }
            if (data[i].month == "Oct") {
                if (data[i].level < parseFloat(stn.Oct)) {
                dataTable.addRow([data[i].month+' '+data[i].year, 
                    data[i].level, 
                    '<b>'+ data[i].month+' '+data[i].year+'<p>'+'Level: </b>'+data[i].level+'<p><b>Avg.  level for Oct: </b>'+stn.Oct, 
                    'color: indianred', 0]);
                } else {
                dataTable.addRow([data[i].month+' '+data[i].year, 
                    data[i].level, 
                    '<b>'+ data[i].month+' '+data[i].year+'<p>'+'Level: </b>'+data[i].level+'<p><b>Avg.  level for Oct: </b>'+stn.Oct, 
                    'color: cornflowerblue', 0]);
                }
            }
            if (data[i].month == "Nov") {
                if (data[i].level < parseFloat(stn.Nov)) {
                dataTable.addRow([data[i].month+' '+data[i].year, 
                    data[i].level, 
                    '<b>'+ data[i].month+' '+data[i].year+'<p>'+'Level: </b>'+data[i].level+'<p><b>Avg.  level for Nov: </b>'+stn.Nov, 
                    'color: indianred', 0]);
                } else {
                dataTable.addRow([data[i].month+' '+data[i].year, 
                    data[i].level, 
                    '<b>'+ data[i].month+' '+data[i].year+'<p>'+'Level: </b>'+data[i].level+'<p><b>Avg.  level for Nov: </b>'+stn.Nov, 
                    'color: cornflowerblue', 0]);
                }
            }
            if (data[i].month == "Dec") {
                if (data[i].level < parseFloat(stn.Dec)) {
                dataTable.addRow([data[i].month+' '+data[i].year, 
                    data[i].level, 
                    '<b>'+ data[i].month+' '+data[i].year+'<p>'+'Level: </b>'+data[i].level+'<p><b>Avg.  level for Dec: </b>'+stn.Dec, 
                    'color: indianred', 0]);
                } else {
                dataTable.addRow([data[i].month+' '+data[i].year, 
                    data[i].level, 
                    '<b>'+ data[i].month+' '+data[i].year+'<p>'+'Level: </b>'+data[i].level+'<p><b>Avg.  level for Dec: </b>'+stn.Dec, 
                    'color: cornflowerblue', 0]);
                }
            }
        }

        var options = {'title': 'Water Table over 10 years for '+id+'('+stn.dam+')',
            'tooltip': {isHtml: true},
            'width':600,
            'height':400
        };
        var node = document.createElement('div');
        node.id = 'chart_canvas';
        var chart = new google.visualization.ColumnChart(node);
        chart.draw(dataTable, options);

        var infowindow = new google.maps.InfoWindow({
            content: ""
        });
        infowindow.setContent(node);
        infowindow.open(map, marker);
}

function markeraction(map, marker, stn) {
    var id = marker.getTitle();
    var resturl = '/stationhistory?id='+id;
    $.getJSON(resturl, function(data) {

        markerchartinfo(map, marker, stn, id, data);

    });
}

function initialize() {
   re_initialize("2014", 0);
}
                
function re_initialize(year, center) {
    if (window.localStorage.getItem("testinit") == "1") {
        $.getJSON('/stations?initstatus=1', function(data) {
            if (data.initstatus < 2) {
                alert("Database being refreshed. Please be patient...\nRetrieved "+data.progress+" records");
                return;
            } else {
                window.localStorage.setItem("testinit", "0");
            }
        });
    }

    var mapOptions = {
        center: new google.maps.LatLng(36.407385,-115.392557), 
        mapTypeId: google.maps.MapTypeId.TERRAIN,
        zoom: 6
    };
    var map = new google.maps.Map(document.getElementById("map_canvas"),
            mapOptions);
    //google.maps.event.addListener(map, 'tilesloaded', tilesLoaded);
    google.maps.event.addListener(map, 'zoom_changed', function () {
        var mapZoom=map.getZoom(); 
        var mapCentre=map.getCenter(); 
        var mapLat=mapCentre.lat(); 
        var mapLng=mapCentre.lng(); 
        var cookiestring=mapLat+"_"+mapLng+"_"+mapZoom; 
        setCookie("myMapCookie",cookiestring, 30); 
    });
    google.maps.event.addListener(map, 'dragend', function () {
        var mapZoom=map.getZoom(); 
        var mapCentre=map.getCenter(); 
        var mapLat=mapCentre.lat(); 
        var mapLng=mapCentre.lng(); 
        var cookiestring=mapLat+"_"+mapLng+"_"+mapZoom; 
        setCookie("myMapCookie",cookiestring, 30); 
    });

    if (center) {
        loadMapState(map);
    }

    var pinColorRed = "FE7569";
    var pinImageRed = new google.maps.MarkerImage("http://chart.apis.google.com/chart?chst=d_map_pin_letter&chld=%E2%80%A2|" + pinColorRed,
                                               new google.maps.Size(21, 34),
                                               new google.maps.Point(0,0),
                                               new google.maps.Point(10, 34));

    $.getJSON('/stations', function(data) {
        $.each(data, function(i, stn) {
            var myLatLng = new google.maps.LatLng(stn.lat, stn.lon);
            var marker = new google.maps.Marker({
                position: myLatLng, map: map, title: stn.id, icon: pinImageRed
            });

            // Change marker color if the current level is
            // greater than the last reported month's average
            var monthNames = [ "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12" ];

            var d = new Date();
            if (year == "2014") {
                var resturl = '/stationhistory?id='+stn.id+'&upto=1&year='+year;
            } else {
                var resturl = '/stationhistory?id='+stn.id+'&upto=1&year='+year+'&month='+monthNames[d.getMonth()];
            }

            $.getJSON(resturl, function(data) {
                var pinColorBlue = "90C1F2";
                var pinImageBlue = new google.maps.MarkerImage("http://chart.apis.google.com/chart?chst=d_map_pin_letter&chld=%E2%80%A2|" + pinColorBlue,
                    new google.maps.Size(21, 34),
                    new google.maps.Point(0,0),
                    new google.maps.Point(10, 34));
                var last = data.length - 1;
                if (data[last].month == "Jan") {
                    if (data[last].level > parseFloat(stn.Jan)) {
                        marker.setIcon(pinImageBlue);
                    }
                }
                if (data[last].month == "Feb") {
                    if (data[last].level > parseFloat(stn.Feb)) {
                        marker.setIcon(pinImageBlue);
                    }
                }
                if (data[last].month == "Apr") {
                    if (data[last].level > parseFloat(stn.Apr)) {
                        marker.setIcon(pinImageBlue);
                    }
                }
                if (data[last].month == "Mar") {
                    if (data[last].level > parseFloat(stn.Mar)) {
                        marker.setIcon(pinImageBlue);
                    }
                }
                if (data[last].month == "May") {
                    if (data[last].level > parseFloat(stn.May)) {
                        marker.setIcon(pinImageBlue);
                    }
                }
                if (data[last].month == "Jun") {
                    if (data[last].level > parseFloat(stn.Jun)) {
                        marker.setIcon(pinImageBlue);
                    }
                }
                if (data[last].month == "Jul") {
                    if (data[last].level > parseFloat(stn.Jul)) {
                        marker.setIcon(pinImageBlue);
                    }
                }
                if (data[last].month == "Aug") {
                    if (data[last].level > parseFloat(stn.Aug)) {
                        marker.setIcon(pinImageBlue);
                    }
                }
                if (data[last].month == "Sep") {
                    if (data[last].level > parseFloat(stn.Sep)) {
                        marker.setIcon(pinImageBlue);
                    }
                }
                if (data[last].month == "Oct") {
                    if (data[last].level > parseFloat(stn.Oct)) {
                        marker.setIcon(pinImageBlue);
                    }
                }
                if (data[last].month == "Nov") {
                    if (data[last].level > parseFloat(stn.Nov)) {
                        marker.setIcon(pinImageBlue);
                    }
                }
                if (data[last].month == "Dec") {
                    if (data[last].level > parseFloat(stn.Dec)) {
                        marker.setIcon(pinImageBlue);
                    }
                }
            });

            google.maps.event.addListener(marker, 'click', function() {
                markeraction(map, marker, stn);
            });
        });
    });
}
