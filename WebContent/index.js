
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
        for (var i = 0; i < data.length; i++) {
            //dataTable.addRow([data[i].month+' '+data[i].year, data[i].level]);
            if (data[i].month == "Jan") {
                dataTable.addRow([data[i].month+' '+data[i].year, 
                    data[i].level, 
                    '<b>'+ data[i].month+' '+data[i].year+'<p>'+'Level: </b>'+data[i].level+'<p><b>Avg.  level for Jan: </b>'+stn.Jan]);
            }
            if (data[i].month == "Feb") {
                dataTable.addRow([data[i].month+' '+data[i].year, 
                    data[i].level, 
                    '<b>'+ data[i].month+' '+data[i].year+'<p>'+'Level: </b>'+data[i].level+'<p><b>Avg.  level for Feb: </b>'+stn.Feb]);
            }
            if (data[i].month == "Mar") {
                dataTable.addRow([data[i].month+' '+data[i].year, 
                    data[i].level, 
                    '<b>'+ data[i].month+' '+data[i].year+'<p>'+'Level: </b>'+data[i].level+'<p><b>Avg.  level for Mar: </b>'+stn.Mar]);
            }
            if (data[i].month == "Apr") {
                dataTable.addRow([data[i].month+' '+data[i].year, 
                        data[i].level, 
                        '<b>'+ data[i].month+' '+data[i].year+'<p>'+'Level: </b>'+data[i].level+'<p><b>Avg.  level for Apr: </b>'+stn.Apr]);
            }
            if (data[i].month == "May") {
                dataTable.addRow([data[i].month+' '+data[i].year, 
                        data[i].level, 
                        '<b>'+ data[i].month+' '+data[i].year+'<p>'+'Level: </b>'+data[i].level+'<p><b>Avg.  level for May: </b>'+stn.May]);
            }
            if (data[i].month == "Jun") {
                dataTable.addRow([data[i].month+' '+data[i].year, 
                        data[i].level, 
                        '<b>'+ data[i].month+' '+data[i].year+'<p>'+'Level: </b>'+data[i].level+'<p><b>Avg.  level for Jun: </b>'+stn.Jun]);
            }
            if (data[i].month == "Jul") {
                dataTable.addRow([data[i].month+' '+data[i].year, 
                        data[i].level, 
                        '<b>'+ data[i].month+' '+data[i].year+'<p>'+'Level: </b>'+data[i].level+'<p><b>Avg.  level for Jul: </b>'+stn.Jul]);
            }
            if (data[i].month == "Aug") {
                dataTable.addRow([data[i].month+' '+data[i].year, 
                        data[i].level, 
                        '<b>'+ data[i].month+' '+data[i].year+'<p>'+'Level: </b>'+data[i].level+'<p><b>Avg.  level for Aug: </b>'+stn.Aug]);
            }
            if (data[i].month == "Sep") {
                dataTable.addRow([data[i].month+' '+data[i].year, 
                        data[i].level, 
                        '<b>'+ data[i].month+' '+data[i].year+'<p>'+'Level: </b>'+data[i].level+'<p><b>Avg.  level for Sep: </b>'+stn.Sep]);
            }
            if (data[i].month == "Oct") {
                dataTable.addRow([data[i].month+' '+data[i].year, 
                        data[i].level, 
                        '<b>'+ data[i].month+' '+data[i].year+'<p>'+'Level: </b>'+data[i].level+'<p><b>Avg.  level for Oct: </b>'+stn.Oct]);
            }
            if (data[i].month == "Nov") {
                dataTable.addRow([data[i].month+' '+data[i].year, 
                        data[i].level, 
                        '<b>'+ data[i].month+' '+data[i].year+'<p>'+'Level: </b>'+data[i].level+'<p><b>Avg.  level for Nov: </b>'+stn.Nov]);
            }
            if (data[i].month == "Dec") {
                dataTable.addRow([data[i].month+' '+data[i].year, 
                        data[i].level, 
                        '<b>'+ data[i].month+' '+data[i].year+'<p>'+'Level: </b>'+data[i].level+'<p><b>Avg.  level for Dec: </b>'+stn.Dec]);
            }
        }

        var options = {'title': 'Water Table over 10 years for '+id,
            'tooltip': {isHtml: true}, 
            'legend': 'none'
        };
        var node = document.createElement('div');
        node.id = 'chart_canvas';
        var chart = new google.visualization.ColumnChart(node);
        chart.draw(dataTable, options);

        var infowindow = new google.maps.InfoWindow({
            // Just an initializer. Actual content initialized below
            content: "Hello World"
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
                alert("Database being refreshed. Comeback after a jog!  "+data.initstatus+" "+data.progress+" "+data.url);
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
