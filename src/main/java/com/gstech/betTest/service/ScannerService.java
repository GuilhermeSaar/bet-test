package com.gstech.betTest.service;

import com.gstech.betTest.dto.ScannerRequest;
import com.gstech.betTest.dto.ScannerResponse;
import com.gstech.betTest.model.TeamStats;
import com.gstech.betTest.utils.Calculations;
import org.springframework.stereotype.Service;

@Service
public class ScannerService {

    private final ProbabilityService probabilityService;
    private final com.gstech.betTest.repository.SavedBetRepository savedBetRepository;

    public ScannerService(ProbabilityService probabilityService,
            com.gstech.betTest.repository.SavedBetRepository savedBetRepository) {
        this.probabilityService = probabilityService;
        this.savedBetRepository = savedBetRepository;
    }

    public com.gstech.betTest.model.SavedBet saveBet(com.gstech.betTest.model.SavedBet bet) {
        return savedBetRepository.save(bet);
    }

    public java.util.List<com.gstech.betTest.model.SavedBet> getAllSavedBets() {
        return savedBetRepository.findAll();
    }

    public void deleteBet(Long id) {
        savedBetRepository.deleteById(id);
    }

    public com.gstech.betTest.model.SavedBet updateMatchResult(Long id, String finalScore) {
        com.gstech.betTest.model.SavedBet bet = savedBetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aposta não encontrada"));

        bet.setFinalScore(finalScore);

        try {
            // Parse score "2-1"
            String[] goals = finalScore.split("-");
            int total = Integer.parseInt(goals[0].trim()) + Integer.parseInt(goals[1].trim());

            // Regra Over 1.5 — WIN com 2 ou mais gols no total
            if (total > 1) {
                bet.setResultOutcome("WIN");
            } else {
                bet.setResultOutcome("LOSS");
            }
        } catch (Exception e) {
            bet.setResultOutcome("ERRO");
        }

        return savedBetRepository.save(bet);
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

        // 3. Calcular Probabilidades de Mercado (Over1.5, BTTS)
        java.util.Map<String, Double> markets = probabilityService.calculateMarketProbabilities(scoreMatrix);
        double probOver15Poisson = markets.get("Over1.5");

        // Probabilidade de gols individuais (derivado do Poisson array)
        // homeProbs[0] = chance de 0 gols. Over 0.5 = 1 - probs[0]
        double homeOver05 = 1.0 - homeProbs[0];
        double homeOver15 = 1.0 - (homeProbs[0] + homeProbs[1]);

        double awayOver05 = 1.0 - awayProbs[0];
        double awayOver15 = 1.0 - (awayProbs[0] + awayProbs[1]);

        // Probabilidade Combinada (Over 1.5 Geral)
        double resultFinalProbability = Calculations.finalProbability(probOver15Poisson, homeStats.overPercent(),
                awayStats.overPercent());

        // definir a resposta
        double finalProbabilityForResponse = Calculations.round(resultFinalProbability * 100, 1);

        // calcular odd justa
        double fairOdd = 1.0 / resultFinalProbability;
        if (resultFinalProbability == 0)
            fairOdd = 99.0; // evitar infinito
        double fairOddForResponse = Calculations.round(fairOdd, 2);

        // checar valor de aposta
        double marketOdd = request.getMarketOddOver15();

        // filtro para verificar se a aposta é de valor ou não
        boolean isRiskyMatch = Calculations.isRiskyMatch(totalExpectedGoals, homeStats.overPercent(),
                awayStats.overPercent());
        // considerar uma aposta de valor apenas se nao for uma partida arriscada
        boolean isValue = !isRiskyMatch && (request.getMarketOddOver15() > fairOdd); // ex: 1.60 > 1.30

        // nivel de confiança baseado na margem de valor
        double edge = (request.getMarketOddOver15() - fairOdd) / fairOdd;

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
