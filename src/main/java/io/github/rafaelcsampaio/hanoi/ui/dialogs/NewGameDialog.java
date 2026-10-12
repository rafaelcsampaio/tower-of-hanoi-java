package io.github.rafaelcsampaio.hanoi.ui.dialogs;

import io.github.rafaelcsampaio.hanoi.ui.components.RoundedButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;

import static io.github.rafaelcsampaio.hanoi.ui.theme.HanoiTheme.*;

public class NewGameDialog extends JDialog {

    private static final int MIN_DISKS = 1;
    private static final int MAX_DISKS = 6;
    private static final int DEFAULT_DISKS = 3;

    private static final Dimension ACTION_BUTTON_SIZE =
            new Dimension(100, 38);

    private static final Dimension DISK_BUTTON_SIZE =
            new Dimension(56, 40);

    private static final Color DISK_BUTTON_BACKGROUND =
            new Color(250, 248, 244);

    private static final Color DISK_BUTTON_BORDER =
            new Color(214, 202, 192);

    private static final Color DISK_BUTTON_SELECTED_BACKGROUND =
            new Color(194, 132, 84);

    private static final Color DISK_BUTTON_SELECTED_BORDER =
            new Color(120, 74, 43);

    private final List<DiskOptionButton> diskButtons =
            new ArrayList<>();

    private int selectedDiskCount =
            DEFAULT_DISKS;

    private boolean confirmed;

    private NewGameDialog(
            Window owner
    ) {
        super(
                owner,
                "New Game",
                Dialog.ModalityType.APPLICATION_MODAL
        );

        setDefaultCloseOperation(
                DISPOSE_ON_CLOSE
        );

        setResizable(false);

        JPanel content =
                new JPanel();

        content.setBackground(
                BACKGROUND_COLOR
        );

        content.setBorder(
                new EmptyBorder(
                        28,
                        32,
                        26,
                        32
                )
        );

        content.setLayout(
                new BoxLayout(
                        content,
                        BoxLayout.Y_AXIS
                )
        );

        content.add(
                createTitleSection()
        );

        content.add(
                Box.createVerticalStrut(26)
        );

        content.add(
                createDiskSelectionSection()
        );

        content.add(
                Box.createVerticalStrut(28)
        );

        content.add(
                createActionSection()
        );

        setContentPane(
                content
        );

        getRootPane().registerKeyboardAction(
                e -> cancel(),
                KeyStroke.getKeyStroke(
                        KeyEvent.VK_ESCAPE,
                        0
                ),
                JComponent.WHEN_IN_FOCUSED_WINDOW
        );

        pack();

        setSize(
                455,
                getHeight()
        );

        setLocationRelativeTo(
                owner
        );
    }

    private JPanel createTitleSection() {
        JPanel panel =
                new JPanel();

        panel.setOpaque(false);

        panel.setLayout(
                new BoxLayout(
                        panel,
                        BoxLayout.Y_AXIS
                )
        );

        panel.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        JLabel title =
                new JLabel(
                        "Start a new game"
                );

        title.setFont(
                DIALOG_TITLE_FONT
        );

        title.setForeground(
                HEADER_TITLE_COLOR
        );

        title.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        JLabel description =
                new JLabel(
                        "Choose how many disks you want to play with."
                );

        description.setFont(
                DIALOG_TEXT_FONT
        );

        description.setForeground(
                HEADER_SUBTITLE_COLOR
        );

        description.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        panel.add(
                title
        );

        panel.add(
                Box.createVerticalStrut(6)
        );

        panel.add(
                description
        );

        return panel;
    }

    private JPanel createDiskSelectionSection() {
        JPanel section =
                new JPanel();

        section.setOpaque(false);

        section.setLayout(
                new BoxLayout(
                        section,
                        BoxLayout.Y_AXIS
                )
        );

        section.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        JLabel label =
                new JLabel(
                        "Number of disks"
                );

        label.setFont(
                DIALOG_TEXT_FONT.deriveFont(
                        Font.BOLD
                )
        );

        label.setForeground(
                TEXT_COLOR
        );

        label.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        JLabel helper =
                new JLabel(
                        "Choose from 1 to 6 disks"
                );

        helper.setFont(
                DIALOG_TEXT_FONT
        );

        helper.setForeground(
                HEADER_SUBTITLE_COLOR
        );

        helper.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        JPanel buttonsPanel =
                new JPanel(
                        new GridLayout(
                                1,
                                MAX_DISKS,
                                8,
                                0
                        )
                );

        buttonsPanel.setOpaque(false);

        buttonsPanel.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        ButtonGroup group =
                new ButtonGroup();

        for (
                int diskCount = MIN_DISKS;
                diskCount <= MAX_DISKS;
                diskCount++
        ) {
            DiskOptionButton button =
                    createDiskButton(
                            diskCount
                    );

            group.add(
                    button
            );

            buttonsPanel.add(
                    button
            );

            diskButtons.add(
                    button
            );
        }

        updateDiskButtonStyles();

        JPanel difficultyPanel =
                new JPanel(
                        new BorderLayout()
                );

        difficultyPanel.setOpaque(false);

        difficultyPanel.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        JLabel easiest =
                new JLabel(
                        "Easiest"
                );

        easiest.setFont(
                DIALOG_TEXT_FONT.deriveFont(
                        11f
                )
        );

        easiest.setForeground(
                HEADER_SUBTITLE_COLOR
        );

        JLabel hardest =
                new JLabel(
                        "Hardest"
                );

        hardest.setFont(
                DIALOG_TEXT_FONT.deriveFont(
                        11f
                )
        );

        hardest.setForeground(
                HEADER_SUBTITLE_COLOR
        );

        difficultyPanel.add(
                easiest,
                BorderLayout.WEST
        );

        difficultyPanel.add(
                hardest,
                BorderLayout.EAST
        );

        section.add(
                label
        );

        section.add(
                Box.createVerticalStrut(3)
        );

        section.add(
                helper
        );

        section.add(
                Box.createVerticalStrut(12)
        );

        section.add(
                buttonsPanel
        );

        section.add(
                Box.createVerticalStrut(7)
        );

        section.add(
                difficultyPanel
        );

        return section;
    }

