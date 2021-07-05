package dain;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.io.*;
import java.util.Scanner;

public class DiscordBotFrame extends JFrame {

    public static final int WIDTH_FRAME = 800;
    public static final int HEIGHT_FRAME = 650;
    public static final int X_COORD_LOGO = 20;
    public static final int Y_COORD_LOGO = 20;
    public static final int WIDTH_LOGO = 100;
    public static final int HEIGHT_LOGO = 100;
    public static final int X_COORD_BUTTON = X_COORD_LOGO + WIDTH_LOGO + 10;
    public static final int Y_COORD_BUTTON = Y_COORD_LOGO;
    public static final int WIDTH_BUTTON = WIDTH_LOGO;
    public static final int HEIGHT_BUTTON = HEIGHT_LOGO;

    private final GridBagLayout layout = new GridBagLayout();
    private final GridBagConstraints constraints = new GridBagConstraints();
    private final GridBagLayout layout2 = new GridBagLayout();
    private JPanel master;
    private JPanel slaveTop;

    public DiscordBotFrame(int asdf) {
        JPanel testPanel = new JPanel();
        setContentPane(testPanel);
        GridBagLayout gbl_testPanel = new GridBagLayout();
        gbl_testPanel.columnWidths = new int[]{0, 0};
        gbl_testPanel.rowHeights = new int[]{0};
        gbl_testPanel.columnWeights = new double[]{0.5, 0.5};
        gbl_testPanel.rowWeights = new double[]{1.0};
        testPanel.setLayout(gbl_testPanel);

        JPanel leftPanel = new JPanel();
        GridBagConstraints gbc_leftPanel = new GridBagConstraints();
        gbc_leftPanel.fill = GridBagConstraints.BOTH;
        gbc_leftPanel.insets = new Insets(0, 0, 0, 5);
        gbc_leftPanel.gridx = 0;
        gbc_leftPanel.gridy = 0;
        testPanel.add(leftPanel, gbc_leftPanel);
        GridBagLayout gbl_leftPanel = new GridBagLayout();
        gbl_leftPanel.columnWidths = new int[]{0};
        gbl_leftPanel.rowHeights = new int[]{0, 0};
        gbl_leftPanel.columnWeights = new double[]{0.0};
        gbl_leftPanel.rowWeights = new double[]{0.6, 0.4};
        leftPanel.setLayout(gbl_leftPanel);

        JLabel lbl00 = new JLabel("Upper Left");
        GridBagConstraints gbc_lbl00 = new GridBagConstraints();
        gbc_lbl00.anchor = GridBagConstraints.NORTH;
        gbc_lbl00.fill = GridBagConstraints.HORIZONTAL;
        gbc_lbl00.insets = new Insets(0, 0, 5, 0);
        gbc_lbl00.gridx = 0;
        gbc_lbl00.gridy = 0;
        leftPanel.add(lbl00, gbc_lbl00);

        JLabel lbl10 = new JLabel("Lower Left");
        GridBagConstraints gbc_lbl10 = new GridBagConstraints();
        gbc_lbl10.anchor = GridBagConstraints.NORTH;
        gbc_lbl10.fill = GridBagConstraints.HORIZONTAL;
        gbc_lbl10.gridx = 0;
        gbc_lbl10.gridy = 1;
        leftPanel.add(lbl10, gbc_lbl10);

        JPanel rightPanel = new JPanel();
        GridBagConstraints gbc_rightPanel = new GridBagConstraints();
        gbc_rightPanel.fill = GridBagConstraints.BOTH;
        gbc_rightPanel.gridx = 1;
        gbc_rightPanel.gridy = 0;
        testPanel.add(rightPanel, gbc_rightPanel);
        GridBagLayout gbl_rightPanel = new GridBagLayout();
        gbl_rightPanel.columnWidths = new int[]{0};
        gbl_rightPanel.rowHeights = new int[]{0, 0};
        gbl_rightPanel.columnWeights = new double[]{0.0};
        gbl_rightPanel.rowWeights = new double[]{0.4, 0.6};
        rightPanel.setLayout(gbl_rightPanel);

        JLabel lbl01 = new JLabel("Upper Right");
        GridBagConstraints gbc_lbl01 = new GridBagConstraints();
        gbc_lbl01.insets = new Insets(0, 0, 5, 0);
        gbc_lbl01.fill = GridBagConstraints.HORIZONTAL;
        gbc_lbl01.anchor = GridBagConstraints.NORTH;
        gbc_lbl01.gridx = 0;
        gbc_lbl01.gridy = 0;
        rightPanel.add(lbl01, gbc_lbl01);

        JLabel lbl11 = new JLabel("Lower Right");
        GridBagConstraints gbc_lbl11 = new GridBagConstraints();
        gbc_lbl11.anchor = GridBagConstraints.NORTH;
        gbc_lbl11.fill = GridBagConstraints.HORIZONTAL;
        gbc_lbl11.gridx = 0;
        gbc_lbl11.gridy = 1;
        rightPanel.add(lbl11, gbc_lbl11);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(WIDTH_FRAME, HEIGHT_FRAME);
        this.setResizable(false);
        this.setTitle("Discord-Minecraft Chat Bridge Bot");
        this.setLocation(120, 80);
        this.setVisible(true);
    }

