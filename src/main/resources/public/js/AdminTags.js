const checkpointTags = document.getElementById("checkpointTags");
if (checkpointTags !== null) {
    const checkpointIdField = document.getElementById("checkpointId");
    const checkpointId = parseInt(checkpointIdField.innerHTML);

    const activeTags = document.getElementById("activeTags");
    const nonActiveTags = document.getElementById("nonActiveTags");

    const tags1 = activeTags.getElementsByClassName("tag");
    const tags2 = nonActiveTags.getElementsByClassName("tag");


    for (let i = 0; i < tags1.length; i++) {
        const arrowRemove = tags1[i].querySelector(".tagAction");
        arrowRemove.addEventListener("click", function () {
            const classnames = arrowRemove.classList;
            for (let j = 0; j < classnames.length; j++) {
                if (classnames[j].startsWith("tagId")) {
                    addRemoveTag(checkpointId, classnames[j].substring(5));
                }
            }
        });
    }

    for (let i = 0; i < tags2.length; i++) {
        const arrowAdd = tags2[i].querySelector(".tagAction");
        arrowAdd.addEventListener("click", function () {
            const classnames = arrowAdd.classList;
            for (let j = 0; j < classnames.length; j++) {
                if (classnames[j].startsWith("tagId")) {
                    addRemoveTag(checkpointId, classnames[j].substring(5));
                }
            }
        });
    }
}

async function addRemoveTag(checkpointId, tagId) {
    await fetch("/api/checkpoints/" + checkpointId + "/tags/" + tagId);
    window.location.reload();
}
