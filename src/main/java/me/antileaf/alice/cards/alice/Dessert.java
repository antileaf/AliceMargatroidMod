package me.antileaf.alice.cards.alice;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import me.antileaf.alice.action.utils.AnonymousAction;
import me.antileaf.alice.cards.AbstractAliceCard;
import me.antileaf.alice.patches.enums.AbstractCardEnum;
import me.antileaf.alice.utils.AliceHelper;
import me.antileaf.alice.utils.AliceImageMaster;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Dessert extends AbstractAliceCard {
	private static final Logger logger = LogManager.getLogger(Dessert.class);
	
	public static final String SIMPLE_NAME = Dessert.class.getSimpleName();
	public static final String ID = AliceHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = 1;
	private static final int MAGIC = 1;
	private static final int UPGRADE_PLUS_MAGIC = 1;
	
	public static final float FADE_DUR = 0.25F;
	private float iconTransparency = 0.0F;
	
	public Dessert() {
		super(
				ID,
				cardStrings.NAME,
				AliceHelper.getCardImgFilePath(SIMPLE_NAME),
				COST,
				cardStrings.DESCRIPTION,
				CardType.SKILL,
				AbstractCardEnum.ALICE_MARGATROID_COLOR,
				CardRarity.UNCOMMON,
				CardTarget.NONE
		);
		
		this.magicNumber = this.baseMagicNumber = MAGIC;
	}
	
	@Override
	public void triggerOnOtherCardPlayed(AbstractCard c) {
//		AliceSpireKit.log("Dessert.triggerOnOtherCardPlayed: Triggered.");
		
		if (!AbstractDungeon.player.hand.contains(this)) {
//			AliceHelper.logger.info("Dessert.triggerOnOtherCardPlayed: This card is not in player's hand.");
			return;
		}
		
		if (!AbstractDungeon.player.hand.contains(c)) {
//			AliceHelper.logger.info("Dessert.triggerOnOtherCardPlayed: The other card is not in player's hand.");
			return;
		}
		
		if (!AbstractDungeon.player.hand.isEmpty() &&
				(AbstractDungeon.player.hand.getTopCard() == c ||
						AbstractDungeon.player.hand.getBottomCard() == c)) {
//			this.use(AbstractDungeon.player, null);
//			this.addToBot(new DiscardSpecificCardAction(this));
//			this.flash();
//			this.addToBot(new AlicePlayACardAction(this, AbstractDungeon.player.hand, null, false));

			this.addToBot(new AnonymousAction(() -> {
				if (AbstractDungeon.player.hand.contains(this))
					this.addToBot(new NewQueueCardAction(this, true, false, true));
			}));
		}
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		this.addToBot(new GainEnergyAction(this.magicNumber));
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new Dessert();
	}
	
	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeMagicNumber(UPGRADE_PLUS_MAGIC);
			this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
			this.initializeDescription();
		}
	}
	
	public void renderDessertIcon(SpriteBatch sb) {
		if (AliceHelper.isInBattle()) {
			boolean shouldShowIcon = false;
			
			if (AbstractDungeon.player.hand.contains(this)) {
				AbstractCard leftmost = AbstractDungeon.player.hand.getBottomCard();
				AbstractCard rightmost = AbstractDungeon.player.hand.getTopCard(); // 显然不可能是空的
				
				AbstractCard selected = AbstractDungeon.player.hoveredCard;
				if (selected == null && rightmost.isHoveredInHand(rightmost.drawScale))
					selected = rightmost;

				if (selected == null && leftmost.isHoveredInHand(leftmost.drawScale))
					selected = leftmost;
				
				if (selected == leftmost || selected == rightmost)
					shouldShowIcon = true;
			}
			
			if (shouldShowIcon) {
				this.iconTransparency += Gdx.graphics.getDeltaTime() / FADE_DUR;
				if (this.iconTransparency > 1.0F)
					this.iconTransparency = 1.0F;
			}
			else {
				this.iconTransparency -= Gdx.graphics.getDeltaTime() / FADE_DUR;
				if (this.iconTransparency < 0.0F)
					this.iconTransparency = 0.0F;
			}
			
//			if (this.iconTransparency > 0.0F && this.iconTransparency < 1.0F) {
//				logger.debug("render: iconTransparency = {}", this.iconTransparency);
//			}
			
			if (this.iconTransparency > 0.0F && AbstractDungeon.player.hand.contains(this)) {
				Texture icon = AliceImageMaster.DESSERT_ICON;
				float scale = 1 / 2.4F * Settings.scale;
				
//				Color renderColor = ReflectionHacks.getPrivate(this, AbstractCard.class, "renderColor");
//
//				ReflectionHacks.privateMethod(AbstractCard.class, "renderHelper",
//						SpriteBatch.class, Color.class, TextureAtlas.AtlasRegion.class,
//						float.class, float.class, float.class)
//								.invoke(this, sb,
//										new Color(1.0F, 1.0F, 1.0F,
//												this.iconTransparency * renderColor.a * 0.9F),
//										new TextureAtlas.AtlasRegion(icon, 0, 0, icon.getWidth(), icon.getHeight()),
//										this.current_x,
//										this.current_y + 350.0F,
//										scale
//								);
				
				sb.setColor(new Color(1.0F, 1.0F, 1.0F, this.iconTransparency));
				float width = AbstractCard.IMG_WIDTH * this.drawScale / 2.0F;
				float height = AbstractCard.IMG_HEIGHT * this.drawScale / 2.0F;
				float topOfCard = this.current_y + height;
				float spacing = 50.0F * Settings.scale;
				float centerY = topOfCard + spacing;
				float sin = (float) Math.sin(this.angle / 180.0F * Math.PI);
				float xOffset = sin * width;
				sb.draw(
						icon,
						this.current_x - xOffset - icon.getWidth() / 2.0F,
						centerY - icon.getHeight() / 2.0F,
						icon.getWidth() / 2.0F, icon.getHeight() / 2.0F,
						icon.getWidth(), icon.getHeight(),
						scale, scale,
						0.0F,
						0, 0, icon.getWidth(), icon.getHeight(),
						false, false
				);

//				sb.setColor(originalColor);
			}
		}
	}
}
