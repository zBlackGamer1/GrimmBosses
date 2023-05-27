package pt.com.GrimmBosses.methods;

import org.bukkit.configuration.file.FileConfiguration;

import pt.com.GrimmBosses.Main;

public class MessagesCfg {
	public static String msgKillBossRecompensa;
	public static String msgKillBoss;
	public static String msgNearbyBoss;
	public static String msgSpawnedBoss;
	public static String msgTPBoss;
	public static String msgInvFull;
	public static String msgColetados;
	public static String msgParaColetar;
	public static String msgNotOwner;

	public static String msgActionBar;

	public static void carregarTodas() {
		FileConfiguration c = Main.getInstance().getConfig();
		msgKillBossRecompensa = r(c.getString("msg-killboss-recompensa"));
		msgKillBoss = r(c.getString("msg-killboss"));

		msgNearbyBoss = r(c.getString("msg-mobperto"));
		msgSpawnedBoss = r(c.getString("msg-bossspawned"));
		msgTPBoss = r(c.getString("msg-bossteleported"));
		msgInvFull = r(c.getString("msg-invfull"));
		msgColetados = r(c.getString("msg-coletados"));
		msgParaColetar = r(c.getString("msg-itensparacoletar"));
		msgNotOwner = r(c.getString("msg-notowner"));

		msgActionBar = r(c.getString("msg-actionbar"));
	}

	static String r(String s) {
		return s.replace("&", "§");
	}
}
