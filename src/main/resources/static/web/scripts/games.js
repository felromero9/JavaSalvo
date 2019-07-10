
    var app;


$(function () {

    app = new Vue({
        el: '#app',
        data: {
                games: [],
                dataObjectsOfPlayers : [],
                playerWelcome:"",
                myPlayerId:"",
                user:"",
                gamePlayerId:"",
                side:""

            }
        });
//llamado del fetch

        fetchJson("/api/games", {
                       method: 'GET',

                   })
                       .then(function (json) {
                          console.log("well done my friend!");
                          app.games=json.games;
                          processPoints(json);
                          app.playerWelcome = json.user.userName;
                          app.myPlayerId = json.user.id;
                          app.gamePlayerId = json.games.id;
                          app.user = json.user;
                          app.side = json.user.side;
                          console.log("y que onda"+json.user!=="Guest")


                       }).then(function(){
                           checkIfGuest();

                       }).catch(function (error) {
                       console.log("FATAL error!");
                   });
// finaliza llamado del fetch


    function processPoints(json){
        for (var i =0; i<json.games.length; i++){
            for (var j=0; j<2;j++){
                if(json.games[i].gamePlayers[j]){
                    var currentIdInJson = json.games[i].gamePlayers[j].player.id;
                    var findInAppIdJson = app.dataObjectsOfPlayers.findIndex(player=>player.id === currentIdInJson);
                    var currentPlayerInApp= app.dataObjectsOfPlayers[findInAppIdJson];
                    if (findInAppIdJson == -1){
                        var obj = new Object;
                        obj.id = currentIdInJson;
                        obj.userName = json.games[i].gamePlayers[j].player.username;
                        obj.win=0;
                        obj.lost=0;
                        obj.tied=0;
                        obj.points=0;
                        if (json.games[i].gamePlayers[j].score==3){
                            obj.win++;
                        }
                        else if (json.games[i].gamePlayers[j].score==0){
                            obj.lost++;
                        }
                        else if (json.games[i].gamePlayers[j].score==1){
                            obj.tied++;
                        }
                        obj.points=(obj.win*3) + (obj.tied*1);
                        app.dataObjectsOfPlayers.push(obj);
                    }
                    else {
                        if (json.games[i].gamePlayers[j].score==3){
                            currentPlayerInApp.win++;
                        }
                        else if (json.games[i].gamePlayers[j].score==0){
                            currentPlayerInApp.lost++;
                        }
                        else if (json.games[i].gamePlayers[j].score==1){
                            currentPlayerInApp.tied++;
                        }
                        currentPlayerInApp.points=(currentPlayerInApp.win*3) + (currentPlayerInApp.tied*1);
                    }
                }
            }
        }
    }




});

    function checkIfGuest(){
        if (app.user!=="Guest"){
            $("#logoutButton").show();
            $("#welcomePlayer").show();
            $("#myForm").hide();
            $("#newGameButton").show();
            $('#title').hide();
            $('#welcome').show();
            $('#newgame').show();
            $('.joinGame').show();
            $('#playerSide').show();
            $("#newUserButton").hide();
            $("#joinUserButton").hide();
            $("#gameList").show();
            $("#tableScore").show();

        }
        else{
            $("#logoutButton").hide();
            $("#welcomePlayer").hide();
            $("#myForm").show();
            $("#newGameButton").hide();
            $('#title').hide();
            $('#welcome').show();
            $('#newgame').hide();
            $('.joinGame').hide();
            $('#playerSide').hide();
            $("#newUserButton").show();
            $("#joinUserButton").show();
            $("#gameList").hide();
            $("#tableScore").hide();




        }
    }