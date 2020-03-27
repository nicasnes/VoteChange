package xyz.asnes.votechange.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import xyz.asnes.votechange.VoteChange;

public class ClearVotingQueue implements CommandExecutor {

  private VoteChange plugin;

  public ClearVotingQueue(VoteChange plugin) {
    this.plugin = plugin;
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (plugin.dataQueue.isEmpty()) {
      Bukkit.broadcastMessage(ChatColor.RED + plugin.getConfig().getString("queue-empty-message"));
    } else {
      plugin.dataQueue.clear();
      plugin.voteProposers.clear();
      Bukkit.broadcastMessage(ChatColor.RED + plugin.getConfig().getString("queue-cleared-message"));
    }
    return true;
  }
}
