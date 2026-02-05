package com.gstech.betTest.utils;

import com.gstech.betTest.model.TeamStats;

import java.util.List;

public class Calculations {

    public static double finalProbability(double probabilityPoisson, double homeStatsOverPercent,
            double awayStatsOverPercent) {

        // Peso dinâmico
        double weightPoisson;
        double weightHistory;

        if (homeStatsOverPercent >= 0.60 && awayStatsOverPercent >= 0.60) {
            weightHistory = 0.65;
            weightPoisson = 0.35;
        } else {
            weightHistory = 0.50;
            weightPoisson = 0.50;
        }

        double avgHistoricalOver = (homeStatsOverPercent + awayStatsOverPercent) / 2.0;
        double finalProbability = probabilityPoisson * weightPoisson + avgHistoricalOver * weightHistory;
        return Math.max(finalProbability, 0.01);
    }

    public static boolean isRiskyMatch(double expectedGoals, double homeOverPercent, double awayOverPercent) {
        return expectedGoals < 2.50 ||
                homeOverPercent < 0.50 ||
                awayOverPercent < 0.50;
    }

    // Calcula o Expected Goals usando média geométrica (Ataque x Defesa).
    public static double expectedTotalGoals(double avgScored, double avgConceded) {
        // Garante valores mínimos para evitar zero
        double safeScored = Math.max(avgScored, 0.1);
        double safeConceded = Math.max(avgConceded, 0.1);

        // Média geométrica com redutor conservador leve (0.95)
        double expected = Math.sqrt(safeScored * safeConceded) * 0.95;
        return Calculations.round(expected, 2);
    }

    // pega os placares de um time, calcula médias de gols feitos, sofridos e a
    // porcentagem de jogos com mais de 2.5 gols
    public static TeamStats calculateStats(List<String> matches) {

        if (matches == null || matches.isEmpty()) {

            return new TeamStats(0, 0, 0);
        }

        double totalScored = 0;
        double totalConceded = 0;
        int overCount = 0;

        for (String match : matches) {

            try {

                String[] parts = match.split("-");
                double scored = Double.parseDouble(parts[0].trim());
                double conceded = Double.parseDouble(parts[1].trim());

                totalScored += scored;
                totalConceded += conceded;

                if ((scored + conceded) > 2.5) {
                    overCount++;
                }

            } catch (Exception e) {
                System.err.println("Invalid score format: " + match);
            }
        }

        double avgScored = totalScored / matches.size();
        double avgConceded = totalConceded / matches.size();
        double overPercent = (double) overCount / matches.size();

        return new TeamStats(round(avgScored, 2), round(avgConceded, 2), overPercent);
    }

    public static double round(double value, int places) {
        if (places < 0)
            throw new IllegalArgumentException();
        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

}
