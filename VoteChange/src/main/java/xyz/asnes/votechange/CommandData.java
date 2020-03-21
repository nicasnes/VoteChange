package xyz.asnes.votechange;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class CommandData {
  public String[] args;
  public Command command;
  public String label;
  public CommandSender sender;

  public CommandData(CommandSender sender, Command command, String label, String[] args) {
    this.sender = sender;
    this.command = command;
    this.label = label;
    this.args = args;
  }

}
