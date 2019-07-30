package zbv5.cn.XiaoSign.Gui;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import zbv5.cn.XiaoSign.Main;
import zbv5.cn.XiaoSign.Utils.FileUtils;
import zbv5.cn.XiaoSign.Utils.Util;

import java.util.ArrayList;

public class Inv
{
    public static void openInv(Player p)
    {
        if(!Util.WeekDate.containsValue(Util.getNowTime()))
        {
            Util.getWeekDate();
        }

        Inventory inv = Main.getInstance().getServer().createInventory(null, FileUtils.inv.getInt("size"), Util.cc(FileUtils.inv.getString("Title")));

        ConfigurationSection ItemList = (ConfigurationSection)FileUtils.inv.get("items");

        for (String Items : ItemList.getKeys(false))
        {
            ConfigurationSection items = FileUtils.inv.getConfigurationSection("items."+Items);
            ItemStack item;
            Boolean sign = false;
            int SignWeekDay = 0;
            int amount = 0;
            if(items.getBoolean("Sign"))
            {
                SignWeekDay = items.getInt("SignWeekDay");
                String SignInfo = Util.CheckPlayerSign(p,Util.WeekDate.get(Integer.toString(SignWeekDay)));
                items = FileUtils.inv.getConfigurationSection("SignItem."+SignInfo);
                sign = true;
                amount = SignWeekDay;
            }
            if(amount == 0)
            {
                amount = items.getInt("amount");
            }
            item = new ItemStack(Material.getMaterial(items.getInt("material")), amount, (short) items.getInt("data"));

            ItemMeta id = item.getItemMeta();
            id.setDisplayName(Util.StringHook(p,items.getString("display_name"),sign,SignWeekDay));
            ArrayList<String> lore = new ArrayList<String>();
            for(String line:items.getStringList("Lore"))
            {
                lore.add(Util.StringHook(p,line,sign,SignWeekDay));
            }
            id.setLore(lore);
            item.setItemMeta(id);

            for(int slot:FileUtils.inv.getIntegerList("items."+Items+".slots"))
            {
                inv.setItem(slot,item);
            }
        }
        p.openInventory(inv);
    }


}
