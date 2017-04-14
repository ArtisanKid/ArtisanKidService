package com.artisankid.elementwar.common.dao;

import com.artisankid.elementwar.common.ewmodel.Version;
import com.artisankid.elementwar.common.ewmodel.Version.VersionType;

import java.util.Map;

;

/**
 * Version不具备更新和删除的功能。如果出现错误，那么应该以添加新的Version来覆盖
 * @author LiXiangYu
 *
 */
public class VersionDao {
	/**
	 * 查询最新应用版本
	 * @return
	 */
	public Version selectLatestRelease() {
		String sql = "SELECT * FROM Version WHERE type = '" + VersionType.Release.getValue() + "' ORDER BY id DESC LIMIT 1";
		return this.selectVersionBySQL(sql);
	}
	
	/**
	 * 查询最新资源版本
	 * @return
	 */
	public Version selectLatestResource() {
		String sql = "SELECT * FROM Version WHERE type = '" + VersionType.Resource.getValue() + "' ORDER BY id DESC LIMIT 1";
		return this.selectVersionBySQL(sql);
	}
	
	public Version selectVersionBySQL(String sql) {
		DatabaseManager manager = new DatabaseManager();
		manager.connection();
		Map<String, Object> result = manager.selectOne(sql);
		manager.close();
		
		if(result == null) {
			return null;
		}
				
		Version version = new Version();
		version.setVersionID((String) result.get("versionID"));
		version.setType(VersionType.enumOf((Integer) result.get("type")));
		version.setLatestVersion((String) result.get("latest_version"));
		version.setForceUpdate((Boolean) result.get("force_update"));
		version.setAlert((Boolean) result.get("alert"));
		version.setMessage((String) result.get("message"));
		version.setUrl((String) result.get("url"));
		return version;
	}
	
	/**
	 * 插入新的版本信息
	 * @param version
	 * @return
	 */
	public boolean insert(Version version) {
		String sql = "INSERT INTO Version (versionID, type, latest_version, force_update, alert, message, url) VALUES ('" + version.getVersionID() + "', '" + version.getType().getValue() + "', '" + version.getLatestVersion() + "', '" + version.getForceUpdate() + "', '" + version.getAlert() + "', '" + version.getMessage() + "', '" + version.getUrl()+ "');";
		
		DatabaseManager manager = new DatabaseManager();
		manager.connection();
		boolean result = manager.insert(sql);
		manager.close();
		return result;
	}
}
