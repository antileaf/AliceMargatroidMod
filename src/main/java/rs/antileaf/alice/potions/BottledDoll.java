package rs.antileaf.alice.potions;

import basemod.ReflectionHacks;
import basemod.abstracts.CustomPotion;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.utils.AliceHelper;
import rs.antileaf.alice.utils.AliceKeywordsHelper;

public class BottledDoll extends CustomPotion {
	public Texture img;
	public static final PotionStrings potionStrings =
			CardCrawlGame.languagePack.getPotionString("BottledDoll");

	private String dollClazz = null;
	private PotionStrings strings = null;

	public BottledDoll(String id) {
		super(
				potionStrings.NAME,
				id,
				PotionRarity.UNCOMMON,
				PotionSize.SPHERE,
				PotionColor.SWIFT
		);

		this.labOutlineColor = CardHelper.getColor(255,215,0);
		this.img = new Texture(AliceHelper.getImgFilePath("orbs", "ShanghaiDoll"));
	}

	private void updateDescription() {
		if (this.dollClazz == null)
			this.strings = potionStrings;
		else
			this.strings = CardCrawlGame.languagePack.getPotionString("Bottled" + this.dollClazz);
	}

	public void setDoll(String dollClazz) {
		this.dollClazz = dollClazz;
		this.updateDescription();
		this.img = new Texture(AliceHelper.getImgFilePath("orbs", dollClazz));
	}

	@Override
	public void initializeData() {
		this.description = this.strings.DESCRIPTIONS[0];

		this.tips.clear();
		this.tips.add(new PowerTip(this.name, this.description));

		if (dollClazz != null)
			this.tips.add(new PowerTip(
					AbstractDoll.getName(dollClazz),
					AliceKeywordsHelper.descriptions.get(dollClazz)));
	}

	@Override
	public void use(AbstractCreature target) {
		// TODO
	}

	@Override
	public int getPotency(int ascensionLevel) {
		return 1;
	}

	@Override
	public AbstractPotion makeCopy() {
		BottledDoll copy = new BottledDoll(this.ID);
		if (this.dollClazz != null)
			copy.setDoll(this.dollClazz);
		return copy;
	}

	@Override
	public void render(SpriteBatch sb) {
//		ReflectionHacks.privateMethod(AbstractPotion.class, "updateFlash").invoke(this);
//		ReflectionHacks.privateMethod(AbstractPotion.class, "updateEffect").invoke(this);

		sb.draw(
				this.img,
				this.posX - 20.0F,
				this.posY - 20.0F,
				16.0F,
				16.0F,
				32.0F,
				32.0F,
				this.scale,
				this.scale,
				ReflectionHacks.getPrivate(this, AbstractPotion.class, "angle"),
				0,
				0,
				this.img.getWidth(),
				this.img.getHeight(),
				false,
				false
		);

		super.render(sb);
	}
}
