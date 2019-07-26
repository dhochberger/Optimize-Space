import java.io.File;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFileChooser;


public class ChooseFileButton extends JButton {

  /**
   * Constructeur du bouton permettant de choisir un fichier
   */
  public ChooseFileButton() {
    super();

    setText("Ouvrir un fichier SVG");
    addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        chooseFileAction();
      }
    });
  }

  /**
   * Définit l'action au clic sur le bouton
   * Ici l'ouverture d'une boîte de dialogue pour choisir un fichier
   */
  public void chooseFileAction() {
    File choosedFile;
    String choosedFileName;
    Core core = Core.getInstance();

    core.debug("Instanciation de la boîte de dialogue");

    JFileChooser importFile = new JFileChooser();
    File workingDirectory = new File(System.getProperty("user.dir") + "/examples/");
    importFile.setCurrentDirectory(workingDirectory);
    importFile.showOpenDialog(null); // affiche
    choosedFile = importFile.getSelectedFile();

    if (choosedFile != null) {
      choosedFileName = choosedFile.toString();
      core.debug("Fichier choisi : " + choosedFileName);
      core.setSvgUri(choosedFileName);
    }
  }
}
