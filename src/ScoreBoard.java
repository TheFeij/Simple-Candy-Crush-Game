import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;


/**
 * JFrame which represents a scoreboard used to show top 5 score of the player
 *
 * @author The Feij
 * @since 2022.1.20
 */
public class ScoreBoard extends JFrame {


    private JTable scoreboard;
    private DefaultTableModel tableModel;
    private JScrollPane scrollPane;


    public ScoreBoard(){
        super("Top Scores");
        this.setSize(new Dimension(300, 200));
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        tableModel = new DefaultTableModel();
        tableModel.addColumn("Record");
        tableModel.addColumn("Score");

        scoreboard = new JTable(tableModel);
        scoreboard.setBackground(Color.cyan);
        scoreboard.setOpaque(true);

        scrollPane = new JScrollPane(scoreboard);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));
        scrollPane.createVerticalScrollBar();


        Scanner scanner = null;
        try {
            scanner = new Scanner(new FileReader("ScoreBoard.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        int counter = 1;
        while (scanner.hasNext()){
            String[] information = new String[2];
            information[0] = String.valueOf(counter);
            information[1] = scanner.next();
            tableModel.insertRow(tableModel.getRowCount(), information);
            counter++;
        }

        add(scrollPane);
        setVisible(true);
    }


    public void dispose(){
        setVisible(false);
        Menu menu = new Menu();
    }
}
