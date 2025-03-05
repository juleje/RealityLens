const submenu_checkpoints = document.getElementById("submenu_checkpoints");
if (submenu_checkpoints !== null) {
	const map_view_menu = submenu_checkpoints.getElementsByTagName("button")[0];
	const list_view_menu = submenu_checkpoints.getElementsByTagName("button")[1];

	if (map_view_menu !== null && list_view_menu !== null) {
		map_view_menu.addEventListener("click", function () {
			changeCheckpointsView("submenu_checkpoints_map", "checkpoints_map");
		})
		list_view_menu.addEventListener("click", function () {
			changeCheckpointsView("submenu_checkpoints_list", "checkpoints_list");
		});
	}
}

function changeCheckpointsView(menu_name, area_name) {
	//Menu change item
	const menu_items = submenu_checkpoints.getElementsByTagName("button");
	for (let i = 0; i < menu_items.length; i++) {
		menu_items[i].classList.remove("active");
	}
	const clickedItem = document.getElementById(menu_name);
	clickedItem.classList.add("active");

	//Area change item
	const checkpoints_areas = document.getElementById("checkpoints_areas");
	const area_items = checkpoints_areas.children;
	for (let i = 0; i < area_items.length; i++) {
		area_items[i].classList.add("d-none");
	}
	const area_to_show = document.getElementById(area_name);
	area_to_show.classList.remove("d-none");
}

const checkpoint_list_select = document.getElementById("checkpoint_list_select");
if (checkpoint_list_select !== null) {
	checkpoint_list_select.addEventListener("change", function (event) {
		const table = document.getElementById("checkpoints_table");
		const tableRows = table.getElementsByClassName("table_list_item");
		for (let i = tableRows.length - 1; i >= 0; i--) {
			tableRows[i].remove();
		}

		const selectedValue = checkpoint_list_select.value;
		const table_spacer = document.getElementById("table_spacer");
		if (parseInt(selectedValue) === 0) {
			table_spacer.classList.remove("d-none");
		} else {
			table_spacer.classList.add("d-none");

			fetch('/api/checkpoints/' + selectedValue + '/all')
				.then(response => response.json())
				.then(data => handleCheckpoints(data));
		}
	})
}

function handleCheckpoints(data) {
	for (let i = 0; i < data.length; i++) {
		const checkpointData = data[i];
		fetch('https://maps.googleapis.com/maps/api/geocode/json?latlng=' + checkpointData.location.latitude + ',' + checkpointData.location.longitude + '&key=googlemapsapikey')
			.then(response => response.json())
			.then(function (data) {
				let address = ""
				if (data.results[0] != null) {
					const address = data.results[0].formatted_address;

					const checkpoint = {
						id: checkpointData.id,
						name: checkpointData.name, description: checkpointData.description,
						address: address,
						latitude: checkpointData.location.latitude, longitude: checkpointData.location.longitude
					};
					showCheckpointInTable(checkpoint);
				}
			});
	}
}

function showCheckpointInTable(data) {
	const table = document.getElementById("checkpoints_table");
	const tableBody = table.querySelector("tbody");
	const tableRow = document.createElement("tr");
	tableRow.classList.add("table_list_item");

	const thName = document.createElement("td");
	thName.innerHTML = data.name;
	tableRow.appendChild(thName);

	const thDescription = document.createElement("td");
	thDescription.innerHTML = data.description;
	tableRow.appendChild(thDescription);

	const thAddress = document.createElement("td");
	thAddress.innerHTML = data.address;
	tableRow.appendChild(thAddress);

	const thLatitude = document.createElement("td");
	thLatitude.innerHTML = data.latitude;
	tableRow.appendChild(thLatitude);

	const thLongitude = document.createElement("td");
	thLongitude.innerHTML = data.longitude;
	tableRow.appendChild(thLongitude);

	const thDetail = document.createElement("td");
	const aDetail = document.createElement("a");
	aDetail.href = "/admin/checkpoint/" + data.id;
	const imgDetail = document.createElement("img");
	imgDetail.src = "/img/eye.png";
	imgDetail.alt = "detail";
	aDetail.appendChild(imgDetail);
	thDetail.appendChild(aDetail);
	tableRow.appendChild(thDetail);

	const thEdit = document.createElement("td");
	const aEdit = document.createElement("a");
	aEdit.href = "/admin/editCheckpoint/" + data.id;
	const imgEdit = document.createElement("img");
	imgEdit.src = "/img/editIcon.png";
	imgEdit.alt = "detail";
	aEdit.appendChild(imgEdit);
	thEdit.appendChild(aEdit);
	tableRow.appendChild(thEdit);

	const thDelete = document.createElement("td");
	const aDelete = document.createElement("a");
	aDelete.href = "/admin/deleteCheckpoint/" + data.id;
	const imgDelete = document.createElement("img");
	imgDelete.src = "/img/deleteIcon.png";
	imgDelete.alt = "detail";
	aDelete.appendChild(imgDelete);
	thDelete.appendChild(aDelete);
	tableRow.appendChild(thDelete);

	tableBody.appendChild(tableRow);
}