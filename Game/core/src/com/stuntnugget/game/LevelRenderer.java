package com.stuntnugget.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape.Type;
import com.badlogic.gdx.utils.Array;

public class LevelRenderer {
	Array<Polygon> polygons;
	ShapeRenderer shapeRenderer;

	LevelRenderer(Array<Fixture> fixtures) {
		polygons = new Array<Polygon>();

		for (int fixtureIdx = 0; fixtureIdx < fixtures.size; ++fixtureIdx) {
			Type shapeType = fixtures.get(fixtureIdx).getShape().getType();
			if (shapeType == Type.Polygon) {
				PolygonShape polygonShape = (PolygonShape) fixtures.get(
						fixtureIdx).getShape();
				float[] vertexArray = new float[polygonShape.getVertexCount() * 2];
				for (int vIndex = 0; vIndex < polygonShape.getVertexCount(); vIndex++) {
					Vector2 vertex = new Vector2(0f, 0f);
					polygonShape.getVertex(vIndex, vertex);
					vertexArray[2 * vIndex] = vertex.x * StuntNugget.PPM;
					vertexArray[2 * vIndex + 1] = vertex.y * StuntNugget.PPM;
				}
				polygons.add(new Polygon(vertexArray));
			} else {
				Gdx.app.error("LevelRenderer", "Unsuppported shape type");
			}
		}
		shapeRenderer = new ShapeRenderer();
	}

	public void draw(Matrix4 projectionMatrix) {
		//Gdx.app.log("LevelRenderer", "" + polygons);

		shapeRenderer.setProjectionMatrix(projectionMatrix);
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(0.6823f, 0.4196f, 0.0745f, 1);
		for (int i = 0; i < polygons.size; ++i) {
			shapeRenderer.polygon(polygons.get(i).getVertices());
		}
		shapeRenderer.end();
		
		//TODO this will work but you need to triangulate the polygons
		/*
		PolygonSprite poly;
		PolygonSpriteBatch polyBatch = new PolygonSpriteBatch(); 
		Texture textureSolid;
		// Creating the color filling (but textures would work the same way)
		Pixmap pix = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
		pix.setColor(0xFFFF00FF); // DE is red, AD is green and BE is blue.
		pix.fill();
		textureSolid = new Texture(pix);
		textureSolid.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		PolygonRegion polyReg = new PolygonRegion(
				new TextureRegion(textureSolid),polygons.get(0).getVertices() ,  new short[] {
				    0, 1, 2,         // Two triangles using vertex indices.
				    0, 0, 0          // Take care of the counter-clockwise direction. 
				});
		poly = new PolygonSprite(polyReg);
		poly.setOrigin(0, 0);
		polyBatch = new PolygonSpriteBatch();
		polyBatch.begin();
	    poly.draw(polyBatch);
	    polyBatch.end();
	    */
	}

	public void dispose() {
		shapeRenderer.dispose();
	}
}
