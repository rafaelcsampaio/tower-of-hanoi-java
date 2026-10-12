package io.github.rafaelcsampaio.hanoi.ui;

import io.github.rafaelcsampaio.hanoi.game.HanoiGame;
import io.github.rafaelcsampaio.hanoi.game.MoveResult;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.List;

public class HanoiFrame extends JFrame {

    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;

    private static final int DISK_HEIGHT = 25;
    private static final int BASE_DISK_WIDTH = 50;
    private static final int DISK_WIDTH_STEP = 20;

    private static final int ROD_HEIGHT = 300;
    private static final int ROD_WIDTH = 10;

    private static final Color LABEL_COLOR =
            new Color(70, 70, 70);

    private static final Color BACKGROUND_COLOR =
            new Color(240, 240, 240);

    private static final Color ROD_COLOR =
            new Color(100, 70, 40);

    private static final Color BASE_COLOR =
            new Color(60, 40, 20);

    private static final Color[] DISK_COLORS = {
            new Color(231, 76, 60),
            new Color(230, 126, 34),
            new Color(241, 196, 15),
            new Color(46, 204, 113),
            new Color(52, 152, 219),
            new Color(155, 89, 182)
    };

    private final HanoiGame game;
    private final GamePanel gamePanel;

    private Integer draggedDisk = null;
    private int sourceTowerIndex = -1;
    private Point dragPoint = null;

    public HanoiFrame() {
        int diskCount = requestDiskCount();

        game = new HanoiGame(diskCount);

        setTitle("Tower of Hanoi");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        gamePanel = new GamePanel();
        add(gamePanel);

        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("Game");
        JMenuItem restartItem = new JMenuItem("Restart");

        restartItem.addActionListener(e -> restartGame());

        gameMenu.add(restartItem);
        menuBar.add(gameMenu);

        setJMenuBar(menuBar);
    }

