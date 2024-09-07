package rs.antileaf.alice.targeting;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import rs.antileaf.alice.doll.AbstractDoll;

// Used to pass targets needed to render the reticle
public class AliceHoveredTargets {
	public boolean player = false;
	public AbstractMonster[] monsters = new AbstractMonster[0];
	public AbstractDoll[] dolls = new AbstractDoll[0];
	
	public static AliceHoveredTargets NONE = new AliceHoveredTargets();
	public static AliceHoveredTargets PLAYER = new AliceHoveredTargets() {{ player = true; }};
	public static AliceHoveredTargets allMonsters() {
		return new AliceHoveredTargets() {{
			monsters = AbstractDungeon.getMonsters().monsters.stream()
				.filter(m -> !m.isDeadOrEscaped())
				.toArray(AbstractMonster[]::new);
		}};
	}
	public static AliceHoveredTargets fromDolls(AbstractDoll... froms) {
		return new AliceHoveredTargets() {{ this.dolls = froms; }};
	}
}
