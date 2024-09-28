package rs.antileaf.alice.utils;

import rs.antileaf.alice.AliceMargatroidMod;
import rs.antileaf.alice.strings.AliceTargetIconStrings;
import rs.antileaf.alice.targeting.AliceTargetIcon;

import java.util.ArrayList;

public class AliceTargetIconTipHelper {
	public static String getIcon(AliceTargetIcon icon) {
		return "[" + AliceMargatroidMod.SIMPLE_NAME.toLowerCase() + ":" + icon.id + "Icon]";
	}

	public static String getIconAndName(AliceTargetIcon icon) {
		return getIcon(icon) + " " + AliceTargetIconStrings.DETAILS.get(icon.id);
	}

	public static String generateTip(ArrayList<AliceTargetIcon> icons) {
		return String.format(AliceTargetIconStrings.DESCRIPTION + " NL ",
				icons.stream()
						.map(AliceTargetIconTipHelper::getIconAndName)
						.reduce((a, b) -> a + " NL " + b)
						.orElse(" [MISSING ICON DESCRIPTION] ")
		);
	}

//	public static void render(SpriteBatch sb, AbstractAliceCard card) {
//		if (card.iconsHb.hovered && AliceConfigHelper.enableCardTargetIcons()) {
//			AliceSpireKit.logger.info("Hovered over target icons.");
//
//			if (card.current_x < Settings.WIDTH / 2.0F)
//				TipHelper.queuePowerTips(
//						card.iconsHb.cX + 20.0F * Settings.scale,
//						card.iconsHb.cY + TipHelper.calculateAdditionalOffset(card.iconsTips, card.iconsHb.cY),
//						card.iconsTips
//				);
//			else
//				TipHelper.queuePowerTips(
//						card.iconsHb.cX - 380.0F * Settings.scale,
//						card.iconsHb.cY + TipHelper.calculateAdditionalOffset(card.iconsTips, card.iconsHb.cY),
//						card.iconsTips
//				);
//		}
//	}
}
