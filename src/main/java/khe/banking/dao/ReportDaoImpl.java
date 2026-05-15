package khe.banking.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import khe.banking.models.Category;
import khe.banking.models.CategoryReport;
import khe.banking.models.DashboardStats;
import khe.banking.models.MonthlyReport;

public class ReportDaoImpl implements ReportDao {

	@Override
	public List<MonthlyReport> getMonthlyReports(int year) {
		List<MonthlyReport> list = new ArrayList<>();
		String sql = """
				SELECT 
					MONTH(date) as txn_month,
					SUM(
						CASE WHEN type='INCOME' THEN amount ELSE 0
						END
					) AS income,
					SUM(
						CASE WHEN type='EXPENSE' THEN amount ELSE 0
						END
					) AS expense
				FROM transactions
				WHERE YEAR(date)=?
				GROUP BY MONTH(date)
				ORDER BY txn_month""";
		
		try(Connection conn = ConnectDB.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, year);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				BigDecimal income = rs.getBigDecimal("income");
				BigDecimal expense = rs.getBigDecimal("expense");
				MonthlyReport mr = new MonthlyReport(
						Month.of(rs.getInt("txn_month")),
						income,
						expense,
						income.subtract(expense));
				list.add(mr);
			}			
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return list;
	}

	@Override
	public List<CategoryReport> getExpenseByCategory() {
		List<CategoryReport> list = new ArrayList<>();
		String sql = """
				SELECT
					c.name,
					SUM(t.amount) AS total
				FROM transactions t
				JOIN categories c ON t.category_id = c.id
				WHERE t.type='EXPENSE'
				GROUP BY c.name
				ORDER BY total DESC""";
		
		try (Connection conn = ConnectDB.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while(rs.next()) {
				Category c = new Category();
				c.setName(rs.getString("name"));
				
				CategoryReport cr = new CategoryReport(
						c,
						rs.getBigDecimal("total"));
				list.add(cr);
			}			
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return list;
	}

	@Override
	public DashboardStats getDashboardStats() {
		String sql = """
				SELECT
					(SELECT SUM(balance) FROM accounts) AS total_balance,
	
					(SELECT SUM(amount) FROM transactions WHERE type='INCOME') AS income,
	
					(SELECT SUM(amount) FROM transactions WHERE type='EXPENSE') AS expense,
	
					(SELECT COUNT(*) FROM transactions) AS txn_count""";
		
		try (Connection conn = ConnectDB.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			if(rs.next()) {
				return new DashboardStats(
						rs.getBigDecimal("total_balance"),
						rs.getBigDecimal("income"),
						rs.getBigDecimal("expense"),
						rs.getInt("txn_count"));
			}			
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return null;
	}

}
