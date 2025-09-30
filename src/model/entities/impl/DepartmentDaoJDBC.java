package model.entities.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import db.DbIntegrityException;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentDaoJDBC implements DepartmentDao {
	private Connection conn;

	public DepartmentDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Department obj) {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement("insert into department (name) value (?)", Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, "name");
			int linhasAfetadas = ps.executeUpdate();
			if (linhasAfetadas > 0) {
				ResultSet rs = ps.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
			} else {
				throw new DbException("erro inesperado, nenhuma linha afectada!");
			}

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatament(ps);
		}

	}

	@Override
	public void update(Department obj) {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement("update department set name =? where id= ?");
			ps.setString(1, obj.getName());
			ps.setInt(2, obj.getId());
			ps.executeUpdate();

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatament(ps);
		}

	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement("delete from department where id = ?");
			ps.setInt(1, id);
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new DbIntegrityException(e.getMessage());
		} finally {
			DB.closeStatament(ps);
		}

	}

	@Override
	public Department findById(Integer id) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("select * from department where id = ?");
			ps.setInt(1, id);
			rs = ps.executeQuery();
			if (rs.next()) {
				Department obj = new Department();
				obj.setId(rs.getInt("Id"));
				obj.setName(rs.getString("Name"));
			}
			return null;

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatament(ps);
			DB.closeResultSet(rs);
		}

	}

	@Override
	public List<Department> findAll() {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("select * from department order by Name");
			rs = ps.executeQuery();
			List<Department> list = new ArrayList<>();
			while (rs.next()) {
				Department obj = new Department();
				obj.setId(rs.getInt("Id"));
				obj.setName(rs.getString("Name"));
				list.add(obj);
			}
			return list;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatament(ps);
			DB.closeResultSet(rs);
		}

	}

}
