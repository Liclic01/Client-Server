package Frame;
import java.awt.*;
import java.awt.geom.Path2D;
import java.util.LinkedList;
import javax.swing.JComponent;

public class LinesComponent extends JComponent{

    private static class Line{
        final double x1;
        final double y1;
        final double x2;
        final double y2;
        final Color color;

        public Line(double x1, double y1, double x2, double y2, Color color) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
            this.color = color;
        }
    }

    private final LinkedList<Line> lines = new LinkedList<>();


    public void addLine(double x1, double x2, double x3, double x4, Color color) {
        lines.add(new Line(x1,x2,x3,x4, color));
        repaint();
    }

    Path2D createSmoothLink(Double p1, Double p2,Double p3, Double p4) {
        final double cx1 = (p1 + p3) * 0.5;
        final double cy1 = (p2 + p4) * 0.5;
        final double cx2 = (p1 + p3) * 0.5;
        final double cy2 = (p2 + p4) * 0.5;

        final Path2D link = new Path2D.Double();
        link.moveTo(p1, p2);
        link.curveTo(cx1, cy1, cx2, cy2, p3, p4);
        return link;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        for (Line line : lines) {
            g2d.setColor(line.color);
            Path2D link=createSmoothLink(line.x1,line.y1,line.x2,line.y2);
            g2d.draw(link);
        }
    }

    public static void main(String[] args) {

    }

}