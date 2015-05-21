package electricdrill.interactiveproject;

import electricdrill.interactiveproject.gui.Console;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;

public class InteractiveProject extends JFrame {
    public static Console console = new Console();
    public static JTextField inputField = new JTextField();

    public static Font font = new Font("Avenir-Light", Font.PLAIN, 12);

    private void addComponentsToPane() {
        JPanel panel = new JPanel();
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(panel);
        setLayout(new BorderLayout());

        Border border = BorderFactory.createLineBorder(Color.BLACK);

        console.setEditable(false);

        JScrollPane consoleScrollPane = new JScrollPane(console);

        console.setBackground(new Color(230, 230, 250));
        console.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        console.setFont(font);

        DefaultCaret caret = (DefaultCaret) console.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        inputField.setFont(font);

        inputField.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "enterKeyPressed");
        inputField.getActionMap().put("enterKeyPressed", new AbstractAction() {
            @SuppressWarnings("static-access")
            @Override
            public void actionPerformed(ActionEvent e) {
                console.printToConsole("[User] ");

                inputField.setText("");
            }
        });

        getContentPane().add(inputField, BorderLayout.SOUTH);
        getContentPane().add(consoleScrollPane);
    }

    private static void createAndShowGUI() {
        InteractiveProject frame = new InteractiveProject("Quiz");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        frame.addComponentsToPane();

        frame.setSize(Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height);
        frame.setVisible(true);

        inputField.requestFocusInWindow();
    }

    public InteractiveProject(String name) {
        super(name);
    }

    public static void main(String[] args) throws InterruptedException {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createAndShowGUI();
            }
        });
    }

}
