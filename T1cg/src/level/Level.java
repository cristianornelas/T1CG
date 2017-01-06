package level;

import graphics.Shader;
import graphics.Texture;
import graphics.VertexArray;
import input.Input;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import maths.Matrix4f;
import maths.Vector3f;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glGetError;

/**
 *
 * @authors Cristiano & Jefferson
 * Computacao Grafica - T1: Flappy Bird
 * 
 */
public class Level {
    
    //  This class represents a Level in the game.
    
    private VertexArray backgroud;
    private Texture bgTexture;
    private Bird bird;
    private List<Number> numbers;
    
    private int xScroll = 0;
    private int bgMov = 0;
    
    private Pipe[] pipes = new Pipe[5 * 2];
    private int index = 0;
    
    private Random random = new Random();
    private int points = 0;
    private final float OFFSET = 5.0f;
    private boolean control = true, reset = false;
    
    public Level() {
        
        //  Vertices para o backgound
        float[] vertices = new float[] {
            -10.0f, -10.0f * 9.0f / 16.0f, 0.0f,
            -10.0f,  10.0f * 9.0f / 16.0f, 0.0f,
             0.0f,   10.0f * 9.0f / 16.0f, 0.0f,
             0.0f,  -10.0f * 9.0f / 16.0f, 0.0f,
        };
        
        //  Cria 2 triangulos, os numeros aqui representam as linhas dos vertices
        //  que serao utilizadas para criar esses triangulos.
        //  Isso evitar criar varias matrizes de vertices.
        byte[] indices = new byte[] {
            0, 1, 2,
            2, 3, 0
        };
        
        
        //  Coordenadas de texturas. Tambem aplicada aos vertices.
        float[] tcs = new float[] {
            0, 1,
            0, 0,
            1, 0,
            1, 1
        };
        
        //  Cria o background, birds and pipes
        backgroud = new VertexArray(vertices, indices, tcs);
        bgTexture = new Texture("res/bg.jpeg");      
        
        bird = new Bird();
        
        numbers = new ArrayList<Number>();
        float xPos = -9.0f;
        for (int i = 0; i < 10; i++) {
            Number number = new Number(Integer.toString(0), xPos);
            numbers.add(number);
            xPos += 0.5f;
        }
        
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
        
        //  xScroll varia conforme avancamos na horizontal, esse trecho atualiza 
        //  o background e cria mais pipes.
        if (control){
            xScroll--;
            if (-xScroll % 335 == 0)
                bgMov++;
            if (-xScroll > 250 && -xScroll % 120 == 0)
                updatePipes();
        }
        
        //  Faz com que o passaro se mova
        bird.update();
        
        if (control && collision()) {
            bird.control = false;
            bird.fall();
            control = false;
        }
        
        if (score()) {
            points++;
            
            for(int i = Integer.toString(points/2).length(); i>0; i--){
                numbers.get(i-1).updateTexture(String.valueOf(Integer.toString(points/2).charAt(i-1)));
            }
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
            
            //System.out.println(by0);
            if (by0 < -6 || by1 > 6)
               return true;
           
        }
        return false;
    }
    
    private boolean score() {
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
        
            if (bx > px && points % 10 == i)
                return true;
        
        }
        
        return false;
    }
    
    public boolean isGameOver(){
        return reset;
    }
    
    public void render() {
        //  Renderiza o background conforme ele se move, assim como os novos pipes.
        
        bgTexture.bind();
        Shader.BG.enable();
        backgroud.bind();
        
        //  Utiliza um loop pois o background sempre avanca.
        //  Esse codigo nao insere uma nova imagem, mas sim retira uma imagem do
        //  inicio e poe no final. Poupando recursos.
        for (int i = bgMov; i < bgMov + 4; i++) {
            Shader.BG.setUniformMat4f("vw_matrix", Matrix4f.translate(new Vector3f(i * 10 + xScroll * 0.03f, 0.0f, 0.0f)));
            backgroud.draw();
        }
        
        backgroud.render();
        Shader.BG.disable();
        bgTexture.unbind();
        renderPipes();
        bird.render();
        /*for (Number number : numbers) {
            number.render();
        }
        for (Number number : numbers) {
            number.printNumber();
        }
        System.out.println();
        */
        for(int i = Integer.toString(points/2).length(); i>0; i--){
            numbers.get(i-1).render();
        }
    }
}
