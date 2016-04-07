package net.samongi.frunction.expression.tokens;

import net.samongi.frunction.expression.exceptions.TokenException;

public interface Token
{
  /**
   * Types of the tokens
   */
  public enum Type
  {
    SYMBOL, ACCESSOR, GROUP, INPUT, FRUNCTION
  }

  /**
   * Gets the scope openers
   * 
   * @return A list of strings that identify scope openers
   */
  public static String[] getScopeOpenIdentifiers()
  {
    return new String[] { FrunctionToken.OPEN, InputToken.OPEN };
  }

  /**
   * Gets the scope closers
   * 
   * @return A list of strings that identify scope closers
   */
  public static String[] getScopeCloseIdentifiers()
  {
    return new String[] { FrunctionToken.CLOSE, InputToken.CLOSE };
  }

  /**
   * Gets the scope toggles
   * 
   * @return
   */
  public static String[] getScopeeToggleIdentifiers()
  {
    return new String[] { "\"" };
  }

  public static Token.Type[] arrayToTypeArray(Token[] tokens)
  {
    Token.Type[] type_array = new Token.Type[tokens.length];
    for(int i = 0; i < tokens.length; i++)
      type_array[i] = tokens[i].getType();
    return type_array;
  }

  /**
   * Parses the string trying to find tokens If multiple tokens are found, it
   * will return a group token.
   * 
   * @param source
   * @return
   * @throws TokenException
   */
  public static GroupToken parseTokens(String source) throws TokenException
  {
    return new GroupToken(source);
  }

  /**
   * Returns the source of this token The source will be a string that the token
   * is generated from. This will not include identifiers for the token such as
   * parens for input tokens.
   * 
   * @return
   */
  public String getSource();

  /**
   * Returns the type of the token. This can be used with just the class but
   * this is more official
   * 
   * @return
   */
  public Token.Type getType();
}
