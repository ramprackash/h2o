
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
        dataTable.addColumn('number', 'Water Level');
        dataTable.addColumn({'type':'string', 'role':'tooltip', 'p': {'html':true}});
        dataTable.addColumn({'type':'string', 'role':'style'});
        for (var i = 0; i < data.length; i++) {
            if (data[i].month == "Jan") {
                if (data[i].level < parseFloat(stn.Jan)) {
                dataTable.addRow([data[i].month+' '+data[i].year, 
                    data[i].level, 
                    '<b>'+ data[i].month+' '+data[i].year+'<p>'+'Level: </b>'+data[i].level+'<p><b>Avg.  level for Jan: </b>'+stn.Jan,
                    'color: indianred']);
                } else {
                dataTable.addRow([data[i].month+' '+data[i].year, 
                    data[i].level, 
                    '<b>'+ data[i].month+' '+data[i].year+'<p>'+'Level: </b>'+data[i].level+'<p><b>Avg.  level for Jan: </b>'+stn.Jan,
                    'color: cornflowerblue']);
                }
            }
            if (data[i].month == "Feb") {
                if (data[i].level < parseFloat(stn.Feb)) {
                dataTable.addRow([data[i].month+' '+data[i].year, 
                    data[i].level, 
                    '<b>'+ data[i].month+' '+data[i].year+'<p>'+'Level: </b>'+data[i].level+'<p><b>Avg.  level for Feb: </b>'+stn.Feb,
                    'color: indianred']);
                } else {
                dataTable.addRow([data[i].month+' '+data[i].year, 
                    data[i].level, 
                    '<b>'+ data[i].month+' '+data[i].year+'<p>'+'Level: </b>'+data[i].level+'<p><b>Avg.  level for Feb: </b>'+stn.Feb, 
                    'color: cornflowerblue']);
                }
            }
            if (data[i].month == "Mar") {
                if (data[i].level < parseFloat(stn.Mar)) {
                dataTable.addRow([data[i].month+' '+data[i].year, 
                    data[i].level, 
                    '<b>'+ data[i].month+' '+data[i].year+'<p>'+'Level: </b>'+data[i].level+'<p><b>Avg.  level for Mar: </b>'+stn.Mar, 
                    'color: indianred']);
                } else {
                dataTable.addRow([data[i].month+' '+data[i].year, 
                    data[i].level, 
                    '<b>'+ data[i].month+' '+data[i].year+'<p>'+'Level: </b>'+data[i].level+'<p><b>Avg.  level for Mar: </b>'+stn.Mar, 
                    'color: cornflowerblue']);
                }
            }
            if (data[i].month == "Apr") {
                if (data[i].level < parseFloat(stn.Apr)) {
                dataTable.addRow([data[i].month+' '+data[i].year, 
                    data[i].level, 
                    '<b>'+ data[i].month+' '+data[i].year+'<p>'+'Level: </b>'+data[i].level+'<p><b>Avg.  level for Apr: </b>'+stn.Apr, 
                    'color: indianred']);
                } else {
                dataTable.addRow([data[i].month+' '+data[i].year, 
                    data[i].level, 
                    '<b>'+ data[i].month+' '+data[i].year+'<p>'+'Level: </b>'+data[i].level+'<p><b>Avg.  level for Apr: </b>'+stn.Apr, 
                    'color: cornflowerblue']);
                }
            }
            if (data[i].month == "May") {
                if (data[i].level < parseFloat(stn.May)) {
                dataTable.addRow([data[i].month+' '+data[i].year, 
                    data[i].level, 
                    '<b>'+ data[i].month+' '+data[i].year+'<p>'+'Level: </b>'+data[i].level+'<p><b>Avg.  level for May: </b>'+stn.May, 
                    'color: indianred']);
                } else {
                dataTable.addRow([data[i].month+' '+data[i].year, 
                    data[i].level, 
                    '<b>'+ data[i].month+' '+data[i].year+'<p>'+'Level: </b>'+data[i].level+'<p><b>Avg.  level for May: </b>'+stn.May, 
                    'color: cornflowerblue']);
                }
            }
            if (data[i].month == "Jun") {
                if (data[i].level < parseFloat(stn.Jun)) {
                dataTable.addRow([data[i].month+' '+data[i].year, 
                    data[i].level, 
                    '<b>'+ data[i].month+' '+data[i].year+'<p>'+'Level: </b>'+data[i].level+'<p><b>Avg.  level for Jun: </b>'+stn.Jun, 
                    'color: indianred']);
                } else {
                dataTable.addRow([data[i].month+' '+data[i].year, 
                    data[i].level, 
                    '<b>'+ data[i].month+' '+data[i].year+'<p>'+'Level: </b>'+data[i].level+'<p><b>Avg.  level for Jun: </b>'+stn.Jun, 
                    'color: cornflowerblue']);
                }
            }
            if (data[i].month == "Jul") {
                if (data[i].level < parseFloat(stn.Jul)) {
                dataTable.addRow([data[i].month+' '+data[i].year, 
                    data[i].level, 
                    '<b>'+ data[i].month+' '+data[i].year+'<p>'+'Level: </b>'+data[i].level+'<p><b>Avg.  level for Jul: </b>'+stn.Jul, 
                    'color: indianred']);
                } else {
                dataTable.addRow([data[i].month+' '+data[i].year, 
                    data[i].level, 
                    '<b>'+ data[i].month+' '+data[i].year+'<p>'+'Level: </b>'+data[i].level+'<p><b>Avg.  level for Jul: </b>'+stn.Jul, 
                    'color: cornflowerblue']);
                }
            }
            if (data[i].month == "Aug") {
                if (data[i].level < parseFloat(stn.Aug)) {
                dataTable.addRow([data[i].month+' '+data[i].year, 
                    data[i].level, 
                    '<b>'+ data[i].month+' '+data[i].year+'<p>'+'Level: </b>'+data[i].level+'<p><b>Avg.  level for Aug: </b>'+stn.Aug, 
                    'color: indianred']);
                } else {
                dataTable.addRow([data[i].month+' '+data[i].year, 
                    data[i].level, 
                    '<b>'+ data[i].month+' '+data[i].year+'<p>'+'Level: </b>'+data[i].level+'<p><b>Avg.  level for Aug: </b>'+stn.Aug, 
                    'color: cornflowerblue']);
                }
            }
            if (data[i].month == "Sep") {
                if (data[i].level < parseFloat(stn.Sep)) {
                dataTable.addRow([data[i].month+' '+data[i].year, 
                    data[i].level, 
                    '<b>'+ data[i].month+' '+data[i].year+'<p>'+'Level: </b>'+data[i].level+'<p><b>Avg.  level for Sep: </b>'+stn.Sep, 
                    'color: indianred']);
                } else {
                dataTable.addRow([data[i].month+' '+data[i].year, 
                    data[i].level, 
                    '<b>'+ data[i].month+' '+data[i].year+'<p>'+'Level: </b>'+data[i].level+'<p><b>Avg.  level for Sep: </b>'+stn.Sep, 
                    'color: cornflowerblue']);
                }
            }
            if (data[i].month == "Oct") {
                if (data[i].level < parseFloat(stn.Oct)) {
                dataTable.addRow([data[i].month+' '+data[i].year, 
                    data[i].level, 
                    '<b>'+ data[i].month+' '+data[i].year+'<p>'+'Level: </b>'+data[i].level+'<p><b>Avg.  level for Oct: </b>'+stn.Oct, 
                    'color: indianred']);
                } else {
                dataTable.addRow([data[i].month+' '+data[i].year, 
                    data[i].level, 
                    '<b>'+ data[i].month+' '+data[i].year+'<p>'+'Level: </b>'+data[i].level+'<p><b>Avg.  level for Oct: </b>'+stn.Oct, 
                    'color: cornflowerblue']);
                }
            }
            if (data[i].month == "Nov") {
                if (data[i].level < parseFloat(stn.Nov)) {
                dataTable.addRow([data[i].month+' '+data[i].year, 
                    data[i].level, 
                    '<b>'+ data[i].month+' '+data[i].year+'<p>'+'Level: </b>'+data[i].level+'<p><b>Avg.  level for Nov: </b>'+stn.Nov, 
                    'color: indianred']);
                } else {
                dataTable.addRow([data[i].month+' '+data[i].year, 
                    data[i].level, 
                    '<b>'+ data[i].month+' '+data[i].year+'<p>'+'Level: </b>'+data[i].level+'<p><b>Avg.  level for Nov: </b>'+stn.Nov, 
                    'color: cornflowerblue']);
                }
            }
            if (data[i].month == "Dec") {
                if (data[i].level < parseFloat(stn.Dec)) {
                dataTable.addRow([data[i].month+' '+data[i].year, 
                    data[i].level, 
                    '<b>'+ data[i].month+' '+data[i].year+'<p>'+'Level: </b>'+data[i].level+'<p><b>Avg.  level for Dec: </b>'+stn.Dec, 
                    'color: indianred']);
                } else {
                dataTable.addRow([data[i].month+' '+data[i].year, 
                    data[i].level, 
                    '<b>'+ data[i].month+' '+data[i].year+'<p>'+'Level: </b>'+data[i].level+'<p><b>Avg.  level for Dec: </b>'+stn.Dec, 
                    'color: cornflowerblue']);
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
    if (window.localStorage.getItem("testinit") == "1") {
        $.getJSON('/stations?initstatus=1', function(data) {
            if (data.initstatus < 2) {
                alert("Database being refreshed. You have time to go for jog!  "+data.initstatus+" "+data.progress+" "+data.url);
                return;
            } else {
                window.localStorage.setItem("testinit", "0");
            }
        });
    }

    var mapOptions = {
        center: new google.maps.LatLng(39.33300018310547,-120.25), 
        zoom: 7
    };
    var map = new google.maps.Map(document.getElementById("map_canvas"),
            mapOptions);

    $.getJSON('/stations', function(data) {
        $.each(data, function(i, stn) {
            var myLatLng = new google.maps.LatLng(stn.lat, stn.lon);
            var marker = new google.maps.Marker({
                position: myLatLng, map: map, title: stn.id
            });

            google.maps.event.addListener(marker, 'click', function() {
                markeraction (map, marker, stn);
            });
        });
    });
}
