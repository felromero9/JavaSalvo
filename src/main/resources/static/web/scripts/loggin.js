jQuery(document).ready(function($) {
    $("#newUserButton").change(function(){
        $("#loginButton").toggle();
        $("#signupButton").toggle();
    });

//SIGN UP------------------------------------------------------------------
    $("#signupButton").click(function(){
        var newUserName = $("#writeUserName").val();
        var newUserPassword = $("#writePassword").val();
        if (newUserName && newUserPassword) {
            postPlayer(newUserName, newUserPassword);
        }
        else {
            console.log("please enter a username and password")
            alertify.error(' please enter a username and password ');
        }
    });

    function postPlayer(newUserName, newUserPassword) {
        $.post( "/api/players",{ userName: newUserName, password: newUserPassword })
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

                        checkIfGuest(json);
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
            .done(function(response ) {
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

});

