package xyz.asnes.votechange;

import org.bukkit.command.Command;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.asnes.votechange.commands.CallVote;
import xyz.asnes.votechange.commands.ClearVotingQueue;
import xyz.asnes.votechange.commands.ViewVotingQueue;
import xyz.asnes.votechange.commands.Vote;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public final class VoteChange extends JavaPlugin implements Listener {

  public Queue<CommandData> dataQueue;
  public ArrayList<String> voteProposers;

  @Override
  public void onEnable() {
    // Plugin startup logic
    getConfig().options().copyDefaults();
    saveDefaultConfig();
    System.out.println("VoteChange enabled");
    dataQueue = new LinkedList<>();
    voteProposers = new ArrayList<>();
    getCommand("callvote").setExecutor(new CallVote(this));
    getCommand("vote").setExecutor(new Vote());
    getCommand("clearqueue").setExecutor(new ClearVotingQueue(this));
    getCommand("viewqueue").setExecutor(new ViewVotingQueue(this));

  }

  @Override
  public void onDisable() {
    // Plugin shutdown logic
    System.out.println("VoteChange disabled");

  }

}
