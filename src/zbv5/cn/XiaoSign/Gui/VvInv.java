package zbv5.cn.XiaoSign.Gui;

import lk.vexview.api.VexViewAPI;
import lk.vexview.gui.VexGui;
import lk.vexview.gui.components.*;
import lk.vexview.gui.components.expand.VexGifImage;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import zbv5.cn.XiaoSign.Listener.PlayerListener;
import zbv5.cn.XiaoSign.Main;
import zbv5.cn.XiaoSign.Utils.FileUtils;
import zbv5.cn.XiaoSign.Utils.Util;

import java.util.ArrayList;
import java.util.List;

public class VvInv
{
    public static VexGui Gui(Player p)
    {
        if(Main.VexView)
        {
            VexGui inv = new VexGui(FileUtils.vv.getString("Url"), FileUtils.vv.getInt("X"), FileUtils.vv.getInt("Y"),FileUtils.vv.getInt("Width"),FileUtils.vv.getInt("High"), FileUtils.vv.getInt("Width"),FileUtils.vv.getInt("High"));
            if(FileUtils.vv.getBoolean("PlayerDraw.Enable"))
            {
                inv.addComponent(new VexPlayerDraw(FileUtils.vv.getInt("PlayerDraw.X"), FileUtils.vv.getInt("PlayerDraw.Y"), FileUtils.vv.getInt("PlayerDraw.Scale"), p));
            }

            if(FileUtils.vv.getBoolean("Image.Enable"))
            {
                ConfigurationSection ImageList = (ConfigurationSection)FileUtils.vv.get("Image.List");
                for (String Images : ImageList.getKeys(false))
                {
                    ConfigurationSection Image = FileUtils.vv.getConfigurationSection("Image.List."+Images);
                    String Url = Image.getString("Url");
                    if(Image.getBoolean("Sign"))
                    {
                        int SignWeekDay = Image.getInt("SignWeekDay");
                        String SignInfo = Util.CheckPlayerSign(p,Util.WeekDate.get(Integer.toString(SignWeekDay)));
                        Url = FileUtils.vv.getString("Sign."+SignInfo);
                    }
                    if(Url.endsWith(".gif"))
                    {
                        if(Image.getStringList("HoverText").isEmpty())
                        {
                            inv.addComponent(new VexGifImage(Url, Image.getInt("X"), Image.getInt("Y"), Image.getInt("W"), Image.getInt("H"), Image.getInt("XS"), Image.getInt("YS"), Image.getInt("Interval")));
                        }
                        else
                        {
                            VexHoverText text = new VexHoverText(Image.getStringList("HoverText"));
                            inv.addComponent(new VexGifImage(Url, Image.getInt("X"), Image.getInt("Y"), Image.getInt("W"), Image.getInt("H"), Image.getInt("XS"), Image.getInt("YS"), Image.getInt("Interval"),text));
                        }
                    } else {
                        if(Image.getStringList("HoverText").isEmpty())
                        {
                            inv.addComponent(new VexImage(Url, Image.getInt("X"), Image.getInt("Y"), Image.getInt("XS"), Image.getInt("YS")));
                        }
                        else
                        {
                            VexHoverText text = new VexHoverText(Image.getStringList("HoverText"));
                            inv.addComponent(new VexImage(Url, Image.getInt("X"), Image.getInt("Y"), Image.getInt("XS"), Image.getInt("YS"),text));
                        }
                    }
                }
            }

            if(FileUtils.vv.getBoolean("Text.Enable"))
            {
                ConfigurationSection TextList = (ConfigurationSection)FileUtils.vv.get("Text.List");
                for (String Texts : TextList.getKeys(false))
                {
                    ConfigurationSection Text = FileUtils.vv.getConfigurationSection("Text.List."+Texts);
                    List<String> l = Text.getStringList("List");
                    if(l.size() > 0)
                    {
                        List<String> ll = new ArrayList<String>();
                        for(String line:l)
                        {
                            if(Text.getBoolean("Sign"))
                            {
                                line =Util.StringHook(p,line,true,Text.getInt("SignWeekDay"));
                            } else {
                                line =Util.StringHook(p,line,false,0);
                            }
                            ll.add(line);
                        }
                        inv.addComponent(new VexText( Text.getInt("X"), Text.getInt("Y"), ll));
                    } else {
                        inv.addComponent(new VexText( Text.getInt("X"), Text.getInt("Y"), l));
                    }
                }
            }
            if(FileUtils.vv.getBoolean("Button.Enable"))
            {
                ConfigurationSection ButtonList = (ConfigurationSection)FileUtils.vv.get("Button.List");
                for (String Buttons : ButtonList.getKeys(false))
                {
                    ConfigurationSection Button = FileUtils.vv.getConfigurationSection("Button.List."+Buttons);
                    if(Button.getBoolean("Sign"))
                    {
                        int X = FileUtils.vv.getInt("Sign.Site."+Util.getNowWeek()+".X");
                        int Y = FileUtils.vv.getInt("Sign.Site."+Util.getNowWeek()+".Y");
                        String SignInfo = Util.CheckPlayerSign(p,Util.WeekDate.get(Integer.toString(Util.getNowWeek())));
                        if(SignInfo.equals("NotSign"))
                        {
                            if(Button.getStringList("HoverText").isEmpty())
                            {
                                VexButton vb = new VexButton(Button.getInt("ID") , Button.getString("Name"), Button.getString("Url1"),Button.getString("Url2"),X,Y, Button.getInt("W"), Button.getInt("H"), new ButtonFunction()
                                {
                                    @Override
                                    public void run(Player p)
                                    {
                                        Util.Run(Button.getStringList("Run"),p);
                                        p.closeInventory();
                                        VexViewAPI.openGui(p,Gui(p));
                                    }
                                });
                                inv.addComponent(vb);
                            } else {
                                VexHoverText text = new VexHoverText(Button.getStringList("HoverText"));

                                VexButton vb = new VexButton(Button.getInt("ID") , Button.getString("Name"), Button.getString("Url1"),Button.getString("Url2"),X,Y, Button.getInt("W"), Button.getInt("H"), new ButtonFunction()
                                {
                                    @Override
                                    public void run(Player p)
                                    {
                                        Util.Run(Button.getStringList("Run"),p);
                                        p.closeInventory();
                                        VexViewAPI.openGui(p,Gui(p));
                                    }
                                },text);
                                inv.addComponent(vb);
                            }
                        }
                    } else {
                        if(Button.getStringList("HoverText").isEmpty())
                        {
                            VexButton vb = new VexButton(Button.getInt("ID") , Button.getString("Name"), Button.getString("Url1"),Button.getString("Url2"),Button.getInt("X"),Button.getInt("Y"), Button.getInt("W"), Button.getInt("H"), new ButtonFunction()
                            {
                                @Override
                                public void run(Player p)
                                {
                                    Util.Run(Button.getStringList("Run"),p);
                                }
                            });
                            inv.addComponent(vb);
                        } else {
                            VexHoverText text = new VexHoverText(Button.getStringList("HoverText"));

                            VexButton vb = new VexButton(Button.getInt("ID") , Button.getString("Name"), Button.getString("Url1"),Button.getString("Url2"),Button.getInt("X"),Button.getInt("Y"), Button.getInt("W"), Button.getInt("H"), new ButtonFunction()
                            {
                                @Override
                                public void run(Player p)
                                {
                                    Util.Run(Button.getStringList("Run"),p);
                                }
                            },text);
                            inv.addComponent(vb);
                        }
                    }
                }
            }
            PlayerListener.OpenCheck.put(p.getName(),99999);
            return  inv;
        } else {
        p.sendMessage(Util.cc(FileUtils.lang.getString("NoVexView")));
        return  null;
        }
    }
}
