package me.jacksondasheng.theMaze;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main {
    private static final JFrame frame = new JFrame("Score: 0");
    private static final ArrayList<HashMap<String, Integer>> blocked = new ArrayList<>();
    private static final int size = 11;
    private static int seed = new Random().nextInt();
    private static int score = 0;
    private static int x = 0;
    private static int y = 0;

    public static void main(String[] args) {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize((int) (screen.getHeight() * 0.75), (int) (screen.getHeight() * 0.75));
        frame.setLocation((int) (screen.getWidth() / 2 - frame.getWidth() / 2), (int) (screen.getHeight() / 2 - frame.getHeight() / 2));
        frame.addKeyListener(
                new KeyListener() {
                    @Override
                    public void keyPressed(KeyEvent event) {
                        switch(event.getKeyCode()) {
                            case 38: case 87: move(0, -1); break;
                            case 37: case 65: move(-1, 0); break;
                            case 40: case 83: move(0, 1); break;
                            case 39: case 68: move(1, 0); break;
                            case 32: {
                                if(isBlocked(x, y - 1) && isBlocked(x - 1, y) && isBlocked(x, y + 1) && isBlocked(x + 1, y)) {
                                    x = 0;
                                    y = 0;
                                    score = 0;
                                    seed = new Random().nextInt();
                                    blocked.clear();
                                    frame.repaint();
                                }
                            }
                        }
                    }

                    @Override
                    public void keyTyped(KeyEvent event) {}

                    @Override
                    public void keyReleased(KeyEvent event) {}
                }
        );

        frame.add(
                new JPanel() {
                    @Override
                    public void paint(Graphics graphics) {
                        for(int i = 0; i < size; i++) {
                            for(int j = 0; j < size; j++) {
                                if(isBlocked(x + i - size / 2, y + j - size / 2)) {
                                    graphics.setColor(blocked.contains(map(x + i - size / 2, y + j - size / 2)) ? Color.CYAN : Color.BLACK);
                                    graphics.fillRect(
                                            i * frame.getWidth() / size,
                                            j * frame.getHeight() / size,
                                            frame.getWidth() / size + 1,
                                            frame.getHeight() / size + 1
                                    );
                                }
                            }
                        }

                        graphics.setColor(Color.BLUE);
                        graphics.fillRect(
                                size / 2 * frame.getWidth() / size,
                                size / 2 * frame.getHeight() / size,
                                frame.getWidth() / size + 1,
                                frame.getHeight() / size + 1
                        );

                        frame.setTitle("Score: " + score);
                    }
                }
        );

        frame.setVisible(true);
    }

    private static void move(int xOffset, int yOffset) {
        if(!isBlocked(x + xOffset, y + yOffset)) {
            blocked.add(map(x, y));
            x += xOffset;
            y += yOffset;
            score++;
            frame.repaint();
        }
    }

    private static boolean isBlocked(int x, int y) {
        if((blocked.contains(map(x, y))) || (Math.abs(x) % 2 == 1 && Math.abs(y) % 2 == 1)) {
            return true;
        } else if(x % 2 == 0 && y % 2 == 0) {
            return false;
        }

        return new Random(seed + ((long) x * x * 0x4c1906) + (x * 0x5ac0dbL) + ((long) y * y) * 0x4307a7L + (y * 0x5f24fL) ^ 0x3ad8025fL).nextBoolean();
    }

    private static HashMap<String, Integer> map(int x, int y) {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("x", x);
        map.put("y", y);
        return map;
    }
}
