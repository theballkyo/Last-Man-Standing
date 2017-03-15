package com.lms.entity;

import java.util.HashMap;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Rectangle;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.components.physics.PhysicsBodyComponent;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;

public class EntityData {

	private float x;
	private float y;
	private float width;
	private float height;
	private boolean isPhysic;

	private static HashMap<Long, EntityData> allEntityData = new HashMap<>();
	
	/**
	 * 
	 * @return
	 */
	public synchronized static HashMap<Long, EntityData> getAll() {
		return allEntityData;
	}
	
	/**
	 * 
	 * @param entity
	 */
	public synchronized static void addEntity(Entity entity) {
		allEntityData.put(entity.getId(), new EntityData(entity));
	}
	
	
	private EntityData(Entity e) {
		TransformComponent tf = ComponentRetriever.get(e, TransformComponent.class);
		DimensionsComponent dc = ComponentRetriever.get(e, DimensionsComponent.class);
		PhysicsBodyComponent pbc = ComponentRetriever.get(e, PhysicsBodyComponent.class);
		if (pbc == null) {
			isPhysic = false;
		} else {
			isPhysic = true;
		}

		width = dc.width;
		height = dc.height;
		x = tf.x;
		y = tf.y;
		x = x - ((width * tf.scaleX - width) / 2);
		y = y - ((height * tf.scaleY - height) / 2);
		width *= tf.scaleX;
		height *= tf.scaleY;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public boolean isPhysic() {
		return isPhysic;
	}

	public void setPhysic(boolean isPhysic) {
		this.isPhysic = isPhysic;
	}

	public float getUpperY() {
		return y + height;
	}

	public boolean isRayCast(Rectangle playerPosRect) {
		Rectangle r = new Rectangle(x, y + height - 10, width, 10);
		if (r.overlaps(playerPosRect)) {
			return true;
		}
		return false;
	}
}
