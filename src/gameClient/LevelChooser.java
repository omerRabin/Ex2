package gameClient;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class LevelChooser {
    int ChosenLevel = 0;
    JLabel label;
    JFrame frame;
    JPanel panel;

    public LevelChooser() {
        panel = new JPanel();
        frame = new JFrame();
        frame.setSize(350, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        panel.setLayout(null);

        label = new JLabel("Enter level");
        label.setBounds(10, 20, 80, 25);
        panel.add(label);

        JTextField userText = new JTextField(20);
        userText.setBounds(100, 20, 165, 25);
        panel.add(userText);

        JLabel choose = new JLabel("No level chosen");
        choose.setBounds(100, 110, 300, 25);
        panel.add(choose);

        JButton button = new JButton("Choose");
        button.setBounds(10, 80, 80, 25);
        panel.add(button);
        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                ChosenLevel = Integer.parseInt(userText.getText());
                choose.setText("Chosen level: " + ChosenLevel);
            }
        });


        frame.setVisible(true);
    }
}