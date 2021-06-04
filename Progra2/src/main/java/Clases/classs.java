/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import Clases.GetPixels;

/**
 *
 * @author famil
 */
public class classs {
    
    public static void main(String args[]) throws IOException{
        GetPixels pixel = new GetPixels();
           
           File file = new File("C:\\Users\\famil\\OneDrive\\Documents\\GitHub\\PrograAnalisis2\\Progra2\\Imagenes\\1.png");
           pixel.imagenes =  new ArrayList<>();
           pixel.arbol_modelo =  new ArrayList<>();
           pixel.pixeles_arbol_fractal=0;
           pixel.pixeles_arbol_original=0;
           pixel.arbol_modelo = pixel.arbol(file);
           File file2 = new File("C:\\Users\\famil\\OneDrive\\Documents\\GitHub\\PrograAnalisis2\\Progra2\\Imagenes\\2.png");
           pixel.imagenes = pixel.arbol(file);
           pixel.fitness(pixel.arbol_modelo, pixel.imagenes);
           System.out.println(pixel.fitness(pixel.arbol_modelo, pixel.imagenes));
       }
}
