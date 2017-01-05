/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

/**
 *
 * @author Cristiano
 */

//responsavel por carregar um arquivo e restornar um opengl program id
public class ShaderUtils {
    private ShaderUtils() {
        
    }
    
    public static int load(String vertPath, String fragPath) {
        //vert e frag eh o codigo do shader
        //Usado pra rodar na GPU
        String vert = FileUtils.loadAsString(vertPath);
        String frag = FileUtils.loadAsString(fragPath);
        
        return create(vert, frag);
    }
    
    public static int create(String vert, String frag) {
        int program = glCreateProgram();
        int vertID = glCreateShader(GL_VERTEX_SHADER);
        int fragID = glCreateShader(GL_FRAGMENT_SHADER);
        
        glShaderSource(vertID, vert);
        glShaderSource(fragID, frag);
        
        glCompileShader(vertID);
        //Verifica se a compilacao do shader deu certo
        if (glGetShaderi(vertID, GL_COMPILE_STATUS) == GL_FALSE) {
            System.err.println("Falhou em compilar o vertex shader!");
            System.err.println(glGetShaderInfoLog(vertID));
            return -1;
        }
        
        glCompileShader(fragID);
        if (glGetShaderi(fragID, GL_COMPILE_STATUS) == GL_FALSE) {
            System.err.println("Falhou em compilar o fragment shader!");
            System.err.println(glGetShaderInfoLog(fragID));
            return -1;
        }
        
        glAttachShader(program, vertID);
        glAttachShader(program, fragID);
        glLinkProgram(program);
        glValidateProgram(program);
        
        glDeleteShader(vertID);
        glDeleteShader(fragID);
        
        return program;
        
    } 
}
