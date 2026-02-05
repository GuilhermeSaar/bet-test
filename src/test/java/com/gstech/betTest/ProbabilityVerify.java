package com.gstech.betTest;

import com.gstech.betTest.service.ProbabilityService;
import java.util.Map;
import java.util.Arrays;

public class ProbabilityVerify {
    public static void main(String[] args) {
        ProbabilityService service = new ProbabilityService();

        // Exemplo: Home 1.5, Away 1.2
        double lambdaHome = 1.5;
        double lambdaAway = 1.2;

        System.out.println("Testing with Home=" + lambdaHome + ", Away=" + lambdaAway);

        double[] homeProbs = service.calculateGoalProbabilities(lambdaHome);
        double[] awayProbs = service.calculateGoalProbabilities(lambdaAway);

        System.out.println("Home Probs Sum: " + Arrays.stream(homeProbs).sum());
        System.out.println("Away Probs Sum: " + Arrays.stream(awayProbs).sum());

        double[][] matrix = service.generateScoreMatrix(homeProbs, awayProbs);

        double totalProb = 0;
        for (double[] row : matrix) {
            for (double p : row)
                totalProb += p;
        }
        System.out.println("Matrix Sum: " + totalProb);

        Map<String, Double> markets = service.calculateMarketProbabilities(matrix);
        markets.forEach((k, v) -> System.out.println(k + ": " + v));

        // Assertions basicas
        if (Math.abs(totalProb - 1.0) > 0.001)
            throw new RuntimeException("Matrix sum invalid");
        if (markets.get("Over2.5") < 0 || markets.get("Over2.5") > 1)
            throw new RuntimeException("Over2.5 invalid");

        System.out.println("VERIFICATION SUCCESS");
    }
}
