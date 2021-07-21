package dain;

import com.apple.laf.AquaButtonBorder;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;

public class SettingsFrame extends JFrame {

    public static final int WIDTH_FRAME = 360;
    public static final int HEIGHT_FRAME = 330;
    public static final int WIDTH_LEFT_PANEL = 160;

    private final GridBagConstraints constraints = new GridBagConstraints();
    private static final Color lightLightGray = new Color(223, 223, 223);
    private JTextField[] textFields;

    public SettingsFrame() {

        /*
            creating left panel
         */
        String[] labels = new String[]{
                "Command Prefix:",
                "Server Names:",
                "Server IPs:",
                "RCon Ports:",
                "RCon Passwords:",
                "FTP Usernames:",
                "FTP Passwords:",
                "Discord Bot Token:",
                "Discord Channel IDs:",
                "Discord Channel Names:"
        };
        constraints.anchor = GridBagConstraints.EAST;
        GridBagLayout leftgbl = new GridBagLayout();
        JPanel leftPanel = new JPanel();
        for (int i = 0; i < labels.length; i++) {
            JLabel label = new JLabel(labels[i]);
            label.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
            if (i % 2 == 0) {
                label.setBackground(lightLightGray);
            }
            label.setOpaque(true);
            addComponent(label, leftgbl, leftPanel, 0, i, 1, 1);
        }
        leftPanel.setPreferredSize(new Dimension(160, 0)); //set width of left panel (height changes with window height)
        leftPanel.setLayout(leftgbl);

        /*
            creating right panel
         */
        int widest = 0;
        String[] textFieldNames = new String[]{
                Settings.COMMAND_PREFIX,
                combine(Settings.SERVER_NAMES),
                combine(Settings.SERVER_IPS),
                combine(Settings.RCON_PORTS),
                combine(Settings.RCON_PASSWORDS),
                combine(Settings.FTP_USERNAMES),
                combine(Settings.FTP_PASSWORDS),
                Settings.DISCORD_TOKEN,
                combine(Settings.DISCORD_CHANNEL_IDS),
                combine(Settings.DISCORD_CHANNEL_NAMES)
        };
        constraints.anchor = GridBagConstraints.WEST;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridwidth = 200;
        textFields = new JTextField[textFieldNames.length];
        GridBagLayout rightgbl = new GridBagLayout();
        JPanel rightPanel = new JPanel();
        for (int i = 0; i < textFieldNames.length; i++) {
            textFields[i] = new JTextField(textFieldNames[i]);
            if (i % 2 == 0) {
                textFields[i].setBackground(lightLightGray);
            }
            addComponent(textFields[i], rightgbl, rightPanel, 0, i, 1, 1);
            //textField.setSize(new Dimension(140, 0));
        }
        rightPanel.setLayout(rightgbl);

        /*
            creating bottom panel
         */
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource().equals(saveButton)) saveValues();
            }
        });
        bottomPanel.add(saveButton);

        /*
            creating frame from panels
         */
        // adding panels to frame
        this.add(leftPanel, BorderLayout.WEST);
        this.add(rightPanel, BorderLayout.CENTER);
        this.add(bottomPanel, BorderLayout.SOUTH);

        //boilerplatey junk
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //this.setIconImage(logoIcon.getImage());
        this.setTitle("Settings");
        this.setType(Window.Type.POPUP);
        this.setLocation(320, 120);

        //figuring out how big to make the window based on the width of the text in the settings fields
        this.setSize(1024, HEIGHT_FRAME); //arbitrary big number
        this.setVisible(true);
        // we get the width of the widest text field
        for (int i = 0; i < textFields.length; i++) widest = textFields[i].getWidth() > widest ? textFields[i].getWidth() : widest;
        //then set the size of the frame accordingly
        this.setSize(WIDTH_LEFT_PANEL + widest, HEIGHT_FRAME);
        this.setMinimumSize(this.getSize());

        //makes the frame 5% transparent (95% opaque)
        this.setOpacityWithDecoration(0.95F); //must be AFTER setVisible(true)!!!
    }

    // remember that the position starts at 0 but the size does not
    public void addComponent(Component component, GridBagLayout layoutIn, JPanel panelIn, int x, int y, int width, int height) {
        constraints.gridx = x;
        constraints.gridy = y;
        constraints.gridwidth = width;
        constraints.gridheight = height;
        layoutIn.setConstraints(component, constraints);
        panelIn.add(component);
    }

    private String combine(String[] stringArray) {
        String stringOut = "";
        for (int i = 0; i < stringArray.length; i++) {
            stringOut = stringOut + stringArray[i];
            if (i != stringArray.length - 1) stringOut = stringOut + " ";
        }
        return stringOut;
    }

    private void setOpacityWithDecoration(float opacity) {
        Field undecoratedField = null;
        try {
            undecoratedField = Frame.class.getDeclaredField("undecorated");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        undecoratedField.setAccessible(true);
        try {
            undecoratedField.set(this, true);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        this.setOpacity(opacity);
    }

    public void saveValues() {

        Settings.COMMAND_PREFIX = textFields[0].getText();

        Settings.SERVER_NAMES = textFields[1].getText().split(" ");
        Settings.SERVER_IPS = textFields[2].getText().split(" ");
        Settings.RCON_PORTS = textFields[3].getText().split(" ");
        Settings.RCON_PASSWORDS = textFields[4].getText().split(" ");

        Settings.FTP_USERNAMES = textFields[5].getText().split(" ");
        Settings.FTP_PASSWORDS = textFields[6].getText().split(" ");

        Settings.DISCORD_TOKEN = textFields[7].getText();
        Settings.DISCORD_CHANNEL_IDS = textFields[8].getText().split(" ");
        Settings.DISCORD_CHANNEL_NAMES = textFields[9].getText().split(" ");

        Settings.writeToFile();
    }
}
