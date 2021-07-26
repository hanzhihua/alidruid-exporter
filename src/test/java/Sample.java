import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.stat.JdbcStatManager;
import com.alibaba.druid.util.JdbcConstants;

import javax.management.JMException;
import java.sql.*;

public class Sample {

    public static void main(String[] args) throws SQLException, JMException {
        DruidDataSource dataSource = new DruidDataSource();

        dataSource.setUrl("jdbc:mysql://localhost:3306/kucun3?useUnicode=true&characterEncoding=utf8&allowMultiQueries=true&autoReconnect=true&rewriteBatchedStatements=true&rewriteBatchedStatements=true&useServerPrepStmts=true&allowMultiQueries=true&connectTimeout=10000&socketTimeout=20000");
        dataSource.setFilters("stat");
        dataSource.setDbType(JdbcConstants.MYSQL);
        dataSource.setUsername("root");
        dataSource.setPassword("root");
        dataSource.init();
        Connection connection = dataSource.getConnection();
        PreparedStatement stmt = connection.prepareStatement("select ?");
        stmt.setNull(1, Types.VARCHAR);
        ResultSet rs = stmt.executeQuery();
        while(rs.next()){
            System.out.println(rs.getObject(1));
        }
        JdbcStatManager.getInstance().getSqlList();
//        JdbcStatManager.getInstance().getConnectionStat()
        System.out.println(connection);
    }
}
