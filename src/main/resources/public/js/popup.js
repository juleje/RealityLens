function initPopupListeners () {
    let popup = document.getElementById("popup");
    let scene = document.getElementById("scene");
    let close = document.getElementById("close");
    let commentsDiv = document.getElementById("commentsId");
    let likesDiv = document.getElementById("likesIdDiv");
    let haveLiked = document.getElementById("checkIfLiked").innerHTML;
    let loggedIn = document.getElementById("checkIfLoggedIn").innerHTML;
    let imgLiked = document.getElementById("imageLike");
    let checkpointId = document.getElementById("divCheckpointId").innerHTML;

    close.addEventListener("click", () => {
        console.log("sluit popup")
        popup.classList.add("d-none");
    })


    if (loggedIn === "false") {
        likesDiv.addEventListener("click", () => {
            alert("Je moet ingelogd zijn om te kunnen liken");
        })
        commentsDiv.addEventListener("click", () => {
            alert("Je moet ingelogd zijn om te kunnen commenten");
        })
    } else {
        commentsDiv.addEventListener("click", () => {
            const queryParams = new URLSearchParams(window.location.search);
                const urlParts = window.location.href.split("ar/");
               const params = urlParts[1].split(("?"))[0];
                window.location.href = '/user/comments/'+ params + '/' + checkpointId;
        })
        if (haveLiked === "true") {
            likesDiv.addEventListener("click", () => {
                fetch("/api/ar/unlike/" + checkpointId)
                    .then(response => response.json())
                    .then(data => {
                        if (data) {
                            redirectAfterLike(checkpointId);
                        }
                    });

            })
            imgLiked.src = "/img/unlike.png"
        } else {
            likesDiv.addEventListener("click", () => {
                fetch("/api/ar/like/" + checkpointId)
                    .then(response => response.json())
                    .then(data => {
                        if (data) {
                            redirectAfterLike(checkpointId);
                        }
                    });
            })
        }

    }
function redirectAfterLike(checkpointId) {
    const queryParams = new URLSearchParams(window.location.search);
    if (queryParams.has("activeCheckId")) {
        const urlParts = window.location.href.split("=");
        window.location.href = urlParts[0] + '=' + checkpointId;
    } else {
        window.location.href += '?activeCheckId=' + checkpointId;
    }
}

}