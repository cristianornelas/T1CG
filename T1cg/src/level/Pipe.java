/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package level;

import graphics.Texture;
import graphics.VertexArray;
import maths.Matrix4f;
import maths.Vector3f;

/**
 *
 * @author Cristiano
 */
public class Pipe {
    private Vector3f position = new Vector3f();
    private static float width = 1.5f, height = 8.0f;
    private static Texture texture;
    private Matrix4f ml_matrix;
    private static VertexArray mesh;
    
    public static void create() {
        float[] vertices = new float[] {
            0.0f, 0.0f, 0.1f,
            0.0f, height, 0.1f,
            width, height, 0.1f,
            width, 0.0f, 0.1f,
            };
        
        byte[] indices = new byte[] {
            0, 1, 2,
            2, 3, 0
        };
        
        float[] tcs = new float[] {
            0, 1,
            0, 0,
            1, 0,
            1, 1
        };
        
        mesh = new VertexArray(vertices, indices, tcs);
        texture = new Texture("res/pipe.png");
    }
    
    public Pipe(float x, float y) {
        position.x = x;
        position.y = y; 
        ml_matrix = Matrix4f.translate(position); 
    }
    
    public float getX() {
        return position.x;
    }
    
    public float getY() {
        return position.y;
    }
    
    public Matrix4f getModelMatrix() {
        return ml_matrix;
    }
    
    public static VertexArray getMesh(){
        return mesh;
    }
    
    public static Texture getTexture() {
        return texture;
    }
    
    public static float getWidth() {
        return width;
    }
    
    public static float getHeigth() {
        return height;
    }
}
