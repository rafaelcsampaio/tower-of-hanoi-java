package io.github.rafaelcsampaio.hanoi.ui.theme;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.JFrame;
import javax.swing.UIManager;

public final class HanoiLookAndFeel {

    private HanoiLookAndFeel() {
    }

    public static void setup() {
        System.setProperty(
                "flatlaf.useWindowDecorations",
                "true"
        );

        FlatLightLaf.setup();

        UIManager.put(
                "TitlePane.unifiedBackground",
                false
        );

        UIManager.put(
                "TitlePane.background",
                HanoiTheme.TITLE_BAR_BACKGROUND_COLOR
        );

        UIManager.put(
                "TitlePane.inactiveBackground",
                HanoiTheme.TITLE_BAR_INACTIVE_BACKGROUND_COLOR
        );

        UIManager.put(
                "TitlePane.foreground",
                HanoiTheme.TITLE_BAR_FOREGROUND_COLOR
        );

        UIManager.put(
                "TitlePane.inactiveForeground",
                HanoiTheme.TITLE_BAR_INACTIVE_FOREGROUND_COLOR
        );

        UIManager.put(
                "TitlePane.borderColor",
                HanoiTheme.TITLE_BAR_BORDER_COLOR
        );

        UIManager.put(
                "Panel.background",
                HanoiTheme.BACKGROUND_COLOR
        );

        UIManager.put(
                "OptionPane.background",
                HanoiTheme.BACKGROUND_COLOR
        );

        UIManager.put(
                "OptionPane.messageBackground",
                HanoiTheme.BACKGROUND_COLOR
        );

        UIManager.put(
                "TextField.background",
                HanoiTheme.INPUT_BACKGROUND_COLOR
        );

        UIManager.put(
                "TextField.foreground",
                HanoiTheme.TEXT_COLOR
        );

        UIManager.put(
                "TextField.caretForeground",
                HanoiTheme.TEXT_COLOR
        );

        UIManager.put(
                "Component.arc",
                12
        );

        UIManager.put(
                "Button.arc",
                16
        );

        UIManager.put(
                "TextComponent.arc",
                10
        );

        UIManager.put(
                "Component.focusWidth",
                1
        );

        UIManager.put(
                "Component.innerFocusWidth",
                0
        );
    }

    public static void applyWindowStyle(
            JFrame frame
    ) {
        frame.getRootPane().putClientProperty(
                "JRootPane.useWindowDecorations",
                true
        );

        frame.getRootPane().putClientProperty(
                "JRootPane.titleBarBackground",
                HanoiTheme.TITLE_BAR_BACKGROUND_COLOR
        );

        frame.getRootPane().putClientProperty(
                "JRootPane.titleBarForeground",
                HanoiTheme.TITLE_BAR_FOREGROUND_COLOR
        );
    }
}