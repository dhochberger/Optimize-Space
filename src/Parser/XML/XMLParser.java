import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;
import java.util.Vector;
import java.util.regex.Pattern;


public class XMLParser {

  private String url;
  private String content;
  private int contentLength;
  private int cursor;
  private char lastChar;
  private int deep;

  private Vector<Attribute> xmlProlog;
  private Tag firstTag; // balise prncipale

  /**
   * Constructeurs
   */
  public XMLParser() {
  }

  public XMLParser(String url) {
    this.url = url;
  }

  /**
   * Lance le parseur (méthode principale)
   * @return XMLDocument à partir de l'url
   */
  public XMLDocument parse() throws IOException {
    cursor = 0;
    contentLength = 0;
    content = null;
    fetchContent(); // on récupère le contenu
    parseXMLProlog(); // on parse le prologue XML
    read_firstTag(); // on parse tout le contenu
    return new XMLDocument(xmlProlog, firstTag);
  }

  /**
   * Récupérer une lecture du contenu (local/depuis une URL)
   * @return Stream<String> le contenu du fichier
   */
  private Stream<String> fetchStream() throws IOException {
    if (url.startsWith("http")) { // s'il s'agit d'une URL
      InputStream is = new URL(url).openConnection().getInputStream();
      BufferedReader reader = new BufferedReader(new InputStreamReader(is));
      return reader.lines();
    } else { // sinon c'est un fichier classique
      return Files.lines(Paths.get(url));
    }
  }

  /**
   * Utilise le fichier récupéré par fetchStream :
   * Récupère le contenu, le met en une ligne unique, le nettoie
   */
  private void fetchContent() throws IOException {
    StringBuilder contentBuilder = new StringBuilder();
    Stream<String> stream = fetchStream();
    stream.map(s -> s.trim())
      .filter(s -> !s.isEmpty())
      .forEach(s -> contentBuilder.append(s).append(" "));
    content = contentBuilder.toString()
      .replaceAll("(?i)" + Pattern.quote("<!") + "[^>]*>", "")
      .replaceAll("  *", " ")
      .replaceAll("<!--(.*?)-->", "")
      .replaceAll("  *", " ")
      .replaceAll("> <", "><")
      .trim();
    contentLength = content.length();
  }

  /**
   * Parse le prologue (première ligne du fichier XML)
   */
  public void parseXMLProlog() {
    xmlProlog = new Vector<Attribute>();
    Attribute attr;
    if (read_string("<?xml")) {
      read_spaces();
      while ((attr = read_attribute()) != null) {
        xmlProlog.addElement(attr);
        read_spaces();
      }
      if (!read_string("?>")) error("Mauvais prologue XML.");
    }
  }

  /**
   * Indique s'il y a du contenu à parser
   * @return boolean
   */
  private boolean isContent() {
    return content != null && !content.isEmpty();
  }

  /**
   * Envoi de l'erreur au noyau (Core)
   * @param string à afficher
   */
  private void error(String str) {
    Core core = Core.getInstance();
    core.error(str);
  }

  /**
   * Incrémentation du curseur à la lecture d'un "espace"
   */
  private void read_spaces() {
    if (!isContent()) error("Contenu vide !");
    while (cursor < contentLength && content.charAt(cursor) == ' ') cursor++;
  }

  /**
   * Indique si le caractère attendu a été lu
   * @param caractère attendu
   * @return boolean qui indique s'il a été lu
   */
  private boolean read_char(char c) {
    if (!isContent()) error("Contenu vide !");
    if (cursor < contentLength && content.charAt(cursor) == c) {
      lastChar = c;
      cursor++;
      return true;
    }
    return false;
  }

  /**
   * Indique si la chaîne attendue a été lue
   * @param chaîne attendue
   * @return boolean qui indique si elle a été lue
   */
  private boolean read_string(String str) {
    if (!isContent()) error("Contenu vide !");
    int initCursor = cursor;
    int strLen = str.length();
    if ((cursor + strLen) >= contentLength) return false;
    for (int i = 0; i < strLen; i++) {
      if (!read_char(str.charAt(i))) {
        cursor = initCursor;
        return false;
      }
    }
    return true;
  }

  /**
   * Indique si la chaîne attendue a été lue sans prendre compte de la casse
   * @param chaîne attendue
   * @return boolean qui indique si elle a été lue
   */
  private boolean read_string_insensitive(String str) {
    if (!isContent()) error("Contenu vide !");
    int initCursor = cursor;
    int strLen = str.length();
    if ((cursor + strLen) >= contentLength) return false;
    for (int i = 0; i < strLen; i++) {
      if (!read_char(Character.toLowerCase(str.charAt(i)))
        && !read_char(Character.toUpperCase(str.charAt(i)))) {
        cursor = initCursor;
        return false;
      }
    }
    return true;
  }

