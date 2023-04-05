var map;
var markersGroup;

const CLEAR_MAP = "clear-map";
const ADD_POINT_LIST_AND_FIT_BOUND = "add-point-list-and-fit-bound";
const POINT_RELAXED = "point-relaxed";

const ADD_POLYLINE = "add-polyline";

function callPingApi() {
    const xhttp = new XMLHttpRequest();
    xhttp.onload = function() {
        if (this.readyState == 4 && this.status == 200) {
            handlePingApiResponse(this.responseText);
        }
    };

    const url = 'http://localhost:8080/api/ping';
    xhttp.open("GET", url, true);
    xhttp.send();
}

function callUploadCsvApi() {
    const xhttp = new XMLHttpRequest();
    xhttp.onload = function() {
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

function handlePingApiResponse(responseText) {
    console.log(responseText);
}

function handleUploadCsvApiResponse(responseText) {
    console.log(responseText);
}

function init() {
    initMap();

    document.getElementById('upload-button').addEventListener('click', uploadButtonClicked);

    setupWebsocketConnection();
}

function initMap() {
    map = L.map('map').setView([51.505, -0.09], 13);

    L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 19,
        attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
    }).addTo(map);

    // markersGroup = L.featureGroup.include({
    //     findById : function(id) {
    //         for (var i in this._layers) {
    //             if (this._layers[i].id === id) {
    //                return this._layers[i];
    //             }
    //         }
    //     }
    // });

    markersGroup = L.featureGroup();

    markersGroup.findById = function(id) {
        for (var layer of this.getLayers()) {
            if (layer.id === id) {
                return layer;
            }
        }
    }

    map.addLayer(markersGroup);
}

function uploadButtonClicked() {
    // callPingApi();
    callUploadCsvApi();
}

function setupWebsocketConnection() {
    console.log('setting up web socket connection');

    const url = 'ws://localhost:8080/tsp-websocket';
    
    var stompClient = Stomp.client(url);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/graph-action', function (greeting) {
            handleWebsocketMessage(greeting.body);
        });
    });
}

function handleWebsocketMessage(message) {
    var json = JSON.parse(message);
    var action = json.actionType;
    var payload = json.payload;

    if (action === CLEAR_MAP) {
        handleClearMapAction(payload);
    } else if (action === ADD_POINT_LIST_AND_FIT_BOUND) {
        handleAddPointsAndFitBound(payload);
    } else if (action === POINT_RELAXED) {
        handlePointRelaxed(payload);
    }
}

function handleClearMapAction(payload) {
    markersGroup.clearLayers();
}

function handleAddPointsAndFitBound(payload) {
    // var marker = L.marker([payload.latitude, payload.longitude]);
    payload.forEach((payload) => {
        var circle = L.circle([payload.latitude, payload.longitude], 10)
        circle.id = payload.id;
        markersGroup.addLayer(circle);
    });

    map.fitBounds(markersGroup.getBounds());
}

function handlePointRelaxed(payload) {
    const circle = markersGroup.findById(payload);
    circle.setStyle({color: 'red'});
}