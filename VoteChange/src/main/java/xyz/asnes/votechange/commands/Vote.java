package xyz.asnes.votechange.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Vote implements CommandExecutor {

  /**
   * This method conducts when a player executes the vote command. They are permitted to vote
   * "yes" or "no", where only the first string entered following vote is considered. Players may not vote
   * more than once. Non-players (console) are not permitted to vote. Players may not vote when there is no
   * vote occurring.
   * @param sender the user that executes the command
   * @param command the entirety of the command being executed
   * @param label the exact command executed by the user, i.e vote or v
   * @param args the arguments to the command, i.e "yes" or "no"
   * @return whether or not the command entered was valid.
   */
  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (CallVote.voters.contains(sender.getName())) {
      sender.sendMessage(ChatColor.RED + "You can only vote once.");
      return true;
    }
    if (!CallVote.isVote) {
      sender.sendMessage(ChatColor.RED + "There is no vote happening currently.");
      return true;
    }
    if (sender instanceof Player) {
      String vote = args[0];
      switch(vote) {
        case "yes":
          CallVote.voters.add(sender.getName());
          CallVote.vote(true);
          sender.sendMessage(ChatColor.AQUA + "You voted " + ChatColor.GREEN + "YES");
          return true;
        case "no":
          CallVote.voters.add(sender.getName());
          CallVote.vote(false);
          sender.sendMessage(ChatColor.AQUA + "You voted " + ChatColor.RED + "NO");
          return true;
        default:
          sender.sendMessage(ChatColor.RED + "You can only vote yes or no.");
          return true;
      }
    } else {
      sender.sendMessage("You must be a player to vote.");
    }
    return false;
  }
}
