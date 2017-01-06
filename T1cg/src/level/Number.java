/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package level;

import graphics.Shader;
import graphics.Texture;
import graphics.VertexArray;
import maths.Matrix4f;
import maths.Vector3f;

/**
 *
 * @author Cristiano
 */
public class Number {
    private float SIZE = 0.5f;
    private String number;
    private Vector3f position = new Vector3f();
    private static float width = 1.0f, height = 1.0f;
    private static Texture texture;
    private Matrix4f ml_matrix;
    private static VertexArray mesh;
    
    public void create() {
        float[] vertices = new float[] {
            -SIZE / 2.0f, -SIZE / 2.0f, 0.5f,
            -SIZE / 2.0f,  SIZE / 2.0f, 0.5f,
             SIZE / 2.0f,  SIZE / 2.0f, 0.5f,
             SIZE / 2.0f, -SIZE / 2.0f, 0.5f,            
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
        texture = new Texture("res/" + number + ".png");
    }
    
    public Number(String num, float x) {
        number = num;
        position.x = x;
        position.y = 5.0f;
        position.z = 0.0f;
        
        create();
    }
    
    public float getX(){
        return position.x;
    }
    
    public void updateTexture(String number){
        texture.unbind();
        texture.bind();
        this.number = number;
        texture = new Texture("res/" + this.number + ".png");
        //System.out.println(number);
        //texture = new Texture("res/0.png");
    }
    
    public void printNumber(){
        System.out.print(number);
    }
    
    public String getNumber(){
        return number;
    }
    
    public void translateX(int dx) {
        position.x += dx;
    }
    
    public void render() {
        Shader.NUMBER.enable();
        Shader.NUMBER.setUniformMat4f("ml_matrix", Matrix4f.translate(position));
        texture.bind();
        mesh.render();
        Shader.NUMBER.disable();
    }

}
