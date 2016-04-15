package net.samongi.frunction.expression.tokens;

import java.util.ArrayList;
import java.util.List;

import net.samongi.frunction.error.syntax.TokenError;
import net.samongi.frunction.frunction.literal.StringFrunction;
import net.samongi.frunction.parse.ParseUtil;

/** The main building block of tokens This contains a list of tokens and will construct it's own list based on its
 * source. */
public class GroupToken implements Token
{

  private final String source;
  private List<Token> tokens = null;

  public GroupToken(String source)
  {
    this.source = source.trim();
  }

  // checks to see if the prebious index is a number
  private boolean prevIsNumber(int i)
  {
    return (i > 0) && (source.substring(i - 1, i).matches("\\d"));
  }

  // checks to see if the next index is a number
  private boolean nextIsNumber(int i)
  {
    return (i < source.length() - 1) && (source.substring(i + 1, i + 2).matches("\\d"));
  }

  public void evaluate() throws TokenError
  {
    if(this.tokens != null) return;
    this.tokens = new ArrayList<>();

    int i = 0;
    while(i < this.source.length())
    {
      // Creating accessor tokens
      if(ParseUtil.matchesAt(source, i, AccessorToken.OPERATOR) && !(this.prevIsNumber(i) && this.nextIsNumber(i)))
      {
        // TODO make it so REALS are not split up by this operator
        i += AccessorToken.OPERATOR.length();

        tokens.add(new AccessorToken());
      }
      // Creating input tokens
      if(ParseUtil.matchesAt(source, i, InputToken.OPEN))
      {
        i += InputToken.OPEN.length();
        // Getting the section until the close identifier
        // This will start after the open identifier
        String section = ParseUtil.getSection(source, i, InputToken.CLOSE, Token.getScopeOpenIdentifiers(), Token.getScopeCloseIdentifiers(), Token.getScopeeToggleIdentifiers());
        // Incrementing the next index based on the section length found
        i += section.length();

        // Checking to see if the section ends correctly.
        if(!ParseUtil.matchesAt(section, section.length() - InputToken.CLOSE.length(), InputToken.CLOSE)) { throw new TokenError(section); // Throwing the exception
        }

        // Creating the actual token
        InputToken token = new InputToken(section.substring(0, section.length() - InputToken.CLOSE.length()));
        this.tokens.add(token);
      }
      // Creating frunction tokens
      if(ParseUtil.matchesAt(source, i, FrunctionToken.OPEN))
      {
        i += FrunctionToken.OPEN.length();
        // Getting the section until the close identifier
        // This will start after the open identifier
        String section = ParseUtil.getSection(source, i, FrunctionToken.CLOSE, Token.getScopeOpenIdentifiers(), Token.getScopeCloseIdentifiers(), Token.getScopeeToggleIdentifiers());
        // Incrementing the next index based on the section length found
        i += section.length();

        // Checking to see if the section end correctlys.
        if(!ParseUtil.matchesAt(section, section.length() - FrunctionToken.CLOSE.length(), FrunctionToken.CLOSE)) { throw new TokenError(section); // Throwing the exception
        }

        // Creating the actual token
        FrunctionToken token = new FrunctionToken(section.substring(0, section.length() - FrunctionToken.CLOSE.length()));
        this.tokens.add(token);
      }

      // otherwise its the start of a symbol
      int sym_start = i;

      // Checking to see if the symbol start is encapsilated
      boolean encapped = ParseUtil.matchesAt(this.source, i, StringFrunction.STRING_CAPSULE);

      String[] not_sym = new String[] { InputToken.OPEN, InputToken.CLOSE, FrunctionToken.OPEN, FrunctionToken.CLOSE };
      // going while the character can still be part of a symbol
      while((!(!encapped && ParseUtil.matchesAt(this.source, i, not_sym)) // If the index is not a symbol
      && !(ParseUtil.matchesAt(this.source, i, AccessorToken.OPERATOR) && !(this.prevIsNumber(i) && this.nextIsNumber(i)))) // If the index is a
                                                                                                                            // valid operator
          && i < this.source.length())
      {
        i++;
        if(ParseUtil.matchesAt(this.source, i, StringFrunction.STRING_CAPSULE))
        {
          if(encapped) encapped = false;
          else encapped = true;
        }
      }
      // If the sym_i ends at an index, that means it matches an identifier
      String section = source.substring(sym_start, i);
      if(section.length() > 0)
      {
        SymbolToken token = new SymbolToken(section);
        this.tokens.add(token);
      }
    }
  }

  /** Returns an array of tokens that is generated by this GroupToken
   * 
   * @return An array of tokens contained within this group token. */
  public Token[] getTokens() throws TokenError
  {
    if(this.tokens == null) this.evaluate();
    return this.tokens.toArray(new Token[this.tokens.size()]);
  }

  /** Returns a list of this group's token's types
   * 
   * @return A string representing the types */
  public String displayTypes()
  {
    String str = "[";
    for(Token t : this.tokens)
    {
      if(t == null) str += "null";
      else str += t.getType().toString();
    }
    str += "]";
    return str;
  }

  @Override public String getSource()
  {
    return this.source;
  }

  @Override public Token.Type getType()
  {
    return Token.Type.GROUP;
  }

}
