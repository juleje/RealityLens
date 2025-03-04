const RADIUS_USER = 50;
const filterIds = [];
let alreadyInRange = false;

window.addEventListener('load', (event) => {
    initFilterEventListener();
});

function initFilterEventListener () {
    const activeFilterField = document.getElementById("activeFilter");
    const availableFilterField = document.getElementById("availableFilter");
    if (activeFilterField != null && availableFilterField != null) {
        //ADD TAG
        const availableFilters = availableFilterField.getElementsByClassName("tag");
        for (let i = 0; i < availableFilters.length; i++) {
            availableFilters[i].onclick = function () {
                const id = availableFilters[i].id.split("-")[1];
                filterIds.push(id);
                const tagNode = availableFilters[i];
                availableFilterField.removeChild(availableFilters[i]);

                const activeTagNode = document.createElement("div");
                activeTagNode.classList.add("tag");
                activeTagNode.id = "tag-" + id;
                const firstP = document.createElement("p");
                firstP.classList.add("tagName");
                firstP.innerHTML = tagNode.children[0].innerHTML;
                const secondP = document.createElement("p");
                secondP.classList.add("tagRemove");
                secondP.innerHTML = "X";
                activeTagNode.append(firstP, secondP);
                activeFilterField.appendChild(activeTagNode);

                initMapUser();
                initFilterEventListener();
            }
        }

        //REMOVE TAG
        const activeTags = activeFilterField.getElementsByClassName("tag");
        for (let i = 0; i < activeTags.length; i++) {
            activeTags[i].onclick = function () {
                const id = activeTags[i].id.split("-")[1];
                filterIds.splice(filterIds.indexOf(id), 1);
                const tagNode = activeTags[i];
                activeFilterField.removeChild(activeTags[i]);

                const availableTagNode = document.createElement("div");
                availableTagNode.classList.add("tag");
                availableTagNode.id = "tag-" + id;
                const pElement = document.createElement("p");
                pElement.classList.add("tagName");
                pElement.innerHTML = tagNode.children[0].innerHTML;
                availableTagNode.appendChild(pElement);
                availableFilterField.appendChild(availableTagNode);

                const listItems = [];
                for (let j = 0; j < availableFilters.length; j++) {
                    listItems.push(availableFilters[j]);
                }
                listItems.sort(function (a, b) {
                    const compA = a.getAttribute("id").toUpperCase();
                    const compB = b.getAttribute("id").toUpperCase();
                    return (compA < compB) ? -1 : (compA > compB) ? 1 : 0;
                })
                availableFilterField.innerHTML = '';
                for (let j = 0; j < listItems.length; j++) {
                    availableFilterField.appendChild(listItems[j]);
                }
                initMapUser();
                initFilterEventListener();
            }
        }

        const filterOpenBtn = document.getElementById("openFilter");
        filterOpenBtn.onclick = function () {
            const filter_user_checkpoints_field = document.getElementById("filter_user_checkpoints");
            filter_user_checkpoints_field.classList.remove("d-none");
        }

        const filterCloseBtn = document.getElementById("filterClose");
        filterCloseBtn.onclick = function () {
            const filter_user_checkpoints_field = document.getElementById("filter_user_checkpoints");
            filter_user_checkpoints_field.classList.add("d-none");
        }

    }
}


