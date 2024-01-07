var currentArticle = {
    id: -1,
    nom: "",
    prix: 0.0,
    quantiter: -1,
    image: "",
};

var listArticle = [];  // Correction: changement de 'listeArticle' à 'listArticle'

document.addEventListener("DOMContentLoaded", function () {
    getArticle();

    document.getElementById("updateButton").addEventListener("click", function (event) {
        event.preventDefault();  // Empêche le rechargement de la page
        updateDB();
    });
});

function getArticle() {
    var xhr = new XMLHttpRequest();
    var url = "http://localhost:8080/FormArticle";
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            var reponseData = JSON.parse(xhr.responseText);
            console.log(reponseData);
            tableUpdate(reponseData);
        }
    };
    xhr.open("GET", url, true);
    xhr.send();
}

function tableUpdate(articles) {
    resetTable();
    resetForm();
    listArticle = [];
    var body = document.getElementById("tableBody");
    currentArticle.id = -1;

    articles.forEach((element) => {
        const newRow = body.insertRow();

        newRow.insertCell(0).textContent = element.id;
        newRow.insertCell(1).textContent = element.nom;
        newRow.insertCell(2).textContent = element.prix;
        newRow.insertCell(3).textContent = element.quantiter;
        listArticle.push(element);
        newRow.onclick = function () {
            if (currentArticle.id !== -1) {
                var selectedRows = body.getElementsByClassName("selected");
                selectedRows[0].classList.remove("selected");
            }

            this.classList.add("selected");
            var idSelected = parseInt(this.cells[0].innerHTML);
            currentArticle = listArticle.find((article) => article.id === idSelected);

            document.getElementById("id").value = currentArticle.id;
            document.getElementById("article").value = currentArticle.nom;
            document.getElementById("prixUnitaire").value = currentArticle.prix;
            document.getElementById("quantite").value = currentArticle.quantiter;
            document.getElementById("imageaff").src = "img/" + currentArticle.image;
            console.log("img/" + currentArticle.image);

        };
    });
}

function resetTable() {
    var body = document.getElementById("tableBody");

    for (var i = body.rows.length - 1; i >= 0; i--) {
        body.deleteRow(i);
    }
}

function resetForm() {
    document.getElementById("id").value = "";
    document.getElementById("article").value = "";
    document.getElementById("prixUnitaire").value = 0;
    document.getElementById("quantite").value = 0;
}

function updateCurrentArticleFromForm() {
    currentArticle.prix = document.getElementById("prixUnitaire").value;
    currentArticle.quantiter = document.getElementById("quantite").value;
}

function updateDB() {
    var xhr = new XMLHttpRequest();
    var url = "http://localhost:8080/FormArticle";
    updateCurrentArticleFromForm();
    var data =
        "id=" +
        currentArticle.id +
        "&price=" +
        currentArticle.prix +
        "&quantity=" +
        currentArticle.quantiter;

    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            getArticle();
        }
    };
    url += data;
    xhr.open("POST", url, true);
    xhr.send();
}
