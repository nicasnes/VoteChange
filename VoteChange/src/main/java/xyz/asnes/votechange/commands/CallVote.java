package xyz.asnes.votechange.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.asnes.votechange.CommandData;
import xyz.asnes.votechange.VoteChange;
import java.util.ArrayList;


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
   *
   * Current implementation does not permit for console to call a vote. Additionally, only one vote can
   * occur at a time and players may request a vote for any String. If the String is not a command,
   * the vote will still occur and if it is approved, the player will execute a nonexistent command.
   *
   * @param sender the user that executes the command
   * @param command the entirety of the command being executed
   * @param label the exact command executed by the user, i.e callvote or cv
   * @param args the arguments to the command; the command to be executed upon an approved vote
   *             i.e "time set day"
   * @return whether or not the command entered was valid.
   */
  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (args.length == 0) {
      sender.sendMessage(ChatColor.RED + "You cannot call a vote on nothing.");
      return true;
    } else if (isVote && sender instanceof Player) {
        if (plugin.voteProposers.contains(sender.getName())) {
          sender.sendMessage(ChatColor.RED + "You cannot queue two votes. Please try again after your vote is complete.");
          return true;
        }
        sender.sendMessage(ChatColor.RED + "There is already a vote in progress. Your vote has been added to the queue.");
        plugin.dataQueue.add(new CommandData(sender, command, label, args));
        plugin.voteProposers.add(sender.getName());
        return true;
    } else if (sender instanceof Player) {
        isVote = true;
        Player p = (Player) sender;
        String subCom = constructSubCommand(args);
        Bukkit.broadcastMessage(p.getDisplayName() + " has called a VOTE! \nThey want to execute command " +
            ChatColor.BOLD + ChatColor.GOLD + subCom + ChatColor.WHITE + "\nType " + ChatColor.GREEN + "/vote yes" + ChatColor.WHITE + " or " +
            ChatColor.RED + "/vote no");
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
          public void run() {
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
          }
        }, 450L);
    } else {
      sender.sendMessage("You must be a player to call a vote.");
    }
    return true;
  }

  public boolean voteApproved() {
    if ((double) numYes >= (numVotes * (2.0 / 3.0))) {
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

  public String constructSubCommand(String[] args) {
    String subCom = "";
    for (int i = 0; i < args.length; i++) {
      subCom += args[i] + " ";
    }
    return subCom;
  }

}
