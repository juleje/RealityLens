function initGeneralMap(centerPoint, zoom, map_id) {
    const stylesArray = new google.maps.StyledMapType([
            {
                featureType: "landscape",
                elementType: "geometry",
                stylers: [
                    {color: "#85B8A2"}
                ]
            }, {
                featureType: "poi",
                elementType: "labels",
                stylers: [
                    {visibility: "off"}
                ]
            },
            {
                featureType: "transit",
                elementType: "labels",
                stylers: [
                    {visibility: "off"}
                ]
            }, {
                featureType: "poi.park",
                elementType: "labels",
                stylers: [
                    {visibility: "on"}
                ]
            },
            {
                featureType: "poi.government",
                elementType: "labels",
                stylers: [
                    {visibility: "on"}
                ]
            },
            {
                featureType: "poi.park",
                elementType: "geometry",
                stylers: [
                    {color: "#AED4AE"}
                ]
            }, {
                featureType: "road.highway",
                elementType: "geometry.fill",
                stylers: [
                    {color: "#4C8084"}
                ]
            }
            , {
                featureType: "road.highway",
                elementType: "geometry.stroke",
                stylers: [
                    {color: "#2F4858"}
                ]
            }
        ],
        {name: "Kaart"}
    );

    map = new google.maps.Map(document.getElementById(map_id), {
        zoom: zoom,
        center: centerPoint,
        mapTypeControlOptions: {
            mapTypeIds: ["styled_map", "satellite"],
        },
    });
    map.mapTypes.set("styled_map", stylesArray);
    map.setMapTypeId("styled_map");

    return map;
}