package com.gstech.betTest;

import com.gstech.betTest.utils.Calculations;
import com.gstech.betTest.model.TeamStats;
import com.gstech.betTest.service.ProbabilityService;
import java.util.List;
import java.util.Map;

/**
 * Teste de verificação do cálculo expectedTotalGoals com média geométrica.
 * Compara os resultados com valores esperados.
 */
public class ExpectedGoalsVerify {
    public static void main(String[] args) {
        System.out.println("=== Verificação: expectedTotalGoals com Média Geométrica ===\n");

        ProbabilityService probabilityService = new ProbabilityService();

        // --- Cenário 1: Time Ofensivo vs Time Fraco Defensivamente ---
        System.out.println("Cenário 1: Time A (ataque forte) vs Time B (defesa fraca)");
        double homeAttack = 2.0;
        double awayDefense = 1.5;
        double homeXG = Calculations.expectedTotalGoals(homeAttack, awayDefense);
        System.out.println("  Home Attack: " + homeAttack + " gols/jogo");
        System.out.println("  Away Defense: " + awayDefense + " gols sofridos/jogo");
        System.out.println("  Home xG (média geométrica): " + homeXG);
        System.out.println("  Esperado sqrt(2.0 * 1.5) = " + String.format("%.2f", Math.sqrt(2.0 * 1.5)));

        // Cenário inverso
        double awayAttack = 0.8;
        double homeDefense = 1.0;
        double awayXG = Calculations.expectedTotalGoals(awayAttack, homeDefense);
        System.out.println("\n  Away Attack: " + awayAttack + " gols/jogo");
        System.out.println("  Home Defense: " + homeDefense + " gols sofridos/jogo");
        System.out.println("  Away xG (média geométrica): " + awayXG);
        System.out.println("  Esperado sqrt(0.8 * 1.0) = " + String.format("%.2f", Math.sqrt(0.8 * 1.0)));

        double totalXG = homeXG + awayXG;
        System.out.println("\n  TOTAL xG: " + String.format("%.2f", totalXG));
        System.out.println("  (Antes com redutor 0.85: ~2.12, Agora: ~" + String.format("%.2f", totalXG) + ")");

        // --- Cenário 2: Dados Reais Simulados (5 jogos) ---
        System.out.println("\n\n=== Cenário 2: Teste com Placares Reais ===");

        // Time A: 3-1, 2-2, 4-0, 1-1, 2-1
        List<String> homeMatches = List.of("3-1", "2-2", "4-0", "1-1", "2-1");
        TeamStats homeStats = Calculations.calculateStats(homeMatches);
        System.out.println("\nTime Casa (placares: 3-1, 2-2, 4-0, 1-1, 2-1):");
        System.out.println("  Média GP: " + homeStats.avgScored());
        System.out.println("  Média GS: " + homeStats.avgConceded());
        System.out.println("  Over %: " + (homeStats.overPercent() * 100) + "%");

        // Time B: 1-2, 0-1, 2-3, 1-0, 0-2
        List<String> awayMatches = List.of("1-2", "0-1", "2-3", "1-0", "0-2");
        TeamStats awayStats = Calculations.calculateStats(awayMatches);
        System.out.println("\nTime Visitante (placares: 1-2, 0-1, 2-3, 1-0, 0-2):");
        System.out.println("  Média GP: " + awayStats.avgScored());
        System.out.println("  Média GS: " + awayStats.avgConceded());
        System.out.println("  Over %: " + (awayStats.overPercent() * 100) + "%");

        // Cruzamento
        double homeExpected = Calculations.expectedTotalGoals(homeStats.avgScored(), awayStats.avgConceded());
        double awayExpected = Calculations.expectedTotalGoals(awayStats.avgScored(), homeStats.avgConceded());
        double totalExpected = homeExpected + awayExpected;

        System.out.println("\n--- Expected Goals Calculados ---");
        System.out.println("  Home xG (ataque casa x defesa visitante): " + homeExpected);
        System.out.println("  Away xG (ataque visitante x defesa casa): " + awayExpected);
        System.out.println("  TOTAL xG: " + String.format("%.2f", totalExpected));

        // Gerar Poisson e matriz
        double[] homeProbs = probabilityService.calculateGoalProbabilities(homeExpected);
        double[] awayProbs = probabilityService.calculateGoalProbabilities(awayExpected);
        double[][] matrix = probabilityService.generateScoreMatrix(homeProbs, awayProbs);
        Map<String, Double> markets = probabilityService.calculateMarketProbabilities(matrix);

        System.out.println("\n--- Probabilidades de Mercado (Poisson Puro) ---");
        System.out.println("  Over 2.5: " + String.format("%.1f%%", markets.get("Over2.5") * 100));
        System.out.println("  BTTS: " + String.format("%.1f%%", markets.get("BTTS") * 100));
        System.out.println("  Home Win: " + String.format("%.1f%%", markets.get("HomeWin") * 100));
        System.out.println("  Draw: " + String.format("%.1f%%", markets.get("Draw") * 100));
        System.out.println("  Away Win: " + String.format("%.1f%%", markets.get("AwayWin") * 100));

        // Odd justa Over 2.5
        double fairOdd = 1.0 / markets.get("Over2.5");
        System.out.println("\n  Odd Justa Over 2.5: " + String.format("%.2f", fairOdd));

        // --- Validação ---
        System.out.println("\n\n=== VALIDAÇÃO ===");
        boolean valid = true;

        if (totalExpected < 1.5 || totalExpected > 5.0) {
            System.out.println("❌ Total xG fora do intervalo esperado (1.5-5.0)");
            valid = false;
        } else {
            System.out.println("✅ Total xG dentro do intervalo esperado");
        }

        if (markets.get("Over2.5") > 0.3 && markets.get("Over2.5") < 0.85) {
            System.out.println(
                    "✅ Prob Over 2.5 realista (" + String.format("%.1f%%", markets.get("Over2.5") * 100) + ")");
        } else {
            System.out.println("⚠️ Prob Over 2.5 pode estar fora do normal");
        }

        if (valid) {
            System.out.println("\n✅ VERIFICAÇÃO CONCLUÍDA COM SUCESSO!");
        }
    }
}
