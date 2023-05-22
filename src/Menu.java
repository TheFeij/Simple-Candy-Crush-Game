import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * JFrame which represents a Game Menu
 *
 * @author The Feij
 * @since 2022.1.20
 */
public class Menu extends JFrame {

    private JButton startRandomButton;
    private JButton startFromFileButton;
    private JButton scoreBoardButton;


    public Menu(){
        super();
        setSize(new Dimension(400, 300));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Simple Candy Crush Game");




        setLayout(new GridLayout(3, 1));
        ActionHandler handler = new ActionHandler();

        startRandomButton = new JButton("Start Random Game");
        startRandomButton.setFont(new Font("serif", Font.BOLD, 20));
        startRandomButton.addActionListener(handler);

        startFromFileButton = new JButton("Start From File");
        startFromFileButton.setFont(new Font("serif", Font.BOLD, 20));
        startFromFileButton.addActionListener(handler);

        scoreBoardButton = new JButton("Score Board!");
        scoreBoardButton.setFont(new Font("serif", Font.BOLD, 20));
        scoreBoardButton.addActionListener(handler);

        add(startRandomButton);
        add(startFromFileButton);
        add(scoreBoardButton);


        setVisible(true);
    }








    private class ActionHandler implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            if(actionEvent.getSource().equals(startRandomButton)){
                setVisible(false);
                GameEngine engine = new GameEngine();
                GameFrame frame = new GameFrame(engine, "Random");
            }
            else if(actionEvent.getSource().equals(startFromFileButton)){
                setVisible(false);
                GameEngine engine = new GameEngine();
                GameFrame frame = new GameFrame(engine, "From File");
            }
            else if(actionEvent.getSource().equals(scoreBoardButton)){
                setVisible(false);
                ScoreBoard scoreBoard = new ScoreBoard();
            }
        }
    }
}
