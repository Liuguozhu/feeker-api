package org.sidao.jdbc;

import javax.sql.DataSource;

import com.jfinal.plugin.IPlugin;
import com.jfinal.plugin.activerecord.IDataSourceProvider;
import com.jolbox.bonecp.BoneCPDataSource;

public class BonePlugin implements IPlugin, IDataSourceProvider {
	BoneCPDataSource datasource;
	
	public BonePlugin() {
//		ConfigFactory.setDbConfig(dbConfig);
	}
	public DataSource getDataSource() {
		return datasource;
	}

	public boolean start() {
		datasource =ConnectionPool.getConnectionPool();
		return true;
	}

	public boolean stop() {
		datasource.close();
		return true;
	}

}
