/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2026 Link.it srl (http://www.link.it).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3, as published by
 * the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package utils.java;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

/**
 * Helper minimale per eseguire query SQL dai test Karate.
 * Pattern mutuato dalla testsuite govway (tools/rs/monitor/server/testsuite/.../DbUtils.java).
 *
 * Uso da feature:
 *   * def DbUtils = Java.type('utils.java.DbUtils')
 *   * def db = new DbUtils(govpayDbConfig)
 *   * eval db.update("UPDATE tracciati SET stato='IN_STAMPA' WHERE id=" + idTracciato)
 *   * def size = db.readValue("SELECT length(raw_esito) FROM tracciati WHERE id=" + idTracciato)
 */
public class DbUtils {

	private final JdbcTemplate jdbc;

	public DbUtils(Map<String, Object> config) {
		String url = (String) config.get("url");
		String username = (String) config.get("username");
		String password = (String) config.get("password");
		String driver = (String) config.get("driverClassName");
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(driver);
		dataSource.setUrl(url);
		dataSource.setUsername(username);
		dataSource.setPassword(password);
		this.jdbc = new JdbcTemplate(dataSource);
	}

	public Object readValue(String query) {
		return this.jdbc.queryForObject(query, Object.class);
	}

	public Map<String, Object> readRow(String query) {
		return this.jdbc.queryForMap(query);
	}

	public List<Map<String, Object>> readRows(String query) {
		return this.jdbc.queryForList(query);
	}

	public int update(String query) {
		return this.jdbc.update(query);
	}

	public int update(String query, Object... params) {
		return this.jdbc.update(query, params);
	}

}
