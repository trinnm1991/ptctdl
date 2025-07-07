package edu.usfca.ds.panels;

import edu.usfca.ds.DSWindow;
import edu.usfca.ds.views.DSView;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.Hashtable;
import java.util.Vector;

public class DSPanel extends JPanel {

    protected DSWindow window;
    protected JButton stepButton;
    protected JButton pauseButton;
    protected JButton skipButton;
    protected JSlider speed;
    protected JLabel runningMsg;
    protected boolean paused;
    protected boolean running;
    protected DSView view;
    protected Vector DirtyDisplay;

    public DSPanel(DSWindow window) {
        super(new BorderLayout());
        DirtyDisplay = new Vector();
        this.window = window;
    }

    protected void Animate(final int Function, final Object param1, final Object param2) {

        Thread v = new Thread() {
            public void run() {
                StartingAnimation();
                view.Animate(Function, param1, param2);
                EndingAnimation();
                repaint();
            }
        };
        v.start();
    }



    protected void Animate(final int Function, final Object param) {

        Thread v = new Thread() {
            public void run() {
                StartingAnimation();
                view.Animate(Function, param);
                EndingAnimation();
                repaint();
            }
        };
        v.start();
    }

    protected void Animate(final int Function) {

        Thread v = new Thread() {
            public void run() {
                StartingAnimation();
                view.Animate(Function);
                EndingAnimation();
                repaint();
            }
        };
        v.start();
    }

    protected void SetupAnimationPanel(final DSView view) {
        Box bottombox = Box.createHorizontalBox();

        Box buttonLabelBox = Box.createVerticalBox();
        Box buttonBox = Box.createHorizontalBox();



        runningMsg = new JLabel("Dừng hoạt ảnh");

        pauseButton = new JButton("Tạm dừng");
        pauseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                if (paused) {
                    pauseButton.setText("Tạm dừng");
                    view.go();
                    paused = false;
                    if (running) {
                        runningMsg.setText("Hoạt ảnh đang chạy");
                        runningMsg.setForeground(Color.GREEN);
                    }
                    stepButton.setEnabled(false);
                } else {
                    pauseButton.setText("Bắt đầu");
                    if (running) {
                        runningMsg.setText("Hoạt ảnh đã tạm dừng");
                        runningMsg.setForeground(Color.RED);
                        stepButton.setEnabled(true);
                    }
                    paused = true;
                    view.pause();

                }
                changeDone();
            }
        });
        view.setDelay(25);

        buttonBox.add(pauseButton);


        stepButton = new JButton("Từng bước");
        stepButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                view.step();
                changeDone();
            }
        });
        buttonBox.add(stepButton);

        stepButton.setEnabled(false);

        skipButton = new JButton("Bỏ qua");
        skipButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                view.skip();
                changeDone();
            }
        });
        buttonBox.add(skipButton);
        stepButton.setEnabled(false);


        buttonLabelBox.add(runningMsg, BorderLayout.WEST);
        buttonLabelBox.add(buttonBox, BorderLayout.WEST);
        bottombox.add(buttonLabelBox);

        speed = new JSlider();
        speed.setBorder(BorderFactory.createTitledBorder("Tốc độ hoạt ảnh"));

        Hashtable labelTable = new Hashtable();
        labelTable.put(new Integer(0), new JLabel("Nhanh"));
        labelTable.put(new Integer(100), new JLabel("Chậm"));
        speed.setLabelTable(labelTable);
        speed.setInverted(true);
        speed.setPaintLabels(true);
        speed.setValue(25);

        speed.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent event) {
                view.setDelay(speed.getValue());
                changeDone();
            }
        });
        bottombox.add(speed);


        this.add(bottombox, BorderLayout.SOUTH);

    }

    public void DisableSpecific() {
        /* Override this method to disable specific buttons */
    }

    public void StartingAnimation() {


        running = true;
        if (paused) {
            runningMsg.setText("Hoạt ảnh đã tạm dừng");
            runningMsg.setForeground(Color.RED);
            stepButton.setEnabled(true);
        } else {
            runningMsg.setText("Hoạt ảnh đang chạy");
            runningMsg.setForeground(Color.GREEN);
            stepButton.setEnabled(false);
        }
        skipButton.setEnabled(true);
        DisableSpecific();
    }

    protected int ExtractInt(String text,int digits) {
        int extracted;

        try {
            extracted = Integer.parseInt(ExtractString(text,digits));
        } catch (Exception e) {
            extracted = Integer.MAX_VALUE;
        }
        return extracted;
    }


    protected String ExtractString(String val, int maxsize) {
        if (val.length() <= maxsize)
            return val;
        return val.substring(0,maxsize);
    }




    public void EnableSpecific() {
/* Override this method to enable specific buttons */

    }

    public void EndingAnimation() {

        running = false;
        runningMsg.setText("Hoạt ảnh đã hoàn tất.");
        runningMsg.setForeground(Color.BLACK);
        stepButton.setEnabled(false);
        skipButton.setEnabled(false);
        EnableSpecific();
    }

    // This method mark the document as dirty (the Save menu item is enabled)
    public void changeDone() {
        window.getDocument().changeDone();
    }

    public BufferedImage getImage() {
        return view.getImage();
    }

    public String getEPS() {
        return view.getEPS();
    }
}
