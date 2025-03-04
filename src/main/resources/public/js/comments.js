window.onload = function () {
    const urlParts = window.location.href.split("comments/");
    const params = urlParts[1].split(("/"))[0];
    const checkpointId = urlParts[1].split(("/"))[1];
    console.log(checkpointId);
    let closeComments = document.getElementById("closeComments");

    closeComments.addEventListener("click" , redirectAfterLike)

    function redirectAfterLike() {
        window.location.href = "/user/ar/" +params + "?activeCheckId="+checkpointId;

    }
}