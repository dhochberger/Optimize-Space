// attribute container

public class Attribute {
  private XMLAttribute inner;

  public Attribute(String name) {
    inner = new XMLAttribute(name);
  }

  public Attribute(String name, String value) {
    inner = new XMLAttribute(name, value);
  }

  public void SVGUpgrade(Point translationPoint) {
    if (getLowerName().equals("d")) {
      SVGPath path = new SVGPath(getValue(), translationPoint);
      path.setSeparator(inner.getSeparator());
      inner = path;
    }
  }

  public XMLAttribute getInner() {
    return inner;
  }

  public void setValue(String value) {
    inner.setValue(value);
  }

  public void setSeparator(char separator) {
    inner.setSeparator(separator);
  }

  public char getSeparator() {
    return inner.getSeparator();
  }

  public String getName() {
    return inner.getName();
  }

  public String getLowerName() {
    return inner.getLowerName();
  }

  public String getValue() {
    return inner.getValue();
  }

  public String toString() {
    return inner.toString();
  }
}
