package com.spaceclient.launcher;

import java.awt.AlphaComposite;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/** Minimal Space Client launcher shell. */
public final class LauncherMain extends Canvas implements Runnable, KeyListener {
    private static final int TARGET_FPS = 120;
    private static final long FRAME_TIME_NS = 1_000_000_000L / TARGET_FPS;
    private static final int STAR_COUNT = 520;
    private static final long QUOTE_INTERVAL_MS = 30_000L;
    private static final long QUOTE_TRAVEL_MS = 18_000L;

    private static final String[] QUOTES = {
        "Space Client on top",
        "Did you know Space Client is the best client out right now",
        "Built to feel clean. Built to feel fast.",
        "Welcome to Space Client",
        "Ready for takeoff",
        "Clean. Fast. Space.",
        "Your game. Your way.",
        "See you in the stars"
    };

    private final Random random = new Random(0x5ACE2026L);
    private final List<Star> stars = new ArrayList<>(STAR_COUNT);
    private JFrame frame;
    private volatile boolean running;
    private Thread renderThread;
    private long lastUpdateNs;
    private long nextQuoteAtMs;
    private Quote activeQuote;
    private int quoteIndex;

    public static void main(String[] args) {
        if (GraphicsEnvironment.isHeadless()) {
            throw new IllegalStateException("Space Client Launcher requires a graphical environment.");
        }
        SwingUtilities.invokeLater(() -> new LauncherMain().start());
    }

