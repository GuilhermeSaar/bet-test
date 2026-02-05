package com.gstech.betTest;

import com.gstech.betTest.service.ProbabilityService;
import org.junit.jupiter.api.Test;
import java.util.Map;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;

public class ProbabilityServiceTest {

    @Test
    public void testProbabilityCalculation() {
        ProbabilityService service = new ProbabilityService();

        // Exemplo: Home 1.5, Away 1.2
        double lambdaHome = 1.5;
        double lambdaAway = 1.2;

        System.out.println("Testing with Home=" + lambdaHome + ", Away=" + lambdaAway);

        double[] homeProbs = service.calculateGoalProbabilities(lambdaHome);
        double[] awayProbs = service.calculateGoalProbabilities(lambdaAway);

        double sumHome = Arrays.stream(homeProbs).sum();
        double sumAway = Arrays.stream(awayProbs).sum();

        System.out.println("Home Probs Sum: " + sumHome);
        assertEquals(1.0, sumHome, 0.001, "Home probs should sum to ~1");
        assertEquals(1.0, sumAway, 0.001, "Away probs should sum to ~1");

        double[][] matrix = service.generateScoreMatrix(homeProbs, awayProbs);

        double totalProb = 0;
        for (double[] row : matrix) {
            for (double p : row)
                totalProb += p;
        }
        System.out.println("Matrix Sum: " + totalProb);
        assertEquals(1.0, totalProb, 0.001, "Matrix sum should be ~1");

        Map<String, Double> markets = service.calculateMarketProbabilities(matrix);
        markets.forEach((k, v) -> System.out.println(k + ": " + v));

        assertTrue(markets.containsKey("Over2.5"));
        assertTrue(markets.containsKey("BTTS"));
        assertTrue(markets.containsKey("HomeWin"));

        double probOver25 = markets.get("Over2.5");
        assertTrue(probOver25 >= 0 && probOver25 <= 1, "Probability must be valid");
    }
}
