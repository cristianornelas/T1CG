package level;

import graphics.Shader;
import graphics.Texture;
import graphics.VertexArray;
import input.Input;
import maths.Matrix4f;
import maths.Vector3f;
import org.lwjgl.glfw.GLFW;

/**
 *
 * @authors Cristiano & Jefferson
 * Computacao Grafica - T1: Flappy Bird
 * 
 */
public class Bird {
    
    //  Essa classe define um passaro
    
    private float SIZE = 1.0f;
    private VertexArray mesh;
    private Texture texture;
    private Vector3f position = new Vector3f();
    private float rot;
    private float delta = 0.0f;
    public boolean control = true;
    
    public Bird() {
        
        //  Aqui aplicamos a mesma logica que usamos em Level
        float[] vertices = new float[] {
            -SIZE / 2.0f, -SIZE / 2.0f, 0.2f,
            -SIZE / 2.0f,  SIZE / 2.0f, 0.2f,
             SIZE / 2.0f,  SIZE / 2.0f, 0.2f,
             SIZE / 2.0f, -SIZE / 2.0f, 0.2f,            
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
        texture = new Texture("res/bird.png");
    }
    
    public float getY(){
        return position.y;
    }
    
    public void update() {
        //  Configura a posicao do passaro e o que acontece quando espaco eh
        //  pressionado
        
        position.y  -= delta;
        if (control && Input.isKeyDown(GLFW.GLFW_KEY_SPACE))
            delta = -0.13f;
        else
            delta += 0.01f;
        
        rot = -delta * 90.0f;
    }
    
    public void fall() {
        //  Determina a queda do passaro
        delta = -0.15f;
    }

    public void render() {
        //  Renderiza o passaro se movendo na tela
        Shader.BIRD.enable();
        
        //  Aqui aplicamos rotacao para fazer com que o passo se incline no pulo/queda
        Shader.BIRD.setUniformMat4f("ml_matrix", Matrix4f.translate(position).multiply(Matrix4f.rotate(rot)));
        texture.bind();
        mesh.render();
        Shader.BIRD.disable();
    }

    public float getSize() {
        return SIZE;
    }
    
}
