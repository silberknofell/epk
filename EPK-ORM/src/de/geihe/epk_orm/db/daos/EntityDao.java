package de.geihe.epk_orm.db.daos;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;

public class EntityDao<T> extends BaseDaoImpl<T, Integer> {

	protected EntityDao(ConnectionSource connectionSource, Class<T> dataClass)
			throws SQLException {
		super(connectionSource, dataClass);
		
	}
	
	@Override
	public T queryForId(Integer id) {
		T result = null;
		try {
			result = super.queryForId(id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@Override
	public boolean idExists(Integer id) {
		try {
			return super.idExists(id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public int create(T data) {
		int result = 0;
		try {
			result = super.create(data);
			if (result != 1) {
				System.out
						.println("FEHLER! create ergab mehr als ein Objekt");
			}
			;
		} catch (SQLException e) {		
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public int update(T data) {
		int result = 0;
		try {
			result = super.update(data);
			if (result != 1) {
				System.out
						.println("FEHLER! update ergab mehr als ein Objekt");
			}
			;
		} catch (SQLException e) {			
			e.printStackTrace();
		}
		return result;
	}
	
	@Override
	public int refresh(T data) {
		int result = 0;
		try {
			result = super.refresh(data);
			if (result != 1) {
				System.out
				.println("FEHLER! update ergab mehr als ein Objekt");
			}
			;
		} catch (SQLException e) {			
			e.printStackTrace();
		}
		return result;
	}
	
	@Override
	public int delete(T data) {
		int result = 0;
		try {
			result = super.delete(data);
			if (result != 1) {
				System.out
				.println("FEHLER! delete ergab mehr als ein Objekt");
			}
			;
		} catch (SQLException e) {	
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public List<T> queryForAll() {
		try {
			return super.queryForAll();			
		} catch (SQLException e) {		
			e.printStackTrace();
		}
		return new ArrayList<T>();
	}
	
	@Override
	public List<T> queryForEq(String fieldName, Object value) {
		try {
			return super.queryForEq(fieldName, value);
		} catch (SQLException e) {		
			e.printStackTrace();
		}
		return new ArrayList<T>();
	}


}
