package com.gstech.betTest.utils;

import com.gstech.betTest.model.TeamStats;

import java.util.List;

public class Calculations {


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

    // Poisson Probability for matches with 0, 1, 2 goals. Over 2.5 = 1 - (Prob(0) +
    // Prob(1) + Prob(2))
    public static double calculatePoissonOver25(double lambda) {

        double p0 = Math.exp(-lambda); // k=0
        double p1 = Math.exp(-lambda) * lambda; // k=1
        double p2 = Math.exp(-lambda) * Math.pow(lambda, 2) / 2.0; // k=2
        return 1.0 - (p0 + p1 + p2);
    }

}
