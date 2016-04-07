package net.samongi.frunction.parse;

import java.util.HashSet;
import java.util.Set;

public class ParseLogger
{
  private static ParseLogger instance = null;

  /**
   * Returns the instance of the parse logger This will construct a new parse
   * logger if one doesn't already eist
   * 
   * @return The parse logger.
   */
  public static ParseLogger instance()
  {
    if(ParseLogger.instance == null) instance = new ParseLogger();
    return ParseLogger.instance;
  }

  private static String ALL_CHANNELS = "ALL";

  /**
   * Represents a state of logging
   * 
   * @author Alex
   *
   */
  public enum LogState
  {
    DEBUG("DEBG"), WARNING("WARN"), INFO("INFO");

    private final String tag;

    LogState(String tag)
    {
      this.tag = tag;
    }

    public String getTag()
    {
      return this.tag;
    };
  }

  // Start fields and methods

  private Set<String> channels = new HashSet<>();
  private Set<LogState> log_state = new HashSet<>();

  private boolean showAllChannels()
  {
    return this.channels.contains(ALL_CHANNELS);
  }

  private boolean hasActivatedChannel(String[] channels)
  {
    for(String c : channels)
      if(this.channels.contains(c.toUpperCase())) return true;
    return false;
  }

  private String getChannelString(String[] channels)
  {
    String channel_str = "< ";
    for(String c : channels)
    {
      if(!this.channels.contains(c.toUpperCase())) continue;
      channel_str += c.toUpperCase() + " ";
    }
    channel_str += ">";
    return channel_str;
  }

  /**
   * Logs the message to the system out
   * 
   * @param state
   *          The state of the logging
   * @param channels
   *          The channels that the message will be logged to
   * @param message
   *          The message to log
   */
  public void Log(LogState state, String[] channels, String message)
  {
    if(!this.log_state.contains(state)) return; // The log state has been
                                                // activated
    if(!this.hasActivatedChannel(channels) && !this.showAllChannels()) return; // The
                                                                               // channel
                                                                               // is
                                                                               // activated.
    String channel_str = this.getChannelString(channels);
    String state_str = "[" + state.getTag() + "]";
    String logged_message = state_str + channel_str + ":" + message;
    System.out.println(logged_message);
  }

}
