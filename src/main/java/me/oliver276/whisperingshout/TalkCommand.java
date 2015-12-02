package me.oliver276.whisperingshout;


import cpw.mods.fml.client.config.GuiConfigEntries;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerSelector;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.gui.PlayerListComponent;
import net.minecraft.server.management.PlayerManager;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.ChunkCoordComparator;

import java.util.ArrayList;
import java.util.List;

public class TalkCommand extends CommandBase{
    private List aliases;

    public TalkCommand(){
        this.aliases = new ArrayList();
        this.aliases.add("talk");
        this.aliases.add("speak");
        this.aliases.add("t");
        this.aliases.add("ta");
    }

    @Override
    public String getCommandName()
    {
        return "talk";
    }

    @Override
    public String getCommandUsage(ICommandSender icommandsender)
    {
        return "talk <range> <text>";
    }

    @Override
    public List getCommandAliases()
    {
        return this.aliases;
    }

    @Override
    public void processCommand(ICommandSender icommandsender, String[] astring)
    {
        EntityPlayer player;

        if(icommandsender instanceof EntityPlayer){
            player = (EntityPlayer)icommandsender;

        }
        else {
            icommandsender.addChatMessage((IChatComponent) new ChatComponentText("Player only command!"));
            return;
        }
        if(astring.length < 2)
        {
            icommandsender.addChatMessage((IChatComponent) new ChatComponentText( EnumChatFormatting.DARK_RED + "Invalid arguments."));
            icommandsender.addChatMessage((IChatComponent) new ChatComponentText( EnumChatFormatting.DARK_GREEN + "Use /talk <blockRange> <Message... ...>"));
            return;
        }

        double range = 0;
        try {
            range = Double.valueOf(astring[0]);
        } catch (Exception e){
            icommandsender.addChatMessage((IChatComponent) new ChatComponentText( EnumChatFormatting.DARK_RED + "Not a recognised number: " + astring[0]));
            icommandsender.addChatMessage((IChatComponent) new ChatComponentText( EnumChatFormatting.DARK_GREEN + "Use /talk <blockRange> <Message... ...>"));
            return;
        }
        if (range > 50.0){
            icommandsender.addChatMessage((IChatComponent) new ChatComponentText( EnumChatFormatting.DARK_RED + "Not a chance you can shout as far as 50 blocks"));
            return;
        }
        StringBuilder strBuilder = new StringBuilder();
        boolean done1stword = false;
        for (String word : astring){
            if (done1stword) {
                strBuilder.append(word).append(" ");
            }
            done1stword = true;
        }
        String message = strBuilder.toString();
        System.out.println(message);
        World workingWorld = icommandsender.getEntityWorld();
        List playerEntities = workingWorld.playerEntities;
        EntityPlayer mainPlayer = workingWorld.getPlayerEntityByName(icommandsender.getCommandSenderName());
        for(Object name : playerEntities){
            EntityPlayer comparePlayer = (workingWorld.getPlayerEntityByName(((EntityPlayerMP) name).getCommandSenderName()));
            if (compareCoordinatesDistance(mainPlayer.getPlayerCoordinates(),comparePlayer.getPlayerCoordinates()) <= range){
                  ((EntityPlayerMP) name).addChatMessage((IChatComponent) new ChatComponentText(EnumChatFormatting.GOLD + "[From " + EnumChatFormatting.RED + compareCoordinatesDistance(mainPlayer.getPlayerCoordinates(),comparePlayer.getPlayerCoordinates()) + EnumChatFormatting.GOLD + " blocks away] " + EnumChatFormatting.GRAY + "<" + icommandsender.getCommandSenderName() + "> " + EnumChatFormatting.WHITE + message));
             }

        }
    }

    public double compareCoordinatesDistance(ChunkCoordinates player1, ChunkCoordinates player2){
        double x = Math.abs(player1.posX - player2.posX);
        double y = Math.abs(player1.posY - player2.posY);
        double z = Math.abs(player1.posZ - player2.posZ);
        return x + y + z;
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender icommandsender)
    {
        return true;
    }

    @Override
    public List addTabCompletionOptions(ICommandSender icommandsender,
                                        String[] astring)
    {
        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] astring, int i)
    {
        return false;
    }

    @Override
    public int compareTo(Object o)
    {
        return 0;
    }
}
