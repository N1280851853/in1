package sql;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

@SuppressWarnings({"All"})
public class StuCRUD {  //学生表的 CRUD 模块
    static Scanner scanner = new Scanner(System.in);
    static String tableName1 = "student";  //表示表名
    public static void add() {
        Statement stmt;
        try {

            System.out.print("请输入你要插入的学生学号:");
            String newID = scanner.nextLine(); //学生学号 不能为空

            System.out.print("请输入您要插入的学生姓名:");
            String newName = scanner.next(); //新的姓名

            System.out.print("请输入您要插入的学生性别:");
            char newGender = scanner.next().charAt(0); //新的性别

            System.out.print("请输入您要插入的学生年龄:");
            int newAge = Integer.parseInt(scanner.next());  //新的年龄

            System.out.print("请输入您要插入的学生电话号码:");
            String newTel = scanner.next();
            String sql = "insert into " + tableName1 + " values (\'" +
                    newID + "\',\'" +
                    newName + "\',\'" +
                    newGender + "\',\'" +
                    newAge + "\',\'" +
                    newTel + "\')";
            stmt = Conn.con.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
            System.out.println("insert success!");
            return;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void delete() {
        Statement stmt;
        try {
            stmt = Conn.con.createStatement();
            Borrow.scan();
            String sql = "delete from " + tableName1 + " where Stu_id=" + Borrow.stuID;  //根据id来进行删除
            stmt.executeUpdate(sql);
            stmt.close();
            System.out.println("delete success!");
            return;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void upDate() { //更新
        Statement stmt;
        try {

            System.out.print("请输入你要替换的学生学号:");
            String oldID = scanner.next(); //学生学号

            System.out.print("请输入你要更新的学生学号:");
            String newID = scanner.next(); //学生学号

            System.out.print("请输入您要更新的学生姓名:");
            String newName = scanner.next(); //新的姓名

            System.out.print("请输入您要更新的学生性别:");
            char newGender = scanner.next().charAt(0); //新的性别

            System.out.print("请输入您要更新的学生年龄:");
            int newAge = scanner.nextInt();  //新的年龄

            System.out.print("请输入您要更新的学生电话号码:");
            String newTel = scanner.next();

            stmt = Conn.con.createStatement();
            String sql = "update " + tableName1 + " set Stu_id=\'" + newID + "\'," +
                    "name=\'" + newName +"\'," +
                    "gender=\'" + newGender + "\'," +
                    "age=\'" + newAge + "\'," +
                    "Stu_tel=\'" + newTel + "\'" +
                    " where Stu_id=\'" + oldID + "\'";
            stmt.executeUpdate(sql);
            stmt.close();
            System.out.println("change success!");
            return;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }



    public static void showStudent() {
        Statement stmt;
        String sql = "select * from " + tableName1;    //要执行的SQL
        try {

            stmt = Conn.con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);//创建数据对象
            System.out.println("学号 \t姓名 \t性别 \t年龄 \t电话号码");
            while (rs.next()) {
                System.out.print(rs.getInt(1) + " \t");
                System.out.print(rs.getString(2) + " \t");
                System.out.print(rs.getString(3) + "\t\t");
                System.out.print(rs.getInt(4) + "\t\t");
                System.out.print(rs.getString(5));
                System.out.println();
            }
            rs.close();
            stmt.close();
            return;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


}
