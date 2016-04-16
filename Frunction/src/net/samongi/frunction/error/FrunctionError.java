package net.samongi.frunction.error;

import java.io.InputStream;
import java.lang.ClassLoader;

import net.samongi.frunction.file.IniFileReader;


public class FrunctionError extends Exception
{
  private static IniFileReader error_dictionary;
  private static boolean dictionary_load_success;
  private static boolean dictionary_load_attempted;
  
  private static final long serialVersionUID = 1125560200927435242L;

  private String message;
  
  public FrunctionError() {
    super();
    if (!dictionary_load_attempted) loadIniFile();
  }
  
  public FrunctionError(int ini_key) {
    super();
    if (!dictionary_load_attempted) loadIniFile();
    
    this.message = getIniValue(ini_key);
  }
  
  public FrunctionError(int ini_key, Object... objects) {
    super();
    if (!dictionary_load_attempted) loadIniFile();
    
    this.message = getIniValue(ini_key);
    if (this.message != null) {
      try {
        this.message = String.format(this.message, objects);
      }
      catch (Exception ex) {
        this.message = "An error has occured.";
      }
    }
  }

  public FrunctionError(String section, int ini_key, Object... objects) {
    super();
    if (!dictionary_load_attempted) loadIniFile();
    
    this.message = getIniValue(section, ini_key);
    if (this.message != null) {
      try {
        this.message = String.format(this.message, objects);
      }
      catch (Exception ex) {
        this.message = "An error has occured.";
      }
    }
  }

  
  @Override public String getMessage() {
    return this.message;
  }

  public String getFormattedMessage() {
    return "[Error]: " + this.getMessage();
  }
    
  
  private String getIniValue(int key) {
    return getIniValue(this.getClass().getSimpleName(), key);
  }
  
  
  private static void loadIniFile() {
    if (!dictionary_load_attempted) {
      InputStream error_file = ClassLoader.getSystemResourceAsStream("net/samongi/frunction/resources/errors-EN.ini");
  
      if (error_file != null) {
        try {
          error_dictionary = new IniFileReader(error_file);
          dictionary_load_success = true;
        }
        catch (Exception ex) {
          dictionary_load_success = false;
        }
      }
      dictionary_load_attempted = true;
    }
  }

  
  protected static String getIniValue(String section, int key) {
    return getIniValue(section, key, null);
  }
  
  protected static String getIniValue(String section, int key, String default_value) {
    if (dictionary_load_success) {
      return error_dictionary.getString(section, String.valueOf(key), default_value);
    }
    
    return default_value;
  }
}
