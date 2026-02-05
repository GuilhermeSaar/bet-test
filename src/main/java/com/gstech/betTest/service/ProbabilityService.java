package com.gstech.betTest.service;

import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class ProbabilityService {

    private static final int MAX_GOALS = 10; // Limite razoável para cálculo

    /**
     * Gera a distribuição de probabilidades de gols (0 a N) usando Poisson.
     * 
     * @param lambda Média esperada de gols (xG) para o time.
     * @return Array onde o índice é o número de gols e o valor é a probabilidade.
     */
    public double[] calculateGoalProbabilities(double lambda) {
        double[] probabilities = new double[MAX_GOALS + 1];
        for (int k = 0; k <= MAX_GOALS; k++) {
            probabilities[k] = poisson(lambda, k);
        }
        return probabilities;
    }

    /**
     * Fórmula de Poisson: P(k; λ) = (e^-λ * λ^k) / k!
     */
    private double poisson(double lambda, int k) {
        return (Math.exp(-lambda) * Math.pow(lambda, k)) / factorial(k);
    }

    private long factorial(int n) {
        if (n == 0)
            return 1;
        long fact = 1;
        for (int i = 1; i <= n; i++) {
            fact *= i;
        }
        return fact;
    }

    /**
     * Gera a Matriz de Placares (Score Matrix).
     * 
     * @param homeProbs Distribuição de probabilidades do time da casa.
     * @param awayProbs Distribuição de probabilidades do time visitante.
     * @return Matriz[golsCasa][golsVisitante] = Probabilidade do placar exato.
     */
    public double[][] generateScoreMatrix(double[] homeProbs, double[] awayProbs) {
        double[][] scoreMatrix = new double[homeProbs.length][awayProbs.length];
        for (int i = 0; i < homeProbs.length; i++) {
            for (int j = 0; j < awayProbs.length; j++) {
                // Eventos independentes: P(A e B) = P(A) * P(B)
                scoreMatrix[i][j] = homeProbs[i] * awayProbs[j];
            }
        }
        return scoreMatrix;
    }

    /**
     * Calcula probabilidades de mercados a partir da matriz de placares.
     * 
     * @param scoreMatrix A matriz de probabilidades de placares exatos.
     * @return Map contendo probabilidades para Over2.5, BTTS, HomeWin, Draw,
     *         AwayWin.
     */
    public Map<String, Double> calculateMarketProbabilities(double[][] scoreMatrix) {
        double probOver25 = 0.0;
        double probBTTS = 0.0;
        double probHomeWin = 0.0;
        double probDraw = 0.0;
        double probAwayWin = 0.0;

        for (int i = 0; i < scoreMatrix.length; i++) {
            for (int j = 0; j < scoreMatrix[i].length; j++) {
                double probScore = scoreMatrix[i][j];

                // Over 2.5 (Soma de gols > 2.5)
                if ((i + j) > 2.5) {
                    probOver25 += probScore;
                }

                // BTTS (Ambos marcam: cada time > 0)
                if (i > 0 && j > 0) {
                    probBTTS += probScore;
                }

                // 1X2
                if (i > j) {
                    probHomeWin += probScore;
                } else if (i == j) {
                    probDraw += probScore;
                } else {
                    probAwayWin += probScore;
                }
            }
        }

        Map<String, Double> markets = new HashMap<>();
        markets.put("Over2.5", probOver25);
        markets.put("BTTS", probBTTS);
        markets.put("HomeWin", probHomeWin);
        markets.put("Draw", probDraw);
        markets.put("AwayWin", probAwayWin);

        return markets;
    }
}
