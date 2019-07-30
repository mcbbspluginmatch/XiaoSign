package zbv5.cn.XiaoSign.Utils;

import java.text.SimpleDateFormat;
import java.util.*;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import zbv5.cn.XiaoSign.Main;
import zbv5.cn.XiaoSign.Store.Mysql;

public class Util
{
    public static HashMap<String, String> WeekDate = new HashMap<String, String>();
    public static List<String> WeekDateList = new ArrayList<String>();
    public static String Prefix = "&6[&bXiaoSign&6]";

    public static void Print(String s)
    {
        Bukkit.getConsoleSender().sendMessage(cc(Prefix+s));
    }

    public static String cc(String s)
    {
        s = ChatColor.translateAlternateColorCodes('&', s);
        return s;
    }

    public static void CheckPlugin()
    {
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI"))
        {
            Main.PlaceholderAPI = true;
            Print("&b检测到前置插件 &aPlaceholderAPI");
        } else{
            Print("&c未检测到前置插件 &aPlaceholderAPI");
        }
        if (Bukkit.getPluginManager().isPluginEnabled("VexView"))
        {
            Main.VexView = true;
            Print("&b检测到前置插件 &aVexView");
        } else{
            Print("&c未检测到前置插件 &aVexView");
        }
    }

    public static void  getWeekDate()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Calendar cal = Calendar.getInstance();

