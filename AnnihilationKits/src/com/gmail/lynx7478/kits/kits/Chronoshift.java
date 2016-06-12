package com.gmail.lynx7478.kits.kits;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.gmail.lynx7478.anni.anniGame.AnniPlayer;
import com.gmail.lynx7478.anni.kits.KitUtils;
import com.gmail.lynx7478.anni.kits.Loadout;
import com.gmail.lynx7478.anni.main.AnnihilationMain;
import com.gmail.lynx7478.kits.base.SpecialItemKit;

public class Chronoshift extends SpecialItemKit {
	
	private ArrayList<Player> chronosift;
	
	@Override
	protected void onInitialize()
	{
		this.chronosift = new ArrayList<Player>();
	}

	@Override
	protected ItemStack specialItem()
	{
		ItemStack firestorm  = KitUtils.addSoulbound(new ItemStack(Material.WATCH));
		ItemMeta meta = firestorm.getItemMeta();
		meta.setDisplayName(getSpecialItemName()+" "+ChatColor.GREEN+"READY");
		firestorm.setItemMeta(meta);
		return firestorm;
	}

	@Override
	protected String defaultSpecialItemName()
	{
		return ChatColor.AQUA+"Chronoshift";
	}

	@Override
	protected boolean isSpecialItem(ItemStack stack)
	{
		if(stack != null && stack.hasItemMeta() && stack.getItemMeta().hasDisplayName())
		{
			String name = stack.getItemMeta().getDisplayName();
			if(name.contains(getSpecialItemName()) && KitUtils.isSoulbound(stack))
				return true;
		}
		return false;
	}

	@Override
	protected long getDelayLength()
	{
		return 40000;
	}

	@Override
	protected String getInternalName()
	{
		return "Chronoshift";
	}

	@Override
	protected ItemStack getIcon()
	{
		return new ItemStack(Material.GOLD_BOOTS);
	}

	@Override
	protected List<String> getDefaultDescription()
	{
        final List<String> toReturn = new ArrayList<String>();
        final ChatColor aqua = ChatColor.AQUA;
        toReturn.add(aqua + "You are the time traveler.");
        toReturn.add(" ");
        return toReturn;
	}

	@Override
	public void cleanup(Player player)
	{
		
	}
	
	@Override
	protected Loadout getFinalLoadout()
	{
		return new Loadout().addGoldSword().addWoodPick().addWoodAxe().addItem(super.getSpecialItem());
	}

	@Override
	protected boolean useDefaultChecking()
	{
		return true;
	}

	@Override
	protected boolean performPrimaryAction(final Player player, AnniPlayer p) {
		if(p.getTeam() != null)
		{
			this.chronosift.add(player);
			Bukkit.getServer().getScheduler().runTaskLater(AnnihilationMain.getInstance(), new Runnable()
					{
				public void run()
				{
					Chronoshift.this.chronosift.remove(player);
				}
					}, 5 * 20);
			player.getInventory().remove(player.getItemInHand());
			return true;
		}
		else return false;
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e)
	{
		final Player p = e.getEntity();
		final ItemStack[] drops = p.getInventory().getContents();
		Player k = e.getEntity().getKiller();
		final Location dLoc = k.getLocation();
		if(this.chronosift.contains(p))
		{
			Bukkit.broadcastMessage(ChatColor.GREEN+"CRHONOSHIFT!");
			e.getDrops().clear();
			for(int i = 0; i<5; i++)
			{
				p.getWorld().strikeLightningEffect(dLoc);
			}
			Bukkit.getServer().getScheduler().runTaskLater(AnnihilationMain.getInstance(), new Runnable()
					{
				public void run()
				{
					p.teleport(dLoc);
					p.getInventory().setContents(drops);
				}
					}, 20);
		}
	}

	@Override
	protected boolean performSecondaryAction(Player player, AnniPlayer p) 
	{
		return false;
	}

}
