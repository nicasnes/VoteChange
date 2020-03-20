# VoteChange
A Minecraft plugin allowing for majority-based voting to execute commands.

# Installation
Download the .jar file and place it in the plugins folder of your server. 

# Configuration
At this time, there is no user configuration available through a config.yml file. This feature will be implemented in the future to disallow commands, change the required majority, and set a minimum number of players online, among other features. 

The plugin utilizes two primary commands: */callvote* and */vote*. 

# Commands
###### callvote
This command can take any String as a parameter, but it should be a working command in your Minecraft server. If the vote receives a 2/3 majority of *yes* votes, the user will execute the command. The current voting period lasts ten seconds. Users cannot vote to set themselves a server operator at the present moment.

Example usage:
*/callvote gamemode creative myUserName*  
*/callvote time set day*  
*/callvote weather clear*  

###### vote
This command allows users to vote in the current vote being called. Arguments of the vote command can only be *yes* or *no*. 

Example usage:   
*/vote yes*  
*/vote no*  
*/vote yes but only the first argument is considered* will still vote yes.  
*/vote no yes* will vote yes, as only the first argument is considered.   
