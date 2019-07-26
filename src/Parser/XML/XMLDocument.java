import java.util.Vector;


public class XMLDocument {

  private Vector<Attribute> prolog; // le prologue xml
  private Tag tag; // tag principal

  /**
   * Constructeur
   * Les attributs sont récupérés par le parseur
   * @param prolog suite d'attributs indiqués dans la première ligne
   * @param tag premier tag du fichier
   */
  public XMLDocument(Vector<Attribute> prolog, Tag tag) {
    this.prolog = prolog;
    this.tag = tag;
  }

  /**
   * Récupérer le tag principal (le premier) du document XML
   */
  public Tag getTag() {
    return tag;
  }

  /**
   * Retourne le document parsé au format texte
   * @return String document XML parsé
   */
  public String toString() {
    StringBuilder r = new StringBuilder("<?xml");
    if (prolog != null) for (Attribute a: prolog) r.append(a);
    r.append("?>\n");
    if (tag != null) r.append(tag);
    return r.toString();
  }

}
