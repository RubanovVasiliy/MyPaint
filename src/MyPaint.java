import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.*;
import javax.swing.*;


public class MyPaint extends JFrame {
    private static final long serialVersionUID = 1L;
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final String NAME = "MyPaint";
    private String tool = ToolType.Point;

    public MyPaint() {
        super(NAME);
        setDefaultLookAndFeelDecorated(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        var menuBar = new JMenuBar();
        menuBar.add(new JMenuItem(new ToolsAction(ToolType.Point)));
        menuBar.add(new JMenuItem(new ToolsAction(ToolType.Line)));
        menuBar.add(new JMenuItem(new ToolsAction(ToolType.Polygon)));
        menuBar.add(new JMenuItem(new ClearAction(SystemActionType.Clear, this)));
        menuBar.add(Box.createHorizontalGlue());
        add(new Board());
        setJMenuBar(menuBar);
        setSize(WIDTH, HEIGHT);
        setVisible(true);
    }

    class ToolsAction extends AbstractAction {

        ToolsAction(String tool) {
            putValue(NAME, tool);
        }

        public void actionPerformed(ActionEvent e) {
            if (Objects.equals(e.getActionCommand(), ToolType.Point)) {
                tool = ToolType.Point;
            }
            if (Objects.equals(e.getActionCommand(), ToolType.Line)) {
                tool = ToolType.Line;
            }
            if (Objects.equals(e.getActionCommand(), ToolType.Polygon)) {
                tool = ToolType.Polygon;
            }
        }
    }

    static class ClearAction extends AbstractAction {
        private final MyPaint paint;

        ClearAction(String action, MyPaint myPaint) {
            putValue(NAME, action);
            paint = myPaint;
        }

        public void actionPerformed(ActionEvent e) {
            if (Objects.equals(e.getActionCommand(), SystemActionType.Clear)) {
                paint.repaint();
            }
        }
    }

    public class Board extends JPanel implements MouseListener, MouseMotionListener {
        private final Deque<Integer> xPoints = new LinkedList<>();
        private final Deque<Integer> yPoints = new LinkedList<>();
        private Graphics g;

        public Board() {
            setBackground(Color.WHITE);
            setPreferredSize(new Dimension(WIDTH, HEIGHT));
            addMouseListener(this);
            addMouseMotionListener(this);
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
        }

        private void setUpDrawingGraphics() {
            g = getGraphics();
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            setUpDrawingGraphics();
            int x = e.getX();
            int y = e.getY();

            if (ToolType.Point.equals(tool)) {
                g.drawLine(x, y, x, y);
            } else {
                if (!xPoints.isEmpty() && !yPoints.isEmpty()) {
                    g.drawLine(xPoints.peekLast(), yPoints.peekLast(), x, y);
                }
                xPoints.add(x);
                yPoints.add(y);

                if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
                    if (Objects.equals(tool, ToolType.Polygon)) {
                        g.fillPolygon(xPoints.stream().mapToInt(o -> o).toArray(),
                                yPoints.stream().mapToInt(o -> o).toArray(), xPoints.size());
                    }
                    xPoints.clear();
                    yPoints.clear();
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseDragged(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }

        @Override
        public void mouseMoved(MouseEvent e) {
        }
    }

    public static final class SystemActionType {
        public static final String Clear = "Clear";
    }

    public static final class ToolType {
        public static final String Point = "Point";
        public static final String Line = "Line";
        public static final String Polygon = "Polygon";
    }

    public static void main(String[] args) {
        new MyPaint();
    }
}