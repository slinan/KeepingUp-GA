package JMetalTutorial;
/**
 * Tomado de https://github.com/jMetal/jMetal
 */
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.singleobjective.geneticalgorithm.GeneticAlgorithmBuilder;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.crossover.PMXCrossover;
import org.uma.jmetal.operator.impl.mutation.PermutationSwapMutation;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.problem.PermutationProblem;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.problem.singleobjective.TSP;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.solution.PermutationSolution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.comparator.RankingAndCrowdingDistanceComparator;
import org.uma.jmetal.util.fileoutput.SolutionListOutput;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;

import com.google.gson.Gson;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class SingleObjective 
{
	/**
     *https://github.com/jMetal/jMetal/blob/master/jmetal-exec
     *@author Antonio J. Nebro <antonio@lcc.uma.es>
	 */
	public static void main(String[] args) throws Exception {

		PermutationProblem<PermutationSolution<Integer>> problem;
		Algorithm<PermutationSolution<Integer>> algorithm;
		CrossoverOperator<PermutationSolution<Integer>> crossover;
		MutationOperator<PermutationSolution<Integer>> mutation;
		SelectionOperator<List<PermutationSolution<Integer>>, PermutationSolution<Integer>> selection;
		
		/*
		 * Traveling Salesman Problem
		 * https://github.com/jMetal/jMetal/blob/master/jmetal-core/src/main/resources/tspInstances/kroA100.tsp
		 * Given a list of cities and the distances between each pair of cities, 
		 * what is the shortest possible route that visits each city 
		 * and returns to the origin city?
		 */
		
		problem = new TSP("/tspInstances/kroA100.tsp");
		
		/* 
		 * PMX Crossover
		 * https://en.wikipedia.org/wiki/Crossover_(genetic_algorithm)
		 * http://www.rubicite.com/Tutorials/GeneticAlgorithms/CrossoverOperators/PMXCrossoverOperator.aspx/
		 */
		crossover = new PMXCrossover(0.9) ;
		double mutationProbability = 1.0 / problem.getNumberOfVariables() ;
		
		/*
		 * PermutationSwapMutation
		 *https://www.tutorialspoint.com/genetic_algorithms/genetic_algorithms_mutation.htm 
		 */
		mutation = new PermutationSwapMutation<Integer>(mutationProbability) ;

		/*
		 *https://en.wikipedia.org/wiki/Tournament_selection
		 * k = 2 
		 */
		selection = new BinaryTournamentSelection<PermutationSolution<Integer>>(
				new RankingAndCrowdingDistanceComparator<PermutationSolution<Integer>>());

		/*
		 * Construcción del algoritmo genético 
		 */
		algorithm = new GeneticAlgorithmBuilder<>(problem, crossover, mutation)
				.setPopulationSize(100)
				.setMaxEvaluations(250000)
				.setSelectionOperator(selection)
				.build() ;

		AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm).execute() ;

		PermutationSolution<Integer> solution = algorithm.getResult() ;
		List<PermutationSolution<Integer>> population = new ArrayList<>(1) ;
		population.add(solution) ;

		long computingTime = algorithmRunner.getComputingTime();

		new SolutionListOutput(population)
		.setSeparator("\t")
		.setVarFileOutputContext(new DefaultFileOutputContext("VAR.tsv"))
		.setFunFileOutputContext(new DefaultFileOutputContext("FUN.tsv"))
		.print();

		JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");
		JMetalLogger.logger.info("Objectives values have been written to file FUN.tsv");
		JMetalLogger.logger.info("Variables values have been written to file VAR.tsv");

	}
}
