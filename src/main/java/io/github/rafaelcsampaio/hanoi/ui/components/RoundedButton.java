package io.github.rafaelcsampaio.hanoi.ui.components;

import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;

import static io.github.rafaelcsampaio.hanoi.ui.theme.HanoiTheme.BUTTON_COLOR;
import static io.github.rafaelcsampaio.hanoi.ui.theme.HanoiTheme.BUTTON_HOVER_COLOR;
import static io.github.rafaelcsampaio.hanoi.ui.theme.HanoiTheme.BUTTON_PRESSED_COLOR;
import static io.github.rafaelcsampaio.hanoi.ui.theme.HanoiTheme.brighten;
import static io.github.rafaelcsampaio.hanoi.ui.theme.HanoiTheme.darken;

public class RoundedButton extends JButton {

    private static final int ARC = 18;

    public RoundedButton(String text) {
        super(text);

        setFont(
                new Font(
                        Font.SANS_SERIF,
                        Font.BOLD,
                        13
                )
        );

        setForeground(Color.WHITE);

        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setRolloverEnabled(true);

        setCursor(
                Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR
                )
        );

        setBorder(
                new EmptyBorder(
                        9,
                        19,
                        9,
                        19
                )
        );
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 =
                (Graphics2D) g.create();

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        ButtonModel model =
                getModel();

        Color currentColor =
                BUTTON_COLOR;

        if (model.isPressed()) {
            currentColor =
                    BUTTON_PRESSED_COLOR;

        } else if (model.isRollover()) {
            currentColor =
                    BUTTON_HOVER_COLOR;
        }

        RoundRectangle2D shadow =
                new RoundRectangle2D.Float(
                        1,
                        3,
                        getWidth() - 2,
                        getHeight() - 3,
                        ARC,
                        ARC
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

        LinearGradientPaint gradient =
                new LinearGradientPaint(
                        0,
                        0,
                        0,
                        getHeight(),
                        new float[]{
                                0f,
                                1f
                        },
                        new Color[]{
                                brighten(
                                        currentColor,
                                        1.13f
                                ),
                                darken(
                                        currentColor,
                                        0.82f
                                )
                        }
                );

        RoundRectangle2D button =
                new RoundRectangle2D.Float(
                        0,
                        0,
                        getWidth() - 1,
                        getHeight() - 4,
                        ARC,
                        ARC
                );

        g2.setPaint(gradient);
        g2.fill(button);

        g2.setColor(
                new Color(
                        255,
                        255,
                        255,
                        model.isRollover()
                                ? 75
                                : 45
                )
        );

        g2.setStroke(
                new BasicStroke(1f)
        );

        g2.draw(button);

        g2.dispose();

        super.paintComponent(g);
    }

    @Override
    public boolean contains(int x, int y) {
        return new RoundRectangle2D.Float(
                0,
                0,
                getWidth(),
                getHeight(),
                ARC,
                ARC
        ).contains(x, y);
    }
}