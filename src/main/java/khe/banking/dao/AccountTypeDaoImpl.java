package khe.banking.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import khe.banking.models.AccountType;
import khe.banking.models.enums.AccountTypeEnum;

public class AccountTypeDaoImpl implements AccountTypeDao {

	@Override
	public List<AccountType> findAll() {
		List<AccountType> list = new ArrayList<>();
		String sql = "SELECT * FROM account_types ORDER BY id";
		
		try(Connection conn = ConnectDB.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while(rs.next()) {
				AccountType at = buildAccountType(rs);
				list.add(at);
			}			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public boolean add(AccountType o) {
		String sql = """
				INSERT INTO acount_types 
					(code, name, interest_rate, monthly_fee, withdrawal_limit, minimum_balance)
				VALUES (?, ?, ?, ?, ?, ?)""";
		
		try (Connection conn = ConnectDB.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, o.getCode().name());
			ps.setString(2, o.getName());
			ps.setBigDecimal(3, o.getInterestRate());
			ps.setBigDecimal(4, o.getMonthlyFee());
			
			if(o.getWithdrawalLimit() == null) {
				ps.setNull(5, Types.INTEGER);
			} else {
				ps.setInt(5, o.getWithdrawalLimit());
			}
			
			ps.setBigDecimal(6, o.getMinimumBalance());
			return ps.executeUpdate() > 0;			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean update(AccountType o) {
		String sql = """
				UPDATE account_types 
				SET 
					code=?, name=?, interest_rate=?, monthly_fee=?, withdrawal_limit=?, minimum_balance=?
				WHERE id=?""";
		
		try (Connection conn = ConnectDB.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, o.getCode().name());
            ps.setString(2, o.getName());
            ps.setBigDecimal(3, o.getInterestRate());
            ps.setBigDecimal(4, o.getMonthlyFee());

            if (o.getWithdrawalLimit() == null) {
                ps.setNull(5, Types.INTEGER);
            } else {
                ps.setInt(5, o.getWithdrawalLimit());
            }

            ps.setBigDecimal(6, o.getMinimumBalance());
            ps.setInt(7, o.getId());
            return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean delete(AccountType o) {
		String sql = "DELETE FROM account_types WHERE id=?";
		
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
	public AccountType findById(int id) {
		String sql = "SELECT * FROM account_types WHERE id=?";
		
		try(Connection conn = ConnectDB.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, id);
			
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				return buildAccountType(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	
	// HELPER
	private AccountType buildAccountType(ResultSet rs) throws SQLException {
		return new AccountType(
				rs.getInt("id"),
				AccountTypeEnum.valueOf(rs.getString("code")),
				rs.getString("name"),
				rs.getBigDecimal("interest_rate"),
				rs.getBigDecimal("monthy_fee"),
				rs.getObject("withdrawal_limit", Integer.class),
				rs.getBigDecimal("minimum_balance"));				
	}
	
}
