public class XMLAttribute {

  private String name;
  private String value;
  private char separator = '\0'; // ' or "

  /**
   * Constructeurs
   * Un attribut a toujours un nom au moins
   */
  public XMLAttribute(String name) {
    this.name = name;
  }

  public XMLAttribute(String name, String value) {
    this.name = name;
    this.value = value;
    if (value.contains("\"")) separator = '\'';
    else separator = '"';
  }

  /**
   * Modifier la valeur d'un attribut
   * @param valeur à donner à l'attribut
   */
  public void setValue(String value) {
    this.value = value;
  }

  /**
   * Choix des quotes/guillemets pour l'attribut
   * @param caractère choisi
   */
  public void setSeparator(char separator) {
    this.separator = separator;
  }

  /**
   * Récupérer le caractère utilisé
   * @return caractère (quotes/guillemets)
   */
  public char getSeparator() {
    return separator;
  }

  /**
   * Récupérer le nom de l'attribut
   * @return nom
   */
  public String getName() {
    return name;
  }

  /**
   * Récupérer le nom de l'attribut en minuscules
   * @return nom en minuscules
   */
  public String getLowerName() {
    return getName().toLowerCase();
  }

  /**
   * Récupérer la valeur de l'attribut
   * @return valeur (string)
   */
  public String getValue() {
    return value;
  }

  /**
   * Retourne l'attribut sous la forme d'une chaîne de caractères
   * @return la chaîne représentant l'attribut
   */
  public String toString() {
    StringBuilder str = new StringBuilder(" ");
    if (value.isEmpty()) return str.append(name).toString();
    return str.append(getName()).append("=").append(getSeparator())
      .append(getValue()).append(getSeparator()).toString();
  }
}
