package dain;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TokenEntryFrame extends JFrame {


        public static final int WIDTH_FRAME = 800;
        public static final int HEIGHT_FRAME = 200;

    public TokenEntryFrame() {

        JLabel onlyLabel = new JLabel("Please enter the token for your Discord bot application:");
        onlyLabel.setBounds(20, 20, 360, 20);
        this.add(onlyLabel);

        JTextField onlyTextField = new JTextField("");
        onlyTextField.setBounds(20, 40, 720, 20);
        this.add(onlyTextField);

        JButton saveButton = new JButton("Save");
        saveButton.setBounds(20, 80, 80, 20);
        this.add(saveButton);
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource().equals(saveButton)) {
                    Settings.DISCORD_TOKEN = onlyTextField.getText();
                    Settings.writeToFile();

                    for (int i = 0; i < 8; i++) { /* we want to wait some time before closing the window
                                                    because the main window doesn't pop up immediately
                                                    and the user may become confused */
                        sleap(250);
                        TokenEntryFrame.this.setTitle("Please wait."); // give the user feedback in the title bar
                        sleap(250);
                        TokenEntryFrame.this.setTitle("Please wait.."); // so that they know their click had an effect
                        sleap(250);
                        TokenEntryFrame.this.setTitle("Please wait...");
                        sleap(250);
                        TokenEntryFrame.this.setTitle("Please wait");
                    }

                    TokenEntryFrame.this.dispose();
                }
            }
        });

        this.setLayout(null);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //this.setIconImage(logoIcon.getImage());
        this.setTitle("Discord Bot Token Entry");
        this.setType(Window.Type.POPUP);
        this.setLocation(320, 120);
        this.setSize(WIDTH_FRAME, HEIGHT_FRAME);
        this.setVisible(true);
    }

    private void sleap(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
