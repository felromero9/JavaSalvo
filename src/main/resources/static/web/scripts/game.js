var app;
var currentURL = window.location.href;
var numberVariable= takeNumberURL(currentURL);

//find number in url!
function takeNumberURL(url) {
    var n = url.slice(url.indexOf("gp=")+3);
    console.log(n);
    return n;
}

$(function () {
    app = new Vue({
        el: '#app',
        data: {
            vertical: ["A", "B", "C", "D", "E", "F", "G", "H", "I", "J"],
            horizontal: ["", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"],
            ships:[],
            salvoes:[],
            playerA:"",
            playerB:"",
            msgA:"",
            msgB:"",
            myGpId:"",
            opponentGpId:"",
            myId:"",
            opponentId:"",
            side:""
        }
    });
    fetchJson("http://localhost:8080/api/game_view/"+ numberVariable, {
        method: 'GET',

    })

        .then(function (json) {
            console.log("well done my friend!");
            getmsg(json);
            app.ships = json.ships;
            app.salvoes = json.salvoes;
            app.playerA = json.gamePlayer[0].player.username;
            app.playerB = json.gamePlayer[1].player.username;
            getIds(json);
            paintPosition(app.ships);
            paintPositionSalvoes(json, app.salvoes);


        }).catch(function (error) {
        console.log("FATAL error!");
    });


    function getIds(json){
        if (json.gamePlayer[0].id==numberVariable){
            app.myGpId=json.gamePlayer[0].id;
            app.myId=json.gamePlayer[0].player.id;
            app.opponentGpId=json.gamePlayer[1].id;
            app.opponentId=json.gamePlayer[1].player.id;
        }
        else {
            app.myGpId=json.gamePlayer[1].id;
            app.myId=json.gamePlayer[1].player.id;
            app.opponentGpId=json.gamePlayer[0].id
            app.opponentId=json.gamePlayer[0].player.id;
        }
    }



    function paintPosition(ships) {
        ships.forEach(function (ship) {
            ship.cells.forEach(function (cell) {
                $('#' + cell).addClass("my-ship");
            })
        });
    }


    function paintPositionSalvoes(json, salvoes){
        for (var i = 0; i < salvoes.length; i++) {
            if (salvoes[i].player.id == app.myGpId) {
                salvoes[i].cells.forEach(cell => mySalvosStyle(cell, json, i));
            }
            else {
                salvoes[i].cells.forEach(cell => opponentSalvosStyle(cell, json, i));
            }
        }
    }

    function mySalvosStyle(cell, json, i) {
        $('#' + cell + 's').addClass("my-salvo").html(json.salvoes[i].turn);
    }

    function opponentSalvosStyle(cell, json, i){
        if ($('#' + cell).hasClass("my-ship")){
            $('#' + cell).addClass("hit-my-ship").html(json.salvoes[i].turn);
        }
        else{
            $('#' + cell).addClass("opponent-salvo").html(json.salvoes[i].turn);
        }
    }
    function getmsg(json) {
        if(numberVariable == json.gamePlayer[0].id){
            app.msgA="YOU ARE";
        }
        else app.msgB="YOU ARE";
    }
});

$('#logoutButton').click(function(){
    console.log("adios");
    postLoginPlayerOut();
    
});

function postLoginPlayerOut(userName, userPassword) {
    $.post("/api/logout", {username: userName, password: userPassword})
        .done(function () {
            console.log("You are logged out.");
            location.reload();
            location.href='http://localhost:8080/web/games.html';

        })
        .fail(function (jqXHR, textStatus) {
            console.log("error" + textStatus);
        });
}



/*jQuery(document).ready(function($) {
    $("#logoutButton").click(function () {
        postLoginPlayerOut();
    });
},

function postLoginPlayerOut(userName, userPassword) {
    $.post( "/api/logout",{ username: userName, password: userPassword })
        .done(function( ) {
            console.log( "You are logged out.");
            location.reload();


        })
        .fail(function( jqXHR, textStatus ) {
            console.log( "error" + textStatus );
        });
},*/




/*function paintPositionSalvo(salvoes) {
       salvoes.forEach(function (salvo) {
           salvo.cells.forEach(function (cell) {
               $('#' + cell+'s').addClass("my-salvo");
           })
       });
   }*/
/*function addAllLocation(json) {
    var shipCells = [];
    for (i = 0; i < json.ships.length; i++) {
        shipCells.push(json.ships[i].cells)
    }
    return shipCells;
}*/

/*function paintPosition(ships) {
    for (var j = 0; j < ships.length; j++) {
        for (var i = 0; i < ships[j].cells.length; i++)

            $('#' + ships[j].cells[i]).addClass("my-ship");
    }
}*/