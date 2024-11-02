import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PlotPanel extends JPanel {
    private final List<Point> linePoints = new ArrayList<>();
    private final List<Point> circlePoints = new ArrayList<>();
    private final int gridSize = 20;
    private JLabel timeLabel;

    public PlotPanel(JLabel timeLabel) {
        setBackground(Color.WHITE);
        this.timeLabel = timeLabel;
    }

    public void drawSequentialLine(int x1, int x2, int y1, int y2) {
        linePoints.clear();
        long startTime = System.nanoTime();
        double slope = (double) (y2 - y1) / (x2 - x1);
        double intercept = y1 - slope * x1;
        for (int x = x1; x <= x2; x++) {
            int y = (int) (slope * x + intercept);
            linePoints.add(new Point(x, y));
        }
        long endTime = System.nanoTime();
        timeLabel.setText("Sequential Line Time: " + (endTime - startTime) + " ns");
        repaint();
    }

    public void drawBresenhamLine(int x1, int x2, int y1, int y2) {
        linePoints.clear();
        long startTime = System.nanoTime();
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
        int sx = x1 < x2 ? 1 : -1;
        int sy = y1 < y2 ? 1 : -1;
        int err = dx - dy;

        while (true) {
            linePoints.add(new Point(x1, y1));
            if (x1 == x2 && y1 == y2) break;
            if ((err - 0.5*dy)>0) {
                err -= dy;
                x1 += sx;
            }
            if (err-0.5*dx < 0) {
                err += dx;
                y1 += sy;
            }
        }
        long endTime = System.nanoTime();
        timeLabel.setText("Bresenham Line Time: " + (endTime - startTime) + " ns");
        repaint();
    }

    public void drawDDALine(int x1, int x2, int y1, int y2) {
        linePoints.clear();
        long startTime = System.nanoTime();
        int dx = x2 - x1;
        int dy = y2 - y1;
        int steps = Math.max(Math.abs(dx), Math.abs(dy));
        float xInc = dx / (float) steps;
        float yInc = dy / (float) steps;
        float x = x1, y = y1;

        for (int i = 0; i <= steps; i++) {
            linePoints.add(new Point((int) Math.floor(x), (int) Math.floor(y)));
            x += xInc;
            y += yInc;
        }
        long endTime = System.nanoTime();
        timeLabel.setText("DDA Line Time: " + (endTime - startTime) + " ns");
        repaint();
    }

    public void drawBresenhamCircle(int xc, int yc, int radius) {
        circlePoints.clear();
        long startTime = System.nanoTime();
        int x = 0, y = radius;
        int d = 3 - 2 * radius;
        drawCirclePoints(xc, yc, x, y);
        while (y >= x) {
            x++;
            if (d > 0) {
                y--;
                d = d + 4 * (x - y) + 10;
            } else {
                d = d + 4 * x + 6;
            }
            drawCirclePoints(xc, yc, x, y);
        }
        long endTime = System.nanoTime();
        timeLabel.setText("Bresenham Circle Time: " + (endTime - startTime) + " ns");
        repaint();
    }

    private void drawCirclePoints(int xc, int yc, int x, int y) {
        circlePoints.add(new Point(xc + x, yc + y));
        circlePoints.add(new Point(xc - x, yc + y));
        circlePoints.add(new Point(xc + x, yc - y));
        circlePoints.add(new Point(xc - x, yc - y));
        circlePoints.add(new Point(xc + y, yc + x));
        circlePoints.add(new Point(xc - y, yc + x));
        circlePoints.add(new Point(xc + y, yc - x));
        circlePoints.add(new Point(xc - y, yc - x));
    }

    public void clearLine() {
        linePoints.clear();
        repaint();
    }

    public void clearCircle() {
        circlePoints.clear();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        drawGridAndAxes(g2d);

        g2d.setColor(Color.BLUE);
        drawConnectedPoints(g2d, linePoints);

        g2d.setColor(Color.RED);
        drawCirclePoints(g2d, circlePoints);
    }

    private void drawGridAndAxes(Graphics2D g) {
        int width = getWidth();
        int height = getHeight();

        int centerX = (width / 2 / gridSize) * gridSize;
        int centerY = (height / 2 / gridSize) * gridSize;

        g.setColor(Color.LIGHT_GRAY);
        for (int i = 0; i < width; i += gridSize) {
            g.drawLine(i, 0, i, height);
        }
        for (int i = 0; i < height; i += gridSize) {
            g.drawLine(0, i, width, i);
        }

        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(2));
        g.drawLine(centerX, 0, centerX, height);
        g.drawLine(0, centerY, width, centerY);

        for (int i = centerX; i < width; i += gridSize) {
            g.drawString(String.valueOf((i - centerX) / gridSize), i, centerY + 12);
        }
        for (int i = centerX; i > 0; i -= gridSize) {
            g.drawString(String.valueOf((i - centerX) / gridSize), i, centerY + 12);
        }
        for (int i = centerY; i < height; i += gridSize) {
            g.drawString(String.valueOf(-(i - centerY) / gridSize), centerX + 5, i);
        }
        for (int i = centerY; i > 0; i -= gridSize) {
            g.drawString(String.valueOf(-(i - centerY) / gridSize), centerX + 5, i);
        }
    }

    private void drawConnectedPoints(Graphics g, List<Point> points) {
        for (int i = 1; i < points.size(); i++) {
            Point p1 = points.get(i - 1);
            Point p2 = points.get(i);
            int centerX = (getWidth() / 2 / gridSize) * gridSize;
            int centerY = (getHeight() / 2 / gridSize) * gridSize;

            int x1 = p1.x * gridSize + centerX;
            int y1 = centerY - p1.y * gridSize;
            int x2 = p2.x * gridSize + centerX;
            int y2 = centerY - p2.y * gridSize;

            g.drawLine(x1, y1, x2, y2);
        }
    }

    private void drawCirclePoints(Graphics g, List<Point> points) {
        for (Point p : points) {
            int centerX = (getWidth() / 2 / gridSize) * gridSize;
            int centerY = (getHeight() / 2 / gridSize) * gridSize;

            int x = p.x * gridSize + centerX;
            int y = centerY - p.y * gridSize;

            g.fillRect(x - 2, y - 2, 4, 4);
        }
    }
}