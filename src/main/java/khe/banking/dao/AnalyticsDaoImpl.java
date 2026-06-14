package khe.banking.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.YearMonth;
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
		        WHERE (? = 'ADMIN' OR a.user_id = ?)
				    AND YEARWEEK(t.date, 1) = YEARWEEK(CURDATE(), 1)
		        GROUP BY DAYOFWEEK(t.date), day_name
		        ORDER BY FIELD(day_name, 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun')""";
		
		try(Connection conn = ConnectDB.getConnection();
		        PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, u.getRole().name());
			ps.setInt(2, u.getId());
			ResultSet rs = ps.executeQuery();			
			while(rs.next()) {
				weekly.put(rs.getString("day_name"), rs.getDouble("total"));
			}					
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return weekly;
	}

	@Override
	public Map<String, Double> getWeeklyCashflowByMonth(User u, int year, int month) {
		Map<String, Double> data = initializeMonthWeeks(YearMonth.of(year, month));		
		String sql = """
		        SELECT 
		            WEEK(t.date, 1) - WEEK(DATE_SUB(t.date, INTERVAL DAYOFMONTH(t.date) - 1 DAY), 1) + 1 AS week_number,
		            SUM(CASE WHEN t.type = 'INCOME' THEN t.amount ELSE -t.amount END) AS total
		        FROM transactions t
		        JOIN accounts a ON t.account_id = a.id
		        WHERE (? = 'ADMIN' OR a.user_id = ?) 
				    AND YEAR(t.date) = ?
				    AND MONTH(t.date) = ?
		        GROUP BY week_number
		        ORDER BY week_number""";
		
		try (Connection conn = ConnectDB.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, u.getRole().name());
			ps.setInt(2, u.getId());
			ps.setInt(3, year);
			ps.setInt(4, month);			
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				data.put("Week " + rs.getInt("week_number"), rs.getDouble("total"));
			}			
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return data;
	}
	
	@Override
	public Map<String, Double> getMonthlyTotalByType(User u, TxnType type, int year) {
		Map<String, Double> data = initializeYearlyMap();
		String sql = """
				SELECT
				    DATE_FORMAT(t.date, '%b') AS month_name,
				    MONTH(t.date) AS month_number,
				    SUM(t.amount) AS total
				FROM transactions t
				JOIN accounts a ON t.account_id = a.id
				WHERE (? = 'ADMIN' OR a.user_id = ?)
					AND t.type = ?
					AND YEAR(t.date) = ?
				GROUP BY month_number, month_name
				ORDER BY month_number""";

		try (Connection conn = ConnectDB.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, u.getRole().name());
			ps.setInt(2, u.getId());
			ps.setString(3, type.name());
			ps.setInt(4, year);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				data.put(rs.getString("month_name"), rs.getDouble("total"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return data;
	}
	
	@Override
	public Map<String, Double> getYearlyCashflow(User u, int year) {
		Map<String, Double> data = initializeYearlyMap();
		String sql = """
		        SELECT
		            DATE_FORMAT(t.date, '%b') AS month_name,
		            MONTH(t.date) AS month_number,
		            SUM(CASE WHEN t.type = 'INCOME' THEN t.amount ELSE -t.amount END) AS total
		        FROM transactions t
		        JOIN accounts a ON t.account_id = a.id
		        WHERE (? = 'ADMIN' OR a.user_id = ?) AND YEAR(t.date) = ?
		        GROUP BY month_number, month_name
		        ORDER BY month_number""";
		
		try (Connection conn = ConnectDB.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, u.getRole().name());
			ps.setInt(2, u.getId());
			ps.setInt(3, year);			
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				data.put(rs.getString("month_name"), rs.getDouble("total"));
			}			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return data;
	}
	
	@Override
	public Map<String, Double> getCategoryBreakdown(User u, TxnType type, int year, int month) {
	    Map<String, Double> data = new LinkedHashMap<>();

	    String sql = """
	    		SELECT c.name AS category_name, SUM(t.amount) AS total
		        FROM transactions t
		        JOIN accounts a ON t.account_id = a.id
		        JOIN categories c ON t.category_id = c.id
		        WHERE (? = 'ADMIN' OR a.user_id = ?)
		    		AND t.type = ?
		    		AND YEAR(t.date) = ?
		    		AND (? = 0 OR MONTH(t.date) = ?)
		        GROUP BY c.name
		        ORDER BY total DESC""";

	    try (Connection conn = ConnectDB.getConnection();
	    		PreparedStatement ps = conn.prepareStatement(sql)) {
	    	ps.setString(1, u.getRole().name());
	    	ps.setInt(2, u.getId());
	    	ps.setString(3, type.name());
	    	ps.setInt(4, year);
	    	ps.setInt(5, month);
	    	ps.setInt(6, month);
	    	ResultSet rs = ps.executeQuery();
	    	while (rs.next()) {
	    		data.put(rs.getString("category_name"), rs.getDouble("total"));
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
	
	private Map<String, Double> initializeYearlyMap() {
	    Map<String, Double> map = new LinkedHashMap<>();
	    map.put("Jan", 0.0);
	    map.put("Feb", 0.0);
	    map.put("Mar", 0.0);
	    map.put("Apr", 0.0);
	    map.put("May", 0.0);
	    map.put("Jun", 0.0);
	    map.put("Jul", 0.0);
	    map.put("Aug", 0.0);
	    map.put("Sep", 0.0);
	    map.put("Oct", 0.0);
	    map.put("Nov", 0.0);
	    map.put("Dec", 0.0);
	    return map;
	}

	private Map<String, Double> initializeMonthWeeks(YearMonth ym) {
	    Map<String, Double> map = new LinkedHashMap<>();
	    int weeks = (int) Math.ceil(ym.lengthOfMonth() / 7.0);
	    for (int i = 1; i <= weeks; i++) {
	        map.put("Week " + i, 0.0);
	    }
	    return map;
	}
	

	

}
