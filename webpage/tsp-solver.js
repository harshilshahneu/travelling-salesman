var map;
var markersGroup;
var linesGroup;

const CLEAR_MAP = "clear-map";
const ADD_POINT_LIST_AND_FIT_BOUND = "add-point-list-and-fit-bound";
const ADD_MST_POLYLINE_AND_FIT_BOUND = "add-mst-polyline-and-fit-bound";
const CLEAR_MST_POLYLINE = "clear-mst-polyline";
const ADD_POLYLINE_AND_FIT_BOUND = "add-polyline-and-fit-bound";
const POINT_RELAXED = "point-relaxed";
const DRAW_EDGE = "draw-edge"
const CHANGE_POINT_COLOR_RED = "change-point-color-red";
const CHANGE_POINT_COLOR_GREEN = "change-point-color-green";
const DRAW_EDGE_COLOR_GREEN = "draw-edge-color-green";

// const Z_INDEX_MARKER = 9000;
// const Z_INDEX_POINTS = 8000;
// const Z_INDEX_MST_POLYLINE = 7000;
// const Z_INDEX_FINAL_TOUR_POLYLINE = 6000;

let tspPayload;

init = () => {
    initTSPPayload();
    initMap();

    document.getElementById('upload-button').addEventListener('click', uploadButtonClicked);

    setupWebsocketConnection();
    
    callTSPServiceAPI();
    // setTimeout(callTSPServiceAPI, 2000);
}

closeModal = () => {
    const modal = document.getElementById("json-modal");
    $(modal).modal("hide");
}

updateTSPPayload = () => {
    /* 
    It's possible for user to enter an invalid json 
    so need to handle unexpected behaviour 
    with try catch
    */
    try {
        const textArea = document.getElementById("json-textarea");
        const jsonString = textArea.value;
        const json = JSON.parse(jsonString);
        tspPayload = json;
        console.log('updated tspPayload', tspPayload);    
    } catch (e) {
        console.error('error occured', e);
        alert('Something went wrong');
        restTSPPayload();
    }

    closeModal();
}

restTSPPayload = () => {
    const jsonString = JSON.stringify(tspPayload, null, 4);
    const textArea = document.getElementById("json-textarea");
    textArea.value = jsonString;
}

initTSPPayload = () => {
    tspPayload = {
        "twoOptPayload": {
            "strategy": 1,
            "budget": 10000
        },
        "threeOptPayload": {
            "strategy": 1,
            "budget": 10000
        },
        "simulatedAnnealingPayload": {
            "maxIteration": 1000000,
            "startingTemperature": 1000,
            "finalTemperature": 1,
            "coolingRate": 0.9995
        },
        "antColonyOptimazationPayload": {
            "numberOfAnts": 10,
            "phermoneExponent": 1.0,
            "heuristicExponent": 2.0,
            "phermoneEvaporationRate": 0.1,
            "phermoneDepositFactor": 1.0,
            "numberOfIterations": 20,
            "maxImprovementIterations": 1000
        }
    };

    const jsonString = JSON.stringify(tspPayload, null, 4);
    const textArea = document.getElementById("json-textarea");
    textArea.value = jsonString;
}

