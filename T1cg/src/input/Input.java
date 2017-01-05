package input;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;

/**
 *
 * @author Cristiano
 */
public class Input extends GLFWKeyCallback{

    public static boolean[] keys = new boolean[65536];
    
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
