var Articlecourrant = {
    id: -1,
    nom: "",
    prix: 0.0,
    quantiter: -1,
    image: "",
};

var listArticle = [];
var data;  // Déclarer la variable data à un niveau supérieur

document.addEventListener("DOMContentLoaded", function () {
    getArticle();

    document.getElementById("updateButton").addEventListener("click", function (event) {
        event.preventDefault();
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

    document.getElementById("tableBody").classList.add("animate__animated", "animate__fadeInUp");
}

function tableUpdate(articles) {
    resetTable();
    listArticle = [];
    var body = document.getElementById("tableBody");
    Articlecourrant.id = -1;

    articles.forEach((element) => {
        const newRow = body.insertRow();

        newRow.insertCell(0).textContent = element.id;
        newRow.insertCell(1).textContent = element.nom;
        newRow.insertCell(2).textContent = element.prix;
        newRow.insertCell(3).textContent = element.quantiter;
        listArticle.push(element);

        // Ajouter une animation pour chaque ligne du tableau
        newRow.classList.add("animate__animated", "animate__fadeIn");

        newRow.onclick = function () {
            if (Articlecourrant.id !== -1) {
                var selectedRows = body.getElementsByClassName("selected");
                selectedRows[0].classList.remove("selected");
            }

            this.classList.add("selected");

            this.classList.add("animate__animated", "animate__pulse");

            var idSelected = parseInt(this.cells[0].innerHTML);
            Articlecourrant = listArticle.find((article) => article.id === idSelected);

            document.getElementById("id").value = Articlecourrant.id;
            document.getElementById("article").value = Articlecourrant.nom;
            document.getElementById("prixUnitaire").value = Articlecourrant.prix;
            document.getElementById("quantite").value = Articlecourrant.quantiter;
            document.getElementById("imageaff").src = "images/" + Articlecourrant.image;
        };
    });
}

function resetTable() {
    var body = document.getElementById("tableBody");

    for (var i = body.rows.length - 1; i >= 0; i--) {
        body.deleteRow(i);
    }
}

function updateDB() {
    var xhr = new XMLHttpRequest();
    var url = "http://localhost:8080/FormArticle";
    update();
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            getArticle();
        }
    };
    xhr.open("POST", url, true);

    // Définir l'en-tête de la requête pour indiquer l'envoi de données JSON
    xhr.setRequestHeader("Content-Type", "application/json");

    console.log(data)

    xhr.send(data);
    getArticle();
    resetTable();
}

function resetFormulaire() {
    document.getElementById("id").value = "";
    document.getElementById("article").value = "";
    document.getElementById("prixUnitaire").value = 0;
    document.getElementById("quantite").value = 0;
}

function update() {
    Articlecourrant.prix = document.getElementById("prixUnitaire").value;
    Articlecourrant.quantiter = document.getElementById("quantite").value;

    // Mettre à jour la variable data avec les nouvelles valeurs
    var articleData = {
        id: Articlecourrant.id,
        price: Articlecourrant.prix,
        quantity: Articlecourrant.quantiter
    };

    // Convertir les données en JSON
    data = JSON.stringify(articleData);
}
