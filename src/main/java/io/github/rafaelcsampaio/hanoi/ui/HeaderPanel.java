package io.github.rafaelcsampaio.hanoi.ui;

import io.github.rafaelcsampaio.hanoi.game.GameStats;
import io.github.rafaelcsampaio.hanoi.ui.components.RoundedButton;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;

import static io.github.rafaelcsampaio.hanoi.ui.theme.HanoiTheme.HEADER_BACKGROUND_COLOR;
import static io.github.rafaelcsampaio.hanoi.ui.theme.HanoiTheme.MUTED_TEXT_COLOR;
import static io.github.rafaelcsampaio.hanoi.ui.theme.HanoiTheme.TEXT_COLOR;

public class HeaderPanel extends JPanel {

    private final JLabel minimumMovesLabel;
    private final JLabel movesLabel;
    private final JLabel invalidMovesLabel;

    public HeaderPanel(Runnable restartAction) {
        super(
                new BorderLayout(
                        24,
                        0
                )
        );

        setBackground(
                HEADER_BACKGROUND_COLOR
        );

        setBorder(
                new EmptyBorder(
                        18,
                        28,
                        18,
                        28
                )
        );

        minimumMovesLabel =
                createMetricLabel();

        movesLabel =
                createMetricLabel();

        invalidMovesLabel =
                createMetricLabel();

        add(
                createTitlePanel(),
                BorderLayout.WEST
        );

        add(
                createControlsPanel(
                        restartAction
                ),
                BorderLayout.EAST
        );
    }

    private JPanel createTitlePanel() {
        JPanel titlePanel =
                new JPanel();

        titlePanel.setOpaque(false);

        titlePanel.setLayout(
                new BoxLayout(
                        titlePanel,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel title =
                new JLabel(
                        "Tower of Hanoi"
                );

        title.setForeground(
                TEXT_COLOR
        );

        title.setFont(
                new Font(
                        Font.SANS_SERIF,
                        Font.BOLD,
                        24
                )
        );

        JLabel subtitle =
                new JLabel(
                        "Move the tower from START to GOAL"
                );

        subtitle.setForeground(
                MUTED_TEXT_COLOR
        );

        subtitle.setFont(
                new Font(
                        Font.SANS_SERIF,
                        Font.PLAIN,
                        13
                )
        );

        titlePanel.add(title);

        titlePanel.add(
                Box.createVerticalStrut(3)
        );

        titlePanel.add(subtitle);

        return titlePanel;
    }

    private JPanel createControlsPanel(
            Runnable restartAction
    ) {
        JPanel controlsPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                18,
                                6
                        )
                );

        controlsPanel.setOpaque(false);

        controlsPanel.add(
                minimumMovesLabel
        );

        controlsPanel.add(
                movesLabel
        );

        controlsPanel.add(
                invalidMovesLabel
        );

        RoundedButton restartButton =
                new RoundedButton(
                        "Restart"
                );

        restartButton.addActionListener(
                e -> restartAction.run()
        );

        controlsPanel.add(
                restartButton
        );

        return controlsPanel;
    }

    private JLabel createMetricLabel() {
        JLabel label =
                new JLabel();

        label.setForeground(
                TEXT_COLOR
        );

        label.setFont(
                new Font(
                        Font.SANS_SERIF,
                        Font.BOLD,
                        12
                )
        );

        return label;
    }

    public void updateStats(
            GameStats stats
    ) {
        minimumMovesLabel.setText(
                "Minimum  "
                        + stats.minimumMoves()
        );

        movesLabel.setText(
                "Moves  "
                        + stats.validMoves()
        );

        invalidMovesLabel.setText(
                "Invalid  "
                        + stats.invalidMoves()
        );
    }
}