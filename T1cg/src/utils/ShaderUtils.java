package utils;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

/**
 *
 * @authors Cristiano & Jefferson
 * Computacao Grafica - T1: Flappy Bird
 * 
 */
public class ShaderUtils {
    
    //  Esta classe eh responsavel por carregar um arquivo e retornar um OpenGL 
    //  program ID. Os arquivos carregados por essa classe estao localizados no
    //  diretorio shaders.
    
    private ShaderUtils() {}
    
    //  Este metodo ira carregar dois arquivos: vert e frag que sao codigos
    //  do shader, utilizado para rodar na GPU. Em seguida invoca o metodo
    //  create para que estes sejam criados.
    public static int load(String vertPath, String fragPath) {
        String vert = FileUtils.loadAsString(vertPath);
        String frag = FileUtils.loadAsString(fragPath);
        
        return create(vert, frag);
    }
    
    
    
    public static int create(String vert, String frag) {
        
        //  Faz com que rode na GPU
        int program = glCreateProgram();
        
        
        //  Cria os OpenGL ID
        //  O que esse trecho faz eh dizer a placa de video aonde renderizar 
        //  elementos para o jogo, como efeitos de luz e sombra.
        //  Isso ajuda a GPU a saber o que fazer com o que esta sendo enviado a 
        //  ela.
        int vertID = glCreateShader(GL_VERTEX_SHADER);
        int fragID = glCreateShader(GL_FRAGMENT_SHADER);
        
        glShaderSource(vertID, vert);
        glShaderSource(fragID, frag);
        
        
        //  Verifica se a compilacao do shader deu certo
        glCompileShader(vertID);
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
        
        
        //  Faz a magica aconteca de fato
        glAttachShader(program, vertID);
        glAttachShader(program, fragID);
        glLinkProgram(program);
        glValidateProgram(program);
        
        
        //  Limpa tudo quando nao for mais necessario
        glDeleteShader(vertID);
        glDeleteShader(fragID);
        
        return program;
        
    } 
}
