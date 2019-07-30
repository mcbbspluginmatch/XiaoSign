package zbv5.cn.XiaoSign;

import org.bukkit.plugin.java.JavaPlugin;
import zbv5.cn.XiaoSign.Command.MainCommand;
import zbv5.cn.XiaoSign.Listener.PlayerListener;
import zbv5.cn.XiaoSign.Utils.FileUtils;
import zbv5.cn.XiaoSign.Utils.Util;

/**
 * @author wow_xiaoyao
 */

public class Main extends JavaPlugin
{
    private static Main instance;
    public static boolean PlaceholderAPI = false;
    public static boolean VexView = false;

    public static Main getInstance()
    {
        return instance;
    }

    @Override
    public void onEnable()
    {
        instance = this;
        FileUtils.LoadConfig();
        FileUtils.LoadData();
        Util.CheckPlugin();
        Util.getWeekDate();
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        getCommand("XiaoSign").setExecutor(new MainCommand());
        Util.Print("&a插件加载完成");
    }
    @Override
    public void onDisable()
    {
        Util.Print("&c插件卸载");
    }
}
