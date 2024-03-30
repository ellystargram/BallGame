package XWindow;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

public class XWindow extends JFrame {
    private ImageIcon closeAlive = new ImageIcon(new ImageIcon("./src/XWindow/closeAlive.png").getImage()
            .getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH));
    private ImageIcon closeDead = new ImageIcon(new ImageIcon("./src/XWindow/closeDead.png").getImage()
            .getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH));
    private ImageIcon miniAlive = new ImageIcon(new ImageIcon("./src/XWindow/miniAlive.png").getImage()
            .getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH));
    private ImageIcon miniDead = new ImageIcon(new ImageIcon("./src/XWindow/miniDead.png").getImage()
            .getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH));
    private ImageIcon maxAlive = new ImageIcon(new ImageIcon("./src/XWindow/maxAlive.png").getImage()
            .getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH));
    private ImageIcon maxDead = new ImageIcon(new ImageIcon("./src/XWindow/maxDead.png").getImage()
            .getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH));
    private ImageIcon unavail = new ImageIcon(new ImageIcon("./src/XWindow/unavail.png").getImage()
            .getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH));

    private JLabel titleBar = new JLabel("Untitled");
    private JLabel close = new JLabel(closeDead);
    private JLabel mini = new JLabel(miniDead);
    private JLabel max = new JLabel(maxDead);
    private JPanel contentScreen = new JPanel(new BorderLayout());

    private Color titleBarColor = new Color(0, 0, 0);
    private Color titleBarTextColor = new Color(255, 255, 255);
    private Color contentScreenBackgroundColor = new Color(64, 64, 64);

    private int xSize = 500;
    private int ySize = 500;
    private Point location = new Point(0, 0);

    private boolean enableMini = true;
    private boolean enableMax = true;

    private MouseMotionAdapter buttonsAnimationMouseMotionAdapter = new MouseMotionAdapter() {
        @Override
        public void mouseMoved(MouseEvent evt) {
            super.mouseMoved(evt);
            boolean buttonHover = false;
            if (isFocused() == false) {
                close.setIcon(unavail);
                mini.setIcon(unavail);
                max.setIcon(unavail);
                return;
            } else if (evt.getSource() == close) {
                buttonHover = true;
            } else if (evt.getSource() == mini && enableMini) {
                buttonHover = true;
            } else if (evt.getSource() == max && enableMax) {
                buttonHover = true;
            }

            if (buttonHover) {
                close.setIcon(closeAlive);
                if (enableMini) {
                    mini.setIcon(miniAlive);
                } else {
                    mini.setIcon(unavail);
                }
                if (enableMax) {
                    max.setIcon(maxAlive);
                } else {
                    max.setIcon(unavail);
                }
            } else {
                close.setIcon(closeDead);
                if (enableMini) {
                    mini.setIcon(miniDead);
                } else {
                    mini.setIcon(unavail);
                }
                if (enableMax) {
                    max.setIcon(maxDead);
                } else {
                    max.setIcon(unavail);
                }
            }
        }
    };
    private MouseMotionAdapter titleBarMovMouseMotionAdapter = new MouseMotionAdapter() {
        @Override
        public void mouseDragged(MouseEvent evt) {
            super.mouseDragged(evt);
            Point p = evt.getLocationOnScreen();
            setLocation(p.x - location.x, p.y - location.y);
        }
    };
    private MouseAdapter titleBarMouseAdapter = new MouseAdapter() {
        @Override
        public void mousePressed(MouseEvent evt) {
            super.mousePressed(evt);
            location = evt.getPoint();
        }
    };

    private WindowFocusListener xWindowFocusListener = new WindowFocusListener() {
        @Override
        public void windowGainedFocus(WindowEvent e) {
            close.setIcon(closeDead);
            if (enableMini) {
                mini.setIcon(miniDead);
            } else {
                mini.setIcon(unavail);
            }
            if (enableMax) {
                max.setIcon(maxDead);
            } else {
                max.setIcon(unavail);
            }
        }

        @Override
        public void windowLostFocus(WindowEvent e) {
            close.setIcon(unavail);
            mini.setIcon(unavail);
            max.setIcon(unavail);
        }
    };

    public XWindow(int xSize, int ySize, String title) {
        this.xSize = xSize;
        this.ySize = ySize;
        this.titleBar.setText(title);
        defaultSettings();
    }

    private void defaultSettings() {
        setSize(xSize, ySize);
        setLocationRelativeTo(null);
        setUndecorated(true);
        setDefaultCloseOperation(2);
        addWindowFocusListener(xWindowFocusListener);

        contentScreen.setBackground(contentScreenBackgroundColor);
        add(contentScreen);

        installTitleBar();

        if (enableMini) {
            mini.setIcon(miniDead);
        } else {
            mini.setIcon(unavail);
        }
        if (enableMax) {
            max.setIcon(maxDead);
        } else {
            max.setIcon(unavail);
        }

        setVisible(true);
    }

    private void installTitleBar() {
        titleBar.setOpaque(true);
        titleBar.setBackground(titleBarColor);
        titleBar.setForeground(titleBarTextColor);
        titleBar.setHorizontalAlignment(JLabel.CENTER);
        titleBar.setVerticalAlignment(JLabel.CENTER);
        titleBar.setPreferredSize(new Dimension(xSize, 30));
        titleBar.addMouseMotionListener(buttonsAnimationMouseMotionAdapter);
        titleBar.addMouseMotionListener(titleBarMovMouseMotionAdapter);
        titleBar.addMouseListener(titleBarMouseAdapter);

        add(titleBar, "North");

        close.setBounds(5, 5, 20, 20);
        close.addMouseMotionListener(buttonsAnimationMouseMotionAdapter);
        close.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                super.mouseClicked(evt);
                if (evt.getButton() == MouseEvent.BUTTON1)
                    System.exit(0);
            }
        });

        mini.setBounds(35, 5, 20, 20);
        mini.addMouseMotionListener(buttonsAnimationMouseMotionAdapter);
        mini.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                super.mouseClicked(evt);
                if (evt.getButton() == MouseEvent.BUTTON1 && enableMini)
                    setState(ICONIFIED);
            }
        });

        max.setBounds(65, 5, 20, 20);
        max.addMouseMotionListener(buttonsAnimationMouseMotionAdapter);
        max.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                super.mouseClicked(evt);
                if (evt.getButton() == MouseEvent.BUTTON1 && enableMax)
                    if (getExtendedState() == MAXIMIZED_BOTH) {
                        setExtendedState(NORMAL);
                        setSize(xSize, ySize);
                        setLocation(location);
                    } else {
                        location = getLocation();
                        setExtendedState(MAXIMIZED_BOTH);
                    }
            }
        });

        titleBar.add(close);
        titleBar.add(mini);
        titleBar.add(max);

        contentScreen.addMouseMotionListener(buttonsAnimationMouseMotionAdapter);
    }

    public Component addContent(Component comp) {
        return contentScreen.add(comp);
    }

    public void addContent(PopupMenu popup) {
        contentScreen.add(popup);
    }

    public void addContent(Component comp, Object constraints) {
        contentScreen.add(comp, constraints);
    }

    public Component addContent(Component comp, int index) {
        return contentScreen.add(comp, index);
    }

    public Component addContent(String name, Component comp) {
        return contentScreen.add(name, comp);
    }

    public void addContent(Component comp, Object constraints, int index) {
        contentScreen.add(comp, constraints, index);
    }

    public void setEnableMini(boolean enableMini) {
        this.enableMini = enableMini;
        if (enableMini) {
            mini.setIcon(miniDead);
        } else {
            mini.setIcon(unavail);
        }
    }
    public boolean getEnableMini(){
        return enableMini;
    }

    public void setEnableMax(boolean enableMax) {
        this.enableMax = enableMax;
        if (enableMax) {
            max.setIcon(maxDead);
        } else {
            max.setIcon(unavail);
        }
    }
    public boolean getEnableMax(){
        return enableMax;
    }

    @Override
    public void setTitle(String title) {
        titleBar.setText(title);
    }

    @Override
    public int getWidth() {
        return contentScreen.getWidth();
    }
    @Override
    public int getHeight() {
        return contentScreen.getHeight();
    }

}
