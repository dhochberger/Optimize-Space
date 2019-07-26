import java.awt.Dimension;
import java.awt.geom.QuadCurve2D;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import java.awt.geom.Path2D;

import java.awt.BasicStroke;
import java.awt.RenderingHints;

import java.awt.geom.AffineTransform;

import java.util.Vector;


public class DrawingZone extends JPanel {

  private static DrawingZone instance;
  private JPanel drawingZone;
  private SVGDocument svg;
  private Vector<SVGPathCollection> collections;
  private Core core;

  private double currentX = 0, currentY = 0;

  /**
   * Constructeur de l'emplacement où le SVG s'affiche
   */
  private DrawingZone() {
    super();
    core = Core.getInstance();
    setBackground(Color.white);
    resizeZone(1000, 1000);
  }

  /**
   * Récupérer l'instance active
   * @return Drawingzone : emplacement où le SVG s'affiche
   */
  public static DrawingZone getInstance() {
    if (instance == null) {
      synchronized (Core.class) {
        if (instance == null) {
          instance = new DrawingZone();
        }
      }
    }
    return instance;
  }

  public void paint(Graphics g) {
    super.paint(g);
    Graphics2D gg = (Graphics2D) g;

    // on améliore les traits (on les rends plus arrondis)
    gg.setStroke(
      new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND)
    );

    // on active l'antialiasing
    gg.setRenderingHint(
      RenderingHints.KEY_ANTIALIASING,
      RenderingHints.VALUE_ANTIALIAS_ON
    );

    // AffineTransform at = new AffineTransform();
    // at.scale(.2, .2);

    // double maxX = 0, maxY = 0;

    SVGDocument svg = core.getSVG();
    if (svg != null) {
      collections = svg.getCollections();
      for (SVGPathCollection c : collections) {

        // // calcul du plus grand x et y
        // double maxBX = c.getMaxBoundsX();
        // double maxBY = c.getMaxBoundsY();
        // if (maxX < maxBX) maxX = maxBX;
        // if (maxY < maxBY) maxY = maxBY;

        for (SVGPath p : c.getPaths()) {

          Path2D path = p.getPath();
          // AffineTransform at = path.getTransform();
          // path.transform(at);
          // gg.fill(path);
          gg.draw(path);
        }
      }
      // if (currentX != maxX || currentY != maxY) {
      //   currentX = maxX;
      //   currentY = maxY;
      //   resizeZone(maxX / core.getZoom(), maxY / core.getZoom());
      // }
    }

  }

  public void resizeZone(int x, int y) {
    setPreferredSize(new Dimension(x, y));
    // repaint();
  }

  // public void resizeZone(double x, double y) {
  //   System.out.println(" == " + x + " == " + y);
  //   setPreferredSize(new Dimension((int) x + 1, (int) y + 1));
  //   repaint();
  // }
}
