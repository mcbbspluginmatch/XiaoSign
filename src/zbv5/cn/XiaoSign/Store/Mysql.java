package zbv5.cn.XiaoSign.Store;
import org.bukkit.entity.Player;
import zbv5.cn.XiaoSign.Utils.FileUtils;
import zbv5.cn.XiaoSign.Utils.Util;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Mysql
{
    public static HashMap<String, String> Mysql_PlayerAllEx = new HashMap<String,String>();
    public static HashMap<String, String> Mysql_PlayerMonthEx = new HashMap<String,String>();
    public static HashMap<String, String> Mysql_PlayerMonth = new HashMap<String,String>();

    public static String getYears()
    {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM");
        return dateFormat.format(date);
    }
    public static void createTable()
    {
        try {
            Connection conn = DriverManager.getConnection(FileUtils.config.getString("Mysql.Url"), FileUtils.config.getString("Mysql.User"), FileUtils.config.getString("Mysql.PassWord"));
            Statement st = conn.createStatement();
            st.execute("CREATE TABLE IF NOT EXISTS `xiaosign` (`player` VARCHAR(40) PRIMARY KEY, `uuid`  VARCHAR(40), `month`  VARCHAR(40), `ex`  VARCHAR(40), `monthex`  VARCHAR(40), `week1`  VARCHAR(40), `week2`  VARCHAR(40), `week3`  VARCHAR(40), `week4`  VARCHAR(40), `week5`  VARCHAR(40), `week6`  VARCHAR(40), `week7`  VARCHAR(40));");
            Util.Print("&e连接&aMysql&e成功!");
        } catch (SQLException e)
        {
            Util.Print("&e连接&aMysql&c失败,具体原因如下:");
            e.printStackTrace();
        }
    }

    public static List<String> getPlayerSign(Player p)
    {
        try {
            Connection conn = DriverManager.getConnection(FileUtils.config.getString("Mysql.Url"), FileUtils.config.getString("Mysql.User"), FileUtils.config.getString("Mysql.PassWord"));
            Statement st = conn.createStatement();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM xiaosign WHERE player=?");
            String sql = "select * from xiaosign where player='" + p.getName() + "' ";
            ResultSet rs = ps.executeQuery(sql);
            while (!rs.next())
            {
                st.execute("INSERT INTO xiaosign VALUES ('" + p.getName() + "', '" + p.getUniqueId() + "', '" + getYears() + "', '0', '0', '0', '0', '0', '0', '0', '0', '0' )");
                Mysql_PlayerAllEx.put(p.getName(),"0");
                Mysql_PlayerMonthEx.put(p.getName(),"0");
                Mysql_PlayerMonth.put(p.getName(),getYears());
                rs.close();
                st.close();
                conn.close();
                return null;
            }
            List<String> Data = new ArrayList<String>();
            for (int i = 1; i < 8; i++)
            {
                String d = "week"+i;
                if (rs.getString(d).equals(Util.WeekDate.get(Integer.toString(i))))
                {
                    Data.add(rs.getString(d));
                }
            }
            Mysql_PlayerAllEx.put(p.getName(),rs.getString("ex"));
            Mysql_PlayerMonthEx.put(p.getName(),rs.getString("monthex"));
            Mysql_PlayerMonth.put(p.getName(),rs.getString("month"));
            rs.close();
            st.close();
            conn.close();
            if(Data.size() == 0)
            {
                return null;
            } else {
                return Data;
            }
        } catch (SQLException e)
        {
            Util.Print("&eMysql&c出现了一点小问题,具体原因如下:");
            e.printStackTrace();
        }
        return null;
    }
    public static void setPlayerSign(Player p,String w,String date)
    {
        w = "week"+w;
        int PlayerAllEx = Integer.parseInt(Mysql_PlayerAllEx.get(p.getName()));
        int PlayerMonthEx = Integer.parseInt(Mysql_PlayerMonthEx.get(p.getName()));
        try {
            Connection conn = DriverManager.getConnection(FileUtils.config.getString("Mysql.Url"), FileUtils.config.getString("Mysql.User"), FileUtils.config.getString("Mysql.PassWord"));
            Statement st = conn.createStatement();
            PreparedStatement ps = conn.prepareStatement("UPDATE xiaosign SET " + w + " = ? WHERE player=?");
            ps.setString(1, date);
            ps.setString(2, p.getName());
            ps.executeUpdate();

            PlayerAllEx = PlayerAllEx +1;
            ps = conn.prepareStatement("UPDATE xiaosign SET ex = ? WHERE player=?");
            ps.setString(1, Integer.toString(PlayerAllEx));
            ps.setString(2, p.getName());
            ps.executeUpdate();

            ResultSet rs = ps.executeQuery("select * from xiaosign where player='" + p.getName() + "' ");
            String SqlMonth = Mysql_PlayerMonth.get(p.getName());
            if(!SqlMonth.equals(getYears()))
            {
                ps = conn.prepareStatement("UPDATE xiaosign SET month = ? WHERE player=?");
                ps.setString(1, getYears());
                ps.setString(2, p.getName());
                ps.executeUpdate();

                ps = conn.prepareStatement("UPDATE xiaosign SET monthex = ? WHERE player=?");
                ps.setString(1, "1");
                ps.setString(2, p.getName());
                ps.executeUpdate();
            } else {
                PlayerMonthEx = PlayerMonthEx+1;
                ps = conn.prepareStatement("UPDATE xiaosign SET monthex = ? WHERE player=?");
                ps.setString(1, Integer.toString(PlayerMonthEx));
                ps.setString(2, p.getName());
                ps.executeUpdate();
            }
            rs.close();
            st.close();
            ps.close();
            conn.close();
        } catch (SQLException e)
        {
            Util.Print("&eMysql&c出现了一点小问题,具体原因如下:");
            e.printStackTrace();
        }
    }
}
