package sql;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class User {
    static Scanner scanner = new Scanner(System.in);
    static int URL;
    public static void main(String[] args) throws SQLException{ // 主方法，测试连接
        Conn c = new Conn(); // 创建Conn对象

        System.out.println("");

        c.getConnection(); // 调用连接数据库的方法
        while(Login.log()) {
            switch (URL) {
                case 1 :  mainMenu();  //进入管理员菜单
                return;
                case -1 : common();    //进入普通用户菜单
                return;

            }
        }
    }

    public static void mainMenu() throws SQLException {
        do {
            System.out.println("==================管理员菜单==================");
            System.out.println("1. 对student表进行增、删、改、查");
            System.out.println("2. 对book表进行增、删、改、查");
            System.out.println("3. 进行借、还操作");
            System.out.println("4. 查询逾期的学生");
            System.out.println("5. 将借阅信息导出");
            System.out.println("6. 退出");
            System.out.print("您要对那个表进行操作:");
            int i = scanner.nextInt();
            switch (i) {
                case 1 : stu();    //进入学生菜单
                    continue;
                case 2: book();    //进入图书菜单
                    continue;
                case 3: borrow();  //进行借阅操作
                    continue;
                case 4: Borrow.select();
                    continue;
                case 5: Borrow.derive();
                    continue;
                case 6: Conn.con.close();
                    return;

                default : System.out.println("选择有误，请重新选择");
            }
        } while (true);
    }

    public static void common() throws SQLException{
        do {
            System.out.println("==================普通用户菜单==================");
            System.out.println("1. 查询馆内书籍");
            System.out.println("2. 进行借、还操作");
            System.out.println("3. 退出");
            System.out.print("您要对那个表进行操作:");
            int i = scanner.nextInt();
            switch (i) {
                case 1 : BookCRUD.showStudent();
                    continue;
                case 2: borrow();
                    continue;
                case 3: Conn.con.close();
                    return;
                default : System.out.println("选择有误，请重新选择");
            }
        } while (true);
    }


    public static void stu() { //对student 表进行操作
        do {
            System.out.println("==================学生(Student)菜单==================");
            System.out.println("\t\t\t1：增添记录");
            System.out.println("\t\t\t2：删除记录");
            System.out.println("\t\t\t3：修改记录");
            System.out.println("\t\t\t4：显示记录");
            System.out.println("\t\t\t5：退出");
            System.out.print("请输入你要进行的操作：");
            int n = scanner.nextInt();
            switch (n) {
                case 1 :
                    StuCRUD.add();
                    continue;
                case 2 :
                    StuCRUD.delete();
                    continue;
                case 3 :
                    StuCRUD.upDate();
                    continue;
                case 4 :
                    StuCRUD.showStudent();
                    continue;
                case 5 :
                    return;
                default : System.out.println("选择有误，请重新选择");
            }
        } while (true);
    }

    public static void book() {
        do {
            System.out.println("==================图书(Book)菜单==================");
            System.out.println("\t\t\t1：增添记录");
            System.out.println("\t\t\t2：删除记录");
            System.out.println("\t\t\t3：修改记录");
            System.out.println("\t\t\t4：显示记录");
            System.out.println("\t\t\t5：退出");
            System.out.print("请输入你要进行的操作：");
            int n = scanner.nextInt();
            switch (n) {
                case 1 :
                    BookCRUD.add();
                    continue;
                case 2 :
                    BookCRUD.delete();
                    continue;
                case 3 :
                    BookCRUD.upDate();
                    continue;
                case 4 :
                    BookCRUD.showStudent();
                    continue;
                case 5 :
                    return;
                default : System.out.println("选择有误，请重新选择");
            }
        } while (true);
    }

    public static void borrow() {
        if(!Borrow.scan()) {
            return;
        }
        do {
            System.out.println("==================借阅(Borrow)菜单==================");
            System.out.println("\t\t\t1：借阅");
            System.out.println("\t\t\t2：归还图书");
            System.out.println("\t\t\t3：续借");
            System.out.println("\t\t\t4：退出");
            System.out.print("请输入你要进行的操作：");
            int n = scanner.nextInt();
            switch (n) {
                case 1:
                    Borrow.borrowBook();
                    continue;
                case 2:
                    Borrow.returnBook();
                    continue;
                case 3:
                    Borrow.renewal();
                    continue;
                case 4:
                    return;
                default : System.out.println("选择有误，请重新选择");
            }
        } while(true);
    }
}
