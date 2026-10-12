package io.github.rafaelcsampaio.hanoi.app;

import io.github.rafaelcsampaio.hanoi.ui.HanoiFrame;
import io.github.rafaelcsampaio.hanoi.ui.dialogs.NewGameDialog;
import io.github.rafaelcsampaio.hanoi.ui.theme.HanoiLookAndFeel;

import javax.swing.SwingUtilities;

public final class HanoiApplication {

    private HanoiApplication() {
    }

    public static void main(String[] args) {
        HanoiLookAndFeel.setup();

        SwingUtilities.invokeLater(
                () -> {
                    Integer diskCount =
                            NewGameDialog.showDialog(
                                    null
                            );

                    if (diskCount == null) {
                        return;
                    }

                    new HanoiFrame(
                            diskCount
                    ).setVisible(true);
                }
        );
    }
}