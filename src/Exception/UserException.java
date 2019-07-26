// exception levée si l'utilisateur a fait une mauvaise action
public class UserException extends Exception {
  public UserException() {
    super();
  }

  public UserException(String message) {
    super(message);
  }
}
