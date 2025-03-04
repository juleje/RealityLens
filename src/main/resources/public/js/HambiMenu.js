const hambiIcon = document.getElementById("hambiIcon");
if (hambiIcon != null) {
    const actions = document.getElementById("actions");
    const filterBtn = actions.querySelector("#openFilter");
    const lookCheckBtn = actions.querySelector("#btn-checkpoints");

    function openHambiMenu() {
        filterBtn.classList.add("left_top");
        lookCheckBtn.classList.add("right_top");
    }
    function closeHambiMenu() {
        filterBtn.classList.remove("left_top");
        lookCheckBtn.classList.remove("right_top");
    }

    hambiIcon.onclick = function () {
        if (filterBtn.classList.contains("left_top")) {
            closeHambiMenu();
        } else {
            openHambiMenu();
        }
    }
}