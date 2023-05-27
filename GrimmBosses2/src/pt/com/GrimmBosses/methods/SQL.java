package pt.com.GrimmBosses.methods;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import pt.com.GrimmBosses.Main;
import pt.com.GrimmBosses.utils.zBUtils;

public class SQL {
	public Connection con = null;
	private String url;
	private String user;
	private String password;

	public void Iniciar() {
		openSQLite();
		String adress = Main.getInstance().getConfig().getString("MySQL.address");
		String database = Main.getInstance().getConfig().getString("MySQL.database");
		url = "jdbc:mysql://" + adress + "/" + database;
		user = Main.getInstance().getConfig().getString("MySQL.username");
		password = Main.getInstance().getConfig().getString("MySQL.password");
		if (openMySQL()) {
			zBUtils.ConsoleMsg("§6[GrimmBosses] §aLigação com MySQL criada com sucesso.");
			loadDados();
		} else {
			if (openSQLite())
				loadDados();
			else
				return;
		}
		AutoSaveTimer();
	}

	public void Encerrar() {
		close();
		saveDados();
	}

	private Boolean openMySQL() {
		if (Main.getInstance().getConfig().getBoolean("MySQL.ativo")) {
			try {
				con = DriverManager.getConnection(url, user, password);
				createtable();
				return true;
			} catch (SQLException e) {
			}
		}
		return false;
	}

	private Boolean openSQLite() {
		File file = new File(Main.getInstance().getDataFolder(), "database.db");
		String url = "jdbc:sqlite:" + file;
		try {
			con = DriverManager.getConnection(url);
			createtable();
			return true;
		} catch (SQLException e) {
			System.out.println(e);
			Main.getInstance().getPluginLoader().disablePlugin(Main.getInstance());
			zBUtils.ConsoleMsg("§6[GrimmBosses] §cNão foi poss§vel conectar com o banco de dados.");
		}
		return false;
	}

	private void close() {
		if (con != null) {
			try {
				con.close();
				con = null;
			} catch (SQLException e) {
			}
		}
	}

	private void createtable() {
		PreparedStatement stm = null;
		try {
			stm = con.prepareStatement(
					"CREATE TABLE IF NOT EXISTS `grimmbplayer` (`jogador` TEXT, `stats` TEXT, `rewards` TEXT)");
			stm.execute();
			stm = con.prepareStatement(
					"CREATE TABLE IF NOT EXISTS `grimmbosses` (`location` TEXT, `type` TEXT, `vida` TEXT, `owner` TEXT)");
			stm.execute();
			stm.close();
		} catch (SQLException e) {
			Main.getInstance().getPluginLoader().disablePlugin(Main.getInstance());
			zBUtils.ConsoleMsg("§6[GrimmBosses] §cOcorreu um erro ao criar tabela no banco de dados.");
		}
	}

