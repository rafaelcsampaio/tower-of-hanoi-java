package io.github.rafaelcsampaio.hanoi.ui;

import io.github.rafaelcsampaio.hanoi.game.GameStats;
import io.github.rafaelcsampaio.hanoi.game.HanoiGame;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Dimension;

import static io.github.rafaelcsampaio.hanoi.ui.theme.HanoiTheme.BACKGROUND_COLOR;

public class HanoiFrame extends JFrame {

    private static final int WINDOW_WIDTH = 900;
    private static final int WINDOW_HEIGHT = 650;

    private final HanoiGame game;
    private final GamePanel gamePanel;
    private final HeaderPanel headerPanel;

    public HanoiFrame() {
        int diskCount =
                requestDiskCount();

        game =
                new HanoiGame(
                        diskCount
                );

        setTitle(
                "Tower of Hanoi"
        );

        setSize(
                WINDOW_WIDTH,
                WINDOW_HEIGHT
        );

        setMinimumSize(
                new Dimension(
                        760,
                        560
                )
        );

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        setLocationRelativeTo(null);

        getContentPane().setLayout(
                new BorderLayout()
        );

        getContentPane().setBackground(
                BACKGROUND_COLOR
        );

        headerPanel =
                new HeaderPanel(
                        this::restartGame
                );

        gamePanel =
                new GamePanel(
                        game,
                        this::updateStats,
                        this::showVictoryDialog
                );

        add(
                headerPanel,
                BorderLayout.NORTH
        );

        add(
                gamePanel,
                BorderLayout.CENTER
        );

        updateStats();
    }

    private int requestDiskCount() {
        while (true) {
            String input =
                    JOptionPane.showInputDialog(
                            this,
                            "Number of disks (1 to 6):",
                            "New Game",
                            JOptionPane.QUESTION_MESSAGE
                    );

            if (input == null) {
                System.exit(0);
            }

            try {
                int diskCount =
                        Integer.parseInt(
                                input.trim()
                        );

                if (
                        diskCount >= 1
                                && diskCount <= 6
                ) {
                    return diskCount;
                }

                JOptionPane.showMessageDialog(
                        this,
                        "Choose a number of disks between 1 and 6.",
                        "Invalid disk count",
                        JOptionPane.WARNING_MESSAGE
                );

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(
                        this,
                        "Enter an integer between 1 and 6.",
                        "Invalid input",
                        JOptionPane.WARNING_MESSAGE
                );
            }
        }
    }

    private void restartGame() {
        game.reset();

        gamePanel.resetInteraction();

        updateStats();
    }

    private void updateStats() {
        headerPanel.updateStats(
                game.getStats()
        );
    }

    private void showVictoryDialog() {
        GameStats stats =
                game.getStats();

        String message =
                "Puzzle completed!\n\n"
                        + "Moves: "
                        + stats.validMoves()
                        + "\nMinimum moves: "
                        + stats.minimumMoves()
                        + "\nInvalid attempts: "
                        + stats.invalidMoves()
                        + "\nExtra moves: "
                        + stats.extraMoves();

        JOptionPane.showMessageDialog(
                this,
                message,
                "Victory!",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(
                () ->
                        new HanoiFrame()
                                .setVisible(true)
        );
    }
}