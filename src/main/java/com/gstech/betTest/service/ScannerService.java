package com.gstech.betTest.service;

import com.gstech.betTest.dto.ScannerRequest;
import com.gstech.betTest.dto.ScannerResponse;
import com.gstech.betTest.model.TeamStats;
import com.gstech.betTest.utils.Calculations;
import org.springframework.stereotype.Service;

@Service
public class ScannerService {

    private final ProbabilityService probabilityService;

    public ScannerService(ProbabilityService probabilityService) {
        this.probabilityService = probabilityService;
    }

    public ScannerResponse analyzeMatch(ScannerRequest request) {

        // estatistica de time casa e visitante
        TeamStats homeStats = Calculations.calculateStats(request.getHomeLastMatches());
        TeamStats awayStats = Calculations.calculateStats(request.getAwayLastMatches());

        // calculo expected goals individuais (Cruzamento: Ataque x Defesa adversaria)
        // Usando média geométrica: sqrt(ataque * defesa_adversaria)
        double homeExpectedGoals = Calculations.expectedTotalGoals(homeStats.avgScored(), awayStats.avgConceded());
        double awayExpectedGoals = Calculations.expectedTotalGoals(awayStats.avgScored(), homeStats.avgConceded());

        // Total esperado (para registro e compatibilidade)
        double totalExpectedGoals = homeExpectedGoals + awayExpectedGoals;

        // 1. Gerar distribuições de Poisson (0-10 gols)
        double[] homeProbs = probabilityService.calculateGoalProbabilities(homeExpectedGoals);
        double[] awayProbs = probabilityService.calculateGoalProbabilities(awayExpectedGoals);

        // 2. Criar Matriz de Placares
        double[][] scoreMatrix = probabilityService.generateScoreMatrix(homeProbs, awayProbs);

        // 3. Calcular Probabilidades de Mercado (Over2.5, BTTS)
        java.util.Map<String, Double> markets = probabilityService.calculateMarketProbabilities(scoreMatrix);
        double probOver25Poisson = markets.get("Over2.5");

        // Probabilidade de gols individuais (derivado do Poisson array)
        // homeProbs[0] = chance de 0 gols. Over 0.5 = 1 - probs[0]
        double homeOver05 = 1.0 - homeProbs[0];
        double homeOver15 = 1.0 - (homeProbs[0] + homeProbs[1]);

        double awayOver05 = 1.0 - awayProbs[0];
        double awayOver15 = 1.0 - (awayProbs[0] + awayProbs[1]);

        // Probabilidade Combinada (Over 2.5 Geral)
        double resultFinalProbability = Calculations.finalProbability(probOver25Poisson, homeStats.overPercent(),
                awayStats.overPercent());

        // definir a resposta
        double finalProbabilityForResponse = Calculations.round(resultFinalProbability * 100, 1);

        // calcular odd justa
        double fairOdd = 1.0 / resultFinalProbability;
        if (resultFinalProbability == 0)
            fairOdd = 99.0; // evitar infinito
        double fairOddForResponse = Calculations.round(fairOdd, 2);

        // checar valor de aposta
        double marketOdd = request.getMarketOddOver25();

        // filtro para verificar se a aposta é de valor ou não
        boolean isRiskyMatch = Calculations.isRiskyMatch(totalExpectedGoals, homeStats.overPercent(),
                awayStats.overPercent());
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
                totalExpectedGoals,
                finalProbabilityForResponse,
                fairOddForResponse,
                marketOdd,
                isValue,
                isValue ? (edge > 0.15 ? "Alta" : edge > 0.05 ? "Média" : "Baixa") : "Mínima",
                Calculations.round(markets.get("BTTS") * 100, 1),
                Calculations.round(homeOver05 * 100, 1),
                Calculations.round(homeOver15 * 100, 1),
                Calculations.round(awayOver05 * 100, 1),
                Calculations.round(awayOver15 * 100, 1));
    }
}
