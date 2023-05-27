package pt.com.GrimmBosses.listeners;

import java.util.Random;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;

import com.google.common.base.Strings;

import pt.com.GrimmBosses.Main;
import pt.com.GrimmBosses.methods.BPlayer;
import pt.com.GrimmBosses.methods.Boss;
import pt.com.GrimmBosses.methods.Matadora;
import pt.com.GrimmBosses.methods.MessagesCfg;
import pt.com.GrimmBosses.utils.zBUtils;

public class PlayerDamage implements Listener {
	@EventHandler
	void onDamage(EntityDamageByEntityEvent e) {
		Entity entity = e.getEntity();
		if (!(e.getEntityType() == EntityType.ARMOR_STAND)) return;
		if (!(e.getDamager() instanceof Player)) return;
		Boss b = null;
		for(Entity ee : entity.getNearbyEntities(5.0, 5.0, 5.0)) {
			if(ee.getType() == EntityType.ARMOR_STAND) continue;
			if(Main.cache.allBosses.containsKey(ee.getLocation())) {
				b = Main.cache.allBosses.get(ee.getLocation());
				break;
			}
		}
		if (b == null) return;
		Player p = (Player) e.getDamager();
		BPlayer bp = new BPlayer(p);
		if (!bp.isOwner(b)) {
			p.sendMessage(MessagesCfg.msgNotOwner);
			return;
		}
		b.getMob().getNavigator().setHealth(1000);
		if (!Matadora.isMatadora(p.getItemInHand())) return;
		Integer dano = Matadora.getDano(p.getItemInHand());
		if (dano > b.getActualLife()) dano = b.getActualLife();
		b.damage(dano);
		new BPlayer(p).addDano(dano);
		Random ra = new Random();
		if (ra.nextInt(100) <= 6) Impulsar(p);
		if (b.getActualLife() <= 0) {
			b.deleteBoss(p);
			return;
		}
		zBUtils.sendActionBar(p,
				MessagesCfg.msgActionBar.replace("%tipo%", b.getType().getName())
						.replace("%tipom%", b.getType().getName().toUpperCase())
						.replace("%vida%", b.getActualLife() + "").replace("%dano%", dano + "").replace("%progressbar%",
								getProgressBar(b.getActualLife(), b.getType().getLife(), 10, '|', "§a", "§7")));
	}

	String getProgressBar(int current, int max, int totalBars, char symbol, String completedColor,
			String notCompletedColor) {
		float percent = (float) current / max;
		int progressBars = (int) (totalBars * percent);

		return Strings.repeat("" + completedColor + symbol, progressBars)
				+ Strings.repeat("" + notCompletedColor + symbol, totalBars - progressBars);
	}

	void Impulsar(Player p) {
		Vector playerVector = p.getLocation().toVector();
		Vector blockVector = p.getLocation().clone().add(5, 0, 5).toVector();

		double x = playerVector.getX() - blockVector.getX();
		double z = playerVector.getZ() - blockVector.getZ();

		Vector throwVector = new Vector(x, 10, z).normalize().multiply(2);

		p.setVelocity(throwVector);
	}
}
