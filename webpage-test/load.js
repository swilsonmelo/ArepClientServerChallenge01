const url = "https://frozen-bastion-85383.herokuapp.com/index.html";

function load_index() {

    $(document).ready(function () {

        $.get(url, function (data, status) {
            document.getElementById("content").innerHTML = data;
        });
    });

}
