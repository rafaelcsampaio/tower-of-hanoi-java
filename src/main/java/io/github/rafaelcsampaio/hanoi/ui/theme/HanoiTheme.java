package io.github.rafaelcsampaio.hanoi.ui.theme;

import java.awt.Color;

public final class HanoiTheme {

    public static final int BOARD_MAX_WIDTH = 680;
    public static final int BASE_HEIGHT = 34;
    public static final int BOARD_BOTTOM_MARGIN = 70;

    public static final int ROD_HEIGHT = 220;
    public static final int ROD_WIDTH = 14;

    public static final int DISK_HEIGHT = 30;
    public static final int BASE_DISK_WIDTH = 58;
    public static final int DISK_WIDTH_STEP = 22;

    public static final Color BACKGROUND_COLOR =
            new Color(244, 239, 230);

    public static final Color HEADER_BACKGROUND_COLOR =
            new Color(250, 247, 241);

    public static final Color TEXT_COLOR =
            new Color(55, 45, 38);

    public static final Color MUTED_TEXT_COLOR =
            new Color(110, 92, 78);

    public static final Color BASE_LIGHT_COLOR =
            new Color(154, 103, 58);

    public static final Color BASE_DARK_COLOR =
            new Color(92, 58, 32);

    public static final Color ROD_LIGHT_COLOR =
            new Color(135, 82, 44);

    public static final Color ROD_DARK_COLOR =
            new Color(86, 49, 26);

    public static final Color WOOD_BORDER_COLOR =
            new Color(72, 43, 24);

    public static final Color BUTTON_COLOR =
            new Color(96, 61, 40);

    public static final Color BUTTON_HOVER_COLOR =
            new Color(117, 76, 48);

    public static final Color BUTTON_PRESSED_COLOR =
            new Color(76, 47, 31);

    private static final Color[] DISK_COLORS = {
            new Color(226, 72, 60),
            new Color(239, 151, 32),
            new Color(215, 207, 18),
            new Color(53, 182, 98),
            new Color(48, 135, 218),
            new Color(144, 75, 190)
    };

    private HanoiTheme() {
    }

    public static Color getDiskColor(int diskValue) {
        return DISK_COLORS[
                (diskValue - 1) % DISK_COLORS.length
                ];
    }

    public static Color brighten(Color color, float factor) {
        return new Color(
                Math.min(
                        255,
                        Math.round(color.getRed() * factor)
                ),
                Math.min(
                        255,
                        Math.round(color.getGreen() * factor)
                ),
                Math.min(
                        255,
                        Math.round(color.getBlue() * factor)
                )
        );
    }

    public static Color darken(Color color, float factor) {
        return new Color(
                Math.max(
                        0,
                        Math.round(color.getRed() * factor)
                ),
                Math.max(
                        0,
                        Math.round(color.getGreen() * factor)
                ),
                Math.max(
                        0,
                        Math.round(color.getBlue() * factor)
                )
        );
    }
}