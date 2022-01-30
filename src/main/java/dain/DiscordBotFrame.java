package dain;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Objects;

public class DiscordBotFrame extends JFrame {

    public static final int WIDTH_FRAME = 800;
    public static final int HEIGHT_FRAME = 650;

    private final GridBagLayout layout = new GridBagLayout();
    private final GridBagConstraints constraints = new GridBagConstraints();

    public DiscordBotFrame() {

        JLabel logoLabel = new JLabel();
        ImageIcon logoIcon = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("logo.png"))); // chat bridge graphic
        logoLabel.setIcon(logoIcon);
        addComponent(logoLabel, layout, 0, 0, 1, 2, GridBagConstraints.BOTH);

        JLabel settingsLabel = new JLabel("Settings"); // "Settings" title above button
        settingsLabel.setVerticalAlignment(SwingConstants.BOTTOM);
        addComponent(settingsLabel, layout, 1, 0, 1, 1, GridBagConstraints.NONE);

        JButton settingsButton = new JButton(); // no text on settings button just gear icon
        settingsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                if(event.getSource() == settingsButton) {
                    new SettingsFrame();
                }
            }
        });
        ImageIcon settingsIcon = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("settings-icon.png")));
        settingsButton.setIcon(settingsIcon);
        settingsButton.setToolTipText("Open settings pane");
        settingsButton.setSize(20, 30);
        addComponent(settingsButton, layout, 1, 1, 1, 1, GridBagConstraints.NONE);

        JLabel logLabel = new JLabel("Log"); // "Log" text above the log
        logLabel.setHorizontalAlignment(JLabel.LEFT);
        addComponent(logLabel, layout, 0, 2, 1, 1, GridBagConstraints.HORIZONTAL);

        JTextArea logTextArea = new JTextArea(24, 64); // log text area
        logTextArea.setEnabled(false);
        JScrollPane logScrollPane = new JScrollPane(logTextArea);
        logScrollPane.setWheelScrollingEnabled(false); // do not allow scrolling with mouse scroll wheel
        logScrollPane.setHorizontalScrollBarPolicy(31); // 31 = never show horizontal scroll bar
        logScrollPane.setVerticalScrollBarPolicy(21); // 21 = never show vertical scroll bar
        addComponent(logScrollPane, layout, 0, 3, 2, 1, GridBagConstraints.NONE);
        Logger.setTextArea(logTextArea);

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
            Logger.log("thread interrupted exception", Logger.LoggingLevel.ERROR);
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