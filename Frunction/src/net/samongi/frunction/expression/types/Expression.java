package net.samongi.frunction.expression.types;

import net.samongi.frunction.expression.exceptions.TokenException;
import net.samongi.frunction.expression.tokens.FrunctionToken;
import net.samongi.frunction.expression.tokens.GroupToken;
import net.samongi.frunction.expression.tokens.InputToken;
import net.samongi.frunction.expression.tokens.SymbolToken;
import net.samongi.frunction.expression.tokens.Token;
import net.samongi.frunction.frunction.Container;
import net.samongi.frunction.frunction.DynamicFrunction;
import net.samongi.frunction.frunction.Frunction;

public interface Expression
{
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
    GroupToken token = Token.parseTokens(source);
    return Expression.parseTokens(token.getTokens(), environment);
  }
  /**Parses tokens to make an expression object
   * 
   * @param tokens
   * @param environment
   * @return
   * @throws TokenException
   */
  public static Expression parseTokens(Token[] tokens, Container environment) throws TokenException
  {
    Expression left_expr = null;
    int i = 0;
    while(i < tokens.length)
    {
      Token t = tokens[i];
      // System.out.println("Token is of type: " + t.getType().toString() + " with source '" + t.getSource() + "'");
      // Case 1: We have a token without an accessor before it.
      //   This means that it is accessing the current environment
      //   This only occurs if we start on a symbol
      if(t.getType().equals(Token.Type.SYMBOL))
      {
        if(i != 0) return null; // must be first
        if(!(environment instanceof DynamicFrunction)) return null; // TODO make this a valid exception
        AccessorExpression expr = new AccessorExpression(((Frunction) environment).toExpression(), (SymbolToken) t);
        left_expr = expr; // Setting it to be the left expression
        i += 1;
      }
      // Case 2: We come accross and accessor token
      else if(t.getType().equals(Token.Type.ACCESSOR))
      {
        if(left_expr == null) return null; // We need a left hand expression
        if(tokens.length >= i) return null; // We need a right hand symbol.
        if(!tokens[i + 1].getType().equals(Token.Type.SYMBOL)) return null;
        
        Token r_t = tokens[i + 1];
        AccessorExpression expr = new AccessorExpression(left_expr, (SymbolToken) r_t);
        left_expr = expr; // Setting it to be the left expression
        i += 2;
      }
      // Case 3: We come across a frunction token
      //   If we do, then the frunction token should be first in this scenario
      //   This is a very similar case to the symbol token
      else if(t.getType().equals(Token.Type.FRUNCTION))
      {
        if(i != 0) return null; // must be first
        FrunctionExpression expr = new FrunctionExpression((FrunctionToken) t);
        left_expr = expr;
        i += 1;
      }
      // Case 4: We come across a method call.
      //   This requires a symbol to be before it.
      else if(t.getType().equals(Token.Type.INPUT))
      {
        if(left_expr == null) return null; // We need a left hand expression
        MethodExpression expr = new MethodExpression(left_expr, (InputToken) t);
        left_expr = expr;
        i += 1;
      }
      // Case 5: We come across a GroupToken (which shouldn't exist yet)
      //   This should be the first token, just like frunctions and first symbols
      else if(t.getType().equals(Token.Type.GROUP))
      {
        if(i != 0) return null; // must be first
        GroupToken g_t = (GroupToken) t;
        Expression expr = Expression.parseTokens(g_t.getTokens(), environment);
        left_expr = expr;
        i += 1;
      }
      else
      {
        // TODO unknown token
        return null;
      }
      // System.out.println(" - Left expression is now : " + left_expr.getClass().toGenericString());
    }
    return left_expr;
  }
  
	public Frunction evaluate(Container environment);
}
