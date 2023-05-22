import javax.swing.*;

/**
 * this class represents a candy
 *
 * @author The Feij
 * @since 2022.1.20
 */
public class Candy extends JButton {

    //color of the candy
    private String color;
    //type of the candy
    private String type;
    //points given to player if this candy is removed
    private int points;
    //is candy removed or not
    private boolean isDestroyed;


    /**
     * A constructor to create a candy
     */
    public Candy(){
        super();

        this.isDestroyed = false;
    }


    //setters
    public void setColor(String color) {
        this.color = color;
    }
    public void setType(String type) {
        this.type = type;
    }
    public void setDestroyed(boolean destroyed) {
        isDestroyed = destroyed;
    }


    //getters
    public int getPoints() {
        setPoints();
        return points;
    }
    public String getType() {
        return type;
    }
    public String getColor() {
        return color;
    }
    public boolean isDestroyed() {
        return isDestroyed;
    }


    /**
     * A method to calculate points of a candy based on
     * type of the candy
     */
    public void setPoints() {
        switch (type) {
            case "SC" -> points = 5;
            case "LR", "LC" -> points = 10;
            case "RC" -> points = 15;
        }
    }

    /**
     * A method to set a suitable icon for the candy based on
     * its type and color
     */
    public void setImage(){
        switch (type) {
            case "SC" -> {
                points = 5;
                setIcon(new ImageIcon("./Icons/" + color + ".png"));
            }
            case "LR" -> {
                points = 10;
                setIcon(new ImageIcon("./Icons/" + color + " - LR" + ".png"));
            }
            case "LC" -> {
                points = 10;
                setIcon(new ImageIcon("./Icons/" + color + " - LC" + ".png"));
            }
            case "RC" -> {
                points = 15;
                setIcon(new ImageIcon("./Icons/" + color + " - RC" + ".png"));
            }
        }
    }

    /**
     * A method to destroy a candy
     */
    public void setDestroyed() {
        isDestroyed = true;
        setIcon(null);
        setEnabled(false);
    }

}
