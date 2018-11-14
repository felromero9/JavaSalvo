



function getGridOptions(){
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
        staticGrid: true,
        //activa animaciones (cuando se suelta el elemento se ve más suave la caida)
        animate: true
    }
    if (app.ships == 0){
        options.staticGrid= false;
    }
    return options;
}
//create the Dom if the player has ships
function plantsGridStack(){
$('.grid-stack').gridstack(getGridOptions());
        grid = $('#grid').data('gridstack');
        mapShips()

}
//create the Dom if the player has ships
function zombiesGridStack(){
 $('.grid-stack').gridstack(getGridOptions());
        grid = $('#grid').data('gridstack');
        mapShips()
}

function shipSide(){
    if(app.side == "PLANTS"){
       plantsGridStack()
    }
        else{
         zombiesGridStack()
        }
    }

function plantsGridNew(){
        grid = $('#grid').data('gridstack');
        grid.addWidget($('<div id="LANZAGUISANTES"><div class="grid-stack-item-content lanzaguisantes"></div><div/>'),
            1, 8, 2, 1);
        grid.addWidget($('<div id="LANZAMAIZ"><div class="grid-stack-item-content lanzamaiz"></div><div/>'),
            1, 5, 3, 1);
        grid.addWidget($('<div id="HIELAGUISANTES"><div class="grid-stack-item-content hielaguisantes"></div><div/>'),
            2, 4, 3, 1);
        grid.addWidget($('<div id="MELONPULTA"><div class="grid-stack-item-content melonpulta"></div><div/>'),
            0, 5, 4, 1);
        grid.addWidget($('<div id="CARRONIVERA"><div class="grid-stack-item-content carronivera"></div><div/>'),
            4, 5, 5, 1);
             console.log(grid.isAreaEmpty(1, 8, 3, 1));
                        //está libre, true
                        console.log(grid.isAreaEmpty(1, 7, 3, 1));

            $("#CARRONIVERA").click(function(){
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
            $("#LANZAMAIZ,#HIELAGUISANTES").click(function(){
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
            $("#MELONPULTA").click(function(){
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
            $("#LANZAGUISANTES").click(function(){
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

function zombiesGridNew(){
 grid = $('#grid').data('gridstack');
        grid.addWidget($('<div id="ZOMBIECOMUN"><div class="grid-stack-item-content zombiecomun"></div><div/>'),
            1, 8, 2, 1);
        grid.addWidget($('<div id="ZOMBIELECTOR"><div class="grid-stack-item-content zombielector"></div><div/>'),
            1, 5, 3, 1);
        grid.addWidget($('<div id="ZOMBIEBAILARIN"><div class="grid-stack-item-content zombiebailarin"></div><div/>'),
            2, 4, 3, 1);
        grid.addWidget($('<div id="ZOMBIEPLAYERO"><div class="grid-stack-item-content zombieplayero"></div><div/>'),
            0, 5, 4, 1);
        grid.addWidget($('<div id="ZOMBISTEIN"><div class="grid-stack-item-content zombistein"></div><div/>'),
            4, 5, 5, 1);
             console.log(grid.isAreaEmpty(1, 8, 3, 1));
                        //está libre, true
                        console.log(grid.isAreaEmpty(1, 7, 3, 1));

            $("#ZOMBISTEIN").click(function(){
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
            $("#ZOMBIELECTOR,#ZOMBIEBAILARIN").click(function(){
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
            $("#ZOMBIEPLAYERO").click(function(){
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
            $("#ZOMBIECOMUN").click(function(){
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

// ADDING BUTTOM TO SEND SHIP LOCATION TO BACKEND :)
function reverseMapsShips(){
var newShips=[];

$(".grid-stack-item").each(function(){
    var array = []
    var y = parseInt($(this).attr("data-gs-y"));
    var x = parseInt($(this).attr("data-gs-x"))+1;
    var width = parseInt($(this).attr("data-gs-width"));
    var height = parseInt($(this).attr("data-gs-height"));

    var newObject = new Object

    newObject.type = $(this).attr("id")


    if(width>height){
        for(var i=0; i<width; i++ ){
        array.push(getLetter(y)+(x+i).toString());
        }
   }else{
        for (var i=0; i<height; i++){
        array.push(getLetter(y+i)+x)
        }
    }

    newObject.cells = array
    newShips.push(newObject);

})
console.log(newShips);
return newShips;
}

// CONVERT LETTER FOR LETTER IN NUMBER
function getLetter(y){
    switch (y) {
        case 0:
            return "A";
            break;
        case 1:
            return "B";
            break;
        case 2:
            return "C";
            break;
        case 3:
            return "D";
            break;
        case 4:
            return "E";
             break;
        case 5:
            return "F";
             break;
        case 6:
            return "G";
             break;
        case 7:
            return "H";
             break;
        case 8:
            return "I";
            break;
        case 9:
            return "J";
            break;
    default:
        console.log("default");
    }
}

// CONVERT LETTER FOR NUMBER IN CELLS
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

// MAPPING SHIPS TO ADD INFO
function mapShips(){
    if(app.ships.length == 0){
    //saber si es planta o zombie
      if(app.side == "PLANTS"){
       plantsGridNew();
       }
       else {
        zombiesGridNew();
        }
    }
    else{
         app.ships.forEach(function(ship){
          var y = getNumber(ship.cells[0].slice(0,1));
          var x = parseInt(ship.cells[0].slice(1)) - 1;
          var width;
          var height;
          console.log("RESULTADO DEL MAPSHIPS:",x,y);

          if(ship.cells[0].slice(0,1) == ship.cells[1].slice(0,1) ){
                  width = ship.cells.length;
                  height = 1;
               }
               else{
                  width = 1;
                  height = ship.cells.length;
               }
          grid.addWidget($("<div id='"+ship.type+"'><div class='grid-stack-item-content "+ship.type.toLowerCase()+"'></div><div/>"),
                       x, y, width, height);

         })
     }


}



