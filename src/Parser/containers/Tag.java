// tag container
import java.util.Vector;


public class Tag {
  private XMLTag inner;

  public Tag(String name) {
    inner = new XMLTag(name);
  }

  public void SVGUpgrade(Point translatePoint) {
    SVGPathTag path = new SVGPathTag();
    path.setDeep(inner.getDeep());

    // on ajoute les différents attributs
    for (Attribute attr : inner.getAttributes()) {
      if (attr.getLowerName().equals("d")) {
        attr.SVGUpgrade(translatePoint);
      }
      path.addAttribute(attr);
    }

    inner = path;
  }

  public void addAttribute(Attribute attr) {
    inner.addAttribute(attr);
  }

  public void addAttribute(Vector<Attribute> a) {
    inner.addAttribute(a);
  }

  // public void addAttributes(Vector<Attribute> a) {
  //   inner.addAttributes(a);
  // }

  public boolean hasAttribute(String name) {
    return inner.hasAttribute(name);
  }

  public Attribute getAttribute(String name) {
    return inner.getAttribute(name);
  }

  public void addChild(Tag tag) {
    inner.addChild(tag);
  }

  public void setAutoClose() {
    inner.setAutoClose();
  }

  public void setContent(String content) {
    inner.setContent(content);
  }

  public void setDeep(int deep) {
    inner.setDeep(deep);
  }

  public String getName() {
    return inner.getName();
  }

  public String getLowerName() {
    return inner.getLowerName();
  }

  public Vector<Tag> getChilds() {
    return inner.getChilds();
  }

  public Vector<Attribute> getAttributes() {
    return inner.getAttributes();
  }

  public String toString() {
    return inner.toString();
  }
}
