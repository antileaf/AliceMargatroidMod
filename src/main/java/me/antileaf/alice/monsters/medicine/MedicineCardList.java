package me.antileaf.alice.monsters.medicine;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.RunicDome;
import me.antileaf.alice.action.medicine.MedicinePlayCardAction;
import me.antileaf.alice.action.medicine.MedicineCardFadeOutAction;
import me.antileaf.alice.action.utils.AnonymousAction;
import me.antileaf.alice.utils.AliceHelper;

import java.util.ArrayList;

public class MedicineCardList {
	private MedicineCardItem hovered = null;
	
	private float drawStartX, drawEndX, drawStartY;
	private float padX;
	private float scale;
	
	private final ArrayList<MedicineCardItem> cards;
	public AbstractCard cardInUse = null;
	
	public MedicineCardList(float x, float y) {
		this.drawStartX = x;
		this.drawEndX = x;
		this.drawStartY = y;
		this.padX = 72.0F * Settings.scale;
		this.scale = 1.0F;
		
		this.cards = new ArrayList<>();
	}
	
	public void applyPowers(MedicineMelancholy medicine) {
		for (MedicineCardItem item : this.cards)
			item.applyPowers(medicine);
	}
	
	private float getTargetX(int index) {
		float res = this.drawStartX + this.padX * index;
		
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
			this.scale = Settings.scale * (!this.cards.isEmpty() ?
					Math.min(1.5F, 5.0F / this.cards.size()) : 1.5F);
			this.drawStartX = x - this.cards.size() * 36.0F * this.scale;
			this.drawEndX = x + this.cards.size() * 36.0F * this.scale;
			this.drawStartY = y;
			this.padX = this.cards.size() > 1 ?
					(this.drawEndX - this.drawStartX) / (this.cards.size() - 1) : 72.0F * this.scale;
			
			MedicineCardItem newHovered = null;
			
			for (int i = 0; i < this.cards.size(); i++) {
				MedicineCardItem item = this.cards.get(i);
				item.update(this.getTargetX(i), this.drawStartY, this.scale, item == this.hovered);
				
				if (item.card.hb.hovered)
					newHovered = item;
			}
			
			this.hovered = newHovered;
			
			if (this.cardInUse != null) {
				this.cardInUse.update();
				this.cardInUse.hb.hovered = false;
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
		
		if (this.cardInUse != null)
			this.cardInUse.render(sb);
		
		if (!AbstractDungeon.player.isDead && !AbstractDungeon.player.hasRelic(RunicDome.ID))
			this.renderIntent(sb);
	}
	
	public void renderIntent(SpriteBatch sb) {
		for (MedicineCardItem item : this.cards)
			if (item != this.hovered)
				item.renderIntent(sb);
		
		if (this.hovered != null)
			this.hovered.renderIntent(sb);
	}
	
	public void clear() {
		this.cards.forEach(MedicineCardItem::dispose);
		this.cards.clear();
	}
	
	public void add(AbstractCard card) {
		card.drawScale = this.scale;
		card.current_x = (this.drawEndX - this.padX * (this.cards.size() + 1));
		card.current_y = this.drawStartY;
		card.transparency = 0.0F;
		card.targetTransparency = 1.0F;
		this.cards.add(new MedicineCardItem(card));
	}
	
	public void playAll(MedicineMelancholy medicine) {
		for (MedicineCardItem item : this.cards)
			AliceHelper.addToBot(new MedicinePlayCardAction(item, medicine));
	}
	
	public void play(MedicineCardItem item, MedicineMelancholy medicine) {
		this.cards.remove(item);
		this.cardInUse = item.card;
		
		item.play(medicine);
		
		item.card.target_x = Settings.WIDTH / 2.0F;
		item.card.target_y = Settings.HEIGHT / 2.0F;
		item.card.targetDrawScale = 1.0F;
		
		AliceHelper.addActionToBuffer(new MedicineCardFadeOutAction(this, item));
		
		AliceHelper.commitBuffer();
	}
}
