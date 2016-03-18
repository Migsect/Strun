package net.samongi.frunction.frunction;

public interface Frunction extends Container
{
	public void evaluate();
	public boolean isEvaluated();
	public void setType(String type);
	public String getType();
}
