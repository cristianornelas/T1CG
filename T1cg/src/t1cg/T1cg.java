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
 * @authors Cristiano & Jefferson
 * Computacao Grafica - T1: Flappy Bird
 * 
 */
public class T1cg implements Runnable{
    
    //  Determina o tamanho da janela
    private final int width = 1200;
    private final int height = 800;
    
    //  Cria uma thread para separar a logica do jogo da camada de renderizacao
    private Thread thread;
    private boolean running = false;
    
    private long window;
    
    private Level level;
    
    //  Este metodo inicia a thread e seta o jogo como rodando
    public void start(){
        running = true;
        thread = new Thread(this, "Game");
        thread.start();
    }
    
    //  Este metodo executa o loop principal do jogo na thread criada acima
    @Override
    public void run() {
        init();
        
        // Determina um timer para controlar a velocidade com que o jogo avanca
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
            
            //  Isso eh invocado a cada 60fps (valor de delta)
            if (delta >= 1.0) {
                update();
                updates++;
                delta--;
            }
            
            render();
            frames++;
            if(System.currentTimeMillis() - timer > 1000){
                timer += 1000;
                //System.out.println(updates + "ups, " + frames + "fps");
                frames = 0;
                updates = 0;
            } 
            
            
            //  Caso a janela seja fechada, seta o jogo como parado.
            if (glfwWindowShouldClose(window))
                running = false;
        }
        
        
        glfwDestroyWindow(window);
        glfwTerminate();
    }
    
    //  Este metodo tem como objetivo inicializar os valores e principais
    //  variaveis do jogo
    private void init() {
        
        //  Verifica se a biblioteca foi inicializada corretamente
        if (!glfwInit())
            return;
        
        //  Cria uma nova janela, de tamanho reajustavel e verifica se ela foi
        //  construida com sucesso.
        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);
        window = glfwCreateWindow(width, height, "T1CG", NULL, NULL);
        if (window == NULL)
            return;
        
        
        //  Captura informacoes de video do usuario para posicionar a janela na
        //  tela.
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        int xpos = (vidmode.width() - width) / 2;
        int ypos = (vidmode.height() - height) / 2;
        glfwSetWindowPos(window, xpos, ypos);
        
        
        //  Implementa a classe Input, tornando-a responsavel pela captura de 
        //  teclas
        glfwSetKeyCallback(window, new Input());
        
        
        //  Exibe a janela na tela
        glfwMakeContextCurrent(window);
        glfwShowWindow(window);
        GL.createCapabilities();
        
        
        //  Configura caracteristicas do OpenGL como fundo, texturas, etc. 
        glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        glEnable(GL_DEPTH_TEST);
        glActiveTexture(GL_TEXTURE1);
        System.out.println("OpenGL: " + glGetString(GL_VERSION));   
        
        
        //  Carrega os Shaders
        Shader.loadAll();
        Shader.BG.enable();
        
        //  Cria a matriz de projecao
        Matrix4f pr_matrix = Matrix4f.orthographic(-10.0f, 10.0f, -10.0f * 9.0f / 16.0f , 10.0f * 9.0f / 16.0f, -1.0f, 1.0f);
        
        //  Cria os shaders para BG, Bird e pipe.
        Shader.BG.setUniformMat4f("pr_matrix", pr_matrix);
        Shader.BG.setUniform1i("tex", 1);
        Shader.BG.disable();
        
        Shader.BIRD.setUniformMat4f("pr_matrix", pr_matrix);
        Shader.BIRD.setUniform1i("tex", 1);
        
        Shader.PIPE.setUniformMat4f("pr_matrix", pr_matrix);
        Shader.PIPE.setUniform1i("tex", 1);
        
        Shader.NUMBER.setUniformMat4f("pr_matrix", pr_matrix);
        Shader.NUMBER.setUniform1i("tex", 1);
        
        
        
        level = new Level();
        
    }
    
    //  Chamado quando existem atualizacoes a serem feitas, como por exemplo,
    //  movimento, tecla pressionada, etc...
    private void update(){
        glfwPollEvents();
        level.update();
        
        if (level.isGameOver())
            level = new Level();
    }
    
    //  Esse metodo atualiza as imagens na tela. Ele eh invocado cada vez que 
    //  uma nova atuacao se faz necessaria.
    private void render() {
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
