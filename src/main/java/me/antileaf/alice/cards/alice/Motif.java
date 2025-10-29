package me.antileaf.alice.cards.alice;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import me.antileaf.alice.action.utils.AnonymousAction;
import me.antileaf.alice.cards.AbstractAliceCard;
import me.antileaf.alice.doll.AbstractDoll;
import me.antileaf.alice.doll.DollManager;
import me.antileaf.alice.doll.dolls.EmptyDollSlot;
import me.antileaf.alice.patches.enums.AbstractCardEnum;
import me.antileaf.alice.utils.AliceHelper;
import me.antileaf.alice.utils.AliceImageMaster;

import java.util.ArrayList;

public class Motif extends AbstractAliceCard {
	public static final String SIMPLE_NAME = Motif.class.getSimpleName();
	public static final String ID = AliceHelper.makeID(SIMPLE_NAME);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	
	private static final int COST = 1;
	private static final int MAGIC = 4;
	private static final int UPGRADE_PLUS_MAGIC = 1;
	
	public static final float FADE_DUR = 0.25F;
	private float iconTransparency = 0.0F;

	public Motif() {
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
	
	private static ArrayList<Integer> getIndices() {
		ArrayList<Integer> indices = new ArrayList<>();
		
		ArrayList<AbstractDoll> dolls = DollManager.get().getDolls();
		for (int i = dolls.size() - 1; i >= 0; i--) {
			if (dolls.get(i) != null && !(dolls.get(i) instanceof EmptyDollSlot))
				indices.add(dolls.size() - i - 1);
		}
		
		return indices;
	}
	
	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		ArrayList<AbstractCard> toDiscard = new ArrayList<>();
		for (int i : getIndices()) {
			if (i >= AbstractDungeon.player.hand.size())
				break;
			
			AbstractCard c = AbstractDungeon.player.hand.group.get(i);
			if (c != this)
				toDiscard.add(c);
		}
		
		this.addToBot(new AnonymousAction(() -> {
			for (AbstractCard c : toDiscard) {
				if (AbstractDungeon.player.hand.contains(c)) {
					AbstractDungeon.player.hand.moveToDiscardPile(c);
					GameActionManager.incrementDiscard(false);
					c.triggerOnManualDiscard();
				}
			}
		}));
		
		this.addToBot(new DrawCardAction(this.magicNumber));
	}
	
	@Override
	public AbstractCard makeCopy() {
		return new Motif();
	}
	
	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeMagicNumber(UPGRADE_PLUS_MAGIC);
			this.initializeDescription();
		}
	}
	
	public void renderMotifIcon(SpriteBatch sb) {
		if (AliceHelper.isInBattle()) {
			if (AbstractDungeon.player.hoveredCard == this || this.isHoveredInHand(this.drawScale)) {
				this.iconTransparency += Gdx.graphics.getDeltaTime() / FADE_DUR;
				if (this.iconTransparency > 1.0F)
					this.iconTransparency = 1.0F;
			}
			else {
				this.iconTransparency -= Gdx.graphics.getDeltaTime() / FADE_DUR;
				if (this.iconTransparency < 0.0F)
					this.iconTransparency = 0.0F;
			}
			
			if (this.iconTransparency > 0.0F) {
				Texture icon = AliceImageMaster.MOTIF_ICON;
				float scale = 1 / 2.4F * Settings.scale;
				
				sb.setColor(new Color(1.0F, 1.0F, 1.0F, this.iconTransparency));
				
				for (int i : getIndices()) {
					if (i >= AbstractDungeon.player.hand.size())
						break;
					
					AbstractCard c = AbstractDungeon.player.hand.group.get(i);
					if (c == this)
						continue;
					
					float width = AbstractCard.IMG_WIDTH * c.drawScale / 2.0F;
					float height = AbstractCard.IMG_HEIGHT * c.drawScale / 2.0F;
					float topOfCard = c.current_y + height;
					float spacing = 50.0F * Settings.scale;
					float centerY = topOfCard + spacing;
					float sin = (float) Math.sin(c.angle / 180.0F * Math.PI);
					float xOffset = sin * width;
					sb.draw(
							icon,
							c.current_x - xOffset - icon.getWidth() / 2.0F,
							centerY - icon.getHeight() / 2.0F,
							icon.getWidth() / 2.0F, icon.getHeight() / 2.0F,
							icon.getWidth(), icon.getHeight(),
							scale, scale,
							0.0F,
							0, 0, icon.getWidth(), icon.getHeight(),
							false, false
					);
				}
			}
		}
	}
}
