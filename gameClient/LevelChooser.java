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
		frame.setSize(350,200);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(panel);
		
		panel.setLayout(null);
		
		label = new JLabel("Enter level");
		label.setBounds(10,20,80,25);
		panel.add(label);
		
		JTextField userText = new JTextField(20);
		userText.setBounds(100,20,165,25);
		panel.add(userText);
		
		JLabel choose = new JLabel("No level chosen");
		choose.setBounds(100,110,300,25);
		panel.add(choose);
		
		JButton button = new JButton("Choose");
		button.setBounds(10,80,80,25);
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
	
/*
	public LevelChooser() {//another option for choosing a level
		JFrame frame = new JFrame();

		JButton button1 = new JButton("level 1");
		button1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				ChosenLevel = 1;
				label.setText("Chosen level: " + ChosenLevel);
			}
		});
		label = new JLabel("No level chosen");

		panel = new JPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
		panel.setLayout(new GridLayout(0, 1));
		panel.add(label);
////////////////////////////////////////////////////////////////////////////////buttons
		panel.add(button1);
		JButton button2 = new JButton("level 2");
        panel.add(button2);
        button2.addActionListener(
                new ActionListener()
                {
                @Override
                   public void actionPerformed(ActionEvent event)
                   {
                	ChosenLevel = 2;
    				label.setText("Chosen level: " + ChosenLevel);
                   }
                }
                );
        JButton button3 = new JButton("level 3");
        panel.add(button3);
        button3.addActionListener(
                new ActionListener()
                {
                @Override
                   public void actionPerformed(ActionEvent event)
                   {
                	ChosenLevel = 3;
    				label.setText("Chosen level: " + ChosenLevel);
                   }
                }
                );
        JButton button4 = new JButton("level 4");
        panel.add(button4);
        button4.addActionListener(
                new ActionListener()
                {
                @Override
                   public void actionPerformed(ActionEvent event)
                   {
                	ChosenLevel = 4;
    				label.setText("Chosen level: " + ChosenLevel);
                   }
                }
                );
        JButton button5 = new JButton("level 5");
        panel.add(button5);
        button5.addActionListener(
                new ActionListener()
                {
                @Override
                   public void actionPerformed(ActionEvent event)
                   {
                	ChosenLevel = 5;
    				label.setText("Chosen level: " + ChosenLevel);
                   }
                }
                );
        JButton button6 = new JButton("level 6");
        panel.add(button6);
        button6.addActionListener(
                new ActionListener()
                {
                @Override
                   public void actionPerformed(ActionEvent event)
                   {
                	ChosenLevel = 6;
    				label.setText("Chosen level: " + ChosenLevel);
                   }
                }
                );
        JButton button7 = new JButton("level 7");
        panel.add(button7);
        button7.addActionListener(
                new ActionListener()
                {
                @Override
                   public void actionPerformed(ActionEvent event)
                   {
                	ChosenLevel = 7;
    				label.setText("Chosen level: " + ChosenLevel);
                   }
                }
                );
        JButton button8 = new JButton("level 8");
        panel.add(button8);
        button8.addActionListener(
                new ActionListener()
                {
                @Override
                   public void actionPerformed(ActionEvent event)
                   {
                	ChosenLevel = 8;
    				label.setText("Chosen level: " + ChosenLevel);
                   }
                }
                );
        JButton button9 = new JButton("level 9");
        panel.add(button9);
        button9.addActionListener(
                new ActionListener()
                {
                @Override
                   public void actionPerformed(ActionEvent event)
                   {
                	ChosenLevel = 9;
    				label.setText("Chosen level: " + ChosenLevel);
                   }
                }
                );
        JButton button10 = new JButton("level 10");
        panel.add(button10);
        button10.addActionListener(
                new ActionListener()
                {
                @Override
                   public void actionPerformed(ActionEvent event)
                   {
                	ChosenLevel = 10;
    				label.setText("Chosen level: " + ChosenLevel);
                   }
                }
                );
        JButton button11 = new JButton("level 11");
        panel.add(button11);
        button11.addActionListener(
                new ActionListener()
                {
                @Override
                   public void actionPerformed(ActionEvent event)
                   {
                	ChosenLevel = 11;
    				label.setText("Chosen level: " + ChosenLevel);
                   }
                }
                );
        JButton button12 = new JButton("level 12");
        panel.add(button12);
        button12.addActionListener(
                new ActionListener()
                {
                @Override
                   public void actionPerformed(ActionEvent event)
                   {
                	ChosenLevel = 12;
    				label.setText("Chosen level: " + ChosenLevel);
                   }
                }
                );
        JButton button13 = new JButton("level 13");
        panel.add(button12);
        button13.addActionListener(
                new ActionListener()
                {
                @Override
                   public void actionPerformed(ActionEvent event)
                   {
                	ChosenLevel = 13;
    				label.setText("Chosen level: " + ChosenLevel);
                   }
                }
                );
        JButton button14 = new JButton("level 14");
        panel.add(button14);
        button14.addActionListener(
                new ActionListener()
                {
                @Override
                   public void actionPerformed(ActionEvent event)
                   {
                	ChosenLevel = 14;
    				label.setText("Chosen level: " + ChosenLevel);
                   }
                }
                );
        JButton button15 = new JButton("level 15");
        panel.add(button15);
        button15.addActionListener(
                new ActionListener()
                {
                @Override
                   public void actionPerformed(ActionEvent event)
                   {
                	ChosenLevel = 15;
    				label.setText("Chosen level: " + ChosenLevel);
                   }
                }
                );
        JButton button16 = new JButton("level 16");
        panel.add(button16);
        button16.addActionListener(
                new ActionListener()
                {
                @Override
                   public void actionPerformed(ActionEvent event)
                   {
                	ChosenLevel = 16;
    				label.setText("Chosen level: " + ChosenLevel);
                   }
                }
                );
        JButton button17 = new JButton("level 17");
        panel.add(button17);
        button17.addActionListener(
                new ActionListener()
                {
                @Override
                   public void actionPerformed(ActionEvent event)
                   {
                	ChosenLevel = 17;
    				label.setText("Chosen level: " + ChosenLevel);
                   }
                }
                );
        JButton button18 = new JButton("level 18");
        panel.add(button18);
        button18.addActionListener(
                new ActionListener()
                {
                @Override
                   public void actionPerformed(ActionEvent event)
                   {
                	ChosenLevel = 18;
    				label.setText("Chosen level: " + ChosenLevel);
                   }
                }
                );
        JButton button19 = new JButton("level 19");
        panel.add(button19);
        button19.addActionListener(
                new ActionListener()
                {
                @Override
                   public void actionPerformed(ActionEvent event)
                   {
                	ChosenLevel = 19;
    				label.setText("Chosen level: " + ChosenLevel);
                   }
                }
                );
        JButton button20 = new JButton("level 20");
        panel.add(button20);
        button20.addActionListener(
                new ActionListener()
                {
                @Override
                   public void actionPerformed(ActionEvent event)
                   {
                	ChosenLevel = 20;
    				label.setText("Chosen level: " + ChosenLevel);
                   }
                }
                );
        JButton button21 = new JButton("level 21");
        panel.add(button21);
        button21.addActionListener(
                new ActionListener()
                {
                @Override
                   public void actionPerformed(ActionEvent event)
                   {
                	ChosenLevel = 21;
    				label.setText("Chosen level: " + ChosenLevel);
                   }
                }
                );
        JButton button22 = new JButton("level 22");
        panel.add(button22);
        button22.addActionListener(
                new ActionListener()
                {
                @Override
                   public void actionPerformed(ActionEvent event)
                   {
                	ChosenLevel = 22;
    				label.setText("Chosen level: " + ChosenLevel);
                   }
                }
                );
////////////////////////////////////////////////////////////////////////////////buttons
		frame.add(panel, BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Choose level");
		frame.pack();
		frame.setVisible(true);
	}
	*/
/*
	public static void main(String[] args) {
		new LevelChooser();
	}
*/
}
