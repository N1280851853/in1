package sql;
import java.io.*;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

@SuppressWarnings({"all"})
public class Borrow { //借还信息管理模块
    static Scanner scanner = new Scanner(System.in);
    static String stuID;
    static String bookName;

    public static boolean scan() {  //输入学号 判断是否存在
        try {
            Statement stmt;
            stmt = Conn.con.createStatement();
            System.out.print("请输入你的学号:");
            stuID = scanner.next();
            ResultSet rs = stmt.executeQuery("select Stu_id from student where Stu_id=\'" + stuID + "\'"); //将查询结果放在 ResultSet 集合
            if(rs.next()) {
                String inquireStuID = rs.getString(1);
                if(stuID.equals(inquireStuID)) {
                    return true;
                }
            }
            System.out.println("你输入的学号不存在！");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public static void borrowBook() { //借书
        Statement stmt;
        try {
            stmt = Conn.con.createStatement();
            System.out.print("请输入你要借的书的名字:");
            bookName = scanner.next();
            ResultSet rs = stmt.executeQuery("select * from book where Name=\"" + bookName + "\""); //将查询结果放在 ResultSet 集合

            if (rs.next()) {
                String borBookID = rs.getString("Book_id");  //获取 Book_id 字段
                int sum = rs.getInt("Inventery");  //得到书的库存
                System.out.println("图书馆中有" + bookName + "这本书,库存还有:" + sum);
                do {
                    System.out.print("你是否要借阅这本书?(y/n):");
                    char c = scanner.next().charAt(0);
                    if (c == 'Y' || c == 'y') {
                        if(sum > 0) {

                            returnBook();
                            LocalDateTime borrowDate = LocalDateTime.now();
                            LocalDateTime returnDate = borrowDate.plusMonths(6); //应还时间
                            ResultSet rs3 = stmt.executeQuery("select * from borrow where Stu_id=\"" + stuID + "\""); //将查询结果放在 ResultSet 集合
                            if(rs3.next()) {  //如果已经存在 学号为 stuID
                                stmt.executeUpdate("update book set Inventery=Inventery-1 where Name=\"" + bookName + "\"");//书的库存-1
                                String stat = bookName + "(借阅中)";

                                String sql5 = "update borrow set " +
                                        "Book_id=\'" + borBookID + "\'," +
                                        "Borrow_time=\'" + borrowDate + "\'," +
                                        "Return_time=\'" + returnDate + "\'," +
                                        "State=\'" + stat + "\' " +
                                        "where Stu_id=\"" + stuID + "\"";
                                stmt.executeUpdate(sql5);
                                stmt.close();
                                System.out.println("borrow success!");
                                return;
                            } else {
                                stmt.executeUpdate("update book set Inventery=Inventery-1 where Name=\"" + bookName + "\"");
                                //书的库存-1

                                String stat = bookName + "(借阅中)";
                                String sql3 = "insert into borrow values (\'" +
                                        stuID + "\',\'" +
                                        borBookID + "\',\'" +
                                        borrowDate + "\',\'" +
                                        returnDate + "\',\'" +
                                        stat + "\')";
                                stmt.executeUpdate(sql3);
                                stmt.close();
                                System.out.println("borrow success!");
                                return;
                            }
                        }
                        System.out.println("该书库存不够，不能借阅");
                        return;
                    } else if (c == 'N' || c == 'n') {
                        return;
                    }
                    System.out.println("sorry，InputError(y/n)");
                } while (true);

            } else {
                System.out.println("不存在图书名称为" + bookName+ "的记录。");
                return;
            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return;
        }
    }

    public static void returnBook() { //还书
        try {
            Statement stmt;
            stmt = Conn.con.createStatement();

            String sql = "select * from borrow where Stu_id=\"" + stuID + "\"";
            ResultSet rs2 = stmt.executeQuery(sql); //将查询结果放在 ResultSet 集合
            if(rs2.next()) {
                String state = rs2.getString("State");
                String retBookID = rs2.getString("Book_id");  //要归还的书的编号
                if(!state.equals("已归还")) {
                    String stat = "已归还";
                    String sql2 = "update borrow set Return_time=null,State=\"" + stat + "\" where Stu_id=\"" + stuID + "\"";
                    stmt.executeUpdate(sql2);

                    String sql3 = "update book set Inventery=Inventery+1 where Book_id=\"" + retBookID + "\"";
                    stmt.executeUpdate(sql3);//书的库存+1
                    System.out.println("return success!");
                    stmt.close();
                    return;
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void renewal() {  //续借
        try {
            Statement stmt;
            stmt = Conn.con.createStatement();
            String sql = "select * from borrow where Stu_id=\"" + stuID + "\"";
            ResultSet rs = stmt.executeQuery(sql); //将查询结果放在 ResultSet 集合

            System.out.print("请输入你要续借的时间(以月为单位):");
            int r = scanner.nextInt();  //保存续借的月份
            if(r < 0 || r >= 6) {
                System.out.println("抱歉，最多只支持续借6个月！");
                return;
            }
            if(rs.next()) {
                String s = rs.getString("State"); //得到状态
                if(!s.equals("已归还")) {
                    String s2 = rs.getString("Return_time");
                    LocalDateTime returnDate =
                            LocalDateTime.parse(s2 , DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    returnDate = returnDate.plusMonths(r); //加上续借日期
                    String sql3 = "update borrow set " +
                            "Return_time=\'" + returnDate + "\' where Stu_id=\"" + stuID + "\"";
                    stmt.executeUpdate(sql3);
                    System.out.println("renewal success!");
                    return;
                }
                System.out.println("你已经归还了，无法续借！");
                return;
            }
            System.out.println("你当前没有借阅的图书！无法续借");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void select() {  //查询逾期的人及 逾期天数
        try {
            Statement stmt;
            stmt = Conn.con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from borrow");
            while(rs.next()) {
                String id = rs.getString("Stu_id");
                String s2 = rs.getString("Return_time");  //保存应还时间
                if(s2 != null) {   //如果 应还时间不为空 则进行判断
                    LocalDateTime returnDate =
                            LocalDateTime.parse(s2 , DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    LocalDateTime now = LocalDateTime.now();  //保存当前的时间
                    if(now.isAfter(returnDate)) { //现在的时间在归还时间之后
                        Duration duration = Duration.between(now,returnDate);
                        long days = duration.toDays(); //相差的天数
                        long hours = duration.toHours();//相差的小时数
                        long minutes = duration.toMinutes();//相差的分钟数
                        System.out.println("已逾期【 "+days+"天："+hours+" 小时："+minutes+" 分钟】");
                        continue;
                    } if(now.isBefore(returnDate)) { //现在的时间在归还时间之前
                        System.out.println("学号为:" + id + " 的学生没有逾期");
                        continue;
                    }
                }
                System.out.println("学号为:" + id + " 的学生已归还图书");  //如果为空，说明 已归还
                continue;
            }
            return;


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    public static void derive() {  //将借阅信息导出到本地
        try {
            Statement stmt = Conn.con.createStatement();

            // 查询 , 从 borrow 表中查询 所有 字段
            ResultSet rs = stmt.executeQuery("SELECT * FROM borrow");

            // 创建输出文件 borrow.txt
            File file = new File("d://borrow.txt");
            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file));
            writer.write("学号 \t图书编号 \t借阅时间 \t归还时间 \t状态\n");
            while (rs.next()) {
                writer.write(String.valueOf(rs.getLong(1)) + " \t");
                writer.write(rs.getString(2) + " \t");
                writer.write(rs.getString(3) + " \t");
                writer.write(rs.getString(4) + " \t");
                writer.write(rs.getString(5));
                writer.write("\r\n");
            }

            writer.flush();
            writer.close();
            System.out.println("derive success!");
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
