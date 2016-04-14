package net.samongi.frunction.frunction;

import java.util.List;

import net.samongi.frunction.binding.MethodBinding;
import net.samongi.frunction.binding.SymbolBinding;
import net.samongi.frunction.exceptions.parsing.ExpressionException;
import net.samongi.frunction.exceptions.parsing.ParsingException;
import net.samongi.frunction.exceptions.parsing.TokenException;
import net.samongi.frunction.exceptions.runtime.RunTimeException;
import net.samongi.frunction.parse.ParseUtil;

public interface Container
{
  /** Gets the method binding for the corresponding types
   * 
   * @param types THe types to get a method for
   * @return A MethodBinding, otherwise null 
   * @throws RunTimeException 
   * @throws TokenException 
   * @throws ExpressionException */
  public MethodBinding getMethod(String[] types, Frunction[] inputs) throws ParsingException, RunTimeException;

  /** Adds the binding to this container
   * 
   * @param types The types of arguments the binding takes.
   * @param binding The binding to add */
  public void addMethod(MethodBinding binding) throws RunTimeException;

  /** Returns a list of lists of methodbindings.
   * 
   * @return A list of all the methods in the frunction */
  public List<MethodBinding> getMethods();

  /** Gets the symbol binding for the corresponding symbol
   * 
   * @param symbol The symbol to retrieve
   * @return A SymbolBinding
   * @throws ExpressionException 
   * @throws TokenException */
  public default SymbolBinding getSymbol(String symbol) throws ParsingException, RunTimeException
  {
    return this.getSymbol(symbol, this.getEnvironment());
  }
  
  /** Gets the symbol binding for the corresponding symbol
   * 
   * @param symbol The symbol to retrieve
   * @param prozy This will treat getting the symbol as if the proxy was the container's environment
   * @return A SymbolBinding, otherwise null 
   * @throws ExpressionException 
   * @throws TokenException */
  public SymbolBinding getSymbol(String symbol, Container proxy) throws ParsingException, RunTimeException;

  /** Adds the binding to this container
   * 
   * @param symbol The symbol's string to add
   * @param binding The binding to add 
   * @throws ParsingException */
  public void addSymbol(SymbolBinding binding) throws RunTimeException, ParsingException;

  /** Returns all the symbols stored in the container This returns all of the symbols sorted.
   * 
   * @return All the symbols. */
  public List<SymbolBinding> getSymbols();

  /** Returns the environment that the container is contained within. This may be null if the container is a top level
   * container
   * 
   * @return The container */
  public Container getEnvironment();

  /** Used for debugging to display the frunction hierarchy
   * 
   * @param spacing */
  public default void displayHierarchy(int spacing)
  {
    String space = ParseUtil.spacing(spacing);
    List<MethodBinding> met_bindings = this.getMethods();
    for(MethodBinding b : met_bindings)
    {
      System.out.println(space + b.toDisplay());
    }
    if(met_bindings.size() == 0) System.out.println(space + "No Method Bindings");

    List<SymbolBinding> sym_bindings = this.getSymbols();
    for(SymbolBinding b : sym_bindings)
    {
      Frunction f = null;
      try
      {
        f = b.get(this);
      }
      catch(RunTimeException e)
      {
        System.out.println(space + b.getKey() + " : RunTimeException");
        e.printStackTrace();
        continue;
      }
      catch(ParsingException e)
      {
        System.out.println(space + b.getKey() + " : ParsingException");
        e.printStackTrace();
        continue;
      }
      if(f == null)
      {
        System.out.println(space + b.getKey() + " : null frunction");
        continue;
      }
      System.out.println(space + b.getKey() + " : '" + f.getType() + "' <" + f.getSource() + ">");
      f.displayHierarchy(spacing + 2);
    }
    if(sym_bindings.size() == 0) System.out.println(space + "No Symbol Bindings");
  }
}
