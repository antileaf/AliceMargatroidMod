package rs.antileaf.alice.cards.Marisa;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import rs.antileaf.alice.cards.AbstractAliceCard;
import rs.antileaf.alice.utils.AliceConfigHelper;
import rs.antileaf.alice.utils.AliceSpireKit;

public abstract class AbstractAliceMarisaCard extends AbstractAliceCard {
	public ImgPaths imgPaths;
	public BackgroundPaths backgroundPaths;
	
	public AbstractAliceMarisaCard(
			String ID,
			String name,
			String img,
			int cost,
			String rawDescription,
			CardType type,
			CardColor color,
			CardRarity rarity,
			CardTarget target) {
		super(ID, name, img, cost, rawDescription, type, color, rarity, target);
	}
	
//	@Override
//	public void render(SpriteBatch sb) {
//		if (!AliceSpireKit.isInBattle()) {
//			if (imgPaths.isAlternative != AliceConfigHelper.enableAlternativeMarisaCardImage() ||
//					backgroundPaths.isPackMaster != AliceConfigHelper.usePackMasterStyleMarisaCards()) {
//				this.setImages(this.cardID);
//			}
//		}
//
//		super.render(sb);
//	}
	
//	public void setImgPaths(String name, boolean isAlternative) {
//		this.imgPaths = ImgPaths.of(name, isAlternative);
//	}
//
//	public void setBackgroundPaths(boolean isPackMaster) {
//		this.backgroundPaths = BackgroundPaths.of(isPackMaster);
//	}
	
	public static void setImages(CustomCard card, String name) {
		AbstractAliceMarisaCard.ImgPaths imgPaths = AbstractAliceMarisaCard.ImgPaths.of(
				name, AliceConfigHelper.enableAlternativeMarisaCardImage());
		AbstractAliceMarisaCard.BackgroundPaths backgroundPaths = AbstractAliceMarisaCard.BackgroundPaths.of(
				AliceConfigHelper.usePackMasterStyleMarisaCards());
		
//		card.setPortraitTextures(imgPaths.small, imgPaths.large);
		card.loadCardImage(imgPaths.small); // 你妹的 BaseMod
		
		if (card.type == AbstractCard.CardType.ATTACK)
			card.setBackgroundTexture(backgroundPaths.attack_small, backgroundPaths.attack_large);
		else if (card.type == AbstractCard.CardType.POWER)
			card.setBackgroundTexture(backgroundPaths.power_small, backgroundPaths.power_large);
		else
			card.setBackgroundTexture(backgroundPaths.skill_small, backgroundPaths.skill_large);
		
		card.setOrbTexture(backgroundPaths.orb_small, backgroundPaths.orb_large);
		// TODO: set energy texture?
	}
	
	public void setImages(String name) {
		setImages(this, name);
	}
	
	public static class ImgPaths {
		public String small, large;
		public boolean isAlternative;
		
		public ImgPaths(String small, String large, boolean isAlternative) {
			this.small = small;
			this.large = large;
			this.isAlternative = isAlternative;
		}
		
		public static ImgPaths ofAlternative(String name) {
			return new ImgPaths(
					AliceSpireKit.getImgFilePath("Marisa/cards", name),
					AliceSpireKit.getImgFilePath("Marisa/cards", name + "_p"),
					true
			);
		}
		
		public static ImgPaths ofOriginal(String name) {
			return new ImgPaths(
					AliceSpireKit.getImgFilePath("Marisa/cards", name + "_original"),
					AliceSpireKit.getImgFilePath("Marisa/cards", name + "_original_p"),
					false
			);
		}
		
		public static ImgPaths of(String name, boolean isAlternative) {
			return isAlternative ? ofAlternative(name) : ofOriginal(name);
		}
	}
	
	public static class BackgroundPaths {
		public String attack_small, attack_large,
				skill_small, skill_large,
				power_small, power_large,
				orb_small, orb_large,
				energy;
		public boolean isPackMaster;
		
		public BackgroundPaths(
				String attack_small, String attack_large,
				String skill_small, String skill_large,
				String power_small, String power_large,
				String orb_small, String orb_large,
				String energy,
				boolean isPackMaster) {
			this.attack_small = attack_small;
			this.attack_large = attack_large;
			this.skill_small = skill_small;
			this.skill_large = skill_large;
			this.power_small = power_small;
			this.power_large = power_large;
			this.orb_small = orb_small;
			this.orb_large = orb_large;
			this.energy = energy;
			this.isPackMaster = isPackMaster;
		}
		
		public static BackgroundPaths ofPackMaster() {
			return new BackgroundPaths(
					AliceSpireKit.getImgFilePath("Marisa_PackMaster/512", "attack"),
					AliceSpireKit.getImgFilePath("Marisa_PackMaster/1024", "attack"),
					AliceSpireKit.getImgFilePath("Marisa_PackMaster/512", "skill"),
					AliceSpireKit.getImgFilePath("Marisa_PackMaster/1024", "skill"),
					AliceSpireKit.getImgFilePath("Marisa_PackMaster/512", "power"),
					AliceSpireKit.getImgFilePath("Marisa_PackMaster/1024", "power"),
					AliceSpireKit.getImgFilePath("Marisa_PackMaster/512", "orb"),
					AliceSpireKit.getImgFilePath("Marisa_PackMaster/1024", "orb"),
					AliceSpireKit.getImgFilePath("Marisa_PackMaster/512", "orbIcon"),
					true
			);
		}
		
		public static BackgroundPaths ofMarisa() {
			return new BackgroundPaths(
					AliceSpireKit.getImgFilePath("Marisa/512", "attack"),
					AliceSpireKit.getImgFilePath("Marisa/1024", "attack"),
					AliceSpireKit.getImgFilePath("Marisa/512", "skill"),
					AliceSpireKit.getImgFilePath("Marisa/1024", "skill"),
					AliceSpireKit.getImgFilePath("Marisa/512", "power"),
					AliceSpireKit.getImgFilePath("Marisa/1024", "power"),
					AliceSpireKit.getImgFilePath("Marisa/512", "orb"),
					AliceSpireKit.getImgFilePath("Marisa/1024", "orb"),
					AliceSpireKit.getImgFilePath("Marisa/512", "orbIcon"),
					false
			);
		}
		
		public static BackgroundPaths of(boolean isPackMaster) {
			return isPackMaster ? ofPackMaster() : ofMarisa();
		}
	}
}
