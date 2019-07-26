import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.WindowConstants;
import java.text.DecimalFormat;

import java.lang.Double;


public class Interface extends JFrame {

  private RunButton actionBtn;
  private DrawingPane drawingPane;
  private ChooseFileButton fileChooserBtn;
  private JScrollPane logPane;
  private JTextPane logText;
  private JLabel uriFileLabel;
  private JTextField uriFileText;
  private JLabel zoomLabel;
  private JSlider zoom;
//  private SliderListener zoomListener;
  private JLabel heightLabel;
  private JTextField height;
  private JLabel timeoutLabel;
  private JTextField timeout;

  private final String INTERFACE_TITLE = "Optimisation de découpe de formes";
  private final int INTERFACE_WIDTH = 650;
  private final int INTERFACE_HEIGHT = 450;
  private final int DEFAULT_TIMEOUT = 5;
  private final int DEFAULT_HEIGHT = 2700;
  private final String URI_FILE_LABEL = "URI du fichier SVG :";
  private final String ZOOM_LABEL = "Niveau de zoom";
  private final String HEIGHT_LABEL = "Hauteur du tissu";
  private final String TIMEOUT_LABEL = "Temps avant arrêt";

  /**
   * Constructeur de l'interface utilisateur dans son ensemble
   */
  public Interface() {
    initComponents();
  }

 /**
   * Choix du niveau de zoom par l'utilisateur
   * @param zoom dont la valeur par défaut est à 1
   */


  public void changeZoom(double zoom) {
    if (zoomLabel != null) {
      DecimalFormat df = new DecimalFormat("#.####");
      zoomLabel.setText(ZOOM_LABEL + " (" + df.format(zoom) + "x)");
    }
  }

  /**
   * Initialisation de l'ensemble des composants de la fenêtre
   */
  private void initComponents() {
    fileChooserBtn = new ChooseFileButton();
    uriFileLabel = new JLabel();
    uriFileText = new JTextField();
    actionBtn = new RunButton();
    drawingPane = new DrawingPane();
    logPane = new JScrollPane();
    logText = new JTextPane();
    zoomLabel = new JLabel();
    zoom = new JSlider(JSlider.HORIZONTAL, -10000, 10000, 0);
    //zoomListener = new SliderListener();
    heightLabel = new JLabel();
    height = new JTextField();
    timeoutLabel = new JLabel();
    timeout = new JTextField();

    setTitle(INTERFACE_TITLE);
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setMinimumSize(new Dimension(INTERFACE_WIDTH, INTERFACE_HEIGHT));
    setPreferredSize(new Dimension(INTERFACE_WIDTH, INTERFACE_HEIGHT));

    uriFileLabel.setText(URI_FILE_LABEL);
    uriFileText.setFont(new Font("Cantarell", 0, 12));
    zoomLabel.setText(ZOOM_LABEL);
    heightLabel.setText(HEIGHT_LABEL);
    timeoutLabel.setText(TIMEOUT_LABEL);
    height.setText("" + DEFAULT_HEIGHT);
    timeout.setText("" + DEFAULT_TIMEOUT);

    //zoom.addChangeListener(zoomListener);

    logText.setEditable(false);
    logPane.setViewportView(logText);

    GroupLayout layout = new GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(Alignment.TRAILING, false)
          .addComponent(actionBtn,
            GroupLayout.DEFAULT_SIZE,
            GroupLayout.DEFAULT_SIZE,
            Short.MAX_VALUE)
          .addComponent(uriFileText,
            GroupLayout.DEFAULT_SIZE,
            200,
            200)
          .addComponent(fileChooserBtn,
            Alignment.LEADING,
            GroupLayout.DEFAULT_SIZE,
            200,
            Short.MAX_VALUE)
          .addComponent(uriFileLabel,
            Alignment.LEADING,
            GroupLayout.DEFAULT_SIZE,
            GroupLayout.DEFAULT_SIZE,
            Short.MAX_VALUE)
          .addComponent(timeoutLabel,
            GroupLayout.DEFAULT_SIZE,
            200,
            200)
          .addComponent(timeout))
        .addPreferredGap(ComponentPlacement.RELATED)
        .addComponent(drawingPane,
          GroupLayout.DEFAULT_SIZE,
          426,
          Short.MAX_VALUE))
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(Alignment.LEADING)
      .addGroup(Alignment.TRAILING, layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(fileChooserBtn,
          GroupLayout.PREFERRED_SIZE,
          50,
          GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(ComponentPlacement.UNRELATED)
        .addComponent(uriFileLabel)
        .addPreferredGap(ComponentPlacement.RELATED)
        .addComponent(uriFileText,
          GroupLayout.PREFERRED_SIZE,
          GroupLayout.DEFAULT_SIZE,
          GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(ComponentPlacement.RELATED)
        .addPreferredGap(ComponentPlacement.RELATED)
        .addComponent(timeoutLabel)
        .addComponent(timeout,
          GroupLayout.PREFERRED_SIZE,
          GroupLayout.DEFAULT_SIZE,
          GroupLayout.PREFERRED_SIZE)

        .addComponent(actionBtn,
          GroupLayout.PREFERRED_SIZE,
          60,
          GroupLayout.PREFERRED_SIZE)
        .addContainerGap())
      .addComponent(drawingPane)
    );

    pack();
  }

  /**
   * Définit l'URL correspondant au fichier actuellement traité
   * @param url à traiter
   */
  public void setCurrentURI(String uri) {
    uriFileText.setText(uri);
  }

  /**
   * Récupérer l'url du SVG actuellement traité
   * @return url sous forme de chaine de caractères
   */
  public String getCurrentURI() {
    return uriFileText.getText();
  }

  /**
   * Récupérer la valeur souhaitée du timeout
   * @return timeout sous forme de chaîne de caractères
   */
   public String getCurrentTimeout() {
     return timeout.getText();
   }

   /**
    * Récupérer la hauteur de tissu souhaitée
    * @return hauteur (double)
    */
    public double getCurrentHeight() {
      String heightS = height.getText();
      double result;

      if(heightS.isEmpty()) {
        result = 0;
      } else {
        result = Double.parseDouble(heightS);
      }

      return result;
    }

  /**
   * Empêche l'utilisateur d'interagir avec les éléments de la fenêtre
   */
  public void disableComponents() {
    fileChooserBtn.setEnabled(false);
    uriFileText.setEnabled(false);
    zoom.setEnabled(false);
    timeout.setEnabled(false);
    height.setEnabled(false);
    actionBtn.setText(">> Recommencer <<");
  }

  /**
   * Permet à l'utilisateur d'interagir avec les éléments de la fenêtre
   */
  public void enableComponents() {
    fileChooserBtn.setEnabled(true);
    uriFileText.setEnabled(true);
    zoom.setEnabled(true);
    timeout.setEnabled(true);
    height.setEnabled(true);
    actionBtn.setText(">> GO <<");
  }

  /**
   * Permet d'écrire des logs dans la zone prévue à cet effet
   */
  public void log(String str) {
    logText.setText(
      logText.getText() + "\n" + str
    );
  }

}
