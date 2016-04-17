package net.samongi.frunction;

import java.io.File;

import net.samongi.frunction.error.runtime.MethodNotFoundError;
import net.samongi.frunction.error.runtime.RunTimeError;
import net.samongi.frunction.error.runtime.SymbolNotFoundError;
import net.samongi.frunction.error.syntax.ExpressionError;
import net.samongi.frunction.error.syntax.SyntaxError;
import net.samongi.frunction.file.FileUtil;
import net.samongi.frunction.frunction.DynamicFrunction;
import net.samongi.frunction.frunction.library.ArgumentFrunction;
import net.samongi.frunction.frunction.literal.BooleanFrunction;
import net.samongi.frunction.frunction.literal.EmptyFrunction;
import net.samongi.frunction.frunction.literal.IntegerFrunction;
import net.samongi.frunction.frunction.literal.NativeFrunction;
import net.samongi.frunction.frunction.literal.RealFrunction;
import net.samongi.frunction.frunction.literal.StringFrunction;
import net.samongi.frunction.parse.Commenting;
import net.samongi.frunction.parse.ParseUtil;

public class Main
{

  public static void main(String[] args)
  { 
    
    if(args.length < 1) return;
    String file_loc = args[0];
    
    File file = new File(file_loc);
    String text_body = FileUtil.readFile(file); // Reading in the file

    text_body = Commenting.removeComments(text_body); // Removing comments
    text_body = ParseUtil.removeNextLines(text_body); // Removing nextlines
    text_body = ParseUtil.squeeze(text_body); // Squeezing white space

    // Definition of the types
    try
    {
      EmptyFrunction.getTypeDefiner().define();
      NativeFrunction.getTypeDefiner().define();
      BooleanFrunction.getTypeDefiner().define();
      IntegerFrunction.getTypeDefiner().define();
      RealFrunction.getTypeDefiner().define();
      StringFrunction.getTypeDefiner().define();
    }
    catch(SyntaxError | RunTimeError e)
    {
      e.printStackTrace();
    }
    
    // Creating the main_frunction
    // It will not evaluate yet
    DynamicFrunction main_frunction = new DynamicFrunction(null, text_body);
    main_frunction.initialize();
    // Adding the libraries to the main frunction
    try
    {
      main_frunction.addSymbol(ArgumentFrunction.getConstructor(args).asBinding("Args", main_frunction));
    }
    catch(SyntaxError | RunTimeError e)
    {
      e.printStackTrace();
    }
    
    try
    {
      main_frunction.evaluate();
    }
    catch(SyntaxError | RunTimeError e)
    {
      e.printStackTrace();
      if(e instanceof ExpressionError)
      {
        System.out.println("Source : '" + ((ExpressionError) e).getSource() + "'");
      }
      if(e instanceof SymbolNotFoundError)
      {
        SymbolNotFoundError s_e = (SymbolNotFoundError) e;
        System.out.println("Symbol not found : '" + s_e.getSymbol() + "'");
        s_e.getContainer().displayHierarchy(2);
      }
      if(e instanceof MethodNotFoundError)
      {
        MethodNotFoundError m_e = (MethodNotFoundError) e;
        System.out.println("Method not found of types : '" + ParseUtil.concatStringArray(m_e.getTypes()) + "'");
        m_e.getContainer().displayHierarchy(2);
      }
    }
    
  }

}
