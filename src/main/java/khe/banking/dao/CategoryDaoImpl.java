package khe.banking.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import khe.banking.models.Category;
import khe.banking.models.enums.TxnType;

public class CategoryDaoImpl implements CategoryDao {

	@Override
	public List<Category> findAll() {
		List<Category> list = new ArrayList<>();
		String sql = "SELECT * FROM categories ORDER BY name";

		try (Connection conn = ConnectDB.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				Category c = buildCategory(rs);
				list.add(c);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public boolean add(Category o) {
		String sql = "INSERT INTO categories (name, type) VALUES (?, ?)";

		try (Connection conn = ConnectDB.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, o.getName());
			ps.setString(2, o.getType().name());
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean update(Category o) {
		String sql = "UPDATE categories SET name=?, type=? WHERE id=?";

		try (Connection conn = ConnectDB.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, o.getName());
			ps.setString(2, o.getType().name());
			ps.setInt(3, o.getId());
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean delete(Category o) {
		String sql = "DELETE FROM categories WHERE id=?";

		try (Connection conn = ConnectDB.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, o.getId());
			return ps.executeUpdate() > 0;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public List<Category> findByType(String type) {
		List<Category> list = new ArrayList<>();
		String sql = "SELECT * FROM categories WHERE type=? ORDER BY name";

		try (Connection conn = ConnectDB.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, type);

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Category c = buildCategory(rs);
				list.add(c);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public Category findById(int id) {
		String sql = "SELECT * FROM categories WHERE id=?";

		try (Connection conn = ConnectDB.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, id);

			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return buildCategory(rs);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	// HELPER
	private Category buildCategory(ResultSet rs) throws SQLException {

		return new Category(rs.getInt("id"),

				rs.getString("name"),

				TxnType.valueOf(rs.getString("type")));
	}

}
