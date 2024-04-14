package dbSample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.xml.sax.InputSource;

import com.mysql.cj.exceptions.RSAException;
import com.mysql.cj.protocol.Resultset;
import com.mysql.cj.x.protobuf.MysqlxPrepare.Prepare;

public class DbConnectSample04 {

    public static void main(String[] args) {

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            // 1. ドライバーのクラスをJava上で読み込む
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 2. DBと接続する
            con = DriverManager.getConnection("jdbc:mysql://localhost/world?useSSL=false&allowPublicKeyRetrieval=true",
                    "root", "dir-FY142kotaki");
            String sql = "SELECT * FROM country WHERE Name = ? ";

            // 3. DBとやりとりする窓口（Statementオブジェクト）の作成
            pstmt = con.prepareStatement(sql);

            // 4, 5. Select文の実行と結果を格納／代入
            System.out.println("検索キーワードを入力 ＞");
            String input = keyIn();

            pstmt.setString(1, input);

            rs = pstmt.executeQuery();

            // 6. 結果を表示する
            while (rs.next()) {
                String name = rs.getString("Name");
                int population = rs.getInt("Population");

                System.out.print(name);
                System.out.print(" : ");
                System.out.println(population);
            }

            sql = "update country set Population = 105000 where Code = 'ABW'";
            int count = pstmt.executeUpdate(sql);
            System.out.println(count);

            // 7. 接続を閉じる
        } catch (ClassNotFoundException e) {
            System.out.println("JDBCドライバーのロードに失敗しました。");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("データベースに異常が発生しました。");
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    System.out.println("ResultSetを閉じるときにエラーが発生しました。");
                    e.printStackTrace();
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    System.out.println("Statementを閉じるときにエラーが発生しました。");
                    e.printStackTrace();
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    System.out.println("データベース切断時にエラーが発生しました。");
                    e.printStackTrace();
                }
            }

        }

    }

    private static String keyIn() {
        String line = null;
        try {
            BufferedReader key = new BufferedReader(new InputStreamReader(System.in));
            line = key.readLine();
        } catch (IOException e) {

        }
        return line;
    }

}
