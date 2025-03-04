let map;
/*---------------------------
-----CHECKPOINTS OVERVIEW----
---------------------------*/

function initMapCheckpoints() {
    const centerLatlng = { lat: 51.201018, lng: 4.433110 };
    map = initGeneralMap(centerLatlng, 11, "map");

    fetch('/api/checkpoints/all')
        .then(response => response.json())
        .then(data => addMarkers(data));
}

function addMarkers(data) {
    for (let i = 0; i < data.length; i++) {
        const markerLatLng = {lat: data[i].location.latitude, lng: data[i].location.longitude}
        const marker = new google.maps.Marker({
            position: markerLatLng,
            map,
            description: data[i].description,
            title: data[i].name,
        });

        google.maps.event.addListener(marker, 'click', function() {
            clearRadius();
            let circle = new google.maps.Circle({
                map: map,
                radius: data[i].radius,
                fillColor: '#eb877f'
            });
            circle.bindTo('center', marker, 'position');
            activeRadius = circle;

            selectMarker(data[i])
        });
    }
}

function selectMarker(data) {
    const checkpoints = document.getElementById("checkpoints_map");
    checkpoints.getElementsByClassName("markerTitle")[0].innerText = data.name
    checkpoints.getElementsByClassName("markerDescription")[0].innerHTML = data.description
    const markerActions = document.getElementById("markerActions");
    if (markerActions != null) {
        markerActions.classList.remove("hide");
    }

    const detailCheckpointLink = document.getElementById("detailCheckpointLink");
    const editCheckpointLink = document.getElementById("editCheckpointLink");
    const deleteCheckpointLink = document.getElementById("deleteCheckpointLink");
    if (editCheckpointLink != null && deleteCheckpointLink != null) {
        const hrefPartsDetail = detailCheckpointLink.href.split("/");
        const hrefPartsEdit = editCheckpointLink.href.split("/");
        const hrefPartsDelete = deleteCheckpointLink.href.split("/");

        hrefPartsDetail[hrefPartsDetail.length - 1] = data.id
        hrefPartsEdit[hrefPartsEdit.length - 1] = data.id
        hrefPartsDelete[hrefPartsDelete.length - 1] = data.id

        detailCheckpointLink.href = hrefPartsDetail.join("/");
        editCheckpointLink.href = hrefPartsEdit.join("/");
        deleteCheckpointLink.href = hrefPartsDelete.join("/");
    }
}

/*---------------------------
---DETAIL CHECKPOINT---------
---------------------------*/
function initMapCheckpointDetail() {
    const checkpointDetailIdField = document.getElementById("checkpointDetailId");
    if (checkpointDetailIdField != null) {
        const checkpointId = parseInt(checkpointDetailIdField.innerHTML);
        fetch('/api/checkpoints/get/' + checkpointId)
            .then(response => response.json())
            .then(data => showMarkerDetail(data));
    }
}

function showMarkerDetail(data) {
    const centerLatlng = {lat: data.location.latitude, lng: data.location.longitude};
    map = initGeneralMap(centerLatlng, 17, "map");

    const marker = new google.maps.Marker({
        position: centerLatlng,
        map,
        title: "Setup locatie"
    });

    let circle = new google.maps.Circle({
        map: map,
        radius: data.radius,
        fillColor: '#eb877f'
    });
    circle.bindTo('center', marker, 'position');

    const markerDetails = document.getElementById("map_aside");
    if (markerDetails != null) {
        const markerTitle = markerDetails.getElementsByClassName("markerTitle")[0];
        const markerDescription = markerDetails.getElementsByClassName("markerDescription")[0];

        markerTitle.innerHTML = data.name;
        markerDescription.innerHTML = data.description;
    }
}

/*---------------------------
---ADD/EDIT CHECKPOINTS------
---------------------------*/
let activemarker;
let activeRadius;
let radiusInput = document.getElementById("radius");

function initMapAddCheckpoint() {
    const centerLatlng = { lat: 51.201018, lng: 4.433110 };
    map = initGeneralMap(centerLatlng, 11, "map");
    addEditMaps(map);
}

function initMapEditCheckpoint() {
    const editCheckpointIdField = document.getElementById("editCheckpointId");
    if (editCheckpointIdField !== null) {
        const checkpointId = parseInt(editCheckpointIdField.innerHTML);

        fetch('/api/checkpoints/get/' + checkpointId)
            .then(response => response.json())
            .then(data => {
                const centerLatlng = {lat: data.location.latitude, lng: data.location.longitude};
                map = initGeneralMap(centerLatlng, 17, "map");

                const marker = new google.maps.Marker({
                    position:centerLatlng,
                    map,
                    title:data.name
                })
                activemarker = marker;

                addEditMaps(map);
            });
    }
}

function addEditMaps (map) {
    map.addListener("click", (mapsMouseEvent) => {
        clearMarker();

        let newMarker = new google.maps.Marker({
            position: mapsMouseEvent.latLng,
            map,
            title: "Setup locatie"
        });

        activemarker = newMarker;
        let lngJson = mapsMouseEvent.latLng.toJSON().lng.toString();
        document.getElementById("locationLongitude").value = lngJson.substr(0, lngJson.indexOf(".")+7)

        let latJson = mapsMouseEvent.latLng.toJSON().lat.toString();
        document.getElementById("locationLatitude").value = latJson.substr(0, latJson.indexOf(".")+7);
        setRadius()

    })
}

function setRadius() {
    clearRadius();
    let circle = new google.maps.Circle({
        map: map,
        radius: 20,
        fillColor: '#eb877f'
    });
    circle.bindTo('center', activemarker, 'position');
    activeRadius = circle;
}

function clearMarker() {
    if (activemarker != null) {
        activemarker.setMap(null);
    }
}
function clearRadius() {
    if (activeRadius != null) {
        activeRadius.setMap(null);
    }
}

if (radiusInput != null) {
    radiusInput.addEventListener("change", (event) => {
        setRadius()
    })
}