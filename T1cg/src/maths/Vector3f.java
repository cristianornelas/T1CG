/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maths;

/**
 *
 * @author Cristiano
 */
public class Vector3f{
    //Valor z é o valor da ordem de renderização
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
