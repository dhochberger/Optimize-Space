import java.awt.EventQueue;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.JOptionPane;

/**
 * Le Core permet de centraliser l'ensemble des ressources
 * Singleton et threadsafe
 */
class Core {

  private static Core instance;
  private boolean printDebug = true;
  private Parser parser;
  private Interface i;
  private double zoom = 1; // zoom level
  private Optimisation algo;

  private SVGDocument svg = null;

  /**
   * Constructeur
   */
  private Core() {
    // initialisation du parseur
    parser = new Parser();
    // et de l'algo d'optimisation
    algo = new Optimisation(this);

    // initialisation de l'UI
    changeLookDefaultUI();
    EventQueue.invokeLater(() -> {
      i = new Interface();
      i.setVisible(true);
    });
  }

  /**
   * Récupérer l'unique instance du Core
   * @return Core.instance
   */
  public static Core getInstance() {
    if (instance == null) { // meilleures perfs
      synchronized (Core.class) {
        if (instance == null) {
          instance = new Core();
        }
      }
    }
    return instance;
  }

  /**
   * Parse le SVG et le place dans l'attribut Core.svg
   * @param url du fichier svg à parser
   */
  public void parse(String url) {
    svg = parser.parse(url);
  }

  /**
   * Optimise le SVG
   */
  public void optimize() {
    if (svg != null) {
     double height = i.getCurrentHeight();
      height *= zoom;
      try {
        svg = algo.getResult(svg, height);
      } catch(HeightException e) {
        JOptionPane.showMessageDialog(
          null,
          e.getMessage(),
          "Pb",
          JOptionPane.ERROR_MESSAGE
        );
      }
    }
  }

  /**
   * Récupère le svg sous forme de chaîne de caractères
   * @return svg
   */
  public String getParsedContent() {
    return (svg != null) ? svg.toString() : "";
  }

  /**
   * Récupère le svg
   * @return svg
   */
  public SVGDocument getSVG() {
    return svg;
  }

  /**
   * Lance le mode debug (affiche plus d'informations)
   */
  public void startDebug() {
    printDebug = true;
  }

  /**
   * Arrête le mode debug
   */
  public void stopDebug() {
    printDebug = false;
  }

  /**
   * Affiche un message uniquement en situation de debug
   */
  public void debug(String msg) {
    if (printDebug) {
      System.out.println("DEBUG: " + msg);
    }
  }

  /**
   * Affiche un message d'erreur et arrête le programme
   */
  public void error(String msg) {
    System.out.println("ERROR: " + msg);
    System.exit(1);
  }

  /**
   * Indique l'url à parser à l'interface
   * @param url l'url à parser
   */
  public void setSvgUri(String uri) {
    i.setCurrentURI(uri);
  }

  /**
   * Récupère l'url à parser depuis le champ dédié de l'interface
   * @return String représentant l'url indiquée par l'utilisateur
   */
  public String getSvgUri() {
    return i.getCurrentURI();
  }

  /**
   * Personnalisation de la fenêtre
   */
  private void changeLookDefaultUI() {
    try {
      for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
        if ("Nimbus".equals(info.getName())) {
          UIManager.setLookAndFeel(info.getClassName());
          break;
        }
      }
    } catch (ClassNotFoundException
      | InstantiationException
      | IllegalAccessException
      | UnsupportedLookAndFeelException ex) {
      error("Impossible de définir un look pour l'UI. Arrêt.");
    }
  }

  /**
   * Envoie un niveau de zoom à l'interface
   * @param zoom niveau de zoom souhaité (1 par défaut)
   */
  public void setZoom(double zoom) {
    i.changeZoom(zoom);
    this.zoom = zoom;
  }

  /**
   * Récupérer le niveau de zoom utilisé
   * @return zoom
   */
  public double getZoom() {
    return zoom;
  }

  /**
   * Empêche l'utilisateur d'interagir avec les composants de l'interface
   */
  public void disableComponents() {
    i.disableComponents();
  }

  /**
   * Autorise l'utilisateur à interagir avec les composants de l'interface
   */
  public void enableComponents() {
    i.enableComponents();
  }

  /**
   * Permet d'écrire des logs dans la zone prévue à cet effet
   */
   public void log(String str) {
    i.log(str);
  }

}