    public DiscordBotFrame() {

        JLabel logoLabel = new JLabel();
        ImageIcon logoIcon = new ImageIcon("logo.png"); // chat bridge graphic
        logoLabel.setIcon(logoIcon);
        addComponent(logoLabel, layout, 0, 0, 1, 2, GridBagConstraints.BOTH);

        JLabel settingsLabel = new JLabel("Settings"); // "Settings" title above button
        settingsLabel.setVerticalAlignment(SwingConstants.BOTTOM);
        addComponent(settingsLabel, layout, 1, 0, 1, 1, GridBagConstraints.NONE);

        JButton settingsButton = new JButton(); // no text on settings button just gear icon
        settingsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                /*if(event.getSource() == settingsButton) {
                    setTitle("Saving...");
                    sleap((int)(Math.random() * 1000.0));
                    String nameText = nameTextField.getText();
                    String addressText = logTextArea.getText();
                    setTitle("Saving 34%");
                    sleap((int)(Math.random() * 1000.0));
                    try {
                        FileWriter out = new FileWriter("order.txt");
                        setTitle("Saving 78%");
                        sleap((int)(Math.random() * 5000.0));
                        out.write(nameText + "\n" + addressText);
                        out.close();
                        setTitle("Saved");
                    } catch (FileNotFoundException e) {
                        System.out.println("problem: file not found exception");
                        e.printStackTrace();
                    } catch (IOException e) {
                        System.out.println("problem: input/output exception");
                        e.printStackTrace();
                        setTitle("Unable to save");
                    }
                }*/
            }
        });
        ImageIcon settingsIcon = new ImageIcon("settings-icon.png");
        settingsButton.setIcon(settingsIcon);
        settingsButton.setToolTipText("Open settings pane");
        settingsButton.setSize(20, 30);
        addComponent(settingsButton, layout, 1, 1, 1, 1, GridBagConstraints.NONE);

        /*JButton button1 = new JButton();
        button1.setIcon(settingsIcon);
        addComponent(button1, 3, 1, 1, 1, GridBagConstraints.NONE);

        JButton button2 = new JButton();
        button2.setIcon(settingsIcon);
        addComponent(button2, 4, 1, 1, 1, GridBagConstraints.NONE);*/

        //addComponent(layout2, layout, 0, 0, 1, 1, GridBagConstraints.BOTH);

        JLabel logLabel = new JLabel("Log"); // "Log" text above the log
        logLabel.setHorizontalAlignment(JLabel.LEFT);
        addComponent(logLabel, layout, 0, 2, 1, 1, GridBagConstraints.HORIZONTAL);

        JTextArea logTextArea = new JTextArea(24, 64); // log text area
        logTextArea.setEnabled(false);
        addComponent(logTextArea, layout, 0, 3, 2, 1, GridBagConstraints.NONE);

        //constraints.insets = new Insets(1, 1, 1, 1);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(layout);
        this.setSize(WIDTH_FRAME, HEIGHT_FRAME);
        this.setResizable(false);
        this.setIconImage(logoIcon.getImage());
        this.setName("your mom");
        this.setTitle("Discord-Minecraft Chat Bridge Bot");
        this.setType(Type.NORMAL);
        this.setLocation(120, 80);
        //pack();
        this.setVisible(true);
    }

    private static void sleap(int ms) {
        try {
            Thread.sleep(ms);
        } catch(InterruptedException exception) {
            System.out.println("problem: thread interrupted exception");
        }
    }

    // remember that the position starts at 0 but the size does not
    public void addComponent(Component component, GridBagLayout layoutIn, int x, int y, int width, int height, int fill) {
        constraints.gridx = x;
        constraints.gridy = y;
        constraints.gridwidth = width;
        constraints.gridheight = height;
        constraints.fill = fill;
        layoutIn.setConstraints(component, constraints);
        super.add(component);
    }
}