package zbv5.cn.XiaoSign.Listener;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import zbv5.cn.XiaoSign.Store.Mysql;
import zbv5.cn.XiaoSign.Utils.FileUtils;
import zbv5.cn.XiaoSign.Utils.Util;

import java.util.ArrayList;
import java.util.HashMap;

public class PlayerListener implements Listener
{
    public static HashMap<String, Integer> OpenCheck = new HashMap<String, Integer>();

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent e)
    {
        Player p = (Player) e.getPlayer();
        if(e.getInventory().getTitle().equals(Util.cc(FileUtils.inv.getString("Title"))))
        {
            OpenCheck.put(p.getName(),99999);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e)
    {
        if (e.getPlayer() instanceof Player)
        {
            if(OpenCheck.containsKey(e.getPlayer().getName()))
            {
                OpenCheck.remove(e.getPlayer().getName());
            }
        }
    }
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e)
    {
        if(FileUtils.PlayerData.containsKey(e.getPlayer().getName()))
        {
            FileUtils.PlayerData.remove(e.getPlayer().getName());
        }
        if(FileUtils.Store.equals("Mysql"))
        {
            Mysql.Mysql_PlayerMonth.remove(e.getPlayer().getName());
            Mysql.Mysql_PlayerAllEx.remove(e.getPlayer().getName());
        }
    }
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e)
    {
        if (e.getWhoClicked() instanceof Player)
        {
            if(e.getInventory().getTitle().equals(Util.cc(FileUtils.inv.getString("Title"))))
            {
                Player p = ((Player) e.getWhoClicked()).getPlayer();
                if(OpenCheck.containsKey(p.getName()))
                {
                    e.setCancelled(true);
                    int ClickSlot = e.getSlot();
                    ConfigurationSection ItemList = (ConfigurationSection)FileUtils.inv.get("items");

                    for (String Items : ItemList.getKeys(false))
                    {
                        ConfigurationSection items = FileUtils.inv.getConfigurationSection("items."+Items);
                        Boolean sign = false;
                        int SignWeekDay = 0;
                        String SignInfo = "";
                        if(items.getBoolean("Sign"))
                        {
                            SignWeekDay = items.getInt("SignWeekDay");
                            SignInfo = Util.CheckPlayerSign(p,Util.WeekDate.get(Integer.toString(SignWeekDay)));
                            sign = true;
                        }
                        for(int slot:FileUtils.inv.getIntegerList("items."+Items+".slots"))
                        {
                            if(slot == ClickSlot)
                            {
                                if(sign)
                                {
                                    Util.Run(FileUtils.inv.getStringList("SignItem."+SignInfo+".click"),p);
                                    if(SignInfo.equals("NotSign"))
                                    {
                                        SignInfo = Util.CheckPlayerSign(p,Util.WeekDate.get(Integer.toString(SignWeekDay)));
                                        items = FileUtils.inv.getConfigurationSection("SignItem."+SignInfo);
                                        ItemStack item = new ItemStack(Material.getMaterial(items.getInt("material")), SignWeekDay, (short)items.getInt("data"));
                                        ItemMeta id = item.getItemMeta();
                                        id.setDisplayName(Util.StringHook(p,items.getString("display_name"),sign,SignWeekDay));
                                        ArrayList<String> lore = new ArrayList<String>();
                                        for(String line:items.getStringList("Lore"))
                                        {
                                            lore.add(Util.StringHook(p,line,sign,SignWeekDay));
                                        }

                                        id.setLore(lore);
                                        item.setItemMeta(id);
                                        if(OpenCheck.containsKey(p.getName()))
                                        {
                                            e.getClickedInventory().setItem(ClickSlot, item);
                                        }
                                    }
                                } else {
                                    Util.Run(items.getStringList("click"),p);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
