package com.artisankid.elementwar.common.dao;

import com.mysql.jdbc.ResultSetMetaData;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.sql.Date;
import java.util.*;

public class DatabaseManager {
	Connection connection = null;
	Statement statement = null;
	
    public Connection connection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");

            connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/ElementWar", "root", "123456");
            statement = connection.createStatement(); //创建Statement对象
			statement.executeUpdate("SET NAMES UTF8MB4");
            //System.out.println("成功连接数据库！");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
    
    public List<Map<String, Object>> select(String sql) {
    	//System.out.println(sql);
    	
    	List<Map<String, Object>> result = new ArrayList<>();
    	try {
			ResultSet set = statement.executeQuery(sql);
			ResultSetMetaData metaData = (ResultSetMetaData) set.getMetaData();
			int columnCount = metaData.getColumnCount();
			while (set.next()) {
				Map<String, Object> map = new HashMap<>();
				//草泥马，数据库取名称和取值神马的都是从1开始的
				for (int i = 1; i <= columnCount; i++) {
					String key = metaData.getColumnName(i); 
					Object value = set.getObject(i);
					map.put(key, value);
				}
				result.add(map);
			}
			set.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	return result;
	}
    
    public Map<String, Object> selectOne(String sql) {    	
    	List<Map<String, Object>> result = this.select(sql);
    	int size = result.size();
		if(size == 0) {
			return null;
		}

		Map<String, Object> map = result.get(size - 1);
    	return map;
	}

    public boolean update(String sql, StatementHandler handler) {
    	//System.out.println(sql);

    	boolean result = false;
    	try {
    		PreparedStatement statement = connection.prepareStatement(sql);
			handler.supplyToStatement(statement);
			int count = statement.executeUpdate();
			//System.out.println("更新" + count + "条数据");
			if(count > 0) {
				result = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	return result;
	}

	public boolean insert(String sql) {
		//System.out.println(sql);

		boolean result = false;
		try {
			int count = statement.executeUpdate(sql);
			//System.out.println("插入" + count + "条数据");
			if(count > 0) {
				result = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

    public boolean update(String sql) {
    	//System.out.println(sql);

    	boolean result = false;
    	try {
			int count = statement.executeUpdate(sql);
			//System.out.println("更新" + count + "条数据");
			if(count > 0) {
				result = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	return result;
	}
    
    public boolean delete(String sql) {
    	//System.out.println(sql);
    	
    	boolean result = false;
    	try {
			int count = statement.executeUpdate(sql);
			//System.out.println("删除" + count + "条数据");
			if(count > 0) {
				result = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	return result;
	}

    public void close() {
        try {
            if (statement != null) {
            	statement.close();
            	statement = null;
            }
            if (connection != null) {
            	connection.close();
            	connection = null;
            }
            
            //System.out.println("成功关闭数据库！");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}