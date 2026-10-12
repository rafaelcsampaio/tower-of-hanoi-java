package io.github.rafaelcsampaio.hanoi.ui;

import io.github.rafaelcsampaio.hanoi.game.HanoiGame;
import io.github.rafaelcsampaio.hanoi.game.MoveResult;

import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.List;

import static io.github.rafaelcsampaio.hanoi.ui.theme.HanoiTheme.BACKGROUND_COLOR;
import static io.github.rafaelcsampaio.hanoi.ui.theme.HanoiTheme.BASE_DARK_COLOR;
import static io.github.rafaelcsampaio.hanoi.ui.theme.HanoiTheme.BASE_DISK_WIDTH;
import static io.github.rafaelcsampaio.hanoi.ui.theme.HanoiTheme.BASE_HEIGHT;
import static io.github.rafaelcsampaio.hanoi.ui.theme.HanoiTheme.BASE_LIGHT_COLOR;
import static io.github.rafaelcsampaio.hanoi.ui.theme.HanoiTheme.BOARD_BOTTOM_MARGIN;
import static io.github.rafaelcsampaio.hanoi.ui.theme.HanoiTheme.BOARD_MAX_WIDTH;
import static io.github.rafaelcsampaio.hanoi.ui.theme.HanoiTheme.DISK_HEIGHT;
import static io.github.rafaelcsampaio.hanoi.ui.theme.HanoiTheme.DISK_WIDTH_STEP;
import static io.github.rafaelcsampaio.hanoi.ui.theme.HanoiTheme.MUTED_TEXT_COLOR;
import static io.github.rafaelcsampaio.hanoi.ui.theme.HanoiTheme.ROD_DARK_COLOR;
import static io.github.rafaelcsampaio.hanoi.ui.theme.HanoiTheme.ROD_HEIGHT;
import static io.github.rafaelcsampaio.hanoi.ui.theme.HanoiTheme.ROD_LIGHT_COLOR;
import static io.github.rafaelcsampaio.hanoi.ui.theme.HanoiTheme.ROD_WIDTH;
import static io.github.rafaelcsampaio.hanoi.ui.theme.HanoiTheme.WOOD_BORDER_COLOR;
import static io.github.rafaelcsampaio.hanoi.ui.theme.HanoiTheme.brighten;
import static io.github.rafaelcsampaio.hanoi.ui.theme.HanoiTheme.darken;
import static io.github.rafaelcsampaio.hanoi.ui.theme.HanoiTheme.getDiskColor;

public class GamePanel extends JPanel {

    private final HanoiGame game;
    private final Runnable statsChangedAction;
    private final Runnable victoryAction;

    private Integer draggedDisk;
    private int sourceTowerIndex = -1;
    private Point dragPoint;

    public GamePanel(
            HanoiGame game,
            Runnable statsChangedAction,
            Runnable victoryAction
    ) {
        this.game = game;
        this.statsChangedAction =
                statsChangedAction;
        this.victoryAction =
                victoryAction;

        setBackground(
                BACKGROUND_COLOR
        );

        MouseHandler mouseHandler =
                new MouseHandler();

        addMouseListener(
                mouseHandler
        );

        addMouseMotionListener(
                mouseHandler
        );
    }

    public void resetInteraction() {
        clearDragState();
        repaint();
    }

    private void clearDragState() {
        draggedDisk = null;
        sourceTowerIndex = -1;
        dragPoint = null;
    }

    @Override
    protected void paintComponent(
            Graphics g
    ) {
        super.paintComponent(g);

        Graphics2D g2 =
                (Graphics2D) g.create();

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        g2.setRenderingHint(
                RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY
        );

        drawBoard(g2);
        drawTowerLabels(g2);

        for (
                int i = 0;
                i < game.getTowerCount();
                i++
        ) {
            drawTowerDisks(
                    g2,
                    i
            );
        }

        if (
                draggedDisk != null
                        && dragPoint != null
        ) {
            drawDraggedDisk(g2);
        }

        g2.dispose();
    }

    private void drawBoard(
            Graphics2D g2
    ) {
        int baseY =
                getBaseY();

        int boardWidth =
                getBoardWidth();

        int boardX =
                getBoardLeftX();

        drawRods(g2);

        RoundRectangle2D shadow =
                new RoundRectangle2D.Float(
                        boardX + 4,
                        baseY + 6,
                        boardWidth,
                        BASE_HEIGHT,
                        18,
                        18
                );

        g2.setColor(
                new Color(
                        0,
                        0,
                        0,
                        35
                )
        );

        g2.fill(shadow);

        GradientPaint baseGradient =
                new GradientPaint(
                        0,
                        baseY,
                        BASE_LIGHT_COLOR,
                        0,
                        baseY + BASE_HEIGHT,
                        BASE_DARK_COLOR
                );

        RoundRectangle2D base =
                new RoundRectangle2D.Float(
                        boardX,
                        baseY,
                        boardWidth,
                        BASE_HEIGHT,
                        18,
                        18
                );

        g2.setPaint(
                baseGradient
        );

        g2.fill(base);

        g2.setColor(
                WOOD_BORDER_COLOR
        );

        g2.setStroke(
                new BasicStroke(
                        1.5f
                )
        );

        g2.draw(base);
    }

