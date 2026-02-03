package com.gstech.betTest.service;

import com.gstech.betTest.dto.ScannerRequest;
import com.gstech.betTest.dto.ScannerResponse;
import com.gstech.betTest.model.TeamStats;
import com.gstech.betTest.utils.Calculations;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScannerService {

    public ScannerResponse analyzeMatch(ScannerRequest request) {

        // estatistica de time casa
        TeamStats homeStats = Calculations.calculateStats(request.getHomeLastMatches());

        // estatistica de time visitante
        TeamStats awayStats = Calculations.calculateStats(request.getAwayLastMatches());

        // calculo expected total goals
        double xGHome = (homeStats.avgScored() + awayStats.avgConceded()) / 2.0; // ex: (1.8 + 1.3) / 2 = 1.55
        double xGAway = (awayStats.avgScored() + homeStats.avgConceded()) / 2.0; // ex: (1.5 + 1.2) / 2 = 1.35
        double expectedTotalGoals = xGHome + xGAway; // ex: 1.55 + 1.35 = 2.9
        double expectedGoalsForResponse = Calculations.round(expectedTotalGoals, 2);

        // filtro para verificar se a aposta é de valor ou não
        boolean isRiskyMatch = expectedTotalGoals < 2.6 ||
                homeStats.overPercent() < 0.50 ||
                awayStats.overPercent() < 0.45;

        // calcular probabilidade de over 2.5 gols
        // metodo poisson - baseado na media de gols esperados
        double adjustedLambda = expectedTotalGoals * 0.85; // ajuste para evitar superestimação
        double probabilityPoisson = Calculations.calculatePoissonOver25(adjustedLambda); // ex: 0.72

        // Historical
        double avgHistoricalOver = (homeStats.overPercent() + awayStats.overPercent()) / 2.0; // ex: (0.6 + 0.5) / 2 = 0.55

        // Peso dinâmico
        double weightPoisson;
        double weightHistory;

        if (homeStats.overPercent() >= 0.60 && awayStats.overPercent() >= 0.60) {
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
        double finalProbabilityForResponse = Calculations.round(finalProbability * 100, 1);

        // calcular odd justa
        double fairOdd = 1.0 / finalProbability;
        if (finalProbability == 0)
            fairOdd = 99.0; // evitar infinito
        double fairOddForResponse = Calculations.round(fairOdd, 2);

        // checar valor de aposta
        double marketOdd = request.getMarketOddOver25();

        // considerar uma aposta de valor apenas se nao for uma partida arriscada
        boolean isValue = !isRiskyMatch && (request.getMarketOddOver25() > fairOdd); // ex: 2.10 > 1.57

        // nivel de confiança baseado na margem de valor
        double edge = (request.getMarketOddOver25() - fairOdd) / fairOdd;

        return new ScannerResponse(
                homeStats.avgScored(),
                homeStats.avgConceded(),
                awayStats.avgScored(),
                awayStats.avgConceded(),
                homeStats.overPercent(),
                awayStats.overPercent(),
                expectedGoalsForResponse,
                finalProbabilityForResponse,
                fairOddForResponse,
                marketOdd,
                isValue,
                isValue ? (edge > 0.15 ? "Alta" : edge > 0.05 ? "Média" : "Baixa") : "Mínima"
        );
    }
}
