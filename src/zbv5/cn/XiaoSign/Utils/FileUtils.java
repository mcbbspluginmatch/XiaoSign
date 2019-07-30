package zbv5.cn.XiaoSign.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import zbv5.cn.XiaoSign.Main;
import zbv5.cn.XiaoSign.Store.Mysql;
import zbv5.cn.XiaoSign.Store.Yml;

public class FileUtils
{
    public static YamlConfiguration config;
    public static YamlConfiguration lang;
    public static YamlConfiguration inv;
    public static YamlConfiguration vv;
    public static String Store = null;
    public static HashMap<String, List<String>> PlayerData = new HashMap<String, List<String>>();

    public static void LoadConfig()
    {
        File Lang_Yml = new File(Main.getInstance().getDataFolder(), "lang.yml");
        if (!Lang_Yml.exists())
        {
            Main.getInstance().saveResource("lang.yml", false);
        }
        lang = YamlConfiguration.loadConfiguration(Lang_Yml);
        Util.Prefix = lang.getString("Prefix");
        Util.Print("&3lang.yml &a加载.");

        File Config_Yml = new File(Main.getInstance().getDataFolder(), "config.yml");
        if (!Config_Yml.exists())
        {
            Main.getInstance().saveResource("config.yml", false);
        }
        config = YamlConfiguration.loadConfiguration(Config_Yml);
        Util.Print("&3config.yml &a加载.");

        File Inv_Yml = new File(Main.getInstance().getDataFolder(), "inv.yml");
        if (!Inv_Yml.exists())
        {
            Main.getInstance().saveResource("inv.yml", false);
        }
        inv = YamlConfiguration.loadConfiguration(Inv_Yml);
        Util.Print("&3inv.yml &a加载.");

        File Vv_Yml = new File(Main.getInstance().getDataFolder(), "vv.yml");
        if (!Vv_Yml.exists())
        {
            Main.getInstance().saveResource("vv.yml", false);
        }
        vv = YamlConfiguration.loadConfiguration(Vv_Yml);
        Util.Print("&3vv.yml &a加载.");
    }

    public static void LoadData()
    {
        if(config.getString("Store").equals("Yml"))
        {
            Store = "Yml";
            Util.Print("&e储存方式: &6"+Store);
            Yml.createDataFolder();
            return;
        }
        if(config.getString("Store").equals("Mysql"))
        {
            Store = "Mysql";
            Util.Print("&e储存方式: &6"+Store);
            Mysql.createTable();
            return;
        }
        Util.Print("&c储存方式异常,请检查配置文件Store配置.");
    }
    public static void setPlayerSign(Player p,String NewDate)
    {
        List<String> Data = new ArrayList<String>();
        List<String> OldPlayerData = PlayerData.get(p.getName());
        if(OldPlayerData != null)
        {
            Data = OldPlayerData;
        }
        Data.add(NewDate);
        if(config.getString("Store").equals("Yml"))
        {
            Yml.setPlayerSign(p,Data);
        }
        if(config.getString("Store").equals("Mysql"))
        {
            Mysql.setPlayerSign(p,Integer.toString(Util.getNowWeek()),NewDate);
        }
        UpdatePlayerSignDate(p);
    }
    public static void UpdatePlayerSignDate(Player p)
    {
        if(config.getString("Store").equals("Yml"))
        {
            PlayerData.put(p.getName(),Yml.getPlayerSign(p));
        }
        if(config.getString("Store").equals("Mysql"))
        {
            PlayerData.put(p.getName(),Mysql.getPlayerSign(p));
        }
    }
    public static List<String> getPlayerSignDate(Player p)
    {
        if(config.getString("Store").equals("Yml"))
        {
            if(PlayerData.containsKey(p.getName()))
            {
                return PlayerData.get(p.getName());
            } else {
                PlayerData.put(p.getName(),Yml.getPlayerSign(p));
                return PlayerData.get(p.getName());
            }
        }
        if(config.getString("Store").equals("Mysql"))
        {
            if(PlayerData.containsKey(p.getName()))
            {
                return PlayerData.get(p.getName());
            } else {
                PlayerData.put(p.getName(),Mysql.getPlayerSign(p));
                return PlayerData.get(p.getName());
            }
        }
        return null;
    }
}
