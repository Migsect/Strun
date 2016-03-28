package net.samongi.frunction.frunction;

import net.samongi.frunction.expression.types.Expression;

public interface Frunction extends Container, Expression
{
	public void evaluate();
	public boolean isEvaluated();
	public void setType(String type);
	public String getType();
}
