package graphics;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import static org.lwjgl.opengl.GL11.*;
import utils.BufferUtils;

/**
 *
 * @authors Cristiano & Jefferson
 * Computacao Grafica - T1: Flappy Bird
 * 
 */
public class Texture {
    
    //  Como o nome sujere sugere essa classe cria texturas.
    //  Isso significa que ela eh responsavel por carregar uma imagem e
    //  tratar essa imagem de forma que o OpenGL consiga lidar com ela.
    private int width, height;
    private int texture;
    
    //  Inicializa a textura invocando seu carregamento de um arquivo
    public Texture(String path) {
        texture = load(path);
    }
    
    //  Carrega uma imagem e cria a textura
    private int load(String path) {
        int[] pixels = null;
        try {
            BufferedImage image = ImageIO.read(new FileInputStream(path));
            width = image.getWidth();
            height = image.getHeight();
            pixels = new int[width * height];
            image.getRGB(0, 0, width, height, pixels, 0 ,width); 
            
        } 
        catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } 
        catch (IOException ex) {
            ex.printStackTrace();
        }
        
        //  Tem que rearrajar todas os cores pois o OpenGL trabalha com cores
        //  na ordem inversa.
        int[] data = new int[width * height];
        for (int i = 0; i < width * height; i++) {
                int a = (pixels[i] & 0xff000000) >> 24;
                int r = (pixels[i] & 0xff0000) >> 16;
                int g = (pixels[i] & 0xff00) >> 8;
                int b = (pixels[i] & 0xff);

                data[i] = a << 24 | b << 16 | g << 8 | r;
        }
                
        int result = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, result);
        
        //  Isso aqui faz as imagens nao ficarem distorcidas quando 
        //  redimensionadas. Mantendo-as nitidas ao inves de borradas.
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        
        glTexImage2D(GL_TEXTURE_2D, 0 , GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, BufferUtils.createIntBuffer(data));
        glBindTexture(GL_TEXTURE_2D, 0);
         
        return result;
    }
    
    public void bind() {
        glBindTexture(GL_TEXTURE_2D, texture);
    }
    
    public void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }
}
