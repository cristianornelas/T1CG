package t1cg;

import graphics.Shader;
import input.Input;
import level.Level;
import maths.Matrix4f;
import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.system.MemoryUtil.*;

/**
 *
 * @author Cristiano
 */
public class T1cg implements Runnable{
    
    private int width = 1200;
    private int height = 800;
    
    private Thread thread;
    private boolean running = false;
    
    private long window;
    
    private Level level;
    
    public void start(){
        running = true;
        thread = new Thread(this, "Game");
        thread.start();
    }
    
    public void run() {
        init();
        long lastTime = System.nanoTime();
        double ns = 1000000000.0 / 60.0;
        double delta = 0.0;
        int updates = 0;
        int frames = 0;
        long timer = System.currentTimeMillis();
        while (running){
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            if (delta >= 1.0) {
                update();
                updates++;
                delta--;
            }
            
            render();
            frames++;
            if(System.currentTimeMillis() - timer > 1000){
                timer += 1000;
                System.out.println(updates + "ups, " + frames + "fps");
                frames = 0;
                updates = 0;
            } 
            
            if (glfwWindowShouldClose(window)) {
                running = false;
            }
        }
    }
    
    private void init() {
        if ( !glfwInit() )
            return;
        
        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);
        window = glfwCreateWindow(width, height, "T1CG", NULL, NULL);
        if (window == NULL)
            return;
        
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        int xpos = (vidmode.width() - width) / 2;
        int ypos = (vidmode.height() - height) / 2;
        glfwSetWindowPos(window, xpos, ypos);
        
        glfwSetKeyCallback(window, new Input());
        
        glfwMakeContextCurrent(window);
        glfwShowWindow(window);
        GL.createCapabilities();
        
        glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        glEnable(GL_DEPTH_TEST);
        glActiveTexture(GL_TEXTURE1);
        System.out.println("OpenGL: " + glGetString(GL_VERSION));   
        Shader.loadAll();
        
        
        Shader.BG.enable();
        Matrix4f pr_matrix = Matrix4f.orthographic(-10.0f, 10.0f, -10.0f * 9.0f / 16.0f , 10.0f * 9.0f / 16.0f, -1.0f, 1.0f);
        Shader.BG.setUniformMat4f("pr_matrix", pr_matrix);
        Shader.BG.setUniform1i("tex", 1);
        
        Shader.BIRD.setUniformMat4f("pr_matrix", pr_matrix);
        Shader.BIRD.setUniform1i("tex", 1);
        
        Shader.BG.disable();
        
        level = new Level();
        
    }
    
    private void update(){
        glfwPollEvents();
        level.update();
    }
    
    private void render() {
        //update stuff
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        level.render();
        int error = glGetError();
        if (error != GL_NO_ERROR)
            System.out.println(error);
        glfwSwapBuffers(window);
    }
    
    public static void main(String[] args) {
        new T1cg().start();
    }
    
}
