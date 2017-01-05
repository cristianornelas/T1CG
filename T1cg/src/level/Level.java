/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package level;

import graphics.Shader;
import graphics.Texture;
import graphics.VertexArray;
import input.Input;
import java.util.Random;
import maths.Matrix4f;
import maths.Vector3f;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
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
    
    private Pipe[] pipes = new Pipe[5 * 2];
    private int index = 0;
    
    private Random random = new Random();
    
    private final float OFFSET = 5.0f;
    private boolean control = true, reset = false;
    
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
        
        createPipes();
    }
    
    private void createPipes() {
        Pipe.create();
        for (int i = 0; i < 5 * 2; i += 2) {
            pipes[i] = new Pipe(OFFSET + index * 3.0f, random.nextFloat() * 4.0f);
            pipes[i+1] = new Pipe(pipes[i].getX(), pipes[i].getY() - 12.0f);
            index += 2;
        }
    }
    
    private void updatePipes() {
        pipes[index % 10] = new Pipe(OFFSET + index * 3.0f, random.nextFloat() * 4.0f);
        pipes[(index + 1) % 10] = new Pipe(pipes[index % 10].getX(), pipes[index % 10].getY() - 12.0f);
    
        index += 2;
    }
    
    public void update() {
        if (control){
            xScroll--;
            if (-xScroll % 335 == 0)
                bgMov++;
            if (-xScroll > 250 && -xScroll % 120 == 0)
                updatePipes();
        }
        bird.update();
        
        if (control && collision()) {
            bird.control = false;
            bird.fall();
            control = false;
        }
        
        if (!control && Input.isKeyDown(GLFW_KEY_SPACE)){
            reset = true;
        }
    }
    
    private void renderPipes() {
        Shader.PIPE.enable();
        Shader.PIPE.setUniformMat4f("vw_matrix", Matrix4f.translate(new Vector3f(xScroll * 0.05f, 0.0f, 0.0f)));
        Pipe.getTexture().bind();
        Pipe.getMesh().bind();
        
        for (int i = 0; i < 5 * 2; i++) {
            Shader.PIPE.setUniformMat4f("ml_matrix", pipes[i].getModelMatrix());
            Shader.PIPE.setUniform1i("top", i % 2 == 0 ? 1 : 0);
            Pipe.getMesh().draw();
        }
        Pipe.getMesh().unbind();
        Pipe.getTexture().unbind();
        //NAO TEM ISSO SE DER PAU TIRA
        Shader.PIPE.disable();
    }
    
    private boolean collision() {
        for (int i = 0; i < 5 * 2; i++) {
            float bx = -xScroll * 0.05f;
            float by = bird.getY();
            float px = pipes[i].getX();
            float py = pipes[i].getY();
            
            float bx0 = bx - bird.getSize() / 2.0f;
            float bx1 = bx + bird.getSize() / 2.0f;
            float by0 = by - bird.getSize() / 2.0f;
            float by1 = by + bird.getSize() / 2.0f;
            
            float px0 = px;
            float px1 = px + Pipe.getWidth();
            float py0 = py;
            float py1 = py + Pipe.getHeigth();
            
            if (bx1 > px0 && bx0 < px1) {
                if (by1 > py0 && by0 < py1)
                    return true;
            }
            
            System.out.println(by0);
            if (by0 < -6 || by1 > 6)
               return true;
           
        }
        return false;
    }
    
    public boolean isGameOver(){
        return reset;
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
        renderPipes();
        bird.render();
    }
}
