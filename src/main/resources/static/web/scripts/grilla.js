
$(function () {
    var options = {
        //grilla de 10 x 10
        width: 10,
        height: 10,
        //separacion entre elementos (les llaman widgets)
        verticalMargin: 0,
        //altura de las celdas
        cellHeight: 35,
        //desabilitando el resize de los widgets
        disableResize: true,
        //widgets flotantes
		float: true,
        //removeTimeout: 100,
        //permite que el widget ocupe mas de una columna
        disableOneColumnMode: true,
        //false permite mover, true impide
        staticGrid: false,
        //activa animaciones (cuando se suelta el elemento se ve más suave la caida)
        animate: true
    }

    fetchJson("http://localhost:8080/api/games", {
            method: 'GET',
        })
            .then(function (json) {
               console.log("fecth api games");
               app.side = json.user.side;
               shipSide();
               mapShips();
            }).catch(function (error) {
            console.log(error);
            });

function shipSide(){
    if(app.side == "PLANTS"){
            //se inicializa el grid con las opciones
            $('.grid-stack').gridstack(options);
            grid = $('#grid').data('gridstack');
            //agregando un elmento(widget) desde el javascript
            grid.addWidget($('<div id="lanzaguisantes"><div class="grid-stack-item-content lanzaguisantes"></div><div/>'),
                1, 8, 2, 1);
            grid.addWidget($('<div id="lanzamaiz"><div class="grid-stack-item-content lanzamaiz"></div><div/>'),
                1, 5, 3, 1);
            grid.addWidget($('<div id="hielaguisantes"><div class="grid-stack-item-content hielaguisantes"></div><div/>'),
                2, 4, 3, 1);
            grid.addWidget($('<div id="melonpulta"><div class="grid-stack-item-content melonpulta"></div><div/>'),
                0, 5, 4, 1);
            grid.addWidget($('<div id="carronivora"><div class="grid-stack-item-content carronivora"></div><div/>'),
                4, 5, 5, 1);
             console.log(grid.isAreaEmpty(1, 8, 3, 1));
                //está libre, true
                console.log(grid.isAreaEmpty(1, 7, 3, 1));

             $("#carronivora").click(function(){
                    if($(this).children().hasClass("carronivora")){
                        grid.resize($(this),1,5);
                        $(this).children().removeClass("carronivora");
                        $(this).children().addClass("carrierHorizontalRed");
                    }else{
                        grid.resize($(this),5,1);
                        $(this).children().addClass("carronivora");
                        $(this).children().removeClass("carrierHorizontalRed");
                    }
                });

                $("#lanzamaiz,#hielaguisantes").click(function(){
                    if($(this).children().hasClass("lanzamaiz,hielaguisantes")){
                        grid.resize($(this),1,3);
                        $(this).children().removeClass("lanzamaiz,hielaguisantes");
                        $(this).children().addClass("carrierHorizontalRed");
                    }else{
                        grid.resize($(this),3,1);
                        $(this).children().addClass("lanzamaiz,hielaguisantes");
                        $(this).children().removeClass("carrierHorizontalRed");
                    }
                });
                 $("#melonpulta").click(function(){
                        if($(this).children().hasClass("melonpulta")){
                            grid.resize($(this),1,4);
                            $(this).children().removeClass("melonpulta");
                            $(this).children().addClass("carrierHorizontalRed");
                        }else{
                            grid.resize($(this),4,1);
                            $(this).children().addClass("melonpulta");
                            $(this).children().removeClass("carrierHorizontalRed");
                        }
                    });
                $("#lanzaguisantes").click(function(){
                    if($(this).children().hasClass("lanzaguisantes")){
                        grid.resize($(this),1,2);
                        $(this).children().removeClass("lanzaguisantes");
                        $(this).children().addClass("patroalHorizontalRed");
                    }else{
                        grid.resize($(this),2,1);
                        $(this).children().addClass("lanzaguisantes");
                        $(this).children().removeClass("patroalHorizontalRed");
                    }
                });
    }else{
         //se inicializa el grid con las opciones
        $('.grid-stack').gridstack(options);
        grid = $('#grid').data('gridstack');
        //agregando un elmento(widget) desde el javascript
          grid.addWidget($('<div id="zombiecomun"><div class="grid-stack-item-content lanzaguisantes"></div><div/>'),
             1, 8, 2, 1);
          grid.addWidget($('<div id="zombielector"><div class="grid-stack-item-content lanzamaiz"></div><div/>'),
             1, 5, 3, 1);
          grid.addWidget($('<div id="zombiebailon"><div class="grid-stack-item-content hielaguisantes"></div><div/>'),
             2, 4, 3, 1);
          grid.addWidget($('<div id="zombieplayero"><div class="grid-stack-item-content melonpulta"></div><div/>'),
             0, 5, 4, 1);
          grid.addWidget($('<div id="zombiestein"><div class="grid-stack-item-content carronivora"></div><div/>'),
                4, 5, 5, 1);
     console.log(grid.isAreaEmpty(1, 8, 3, 1));
        //está libre, true
        console.log(grid.isAreaEmpty(1, 7, 3, 1));

     $("#carronivora").click(function(){
            if($(this).children().hasClass("carronivora")){
                grid.resize($(this),1,5);
                $(this).children().removeClass("carronivora");
                $(this).children().addClass("carrierHorizontalRed");
            }else{
                grid.resize($(this),5,1);
                $(this).children().addClass("carronivora");
                $(this).children().removeClass("carrierHorizontalRed");
            }
        });

        $("#lanzamaiz,#hielaguisantes").click(function(){
            if($(this).children().hasClass("lanzamaiz,hielaguisantes")){
                grid.resize($(this),1,3);
                $(this).children().removeClass("lanzamaiz,hielaguisantes");
                $(this).children().addClass("carrierHorizontalRed");
            }else{
                grid.resize($(this),3,1);
                $(this).children().addClass("lanzamaiz,hielaguisantes");
                $(this).children().removeClass("carrierHorizontalRed");
            }
        });
         $("#melonpulta").click(function(){
                if($(this).children().hasClass("melonpulta")){
                    grid.resize($(this),1,4);
                    $(this).children().removeClass("melonpulta");
                    $(this).children().addClass("carrierHorizontalRed");
                }else{
                    grid.resize($(this),4,1);
                    $(this).children().addClass("melonpulta");
                    $(this).children().removeClass("carrierHorizontalRed");
                }
            });
        $("#lanzaguisantes").click(function(){
            if($(this).children().hasClass("lanzaguisantes")){
                grid.resize($(this),1,2);
                $(this).children().removeClass("lanzaguisantes");
                $(this).children().addClass("patroalHorizontalRed");
            }else{
                grid.resize($(this),2,1);
                $(this).children().addClass("lanzaguisantes");
                $(this).children().removeClass("patroalHorizontalRed");
            }
        });
        }
    }
});

function getNumber(y){
    switch (y) {
        case "A":
            return 0;
            break;
        case "B":
            return 1;
            break;
        case "C":
            return 2;
            break;
        case "D":
            return 3;
            break;
        case "E":
            return 4;
             break;
        case "F":
            return 5;
             break;
        case "G":
            return 6;
             break;
        case "H":
            return 7;
             break;
        case "I":
            return 8;
            break;
        case "J":
            return 9;
            break;
    default:
        console.log("default");
    }
}
function mapShips(){
    app.ships.forEach(function(ship){

     var y = getNumber(ship.cells[0].slice(0,1));
     var x = parseInt(ship.cells[0].slice(1)) - 1;

    })

    }


/*
var y = app.ships[0].cells[0].slice(1);
var x = app.ships[0].cells[0].slice(0,1);*/




    //verificando si un area se encuentra libre
    //no está libre, false




