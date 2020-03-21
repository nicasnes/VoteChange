package xyz.asnes.votechange.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import xyz.asnes.votechange.CommandData;
import xyz.asnes.votechange.VoteChange;

public class ViewVotingQueue implements CommandExecutor {

  private VoteChange plugin;

  public ViewVotingQueue(VoteChange plugin) {
    this.plugin = plugin;
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (plugin.dataQueue.isEmpty()) {
      sender.sendMessage(ChatColor.AQUA + "The voting queue is empty.");
    } else {
      for (CommandData curData : plugin.dataQueue) {
        sender.sendMessage(ChatColor.GOLD + curData.sender.getName() + ChatColor.WHITE + " will vote to run command " +
            constructSubCommand(curData.args));
      }
    }
    return true;
  }

  public String constructSubCommand(String[] args) {
    String subCom = "";
    for (int i = 0; i < args.length; i++) {
      subCom += args[i] + " ";
    }
    return subCom;
  }
}
