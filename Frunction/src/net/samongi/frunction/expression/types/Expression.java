package net.samongi.frunction.expression.types;

import net.samongi.frunction.expression.exceptions.TokenException;
import net.samongi.frunction.expression.tokens.FrunctionToken;
import net.samongi.frunction.expression.tokens.GroupToken;
import net.samongi.frunction.expression.tokens.InputToken;
import net.samongi.frunction.expression.tokens.SymbolToken;
import net.samongi.frunction.expression.tokens.Token;
import net.samongi.frunction.frunction.Container;
import net.samongi.frunction.frunction.Frunction;

public interface Expression
{
  public static final boolean DEBUG = false;
  
  /**Parses a string to make an expression
   * this uses Token.parseTokens and Expression.parseExpression to do so
   * As such this is a utility method and nothing much else.
   * 
   * @param source
   * @param environment
   * @return
   * @throws TokenException
   */
  public static Expression parseString(String source, Container environment) throws TokenException
  {
    source = source.trim();
    GroupToken token = Token.parseTokens(source);
    return Expression.parseTokens(token.getTokens(), environment);
  }
  /**Parses tokens to make an expression object
   * 
   * @param tokens The tokens to parse
   * @param environment The environment the tokens are being parsed in
   * @return An expression parsed from the tokens
   * @throws TokenException
   */
  public static Expression parseTokens(Token[] tokens, Container environment) throws TokenException
  {
    if(DEBUG) System.out.println("  Expression Parser: Parsing through '" + tokens.length + "' tokens.");
    if(DEBUG) System.out.println("    Token Types:");
    if(DEBUG) for(Token t : tokens) System.out.println("     - " + t.getType().toString());
    
    if(environment == null) System.out.println("  Expression Parser: Environment is null!");
    
    // Note that the left expression is null.
    Expression left_expr = null;
    int i = 0;
    while(i < tokens.length)
    {
    	if(i != 0 && left_expr == null)
    	{
    		System.out.println("  Expression Parser: left expression null on not first loop.");
    		return null;
    	}
    	
      Token t = tokens[i];
      // System.out.println("Token is of type: " + t.getType().toString() + " with source '" + t.getSource() + "'");
      // Case 1: We have a token without an accessor before it.
      //   This means that it is accessing the current environment
      //   This only occurs if we start on a symbol
      if(t.getType().equals(Token.Type.SYMBOL))
      {
        if(i != 0)
        {
          if(DEBUG) System.out.println("  Issue in Expression Parser: Symbol found it was not first");
          return null;
        }
        ContainerAccessorExpression expr = new ContainerAccessorExpression(environment, (SymbolToken) t);
        left_expr = expr; // Setting it to be the left expression
        i += 1;
      }
      // Case 2: We come accross and accessor token
      else if(t.getType().equals(Token.Type.ACCESSOR))
      {
        if(left_expr == null)
        {
          if(DEBUG) System.out.println("  Issue in Expression Parser: Accessor found left expr to be null.");
          return null; // We need a left hand expression
        }
        if(tokens.length <= i + 1) 
        {
          if(DEBUG) System.out.println("  Issue in Expression Parser: Accessor found right symbol to be null.");
          return null; // We need a right hand symbol.
        }
        if(!tokens[i + 1].getType().equals(Token.Type.SYMBOL)) return null;
        
        Token r_t = tokens[i + 1];
        FrunctionAccessorExpression expr = new FrunctionAccessorExpression(left_expr, (SymbolToken) r_t);
        left_expr = expr; // Setting it to be the left expression
        i += 2;
      }
      // Case 3: We come across a frunction token
      //   If we do, then the frunction token should be first in this scenario
      //   This is a very similar case to the symbol token
      else if(t.getType().equals(Token.Type.FRUNCTION))
      {
        if(i != 0)
        {
          if(DEBUG) System.out.println("  Issue in Expression Parser: Frunction found it was not first.");
          return null; // must be first
        }
        FrunctionExpression expr = new FrunctionExpression((FrunctionToken) t);
        left_expr = expr; // setting it to be the left expression
        i += 1;
      }
      // Case 4: We come across a method call.
      //   This requires a symbol to be before it.
      else if(t.getType().equals(Token.Type.INPUT))
      {
        if(left_expr == null)
        
        {
          if(DEBUG) System.out.println("  Issue in Expression Parser: Input left expression was null.");
          return null; // We need a left hand expression
        }
        MethodExpression expr = new MethodExpression(left_expr, (InputToken) t);
        left_expr = expr; // setting it to be the left expression
        i += 1;
      }
      // Case 5: We come across a GroupToken (which shouldn't exist yet)
      //   This should be the first token, just like frunctions and first symbols
      else if(t.getType().equals(Token.Type.GROUP))
      {
        if(i != 0)
        {
          if(DEBUG) System.out.println("  Issue in Expression Parser: Group found it was not first.");
          return null; // must be first
        }
        GroupToken g_t = (GroupToken) t;
        Expression expr = Expression.parseTokens(g_t.getTokens(), environment);
        left_expr = expr; // setting it to be the left expression
        i += 1;
      }
      else
      {
        // TODO unknown token
        if(DEBUG) System.out.println("  Issue in Expression Parser: Unknown Token");
        return null;
      }
      System.out.println("  LeftExpr: " + left_expr.getDisplay());
    }
    if(left_expr == null)
    {
      if(DEBUG) System.out.println("  Issue in Expression Parser: Left expression was null.");
      return null;
    }
    return left_expr; // Returning the final left expression
    // Commented out because memory expressions cause weirdness with methods?
    // return new MemoryExpression(left_expr);
  }
  
	public Frunction evaluate(Container environment);
	
	public default String getDisplay(){return "Generic";};
}
