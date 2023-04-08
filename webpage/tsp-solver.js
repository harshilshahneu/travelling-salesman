var map;
var markersGroup;
var linesGroup;

const CLEAR_MAP = "clear-map";
const ADD_POINT_LIST_AND_FIT_BOUND = "add-point-list-and-fit-bound";
const POINT_RELAXED = "point-relaxed";
const DRAW_EDGE = "draw-edge"

callPingApi = () => {
    const xhttp = new XMLHttpRequest();
    xhttp.onload = () => {
        if (this.readyState == 4 && this.status == 200) {
            handlePingApiResponse(this.responseText);
        }
    };

    const url = 'http://localhost:8080/api/ping';
    xhttp.open("GET", url, true);
    xhttp.send();
}

callUploadCsvApi = () => {
    const xhttp = new XMLHttpRequest();
    xhttp.onload = () => {
        if (this.readyState == 4 && this.status == 200) {
            handleUploadCsvApiResponse(this.responseText);
        }
    };

    var fileInput = document.getElementById('formFile');
    var formData = new FormData();
    formData.append('multiPartFile', fileInput.files[0]);

    const url = 'http://localhost:8080/api/csv';
    xhttp.open("POST", url, true);
    xhttp.send(formData);
}

handlePingApiResponse = (responseText) => {
    console.log(responseText);
}

handleUploadCsvApiResponse = (responseText) => {
    console.log(responseText);
}

init = () => {
    initMap();

    document.getElementById('upload-button').addEventListener('click', uploadButtonClicked);

    setupWebsocketConnection();
}

initMap = () => {
    map = L.map('map').setView([51.505, -0.09], 13);

    L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 19,
        attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
    }).addTo(map);

    markersGroup = L.featureGroup();

    markersGroup.findById = function(id) {
        for (var layer of this.getLayers()) {
            if (layer.id === id) {
                return layer;
            }
        }
    }

    map.addLayer(markersGroup);

    linesGroup = L.featureGroup();

    linesGroup.findById = function(id) {
        for (var layer of this.getLayers()) {
            if (layer.id === id) {
                return layer;
            }
        }
    }

    map.addLayer(linesGroup);
}

uploadButtonClicked = () => {
    // callPingApi();
    callUploadCsvApi();
}

setupWebsocketConnection = () => {
    console.log('setting up web socket connection');

    const url = 'ws://localhost:8080/tsp-websocket';
    
    var stompClient = Stomp.client(url);

    // Not using arrow function sytax here, caused issues when I worked previously like that
    var connectionCallback = (frame) => {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/graph-action', function (greeting) {
            handleWebsocketMessage(greeting.body);
        });
    };

    var errorCallback = (error) => {
        alert(`Socket connection error: ${error?.headers?.message}`);
    }

    stompClient.connect({}, connectionCallback, errorCallback);
}

handleWebsocketMessage = (message) => {
    var json = JSON.parse(message);
    var action = json.actionType;
    var payload = json.payload;

    if (action === CLEAR_MAP) {
        handleClearMapAction(payload);
    } else if (action === ADD_POINT_LIST_AND_FIT_BOUND) {
        handleAddPointsAndFitBound(payload);
    } else if (action === POINT_RELAXED) {
        handlePointRelaxed(payload);
    } else if (action === DRAW_EDGE) {
        handleDrawEdge(payload);
    }
}

handleClearMapAction = (payload) => {
    markersGroup.clearLayers();
    linesGroup.clearLayers();
}

handleAddPointsAndFitBound = (payload) => {
    // var marker = L.marker([payload.latitude, payload.longitude]);
    payload.forEach((payload) => {
        var circle = L.circle([payload.latitude, payload.longitude], 10)
        circle.id = payload.id;
        markersGroup.addLayer(circle);
    });

    map.fitBounds(markersGroup.getBounds());
}

handlePointRelaxed = (payload) => {
    const circle = markersGroup.findById(payload);
    circle.setStyle({color: 'red'});
}

handleDrawEdge = (payload) => {
    const fromPoint = payload.from;
    const toPoint = payload.to;
    const id = `${fromPoint.id}-${toPoint.id}`;

    const polyline = new L.Polyline(
        [
            [fromPoint.latitude, fromPoint.longitude],
            [toPoint.latitude, toPoint.longitude]
        ], 
        {
            color: 'red',
            weight: 1,
            smoothFactor: 1,
            // dashArray: '5, 5', 
            // dashOffset: '0',
            opacity: 0.5
        }
    );

    polyline.id = id;

    linesGroup.addLayer(polyline);
}