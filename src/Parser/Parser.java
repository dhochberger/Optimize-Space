import java.io.IOException;


class Parser {
  private XMLParser parser;
  private XMLDocument xml = null;
  private SVGDocument svg;

  /**
   * Constructeurs
   */
  public Parser(String url) {
    parser = new XMLParser();
    parse(url);
  }

  public Parser() {
    parser = new XMLParser();
  }

  /**
   * Indique l'url au parseur, le lance, et enregistre le SVGDocument
   * dans l'attribut svg
   * @param url du fichier à parser
   * @return SVGDocument correspondant au fichier SVG parsé
   */
  public SVGDocument parse(String url) {
    parser.setUrl(url);
    try {
      xml = parser.parse();
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println(" => Le fichier semble inexistant.");
      System.exit(1);
    }
    svg = new SVGDocument(xml);
    return svg;
  }

  /**
   * Affichage du SVG en tant que String (pour la sauvegarde dans un fichier)
   * @return la chaîne de caractère représentant le SVG
   */
  public String toString() {
    return parser.toString();
  }

  // public SVGDocument scale(int zoom) {
  //   svg = new SVGDocument(xml);
  //   svg.scale(zoom);
  //   return svg;
  // }
}
