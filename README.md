# VoteChange
A Minecraft plugin allowing for majority-based voting to execute commands.

# Installation
Download the .jar file and place it in the plugins folder of your server. 

# Configuration
Server administrators have a degree of freedom with the plugin's implementation through the config.yml file. After installing the plugin and launching the server once, a config.yml file will be generated. To edit a field, delete the pregenerated data
and  enter the desired value. Specific configuration options are listed below.

**vote-duration-seconds**
The default vote duration is 20 seconds. This value should be a positive, real number.

**required-majority**
The default majority requirement is 0.666. This value is inclusive, meaning that an exact 0.666 majority will yield a successful vote. This value should be a decimal between 0.0 and 1.0. 

**minimum-players**
The default minimum player count is 3. When the player count of the server is less than this number, only whitelisted commands will be allowed.

**empty-vote-message**
This message is sent to the user when they attempt to use the callvote command with no arguments.

**one-queued-message**
This message is sent to the user when they attempt to add a second vote to the queue.

**only-one-vote-message**
This message is sent to the user when they attempt to call a vote while there is already a vote occurring.

**command-blacklisted-message**
This message is sent to the user when they attempt to call a vote on a blacklisted command.

**cap-not-reached-message**
This message is sent to the user when there are not enough players online to call a vote on the command they attempted.

**players-only-message**
This message is sent when a non-player attempts to call a vote (i.e, console).

**queue-empty-message**
This message is sent to a user who uses the viewqueue or callqueue command when a queue is empty.

**queue-cleared-message**
This message is sent to a user who executes the clearqueue command.

**blacklisted-commands**
This must be entered as a list with each argument being a String contained in single or double quotes. The only command blacklisted by default is op. The commands can be general such as "time" which would blacklist all
commands involving time, or specific such as "time set night" which would disallow this specific command. Users would still be able to execute command "time set day", for example. Commands on this list will never be 
voted on.

**whitelisted-commands**
This must be entered as a list with each argument being a String contained in single or double quotes. Commands whitelisted by default include time and weather. Once again, the commands can be specific or general. The
commands on this whitelist will be allowed for voting even if the minimum-players requirement is not reached.


# Commands
** callvote **
###### votechange.cv

This command can take any String as a parameter, but it should be a working command in your Minecraft server. If the vote receives a 2/3 majority of *yes* votes, the user will execute the command. The current voting period lasts ten seconds. Users cannot vote to set themselves a server operator at the present moment.

Example usage:   
*/callvote gamemode creative myUserName*  
*/cv time set day*  
*/callvote weather clear*  

** vote **
###### votechange.v
This command allows users to vote in the current vote being called. Arguments of the vote command can only be *yes* or *no*. 

Example usage:   
*/vote yes*  
*/v no*  
*/vote yes but only the first argument is considered* will still vote yes.  
*/vote no yes* will vote no, as only the first argument is considered.   

** clearqueue **
###### votechange.cq
This command allows the user to clear the queue of upcoming votes. Default permission requirement is server operator only.

Example usage:
*/clearqueue*
*/cq*

** viewqueue **
###### votechange.vq
This command allows the user to view the contents of the queue.

Example usage:
*/viewqueue*
*/vq*