function initMapUser () {
    const projectNameField = document.getElementById("projectName");
    let projectName = "";
    if (projectNameField != null) {
        projectName = projectNameField.innerHTML;
    }

    const centerLatlng = { lat: 51.182107, lng: 4.422892 };
    map = initGeneralMap(centerLatlng, 20, "map_user");

    if (projectName !== "") {
        let url = "";
        if (filterIds.length <= 0) {
            url = '/api/checkpoints/byName/' + projectName + '/all'
        } else {
            url = '/api/checkpoints/byName/' + projectName + '/filter/' + filterIds.join(",");
        }

        fetch(url)
            .then(response => response.json())
            .then(data => {
                for (let i = 0; i < data.length; i++) {
                    const markerLatLng = {lat: data[i].location.latitude, lng: data[i].location.longitude}
                    const marker = new google.maps.Marker({
                        position: markerLatLng,
                        map,
                        description: data[i].description,
                        title: data[i].name,
                    });
                }
            });
    }

    if ('geolocation' in navigator) {
        const myPositionMarker = new google.maps.Marker({
            map,
            icon: '../../img/currentLocationDot.png'});

        const myPositionCircle = new google.maps.Circle({
            map: map,
            radius: RADIUS_USER,
            fillColor: '#8db3ef'
        });
        navigator.geolocation.getCurrentPosition(
            position => {
                const myPosition = { lat: position.coords.latitude, lng: position.coords.longitude };
                map.setCenter(myPosition);

            },
            err => console.log(`Error (${err.code}): ${err.message}`)
        )

        options = {
            enableHighAccuracy: true,
            timeout: 250,
            maximumAge: 0
        };
        navigator.geolocation.watchPosition(
            position => {
                const myPosition = { lat: position.coords.latitude, lng: position.coords.longitude };
                setMarkerPosition(myPositionMarker, myPositionCircle, position);
                checkCloseCheckpoints(false, myPosition);
            },
            err => {
                console.log(`Error (${err.code}): ${err.message}`)
            }, options);
    } else {
        console.log('Geolocation is not supported by your browser')
    }
}

function setMarkerPosition(marker, circle, position) {
    marker.setPosition(
        new google.maps.LatLng(
            position.coords.latitude,
            position.coords.longitude)
    );
    circle.bindTo('center', marker, 'position');
}

const btn_checkpoints = document.getElementById("btn-checkpoints");
if (btn_checkpoints !== null) {
    btn_checkpoints.addEventListener("click", function () {
        navigator.geolocation.getCurrentPosition(
            position => {
                const myPosition = {lat: position.coords.latitude, lng: position.coords.longitude};
                checkCloseCheckpoints(true, myPosition);
            },
            err => console.log(`Error (${err.code}): ${err.message}`)
        )
    })
}

function checkCloseCheckpoints(getCheckpoints, position) {
    //calculate distance between aal checkpoints
    const projectNameField = document.getElementById("projectName");
    let projectName = "";
    if (projectNameField != null) {
        projectName = projectNameField.innerHTML;
    }

    if (projectName !== "") {
        let url = "";
        if (filterIds.length <= 0) {
            url = '/api/checkpoints/byName/' + projectName + '/all'
        } else {
            url = '/api/checkpoints/byName/' + projectName + '/filter/' + filterIds.join(",");
        }

        fetch(url)
            .then(response => response.json())
            .then(data => getCloseCheckpoints(data, position, getCheckpoints));
    }
}

function getCloseCheckpoints(data, myPosition, getCheckpoints) {
    const closeCheckpoints = [];

    for (let i = 0; i < data.length; i++) {
        const locationLatLng = {lat: data[i].location.latitude, lng: data[i].location.longitude}

        const R = 6371.0710;
        const rlat1 = myPosition.lat * (Math.PI/180);
        const rlat2 = locationLatLng.lat * (Math.PI/180);
        const difflat = rlat2 - rlat1;
        const difflon = (locationLatLng.lng-myPosition.lng) * (Math.PI/180);

        const dKilometer = 2 * R * Math.asin(Math.sqrt(Math.sin(difflat/2)*Math.sin(difflat/2)+Math.cos(rlat1)*Math.cos(rlat2)*Math.sin(difflon/2)*Math.sin(difflon/2)));
        const dMeter = dKilometer * 1000;
        if (dMeter <= RADIUS_USER) {
            data[i].distance = dMeter;
            closeCheckpoints.push(data[i]);
        }
    }

    if (closeCheckpoints.length <= 0) {
        //No checkpoints in range
        if (getCheckpoints) {
            alert("Er zijn geen checkpoints in de buurt. Gelieve je in de correcte radius te begeven.");
        } else {
            alreadyInRange = false;
        }
    } else {
        //Checkpoints in range
        if (getCheckpoints) {
            let allIds = "";
            for (let i = 0; i < closeCheckpoints.length; i++) {
                allIds+=closeCheckpoints[i].id + ",";
            }

            allIds = allIds.substring(0, allIds.length-1);
            console.log(allIds);

            window.location = "/user/ar/" + allIds;
        } else {
            if (!alreadyInRange) {
                openHambiMenu();
                alreadyInRange = true;
            }
        }
    }
}
