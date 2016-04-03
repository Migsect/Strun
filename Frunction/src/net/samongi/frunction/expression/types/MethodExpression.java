package net.samongi.frunction.expression.types;

import net.samongi.frunction.binding.MethodBinding;
import net.samongi.frunction.expression.exceptions.TokenException;
import net.samongi.frunction.expression.tokens.GroupToken;
import net.samongi.frunction.expression.tokens.InputToken;
import net.samongi.frunction.frunction.Container;
import net.samongi.frunction.frunction.DynamicFrunction;
import net.samongi.frunction.frunction.Frunction;
import net.samongi.frunction.frunction.MethodContainer;

public class MethodExpression implements Expression
{
  private final Expression left_expression;
  private final InputToken right_token;
  
  public MethodExpression(Expression left_expression, InputToken right_token)
  {
    this.left_expression = left_expression;
    this.right_token = right_token;
  }
  
  @Override public Frunction evaluate(Container environment)
  {
  	// Getting group tokens included in the input token.
    GroupToken[] tokens = null;
    try{tokens = this.right_token.getTokens();}
    catch (TokenException e1)
    {
      // TODO proper exception
      return null;
    }
    // Now we will translate the group tokens into expressions
    Expression[] exprs = new Expression[tokens.length];
    for(int i = 0; i < tokens.length; i++)
    {
      Expression expr = null;
      try{expr = Expression.parseTokens(tokens[i].getTokens(), environment);}
      catch (TokenException e)
      {
        // TODO add proper error
        return null;
      }
      if(expr == null) return null;
      exprs[i] = expr;
    }
    
    // evaluating the left expression
    // This is where we will attempt to get the method from.
    Frunction eval = left_expression.evaluate(environment);
    //System.out.println("Left Expression Type: " + left_expression.getClass().toGenericString());
    
    // Evaluating all the inputs because it is needed to get the types.
    Frunction[] r_evals = new DynamicFrunction[exprs.length];
    for(int i = 0; i < exprs.length; i++)
    {
      // These are being evaluated on the normal environment that is called the expression.
    	Frunction i_eval = r_evals[i].toExpression().evaluate(environment);
      if(i_eval == null) return null;
      r_evals[i] = i_eval;
    }
    
    // Getting the types of the evaluated expression.
    String[] types = new String[r_evals.length];
    for(int i = 0; i < types.length; i++) types[i] = r_evals[i].getType(); // type may become null?
    // TODO see if we need to check if the types include a null string
    
    MethodBinding binding = eval.getMethod(types, r_evals);
    if(binding == null)
    {
      // TODO throw a proper exception.
      return null;
    }
    
    // The left expression is the container
    //   This method container will be used by a method to evaluate.
    //   We need to add the inputs to this container.
    MethodContainer container = new MethodContainer(eval);
    String[] input_symbols = binding.getInputSymbols();
    // Adding all the frunction inputs.
    for(int i = 0; i < input_symbols.length; i++) container.addSymbol(input_symbols[i], r_evals[i]);
    
    // Getting the expression
    Expression expr = null;
    try { expr = binding.getExpression();}
    catch (TokenException e){}
    if(expr == null) return null;
    
    // Evaluating the expression
    return expr.evaluate(container);
  }
  
}
