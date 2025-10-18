package me.antileaf.alice.monsters.medicine;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

public class MedicineCardList {
	private MedicineCardItem hovered = null;
	
	private float drawStartX, drawEndX, drawStartY;
	private float padX;
	private float scale;
	
	private final ArrayList<MedicineCardItem> cards;
	
	public MedicineCardList(float x, float y) {
		this.drawStartX = x;
		this.drawEndX = x;
		this.drawStartY = y;
		this.padX = 160.0F * Settings.scale;
		this.scale = 1.0F;
		
		this.cards = new ArrayList<>();
	}
	
	public void applyPowers(MedicineMelancholy medicine) {
		for (MedicineCardItem item : this.cards)
			item.applyPowers(medicine);
	}
	
	private float getTargetX(int index) {
		float res = this.drawEndX - this.padX * (this.cards.size() - index);
		
		if (this.hovered != null) {
			int hoveredIndex = this.cards.indexOf(this.hovered);
			if (hoveredIndex != -1) {
				if (index < hoveredIndex)
					res -= AbstractCard.IMG_WIDTH_S * 0.15F;
				else if (index > hoveredIndex)
					res += AbstractCard.IMG_WIDTH_S * 0.15F;
			}
		}
		
		return res;
	}
	
	public void update(float x, float y) {
		if (!AbstractDungeon.isScreenUp) {
			this.scale = !this.cards.isEmpty() ? Math.min(1.5F, 5.0F / this.cards.size()) : 1.5F;
			this.drawStartX = 0.65F * Settings.WIDTH - this.cards.size() * 80.0F * Settings.scale * this.scale;
			this.drawEndX = x + this.cards.size() * 80.0F * Settings.scale * this.scale;
			this.drawStartY = y;
			this.padX = !this.cards.isEmpty() ? (this.drawEndX - this.drawStartX) / this.cards.size() : 0.0F;
			
			MedicineCardItem lastHovered = this.hovered;
			this.hovered = null;
			
			for (int i = 0; i < this.cards.size(); i++) {
				MedicineCardItem item = this.cards.get(i);
				item.update(this.getTargetX(i), this.drawStartY, this.scale);
			}
		}
	}
	
	public void render(SpriteBatch sb) {
		sb.setColor(Color.WHITE);
		
		for (MedicineCardItem item : this.cards)
			if (item != this.hovered)
				item.render(sb, false);
		
		if (this.hovered != null)
			this.hovered.render(sb, true);
	}
	
	public void renderIntent(SpriteBatch sb) {
		for (MedicineCardItem item : this.cards)
			if (item != this.hovered)
				item.renderIntent(sb);
		
		if (this.hovered != null)
			this.hovered.renderIntent(sb);
	}
}
