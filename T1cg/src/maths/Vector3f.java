package maths;

/**
 *
 * @authors Cristiano & Jefferson
 * Computacao Grafica - T1: Flappy Bird
 * 
 */
public class Vector3f {
    
    //  Nesta classe um vetor tridimensional eh definido.
    //  Duas dimensoes (x, y) sao destinadas a posicionamento.
    //  A terceira dimensao (z) eh destinada a ordem de renderizacao, ie,
    //  elementos com um valor maior de z serao exibidos sobre outros elementos
    //  de ordem mais baixa.
    
    public float x, y, z;
    
    public Vector3f(){
         x = 0.0f;
         y = 0.0f;
         z = 0.0f;
    }
    
    public Vector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }    
}
