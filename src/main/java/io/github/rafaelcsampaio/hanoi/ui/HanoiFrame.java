package io.github.rafaelcsampaio.hanoi.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.util.Stack;

public class HanoiFrame extends JFrame {


    private static final int LARGURA_JANELA = 800;
    private static final int ALTURA_JANELA = 600;
    private static final int NUM_HASTES = 3;
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

 
    private int numDiscos;
    private Stack<Integer>[] hastes; 
    private GamePanel painelJogo;

   
    private Integer discoArrastado = null;
    private int hasteOrigemArrasto = -1;
    private Point pontoArrasto = null; 

    public HanoiFrame() {
      
        String input = JOptionPane.showInputDialog(this, "Número de discos (1 a 6):", "Novo Jogo", JOptionPane.QUESTION_MESSAGE);
        try {
            this.numDiscos = Math.max(1, Math.min(6, Integer.parseInt(input)));
        } catch (NumberFormatException e) {
            this.numDiscos = 3; 
        }

        inicializarEstadoJogo();


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

    private void inicializarEstadoJogo() {
        hastes = new Stack[NUM_HASTES];
        for (int i = 0; i < NUM_HASTES; i++) {
            hastes[i] = new Stack<>();
        }
        for (int i = numDiscos; i > 0; i--) {
            hastes[0].push(i);
        }
        discoArrastado = null;
        hasteOrigemArrasto = -1;
    }

    private void reiniciarJogo() {
        inicializarEstadoJogo();
        painelJogo.repaint();
    }

    private boolean tentarMoverDisco(int origem, int destino) {
        if (origem == destino || hastes[origem].isEmpty()) {
            return false;
        }

        int valorDiscoOrigem = hastes[origem].peek();

        if (!hastes[destino].isEmpty() && valorDiscoOrigem > hastes[destino].peek()) {
            return false;
        }

        hastes[destino].push(hastes[origem].pop());
        verificarVitoria();
        return true;
    }

    private void verificarVitoria() {
        if (hastes[1].size() == numDiscos || hastes[2].size() == numDiscos) {
            JOptionPane.showMessageDialog(this, "Parabéns! Você resolveu a Torre de Hanoi!", "Vitória!", JOptionPane.INFORMATION_MESSAGE);
        }
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
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            desenharCenario(g2);

            for (int i = 0; i < NUM_HASTES; i++) {
                desenharDiscosDaHaste(g2, i);
            }

            if (discoArrastado != null && pontoArrasto != null) {
                desenharDiscoArrastado(g2);
            }
        }

        private void desenharCenario(Graphics2D g2) {
            int yBase = ALTURA_JANELA - 100;
            g2.setColor(COR_BASE);
            g2.fillRect(50, yBase, LARGURA_JANELA - 100, 20);

            g2.setColor(COR_HASTE);
            for (int i = 0; i < NUM_HASTES; i++) {
                int x = getXHaste(i) - (LARGURA_HASTE / 2);
                g2.fillRect(x, yBase - ALTURA_HASTE, LARGURA_HASTE, ALTURA_HASTE);
            }
        }

        private void desenharDiscosDaHaste(Graphics2D g2, int indiceHaste) {
            Stack<Integer> haste = hastes[indiceHaste];
            int xHaste = getXHaste(indiceHaste);
            int yAtual = ALTURA_JANELA - 100;

 
            for (int i = 0; i < haste.size(); i++) {
                Integer valorDisco = haste.get(i);
                yAtual -= ALTURA_DISCO;
                
                
                desenharDisco(g2, xHaste, yAtual, valorDisco);
            }
        }

        private void desenharDiscoArrastado(Graphics2D g2) {
            desenharDisco(g2, pontoArrasto.x, pontoArrasto.y - (ALTURA_DISCO / 2), discoArrastado);
        }

        private void desenharDisco(Graphics2D g2, int x, int y, int valorDisco) {
            int largura = LARGURA_BASE_DISCO + (valorDisco * INCREMENTO_LARGURA);
            int xIncial = x - (largura / 2);

            Color corPrincipal = CORES_DISCOS[(valorDisco - 1) % CORES_DISCOS.length];
            GradientPaint gradient = new GradientPaint(
                    xIncial, y, corPrincipal,
                    xIncial, y + ALTURA_DISCO, corPrincipal.darker()
            );
            
            g2.setPaint(gradient);
            
            RoundRectangle2D disco = new RoundRectangle2D.Float(xIncial, y, largura, ALTURA_DISCO, 10, 10);
            g2.fill(disco);

            g2.setColor(corPrincipal.darker().darker());
            g2.draw(disco);
        }

        private int getXHaste(int indiceHaste) {
            int espacamento = LARGURA_JANELA / (NUM_HASTES + 1);
            return (indiceHaste + 1) * espacamento;
        }

        private int getHastePelaCoordenadaX(int mouseX) {
            int espacamento = LARGURA_JANELA / (NUM_HASTES + 1);
            if (mouseX < espacamento + (espacamento/2)) return 0;
            if (mouseX < (espacamento * 2) + (espacamento/2)) return 1;
            return 2;
        }

        private class ManipuladorMouse extends MouseAdapter {
            @Override
            public void mousePressed(MouseEvent e) {
                int hasteClicada = getHastePelaCoordenadaX(e.getX());
                
                if (!hastes[hasteClicada].isEmpty()) {
                    discoArrastado = hastes[hasteClicada].pop(); 
                    hasteOrigemArrasto = hasteClicada;
                    pontoArrasto = e.getPoint();
                    repaint(); 
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (discoArrastado != null) {
                    int hasteDestino = getHastePelaCoordenadaX(e.getX());
                    
                    hastes[hasteOrigemArrasto].push(discoArrastado); 
                    
                    boolean moveu = tentarMoverDisco(hasteOrigemArrasto, hasteDestino);
                    
                    if (!moveu) {

                    }

                    discoArrastado = null;
                    hasteOrigemArrasto = -1;
                    pontoArrasto = null;
                    repaint();
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (discoArrastado != null) {
                    pontoArrasto = e.getPoint(); 
                    repaint(); 
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new HanoiFrame().setVisible(true);
        });
    }
}