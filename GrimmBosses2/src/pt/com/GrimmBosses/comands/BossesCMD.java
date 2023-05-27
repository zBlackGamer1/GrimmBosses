package pt.com.GrimmBosses.comands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import pt.com.GrimmBosses.Main;
import pt.com.GrimmBosses.methods.BossType;
import pt.com.GrimmBosses.methods.Matadora;
import pt.com.GrimmBosses.methods.menus.PrincipalMenu;
import pt.com.GrimmBosses.utils.zBUtils;

public class BossesCMD implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command c, String arg2, String[] a) {

		if (!(s instanceof Player)) {
			if (a.length == 0) {
				zBUtils.ConsoleMsg("§6§lGRIMMBOSSES - COMANDOS");
				zBUtils.ConsoleMsg("§6 - §a/boss give (jogador) (tipo) [quantia]");
				zBUtils.ConsoleMsg("§6 - §a/boss matadora (jogador) (matadora)");
				zBUtils.ConsoleMsg("§6 - §a/boss contar");
				zBUtils.ConsoleMsg("§6 - §a/boss ajuda");
				return true;
			}
			Player target = null;
			switch (a[0].toLowerCase()) {
			case "give":
			case "g":
				if (a.length < 3) {
					zBUtils.ConsoleMsg("§cArgumentos inválidos, use /boss give (jogador) (tipo) [quantia]");
					return true;
				}
				target = Bukkit.getPlayerExact(a[1]);
				if (target == null) {
					zBUtils.ConsoleMsg("§cEsse jogador não foi encontrado!");
					return true;
				}
				BossType type = BossType.getByName(a[2]);
				if (type == null) {
					zBUtils.ConsoleMsg("§cTipo de boss não encontrado, lista de tipos:");
					for (BossType types : Main.cache.BossTypes) {
						zBUtils.ConsoleMsg("§6 - §a" + types.getName().toLowerCase());
					}
					return true;
				}
				int q = 1;
				if (a.length == 4) {
					try {
						q = Integer.parseInt(a[3]);
					} catch (NumberFormatException e) {
						zBUtils.ConsoleMsg("§cEssa quantidade não é válida!");
						return true;
					}
				}
				if (q > zBUtils.getEmptySlots(target)) {
					zBUtils.ConsoleMsg("§cEsse jogador não tem espaço no inventário!");
					return true;
				}
				ItemStack it = type.getItemToSpawn().clone();
				it.setAmount(q);
				target.getInventory().addItem(it);
				zBUtils.ConsoleMsg("§aO boss §e" + type.getName() + " §afoi entregue com sucesso!");
				break;

			case "matadora":
			case "m":
				if (a.length != 3) {
					zBUtils.ConsoleMsg("§cArgumentos inválidos, use /boss matadora (jogador) (matadora)");
					return true;
				}
				target = Bukkit.getPlayerExact(a[1]);
				if (target == null) {
					zBUtils.ConsoleMsg("§cEsse jogador não foi encontrado!");
					return true;
				}
				String matadora = a[2];
				if (!Main.cache.allMatadoras.containsKey(matadora)) {
					zBUtils.ConsoleMsg("§c§lERRO! §cMatadora não encontrada, veja todas:");
					for (String m : Main.cache.allMatadoras.keySet()) {
						zBUtils.ConsoleMsg("§6 - §a" + m);
					}
					zBUtils.ConsoleMsg("");
					return true;
				}
				target.getInventory().addItem(Matadora.getByName(matadora).getItem());
				zBUtils.ConsoleMsg("§aMatadora §e" + matadora + " §afoi entregue com sucesso!");
				break;

			case "contar":
			case "count":
				zBUtils.ConsoleMsg(
						"§a§lGRIMMBOSSES! §eEstão atualmente §f" + Main.cache.allBosses.size() + "§e bosses vivos!");
				break;

			case "ajuda":
			case "help":
			case "?":
				zBUtils.ConsoleMsg("§6§lGRIMMBOSSES - COMANDOS");
				zBUtils.ConsoleMsg("§6 - §a/boss give (jogador) (tipo) [quantia]");
				zBUtils.ConsoleMsg("§6 - §a/boss matadora (jogador) (matadora)");
				zBUtils.ConsoleMsg("§6 - §a/boss contar");
				zBUtils.ConsoleMsg("§6 - §a/boss ajuda");
				break;

			default:
				zBUtils.ConsoleMsg("§cArgumento §e" + a[0] + "§c não foi encontrado! (/boss ajuda).");
				break;
			}
			return true;
		}

