package Game;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import javax.swing.JPanel;

import XWindow.XWindow;

public class PhysicsDemo {
    BallData[] balls;
    Gravity gravity = new Gravity(0, 0.001);

    public PhysicsDemo() {
        balls = new BallData[200];
        for (int i = 0; i < balls.length; i++) {
            balls[i] = new BallData();
        }

        JPanel _demoPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                // TODO Auto-generated method stub
                super.paintComponent(g);
                g.setColor(Color.white);

                for (int i = 0; i < balls.length; i++) {
                    g.fillOval((int) balls[i].x - (int) (balls[i].radius),
                            (int) balls[i].y - (int) (balls[i].radius), (int) balls[i].radius * 2,
                            (int) balls[i].radius * 2);
                    for (int j = 0; j < balls.length; j++) {
                        if (i != j) {
                            if (balls[i].isContected(balls[j])) {
                                balls[i].applyContectPhysics(balls[j]);
                            }
                        }
                    }

                }
                for (int i = 0; i < balls.length; i++) {


                    if (balls[i].x - balls[i].radius < 0) {
                        balls[i].x = 0 + balls[i].radius;
                        balls[i].xVec = Math.abs(balls[i].xVec) * balls[i].reflectivity;
                    } else if (balls[i].x + balls[i].radius > getWidth()) {
                        balls[i].x = getWidth() - balls[i].radius;
                        balls[i].xVec = -Math.abs(balls[i].xVec) * balls[i].reflectivity;
                    }

                    if (balls[i].y - balls[i].radius < 0) {
                        balls[i].y = 0 + balls[i].radius;
                        balls[i].yVec = Math.abs(balls[i].yVec) * balls[i].reflectivity;
                        balls[i].isOnGround = false;
                    } else if (balls[i].y + balls[i].radius > getHeight()) {
                        balls[i].y = getHeight() - balls[i].radius;
                        balls[i].yVec = -Math.abs(balls[i].yVec) * balls[i].reflectivity;
                        balls[i].isOnGround = true;
                    } else if (balls[i].y + balls[i].radius == getHeight()) {
                        balls[i].isOnGround = true;
                    } else {
                        balls[i].isOnGround = false;
                    }

                    balls[i].x += balls[i].xVec;
                    balls[i].y += balls[i].yVec;

                    balls[i].xVec += gravity.xGravity;
                    if (balls[i].isOnGround == false) {
                        balls[i].yVec += gravity.yGravity;
                    }

                }
                repaint();
            }
        };
        _demoPanel.setBackground(null);

        XWindow _demoWindow = new XWindow(800, 700, "demo physics");
        _demoWindow.addContent(_demoPanel);
        _demoWindow.setVisible(true);
    }

    public static void main(String[] args) {
        new PhysicsDemo();
    }
}

class BallData {
    double x;
    double y;
    double xVec;
    double yVec;
    double radius;
    double mass;
    double reflectivity;
    boolean isOnGround = false;
    Random random = new Random();

    public BallData() {
        this.x = random.nextInt(700);
        this.y = random.nextInt(600);
        this.xVec = random.nextDouble(-2.0, 2.0);
        this.yVec = random.nextDouble(-2.0, 2.0);
        this.radius = random.nextInt(10, 20);
        this.mass = this.radius;
        this.reflectivity = 0.8;
    }

    boolean isContected(BallData targetBall) {
        double distance = Math.sqrt(Math.pow(this.x - targetBall.x, 2) + Math.pow(this.y - targetBall.y, 2));
        if (distance <= this.radius + targetBall.radius) {
            return true;
        }
        return false;
    }

    void applyContectPhysics(BallData targetBall) {
        double distance = Math.sqrt(Math.pow(this.x - targetBall.x, 2) + Math.pow(this.y - targetBall.y, 2));
        double currnetVectorSize = Math.sqrt(Math.pow(this.xVec, 2) + Math.pow(this.yVec, 2));
        double xDistance = targetBall.x - this.x;
        double yDistance = targetBall.y - this.y;
        double massDifference = targetBall.mass / this.mass;
        double totalReflectivity = this.reflectivity * targetBall.reflectivity;

        this.xVec = this.xVec + xDistance / distance * currnetVectorSize * massDifference * -1 * totalReflectivity;
        this.yVec = this.yVec + yDistance / distance * currnetVectorSize * massDifference * -1 * totalReflectivity;

        // if(distance<this.radius+targetBall.radius){
        // double overlap = this.radius+targetBall.radius-distance;
        // double ratio = this.mass/targetBall.mass;
        // this.x-=overlap*ratio;
        // this.y-=overlap*ratio;
        // }
    }
}

class Gravity {
    double xGravity;
    double yGravity;

    public Gravity(double xGravity, double yGravity) {
        this.xGravity = xGravity;
        this.yGravity = yGravity;
    }
}