package me.antileaf.alice.monsters;

import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import me.antileaf.alice.utils.AliceHelper;

public class MedicineMelancholy extends AbstractMonster {
	public static final String SIMPLE_NAME = MedicineMelancholy.class.getSimpleName();
	public static final String ID = AliceHelper.makeID(SIMPLE_NAME);
	private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
	
	public MedicineMelancholy(float x, float y) {
		super(
				monsterStrings.NAME,
				ID,
				100, // TODO HP
				0.0F, // HB offset X
				-20.0F, // HB offset Y
				200.0F, // HB width
				300.0F, // HB height
				null, // TODO: AliceHelper.getMonsterImgFilePath(SIMPLE_NAME),
				x,
				y
		);
		
		this.type = EnemyType.ELITE;
	}
	
	public MedicineMelancholy() {
		this(0.0F, 0.0F);
	}
	
	@Override
	public void takeTurn() {
		// TODO
	}
	
	@Override
	protected void getMove(int i) {
		// TODO
	}
	
	public int calculateCardDamage(int damage) {
		ReflectionHacks.privateMethod(AbstractMonster.class, "calculateDamage", int.class)
				.invoke(this, damage);
		int tmp = this.getIntentDmg();
		ReflectionHacks.setPrivate(this, AbstractMonster.class, "intentDmg", -1);
		return tmp;
	}
}
