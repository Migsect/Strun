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
  private static final boolean DEBUG = false;

  private final Expression left_expression;
  private final InputToken right_token;

  public MethodExpression(Expression left_expression, InputToken right_token)
  {
    this.left_expression = left_expression;
    this.right_token = right_token;
  }

  @Override public String getDisplay()
  {
    return "M<(" + right_token.getSource() + ")->["
        + left_expression.getDisplay() + "]>";
  }

  @Override public Frunction evaluate(Container environment)
  {
    // System.out.println("  Expr: Evaluating a MethodExpression");

    // Getting group tokens included in the input token.
    GroupToken[] tokens = null;
    try
    {
      tokens = this.right_token.getTokens();
    }
    catch(TokenException e1)
    {
      // TODO proper exception
      return null;
    }
    // Now we will translate the group tokens into expressions
    Expression[] exprs = new Expression[tokens.length];
    for(int i = 0; i < tokens.length; i++)
    {
      // We will evaluate the group tokens
      try
      {
        tokens[i].evaluate();
      }
      catch(TokenException e1)
      {
        e1.printStackTrace();
      }

      Expression expr = null;
      if(DEBUG)
        System.out.println("  M-Evaluate: Token[" + i + "] source: '"
            + tokens[i].getSource() + "'");
      if(DEBUG)
        System.out.println("  M-Evaluate: Token[" + i + "] types: '"
            + tokens[i].displayTypes() + "'");
      try
      {
        expr = Expression.parseTokens(tokens[i].getTokens(), environment);
      }
      catch(TokenException e)
      {
        e.printError();
        return null;
      }
      if(expr == null)
      {
        if(DEBUG)
          System.out.println("  Issue in M-Evaluate: Expression '" + i
              + "' from tokens was null");
      }
      exprs[i] = expr;
    }

    // evaluating the left expression
    // This is where we will attempt to get the method from.
    Frunction eval = left_expression.evaluate(environment);
    if(DEBUG)
      if(eval == null)
        System.out
            .println("  Issue in M-Evaluate: Evaluated left expression is null");
    // System.out.println("Left Expression Type: " +
    // left_expression.getClass().toGenericString());

    // Evaluating all the inputs because it is needed to get the types.
    Frunction[] r_evals = new DynamicFrunction[exprs.length];
    for(int i = 0; i < exprs.length; i++)
    {
      // These are being evaluated on the normal environment that is called the
      // expression.
      Frunction i_eval = exprs[i].evaluate(environment);
      if(i_eval == null)
      {
        if(DEBUG)
          System.out.println("  Issue in M-Evaluate: Evaluated input '" + i
              + "' is null");
        return null;
      }
      r_evals[i] = i_eval;
    }

    // Getting the types of the evaluated expression.
    String[] types = new String[r_evals.length];
    for(int i = 0; i < types.length; i++)
      types[i] = r_evals[i].getType(); // type may become null?
    // TODO see if we need to check if the types include a null string

    MethodBinding binding = eval.getMethod(types, r_evals);
    if(DEBUG) System.out.println("  >> Left: " + left_expression.getDisplay());
    if(binding == null)
    {
      if(DEBUG)
        System.out
            .println("  Issue in M-Evaluate: Methodbinding returned null!");
      // TODO throw a proper exception.
      return null;
    }

    // The left expression is the container
    // This method container will be used by a method to evaluate.
    // We need to add the inputs to this container.
    MethodContainer container = new MethodContainer(eval);
    String[] input_symbols = binding.getInputSymbols();
    // Adding all the frunction inputs.
    for(int i = 0; i < input_symbols.length; i++)
    {
    	System.out.println("  M-expr: Added sym '" + input_symbols[i] + "' to container");
      container.addSymbol(input_symbols[i], r_evals[i]);
    }

    // Getting the expression
    Expression expr = null;
    try
    {
      expr = binding.getExpression();
    }
    catch(TokenException e)
    {}
    if(expr == null)
    {
      if(DEBUG)
        System.out
            .println("  Issue in M-Evaluate: Binding returned null expression!");
      if(DEBUG)
        System.out.println("    Left Expression: "
            + this.left_expression.getDisplay());
      return null;
    }

    if(DEBUG) System.out.println(expr.getClass().toGenericString());

    // Evaluating the expression using the method container.
    return expr.evaluate(container);
  }

}