        while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY)
        {
            cal.add(Calendar.DATE, -1);
        }
        for (int i = 1; i < 8; i++)
        {
            WeekDate.put(Integer.toString(i), dateFormat.format(cal.getTime()));
            WeekDateList.add(dateFormat.format(cal.getTime()));
            Print("&e"+i+": "+dateFormat.format(cal.getTime()));
            cal.add(Calendar.DATE, 1);
        }
    }

    public static String getNowTime()
    {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        return dateFormat.format(date);
    }

    public static int getNowWeek()
    {
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
        if(dayWeek==1)
        {
            dayWeek = 8;
        }
        return dayWeek-1;
    }

    public static String CheckPlayerSign(Player p,String time)
    {
        List<String> date = FileUtils.getPlayerSignDate(p);
        String now = getNowTime();
        if(date==null)
        {
            if(time.equals(now))
            {
                return "NotSign";
            } else {
                return "PastNotSign";
            }
        }
        if(time.equals(now))
        {
            if(date.contains(now))
            {
                return "AlreadySign";
            } else{
                return "NotSign";
            }
        }
        if(date.contains(time))
        {
            return "PastSign";
        }else{
            return "PastNotSign";
        }
    }
    private static int getPlayerSignEx(Player p,String type)
    {
        int ex = 0;
        if(FileUtils.Store.equals("Yml"))
        {
            List<String> list = FileUtils.getPlayerSignDate(p);
            if(list != null)
            {
                if(type.equals("All"))
                {
                    for(String s : list)
                    {
                        ex = ex+1;
                    }
                }
                if(type.equals("Month"))
                {
                    Date date = new Date(System.currentTimeMillis());
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/");
                    for(String s : list)
                    {
                        if(s.startsWith(dateFormat.format(date)))
                        {
                            ex = ex+1;
                        }
                    }
                }
                if(type.equals("Week"))
                {
                    for(String s : list)
                    {
                        if(WeekDateList.contains(s))
                        {
                            ex = ex+1;
                        }
                    }
                }
            }
        }
        if(FileUtils.Store.equals("Mysql"))
        {
            List<String> list = FileUtils.getPlayerSignDate(p);
            if(list != null)
            {
                if(type.equals("All"))
                {
                    ex = Integer.parseInt(Mysql.Mysql_PlayerAllEx.get(p.getName()));
                }
                if(type.equals("Month"))
                {
                    ex = Integer.parseInt(Mysql.Mysql_PlayerMonthEx.get(p.getName()));
                }
                if(type.equals("Week"))
                {
                    for(String s : list)
                    {
                        if(WeekDateList.contains(s))
                        {
                            ex = ex+1;
                        }
                    }
                }
            }
        }
        return  ex;
    }
    private static String getChineseWeek(int i)
    {
        if(i==1)
        {
            return cc(FileUtils.lang.getString("Week.1"));
        }
        if(i==2)
        {
            return cc(FileUtils.lang.getString("Week.2"));
        }
        if(i==3)
        {
            return cc(FileUtils.lang.getString("Week.3"));
        }
        if(i==4)
        {
            return cc(FileUtils.lang.getString("Week.4"));
        }
        if(i==5)
        {
            return cc(FileUtils.lang.getString("Week.5"));
        }
        if(i==6)
        {
            return cc(FileUtils.lang.getString("Week.6"));
        }
        if(i==7)
        {
            return cc(FileUtils.lang.getString("Week.7"));
        }
        return "";
    }

    public static String StringHook(Player p,String s,boolean sign,int SignWeekDay)
    {
        s = cc(s.replace("<player>",p.getName()).replace("<today>",getNowTime()).replace("<ex>",Integer.toString(Util.getPlayerSignEx(p,"All"))).replace("<weekex>",Integer.toString(Util.getPlayerSignEx(p,"Week"))).replace("<monthex>",Integer.toString(Util.getPlayerSignEx(p,"Month"))));
        if(Main.PlaceholderAPI)
        {
            s = PlaceholderAPI.setPlaceholders(p, s);
        }
        if(sign)
        {
            s = s.replace("<info>",FileUtils.lang.getString("Sign."+CheckPlayerSign(p,WeekDate.get(Integer.toString(SignWeekDay))))).replace("<day>",Util.getChineseWeek(SignWeekDay)).replace("<date>",WeekDate.get(Integer.toString(SignWeekDay)));
            return s;
        } else {
            return s;
        }
    }
    public static void Run(List<String> list,Player p)
    {
        for(String ss:list)
        {
            if(Main.PlaceholderAPI)
            {
                ss = PlaceholderAPI.setPlaceholders(p, ss);
            }
            ss = ss.replace("<player>",p.getName());
            if(ss.startsWith("[close]"))
            {
                p.closeInventory();
            }
            if(ss.startsWith("[message]"))
            {
                ss=ss.replace("[message]", "");
                p.sendMessage( cc(ss));
            }
            if(ss.startsWith("[bc]"))
            {
                ss=ss.replace("[bc]", "");
                Bukkit.getServer().broadcastMessage(ss);
            }
            if(ss.startsWith("[op]"))
            {
                boolean op = p.isOp();
                p.setOp(true);
                try
                {
                    ss=ss.replace("[op]", "");
                    p.chat("/"+ss);
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
                try
                {
                    p.setOp(op);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    p.setOp(false);
                }
            }
            if(ss.startsWith("[player]"))
            {
                ss=ss.replace("[player]", "");
                p.performCommand(ss);
            }
            if(ss.startsWith("[console]"))
            {
                ss=ss.replace("[console]", "");
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), ss);
            }
            if(ss.startsWith("[sign]"))
            {
                PlayerSign(p);
            }
        }
    }

    private static  boolean CheckPlayerSize(Player p,int NeedSlot)
    {
        Boolean check = false;
        int PlayerSize = p.getInventory().getSize() - 5;
        for (ItemStack item : p.getInventory().getContents())
        {
            if ((item != null))
            {
                PlayerSize = PlayerSize -1;
            }
        }
        if(PlayerSize < NeedSlot)
        {
            p.sendMessage(Util.cc(FileUtils.lang.getString("PlayerInventoryFull")));
        } else {
            check = true;
        }
        return check;
    }

    private static void  PlayerSign(Player p)
    {
        int NeedSlot = 0;
        List<String> RewardRun = new ArrayList<String>();
        int priority = 2147483647;
        ConfigurationSection RewardList = (ConfigurationSection)FileUtils.config.get("SignReward.EveryDay");
        for (String Reward : RewardList.getKeys(false))
        {
            ConfigurationSection RewardConfig = FileUtils.config.getConfigurationSection("SignReward.EveryDay."+Reward);
            if(RewardConfig.getInt("priority") <= priority)
            {
                if(p.hasPermission(RewardConfig.getString("NeedPermission")))
                {
                    priority = RewardConfig.getInt("priority");
                    RewardRun = RewardConfig.getStringList("Reward");
                    NeedSlot = RewardConfig.getInt("NeedSlot");
                }
            }
        }
        if(CheckPlayerSize(p,NeedSlot))
        {
            FileUtils.setPlayerSign(p,getNowTime());
            Run(RewardRun,p);
            if(FileUtils.config.getBoolean("SignReward.RewardWeekEx.Enable"))
            {
                ConfigurationSection RewardExList = (ConfigurationSection)FileUtils.config.get("SignReward.RewardWeekEx.List");
                for (String RewardEx : RewardExList.getKeys(false))
                {
                    int WeekEx = getPlayerSignEx(p,"Week");
                    if(Integer.parseInt(RewardEx) == WeekEx)
                    {
                        ConfigurationSection Reward = FileUtils.config.getConfigurationSection("SignReward.RewardWeekEx.List."+RewardEx);
                        if(CheckPlayerSize(p,Reward.getInt("NeedSlot")))
                        {
                            Run(Reward.getStringList("Reward"),p);
                        }
                    }
                }
            }
            if(FileUtils.config.getBoolean("SignReward.RewardMonthEx.Enable"))
            {
                ConfigurationSection RewardExList = (ConfigurationSection)FileUtils.config.get("SignReward.RewardMonthEx.List");
                for (String RewardEx : RewardExList.getKeys(false))
                {
                    int MonthEx = getPlayerSignEx(p,"Month");
                    if(Integer.parseInt(RewardEx) == MonthEx)
                    {
                        ConfigurationSection Reward = FileUtils.config.getConfigurationSection("SignReward.RewardMonthEx.List."+RewardEx);
                        if(CheckPlayerSize(p,Reward.getInt("NeedSlot")))
                        {
                            Run(Reward.getStringList("Reward"),p);
                        }
                    }
                }
            }
            if(FileUtils.config.getBoolean("SignReward.RewardAllEx.Enable"))
            {
                ConfigurationSection RewardExList = (ConfigurationSection)FileUtils.config.get("SignReward.RewardAllEx.List");
                for (String RewardEx : RewardExList.getKeys(false))
                {
                    int Ex = getPlayerSignEx(p,"All");
                    if(Integer.parseInt(RewardEx) == Ex)
                    {
                        ConfigurationSection Reward = FileUtils.config.getConfigurationSection("SignReward.RewardAllEx.List."+RewardEx);
                        if(CheckPlayerSize(p,Reward.getInt("NeedSlot")))
                        {
                            Run(Reward.getStringList("Reward"),p);
                        }
                    }
                }
            }
        }

    }
}