	private void loadDados() {
		PreparedStatement stm = null;
		try {
			stm = con.prepareStatement("SELECT * FROM `grimmbplayer`");
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				String nick = rs.getString("jogador");
				Map<StatType, Double> stats = deserializeStats(rs.getString("stats"));
				Map<Recompensa, Double> rewards = deserializeRecompensas(rs.getString("rewards"));

				Main.cache.stats.put(nick, stats);
				Main.cache.recompensas.put(nick, rewards);
			}

			stm = con.prepareStatement("SELECT * FROM `grimmbosses`");
			rs = stm.executeQuery();
			while (rs.next()) {
				Location loc = deserializeLocation(rs.getString("location"));
				Boss b = new Boss(BossType.getByName(rs.getString("type")), loc, rs.getString("owner"));
				b.setLife(rs.getInt("vida"));
				b.spawnBoss();
				Main.cache.allBosses.put(loc, b);
			}
		} catch (SQLException e) {
		}
		close();
	}

	public void saveDados() {
		if (!openMySQL()) {
			openSQLite();
		}
		for (String nick : Main.cache.stats.keySet()) {
			if (!BPTcontains(nick)) {
				criarPlayer(nick);
			} else {
				PreparedStatement stm = null;
				try {
					BPlayer bp = new BPlayer(nick);
					stm = con.prepareStatement("UPDATE `grimmbplayer` SET `stats` = ? WHERE `jogador` = ?");
					stm.setString(2, nick.toLowerCase());
					stm.setString(1, serializeStats(bp));
					stm.executeUpdate();

					stm = con.prepareStatement("UPDATE `grimmbplayer` SET `rewards` = ? WHERE `jogador` = ?");
					stm.setString(2, nick.toLowerCase());
					stm.setString(1, serializeRecompensas(bp));
					stm.executeUpdate();
				} catch (SQLException e) {
				}
			}
		}
		PreparedStatement stm = null;
		try {
			stm = con.prepareStatement("DROP TABLE `grimmbosses`");
			stm.executeUpdate();
			stm = con.prepareStatement(
					"CREATE TABLE IF NOT EXISTS `grimmbosses` (`location` TEXT, `type` TEXT, `vida` TEXT, `owner` TEXT)");
			stm.execute();
		} catch (SQLException e1) {}
		for (Boss b : Main.cache.allBosses.values()) criarBoss(b);
		close();
	}

	private Boolean BPTcontains(String PlayerName) {
		PreparedStatement stm = null;
		Boolean b = false;
		try {
			stm = con.prepareStatement("SELECT * FROM `grimmbplayer` WHERE `jogador` = ?");
			stm.setString(1, PlayerName.toLowerCase());
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				b = true;
			}
		} catch (SQLException e) {
		}
		return b;
	}

	private void criarPlayer(String PlayerName) {
		PreparedStatement stm = null;
		try {
			BPlayer bp = new BPlayer(PlayerName);
			stm = con.prepareStatement("INSERT INTO `grimmbplayer` (`jogador`, `stats`, `rewards`) VALUES (?,?,?)");
			stm.setString(1, bp.getName().toLowerCase());
			stm.setString(2, serializeStats(bp));
			stm.setString(3, serializeRecompensas(bp));

			stm.executeUpdate();
		} catch (SQLException e) {
		}
	}

	private void criarBoss(Boss boss) {
		PreparedStatement stm = null;
		try {
			stm = con.prepareStatement(
					"INSERT INTO `grimmbosses` (`location`, `type`, `vida`, `owner`) VALUES (?,?,?,?)");
			stm.setString(1, serializeLocation(boss.getLocation()));
			stm.setString(2, boss.getType().getName());
			stm.setString(3, boss.getActualLife().toString());
			stm.setString(4, boss.getOwner().toLowerCase());
			stm.executeUpdate();
		} catch (SQLException e) {
		}
	}

	private void AutoSaveTimer() {
		new BukkitRunnable() {

			@Override
			public void run() {
				AutoSave();
			}
		}.runTaskTimer(Main.getInstance(), 1200 * 30L, 1200 * 30L);
	}

	private void AutoSave() {
		saveDados();
		zBUtils.ConsoleMsg("§6[GrimmBosses] §aAuto-Save completo.");
	}

	private String serializeStats(BPlayer bp) {
		return bp.getDano() + "!" + bp.getMortos();
	}

	private String serializeRecompensas(BPlayer bp) {
		String s = "";
		if (bp.getRecompensas().size() == 0)
			return "nada";
		for (Recompensa r : bp.getRecompensas().keySet()) {
			String identifier = r.getIdentifier();
			Double quantia = bp.getRecQuantia(r);
			if (s.equalsIgnoreCase(""))
				s = s + identifier + "!" + quantia;
			else
				s = s + ";" + identifier + "!" + quantia;
		}
		return s;
	}

	private String serializeLocation(Location loc) {
		return loc.getWorld().getName() + ";" + loc.getX() + ";" + loc.getY() + ";" + loc.getZ();
	}

	private Map<StatType, Double> deserializeStats(String string) {
		Map<StatType, Double> map = new HashMap<>();
		String[] sp = string.split("!");
		map.put(StatType.DAMAGE, Double.parseDouble(sp[0]));
		map.put(StatType.KILLED, Double.parseDouble(sp[1]));
		return map;
	}

	private Map<Recompensa, Double> deserializeRecompensas(String string) {
		Map<Recompensa, Double> map = new HashMap<>();
		if (string.equalsIgnoreCase("nada"))
			return map;
		String[] sp = string.split(";");
		for (int i = 0; i < sp.length; i++) {
			String[] s = sp[i].split("!");
			Recompensa r = Recompensa.getByIdentifier(s[0]);
			map.put(r, Double.parseDouble(s[1]));
		}
		return map;
	}

	private Location deserializeLocation(String string) {
		String[] sp = string.split(";");
		return new Location(Bukkit.getWorld(sp[0]), Double.parseDouble(sp[1]), Double.parseDouble(sp[2]),
				Double.parseDouble(sp[3]));
	}
}
