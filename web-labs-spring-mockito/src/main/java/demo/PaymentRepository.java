package demo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.sql.DataSource;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import demo.AccountModel.Currency;

@Repository
public class PaymentRepository {

	private final NamedParameterJdbcTemplate template;

	private static final RowMapper<UserModel> userMapper = new RowMapper<UserModel>() {

		@Override
		public UserModel mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
			return new UserModel(resultSet.getString("id"),
					resultSet.getString("firstName"),
					resultSet.getString("lastName"),
					resultSet.getString("email"),
					resultSet.getString("password"));
		}
	};
	
	private static final RowMapper<AccountModel> accountMapper = new RowMapper<AccountModel>() {

		@Override
		public AccountModel mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
			return new AccountModel(resultSet.getString("id"),
					resultSet.getString("user_id"),
					Currency.valueOf(resultSet.getString("currency")),
					resultSet.getDouble("amount"));
		}
	};
	
	private static final RowMapper<PaymentModel> paymentMapper = new RowMapper<PaymentModel>() {

		@Override
		public PaymentModel mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
			return new PaymentModel(resultSet.getString("id"),
					resultSet.getString("sender_account_id"),
					resultSet.getString("receiver_account_id"),
					Currency.valueOf(resultSet.getString("currency")),
					resultSet.getDouble("amount"));
		}
	};
	
    public PaymentRepository(final DataSource dataSource) {
        template = new NamedParameterJdbcTemplate(dataSource);
    }
    
    public Optional<UserModel> getUser(String email) {
    	String sql = "select id, first_name, last_name, email, password from users "
    			+ "where email = :email";
    	MapSqlParameterSource parameters = new MapSqlParameterSource()
				.addValue("email", email);
    	return template.query(sql, parameters, userMapper).stream().findFirst();
    }
    
    public List<AccountModel> getUserAccounts(String userId) {
    	String sql = "select id, user_id, currency, amount from users "
    			+ "where user_id = :user_id";
    	MapSqlParameterSource parameters = new MapSqlParameterSource()
				.addValue("user_id", userId);
    	return template.query(sql, parameters, accountMapper);
    }
    
    public List<PaymentModel> getPaymentsSentByUser(String userId) {
    	String sql = "select id, sender_account_id, receiver_account_id, accounts.currency as currency, amount from payments "
    			+ "inner join accounts on sender_account_id = accounts.id "
    			+ "where sender_account_id = :sender_account_id";
    	MapSqlParameterSource parameters = new MapSqlParameterSource()
				.addValue("sender_account_id", userId);
    	return template.query(sql, parameters, paymentMapper);
    }
    
    public List<PaymentModel> getPaymentsReceivedByUser(String userId) {
    	String sql = "select id, sender_account_id, receiver_account_id, accounts.currency as currency, amount from payments "
    			+ "inner join accounts on receiver_account_id = accounts.id "
    			+ "where receiver_account_id = :receiver_account_id";
    	MapSqlParameterSource parameters = new MapSqlParameterSource()
				.addValue("receiver_account_id", userId);
    	return template.query(sql, parameters, paymentMapper);
    }
    
    public boolean savePayment(PaymentModel payment) {
    	String sql = "insert into payments (id, sender_account_id, receiver_account_id, amount) "
    			+ "values (:id, :sender_account_id, :receiver_account_id, :amount)";
    	MapSqlParameterSource parameters = new MapSqlParameterSource()
				.addValue("id", UUID.randomUUID().toString())
				.addValue("sender_account_id", payment.getSenderAccountId())
				.addValue("receiver_account_id", payment.getReceiverAccountId())
				.addValue("amount", payment.getAmount());
    	if (template.update(sql, parameters) != 1) {
    		return false;
    	}
    	sql = "update accounts set amount = amount - :amount "
    			+ "where id = :id";
    	parameters = new MapSqlParameterSource()
				.addValue("id", payment.getSenderAccountId());
    	return template.update(sql, parameters) == 1;
    }
    
    public boolean transferMoney(String fromAccountId,
    		String toAccountId,
    		double fromAmount,
    		double toAmount) {
    	String fromSql = "update accounts "
    			+ "set amount = amount - :amount "
    			+ "where id = :id";
    	MapSqlParameterSource fromParameters = new MapSqlParameterSource()
    			.addValue("id", fromAccountId)
    			.addValue("amount", fromAmount);
    	String toSql = "update accounts "
    			+ "set amount = amount + :amount "
    			+ "where id = :id";
    	MapSqlParameterSource toParameters = new MapSqlParameterSource()
    			.addValue("id", toAccountId)
    			.addValue("amount", toAmount);
    	return template.update(fromSql, fromParameters) == 1
    			&& template.update(toSql, toParameters) == 1;
    }
    
    public boolean saveUser(UserModel user) {
    	String sql = "update users set "
    			+ "first_name = :first_name, "
    			+ "last_name = :last_name, "
    			+ "password = :password "
    			+ "where id = :id";
    	MapSqlParameterSource parameters = new MapSqlParameterSource()
    			.addValue("id", user.getId())
    			.addValue("first_name", user.getFirstName())
    			.addValue("last_name", user.getLastName())
				.addValue("email", user.getEmail())
				.addValue("password", user.getPassword());
    	if (template.update(sql, parameters) == 1) {
    		return true;
    	} else {
    		sql = "insert into users (id, first_name, last_name, email, password) "
    				+ "values (:id, :first_name, :last_name, :email, :password)";
    		return template.update(sql, parameters) == 1;
    	}
    }
    
    public boolean removeUser(String email) {
    	String sql = "delete from users where email = :email";
    	MapSqlParameterSource parameters = new MapSqlParameterSource()
				.addValue("email", email);
    	return template.update(sql, parameters) == 1;
    }
    
    public boolean saveAccount(AccountModel account) {
    	String sql = "update accounts set "
    			+ "amount = :amount, "
    			+ "where id = :id";
    	MapSqlParameterSource parameters = new MapSqlParameterSource()
    			.addValue("id", account.getId())
    			.addValue("user_id", account.getUserId())
    			.addValue("currency", account.getCurrency())
    			.addValue("amount", account.getId());
    	if (template.update(sql, parameters) == 1) {
    		return true;
    	} else {
    		sql = "insert into accounts (id, user_id, currency, amount) "
    				+ "values (:id, :user_id, :currency, :amount)";
    		return template.update(sql, parameters) == 1;
    	}
    }
    
    public boolean removeAccount(String userId, Currency currency) {
    	String sql = "delete from accounts "
    			+ "where user_id = :user_id and currency = :currency";
    	MapSqlParameterSource parameters = new MapSqlParameterSource()
				.addValue("user_id", userId)
				.addValue("currency", currency);
    	return template.update(sql, parameters) == 1;
    }
}
