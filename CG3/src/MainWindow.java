import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    private final PlotPanel plotPanel;

    public MainWindow() {
        setTitle("Лаба 4 - Алгоритмы растеризации");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel timeLabel = new JLabel("Время выполнения: ");
        plotPanel = new PlotPanel(timeLabel);

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(9, 2, 5, 5));

        JTextField x1Field = new JTextField();
        JTextField y1Field = new JTextField();
        JTextField x2Field = new JTextField();
        JTextField y2Field = new JTextField();
        JTextField radiusField = new JTextField();

        JButton seqLineButton = new JButton("Sequential Line");
        seqLineButton.addActionListener(e -> plotPanel.drawSequentialLine(
                Integer.parseInt(x1Field.getText()),
                Integer.parseInt(x2Field.getText()),
                Integer.parseInt(y1Field.getText()),
                Integer.parseInt(y2Field.getText())
        ));

        JButton bresenhamLineButton = new JButton("Bresenham Line");
        bresenhamLineButton.addActionListener(e -> plotPanel.drawBresenhamLine(
                Integer.parseInt(x1Field.getText()),
                Integer.parseInt(x2Field.getText()),
                Integer.parseInt(y1Field.getText()),
                Integer.parseInt(y2Field.getText())
        ));

        JButton ddaLineButton = new JButton("DDA Line");
        ddaLineButton.addActionListener(e -> plotPanel.drawDDALine(
                Integer.parseInt(x1Field.getText()),
                Integer.parseInt(x2Field.getText()),
                Integer.parseInt(y1Field.getText()),
                Integer.parseInt(y2Field.getText())
        ));

        JButton bresenhamCircleButton = new JButton("Bresenham Circle");
        bresenhamCircleButton.addActionListener(e -> plotPanel.drawBresenhamCircle(
                Integer.parseInt(x1Field.getText()),
                Integer.parseInt(y1Field.getText()),
                Integer.parseInt(radiusField.getText())
        ));

        JButton clearLineButton = new JButton("Clear Line");
        clearLineButton.addActionListener(e -> plotPanel.clearLine());

        JButton clearCircleButton = new JButton("Clear Circle");
        clearCircleButton.addActionListener(e -> plotPanel.clearCircle());

        controlPanel.add(new JLabel("X1:"));
        controlPanel.add(x1Field);
        controlPanel.add(new JLabel("Y1:"));
        controlPanel.add(y1Field);
        controlPanel.add(new JLabel("X2:"));
        controlPanel.add(x2Field);
        controlPanel.add(new JLabel("Y2:"));
        controlPanel.add(y2Field);
        controlPanel.add(new JLabel("Radius:"));
        controlPanel.add(radiusField);
        controlPanel.add(seqLineButton);
        controlPanel.add(bresenhamLineButton);
        controlPanel.add(ddaLineButton);
        controlPanel.add(bresenhamCircleButton);
        controlPanel.add(clearLineButton);
        controlPanel.add(clearCircleButton);
        controlPanel.add(timeLabel); // метка времени выполнения

        add(controlPanel, BorderLayout.WEST);
        add(plotPanel, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainWindow window = new MainWindow();
            window.setVisible(true);
        });
    }
}