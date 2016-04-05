package net.samongi.frunction.expression.tokens;

import java.util.ArrayList;
import java.util.List;

import net.samongi.frunction.expression.exceptions.TokenException;
import net.samongi.frunction.parse.ParseUtil;


public class InputToken implements Token
{
	public static final String OPEN = "(";
	public static final String CLOSE = ")";
	public static final String SEPERATOR = ",";
	
  private final String source;
  
  private GroupToken[] input_tokens = null;
  
  public InputToken(String source)
  {
  	this.source = source.trim();
  }
  
  public void evaluate() throws TokenException
  {
  	if(this.input_tokens != null) return;
  	int i = 0;
  	List<String> section_list = new ArrayList<>();
  	while(i < source.length())
  	{
  	  String section = ParseUtil.getSection(this.source, i, SEPERATOR, Token.getScopeOpenIdentifiers(), Token.getScopeCloseIdentifiers(), Token.getScopeeToggleIdentifiers());
  	  i += section.length();
  	  
  	  // if it ends with the seperator, remove it.
  	  if(ParseUtil.matchesAt(section, section.length() - SEPERATOR.length(), SEPERATOR)) section_list.add(section.substring(0, section.length() - SEPERATOR.length()));
  	  else section_list.add(section);
  	}
  	// System.out.println("  InputTokens - Section Amount: " + section_list.size());
  	// Translating the sections into new GroupTokens
  	this.input_tokens = new GroupToken[section_list.size()];
  	for(int c = 0; c < section_list.size(); c++)
  	{
  	  this.input_tokens[c] = new GroupToken(section_list.get(c));
  	}
  	if(this.input_tokens == null) this.input_tokens = new GroupToken[0]; // Empty array if it doesn't have any.
  }
  
  public GroupToken[] getTokens() throws TokenException
  {
    if(this.input_tokens == null) this.evaluate();
    return this.input_tokens;
  }
  
  @Override public String getSource(){return this.source;}
  @Override public Token.Type getType(){return Token.Type.INPUT;}
}
