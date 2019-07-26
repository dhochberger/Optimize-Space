import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;


public class RunButton extends JButton {

  private boolean started = false;
  private boolean optimizing = false;

  /**
   * Constructeur du bouton permettant de lancer le parseur
   */
  public RunButton() {
    super();

    this.setText(">> GO <<");
    this.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        runAction();
      }
    });
  }

  /**
   * Définit l'action au clic
   * Ici, lancer le parseur et remplir la fenêtre avec le SVG
   * Ou au contraire, permettre de réactiver les composants de la fenêtre
   */
  public void runAction() {
    Core core = Core.getInstance();
    core.debug("clic sur le bouton effectué !");
    DrawingZone d = DrawingZone.getInstance();
    if (!started) {
      String currentFile = core.getSvgUri();

      if (!currentFile.isEmpty()) {
        core.log("Lancement...");
        started = true;
        core.log("parsing..");
        core.parse(currentFile);
        d.repaint();
        core.disableComponents();
        setText(">> Optimiser <<");
        core.debug("Contenu parsé :\n" + core.getParsedContent());
      }
    } else {
      if (optimizing) {
        core.log("Arrêt.");
        core.log("================\n\n");
        core.enableComponents();
        optimizing = false;
        started = false;
      } else {
        setText(">> STOP <<");
        optimizing = true;
        d.repaint();
        core.optimize();
        d.repaint();
      }
    }
  }
}
