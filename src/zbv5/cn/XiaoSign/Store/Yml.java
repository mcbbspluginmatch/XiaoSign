package zbv5.cn.XiaoSign.Store;

import java.io.File;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import zbv5.cn.XiaoSign.Main;

public class Yml
{
    public static void createDataFolder()
    {
        File data = new File(Main.getInstance().getDataFolder(), "data");
        if (!data.exists())
        {
            data.mkdirs();
        }
    }

    public static void createPlayerData(Player p)
    {
        File PlayerData = new File(Main.getInstance().getDataFolder() + "//data", p.getName() + ".yml");

        if (!PlayerData.exists())
        {
            Main.getInstance().getDataFolder().mkdir();
            try {
                PlayerData.createNewFile();
                FileConfiguration config = YamlConfiguration.loadConfiguration(PlayerData);
                config.set("name", p.getName());
                config.set("uuid", p.getUniqueId().toString());
                config.set("date", "");
                config.save(PlayerData);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }

    public static List<String> getPlayerSign(Player p)
    {
        File PlayerData = new File(Main.getInstance().getDataFolder() + "//data", p.getName() + ".yml");
        if (PlayerData.exists())
        {
            FileConfiguration config = YamlConfiguration.loadConfiguration(PlayerData);
            return config.getStringList("date");
        }
        return null;
    }

    public static void setPlayerSign(Player p,List<String> s)
    {
        File PlayerData = new File(Main.getInstance().getDataFolder() + "//data", p.getName() + ".yml");
        if (!PlayerData.exists())
        {
            createPlayerData(p);
        }
        FileConfiguration config = YamlConfiguration.loadConfiguration(PlayerData);
        try
        {
            config.set("date",s);
            config.save(PlayerData);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
