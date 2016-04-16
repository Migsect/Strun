package net.samongi.frunction.expression.types;

import net.samongi.frunction.error.runtime.RunTimeError;
import net.samongi.frunction.error.syntax.ExpressionError;
import net.samongi.frunction.error.syntax.MissingExpectedTokensError;
import net.samongi.frunction.error.syntax.MissingLeftExpressionError;
import net.samongi.frunction.error.syntax.SyntaxError;
import net.samongi.frunction.error.syntax.TokenError;
import net.samongi.frunction.error.syntax.UnexpectedExpressionError;
import net.samongi.frunction.error.syntax.UnexpectedTokenError;
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

  public enum Type
  {
    GENERIC,
    CONTAINER_ACCESSOR,
    FRUNCTION_ACCESSOR,
    METHOD,
    FRUNCTION,
    NATIVE,
    FRUNCTION_CONVERSION;
  }

  /** Parses a string to make an expression this uses Token.parseTokens and Expression.parseExpression to do so As such
   * this is a utility method and nothing much else.
   * 
   * @param source
   * @param environment
   * @return
   * @throws TokenException
   * @throws ExpressionException */
  public static Expression parseString(String source) throws TokenError, ExpressionError
  {
    if(source == null) throw new NullPointerException("'source' was null");

    source = source.trim();
    GroupToken token = Token.parseTokens(source);
    return Expression.parseTokens(source, token.getTokens());
  }

  /** Parses tokens to make an expression object
   * 
   * @param source the source used to make the tokens (used for errors)
   * @param tokens The tokens to parse
   * @param environment The environment the tokens are being parsed in
   * @return An expression parsed from the tokens
   * @throws TokenError */
  public static Expression parseTokens(String source, Token[] tokens) throws TokenError, ExpressionError
  {
    if(tokens == null) throw new NullPointerException("'tokens' was null");
    
    // Note that the left expression is null.
    Expression left_expr = null;
    int i = 0;
    while(i < tokens.length)
    {

      Token t = tokens[i];
      // Case 1: We have a token without an accessor before it.
      // This means that it is accessing the current environment
      // This only occurs if we start on a symbol
      if(t.getType().equals(Token.Type.SYMBOL))
      {
        if(i != 0) throw new UnexpectedExpressionError(Expression.Type.CONTAINER_ACCESSOR, source);
        
        ContainerAccessorExpression expr = new ContainerAccessorExpression((SymbolToken) t);
        left_expr = expr; // Setting it to be the left expression
        i += 1;
      }
      // Case 2: We come across an accessor token
      else if(t.getType().equals(Token.Type.ACCESSOR))
      {
        if(left_expr == null) throw new MissingLeftExpressionError(Expression.Type.FRUNCTION_ACCESSOR, source);
        // Checking to see if there is a next index
        if(tokens.length < i + 1) throw new MissingExpectedTokensError(Expression.Type.FRUNCTION_ACCESSOR, source);
        if(!tokens[i + 1].getType().equals(Token.Type.SYMBOL)) throw new MissingExpectedTokensError(Expression.Type.FRUNCTION_ACCESSOR, source);

        Token r_t = tokens[i + 1]; // Getting the next token
        FrunctionAccessorExpression expr = new FrunctionAccessorExpression(left_expr, (SymbolToken) r_t);
        left_expr = expr; // Setting it to be the left expression
        i += 2;
      }
      // Case 3: We come across a frunction token
      // If we do, then the frunction token should be first in this scenario
      // This is a very similar case to the symbol token
      else if(t.getType().equals(Token.Type.FRUNCTION))
      {
        if(i != 0) new UnexpectedExpressionError(Expression.Type.FRUNCTION, source);
        FrunctionExpression expr = new FrunctionExpression((FrunctionToken) t);
        left_expr = expr; // setting it to be the left expression
        i += 1;
      }
      // Case 4: We come across a method call.
      // This requires a symbol to be before it.
      else if(t.getType().equals(Token.Type.INPUT))
      {
        if(left_expr == null) throw new MissingLeftExpressionError(Expression.Type.METHOD, source);
        MethodExpression expr = new MethodExpression(left_expr, (InputToken) t);
        left_expr = expr; // setting it to be the left expression
        i += 1;
      }
      // Case 5: We come across a GroupToken (which shouldn't exist yet)
      // This should be the first token, just like frunctions and first symbols
      else if(t.getType().equals(Token.Type.GROUP))
      {
        System.out.println("  For some reason there was a group token?!?!");
        if(i != 0) throw new ExpressionError(Expression.Type.GENERIC, source);
        GroupToken g_t = (GroupToken) t;
        Expression expr = Expression.parseTokens(source, g_t.getTokens());
        left_expr = expr; // setting it to be the left expression
        i += 1;
      }
      else throw new UnexpectedTokenError(source);
      
    }
    if(left_expr == null) throw new ExpressionError(Expression.Type.GENERIC, source);
    
    return left_expr; // Returning the final left expression
  }

  public Frunction evaluate(Container environment) throws SyntaxError, RunTimeError;

  public Expression.Type getType();

  public default String getDisplay()
  {
    return "Generic";
  };
}
