/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics;

import java.util.HashMap;
import java.util.Map;
import maths.Matrix4f;
import maths.Vector3f;
import static org.lwjgl.opengl.GL20.*;
import utils.ShaderUtils;

/**
 *
 * @author Cristiano
 */
public class Shader {
       
    private final int ID;
    private Map<String, Integer> locationCache = new HashMap<String, Integer>();
    public static final int VERTEX_ATTRIB = 0;
    public static final int TCOORD = 1;
    public static Shader BG, BIRD, PIPE, NUMBER;
    private boolean enabled = false;
            
    public Shader(String vertex, String fragment) {
        ID = ShaderUtils.load(vertex, fragment);
    }
    
    public static void loadAll() {
        BG = new Shader("shaders/bg.vert", "shaders/bg.frag");
        BIRD = new Shader("shaders/bird.vert", "shaders/bird.frag");
        PIPE = new Shader("shaders/pipe.vert", "shaders/pipe.frag");
        NUMBER = new Shader("shaders/number.vert", "shaders/number.frag");
    }
    
    //Uniform variable sao usadas para transmitir dados da CPU para GPU
    public void setUniform1i(String name, int value) {
        if (!enabled ) 
            enable();
        glUniform1i(getUniform(name), value);
    }
    
    public void setUniform1f(String name, float value) {
        if (!enabled ) 
            enable();
        glUniform1f(getUniform(name), value);
    }
    
    public void setUniform2f(String name, float x, float y) {
        if (!enabled ) 
            enable();
        glUniform2f(getUniform(name), x, y);
    }
    
    public void setUniform3f(String name, Vector3f vector) {
        if (!enabled ) 
            enable();
        glUniform3f(getUniform(name), vector.x, vector.y, vector.z);
    }
    
    public void setUniformMat4f(String name, Matrix4f matrix) {
        if (!enabled ) 
            enable();
        glUniformMatrix4fv(getUniform(name), false, matrix.toFloatBuffer());
    }
    
    public int getUniform(String name) {
        if (locationCache.containsKey(name))
            return locationCache.get(name);
                
        int result = glGetUniformLocation(ID, name);
        
        if (result == -1)
            System.err.println("Nao foi possivel achar uniform variable '" + name + "'!");
        else
            locationCache.put(name, result);
        return result;
    }
    
    public void enable() {
        glUseProgram(ID);
        enabled = true;
    }
    
    public void disable() {
        glUseProgram(0);
        enabled = false;
    }
}
