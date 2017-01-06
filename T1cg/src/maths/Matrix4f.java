package maths;

import utils.BufferUtils;
import java.nio.FloatBuffer;

/**
 *
 * @authors Cristiano & Jefferson
 * Computacao Grafica - T1: Flappy Bird
 * 
 */
public class Matrix4f {
    
    //  Esta classe eh responsavel por criar uma matriz para operacoes dentro do
    //  jogo. Sendo que implementa metodos com operacoes matematicas em matriz.
    //  Por exemplo, criar matriz identidade, de translacao, rotacao, etc...
    
    
    //  Metodos:
    //  identity(): Cria uma matriz identidade, que eh usada como base para
    //              todas as transformacoes do jogo.
    //  multiply(): Define um algoritmo de multiplicacao de matrizes.
    //  translate():    Cria uma matriz de translacao
    //  rotate():   Cria uma matriz de rotacao 2D, ou seja, em torno do eixo z.
    
    //  translate e rotate serao utilizados para mover objetos na tela, nao so
    //  nosso passaro, mas como tambem elementos do mapa.
    
    //  Determina uma matriz de tamanho pre-definido. Em nosso caso, uma 4 x 4.
    public static final int SIZE = 4 * 4;
    public float[] elements = new float[SIZE];
    
    public Matrix4f(){
    }
    
    
    
    //  Este metodo cria a uma matriz identidade.
    //  Uma matriz multiplicada pela sua identidade sera igual a propria matriz.
    //  A * I = A
    //  A criacao dessa matriz eh muito util, pois ela eh base para todas as 
    //  matrizes de transformacao.
    public static Matrix4f identity() {
        Matrix4f result = new Matrix4f();
        
        //  Preenche toda a matriz com 0
        for (int i = 0; i < SIZE; i++)
            result.elements[i] = 0.0f;
        
        //  Cria uma a diagonal principal com valores 1.0f
        //  Onde result.elements[linha + coluna * larguraDaMatriz]
        result.elements[0 + 0 * 4] = 1.0f;
        result.elements[1 + 1 * 4] = 1.0f;
        result.elements[2 + 2 * 4] = 1.0f;
        result.elements[3 + 3 * 4] = 1.0f;
    
        return result;
    }
    
    
    
    //  Este metodo executa um algoritmo padrao de multiplicacao de matrizes.
    //  Ao final retorna uma matriz com o resultado do produto.
    public Matrix4f multiply(Matrix4f matrix) {
        Matrix4f result = new Matrix4f();
        
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                float sum = 0.0f;
                for (int e = 0; e < 4; e++) {
                    sum += this.elements[x + e * 4] * matrix.elements[e + y * 4];
                }
                result.elements[x + y * 4] = sum;
            }
        }
        
        return result;
    }
    
    
    
    //  Este metodo implementa um algoritmo de translacao 2D.
    //  Em OpenGL a translacao eh orientada pelas colunas da matriz, e nao pelas
    //  linhas. Entao essa convencao sera seguida.
    public static Matrix4f translate(Vector3f vector) {
        Matrix4f result = identity();
        
        // [1   0   0   x] --> x eh o 12° elemento da nossa matriz
        // [0   1   0   y] --> y eh o 13° elemento da nossa matriz
        // [0   0   1   z] --> z eh o 14° elemento da nossa matriz
        // [0   0   0   1]
        
        //  A operacao de translacao sera realizada baseada no vetor passado
        //  por parametro e na matriz identidade que contruimos logo acima.
        //  Onde result.elements[linha + coluna * larguraDaMatriz]
        result.elements[0 + 3 * 4] = vector.x;
        result.elements[1 + 3 * 4] = vector.y;
        result.elements[2 + 3 * 4] = vector.z;
        
        return result;
    }
    
    
    
    //  Este metodo trata da rotacao de elementos. Uma vez que estamos lidando
    //  com um ambiente 2D, a rotacao sera feita somente em torno do eixo Z.
    public static Matrix4f rotate(float angle) {
        Matrix4f result = identity();
        
        //  Para rotacionar um objeto em torno do eixo Z, podemos utilizar a 
        //  seguinte matriz de rotacao:
        
        // [cos(k)   -sin(k)   0   0] 
        // [sin(k)    cos(k)   0   0] 
        // [0         0        1   0] 
        // [0         0        0   1]
        // Onde, k eh o angulo passado por paramento para o metodo.
        
        
        float r = (float) Math.toRadians(angle);
        float cos = (float) Math.cos(r);
        float sin = (float) Math.sin(r);
        
        //  Define os elementos modificados na primeira coluna
        //  Onde result.elements[linha + coluna * larguraDaMatriz]
        result.elements[0 + 0 * 4] = cos;
        result.elements[1 + 0 * 4] = sin;
        
        //  Define os elementos modificados na segunda coluna
        //  Onde result.elements[linha + coluna * larguraDaMatriz]
        result.elements[0 + 1 * 4] = -sin;
        result.elements[1 + 1 * 4] = cos;
        
        return result;        
    }
    
    
    
    //  Este metodo cria uma matriz de projecao ortografica que tem como 
    //  objetivo ser utilizada para renderizacao 2D. A ideia de utlizar uma  
    //  matriz projetcao ortografica eh utilizada para criar os elementos na
    //  na tela.
    public static Matrix4f orthographic(float left, float right, float botton, float top, float near, float far) {
        Matrix4f result = identity();
        
        // A matriz de projecao ortografica pode ser definida como:
        
        // [2/(l - r)   0           0           (l + r)/(l - r)] 
        // [0           2/(t - b)   0           (b + t)/(b - t)] 
        // [0           0           2/(n - f)   (n + f)/(f - n)] 
        // [0           0           0           1              ]
       
        //  Onde, 
        //  l = left
        //  r = right
        //  t = top
        //  b = bottom
        //  n = near
        //  f = far

        //  E tambem left, right, top, bottom sao referentes as margens do nossa 
        //  janela. Qualquer coisa fora desse espaco nao eh renderizado.
        //  Tambem near e far determinam a distancia a qual renderizamos esses
        //  objetos. Tudo que esteja muito perto, ou muito longe nao sera 
        //  redenrizado. 
        
        
        //  Onde, result.elements[linha + coluna * larguraDaMatriz]
        result.elements[0 + 0 * 4] = 2.0f / (right - left);
        result.elements[1 + 1 * 4] = 2.0f / (top - botton);
        result.elements[2 + 2 * 4] = 2.0f / (near - far);
       
        result.elements[0 + 3 * 4] = (left + right) / (left - right);
        result.elements[1 + 3 * 4] = (botton + top) / (botton - top);
        result.elements[2 + 3 * 4] = (far + near) / (far - near);
                
        return result;
    }
    
    //goes from float array to flot buffer
    //  A bliblioteca grafica que estamos utilizando nao trabalha diretamente
    //  com arrays de floats, mas sim com um buffer de floats. Por isso eh
    //  necessario converter a matriz que criamos para uma FloatBuffer.
    public FloatBuffer toFloatBuffer() {
        return BufferUtils.createFloatBuffer(elements);
    }
}
