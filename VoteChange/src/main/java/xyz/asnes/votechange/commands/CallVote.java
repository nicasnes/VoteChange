package xyz.asnes.votechange.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import xyz.asnes.votechange.CommandData;
import xyz.asnes.votechange.VoteChange;

import java.util.ArrayList;
import java.util.List;


public class CallVote implements CommandExecutor {

  private VoteChange plugin;
  public static int numVotes;
  public static int numYes;
  public static boolean isVote;
  public static ArrayList<String> voters;

  public CallVote(VoteChange plugin) {
    this.plugin = plugin;
    resetVote();
  }

  /**
   * This method conducts when a player enters the CallVote command. It should initialize a vote and tally
   * the total votes and the number of votes "yes". If a majority of 2/3 or greater votes yes, then the vote is
   * approved the the player is granted momentary permissions to execute the requested command.
   * <p>
   * Current implementation does not permit for console to call a vote. Additionally, only one vote can
   * occur at a time and players may request a vote for any String. If the String is not a command,
   * the vote will still occur and if it is approved, the player will execute a nonexistent command.
   *
   * @param sender  the user that executes the command
   * @param command the entirety of the command being executed
   * @param label   the exact command executed by the user, i.e callvote or cv
   * @param args    the arguments to the command; the command to be executed upon an approved vote
   *                i.e "time set day"
   * @return whether or not the command entered was valid.
   */
  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    FileConfiguration config = plugin.getConfig();
    if (args.length == 0) {
      sender.sendMessage(ChatColor.RED + config.getString("empty-vote-message"));
      return true;
    } else if (isVote && sender instanceof Player) {
      if (plugin.voteProposers.contains(sender.getName())) {
        sender.sendMessage(ChatColor.RED + config.getString("one-queued-message"));
        return true;
      }
      sender.sendMessage(ChatColor.RED + config.getString("only-one-vote-message"));
      plugin.dataQueue.add(new CommandData(sender, command, label, args));
      plugin.voteProposers.add(sender.getName());
      return true;
    } else if (sender instanceof Player) {
      if (isCommandBlacklisted(args)) {
        sender.sendMessage(config.getString("command-blacklisted-message"));
        return true;
      }
      if (!isThresholdMet()) {
        if (isCommandWhitelisted(args)) {
          return callVote(sender, args);
        }
        sender.sendMessage(config.getString("cap-not-reached-message"));
        return true;
      }
      return callVote(sender, args);
    } else {
      sender.sendMessage(config.getString("players-only-message"));
    }
    return true;
  }

  public boolean voteApproved() {
    if (numVotes > 0 && numYes >= (numVotes * (plugin.getConfig().getDouble("required-majority")))) {
      return true;
    }
    return false;
  }

  public static void vote(boolean voteYes) {
    if (voteYes) {
      numYes++;
    }
    numVotes++;
  }

  public static void resetVote() {
    isVote = false;
    numYes = 0;
    numVotes = 0;
    voters = new ArrayList<String>();
  }

  /**
   * Converts an array of Strings up through length into a single String with spaces between array elements.
   * @param args an array of Strings
   * @param length the length in the array to construct the String with
   * @return a String constructed from args with spaces between array elements
   */
  public String constructSubCommand(String[] args, int length) {
    if (length > args.length) {
      length = args.length;
    }
    String subCom = "";
    for (int i = 0; i < length; i++) {
      subCom += args[i] + " ";
    }
    return subCom.substring(0, subCom.length()-1);
  }

  /**
   * A simple function to convert seconds to ticks, based on the fact that Minecraft runs at a fixed rate
   * of 20 ticks per second.
   *
   * @param seconds a long of seconds
   * @return a long of ticks
   */
  private long secondsToTicks(long seconds) {
    return 20 * seconds;
  }

  private boolean isThresholdMet() {
    return plugin.getServer().getOnlinePlayers().size() >= plugin.getConfig().getInt("minimum-players");
  }

  /**
   * Checks if a command is whitelisted in the configuration file.
   * @param args a String[] of the arguments to the callvote command
   * @return true if the command is whitelisted, false otherwise
   */
  private boolean isCommandWhitelisted(String[] args) {
    FileConfiguration config = plugin.getConfig();
    List<String> whitelist = config.getStringList("whitelisted-commands");
    for (String com : whitelist) {
      int numWordsInCom = countWords(com);
      String checkCom = constructSubCommand(args, numWordsInCom);
      if (com.equals(checkCom)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Checks if a command is blacklisted in the configuration file.
   * @param args a String[] of the arguments to the callvote command
   * @return true if the command is blacklisted, false otherwise
   */
  private boolean isCommandBlacklisted(String[] args) {
    FileConfiguration config = plugin.getConfig();
    List<String> blacklist = config.getStringList("blacklisted-commands");
    for (String com : blacklist) {
      int numWordsInCom = countWords(com);
      String checkCom = constructSubCommand(args, numWordsInCom);
      if (com.equals(checkCom)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Executes the vote, keeping track of the number of votes and the number of votes for yes. Calls voteApproved() to
   * check after the vote ends. If the vote is successful, allow the player to perform the command. Regardless of
   * vote outcome, reset the vote and clear the queue.
   * @param sender the user who sent the callVote command
   * @param args the String array of arguments to the callvote command
   * @return true, the vote was executed correctly.
   */
  private boolean callVote(CommandSender sender, String[] args) {
    isVote = true;
    Player p = (Player) sender;
    String subCom = constructSubCommand(args, args.length);
    Bukkit.broadcastMessage(p.getDisplayName() + " has called a VOTE! \nThey want to execute command " +
        ChatColor.BOLD + ChatColor.GOLD + subCom + ChatColor.WHITE + "\nType " + ChatColor.GREEN + "/vote yes" + ChatColor.WHITE + " or " +
        ChatColor.RED + "/vote no");
    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin,
        () -> {
          if (voteApproved()) {
            Bukkit.broadcastMessage(p.getDisplayName() + "'s vote was" + ChatColor.GREEN + " APPROVED.");
            p.setOp(true);
            p.performCommand(subCom);
            p.setOp(false);
          } else {
            Bukkit.broadcastMessage(p.getDisplayName() + "'s vote was" + ChatColor.RED + " DENIED.");
          }
          resetVote();
          if (!plugin.dataQueue.isEmpty()) {
            CommandData topData = plugin.dataQueue.poll();
            onCommand(topData.sender, topData.command, topData.label, topData.args);
            plugin.voteProposers.remove(topData.sender.getName());
          }
        }, secondsToTicks(plugin.getConfig().getLong("vote-duration-seconds")));
    return true;
  }

  private static int countWords(String input) {
    if (input == null || input.isEmpty()) {
      return 0;
    }
    String[] words = input.split("\\s+");
    return words.length;
  }
}