initMap = () => {
    map = L.map('map').setView([51.505, -0.09], 13);

    map.on('click', (e) => {
        const latitude = e.latlng.lat.toFixed(6);
        const longitude = e.latlng.lng.toFixed(6);
        
        L.popup()
            .setLatLng(e.latlng)
            .setContent(`${latitude} ${longitude}`)
            .openOn(map);
    });

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

callPingApi = () => {
    const xhttp = new XMLHttpRequest();
    xhttp.onload = () => {
        if (xhttp.readyState == 4 && xhttp.status == 200) {
            handlePingApiResponse(xhttp.responseText);
        }
    };

    const url = 'http://localhost:8080/api/ping';
    xhttp.open("GET", url, true);
    xhttp.send();
}

uploadButtonClicked = () => {
    // callPingApi();
    callUploadCsvApi();
}

callUploadCsvApi = () => {
    const xhttp = new XMLHttpRequest();
    xhttp.onload = () => {
        if (xhttp.readyState == 4 && xhttp.status == 200) {
            handleUploadCsvApiResponse(xhttp);
        }
    };

    var fileInput = document.getElementById('formFile');
    
    const tspPayloadBlob = new Blob(
        [JSON.stringify(tspPayload)],
        { type: "application/json" }
    );
    
    var formData = new FormData();
    formData.append('multiPartFile', fileInput.files[0]);
    formData.append('tspPayload', tspPayloadBlob);

    const serviceType = document.getElementById('algorithm-select').value;

    const url = `http://localhost:8080/api/csv/${serviceType}`;
    xhttp.open("POST", url, true);
    xhttp.send(formData);
}

callTSPServiceAPI = () => {
    console.log('Getting all the TSP Services');

    const xhttp = new XMLHttpRequest();
    xhttp.onload = () => {
        console.log('readyState', xhttp.readyState, 'status', xhttp.status);
        if (xhttp.readyState == 4 && xhttp.status == 200) {
            handleTSPServiceResponse(xhttp.response);
        }
    };

    const url = 'http://localhost:8080/api/tsp/services';
    xhttp.open("GET", url, true);
    xhttp.send();
}

handlePingApiResponse = (responseText) => {
    console.log('Ping API responseText', responseText);
    console.log(responseText);
}

handleUploadCsvApiResponse = (xhttp) => {
    console.log('CSV API response xhttp', xhttp);

    const filename = 'tsp-solution-result.csv';
    var disposition = xhttp.getResponseHeader('Content-Disposition');
    if (disposition && disposition.indexOf('attachment') !== -1) {
        var filenameRegex = /filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/;
        var matches = filenameRegex.exec(disposition);
        if (matches != null && matches[1]) {
            filename = matches[1].replace(/['"]/g, '');
        }
    }

    var blob = new Blob([xhttp.response], { type: 'application/octet-stream' });
    saveAs(blob, filename);
}

handleTSPServiceResponse = (responseText) => {
    console.log('tsp services responseText', responseText);

    const response = JSON.parse(responseText);

    const selectElement = document.getElementById('algorithm-select');
    
    response.forEach((item) => {
        const optionElement = document.createElement('option');
        optionElement.value = item.identifier;
        optionElement.text = item.name;

        selectElement.appendChild(optionElement);
    });
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
    } else if (action === ADD_MST_POLYLINE_AND_FIT_BOUND) {
        handleAddMSTPoylineAndFitBound(payload);
    } else if (action === CLEAR_MST_POLYLINE) {
        handleClearMSTPolyline(payload);
    } else if (action === ADD_POLYLINE_AND_FIT_BOUND) {
        handleAddPolylineAndFitBound(payload);
    } else if (action === POINT_RELAXED) {
        handlePointRelaxed(payload);
    } else if (action === DRAW_EDGE) {
        handleDrawEdge(payload);
    } else if (action === CHANGE_POINT_COLOR_RED) {
        handleChangePointColorRed(payload);
    } else if (action === CHANGE_POINT_COLOR_GREEN) {
        handleChangePointColorGreen(payload);
    } else if (action === DRAW_EDGE_COLOR_GREEN) {
        handleDrawEdgeColorGreen(payload);
    }
}

handleClearMapAction = (payload) => {
    markersGroup.clearLayers();
    linesGroup.clearLayers();
}

handleAddPointsAndFitBound = (payload) => {
    // var marker = L.marker([payload.latitude, payload.longitude]);
    payload.forEach((payload) => {
        const circle = L.circle([payload.latitude, payload.longitude], 10)
        circle.id = payload.id;
        // circle.setZIndexOffset(Z_INDEX_POINTS);

        markersGroup.addLayer(circle);
    });

    map.fitBounds(markersGroup.getBounds());
}

handleAddMSTPoylineAndFitBound = (payload) => {
    payload.forEach((edge) => {
        const fromPoint = edge.from;
        const toPoint = edge.to;
        const id = `${fromPoint.id}-${toPoint.id}`;
    
        const polyline = new L.Polyline(
            [
                [fromPoint.latitude, fromPoint.longitude],
                [toPoint.latitude, toPoint.longitude]
            ], 
            {
                color: 'red',
                weight: 3,
                smoothFactor: 1,
                // dashArray: '5, 5', 
                // dashOffset: '0',
                // opacity: 0.5
            }
        );
    
        polyline.id = id;
        // polyline.setZIndex(Z_INDEX_MST_POLYLINE);

        linesGroup.addLayer(polyline);
    });

    map.fitBounds(linesGroup.getBounds());
}

handleClearMSTPolyline = (payload) => {
    linesGroup.clearLayers();
}

handleAddPolylineAndFitBound = (payload) => {
    const points = payload.map(item => [item.latitude, item.longitude]);

    const polylineOptions = {
        color: 'black',
        weight: 3,
        smoothFactor: 1,
        // dashArray: '5, 5', 
        // dashOffset: '0',
        // opacity: 0.5
    };

    const polyline = new L.Polyline(points, polylineOptions);
    polyline.id = 'final-tour';
    // polyline.setZIndex(Z_INDEX_FINAL_TOUR_POLYLINE);

    linesGroup.addLayer(polyline);

    map.fitBounds(linesGroup.getBounds());
}

handlePointRelaxed = (payload) => {
    const circle = markersGroup.findById(payload);
    circle.setStyle({color: 'black'});
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
            color: 'black',
            weight: 3,
            smoothFactor: 1,
            // dashArray: '5, 5', 
            // dashOffset: '0',
            // opacity: 0.5
        }
    );

    polyline.id = id;

    linesGroup.addLayer(polyline);
}

handleChangePointColorRed = (payload) => {
    const circle = markersGroup.findById(payload);
    circle.setStyle({color: 'red'});
}

handleChangePointColorGreen = (payload) => {
    const circle = markersGroup.findById(payload);
    circle.setStyle({color: 'green'});
}

handleDrawEdgeColorGreen = (payload) => {
    const fromPoint = payload.from;
    const toPoint = payload.to;
    const id = `${fromPoint.id}-${toPoint.id}`;

    const polyline = new L.Polyline(
        [
            [fromPoint.latitude, fromPoint.longitude],
            [toPoint.latitude, toPoint.longitude]
        ], 
        {
            color: 'green',
            weight: 3,
            smoothFactor: 1,
            // dashArray: '5, 5', 
            // dashOffset: '0',
            // opacity: 0.5
        }
    );

    polyline.id = id;

    linesGroup.addLayer(polyline);
}
