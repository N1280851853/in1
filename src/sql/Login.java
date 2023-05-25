package sql;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Login {
    public static boolean log() {
        try {
            if (Conn.con.isClosed()) {
                Conn.con = DriverManager.getConnection("jdbc:mysql://localhost:3306/books?useUnicode=true&characterEncoding=gbk", Conn.user, Conn.password);
            }
            Statement stmt;
            stmt = Conn.con.createStatement();
            System.out.print("请输入你的登录账户:");
            String account = Borrow.scanner.next();
            ResultSet rs = stmt.executeQuery("select * from login where Account=\"" + account + "\""); //将查询结果放在 ResultSet 集合
            if (rs.next()) {
                int chance = 3;
                while (chance > 0) {
                    chance--;
                    System.out.print("请输入你的密码:");
                    String scPwd = Borrow.scanner.next();
                    String pwd = rs.getString("Password");
                    if (scPwd.equals(pwd)) {  //判断输入的密码和数据库里存的密码是否一致
                        System.out.println("login success");
                        String anno = rs.getString("Annotation");
                        if(anno.equals("管理员")) {
                            User.URL = 1;
                        } else if(anno.equals("普通用户")){
                            User.URL = -1;
                        }
                        return true;
                    } else {
                        System.out.println("你的密码错误,你还有" + chance + "次机会");
                        continue;
                    }
                }
                System.out.println("你的机会已经用完！");
                return false;
            }
            System.out.println("没有查到你的账户!");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }
}


