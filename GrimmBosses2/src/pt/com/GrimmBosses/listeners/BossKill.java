package pt.com.GrimmBosses.listeners;

import java.util.Random;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import pt.com.GrimmBosses.Main;
import pt.com.GrimmBosses.methods.BPlayer;
import pt.com.GrimmBosses.methods.Boss;
import pt.com.GrimmBosses.methods.BossKillEvent;
import pt.com.GrimmBosses.methods.MessagesCfg;
import pt.com.GrimmBosses.methods.Recompensa;

public class BossKill implements Listener {
	@EventHandler
	void onKill(BossKillEvent e) {
		Player p = e.getKiller();
		if (p == null)
			return;
		Boss boss = e.getBoss();
		BPlayer bp = e.getBPlayer();
		int recompensas = giveRecompensas(bp, boss);
		bp.addMortos();
		boss.getMob().remove();
		boss.getHolograma().delete();
		Main.cache.allBosses.remove(boss.getLocation());
		if (recompensas == 0)
			p.sendMessage(MessagesCfg.msgKillBoss);
		else
			p.sendMessage(MessagesCfg.msgKillBossRecompensa.replace("%tipo%", boss.getType().getName()).replace("%quantia%", recompensas + ""));
	}

	int giveRecompensas(BPlayer bp, Boss b) {
		int i = 0;
		for (Recompensa r : b.getType().getRecompensas()) {
			if (r.getChance() >= 1000) {
				bp.addRecompensa(r, 1.0);
				i++;
			} else {
				Random random = new Random();
				if (random.nextInt(1000) <= r.getChance()) {
					bp.addRecompensa(r, 1.0);
					i++;
				}
			}
		}
		bp.refreshMap();
		return i;
	}
}
