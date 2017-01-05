package input;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;

/**
 * 
 * @authors Cristiano & Jefferson
 * Computacao Grafica - T1: Flappy Bird
 * 
 */
//  Essa classe trata os eventos capturados do teclado.
public class Input extends GLFWKeyCallback{

    //  Tamanho maximo de inputs possiveis
    public static boolean[] keys = new boolean[65536];
    
    //  A ideia aqui eh criar um mapa de teclas que estao pressionadas ou nao
    @Override
    public void invoke(long window, int key, int scancode, int action, int mods) {
        if ( action == GLFW.GLFW_RELEASE )
            keys[key] = false;
        else if ( action == GLFW.GLFW_PRESS )
            keys[key] = true;
    }
    
    public static boolean isKeyDown(int keycode){
        return keys[keycode];
    }
    
}
