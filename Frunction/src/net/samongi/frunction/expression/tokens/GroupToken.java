package net.samongi.frunction.expression.tokens;

import java.util.ArrayList;
import java.util.List;

import net.samongi.frunction.expression.exceptions.TokenException;
import net.samongi.frunction.parse.ParseUtil;

/**The main building block of tokens
 * This contains a list of tokens and will construct it's own list based on its source.
 *
 */
public class GroupToken implements Token
{
	

	private final String source;
	private List<Token> tokens = null;
	
	public GroupToken(String source)
	{
		this.source = source;
	}
	
	public void evaluate() throws TokenException
	{
		if(this.tokens != null) return;
		this.tokens = new ArrayList<>();
		
		int i = 0;
	  while(i < this.source.length())
		{
	  	// Creating accessor tokens
			if(ParseUtil.matchesAt(source, i, AccessorToken.OPERATOR))
			{
        i += AccessorToken.OPERATOR.length();
        
				tokens.add(new AccessorToken());
			}
			// Creating input tokens
			if(ParseUtil.matchesAt(source, i, InputToken.OPEN))
			{
				i += InputToken.OPEN.length();
				// Getting the section until the close identifier
				// This will start after the open identifier
				String section = ParseUtil.getSection(source, i, InputToken.CLOSE, 
				    Token.getScopeOpenIdentifiers(), Token.getScopeCloseIdentifiers());
				// Incrementing the next index based on the section length found
				i += section.length();
				
				// Checking to see if the section end correctlys.
				if(!ParseUtil.matchesAt(section, section.length() - InputToken.CLOSE.length(), InputToken.CLOSE))
				{
				  throw new TokenException(); // Throwing the exception
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
        String section = ParseUtil.getSection(source, i, FrunctionToken.CLOSE, 
            Token.getScopeOpenIdentifiers(), Token.getScopeCloseIdentifiers());
        // Incrementing the next index based on the section length found
        i += section.length();
        
        // Checking to see if the section end correctlys.
        if(!ParseUtil.matchesAt(section, section.length() - FrunctionToken.CLOSE.length(), FrunctionToken.CLOSE))
        {
          throw new TokenException(); // Throwing the exception
        }
        
        // Creating the actual token
        FrunctionToken token = new FrunctionToken(section.substring(0, section.length() - FrunctionToken.CLOSE.length()));
        this.tokens.add(token);
			}
			
			// otherwise its the start of a symbol
			int sym_start = i;
			String[] not_sym = new String[]{InputToken.OPEN, InputToken.CLOSE, FrunctionToken.OPEN, FrunctionToken.CLOSE, AccessorToken.OPERATOR};
			// going while the character can still be part of a symbol
			while(!ParseUtil.matchesAt(source, i, not_sym) && i < source.length()) i++;
			// If the sym_i ends at an index, that means it matches an identifier
			String section = source.substring(sym_start, i);
			if(section.length() > 0)
			{
			  SymbolToken token = new SymbolToken(section);
			  this.tokens.add(token);
			}
		}
	}
	
	/**Returns an array of tokens that is generated by this GroupToken
	 * 
	 * @return An array of tokens contained within this group token.
	 */
	public Token[] getTokens() throws TokenException
	{
		if(this.tokens == null) this.evaluate();
		return this.tokens.toArray(new Token[this.tokens.size()]);
	}
	
	@Override public String getSource(){return this.source;}
  @Override public Token.Type getType(){return Token.Type.GROUP;}
	
}