    private void drawRods(
            Graphics2D g2
    ) {
        for (
                int i = 0;
                i < game.getTowerCount();
                i++
        ) {
            Rectangle rodBounds =
                    getRodBounds(i);

            GradientPaint rodGradient =
                    new GradientPaint(
                            rodBounds.x,
                            0,
                            ROD_LIGHT_COLOR,
                            rodBounds.x
                                    + rodBounds.width,
                            0,
                            ROD_DARK_COLOR
                    );

            RoundRectangle2D rod =
                    new RoundRectangle2D.Float(
                            rodBounds.x,
                            rodBounds.y,
                            rodBounds.width,
                            rodBounds.height
                                    + (
                                    BASE_HEIGHT
                                            / 2f
                            ),
                            ROD_WIDTH,
                            ROD_WIDTH
                    );

            g2.setPaint(
                    rodGradient
            );

            g2.fill(rod);

            g2.setColor(
                    WOOD_BORDER_COLOR
            );

            g2.setStroke(
                    new BasicStroke(1f)
            );

            g2.draw(rod);
        }
    }

    private void drawTowerLabels(
            Graphics2D g2
    ) {
        Font originalFont =
                g2.getFont();

        g2.setFont(
                new Font(
                        Font.SANS_SERIF,
                        Font.BOLD,
                        13
                )
        );

        g2.setColor(
                MUTED_TEXT_COLOR
        );

        int y =
                getBaseY()
                        - ROD_HEIGHT
                        - 18;

        drawCenteredText(
                g2,
                "START",
                getTowerX(0),
                y
        );

        drawCenteredText(
                g2,
                "GOAL",
                getTowerX(
                        game.getTowerCount()
                                - 1
                ),
                y
        );

        g2.setFont(
                originalFont
        );
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
                        - (
                        metrics.stringWidth(
                                text
                        )
                                / 2
                );

        g2.drawString(
                text,
                x,
                y
        );
    }

    private void drawTowerDisks(
            Graphics2D g2,
            int towerIndex
    ) {
        List<Integer> tower =
                game.getTowerSnapshot(
                        towerIndex
                );

        int towerX =
                getTowerX(
                        towerIndex
                );

        int currentY =
                getBaseY();

        int disksToDraw =
                tower.size();

        if (
                draggedDisk != null
                        && sourceTowerIndex
                        == towerIndex
        ) {
            disksToDraw--;
        }

        for (
                int i = 0;
                i < disksToDraw;
                i++
        ) {
            int diskValue =
                    tower.get(i);

            currentY -=
                    DISK_HEIGHT;

            drawDisk(
                    g2,
                    towerX,
                    currentY,
                    diskValue
            );
        }
    }

    private void drawDraggedDisk(
            Graphics2D g2
    ) {
        drawDisk(
                g2,
                dragPoint.x,
                dragPoint.y
                        - (
                        DISK_HEIGHT
                                / 2
                ),
                draggedDisk
        );
    }

    private void drawDisk(
            Graphics2D g2,
            int centerX,
            int y,
            int diskValue
    ) {
        int width =
                getDiskWidth(
                        diskValue
                );

        int startX =
                centerX
                        - (
                        width
                                / 2
                );

        Color mainColor =
                getDiskColor(
                        diskValue
                );

        RoundRectangle2D shadow =
                new RoundRectangle2D.Float(
                        startX + 2,
                        y + 4,
                        width,
                        DISK_HEIGHT,
                        14,
                        14
                );

        g2.setColor(
                new Color(
                        0,
                        0,
                        0,
                        42
                )
        );

        g2.fill(shadow);

        LinearGradientPaint bodyGradient =
                new LinearGradientPaint(
                        startX,
                        y,
                        startX,
                        y + DISK_HEIGHT,
                        new float[]{
                                0.0f,
                                0.18f,
                                0.55f,
                                1.0f
                        },
                        new Color[]{
                                brighten(
                                        mainColor,
                                        1.22f
                                ),
                                brighten(
                                        mainColor,
                                        1.08f
                                ),
                                mainColor,
                                darken(
                                        mainColor,
                                        0.72f
                                )
                        }
                );

        RoundRectangle2D disk =
                new RoundRectangle2D.Float(
                        startX,
                        y,
                        width,
                        DISK_HEIGHT,
                        14,
                        14
                );

        g2.setPaint(
                bodyGradient
        );

        g2.fill(disk);

        Shape originalClip =
                g2.getClip();

        g2.clip(disk);

        LinearGradientPaint highlightGradient =
                new LinearGradientPaint(
                        startX,
                        y + 2,
                        startX,
                        y
                                + (
                                DISK_HEIGHT
                                        * 0.55f
                        ),
                        new float[]{
                                0.0f,
                                1.0f
                        },
                        new Color[]{
                                new Color(
                                        255,
                                        255,
                                        255,
                                        125
                                ),
                                new Color(
                                        255,
                                        255,
                                        255,
                                        0
                                )
                        }
                );

        RoundRectangle2D highlight =
                new RoundRectangle2D.Float(
                        startX + 5,
                        y + 3,
                        width - 10,
                        DISK_HEIGHT
                                * 0.45f,
                        11,
                        11
                );

        g2.setPaint(
                highlightGradient
        );

        g2.fill(highlight);

        g2.setClip(
                originalClip
        );

        g2.setColor(
                darken(
                        mainColor,
                        0.52f
                )
        );

        g2.setStroke(
                new BasicStroke(
                        1.2f
                )
        );

        g2.draw(disk);

        g2.setColor(
                new Color(
                        255,
                        255,
                        255,
                        65
                )
        );

        g2.setStroke(
                new BasicStroke(1f)
        );

        g2.draw(
                new RoundRectangle2D.Float(
                        startX + 2,
                        y + 2,
                        width - 4,
                        DISK_HEIGHT - 4,
                        12,
                        12
                )
        );
    }

