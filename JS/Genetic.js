//Basado en Introduction To Genetic Algorithms (https://www.codecademy.com)

var TARGET="METHINKS IT IS LIKE A WEASEL";

//Posibles cromosomas de un genotipo
var ALPHABET = "ABCDEFGHIJKLMONPQRSTUVWXYZ ";

//Propabilidad de mutación
var MUT_PROB = 10;

//Tamaño de la población
var POOL_SIZE = 50;

//UTILITIES
var Console = function () {
    this.log = function(msg){ debug(msg) }; 
};

if(!console)
{
var console = new Console();
}

/**
* Genera un individuo/genotipo/genoma aleatorio 
* @returns {String} Nuevo individuo generado.
*/
var generateGenome = function(){
    var genome = [];
    for (var i = 0; i<28;++i){
        genome[i] = ALPHABET[Math.floor(Math.random()*ALPHABET.length)];
    }
    return genome.join("");
};

/**
* Calcula el 'fitness' de un individuo 
* @param {String} genome - Individuo.
* @returns {Integer} Función objetivo evaluada en el individuo.
*/
var getFitness = function(genome){
    var fitness = 0;
    for (var i = 0; i<TARGET.length;i++){
        if (genome[i]===TARGET[i]){
            fitness++;
        }
    }
    return fitness;
}; 

/**
* Genera la población inicial
* @param {String} genome - Individuo inicial de la población.
* @returns {String[]} Población inicial de individuos.
*/
var getGenePool = function(genome, size){
    var pool = [];
    for (var i=0; i<size;i++){
        pool[i] = genome;
    }
    return pool;
};

/**
* Obtiene el individuo más apto de una población
* @param {String[]} pool - Población
* @returns {String} Individuo más apto de la población.
*/
var getFittest = function(pool){
    var fittestLoc = 0;
    var fittest = 0;
    for (var i=0; i<pool.length;++i){
        if (getFitness(pool[i]) >fittest){
            fittest = getFitness(pool[i]);
            fittestLoc = i;
        }
    }
    return pool[fittestLoc];
};

/**
* Muta un individuo
* @param {String[]} genome - Individuo a mutar
* @returns {String} Nuevo individuo mutado.
*/
var doMutation = function (genome)
{
        var newGenome = "";
        for (var i=0; i<genome.length;++i){
            if(Math.floor(Math.random()*MUT_PROB)===1){
                if(genome[i]!=TARGET[i]){
                    newGenome+= ALPHABET[Math.floor(Math.random()*ALPHABET.length)];
                }
                else{
                    newGenome+=genome[i];
                }
            } 
            else{
                newGenome+=genome[i];
            }
        }
        return newGenome;
}

/**
* Realiza el proceso evolutivo
* @returns {String} Individuo óptimo.
*/
var evolve = function(){

        //Generación
        var count = 0;

        //Generación del individuo inicial
        var fittest = generateGenome();
        console.log(fittest);

        while(getFitness(fittest)!==28){
            count++;

            //Generación de la población a partir del indiviuo más apto
            var pool = getGenePool(fittest, POOL_SIZE);

            //Población con mutación
            var pool2 = [];
            for (var i=0; i<pool.length;++i){
                pool2[i]=doMutation(pool[i],true);
            }
            fittest = getFittest(pool2);
            if(count%3===0){ 
                console.log(fittest);
            }
        }
        console.log(fittest)
        return fittest;
    };

evolve();

