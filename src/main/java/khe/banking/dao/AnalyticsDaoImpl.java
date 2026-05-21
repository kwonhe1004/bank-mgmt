package khe.banking.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

import khe.banking.models.User;
import khe.banking.models.enums.TxnType;

public class AnalyticsDaoImpl implements AnalyticsDao {

	@Override
	public Map<String, Double> getWeeklyCashflow(User u) {
		Map<String, Double> weekly = initializeWeeklyMap();
		String sql = """
				SELECT
					DATE_FORMAT(t.date, '%a') AS day_name,
					SUM(CASE WHEN t.type = 'INCOME' THEN t.amount ELSE -t.amount END) AS total
				FROM transactions t
				JOIN accounts a ON t.account_id = a.id
				WHERE a.user_id = ?
					AND t.date >= DATE_SUB(CURDATE(), INTERVAL 7 DAY)
	            GROUP BY DAYOFWEEK(t.date), DAYNAME(t.date)
	            ORDER BY DAYOFWEEK(t.date)""";
		
		try(Connection conn = ConnectDB.getConnection();
		        PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, u.getId());
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				weekly.put(
						rs.getString("day_name"), 
						rs.getDouble("total"));
			}					
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return weekly;
	}

	@Override
	public Map<String, Double> getMonthlyCashflow(User u) {
		Map<String, Double> monthly = new LinkedHashMap<>();		
		String sql = """
				SELECT 
					WEEK(t.date) - WEEK(DATE_SUB(
						t.date, INTERVAL DAYOFMONTH(t.date)-1 DAY), 1) + 1 AS week_number,
					SUM(CASE WHEN t.type = 'INCOME' THEN t.amount ELSE -t.amount END) AS total
				FROM transactions t
				JOIN accounts a ON t.account_id = a.id
				WHERE a.user_id = ?
					AND MONTH(t.date) = MONTH(CURDATE())
					AND YEAR(t.date) = YEAR(CURDATE())
				GROUP BY week_number
				ORDER BY week_number""";
		
		try(Connection conn = ConnectDB.getConnection();
		        PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, u.getId());
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				monthly.put(
						"Week " + rs.getInt("week_number"), 
						rs.getDouble("total"));
			}						
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return monthly;
	}

	@Override
	public Map<String, Double> getCategoryBreakdown(User u, TxnType t) {
		Map<String, Double> data = new LinkedHashMap<>();
		String sql = """
				SELECT 
					c.name AS category_name,
					SUM(t.amount) AS total
				FROM transactions t
				JOIN accounts a ON t.account_id = a.id
				JOIN categories c ON t.category_id = c.id
				WHERE a.user_id = ? 
					AND t.type = ?
					AND MONTH(t.date) = MONTH(CURDATE())
				    AND YEAR(t.date) = YEAR(CURDATE())
				GROUP BY c.name
				ORDER BY total DESC""";
		
		try(Connection conn = ConnectDB.getConnection();
		        PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, u.getId());
			ps.setString(2, t.name());
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				data.put(
						rs.getString("category_name"), 
						rs.getDouble("total"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return data;
	}
	
	private Map<String, Double> initializeWeeklyMap() {
		Map<String, Double> map = new LinkedHashMap<>();
		map.put("Mon", 0.0);
        map.put("Tue", 0.0);
        map.put("Wed", 0.0);
        map.put("Thu", 0.0);
        map.put("Fri", 0.0);
        map.put("Sat", 0.0);
        map.put("Sun", 0.0);
        return map;
	}

	

}
