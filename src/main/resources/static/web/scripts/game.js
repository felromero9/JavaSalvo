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
            side:"",
            YOU:""
        }
    });

    fetchJson("http://localhost:8080/api/games",{
                    method: 'GET',
                })
                .then(function(json){
                //app.playerA = json.gamePlayer[0].player.username;
              //  app.playerB = json.gamePlayer[1].player.username;
                app.side =json.user.side;
                app.YOU = json.user.userName;
                }).catch(function(error){
                console.log("error fatal");
                });

    fetchJson("http://localhost:8080/api/game_view/"+ numberVariable, {
        method: 'GET',

    })

        .then(function (json) {
            console.log("well done my friend!");
            getmsg(json);
            app.ships = json.ships;
            app.salvoes = json.salvoes;

           getIds(json);
            paintPosition(app.ships);
            paintPositionSalvoes(json, app.salvoes);
            shipSide();
            //playerSide();

        }).catch(function (error) {
        console.log(error);
        });
//////////////////

     function getIds(json){

        json.gamePlayer.forEach(function(gamePlay){
        if(gamePlay.id == numberVariable){
            app.myGpId = gamePlay.id;
            app.myIo = gamePlay.player.id;

        }
            else{
            app.opponentGpId = gamePlay.id;
            app.opponentId = gamePlay.player.id;

            }
        })
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