    private int getBoardWidth() {
        return Math.min(
                BOARD_MAX_WIDTH,
                getWidth() - 120
        );
    }

    private int getBoardLeftX() {
        return (
                getWidth()
                        - getBoardWidth()
        ) / 2;
    }

    private int getTowerX(
            int towerIndex
    ) {
        int boardWidth =
                getBoardWidth();

        int boardLeft =
                getBoardLeftX();

        int spacing =
                boardWidth
                        / (
                        game.getTowerCount()
                                + 1
                );

        return boardLeft
                + (
                towerIndex + 1
        ) * spacing;
    }

    private int getBaseY() {
        return getHeight()
                - BOARD_BOTTOM_MARGIN;
    }

    private int getDiskWidth(
            int diskValue
    ) {
        return BASE_DISK_WIDTH
                + (
                diskValue
                        * DISK_WIDTH_STEP
        );
    }

    private Rectangle getTopDiskBounds(
            int towerIndex
    ) {
        List<Integer> tower =
                game.getTowerSnapshot(
                        towerIndex
                );

        if (
                tower.isEmpty()
        ) {
            return null;
        }

        int diskValue =
                tower.get(
                        tower.size()
                                - 1
                );

        int width =
                getDiskWidth(
                        diskValue
                );

        int x =
                getTowerX(
                        towerIndex
                )
                        - (
                        width
                                / 2
                );

        int y =
                getBaseY()
                        - (
                        tower.size()
                                * DISK_HEIGHT
                );

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
                getDiskWidth(
                        draggedDisk
                );

        return new Rectangle(
                dragPoint.x
                        - (
                        width
                                / 2
                ),
                dragPoint.y
                        - (
                        DISK_HEIGHT
                                / 2
                ),
                width,
                DISK_HEIGHT
        );
    }

    private Rectangle getRodBounds(
            int towerIndex
    ) {
        int x =
                getTowerX(
                        towerIndex
                )
                        - (
                        ROD_WIDTH
                                / 2
                );

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

        if (
                diskBounds == null
        ) {
            return -1;
        }

        for (
                int i = 0;
                i < game.getTowerCount();
                i++
        ) {
            Rectangle rodBounds =
                    getRodBounds(i);

            if (
                    diskBounds.intersects(
                            rodBounds
                    )
            ) {
                return i;
            }
        }

        return -1;
    }

    private class MouseHandler
            extends MouseAdapter {

        @Override
        public void mousePressed(
                MouseEvent e
        ) {
            for (
                    int i = 0;
                    i < game.getTowerCount();
                    i++
            ) {
                Rectangle diskBounds =
                        getTopDiskBounds(i);

                if (
                        diskBounds != null
                                && diskBounds.contains(
                                e.getPoint()
                        )
                ) {
                    List<Integer> tower =
                            game.getTowerSnapshot(
                                    i
                            );

                    draggedDisk =
                            tower.get(
                                    tower.size()
                                            - 1
                            );

                    sourceTowerIndex =
                            i;

                    dragPoint =
                            e.getPoint();

                    repaint();

                    return;
                }
            }
        }

        @Override
        public void mouseReleased(
                MouseEvent e
        ) {
            if (
                    draggedDisk == null
            ) {
                return;
            }

            dragPoint =
                    e.getPoint();

            int targetTowerIndex =
                    getTowerUnderDraggedDisk();

            boolean victory =
                    false;

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

                statsChangedAction.run();

                victory =
                        result == MoveResult.SUCCESS
                                && game.isSolved();
            }

            clearDragState();

            repaint();

            if (victory) {
                javax.swing.Timer victoryTimer =
                        new javax.swing.Timer(
                                180,
                                event ->
                                        victoryAction.run()
                        );

                victoryTimer.setRepeats(false);

                victoryTimer.start();
            }

            clearDragState();
            repaint();
        }

        @Override
        public void mouseDragged(
                MouseEvent e
        ) {
            if (
                    draggedDisk == null
            ) {
                return;
            }

            dragPoint =
                    e.getPoint();

            repaint();
        }
    }
}