  /**
   * Indique si le caractère peut appartenir à un nom d'attribut
   * @return boolean
   */
  private boolean isCharForAttr() {
    char currentChar = content.charAt(cursor);
    return ((currentChar >= 'a' && currentChar <= 'z')
      || (currentChar >= 'A' && currentChar <= 'Z')
      || (currentChar >= '0' && currentChar <= '9')
      || currentChar == ':' || currentChar == '-'
      || currentChar == '_');
  }

  /**
   * Indique si le caractère peut appartenir à une valeur d'attribut
   * @return boolean
   */
  private boolean isCharForAttrValue(boolean allowSpaces, char separator) {
    char currentChar = content.charAt(cursor);
    if (allowSpaces) return currentChar != separator;
    else return currentChar != ' ';
  }

  /**
   * Lecture d'un attribut
   * @return l'attribut lu
   */
  private Attribute read_attribute() {
    if (!isContent()) error("Contenu vide !");
    Attribute attr = null;
    StringBuilder attrName = new StringBuilder();
    while (cursor < contentLength && isCharForAttr()) {
      attrName.append(content.charAt(cursor));
      cursor++;
    }
    if (attrName.length() > 0) {
      attr = new Attribute(attrName.toString().toLowerCase());
      if (read_char('=')) {
        boolean hasQuote = read_char('"') || read_char('\'');
        char separator = lastChar;
        StringBuilder attrValue = new StringBuilder();
        while (cursor < contentLength
          && isCharForAttrValue(hasQuote, separator)) {
          attrValue.append(content.charAt(cursor));
          cursor++;
        }
        if (hasQuote && !read_char(separator)) {
          error("Séparateur '" + separator + "' manquant pour un attribut.");
        }
        if (hasQuote) attr.setSeparator(separator);
        attr.setValue(attrValue.toString());
      }
    }
    return attr;
  }

  /**
   * Lecture du contenu compris entre deux balises
   * @return le contenu lu
   */
  private String read_tagString() {
    read_spaces();
    lastChar = content.charAt(cursor++);
    StringBuilder s = new StringBuilder();
    while (cursor < contentLength && lastChar != '<') {
      s.append(lastChar);
      lastChar = content.charAt(cursor++);
    }
    if (lastChar == '<') cursor--;
    return s.toString().trim();
  }

  /**
   * Lecture de la première balise, qui contient toutes les autres
   */
  private void read_firstTag() {
    deep = 0;
    firstTag = read_tag();
    if (firstTag == null) error("Aucune balise trouvée.");
  }

  /**
   * Lecture d'un tag quelconque, dans sa totalité (attributs)
   * @return Tag lu à partir du XML
   */
  private Tag read_tag() {
    Tag resTag, t;
    StringBuilder tagName = new StringBuilder();
    read_spaces();

    // une balise commence toujours par un '<'
    if (!read_char('<')) return null;
    while (cursor < contentLength && isCharForAttr()) {
      tagName.append(content.charAt(cursor++));
    }

    if (tagName.length() == 0) {
      cursor--; // décrémente le curseur car on a lu un '<' qu'il ne fallait pas
      return null;
    }

    // on a donc déjà un nom à notre balise !
    resTag = new Tag(tagName.toString().toLowerCase());
    read_spaces();

    // on récupère les attributs
    Attribute attr;
    while ((attr = read_attribute()) != null) {
      resTag.addAttribute(attr);
      read_spaces();
    }
    resTag.setDeep(deep++); // entrée dans une balise : augmente la profondeur
    if (read_string("/>")) {
      resTag.setAutoClose();
      deep--; // si c'était une balise auto-fermante, on le rabaisse à nouveau
      return resTag;
    }
    if (!read_char('>')) {
      error("Caractère '>' manquant pour la balise '" + tagName + "'.");
    }

    // on lit toutes les balises enfant
    while ((t = read_tag()) != null) resTag.addChild(t);

    // si jamais le contenu de la balise était une chaîne de caratcère..
    resTag.setContent(read_tagString());

    // les petites vérifications de fin
    if (!read_string("</")) {
      error("Balise de fermeture manquante pour '" + tagName + "'.");
    }
    read_spaces();
    if (!read_string_insensitive(tagName.toString())) {
      error("Balise de fin manquante pour '" + tagName + "'.");
    }
    read_spaces();
    if (!read_char('>')) {
      error("Caractère '>' manquant pour la fermeture de '" + tagName + "'.");
    }

    deep--; // on sort d'une balise, on baisse d'un niveau hiérarchique
    return resTag;
  }

  /**
   * Définition de l'URL à traiter
   * @param url
   */
  public void setUrl(String url) {
    this.url = url;
  }
}