    private int requestDiskCount() {
        while (true) {
            String input = JOptionPane.showInputDialog(
                    this,
                    "Number of disks (1 to 6):",
                    "New Game",
                    JOptionPane.QUESTION_MESSAGE
            );

            if (input == null) {
                System.exit(0);
            }

            try {
                int diskCount = Integer.parseInt(input.trim());

                if (diskCount >= 1 && diskCount <= 6) {
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
        clearDragState();
        gamePanel.repaint();
    }

    private void clearDragState() {
        draggedDisk = null;
        sourceTowerIndex = -1;
        dragPoint = null;
    }

    private class GamePanel extends JPanel {

        public GamePanel() {
            setBackground(BACKGROUND_COLOR);

            MouseHandler mouseHandler = new MouseHandler();

            addMouseListener(mouseHandler);
            addMouseMotionListener(mouseHandler);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g;

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            drawScene(g2);
            drawTowerLabels(g2);

            for (int i = 0; i < game.getTowerCount(); i++) {
                drawTowerDisks(g2, i);
            }

            if (draggedDisk != null && dragPoint != null) {
                drawDraggedDisk(g2);
            }
        }

        private void drawScene(Graphics2D g2) {
            int baseY = getBaseY();

            g2.setColor(BASE_COLOR);

            g2.fillRect(
                    50,
                    baseY,
                    WINDOW_WIDTH - 100,
                    20
            );

            g2.setColor(ROD_COLOR);

            for (int i = 0; i < game.getTowerCount(); i++) {
                Rectangle rodBounds = getRodBounds(i);

                g2.fillRect(
                        rodBounds.x,
                        rodBounds.y,
                        rodBounds.width,
                        rodBounds.height
                );
            }
        }

        private void drawTowerLabels(Graphics2D g2) {
            Font originalFont = g2.getFont();

            g2.setFont(
                    originalFont.deriveFont(
                            Font.BOLD,
                            14f
                    )
            );

            g2.setColor(LABEL_COLOR);

            int y =
                    getBaseY()
                            - ROD_HEIGHT
                            - 20;

            drawCenteredText(
                    g2,
                    "START",
                    getTowerX(0),
                    y
            );

            drawCenteredText(
                    g2,
                    "GOAL",
                    getTowerX(game.getTowerCount() - 1),
                    y
            );

            g2.setFont(originalFont);
        }

        private void drawCenteredText(
                Graphics2D g2,
                String text,
                int centerX,
                int y
        ) {
            FontMetrics metrics =
                    g2.getFontMetrics();

            int x =
                    centerX
                            - (metrics.stringWidth(text) / 2);

            g2.drawString(text, x, y);
        }

        private void drawTowerDisks(
                Graphics2D g2,
                int towerIndex
        ) {
            List<Integer> tower =
                    game.getTowerSnapshot(towerIndex);

            int towerX =
                    getTowerX(towerIndex);

            int currentY =
                    getBaseY();

            int disksToDraw =
                    tower.size();

            if (
                    draggedDisk != null
                            && sourceTowerIndex == towerIndex
            ) {
                disksToDraw--;
            }

            for (int i = 0; i < disksToDraw; i++) {
                int diskValue =
                        tower.get(i);

                currentY -= DISK_HEIGHT;

                drawDisk(
                        g2,
                        towerX,
                        currentY,
                        diskValue
                );
            }
        }

        private void drawDraggedDisk(Graphics2D g2) {
            drawDisk(
                    g2,
                    dragPoint.x,
                    dragPoint.y - (DISK_HEIGHT / 2),
                    draggedDisk
            );
        }

        private void drawDisk(
                Graphics2D g2,
                int x,
                int y,
                int diskValue
        ) {
            int width =
                    getDiskWidth(diskValue);

            int startX =
                    x - (width / 2);

            Color mainColor =
                    DISK_COLORS[
                            (diskValue - 1)
                                    % DISK_COLORS.length
                            ];

            GradientPaint gradient =
                    new GradientPaint(
                            startX,
                            y,
                            mainColor,
                            startX,
                            y + DISK_HEIGHT,
                            mainColor.darker()
                    );

            g2.setPaint(gradient);

            RoundRectangle2D disk =
                    new RoundRectangle2D.Float(
                            startX,
                            y,
                            width,
                            DISK_HEIGHT,
                            10,
                            10
                    );

            g2.fill(disk);

            g2.setColor(
                    mainColor.darker().darker()
            );

            g2.draw(disk);
        }

        private int getTowerX(int towerIndex) {
            int spacing =
                    WINDOW_WIDTH
                            / (game.getTowerCount() + 1);

            return (towerIndex + 1) * spacing;
        }

        private int getBaseY() {
            return WINDOW_HEIGHT - 100;
        }

        private int getDiskWidth(int diskValue) {
            return BASE_DISK_WIDTH
                    + (diskValue * DISK_WIDTH_STEP);
        }

        private Rectangle getTopDiskBounds(
                int towerIndex
        ) {
            List<Integer> tower =
                    game.getTowerSnapshot(towerIndex);

            if (tower.isEmpty()) {
                return null;
            }

            int diskValue =
                    tower.get(tower.size() - 1);

            int width =
                    getDiskWidth(diskValue);

            int x =
                    getTowerX(towerIndex)
                            - (width / 2);

            int y =
                    getBaseY()
                            - (tower.size() * DISK_HEIGHT);

            return new Rectangle(
                    x,
                    y,
                    width,
                    DISK_HEIGHT
            );
        }

        private Rectangle getDraggedDiskBounds() {
            if (
                    draggedDisk == null
                            || dragPoint == null
            ) {
                return null;
            }

            int width =
                    getDiskWidth(draggedDisk);

            return new Rectangle(
                    dragPoint.x - (width / 2),
                    dragPoint.y - (DISK_HEIGHT / 2),
                    width,
                    DISK_HEIGHT
            );
        }

        private Rectangle getRodBounds(
                int towerIndex
        ) {
            int x =
                    getTowerX(towerIndex)
                            - (ROD_WIDTH / 2);

            int y =
                    getBaseY()
                            - ROD_HEIGHT;

            return new Rectangle(
                    x,
                    y,
                    ROD_WIDTH,
                    ROD_HEIGHT
            );
        }

        private int getTowerUnderDraggedDisk() {
            Rectangle diskBounds =
                    getDraggedDiskBounds();

            if (diskBounds == null) {
                return -1;
            }

            for (int i = 0; i < game.getTowerCount(); i++) {
                Rectangle rodBounds =
                        getRodBounds(i);

                if (diskBounds.intersects(rodBounds)) {
                    return i;
                }
            }

            return -1;
        }

        private class MouseHandler extends MouseAdapter {

            @Override
            public void mousePressed(MouseEvent e) {
                for (int i = 0; i < game.getTowerCount(); i++) {
                    Rectangle diskBounds =
                            getTopDiskBounds(i);

                    if (
                            diskBounds != null
                                    && diskBounds.contains(
                                    e.getPoint()
                            )
                    ) {
                        List<Integer> tower =
                                game.getTowerSnapshot(i);

                        draggedDisk =
                                tower.get(
                                        tower.size() - 1
                                );

                        sourceTowerIndex = i;
                        dragPoint = e.getPoint();

                        repaint();

                        return;
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (draggedDisk == null) {
                    return;
                }

                dragPoint = e.getPoint();

                int targetTowerIndex =
                        getTowerUnderDraggedDisk();

                if (
                        targetTowerIndex != -1
                                && targetTowerIndex
                                != sourceTowerIndex
                ) {
                    MoveResult result =
                            game.moveDisk(
                                    sourceTowerIndex,
                                    targetTowerIndex
                            );

                    if (
                            result == MoveResult.SUCCESS
                                    && game.isSolved()
                    ) {
                        JOptionPane.showMessageDialog(
                                HanoiFrame.this,
                                "Congratulations! You solved the Tower of Hanoi!",
                                "Victory!",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                    }
                }

                clearDragState();
                repaint();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (draggedDisk == null) {
                    return;
                }

                dragPoint = e.getPoint();

                repaint();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(
                () ->
                        new HanoiFrame()
                                .setVisible(true)
        );
    }
}