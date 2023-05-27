package pt.com.GrimmBosses.utils;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NumberFormatter {
	private static final Pattern PATTERN = Pattern.compile("^(\\d+\\.?\\d*)(\\D+)");
	private final List<String> suffixes;

	public NumberFormatter() {
		this.suffixes = Arrays.asList("", "K", "M", "B", "T", "Q", "QQ", "S", "SS", "O", "N", "D", "UN", "DD", "TD",
				"QT", "QN", "SD", "SPD", "OD", "ND", "VG", "UVG", "DVG", "TVG");
	}

	public NumberFormatter(List<String> suffixes) {
		this.suffixes = suffixes;
	}

	public String formatNumber(double value) {
		int index;
		double tmp;
		for (index = 0; (tmp = value / 1000.0D) >= 1.0D; ++index) {
			value = tmp;
		}

		DecimalFormat decimalFormat = new DecimalFormat("#.##");
		return decimalFormat.format(value).replace(",", ".") + (String) this.suffixes.get(index);
	}

	public double parseString(String value) throws Exception {
		try {
			return Double.parseDouble(value);
		} catch (Exception var7) {
			Matcher matcher = PATTERN.matcher(value.replace(",", "."));
			if (!matcher.find()) {
				throw new Exception("Invalid format");
			} else {
				double amount = Double.parseDouble(matcher.group(1));
				String suffix = matcher.group(2);
				int index = this.suffixes.indexOf(suffix.toUpperCase());
				return amount * Math.pow(1000.0D, (double) index);
			}
		}
	}
}
