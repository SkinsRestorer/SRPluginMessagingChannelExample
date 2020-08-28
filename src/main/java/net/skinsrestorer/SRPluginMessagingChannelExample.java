package net.skinsrestorer;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

//PluginMessageListener import!

public class SRPluginMessagingChannelExample extends JavaPlugin implements PluginMessageListener {
    private CommandSender console;

    @Override
    public void onEnable() {
        console = getServer().getConsoleSender();
        console.sendMessage("Loading SRPluginMessagingChannelExample...");

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "sr:messagechannel");
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "sr:messagechannel", this);

        try {
            this.getCommand("srpmce").setExecutor(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        console.sendMessage("Finished loading SRPluginMessagingChannelExample");
    }

    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
            /*if (!channel.equals("sr:messagechannel"))
                return;

            Bukkit.getScheduler().runTaskAsynchronously(getInstance(), () -> {
                DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));

                try {
                    String subchannel = in.readUTF();

                    if (subchannel.equalsIgnoreCase("returnSkins")) {

                        Player p = Bukkit.getPlayer(in.readUTF());
                        int page = in.readInt();

                        short len = in.readShort();
                        byte[] msgbytes = new byte[len];
                        in.readFully(msgbytes);

                        Map<String, Property> skinList = convertToObject(msgbytes);

                        // Code here
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                });*/
    }

    public void requestSkinsFromBungeeCord(Player p, int page) {
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(bytes);

            out.writeUTF("getSkins");
            out.writeUTF(p.getName());
            out.writeInt(page); // Page

            p.sendPluginMessage(this, "sr:messagechannel", bytes.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //skin clear p
    public void requestSkinClearFromBungeeCord(Player p) {
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(bytes);

            out.writeUTF("clearSkin");
            out.writeUTF(p.getName());

            p.sendPluginMessage(this, "sr:messagechannel", bytes.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void requestSkinUpdateFromBungeeCord(Player p) {
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(bytes);

            out.writeUTF("updateSkin");
            out.writeUTF(p.getName());

            p.sendPluginMessage(this, "sr:messagechannel", bytes.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void requestSkinSetFromBungeeCord(Player p, String skin) {
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(bytes);

            out.writeUTF("setSkin");
            out.writeUTF(p.getName());
            out.writeUTF(skin);

            p.sendPluginMessage(this, "sr:messagechannel", bytes.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*##################################################*/
    /*#       Below code is part of the example.       #*/
    /*# You don't require to include this in your code #*/
    /*##################################################*/

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender = console;
        }

        //define player sender as "p"
        final Player p = (Player) sender;

        if (args.length < 1) {
            //help message
            sender.sendMessage("Plugin Commands:\n/api <skin name>\n/api clear\n/api set <name>\nTBD...");
            return true;
        }


        if (args.length == 1) {
            // command <clear>
            if (args[0].equalsIgnoreCase("clear")) {
                if (sender.hasPermission("skinsrestorer.command.clear")) {
                    p.getName();

                    requestSkinClearFromBungeeCord(p);
                    return true;
                } else {
                    p.sendMessage("You Don't have permissions for clear");
                    return false;
                }
            }
            if (args[0].equalsIgnoreCase("update")) {
                if (sender.hasPermission("skinsrestorer.command.update")) {
                    p.getName();

                    requestSkinUpdateFromBungeeCord(p);
                    return true;
                } else {
                    p.sendMessage("You Don't have permissions for clear");
                    return false;
                }
            }

                    // command <name>
            } else if (sender.hasPermission("skinsrestorer.command.set")) {
                final String skin = args[0];

                requestSkinSetFromBungeeCord(p, skin);
                return true;
            } else {
                p.sendMessage("You Don't have permissions for set");
                return false;
            }


            // command Set <name>
            if (args.length == 2 && args[0].equalsIgnoreCase("set")) {
                if (sender.hasPermission("skinsrestorer.playercmds")) {
                    final String skin = args[1];

                    requestSkinSetFromBungeeCord(p, skin);
                    return true;
                } else {
                    p.sendMessage("You Don't have permissions for set <name>");
                    return false;
                }
            }

            // command url <name>
            if (args.length == 2 && args[0].equalsIgnoreCase("url")) {
                if (sender.hasPermission("skinsrestorer.playercmds")) {
                    final String url = args[1];

                    // TODO: add url
                    return true;
                } else {
                    p.sendMessage("You Don't have permissions for set <name>");
                    return false;
                }
            }

            // command clear <player>
            if (args.length == 2 && args[0].equalsIgnoreCase("clear")) {
                String target = args[1];
                Player targetPlayer = Bukkit.getPlayer(target);

                if (targetPlayer == null) {
                    sender.sendMessage("Target player '" + target + "' is not online." );
                    return true;
                }

                requestSkinClearFromBungeeCord(targetPlayer);
            }

            //skin set other
            if (args.length > 2 && args[0].equalsIgnoreCase("set")) {
                if (sender.hasPermission("skinsrestorer.command.set.other")) {

                    StringBuilder sb = new StringBuilder();
                    for (int i = 2; i < args.length; i++)
                        if (args.length == 3)
                            sb.append(args[i]);
                        else if (i + 1 == args.length)
                            sb.append(args[i]);
                        else
                            sb.append(args[i]).append(" ");

                    final String skin = sb.toString();
                    Player player = Bukkit.getPlayer(args[1]);

                    if (player == null)
                        for (Player pl : Bukkit.getOnlinePlayers())
                            if (pl.getName().startsWith(args[1])) {
                                player = pl;
                                break;
                            }

                    if (player == null) {
                        sender.sendMessage("player " + player + " is not online");
                        return true;
                    }
                    final Player targetPlayer = player;
                    requestSkinSetFromBungeeCord(targetPlayer, skin);

                } else {
                    p.sendMessage("You Don't have permissions for set <name> <player>");
                    return false;
                }
            }

            // Skin Help
            if (p.hasPermission("skinsrestorer.playercmds")) {
                p.sendMessage("help");
                if (p.hasPermission("skinsrestorer.cmds"))
                    p.sendMessage("adminhelp");
            } else {
                p.sendMessage("You don't have permissions to do that");
            }

        return true;
    }
}

