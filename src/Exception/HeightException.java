// Exception levée si la hauteur est trop petite

public class HeightException extends UserException {
  public HeightException() {
    super("La hauteur de la bande est trop petite !");
  }

  public HeightException(String str) {
    super(str);
  }
}
