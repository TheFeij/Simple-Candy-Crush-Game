import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * this class represents main page of the game
 *
 * @author The Feij
 * @since 2022.1.20
 */
public class GameFrame extends JFrame {

    private GameEngine engine;
    private Candy[][] candies;
    private JButton hintButton, menuButton;
    private int i1, i2, j1, j2;
    private JPanel candiesPanel;
    private ActionHandler handler;
    private JLabel score;



    public GameFrame(GameEngine engine, String mode){
        super();
        setSize(new Dimension(630, 700));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        i1 = i2 = j1 = j2 = -1;

        this.engine = engine;
        candies = new Candy[10][10];

        if(mode.equals("Random"))
            this.engine.startMap(candies);
        else if(mode.equals("From File")){
            while (true){
                String address = JOptionPane.showInputDialog(null, "Enter File's Absolute Adress", JOptionPane.PLAIN_MESSAGE);
                if(Files.exists(Paths.get(address)) && Paths.get(address).isAbsolute()){
                    engine.fillMapFromFile(candies, address);
                    break;
                }
                else {
                    JOptionPane.showMessageDialog(null, "address not valid! try again");
                }
            }
        }


        setLayout(new BorderLayout());
        handler = new ActionHandler();

        candiesPanel = new JPanel(new GridLayout(10, 10));
        for(int i = 0 ; i < 10 ; i++){
            for(int j = 0 ; j < 10 ; j++){
                candies[i][j].addActionListener(handler);
                candiesPanel.add(candies[i][j]);
            }
        }

        score = new JLabel("Score: " + engine.getScore());
        score.setBackground(Color.ORANGE);
        score.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        score.setOpaque(true);
        score.setHorizontalAlignment(JButton.CENTER);
        score.setFont(new Font("serif", Font.BOLD, 30));

        hintButton = new JButton("Hint!");
        hintButton.setFont(new Font("serif", Font.BOLD, 20));
        hintButton.addActionListener(handler);

        menuButton = new JButton("Menu");
        menuButton.setFont(new Font("serif", Font.BOLD, 20));
        menuButton.addActionListener(handler);

        JPanel panel = new JPanel(new GridLayout(1, 2));
        panel.add(hintButton);
        panel.add(menuButton);


        add(score, BorderLayout.NORTH);
        add(candiesPanel, BorderLayout.CENTER);
        add(panel, BorderLayout.SOUTH);


        setVisible(true);
    }


    /**
     * A private class used for handling action events
     */
    private class ActionHandler implements ActionListener{


        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            if(actionEvent.getSource().equals(hintButton)){
                if(!engine.findHint(candies)){
                    setVisible(false);
                    JOptionPane.showMessageDialog(null, "Game Over!");
                    engine.saveResults();
                    Menu menu = new Menu();
                }
            }
            else if(actionEvent.getSource().equals(menuButton)){
                setVisible(false);
                engine.saveResults();
                Menu menu = new Menu();
            }
            else if(actionEvent.getSource() instanceof Candy){
                for(int i = 0 ; i < 10 ; i++){
                    for(int j = 0 ; j < 10 ; j++){
                        if(actionEvent.getSource().equals(candies[i][j])){
                            if(i1 == -1){
                                i1 = i;
                                j1 = j;
                                return;
                            }
                            else {
                                i2 = i;
                                j2 = j;
                            }

                        }
                    }
                }
                if(!engine.isMovePossible(i1, j1, i2, j2, candies)){
                    i1 = j1 = i2 = j2 = -1;
                    System.out.println(" failed");
                    return;
                }
                Candy temp = candies[i1][j1];
                candies[i1][j1] = candies[i2][j2];
                candies[i2][j2] = temp;
                i1 = j1 = i2 = j2 = -1;


                updateMap();
                engine.remove(candies);
                if(engine.getScore() >= 1500){
                    setVisible(false);
                    JOptionPane.showMessageDialog(null, "You Won!");
                    dispose();
                    return;
                }
                engine.adjustMap(candies);
                engine.refillMap(candies);
                updateMap();




            }
        }
    }


    /**
     * this method is used to update map
     */
    public void updateMap(){
        candiesPanel = new JPanel(new GridLayout(10, 10));
        for(int i = 0 ; i < 10 ; i++){
            for(int j = 0 ; j < 10 ; j++){
                candiesPanel.add(candies[i][j]);
                if(candies[i][j].getActionListeners().length == 0){
                    candies[i][j].addActionListener(handler);
                }
                candies[i][j].setBorder(null);
                candies[i][j].setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
            }
        }
        add(candiesPanel, BorderLayout.CENTER);
        score.setText("Score: " + engine.getScore());
        invalidate();
//        validate();
    }

    /**
     * This method is performed when frame is closed,
     * it saves the result and moves to menu
     */
    public void dispose(){
        setVisible(false);
        engine.saveResults();
        Menu menu = new Menu();
    }
}
