package Frame;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.util.*;
import javax.swing.JComponent;

public class LinesComponent extends JComponent {
    private BufferedImage buffer;

    public LinesComponent() {
        addComponentListener(new ComponentListenerImpl());
    }

    private static class Line {
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

    private final java.util.List<Line> lines = Collections.synchronizedList(new LinkedList<Line>());


    public void addLine(double x1, double x2, double x3, double x4, Color color) {
        lines.add(new Line(x1, x2, x3, x4, color));
        repaint();
    }

    Path2D createSmoothLink(Double p1, Double p2, Double p3, Double p4) {
        final double cx1 = (p1 + p3) * 0.5;
        final double cy1 = (p2 + p4) * 0.5;
        final double cx2 = (p1 + p3) * 0.5;
        final double cy2 = (p2 + p4) * 0.5;

        final Path2D link = new Path2D.Double();
        link.moveTo(p1, p2);
        link.curveTo(cx1, cy1, cx2, cy2, p3, p4);
        return link;
    }

    private void rebuildBuffer() {
        int w = getWidth();
        int h = getHeight();
        buffer = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = buffer.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        synchronized (lines) {
            for (Line line : lines) {
                g2d.setColor(line.color);
                Path2D link = createSmoothLink(line.x1 * w, line.y1 * h, line.x2 * w, line.y2 * h);
                g2d.draw(link);
            }
        }

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
            rebuildBuffer();
        g.drawImage(buffer, 0, 0, this);

    }


    private class ComponentListenerImpl extends ComponentAdapter {

        private Dimension lastSize = getSize();

        @Override
        public void componentShown(ComponentEvent e) {
            if (!getSize().equals(lastSize)) {
                rebuildBuffer();
                lastSize = getSize();
            }
        }

        @Override
        public void componentResized(ComponentEvent e) {
            if (!getSize().equals(lastSize)) {
                rebuildBuffer();
                lastSize = getSize();
            }
        }
    }
}
