package com.game;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GameSimulator {
    public static void simulateMatches(String filePath, int numberOfMatches) {
        try {
            Path parentDir = Paths.get(filePath).getParent();
            if (parentDir != null) {
                Files.createDirectories(parentDir);
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                writer.write("MatchId,BotType,Result,TimeMs,TotalClicks");
                writer.newLine();

                for (int i = 0; i < numberOfMatches; i++) {
                    Board board = new Board(8, 6);
                    Player bot = createBot(board);

                    GameOutcome play = GameOutcome.IN_PROGRESS;

                    long start = System.nanoTime();
                    while (play == GameOutcome.IN_PROGRESS) {
                        play = bot.playTurn();
                    }
                    long end = System.nanoTime();
                    long durationInMS = Math.round((float)(end - start) / 1_000_000);

                    writer.write("%d,%s,%b,%d,%d".formatted(
                            i,
                            "Random",
                            play == GameOutcome.VICTORY,
                            durationInMS,
                            bot.getNumberOfMoves()
                    ));
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to write simulation matches to " + filePath);
            System.getLogger(GameSimulator.class.getName())
                    .log(System.Logger.Level.ERROR, "Failed to export matches to " + filePath, e);
        }
    }

    public static Player createBot(Board board) {
        return new Player(board);
    }
}
