package goodss.book.dao;

import org.apache.commons.dbutils.QueryRunner;

import cn.itcast.jdbc.TxQueryRunner;

public class BookDao {
	private QueryRunner rq=new TxQueryRunner();
}
