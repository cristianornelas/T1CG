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
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glGetError;

/**
 *
 * @author Cristiano
 */
public class Level {
    private VertexArray backgroud;
    private Texture bgTexture;
    private Bird bird;
    
    private int xScroll = 0;
    private int bgMov = 0;
    
    public Level() {
        float[] vertices = new float[] {
            -10.0f, -10.0f * 9.0f / 16.0f, 0.0f,
            -10.0f,  10.0f * 9.0f / 16.0f, 0.0f,
             0.0f,   10.0f * 9.0f / 16.0f, 0.0f,
             0.0f,  -10.0f * 9.0f / 16.0f, 0.0f,
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
        
        backgroud = new VertexArray(vertices, indices, tcs);
        bgTexture = new Texture("res/bg.jpeg");
        bird = new Bird();
    }
    
    
    public void update() {
        xScroll--;
        if (-xScroll % 335 == 0)
            bgMov++;
        
        bird.update();
    }
  
    
    public void render() {
        bgTexture.bind();
        Shader.BG.enable();
        backgroud.bind();
        for (int i = bgMov; i < bgMov + 4; i++) {
            Shader.BG.setUniformMat4f("vw_matrix", Matrix4f.translate(new Vector3f(i * 10 + xScroll * 0.03f, 0.0f, 0.0f)));
            backgroud.draw();
        }
        
        backgroud.render();
        Shader.BG.disable();
        bgTexture.unbind();
        
        bird.render();
    }
}
