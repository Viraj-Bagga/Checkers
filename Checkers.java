import javax.swing.*;
import java.awt.*;

public class Checkers extends JPanel {

    public static void main(String[] args) {
        JFrame window = new JFrame("Checkers");
        Checkers content = new Checkers();
        window.setContentPane(content);
        window.pack();
        window.setSize(600, 600);
        window.setLocationRelativeTo(null);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setVisible(true);
    }

    private JButton newGameButton;
    private JButton resignButton;
    private JLabel message;

    public Checkers() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(600, 600));

        // Create the board
        Board board = new Board();
        add(board, BorderLayout.CENTER);

        // Create a panel for buttons and message
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());

        newGameButton = new JButton("New Game");
        resignButton = new JButton("Resign");
        message = new JLabel("", JLabel.CENTER);
        message.setFont(new Font("Serif", Font.BOLD, 14));
        message.setForeground(Color.green);

        controlPanel.add(newGameButton);
        controlPanel.add(resignButton);
        controlPanel.add(message);

        add(controlPanel, BorderLayout.SOUTH);
    }
}