    private DiskOptionButton createDiskButton(
            int diskCount
    ) {
        DiskOptionButton button =
                new DiskOptionButton(
                        String.valueOf(
                                diskCount
                        )
                );

        button.setPreferredSize(
                DISK_BUTTON_SIZE
        );

        button.setMinimumSize(
                DISK_BUTTON_SIZE
        );

        button.setMaximumSize(
                DISK_BUTTON_SIZE
        );

        if (
                diskCount == DEFAULT_DISKS
        ) {
            button.setSelected(true);
        }

        button.addActionListener(
                e -> {
                    selectedDiskCount =
                            diskCount;

                    updateDiskButtonStyles();
                }
        );

        return button;
    }

    private void updateDiskButtonStyles() {
        for (
                int i = 0;
                i < diskButtons.size();
                i++
        ) {
            DiskOptionButton button =
                    diskButtons.get(i);

            boolean selected =
                    i + 1 == selectedDiskCount;

            button.setSelectedStyle(
                    selected
            );
        }
    }

    private JPanel createActionSection() {
        JPanel panel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                10,
                                0
                        )
                );

        panel.setOpaque(false);

        panel.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        JButton cancelButton =
                new JButton(
                        "Cancel"
                );

        cancelButton.setPreferredSize(
                ACTION_BUTTON_SIZE
        );

        cancelButton.setMinimumSize(
                ACTION_BUTTON_SIZE
        );

        cancelButton.setFont(
                DIALOG_TEXT_FONT
        );

        cancelButton.setFocusPainted(
                false
        );

        cancelButton.putClientProperty(
                "JButton.buttonType",
                "roundRect"
        );

        cancelButton.putClientProperty(
                "JComponent.roundRect",
                true
        );

        cancelButton.addActionListener(
                e -> cancel()
        );

        RoundedButton startButton =
                new RoundedButton(
                        "Start"
                );

        startButton.setPreferredSize(
                ACTION_BUTTON_SIZE
        );

        startButton.setMinimumSize(
                ACTION_BUTTON_SIZE
        );

        startButton.setMaximumSize(
                ACTION_BUTTON_SIZE
        );

        startButton.addActionListener(
                e -> confirm()
        );

        getRootPane().setDefaultButton(
                startButton
        );

        panel.add(
                cancelButton
        );

        panel.add(
                startButton
        );

        return panel;
    }

    private void confirm() {
        confirmed =
                true;

        dispose();
    }

    private void cancel() {
        confirmed =
                false;

        dispose();
    }

    public static Integer showDialog(
            Window owner
    ) {
        NewGameDialog dialog =
                new NewGameDialog(
                        owner
                );

        dialog.setVisible(
                true
        );

        if (
                !dialog.confirmed
        ) {
            return null;
        }

        return dialog.selectedDiskCount;
    }

    private static final class DiskOptionButton
            extends JToggleButton {

        private boolean selectedStyle;

        private DiskOptionButton(
                String text
        ) {
            super(text);

            setFont(
                    DIALOG_TEXT_FONT.deriveFont(
                            Font.BOLD
                    )
            );

            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setOpaque(false);

            setCursor(
                    Cursor.getPredefinedCursor(
                            Cursor.HAND_CURSOR
                    )
            );

            setHorizontalAlignment(
                    SwingConstants.CENTER
            );
        }

        private void setSelectedStyle(
                boolean selected
        ) {
            this.selectedStyle =
                    selected;

            setForeground(
                    selected
                            ? Color.WHITE
                            : TEXT_COLOR
            );

            repaint();
        }

        @Override
        protected void paintComponent(
                Graphics graphics
        ) {
            Graphics2D g2 =
                    (Graphics2D) graphics.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            int width =
                    getWidth();

            int height =
                    getHeight();

            int arc =
                    height - 2;

            g2.setColor(
                    selectedStyle
                            ? DISK_BUTTON_SELECTED_BACKGROUND
                            : DISK_BUTTON_BACKGROUND
            );

            g2.fillRoundRect(
                    0,
                    0,
                    width - 1,
                    height - 1,
                    arc,
                    arc
            );

            g2.setColor(
                    selectedStyle
                            ? DISK_BUTTON_SELECTED_BORDER
                            : DISK_BUTTON_BORDER
            );

            g2.setStroke(
                    new BasicStroke(
                            selectedStyle ? 2f : 1f
                    )
            );

            float inset =
                    selectedStyle ? 1f : 0.5f;

            g2.draw(
                    new RoundRectangle2D.Float(
                            inset,
                            inset,
                            width - 1f - (inset * 2f),
                            height - 1f - (inset * 2f),
                            arc,
                            arc
                    )
            );

            g2.dispose();

            super.paintComponent(
                    graphics
            );
        }
    }
}