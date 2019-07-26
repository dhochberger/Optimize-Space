import java.util.Vector;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;

public class Optimisation {

  private SVGDocument document;
  private Core core;

  /**
   * Constructeur
   */
  public Optimisation(Core core) {
    this.core = core;
  }

  // retourne le X actuel
  public double getCurrentX(Vector<SVGPathCollection> groups) {
    double res = 0;
    for (SVGPathCollection group : groups) {
      double max = group.getMaxBoundsX();
      if (max > res) res = max;
    }
    return res;
  }


  /**
   * Récupère le svg à traiter et renvoie le résultat
   * @param svg le document à traiter
   * @param maxHeight la hauteur de la plaque
   * @return svg le document traité
   */
  public SVGDocument getResult(SVGDocument svg, double maxHeight) throws HeightException {
    Vector<SVGPathCollection> groups = new Vector<SVGPathCollection>();
    groups = svg.getCollections();

    // tri à bulle
    int longueur = groups.size();
    SVGPathCollection tmpSwap;
    boolean inversion;
    do {
      inversion = false;
      for (int i = 0; i < longueur - 1; i++) {
        if (groups.get(i).getWidth() < groups.get(i + 1).getWidth()) {
          tmpSwap = groups.get(i);
          groups.set(i, groups.get(i + 1));
          groups.set(i + 1, tmpSwap);
          inversion = true;
        }
      }
    } while (inversion);

    boolean first = true;
    int count = 0; // tests
    double height;
    double heightBefore = 0;
    double widthBefore = 0;
    double colWidth = 0;
    int sizeVect = groups.size();
    SVGPathCollection lastGroup = groups.get(0);

    double x_before, x_after;

    int x_max = 0;

    if (maxHeight <= 0) throw new HeightException("La hauteur doit être strictement positive !");
    for (SVGPathCollection group : groups) {
      // si l'un des groupes ne rentre pas dans la taille indiquée
      if (group.getHeight() > maxHeight) {
        throw new HeightException();
      }

      x_max += group.getWidth();
    }

    x_before = getCurrentX(groups);

    // pour chaque groupe
    for (SVGPathCollection group : groups) {

      count++;

      height = group.getHeight();

      // place l'ensemble des chemins à gauche du document
      while (group.getBoundsX() > 0) {
        group.translate(-1, 0);
      }

      //place l'ensemble des chemins en haut du document
      while (group.getBoundsY() > 0) {
        group.translate(0, -1);
      }

      // définit s'il s'agit du premier groupe d'une colonne
      first = (heightBefore == 0) || (heightBefore + height > maxHeight);

      if (first) {
        heightBefore = 0;
      }

      group.translate(0, heightBefore);

      lastGroup = group;

      if (first) {
        heightBefore = height;
        widthBefore += colWidth;
        colWidth = group.getWidth();
      } else {
        double getWidthGroup = group.getWidth();
        if (colWidth < getWidthGroup) colWidth = getWidthGroup;
        heightBefore += height;
      }
      group.translate(widthBefore, 0);
    }

    x_after = getCurrentX(groups);


    return svg;
  }
}
