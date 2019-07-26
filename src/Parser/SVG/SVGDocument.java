import java.util.Vector;


public class SVGDocument {

  private XMLDocument xml;
  private Core core;
  private Vector<SVGPathCollection> collections;

  public SVGDocument(XMLDocument xml) {
    collections = new Vector<SVGPathCollection>();
    this.xml = xml;
    core = Core.getInstance();
    parse();
  }

  private void parse() {
    Tag tag = basicChecks();
    core.debug("Le fichier semble OK.");
    Vector<Tag> childs = tag.getChilds();

    // si la balise SVG est vide, on a fini.
    if (childs.size() == 0) {
      core.debug("la balise SVG est vide.");
      return;
    }

    // si le premier 'g' est un calque principal, on l'ignore
    if (countTag(childs, "path") == 0 && countTag(childs, "g") == 1) {
      core.debug("le fichier SVG est composé d'un calque principal");
      Tag layer = firstTagWithName(childs, "g");
      Point translatePoint = transformTransform(layer);
      if (layer == null) core.error("une erreur est survenue pour trouver <g>");
      else parseRightLevel(layer.getChilds(), translatePoint);
    } else {
      core.debug("le fichier SVG n'est pas composé d'un calque principal");
      parseRightLevel(childs, new Point(0, 0));
    }
  }

  private Point transformTransform(Tag tag) {
    double x = 0, y = 0;
    StringBuilder newAttr = new StringBuilder();
    Attribute attrT = tag.getAttribute("transform");
    if (attrT == null) return new Point(0, 0);
    String oldAttr = attrT.getValue().trim();
    String wordToRead = "translate";
    int c = 0, cmax = oldAttr.length(), readPos = 0, rMax = wordToRead.length();

    while (c < cmax) {
      newAttr.append(oldAttr.charAt(c));
      c++;
      if (c < cmax && oldAttr.charAt(c-1) == wordToRead.charAt(readPos)) {
        readPos++;
        if (readPos == rMax) {
          readPos = 0;
          while (c < cmax && oldAttr.charAt(c) == ' ') c++;
          if (oldAttr.charAt(c) == '(') c++;
          else core.error("un '(' est attendu !");
          while (c < cmax && oldAttr.charAt(c) == ' ') c++;
          StringBuilder strX = new StringBuilder();
          StringBuilder strY = new StringBuilder();
          while (c < cmax && oldAttr.charAt(c) != ',' && oldAttr.charAt(c) != ' ') {
            strX.append(oldAttr.charAt(c++));
          }
          while (c < cmax && oldAttr.charAt(c) == ' ') c++;
          if (oldAttr.charAt(c) == ',') c++;
          else core.error("un ',' est attendu !");
          while (c < cmax && oldAttr.charAt(c) == ' ') c++;
          while (c < cmax && oldAttr.charAt(c) != ')' && oldAttr.charAt(c) != ' ') {
            strY.append(oldAttr.charAt(c++));
          }
          while (c < cmax && oldAttr.charAt(c) == ' ') c++;
          if (oldAttr.charAt(c) == ')') c++;
          else core.error("un ')' est attendu !");
          x = Double.parseDouble(strX.toString());
          y = Double.parseDouble(strY.toString());
          newAttr.append("(0, 0)");
        }
      }
    }


    attrT.setValue(newAttr.toString());
    return new Point(x, y);
  }

  // effectue les vérifications de base
  private Tag basicChecks() {
    Tag tag;
    if (xml == null) core.error("Aucun fichier XML à parser...");
    tag = xml.getTag();
    if (tag == null) core.error("le fichier est vide...");
    if (!tag.getLowerName().equals("svg")) {
      core.error("La première balise d'un fichier SVG doit être 'svg'");
    }
    return tag;
  }

  // compte le nombre de fois qu'apparaît une balise dans le même niveau
  private int countTag(Vector<Tag> tags, String name) {
    int count = 0;
    for (Tag tag : tags) {
      if (tag.getLowerName().equals(name)) count++;
    }
    return count;
  }

  // retourne le premier tag de ce type trouvé
  private Tag firstTagWithName(Vector<Tag> tags, String name) {
    for (Tag tag : tags) {
      if (tag.getLowerName().equals(name)) {
        return tag;
      }
    }
    return null;
  }

  private void parseRightLevel(Vector<Tag> tags, Point translatePoint) {
    for (Tag tag : tags) {
      if (tag.getLowerName().equals("path")) {
        core.debug("on traite une balise path");
        if (tag.getChilds().size() != 0) {
          core.debug("une balise path ne doit pas avoir de fils; on zappe.");
        } else {
          collections.add(parsePath(tag, translatePoint));
        }
      } else if (tag.getLowerName().equals("g")) {
        core.debug("on traite une balise g");
        if (tag.getChilds().size() == 0) {
          core.debug("...mais on ne fait rien puisqu'elle est vide");
        } else {
          Point translatePointG = transformTransform(tag);
          translatePointG.translate(translatePoint);
          collections.add(parseFirstLevel(tag.getChilds(), translatePointG));
        }
      }
    }
  }

  private SVGPathCollection parsePath(Tag tag, Point translatePoint) {
    SVGPathCollection c = new SVGPathCollection();
    if (!tag.getLowerName().equals("path")) return c;
    core.debug("  --on traite un path");
    if (tag.getChilds().size() != 0) {
      core.debug(" !! une balise path ne doit pas avoir de fils; on ignore.");
      return c;
    }

    if (!tag.hasAttribute("d")) {
      core.debug(" !! pas d'attribut 'd' sur ce path, on ignore.");
      return c;
    }
    Point translatePointP = transformTransform(tag);
    translatePointP.translate(translatePoint);
    tag.SVGUpgrade(translatePointP);
    Attribute attrD = tag.getAttribute("d");
    if (attrD != null) c.addPath((SVGPath) attrD.getInner());
    return c;
  }

  private SVGPathCollection parseFirstLevel(Vector<Tag> tags, Point translatePoint) {
    SVGPathCollection c = new SVGPathCollection();
    for (Tag tag : tags) {
      if (tag.getLowerName().equals("path")) {
        c.merge(parsePath(tag, translatePoint));
      } else if (tag.getLowerName().equals("g")) {
        Point translatePointG = transformTransform(tag);
        translatePointG.translate(translatePoint);
        c.merge(parseFirstLevel(tag.getChilds(), translatePointG));
      }
    }
    return c;
  }

  public String toString() {
    return xml.toString();
  }

  // version minimaliste du SVG
  public String toLightString() {
    StringBuilder str = new StringBuilder("<svg>\n");
    for (SVGPathCollection c : collections) str.append(c);
    return str.append("</svg>").toString();
  }

  public Vector<SVGPathCollection> getCollections() {
    return collections;
  }

  // public void translate(double transformX, double transformY) {
  //   for (SVGPathCollection collection : collections) {
  //     collection.translate(transformX, transformY);
  //     // collection.translate(0, 2490.9448*.2);
  //   }
  // }

  // public void scale(double size) {
  //   for (SVGPathCollection collection : collections) {
  //     collection.scale(size);
  //   }
  // }

}
