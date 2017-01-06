package utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 *
 * @authors Cristiano & Jefferson
 * Computacao Grafica - T1: Flappy Bird
 * 
 */
public class BufferUtils {
    
    //  This class implements the buffers supported by LWJGL/OpenGL
    private BufferUtils() { 
    }
    
    public static ByteBuffer createByteBuffer(byte[] array) {
        ByteBuffer result = ByteBuffer.allocateDirect(array.length).order(ByteOrder.nativeOrder());
        result.put(array).flip();
        return result;
    }
    
    public static FloatBuffer createFloatBuffer(float[] array) {
        FloatBuffer result = ByteBuffer.allocateDirect(array.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        result.put(array).flip();
        return result;
    }
    
    public static IntBuffer createIntBuffer(int[] array) {
        IntBuffer result = ByteBuffer.allocateDirect(array.length * 4).order(ByteOrder.nativeOrder()).asIntBuffer();
        result.put(array).flip();
        return result;
    }
    
    
    
}
