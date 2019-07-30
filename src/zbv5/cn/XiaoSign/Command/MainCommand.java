package zbv5.cn.XiaoSign.Command;

import lk.vexview.api.VexViewAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import zbv5.cn.XiaoSign.Gui.Inv;
import zbv5.cn.XiaoSign.Gui.VvInv;
import zbv5.cn.XiaoSign.Utils.FileUtils;
import zbv5.cn.XiaoSign.Utils.Util;

public class MainCommand implements CommandExecutor
{
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args)
    {
        if (args.length == 0)
        {
            sender.sendMessage(Util.cc("&6========= [&bXiaoSign&6] ========="));
            sender.sendMessage(Util.cc("&6/XiaoSign &aopen inv &6- &b打开签到页面&7(Inventory页面)"));
            sender.sendMessage(Util.cc("&6/XiaoSign &aopen vv &6- &b打开签到页面&7(VexView页面)"));
            sender.sendMessage(Util.cc("&6/XiaoSign &ainfo [玩家] &6- &b查询玩家信息"));
            sender.sendMessage(Util.cc("&6/XiaoSign &creload &6- &4重载全局配置"));
            return true;
        }
        if (args.length == 1)
        {
            if(args[0].equalsIgnoreCase("reload"))
            {
                if(!sender.hasPermission("XiaoSign.reload"))
                {
                    sender.sendMessage(Util.cc(FileUtils.lang.getString("NoPermission")));
                    return false;
                }
                try
                {
                    FileUtils.LoadConfig();
                    sender.sendMessage(Util.cc(FileUtils.lang.getString("SuccessReload")));
                } catch (Exception e)
                {
                    e.printStackTrace();
                    sender.sendMessage(Util.cc(FileUtils.lang.getString("FailReload")));
                }
            }
        }
        if (args.length == 2)
        {
            if(args[0].equalsIgnoreCase("open"))
            {
                if(args[1].equalsIgnoreCase("vv"))
                {
                    if(!sender.hasPermission("XiaoSign.open.vv"))
                    {
                        sender.sendMessage(Util.cc(FileUtils.lang.getString("NoPermission")));
                        return false;
                    }
                    if ((sender instanceof Player))
                    {
                        Player p = (Player)sender;
                        VexViewAPI.openGui(p,VvInv.Gui(p));
                    } else
                        {
                        sender.sendMessage(Util.cc(FileUtils.lang.getString("NeedPlayer")));
                        return false;
                    }
                }
                if(args[1].equalsIgnoreCase("inv"))
                {
                    if(!sender.hasPermission("XiaoSign.open.inv"))
                    {
                        sender.sendMessage(Util.cc(FileUtils.lang.getString("NoPermission")));
                        return false;
                    }
                    if ((sender instanceof Player))
                    {
                        Player p = (Player)sender;
                        Inv.openInv(p);
                    } else
                    {
                        sender.sendMessage(Util.cc(FileUtils.lang.getString("NeedPlayer")));
                        return false;
                    }
                }
            }
            if(args[0].equalsIgnoreCase("info"))
            {
                if(!sender.hasPermission("XiaoSign.info"))
                {
                    sender.sendMessage(Util.cc(FileUtils.lang.getString("NoPermission")));
                    return false;
                }
                Player p = Bukkit.getPlayer(args[1]);
                if(p == null)
                {
                    sender.sendMessage(Util.cc(FileUtils.lang.getString("NullPlayer")));
                } else {
                    for(String line:FileUtils.lang.getStringList("Info"))
                    {
                        sender.sendMessage(Util.StringHook(p,line,false,0));
                    }
                }
            }
        }
        return false;
    }
}
