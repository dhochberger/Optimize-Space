import java.util.Vector;


public class XMLTag {

  private String name;
  private Vector<Attribute> attrs;
  private boolean autoClose = false;
  private Vector<Tag> childs;
  private String content = "";
  private int deep = 0;

  private final static String TABULATION = "  ";

  /**
   * Constructeur
   * @param nom du tag
   */
  public XMLTag(String name) {
    this.name = name;
    attrs = new Vector<Attribute>();
    childs = new Vector<Tag>();
  }

  /**
   * Ajouter un attribut à un tag
   * @param attribut à ajouter
   */
  public void addAttribute(Attribute attr) {
    attrs.addElement(attr);
  }

  /**
   * Ajouter un vecteur d'attributs à un tag
   * @param attributs à ajouter
   */
  public void addAttribute(Vector<Attribute> a) {
    for (Attribute attr: a) addAttribute(attr);
  }

  /**
   * Indique si le tag possède un attribut donné
   * @param nom de l'attribut recherché
   * @return boolean
   */
  public boolean hasAttribute(String name) {
    for (Attribute attr : attrs) {
      if (attr.getLowerName().equals(name)) return true;
    }
    return false;
  }

  /**
   * Retourne l'attribut du tag selon son nom
   * @param nom de l'attribut recherché
   * @return attribut
   */
  public Attribute getAttribute(String name) {
    for (Attribute attr: attrs) {
      if (attr.getLowerName().equals(name)) return attr;
    }
    return null;
  }

  /**
   * Ajoute un tag à l'intérieur de celui-ci (dans le vecteur childs)
   * @param tag à ajouter
   */
  public void addChild(Tag tag) {
    childs.addElement(tag);
  }

  /**
   * Indique que le tag est une balise auto-fermante
   */
  public void setAutoClose() {
    autoClose = true;
  }

  /**
   * Définit le contenu de la balise
   */
  public void setContent(String content) {
    this.content = content;
  }

  /**
   * Indique la profondeur du tag (son niveau dans l'indentation)
   * @param int profondeur indiquée par l'appelant
   */
  public void setDeep(int deep) {
    this.deep = deep;
  }

  /**
   * Récupérer le niveau du tag dans l'indentation
   * @return int profondeur
   */
  public int getDeep() {
    return deep;
  }

  /**
   * Indente le tag selon sa profondeur (affichage des tabulations)
   * @return string constituée des tabulations
   */
  private String indent() {
    StringBuilder s = new StringBuilder();
    for (int i = 0; i < deep; i++) s.append(TABULATION);
    return s.toString();
  }

  /**
   * Récupérer le nom du tag
   * @return nom
   */
  public String getName() {
    return name;
  }

  /**
   * Récupérer le nom du tag en minuscules
   * @return nom en minuscules
   */
  public String getLowerName() {
    return getName().toLowerCase();
  }

  /**
   * Récupérer les enfants du tag
   * @return childs
   */
  public Vector<Tag> getChilds() {
    return childs;
  }

  /**
   * Récupérer les attributs du tag
   * @return attributes
   */
  public Vector<Attribute> getAttributes() {
    return attrs;
  }

  /**
   * Retourner le tag sous forme de chaîne de caractères
   * @return la chaîne représentant le tag
   */
  public String toString() {
    StringBuilder r = new StringBuilder();
    if (!name.equals("svg")) r.append("\n");
    r.append(indent()).append("<").append(name);
    for (Attribute attr : attrs) r.append(attr.toString());
    if (autoClose) return r.append("/>").toString();
    r.append(">").append(content);
    for (Tag tag : childs) r.append(tag.toString());
    if (content.isEmpty() && childs.size() > 0) r.append("\n").append(indent());
    r.append("</").append(name).append(">");
    return r.toString();
  }
}
