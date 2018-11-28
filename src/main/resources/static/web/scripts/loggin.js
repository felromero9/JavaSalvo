jQuery(document).ready(function($) {
    $("#newUserButton").change(function(){
        $("#loginButton").toggle();
        $("#signupButton").toggle();
        $("#playerSide").toggle();

    });

//SIGN UP------------------------------------------------------------------
    $("#signupButton").click(function(){

        var newUserName = $("#writeUserName").val();
        var newUserPassword = $("#writePassword").val();
        var newPlayerSide = $("#playerSide").find("input[type='radio']:checked").val();

        if (newUserName && newUserPassword && newPlayerSide) {
            postPlayer(newUserName, newUserPassword, newPlayerSide);
        }
        else {
            console.log("please enter a username and password")
            alertify.error(' please enter a username and password ');
        }
        console.log("radioVal");
    });

       function postPlayer(newUserName, newUserPassword, newPlayerSide) {
        $.post( "/api/players",{ userName: newUserName, password: newUserPassword, playerSide: newPlayerSide})
            .done(function( ) {
                loginData();
                console.log( "Saved -- reloading");

            })
            .fail(function( respose) {
                console.log( respose.status );
                if(respose.status == 409){
                    console.log("username in use")
                    alertify.error(' Username in use ');
                }
                else if (response.status == 403){
                    console.log("pws or user en blanco");

                }
            });
    }

//LOGIN-----------------------------------------------------
    $("#loginButton").click(function(){
        loginData();
    })

    function loginData(){
        var userName = $("#writeUserName").val();
        var userPassword = $("#writePassword").val();
        if (userName && userPassword) {
            postLoginPlayer(userName, userPassword);
        }
        else {
            console.log("please enter a username and password")
            alertify.error(' please enter a username and password ');
        }
    }

    function postLoginPlayer(userName, userPassword) {
        $.post( "/api/login",{ username: userName, password: userPassword })
            .done(function( ) {
                console.log( "You are logged");

                // llamado del fecht
                $.get("/api/games")
                    .done(function (json)
                    {
                        console.log("well done my friend!");
                        app.games = json.games;
                        app.playerWelcome = json.user.userName;
                        app.myPlayerId = json.user.id;
                        app.user = json.user
                        checkIfGuest();
                     })
                    .fail(function (error)
                    {
                        console.log("error");
                    })
                // finaliza llamado del fecht

            })
        .fail(function( response ) {
            console.log( response.status );
            if (response.status == 401){
                console.log("pws or user invalid")
                alertify.error("password or username invalid")
                }
            });
    }


    //LOGOUT----------------------------------------------------------
    $("#logoutButton").click(function(){
        postLoginPlayerOut();
    })

    function postLoginPlayerOut(userName, userPassword) {
        $.post( "/api/logout",{ username: userName, password: userPassword })
            .done(function( ) {
                console.log( "You are logged out.");
                location.reload();
                location.href='http://localhost:8080/web/games.html';

            })
            .fail(function( jqXHR, textStatus ) {
                console.log( "error" + textStatus );
            });
    };

    //NEWGAME------------------------------------------------------
    $('#newgame').click(function(){
        postNewGame();
    })

    function postNewGame (response){
        $.post( "/api/games")
            .done(function(response) {
                console.log( "This is a new game.");
                console.log(response);
                //location.reload();
               window.location.href = "game.html?gp="+ response.gpid;
            })
            .fail(function(response) {
                console.log( "fatal error in creation game" + response );
                console.log(response);
            });
        }

    //JOIN GAME--------------------------------------------------
    $('#app').on('click', '.joinGame', function(){
         console.log("join game");
          var joinGameData = $(this).data('joingpid');
         joinGame(joinGameData);
    })

    function joinGame(gameDataId){


        $.post( "/api/game/"+gameDataId+"/players")
            .done(function(response) {
                console.log( "This is a join game.");
                 console.log("the game is" + gameDataId);
                console.log(response);
                //location.reload();
              window.location.href = "game.html?gp="+ response.gpid;
            })
            .fail(function(response) {
                console.log( "fatal error in join game" + response );
                console.log(response);

            });
    }



    // SEND SHIPS -----------------------------------------------
  $('#sendShips').click(function(){
    console.log("it works");
    reverseMapsShips();
    sendShipsToBackEnd(app.myGpId);
    location.reload();
  })

    function sendShipsToBackEnd(myGpId){
        $.post({
            url:"/api/games/players/"+myGpId+"/ships",
            data: JSON.stringify(reverseMapsShips()),
            dataType: "text",
            contentType: "application/json"
        })
            .done(function(response) {
                console.log( "New set ships added");
                console.log(response);
            })
            .fail(function(response) {
                console.log( "fatal error cant join ships" + response.status );
                console.log(response);
            });
        }


    // ADDING SALVOES-----------------------------------------------------------------

    $(".cell").on('click', function(){
     if(!$(this).hasClass("targeted")){
        $(this).addClass("targeted")
        console.log($(this).attr("id"));
     }
         else if ($(this).hasClass("targeted")){
            $(this).removeClass("targeted")
         };
    })



$('#sendSalvoes').click(function(){
    console.log("buttom salvoes");
    sendSalvoesToBackEnd(app.myGpId);
})




function sendSalvoesToBackEnd(myGpId){
    $.post({
        url: "/api/games/players/"+myGpId+"/salvoes",
        data: JSON.stringify(addSalvosShots()),
        dataType: "text",
        contentType: "application/json"
        })
    .done(function(){
        console.log("New set shots sends");
         location.reload();


    })
    .fail(function(response){
        var responseData = JSON.parse(response.responseText);
        console.log("fatal error " + response.status);
        console.log(response);
        alertify.error(responseData.error)

    })

  }
  });

