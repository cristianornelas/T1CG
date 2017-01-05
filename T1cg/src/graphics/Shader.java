package graphics;

import java.util.HashMap;
import java.util.Map;
import maths.Matrix4f;
import maths.Vector3f;
import static org.lwjgl.opengl.GL20.*;
import utils.ShaderUtils;

/**
 *
 * @authors Cristiano & Jefferson
 * Computacao Grafica - T1: Flappy Bird
 * 
 */
public class Shader {
    
    //  Esta classe representa um Shader. A ideia por tras de utilizar essa
    //  classe eh evitar realizar chamadas diretas ao OpenGL para ativar objetos
    private final int ID;
    private Map<String, Integer> locationCache = new HashMap<String, Integer>();
    public static final int VERTEX_ATTRIB = 0;
    public static final int TCOORD = 1;
    public static Shader BG, BIRD, PIPE;
    private boolean enabled = false;
        

    //  Cria o ProgramID para nosso Shader
    public Shader(String vertex, String fragment) {
        ID = ShaderUtils.load(vertex, fragment);
    }
    
    
    //  Este metodo ativa o Shade
    public void enable() {
        glUseProgram(ID);
        enabled = true;
    }
    
    
    //  Este metodo desativa o Shade
    public void disable() {
        glUseProgram(0);
        enabled = false;
    }
    
    public static void loadAll() {
        BG = new Shader("shaders/bg.vert", "shaders/bg.frag");
        BIRD = new Shader("shaders/bird.vert", "shaders/bird.frag");
        PIPE = new Shader("shaders/pipe.vert", "shaders/pipe.frag");
    }
    
    //  Os metodos setUniform sao utilizados para transmitir dados da
    //  CPU para a GPU.
    //  Criaremos metodos para valores unicos, vetores bidimensionais,
    //  tridimensionais e para as matrizes que criamos em Matrix4f.
    //  Como esses metodos sao invocados constantemente, para obter informacoes
    //  que ja possuimos e o custo dessa operacao eh alto, criamos um cache
    //  em locationCache para poupar recursos.
    public int getUniform(String name) {
        
        //  Caso o shade ja tenha sido calculado retorna com o valor calculado
        //  senao calcula.
        if (locationCache.containsKey(name))
            return locationCache.get(name);
                
        int result = glGetUniformLocation(ID, name);
        
        if (result == -1)
            System.err.println("Nao foi possivel achar uniform variable '" + name + "'!");
        else
            locationCache.put(name, result);
        
        return result;
    }
    
    public void setUniform1i(String name, int value) {
        if (!enabled) 
            enable();
        glUniform1i(getUniform(name), value);
    }
    
    public void setUniform1f(String name, float value) {
        if (!enabled) 
            enable();
        glUniform1f(getUniform(name), value);
    }
    
    public void setUniform2f(String name, float x, float y) {
        if (!enabled) 
            enable();
        glUniform2f(getUniform(name), x, y);
    }
    
    public void setUniform3f(String name, Vector3f vector) {
        if (!enabled) 
            enable();
        glUniform3f(getUniform(name), vector.x, vector.y, vector.z);
    }
    
    public void setUniformMat4f(String name, Matrix4f matrix) {
        if (!enabled) 
            enable();
        glUniformMatrix4fv(getUniform(name), false, matrix.toFloatBuffer());
    }
}
