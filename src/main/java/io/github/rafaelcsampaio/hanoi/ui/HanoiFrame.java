package io.github.rafaelcsampaio.hanoi.ui;

import io.github.rafaelcsampaio.hanoi.game.HanoiGame;
import io.github.rafaelcsampaio.hanoi.game.MoveResult;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.util.List;

public class HanoiFrame extends JFrame {

    private static final int LARGURA_JANELA = 800;
    private static final int ALTURA_JANELA = 600;

    private static final int ALTURA_DISCO = 25;
    private static final int LARGURA_BASE_DISCO = 50;
    private static final int INCREMENTO_LARGURA = 20;

    private static final int ALTURA_HASTE = 300;
    private static final int LARGURA_HASTE = 10;

    private static final Color COR_FUNDO = new Color(240, 240, 240);
    private static final Color COR_HASTE = new Color(100, 70, 40);
    private static final Color COR_BASE = new Color(60, 40, 20);

    private static final Color[] CORES_DISCOS = {
            new Color(231, 76, 60),
            new Color(230, 126, 34),
            new Color(241, 196, 15),
            new Color(46, 204, 113),
            new Color(52, 152, 219),
            new Color(155, 89, 182)
    };

    private final HanoiGame game;
    private final GamePanel painelJogo;

    private Integer discoArrastado = null;
    private int hasteOrigemArrasto = -1;
    private Point pontoArrasto = null;

