package Game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.util.Random;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JPanel;

import XWindow.XWindow;

class Ball {
    Random rand = new Random();
    double ballX = rand.nextDouble(0, 450);
    double ballY = 70;
    double ballXVec = rand.nextDouble(0.1, 0.5);
    double ballYVec = 0.5;

    public Ball(double ballX, double ballY, double ballXVec, double ballYVec) {
        this.ballX = ballX;
        this.ballY = ballY;
        this.ballXVec = ballXVec;
        this.ballYVec = ballYVec;
    }
}

public class BallGame {
    XWindow gameScreen = new XWindow(500, 500, "XYZ: Ball");
    private Ball[] balls = new Ball[1];
    private JPanel gamePanel = new JPanel() {
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.white);
            g.fillRoundRect(200, 50, 100, 20, 20, 20);

            int x = curXLocation;
            if (curXLocation < 50)
                x = 50;
            if (curXLocation > 450)
                x = 450;

            g.fillRoundRect(x - 50, 400, 100, 20, 20, 20);

            g.setColor(Color.gray);
            for (int ballCount = 0; ballCount < balls.length; ballCount++) {
                g.fillOval((int) balls[ballCount].ballX, (int) balls[ballCount].ballY, 30, 30);
            }

            g.setColor(Color.darkGray);
            g.setFont(new Font("", Font.BOLD, 50));
            int scoreXLocation = g.getFontMetrics().stringWidth(score + "");
            g.drawString(score + "", (500 - scoreXLocation) / 2, 250);

