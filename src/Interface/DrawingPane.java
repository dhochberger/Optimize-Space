import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JScrollPane;
import javax.swing.JViewport;


public class DrawingPane extends JScrollPane {

  private DrawingZone drawingZone;

  /**
   * Constructeur du conteneur de la zone où le SVG est dessiné
   */
  public DrawingPane() {
    super();

    drawingZone = DrawingZone.getInstance();
    setViewportBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));

    GroupLayout drawingZoneLayout = new GroupLayout(drawingZone);
    drawingZone.setLayout(drawingZoneLayout);
    drawingZoneLayout.setHorizontalGroup(
      drawingZoneLayout.createParallelGroup(Alignment.LEADING)
      .addGap(0, 411, Short.MAX_VALUE)
    );
    drawingZoneLayout.setVerticalGroup(
      drawingZoneLayout.createParallelGroup(Alignment.LEADING)
      .addGap(0, 423, Short.MAX_VALUE)
    );

    getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
    setViewportView(drawingZone);
  }
}
