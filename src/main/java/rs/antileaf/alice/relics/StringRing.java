package rs.antileaf.alice.relics;

import basemod.abstracts.CustomRelic;
import basemod.abstracts.CustomSavable;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import rs.antileaf.alice.action.doll.SpawnDollAction;
import rs.antileaf.alice.doll.AbstractDoll;
import rs.antileaf.alice.doll.DollManager;
import rs.antileaf.alice.doll.dolls.EmptyDollSlot;
import rs.antileaf.alice.utils.AliceSpireKit;

import java.util.Optional;

public class StringRing extends CustomRelic implements ClickableRelic, CustomSavable<String> {
	public static final String SIMPLE_NAME = StringRing.class.getSimpleName();

	public static final String ID = SIMPLE_NAME;
	private static final String IMG = AliceSpireKit.getRelicImgFilePath(SIMPLE_NAME);
	private static final String IMG_OTL = AliceSpireKit.getRelicOutlineImgFilePath(SIMPLE_NAME);
	private static final String IMG_LARGE = AliceSpireKit.getRelicLargeImgFilePath(SIMPLE_NAME);
	
	String dollClazz = null;

	public StringRing() {
		super(
				ID,
				ImageMaster.loadImage(IMG),
				ImageMaster.loadImage(IMG_OTL),
				RelicTier.RARE,
				LandingSound.MAGICAL
		);
		
		this.largeImg = ImageMaster.loadImage(IMG_LARGE);
	}
	
	private void updateDesc() {
		this.description = this.getUpdatedDescription();
		this.tips.clear();
		this.tips.add(new PowerTip(this.name, this.description));
		this.initializeTips();
	}
	
	@Override
	public String getUpdatedDescription() {
		if (!CardCrawlGame.isInARun())
			return DESCRIPTIONS[0];
		else {
			if (this.dollClazz != null)
				return DESCRIPTIONS[0] + " NL " + String.format(DESCRIPTIONS[1], AbstractDoll.getName(this.dollClazz));
			else
				return DESCRIPTIONS[0] + " NL " + DESCRIPTIONS[2];
		}
	}
	
	@Override
	public void atPreBattle() {
		if (this.dollClazz != null) {
			this.beginPulse();
			this.pulse = true; // Equivalent to this.beginLongPulse();
			this.updateDesc();
		}
	}
	
	@Override
	public void onRightClick() {
		if (AliceSpireKit.isInBattle() && !AbstractDungeon.actionManager.turnHasEnded && this.dollClazz != null) {
			this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
			this.addToBot(new SpawnDollAction(AbstractDoll.newInst(this.dollClazz), -1));
			this.dollClazz = null;
			this.stopPulse();
			this.updateDesc();
		}
	}
	
	@Override
	public void onVictory() {
		this.stopPulse();
		
//		AliceSpireKit.logger.info("StringRing: onVictory()");
//		AliceSpireKit.logger.info("this.dollClazz: {}", this.dollClazz);
		
		if (this.dollClazz == null) {
			if (DollManager.get().hasDoll()) {
				this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
				Optional<AbstractDoll> o = DollManager.get().getDolls().stream()
						.filter(doll -> !(doll instanceof EmptyDollSlot))
						.findFirst();
				if (o.isPresent()) {
					this.dollClazz = o.get().getID();
					this.updateDesc();
				}
				else {
					this.dollClazz = null;
					AliceSpireKit.log("StringRing: Optional<AbstractDoll> is empty. Maybe there is a bug in the code.");
				}
			}
		}
	}
	
	@Override
	public AbstractRelic makeCopy() {
		return new StringRing();
	}
	
	@Override
	public String onSave() {
		return this.dollClazz == null ? "" : this.dollClazz;
	}
	
	@Override
	public void onLoad(String s) {
		this.dollClazz = s.isEmpty() ? null : s;
		this.description = this.getUpdatedDescription();
	}
	
//	@Override
//	public Type savedType() {
//		return String.class;
//	}
}