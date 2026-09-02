package com.spaceclient.launcher;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public final class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame("Space Client");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setSize(1100, 680);
            f.setLocationRelativeTo(null);
            f.setContentPane(new SpacePanel());
            f.setVisible(true);
        });
    }

    static final class SpacePanel extends JPanel {
        final Star[] stars = new Star[180];
        SpacePanel() {
            Random r = new Random(0x5ACEC11E);
            for (int i = 0; i < stars.length; i++) stars[i] = new Star(r.nextDouble(), r.nextDouble(), 1 + r.nextDouble() * 2);
            setBackground(Color.BLACK);
        }
        protected void paintComponent(Graphics gg) {
            super.paintComponent(gg);
            Graphics2D g = (Graphics2D) gg.create();
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(Color.WHITE);
            for (Star s : stars) g.fillRect((int)(s.x * getWidth()), (int)(s.y * getHeight()), (int)s.size, (int)s.size);
            int w = Math.min(720, getWidth() - 80), h = 280;
            int x = (getWidth() - w) / 2, y = (getHeight() - h) / 2;
            g.setColor(new Color(0, 0, 0, 195));
            g.fillRoundRect(x, y, w, h, 28, 28);
            g.setColor(Color.WHITE);
            g.setFont(getFont().deriveFont(Font.BOLD, 42f));
            String title = "Space Client";
            g.drawString(title, (getWidth() - g.getFontMetrics().stringWidth(title)) / 2, y + 105);
            g.setColor(new Color(205, 205, 205));
            g.setFont(getFont().deriveFont(Font.PLAIN, 17f));
            String sub = "Your Minecraft client is getting ready for launch.";
            g.drawString(sub, (getWidth() - g.getFontMetrics().stringWidth(sub)) / 2, y + 145);
            g.setColor(new Color(180, 180, 180));
            g.setFont(getFont().deriveFont(Font.PLAIN, 13f));
            String ver = "Launcher 0.1.0 | First milestone";
            g.drawString(ver, (getWidth() - g.getFontMetrics().stringWidth(ver)) / 2, y + 178);
            g.dispose();
        }
    }
    record Star(double x, double y, double size) {}
}
