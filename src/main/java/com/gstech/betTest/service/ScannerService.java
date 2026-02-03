package com.gstech.betTest.service;

import com.gstech.betTest.dto.ScannerRequest;
import com.gstech.betTest.dto.ScannerResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScannerService {

    public ScannerResponse analyzeMatch(ScannerRequest request) {
        ScannerResponse response = new ScannerResponse();

        // estatistica de time casa
        TeamStats homeStats = calculateStats(request.getHomeLastMatches());
        response.setHomeAvgScored(homeStats.avgScored);  // ex: 1.8
        response.setHomeAvgConceded(homeStats.avgConceded); // ex: 1.2
        response.setHomeOverPercent(homeStats.overPercent); // ex: 0.6 (60%)

        // estatistica de time visitante
        TeamStats awayStats = calculateStats(request.getAwayLastMatches());
        response.setAwayAvgScored(awayStats.avgScored);
        response.setAwayAvgConceded(awayStats.avgConceded);
        response.setAwayOverPercent(awayStats.overPercent);

        // calculo expected total goals
        double xGHome = (homeStats.avgScored + awayStats.avgConceded) / 2.0; // ex: (1.8 + 1.3) / 2 = 1.55
        double xGAway = (awayStats.avgScored + homeStats.avgConceded) / 2.0; // ex: (1.5 + 1.2) / 2 = 1.35
        double expectedTotalGoals = xGHome + xGAway; // ex: 1.55 + 1.35 = 2.9
        response.setExpectedTotalGoals(round(expectedTotalGoals, 2)); // ex: 2.9

        // filtro para verificar se a aposta é de valor ou não
        boolean isRiskyMatch = expectedTotalGoals < 2.6 ||
                homeStats.overPercent < 0.50 ||
                awayStats.overPercent < 0.45;

        // calcular probabilidade de over 2.5 gols
        // metodo poisson - baseado na media de gols esperados
        double adjustedLambda = expectedTotalGoals * 0.85; // ajuste para evitar superestimação
        double probabilityPoisson = calculatePoissonOver25(adjustedLambda); // ex: 0.72

        // Historical
        double avgHistoricalOver = (homeStats.overPercent + awayStats.overPercent) / 2.0; // ex: (0.6 + 0.5) / 2 = 0.55

        // Peso dinâmico
        double weightPoisson;
        double weightHistory;

        if (homeStats.overPercent >= 0.60 && awayStats.overPercent >= 0.60) {
            weightHistory = 0.65;
            weightPoisson = 0.35;
        } else {
            weightHistory = 0.50;
            weightPoisson = 0.50;
        }

        // Probabilidade Combinada (peso de 50% para cada - heurística simples)
        double finalProbability = probabilityPoisson * weightPoisson + avgHistoricalOver * weightHistory; // ex: 0.72 * 0.5 + 0.55 * 0.5 = 0.635
        finalProbability = Math.max(finalProbability, 0.01); // evitar zero

        // definir a resposta
        response.setProbabilityOver25(round(finalProbability * 100, 1)); // ex: 63.5%

        // calcular odd justa
        double fairOdd = 1.0 / finalProbability;
        if (finalProbability == 0)
            fairOdd = 99.0; // evitar infinito
        response.setFairOddOver25(round(fairOdd, 2));

        // checar valor de aposta
        response.setMarketOdd(request.getMarketOddOver25());

        // considerar uma aposta de valor apenas se nao for uma partida arriscada
        boolean isValue = !isRiskyMatch && (request.getMarketOddOver25() > fairOdd); // ex: 2.10 > 1.57
        response.setValueBet(isValue);

        // nivel de confiança baseado na margem de valor
        double edge = (request.getMarketOddOver25() - fairOdd) / fairOdd;
        if (!isValue) {
            response.setConfidenceLevel("Mínima");
        } else if (edge > 0.15) {
            response.setConfidenceLevel("Alta");
        } else if (edge > 0.05) {
            response.setConfidenceLevel("Média");
        } else {
            response.setConfidenceLevel("Baixa");
        }

        return response;
    }

    // pega os placares de um time, calcula médias de gols feitos, sofridos e a
    // porcentagem de jogos com mais de 2.5 gols
    private TeamStats calculateStats(List<String> matches) {
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

    // Poisson Probability for matches with 0, 1, 2 goals. Over 2.5 = 1 - (Prob(0) +
    // Prob(1) + Prob(2))
    private double calculatePoissonOver25(double lambda) {

        double p0 = Math.exp(-lambda); // k=0
        double p1 = Math.exp(-lambda) * lambda; // k=1
        double p2 = Math.exp(-lambda) * Math.pow(lambda, 2) / 2.0; // k=2
        return 1.0 - (p0 + p1 + p2);
    }

    private double round(double value, int places) {
        if (places < 0)
            throw new IllegalArgumentException();
        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    private static class TeamStats {
        double avgScored;
        double avgConceded;
        double overPercent;

        public TeamStats(double avgScored, double avgConceded, double overPercent) {
            this.avgScored = avgScored;
            this.avgConceded = avgConceded;
            this.overPercent = overPercent;
        }
    }
}