    private void start() {
        frame = new JFrame("Space Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setUndecorated(true);
        frame.setResizable(false);
        frame.setIgnoreRepaint(true);
        frame.addKeyListener(this);

        setIgnoreRepaint(true);
        addKeyListener(this);
        setBackground(new Color(2, 3, 5));

        frame.add(this);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
        requestFocus();

        resizeStarfield(getWidth(), getHeight());
        running = true;
        renderThread = new Thread(this, "Space-Client-Launcher-Render");
        renderThread.setDaemon(false);
        renderThread.start();
    }

    @Override
    public void run() {
        createBufferStrategySafely();
        lastUpdateNs = System.nanoTime();
        nextQuoteAtMs = System.currentTimeMillis() + QUOTE_INTERVAL_MS;

        while (running) {
            long frameStart = System.nanoTime();
            long now = System.nanoTime();
            double deltaSeconds = Math.min((now - lastUpdateNs) / 1_000_000_000.0, 0.05);
            lastUpdateNs = now;

            update(deltaSeconds);
            render();

            long remaining = FRAME_TIME_NS - (System.nanoTime() - frameStart);
            if (remaining > 0) sleepPrecisely(remaining);
        }
    }

    private void update(double deltaSeconds) {
        int width = Math.max(getWidth(), 1);
        int height = Math.max(getHeight(), 1);

        if (stars.size() != STAR_COUNT) resizeStarfield(width, height);

        for (Star star : stars) {
            star.x += star.velocityX * deltaSeconds;
            star.y += star.velocityY * deltaSeconds;
            star.twinkleTime += deltaSeconds * star.twinkleSpeed;

            if (star.x < -8) star.x = width + 8;
            if (star.x > width + 8) star.x = -8;
            if (star.y < -8) star.y = height + 8;
            if (star.y > height + 8) star.y = -8;
        }

        long nowMs = System.currentTimeMillis();
        if (activeQuote == null && nowMs >= nextQuoteAtMs) {
            activeQuote = new Quote(QUOTES[quoteIndex++ % QUOTES.length], nowMs);
            nextQuoteAtMs = nowMs + QUOTE_INTERVAL_MS;
        }

        if (activeQuote != null && nowMs - activeQuote.startedAt >= QUOTE_TRAVEL_MS) {
            activeQuote = null;
        }
    }

    private void render() {
        BufferStrategy strategy = getBufferStrategy();
        if (strategy == null) {
            createBufferStrategySafely();
            return;
        }

        do {
            do {
                Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
                try {
                    int width = getWidth();
                    int height = getHeight();
                    g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
                    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                    paintBackground(g, width, height);
                    paintStars(g);
                    paintQuote(g, width, height);
                } finally {
                    g.dispose();
                }
            } while (strategy.contentsRestored());
            strategy.show();
            Toolkit.getDefaultToolkit().sync();
        } while (strategy.contentsLost());
    }

    private void paintBackground(Graphics2D g, int width, int height) {
        g.setPaint(new GradientPaint(0, 0, new Color(1, 2, 4), width, height, new Color(5, 6, 9)));
        g.fillRect(0, 0, width, height);

        // Very subtle vignette: no blue glow/blob treatment.
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.22f));
        g.setPaint(new GradientPaint(
            width / 2f, 0, new Color(18, 18, 22, 0),
            width / 2f, height, new Color(0, 0, 0, 125)
        ));
        g.fillRect(0, 0, width, height);
        g.setComposite(AlphaComposite.SrcOver);
    }

    private void paintStars(Graphics2D g) {
        for (Star star : stars) {
            float pulse = 0.78f + 0.22f * (float) Math.sin(star.twinkleTime);
            int alpha = Math.max(20, Math.min(220, (int) (star.alpha * pulse)));
            g.setColor(new Color(255, 255, 255, alpha));
            g.fillOval((int) star.x, (int) star.y, star.size, star.size);
        }
    }

    private void paintQuote(Graphics2D g, int width, int height) {
        if (activeQuote == null) return;

        long age = System.currentTimeMillis() - activeQuote.startedAt;
        float progress = Math.min(1f, age / (float) QUOTE_TRAVEL_MS);
        float y = height + 28f - progress * (height + 70f);
        float fadeIn = Math.min(1f, age / 1_500f);
        float fadeOut = Math.min(1f, (QUOTE_TRAVEL_MS - age) / 2_000f);
        float alpha = Math.max(0f, Math.min(fadeIn, fadeOut));

        g.setFont(new Font("SansSerif", Font.PLAIN, 14));
        int textWidth = g.getFontMetrics().stringWidth(activeQuote.text);
        int x = (width - textWidth) / 2;
        g.setColor(new Color(255, 255, 255, Math.round(205 * alpha)));
        g.drawString(activeQuote.text, x, Math.round(y));
    }

    private void resizeStarfield(int width, int height) {
        stars.clear();
        for (int i = 0; i < STAR_COUNT; i++) {
            double depth = random.nextDouble();
            int size = depth > 0.88 ? 2 : 1;
            int alpha = 45 + random.nextInt(145);
            double speed = 3.0 + depth * 10.0;
            double angle = random.nextDouble() * Math.PI * 2.0;
            stars.add(new Star(
                random.nextDouble() * width,
                random.nextDouble() * height,
                Math.cos(angle) * speed,
                Math.sin(angle) * speed,
                size,
                alpha,
                random.nextDouble() * Math.PI * 2.0,
                0.7 + random.nextDouble() * 1.6
            ));
        }
    }

    private void createBufferStrategySafely() {
        try {
            createBufferStrategy(3);
        } catch (IllegalStateException exception) {
            try {
                Thread.sleep(50L);
            } catch (InterruptedException interrupted) {
                Thread.currentThread().interrupt();
            }
            if (running) createBufferStrategySafely();
        }
    }

    private static void sleepPrecisely(long nanos) {
        long millis = nanos / 1_000_000L;
        int extraNanos = (int) (nanos % 1_000_000L);
        try {
            Thread.sleep(millis, extraNanos);
        } catch (InterruptedException interrupted) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void keyPressed(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.VK_ESCAPE) {
            running = false;
            if (frame != null) frame.dispose();
        }
    }

    @Override public void keyTyped(KeyEvent event) { }
    @Override public void keyReleased(KeyEvent event) { }

    private static final class Star {
        private double x;
        private double y;
        private final double velocityX;
        private final double velocityY;
        private final int size;
        private final int alpha;
        private double twinkleTime;
        private final double twinkleSpeed;

        private Star(double x, double y, double velocityX, double velocityY,
                     int size, int alpha, double twinkleTime, double twinkleSpeed) {
            this.x = x;
            this.y = y;
            this.velocityX = velocityX;
            this.velocityY = velocityY;
            this.size = size;
            this.alpha = alpha;
            this.twinkleTime = twinkleTime;
            this.twinkleSpeed = twinkleSpeed;
        }
    }

    private record Quote(String text, long startedAt) { }
}