            if (gameUnderway == true) {
                for (int ballCount = 0; ballCount < balls.length; ballCount++) {
                    balls[ballCount].ballX += balls[ballCount].ballXVec;
                    balls[ballCount].ballY += balls[ballCount].ballYVec;

                    double realVector = Math.sqrt(Math.pow(balls[ballCount].ballXVec, 2)
                            + Math.pow(balls[ballCount].ballYVec, 2));
                    double targetVector = (score / 2) * 0.02 + 0.6;

                    balls[ballCount].ballXVec = balls[ballCount].ballXVec * targetVector / realVector;
                    balls[ballCount].ballYVec = balls[ballCount].ballYVec * targetVector / realVector;

                    if ((balls[ballCount].ballX < 0) && gameUnderway) {
                        boundSound.setMicrosecondPosition(0);
                        balls[ballCount].ballXVec = Math.abs(balls[ballCount].ballXVec);
                        boundSound.start();
                    } else if (balls[ballCount].ballX + 30 > gameScreen.getWidth() && gameUnderway) {
                        boundSound.setMicrosecondPosition(0);
                        balls[ballCount].ballXVec = Math.abs(balls[ballCount].ballXVec) * -1;
                        boundSound.start();
                    }
                    if (balls[ballCount].ballY < 0 && gameUnderway) {
                        boundSound.setMicrosecondPosition(0);

                        balls[ballCount].ballYVec = Math.abs(balls[ballCount].ballYVec);
                        boundSound.start();
                    }

                    if (balls[ballCount].ballY >= gameScreen.getHeight()) {
                        if (balls.length == 1) {
                            gameUnderway = false;
                            gameScreen.setTitle("Game Over! Max Score: " + score);
                            balls[ballCount].ballYVec = 0;
                            balls[ballCount].ballXVec = 0;
                        }
                        Ball[] temp = new Ball[balls.length - 1];
                        int tempSkip = 0;
                        for (int i = 0; i < balls.length; i++) {
                            if (i == ballCount) {
                                tempSkip = 1;
                                continue;
                            }
                            temp[i - tempSkip] = balls[i];
                        }
                        balls = new Ball[temp.length];
                        balls = temp;
                        continue;
                    }

                    if (balls[ballCount].ballY + 30 >= 400 && balls[ballCount].ballY <= 410
                            && balls[ballCount].ballX + 15 > x - 40
                            && balls[ballCount].ballX + 15 < x + 40) {
                        MovableBarSound.setMicrosecondPosition(0);
                        MovableBarSound.start();
                        balls[ballCount].ballYVec *= -1;
                        balls[ballCount].ballY = 400 - 30;
                    } else if (isCollisionTwoCircle(balls[ballCount].ballX + 15, balls[ballCount].ballY + 15, 15,
                            x - 40, 410, 10)
                            && balls[ballCount].ballX + 15 < x - 40) {
                        MovableBarSound.setMicrosecondPosition(0);
                        MovableBarSound.start();
                        double xVecDefault = balls[ballCount].ballXVec;
                        double yVecDefault = balls[ballCount].ballYVec;
                        double VecDefault = Math.sqrt(Math.pow(xVecDefault, 2) + Math.pow(yVecDefault, 2));
                        double distance = Math.sqrt(Math.pow((x - 40) - (balls[ballCount].ballX + 15), 2)
                                + Math.pow((410) - (balls[ballCount].ballY + 15), 2));

                        balls[ballCount].ballXVec = balls[ballCount].ballXVec + VecDefault
                                * (((x - 40) - (balls[ballCount].ballX + 15)) / distance) * -2;
                        balls[ballCount].ballYVec = balls[ballCount].ballYVec + VecDefault
                                * (((410) - (balls[ballCount].ballY + 15)) / distance) * -2;
                    } else if (isCollisionTwoCircle(balls[ballCount].ballX + 15, balls[ballCount].ballY + 15, 15,
                            x + 40, 410, 10)) {
                        MovableBarSound.setMicrosecondPosition(0);
                        MovableBarSound.start();
                        double xVecDefault = balls[ballCount].ballXVec;
                        double yVecDefault = balls[ballCount].ballYVec;
                        double VecDefault = Math.sqrt(Math.pow(xVecDefault, 2) + Math.pow(yVecDefault, 2));
                        double distance = Math.sqrt(Math.pow((x + 40) - (balls[ballCount].ballX + 15), 2)
                                + Math.pow((410) - (balls[ballCount].ballY + 15), 2));

                        balls[ballCount].ballXVec = balls[ballCount].ballXVec + (VecDefault)
                                * (((balls[ballCount].ballX + 15) - (x + 40)) / distance) * 2;
                        balls[ballCount].ballYVec = balls[ballCount].ballYVec + VecDefault
                                * (((410) - (balls[ballCount].ballY + 15)) / distance) * -2;
                    }

                    if ((balls[ballCount].ballY <= 70 && balls[ballCount].ballY >= 20)
                            && (balls[ballCount].ballX + 15 >= 210 && balls[ballCount].ballX + 15 <= 290)) {
                        scoreAdd();
                        balls[ballCount].ballXVec = balls[ballCount].ballXVec;
                        balls[ballCount].ballYVec = balls[ballCount].ballYVec;

                        if (balls[ballCount].ballYVec < 0) {
                            balls[ballCount].ballY = 70;
                        } else if (balls[ballCount].ballYVec > 0) {
                            balls[ballCount].ballY = 50 - 30;
                        }
                        balls[ballCount].ballYVec *= -1;
                    } else if (isCollisionTwoCircle(balls[ballCount].ballX + 15, balls[ballCount].ballY + 15, 15, 210,
                            60, 10) && balls[ballCount].ballX + 15 <= 210) {
                        scoreAdd();
                        double xVecDefault = balls[ballCount].ballXVec;
                        double yVecDefault = balls[ballCount].ballYVec;
                        double VecDefault = Math.sqrt(Math.pow(xVecDefault, 2) + Math.pow(yVecDefault, 2));
                        double distance = Math.sqrt(Math.pow(210 - (balls[ballCount].ballX + 15), 2)
                                + Math.pow((60) - (balls[ballCount].ballY + 15), 2));

                        balls[ballCount].ballXVec = balls[ballCount].ballXVec + VecDefault
                                * ((210 - (balls[ballCount].ballX + 15)) / distance) * -2;
                        balls[ballCount].ballYVec = balls[ballCount].ballYVec + VecDefault
                                * (((balls[ballCount].ballY + 15) - (60)) / distance) * 2;

                    } else if (isCollisionTwoCircle(balls[ballCount].ballX + 15, balls[ballCount].ballY + 15, 15, 290,
                            60, 10) && balls[ballCount].ballX + 15 >= 290) {
                        scoreAdd();
                        double xVecDefault = balls[ballCount].ballXVec;
                        double yVecDefault = balls[ballCount].ballYVec;
                        double VecDefault = Math.sqrt(Math.pow(xVecDefault, 2) + Math.pow(yVecDefault, 2));
                        double distance = Math.sqrt(Math.pow((290 - (balls[ballCount].ballX + 15)), 2)
                                + Math.pow((60) - (balls[ballCount].ballY + 15), 2));

                        balls[ballCount].ballXVec = balls[ballCount].ballXVec + VecDefault
                                * (((balls[ballCount].ballX + 15) - (290)) / distance) * 2;
                        balls[ballCount].ballYVec = balls[ballCount].ballYVec + VecDefault
                                * (((balls[ballCount].ballY + 15) - (60)) / distance) * 2;
                    }
                }

            }
            repaint();
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
    };
    private MouseMotionAdapter mouseLocationAdapter = new MouseMotionAdapter() {
        @Override
        public void mouseMoved(MouseEvent e) {
            super.mouseMoved(e);
            curXLocation = e.getX();
        }
    };
    private int curXLocation = 0;

    private Random rand = new Random();

    private int score = 0;
    private int lastEventScore = 0;

    private boolean gameUnderway = false;
    private File boundSoundFile;
    private Clip boundSound;
    private File MovableBarSoundFile;
    private Clip MovableBarSound;
    private File TargetBarSoundFile;
    private Clip TargetBarSound;

    private MouseAdapter gameStartMouseAdapter = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent evt) {
            super.mouseClicked(evt);
            if (gameUnderway == false) {
                gameScreen.setTitle("XYZ: Ball");
                balls = new Ball[1];
                balls[0] = new Ball(rand.nextDouble(0, gameScreen.getWidth() - 30), 80, rand.nextDouble(0.1, 0.5), 0.5);
                score = 0;
                gameUnderway = true;
            }
        }
    };

    public BallGame() {
        try {
            boundSoundFile = new File("./src/Game/Bound.wav");
            boundSound = AudioSystem.getClip();
            boundSound.open(AudioSystem.getAudioInputStream(boundSoundFile));
            boundSound.setLoopPoints(0, 500);

            MovableBarSoundFile = new File("./src/Game/MovableBar.wav");
            MovableBarSound = AudioSystem.getClip();
            MovableBarSound.open(AudioSystem.getAudioInputStream(MovableBarSoundFile));
            MovableBarSound.setLoopPoints(0, 500);

            TargetBarSoundFile = new File("./src/Game/TargetBar.wav");
            TargetBarSound = AudioSystem.getClip();
            TargetBarSound.open(AudioSystem.getAudioInputStream(TargetBarSoundFile));
            TargetBarSound.setLoopPoints(0, 500);

        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("audioFail");
        }

        balls[0] = new Ball(rand.nextDouble(0, gameScreen.getWidth() - 30), 80, rand.nextDouble(0.1, 0.5), 0.5);
        gameScreen.setEnableMax(false);
        gamePanel.addMouseMotionListener(mouseLocationAdapter);
        gameScreen.addContent(gamePanel);
        gamePanel.setBackground(new Color(25, 25, 25));
        gamePanel.addMouseListener(gameStartMouseAdapter);
        gameScreen.setVisible(true);
    }

    public static void main(String[] args) {
        new BallGame();
    }

    private boolean isCollisionTwoCircle(double x1, double y1, double r1, double x2, double y2, double r2) {
        double distance = Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
        if (distance <= r1 + r2) {
            return true;
        } else
            return false;
    }

    private void scoreAdd() {
        score++;
        TargetBarSound.setMicrosecondPosition(0);
        TargetBarSound.start();
        if (score % 10 == 0 && score != lastEventScore) {
            lastEventScore = score;
            Ball[] temp = new Ball[balls.length + 1];
            for (int i = 0; i < balls.length; i++) {
                temp[i] = balls[i];
            }
            double VecDefault = Math.sqrt(Math.pow(balls[0].ballXVec, 2) + Math.pow(balls[0].ballYVec, 2));
            Random rand = new Random();
            double VecAngle = rand.nextDouble(0, 2 * Math.PI);

            temp[temp.length - 1] = new Ball(rand.nextDouble(0, gameScreen.getWidth() - 30), 80,
                    VecDefault * Math.cos(VecAngle), VecDefault * Math.sin(VecAngle));
            balls = temp;
        }
    }
}