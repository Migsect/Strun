package net.samongi.frunction.expression.types;

import net.samongi.frunction.binding.MethodBinding;
import net.samongi.frunction.error.runtime.MethodNotFoundError;
import net.samongi.frunction.error.runtime.RunTimeError;
import net.samongi.frunction.error.syntax.SyntaxError;
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

  @Override public Type getType()
  {
    return Expression.Type.METHOD;
  }

  @Override public String getDisplay()
  {
    return "M<(" + right_token.getSource() + ")->[" + left_expression.getDisplay() + "]>";
  }

  @Override public Frunction evaluate(Container environment) throws SyntaxError, RunTimeError
  {
    if(environment == null) throw new NullPointerException("'environment' was null");
     
    // System.out.println("InputToken Contents: " + this.right_token.getSource());
    
    // Getting group tokens included in the input token.
    GroupToken[] group_tokens = this.right_token.getTokens();;

    // Now we will translate the group tokens into expressions
    Expression[] exprs = new Expression[group_tokens.length];
    for(int i = 0; i < group_tokens.length; i++)
    {
      group_tokens[i].evaluate();

      // This is the expression that the method represents
      Expression expr = Expression.parseTokens(group_tokens[i].getSource(), group_tokens[i].getTokens());;
      if(DEBUG) System.out.println("  M-Evaluate: GroupToken[" + i + "] source: '" + group_tokens[i].getSource() + "'");
      if(DEBUG) System.out.println("  M-Evaluate: GroupToken[" + i + "] types: '" + group_tokens[i].displayTypes() + "'");

      if(expr == null) throw new NullPointerException("Parsed Expression was null");
      
      exprs[i] = expr;
    }

    // evaluating the left expression
    // This is where we will attempt to get the method from.
    Frunction eval = left_expression.evaluate(environment);
    if(DEBUG) if(eval == null) System.out.println("  Issue in M-Evaluate: Evaluated left expression is null");

    // Evaluating all the inputs because it is needed to get the types.
    // The inputs should be evaluated based on the expression environment and not on the
    // container.
    Frunction[] evaluated_inputs = new DynamicFrunction[exprs.length];
    for(int i = 0; i < exprs.length; i++)
    {
      // These are being evaluated on the normal environment that is called the
      // expression.
      evaluated_inputs[i] = exprs[i].evaluate(environment);
      if(evaluated_inputs[i] == null) throw new NullPointerException();
    }

    // Getting the types of the evaluated expression.
    // The types will be used to get the method.
    String[] types = new String[evaluated_inputs.length];
    for(int i = 0; i < types.length; i++) types[i] = evaluated_inputs[i].getType(); // type may become null?
    // TODO see if we need to check if the types include a null string

    // Retrieving the method biniding.
    MethodBinding binding = eval.getMethod(types, evaluated_inputs);
    //System.out.println("  M-Left: " + left_expression.getDisplay());
    if(binding == null) throw new MethodNotFoundError(environment, types);

    // The left expression is the container
    // This method container will be used by a method to evaluate.
    // We need to add the inputs to this container.
    MethodContainer container = new MethodContainer(eval);
    String[] input_symbols = binding.getInputSymbols();
    // Adding all the frunction inputs.
    for(int i = 0; i < input_symbols.length; i++)
    {
      //System.out.println("  M-expr: Added sym '" + input_symbols[i] + "' to container");
      container.addSymbol(input_symbols[i], evaluated_inputs[i]);
    }

    // Getting the expression
    Expression expr = binding.getExpression(); 
    if(expr == null) throw new NullPointerException();

    // Evaluating the expression using the method container.
    return expr.evaluate(container);
  }

}
