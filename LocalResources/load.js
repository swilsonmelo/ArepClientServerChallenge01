

const url = "https://clientserver01.herokuapp.com//index.html";

function loadIndexFromHeroku() {
    $(document).ready(function () {
        $.get(url, function (data, status) {
            document.getElementById("content").innerHTML = data;
        });
    });
}