		Player p = (Player) s;
		if (a.length == 0) {
			new PrincipalMenu(p);
			return true;
		}
		if (!p.hasPermission("grimmbosses.admin")) {
			new PrincipalMenu(p);
			return true;
		}
		Player target = null;
		switch (a[0].toLowerCase()) {
		case "give":
		case "g":
			if (a.length < 3) {
				p.sendMessage("§c§lERRO! §cUse §c§n/boss give (jogador) (tipo) [quantia].");
				return true;
			}
			target = Bukkit.getPlayerExact(a[1]);
			if (target == null) {
				p.sendMessage("§c§lERRO! §cEsse jogador não foi encontrado!");
				return true;
			}
			BossType type = BossType.getByName(a[2]);
			if (type == null) {
				p.sendMessage("§c§lERRO! §cTipo de boss não encontrado, bosses:");
				for (BossType types : Main.cache.BossTypes) {
					p.sendMessage("§6 - §a" + types.getName());
				}
				p.sendMessage("");
				return true;
			}
			int q = 1;
			if (a.length == 4) {
				try {
					q = Integer.parseInt(a[3]);
				} catch (NumberFormatException e) {
					p.sendMessage("§c§lERRO! §cEssa quantia é inválida!");
					return true;
				}
			}
			if (q > zBUtils.getEmptySlots(target)) {
				p.sendMessage("§c§lERRO! §cEsse jogador não tem espaço no inventário!");
				return true;
			}
			ItemStack it = type.getItemToSpawn().clone();
			it.setAmount(q);
			target.getInventory().addItem(it);
			p.sendMessage("§aO boss §e" + type.getName() + " §afoi entregue com sucesso!");
			break;

		case "matadora":
		case "m":
			if (a.length != 3) {
				p.sendMessage("§c§lERRO! §cArgumentos inválidos, use /boss matadora (jogador) (matadora).");
				return true;
			}
			target = Bukkit.getPlayerExact(a[1]);
			if (target == null) {
				p.sendMessage("§c§lERRO! §cEsse jogador não foi encontrado!");
				return true;
			}
			String matadora = a[2];
			if (!Main.cache.allMatadoras.containsKey(matadora)) {
				p.sendMessage("§c§lERRO! §cMatadora não encontrada, veja todas:");
				for (String m : Main.cache.allMatadoras.keySet()) {
					p.sendMessage("§6 - §a" + m);
				}
				p.sendMessage("");
				return true;
			}
			target.getInventory().addItem(Matadora.getByName(matadora).getItem());
			p.sendMessage("§aMatadora §e" + matadora + " §afoi entregue com sucesso!");
			break;

		case "contar":
		case "count":
			p.sendMessage("§a§lGRIMMBOSSES! §eEstão atualmente §f" + Main.cache.allBosses.size() + "§e bosses vivos!");
			break;

		case "help":
		case "ajuda":
		case "?":
			p.sendMessage(new String[] { "§6§lGRIMM BOSSES - COMANDOS",
					"§a - §f/boss give (jogador) (tipo) [quantia]",
					"§a - §f/boss matadora (jogador) (matadora)",
					"§a - §f/boss contar",
					"§a - §f/boss ajuda",
					"" });
			break;
			
		case "ec":
			int ii = 0;
			for(Entity e : p.getNearbyEntities(5.0, 5.0, 5.0)) {
				ii++;
				p.sendMessage(e.getType() + " - " + ii);
			}
			break;
			
		default:
			p.sendMessage("§c§lERRO! §cArgumento §e" + a[0] + "§c não foi encontrado! (/boss ajuda).");
			break;
		}

		return true;
	}
}
