var map;
var markersGroup;

const CLEAR_MAP = "clear-map";
const ADD_POINT_LIST_AND_FIT_BOUND = "add-point-list-and-fit-bound";
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

    markersGroup = L.featureGroup();

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
    }
}

function handleAddPointsAndFitBound(payload) {
    // var marker = L.marker([payload.latitude, payload.longitude]);
    payload.forEach((payload) => {
        var circle = L.circle([payload.latitude, payload.longitude], 10)
        markersGroup.addLayer(circle);
    });

    map.fitBounds(markersGroup.getBounds());
}

function handleClearMapAction(payload) {
    markersGroup.clearLayers();
}