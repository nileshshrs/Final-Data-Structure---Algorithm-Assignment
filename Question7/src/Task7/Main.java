package Task7;

import Task7.View.AllUserFrame;

import javax.swing.*;

// This is the main class that runs the program
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AllUserFrame frame = new AllUserFrame(); // this is the main frame that contains all the users
            frame.setVisible(true);
        });
    }
}