    public HanoiFrame() {
        int diskCount = solicitarQuantidadeDiscos();

        game = new HanoiGame(diskCount);

        setTitle("Desafio Torre de Hanoi");
        setSize(LARGURA_JANELA, ALTURA_JANELA);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        painelJogo = new GamePanel();
        add(painelJogo);

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Jogo");
        JMenuItem reiniciar = new JMenuItem("Reiniciar");

        reiniciar.addActionListener(e -> reiniciarJogo());

        menu.add(reiniciar);
        menuBar.add(menu);

        setJMenuBar(menuBar);
    }
    private int solicitarQuantidadeDiscos() {
        while (true) {
            String input = JOptionPane.showInputDialog(
                    this,
                    "Número de discos (1 a 6):",
                    "Novo Jogo",
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
                        "Escolha uma quantidade entre 1 e 6 discos.",
                        "Quantidade inválida",
                        JOptionPane.WARNING_MESSAGE
                );

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(
                        this,
                        "Digite um número inteiro entre 1 e 6.",
                        "Entrada inválida",
                        JOptionPane.WARNING_MESSAGE
                );
            }
        }
    }

    private void reiniciarJogo() {
        game.reset();

        discoArrastado = null;
        hasteOrigemArrasto = -1;
        pontoArrasto = null;

        painelJogo.repaint();
    }

    private class GamePanel extends JPanel {

        public GamePanel() {
            setBackground(COR_FUNDO);

            ManipuladorMouse manipulador = new ManipuladorMouse();

            addMouseListener(manipulador);
            addMouseMotionListener(manipulador);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g;

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            desenharCenario(g2);

            for (int i = 0; i < game.getTowerCount(); i++) {
                desenharDiscosDaHaste(g2, i);
            }

            if (discoArrastado != null && pontoArrasto != null) {
                desenharDiscoArrastado(g2);
            }
        }

        private void desenharCenario(Graphics2D g2) {
            int yBase = ALTURA_JANELA - 100;

            g2.setColor(COR_BASE);

            g2.fillRect(
                    50,
                    yBase,
                    LARGURA_JANELA - 100,
                    20
            );

            g2.setColor(COR_HASTE);

            for (int i = 0; i < game.getTowerCount(); i++) {
                int x = getXHaste(i) - (LARGURA_HASTE / 2);

                g2.fillRect(
                        x,
                        yBase - ALTURA_HASTE,
                        LARGURA_HASTE,
                        ALTURA_HASTE
                );
            }
        }

        private void desenharDiscosDaHaste(
                Graphics2D g2,
                int indiceHaste
        ) {
            List<Integer> tower =
                    game.getTowerSnapshot(indiceHaste);

            int xHaste = getXHaste(indiceHaste);
            int yAtual = ALTURA_JANELA - 100;

            int discosParaDesenhar = tower.size();

            if (
                    discoArrastado != null
                            && hasteOrigemArrasto == indiceHaste
            ) {
                discosParaDesenhar--;
            }

            for (int i = 0; i < discosParaDesenhar; i++) {
                int valorDisco = tower.get(i);

                yAtual -= ALTURA_DISCO;

                desenharDisco(
                        g2,
                        xHaste,
                        yAtual,
                        valorDisco
                );
            }
        }

        private void desenharDiscoArrastado(Graphics2D g2) {
            desenharDisco(
                    g2,
                    pontoArrasto.x,
                    pontoArrasto.y - (ALTURA_DISCO / 2),
                    discoArrastado
            );
        }

        private void desenharDisco(
                Graphics2D g2,
                int x,
                int y,
                int valorDisco
        ) {
            int largura =
                    LARGURA_BASE_DISCO
                            + (valorDisco * INCREMENTO_LARGURA);

            int xInicial = x - (largura / 2);

            Color corPrincipal =
                    CORES_DISCOS[
                            (valorDisco - 1)
                                    % CORES_DISCOS.length
                            ];

            GradientPaint gradient = new GradientPaint(
                    xInicial,
                    y,
                    corPrincipal,
                    xInicial,
                    y + ALTURA_DISCO,
                    corPrincipal.darker()
            );

            g2.setPaint(gradient);

            RoundRectangle2D disco =
                    new RoundRectangle2D.Float(
                            xInicial,
                            y,
                            largura,
                            ALTURA_DISCO,
                            10,
                            10
                    );

            g2.fill(disco);

            g2.setColor(
                    corPrincipal.darker().darker()
            );

            g2.draw(disco);
        }

        private int getXHaste(int indiceHaste) {
            int espacamento =
                    LARGURA_JANELA
                            / (game.getTowerCount() + 1);

            return (indiceHaste + 1) * espacamento;
        }

        private int getHastePelaCoordenadaX(int mouseX) {
            int espacamento =
                    LARGURA_JANELA
                            / (game.getTowerCount() + 1);

            if (
                    mouseX
                            < espacamento
                            + (espacamento / 2)
            ) {
                return 0;
            }

            if (
                    mouseX
                            < (espacamento * 2)
                            + (espacamento / 2)
            ) {
                return 1;
            }

            return 2;
        }

        private class ManipuladorMouse extends MouseAdapter {

            @Override
            public void mousePressed(MouseEvent e) {
                int hasteClicada =
                        getHastePelaCoordenadaX(
                                e.getX()
                        );

                List<Integer> tower =
                        game.getTowerSnapshot(
                                hasteClicada
                        );

                if (!tower.isEmpty()) {
                    discoArrastado =
                            tower.get(
                                    tower.size() - 1
                            );

                    hasteOrigemArrasto =
                            hasteClicada;

                    pontoArrasto =
                            e.getPoint();

                    repaint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (discoArrastado == null) {
                    return;
                }

                int hasteDestino =
                        getHastePelaCoordenadaX(
                                e.getX()
                        );

                if (
                        hasteDestino
                                != hasteOrigemArrasto
                ) {
                    MoveResult result =
                            game.moveDisk(
                                    hasteOrigemArrasto,
                                    hasteDestino
                            );

                    if (
                            result == MoveResult.SUCCESS
                                    && game.isSolved()
                    ) {
                        JOptionPane.showMessageDialog(
                                HanoiFrame.this,
                                "Parabéns! Você resolveu a Torre de Hanoi!",
                                "Vitória!",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                    }
                }

                discoArrastado = null;
                hasteOrigemArrasto = -1;
                pontoArrasto = null;

                repaint();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (discoArrastado != null) {
                    pontoArrasto =
                            e.getPoint();

                    repaint();
                }
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