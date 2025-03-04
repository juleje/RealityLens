function addNewAttribute(name, value, node) {
    const attribute = document.createAttribute(name);
    attribute.value = value;
    node.setAttributeNode(attribute);
}

function createPlaneAr(data, scene){
    //Base plane
    const plane = document.createElement("a-plane");
    addNewAttribute("gps-entity-place", 'latitude: ' + data.location.latitude + '; longitude: ' + data.location.longitude + ';', plane);
    addNewAttribute("color", '#AFD0AB', plane);
    addNewAttribute("height", '3', plane);
    addNewAttribute("width", '4', plane);
    addNewAttribute("look-at", '[gps-camera]', plane);
    plane.classList.add("checkpointid-"+data.id)
    const navclick = document.createAttribute("navclick");
    plane.setAttributeNode(navclick);
    scene.appendChild(plane);

    const aPlaneShadow = document.createElement("a-plane");
    addNewAttribute("color", '#000000', aPlaneShadow);
    addNewAttribute("height", '3', aPlaneShadow);
    addNewAttribute("width", '4', aPlaneShadow);
    addNewAttribute("position", '0.5 -0.5 -2', aPlaneShadow);
    addNewAttribute("opacity", "0.4", aPlaneShadow);
    plane.classList.add("checkpointid-"+data.id)
    plane.appendChild(aPlaneShadow);

    //Title
    const title = document.createElement("a-text");
    addNewAttribute("scale", '2 2 2', title);
    addNewAttribute("position", '0 0 0.3', title);
    addNewAttribute("align",'center',title)
    addNewAttribute("height",'3',title)
    addNewAttribute("width",'4',title)
    addNewAttribute("color", 'black', title);
    addNewAttribute("value", data.name, title);
    title.classList.add("checkpointid-"+data.id)
    plane.appendChild(title)

    //Description plane
    const descriptionPlane = document.createElement("a-plane");
    addNewAttribute("color", '#AFD0AB', descriptionPlane);
    addNewAttribute("height", '3', descriptionPlane);
    addNewAttribute("width", '4', descriptionPlane);
    addNewAttribute("position", '0 -3 0', descriptionPlane);
    descriptionPlane.classList.add("checkpointid-"+data.id)
    plane.appendChild(descriptionPlane)

    const aDescriptionPlaneShadow = document.createElement("a-plane");
    addNewAttribute("color", '#000000', aDescriptionPlaneShadow);
    addNewAttribute("height", '3', aDescriptionPlaneShadow);
    addNewAttribute("width", '4', aDescriptionPlaneShadow);
    addNewAttribute("position", '0.5 -3.5 -2', aDescriptionPlaneShadow);
    addNewAttribute("opacity", "0.4", aDescriptionPlaneShadow);
    aDescriptionPlaneShadow.classList.add("checkpointid-"+data.id)
    plane.appendChild(aDescriptionPlaneShadow);

    //Description Text
    const description = document.createElement("a-text");
    addNewAttribute("position", '-1.8 2 0.3', description);
    addNewAttribute("scale", '0.7 0.7 0.7', description);
    addNewAttribute("wrap-count",'23',description)//default 40
    addNewAttribute("align",'left',description)//default left
    addNewAttribute("anchor",'left',description)//horizontal positoning default center
    addNewAttribute("baseline",'top',description)//vertical positoning default center
    addNewAttribute("height",'3',title)
    addNewAttribute("width",'4',title)
    addNewAttribute("color", 'black', description);
    addNewAttribute("value", data.shortDescription, description);
    description.classList.add("checkpointid-"+data.id)
    descriptionPlane.appendChild(description)

    //CallToAction plane
    const ctaPlane = document.createElement("a-plane");
    addNewAttribute("color", '#AFD0AB', ctaPlane);
    addNewAttribute("height", '1', ctaPlane);
    addNewAttribute("width", '4', ctaPlane);
    addNewAttribute("position", '0 -4.5 0', ctaPlane);
    ctaPlane.classList.add("checkpointid-"+data.id)
    plane.appendChild(ctaPlane)

    const aCtaPlaneShadow = document.createElement("a-plane");
    addNewAttribute("color", '#000000', aCtaPlaneShadow);
    addNewAttribute("height", '1.3', aCtaPlaneShadow);
    addNewAttribute("width", '4', aCtaPlaneShadow);
    addNewAttribute("position", '0.5 -5.30 -2', aCtaPlaneShadow);
    addNewAttribute("opacity", "0.4", aCtaPlaneShadow);
    aCtaPlaneShadow.classList.add("checkpointid-"+data.id)
    plane.appendChild(aCtaPlaneShadow);

    //CTA Text
    const cta = document.createElement("a-text");
    addNewAttribute("position", '-1.6 0 0.3', cta);
    addNewAttribute("color", 'black', cta);
    addNewAttribute("value", "Klik op het paneel voor meer info", cta);
    cta.classList.add("checkpointid-"+data.id)
    ctaPlane.appendChild(cta)
}

function create3DAr(data, scene){
    const entity = document.createElement("a-entity");
    addNewAttribute("gps-entity-place", 'latitude: ' + data.location.latitude + '; longitude: ' + data.location.longitude + ';', entity);
    addNewAttribute("scale", data.scale+' '+data.scale+' '+data.scale , entity);
    addNewAttribute("gltf-model", 'url('+data.gltfPath+')', entity);
  //  addNewAttribute("look-at", '[gps-camera]', entity);
    addNewAttribute("position", '0 -0.01 0', entity);//y=-0.01
    scene.appendChild(entity);
   // <a-entity gltf-model="url(/path/to/tree.gltf)"></a-entity>

}

function handleCheckpoints(data) {
    let scene = document.querySelector('a-scene');

    console.log(data)
    if (scene != null) {
        for (let i = 0; i < data.length; i++) {

            if(data[i].objectName==null){
                createPlaneAr(data[i],scene)
            }else{
                create3DAr(data[i],scene)
            }
        }
    }
}

function fetchCheckpoints(allIds) {
    fetch('/api/checkpoints/getAll/' + allIds)
        .then(response => response.json())
        .then(d => {
            handleCheckpoints(d)
        })
        .catch(err => {
            console.log(err)
        });
}//todo eror  in screen

window.addEventListener('load', (event) => {
    const goBackArrowInArBtn = document.getElementById("goBackArrowInAr");
    if (goBackArrowInArBtn != null) {
        goBackArrowInArBtn.addEventListener("click", function () {
            clickedRedirect();
        })
    }
});

function clickedRedirect() {
    const firstIdP = document.getElementById("firstId");
    if (firstIdP != null) {
        const firstId = parseInt(firstIdP.innerHTML);
        fetch('/api/checkpoints/' + firstId + '/project/getName')
            .then(response => response.text())
            .then(data => window.location.href = "/user/" + data + "/checkpoints")
    }
}


/*

                        '<a-plane gps-entity-place="latitude: ' + markerLatLng.lat + '; longitude: ' + markerLatLng.lng + ';" color="#f9f9f9" height="3" width="4">'+
                             '<a-text look-at="[gps-camera]" scale="2 2 2" position="0 0 1" color="black" value="'+data[i].name+'"></a-text>' +
                             '<a-plane height="3" width="4" color="#f9f9f9" position="0 -3 0">'+
                                 '<a-text  position="0 0 1" color="black" value="'+data[i].description+'"></a-text>'+
                             '</a-plane>'+
                             '<a-plane height="3" width="4" position="0 -6 0">'+
                                 '<a-text  position="0 0 1" color="black" value="click me" ></a-text>'+
                             '</a-plane>'+
                         '</a-plane>'*/
