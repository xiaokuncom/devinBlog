package com.devin;

import com.devin.entiy.Staff;
import com.devin.service.StaffService;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.*;
import java.sql.*;
import java.util.Objects;
import java.util.UUID;

@SpringBootTest
class BlobAccessApplicationTests {

    private static final Logger log = LoggerFactory.getLogger(BlobAccessApplicationTests.class);

    @Autowired
    DataSource dataSource;

    @Resource
    private StaffService staffService;

    @Test
    public void testDb() {
        Staff staff = new Staff();
        staff.setName("张三");
        staff.setAge(18);
        staffService.save(staff);
    }

    private static final String tableName = "staff";

    /**
     * 存
     */
    @Test
    void setBlob(){
        // 创建一个员工对象
        Staff staff = new Staff();
        String id = UUID.randomUUID().toString().replace("-", "");
        staff.setId(id);
        staff.setName("Devin");
        staff.setSex("男");
        staff.setAge(18);

        // 读取需要上传的文件
        File file = new File(Objects.requireNonNull(Resource.class.getResource("/data/BlobAccessReadDemo.zip")).getFile());

        try (
            // 获取数据库连接
            Connection connection = dataSource.getConnection();
            // 获取sql对象
             PreparedStatement pstmt = connection.prepareStatement(getInsertSql(tableName, staff));
            // 获取文件输入流
             FileInputStream fis = new FileInputStream(file);
             ) {
            // 读取分段数据并追加到数据库字段中
            pstmt.setBlob(1, fis);
            pstmt.executeUpdate();
            System.out.println("Data insert completed.");
        } catch (SQLException | IOException e) {
            log.error("insert error", e);
        }
    }

    // 构造 INSERT SQL 语句
    private static String getInsertSql(String tableName, Staff staff) {
        return "INSERT INTO " + tableName +  " (id, name, sex, age, data) VALUES "
                + "( '" + staff.getId() + "', '" + staff.getName() + "', '" + staff.getSex() + "'," + staff.getAge() + ", ?)" ;
    }

    /**
     * 取
     */
    @Test
    void getBlob(){
        // 查询条件
        String name = "Devin";
        // 文件列名
        String columnName = "data";
        // 获取输出流
        File outFile = new File(Objects.requireNonNull(Resource.class.getResource("/data")).getFile() + "BlobAccessWriteDemo.zip");

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(getSelectSql(tableName));
        ){
            // 根据条件查询 将指定字段的文件数据写出到指定位置
            ps.setString(1, name);
            ps.executeQuery();
            ResultSet rs = ps.getResultSet();
            rs.next();
            Blob blob = rs.getBlob(columnName);
            InputStream is = blob.getBinaryStream();
            FileUtils.copyInputStreamToFile(is, outFile);

            System.out.println("Data select completed.");
        } catch (Exception e) {
            log.error("select error", e);
        }
    }

    // 构造 SELECT SQL 语句
    private static String getSelectSql(String tableName) {
        return "SELECT * FROM " + tableName +  " WHERE name = " + "?" ;
    }
}
