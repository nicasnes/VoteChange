package xyz.asnes.votechange;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.asnes.votechange.commands.CallVote;
import xyz.asnes.votechange.commands.Vote;

public final class VoteChange extends JavaPlugin implements Listener {

  @Override
  public void onEnable() {
    // Plugin startup logic
    System.out.println("VoteChange enbaled");
    getCommand("callvote").setExecutor(new CallVote(this));
    getCommand("vote").setExecutor(new Vote());
  }

  @Override
  public void onDisable() {
    // Plugin shutdown logic
    System.out.println("VoteChange disabled");

  }

}
