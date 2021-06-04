/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.io.File;
import java.io.FileWriter;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.ArrayList;
       
public class GetPixels {
   public ArrayList<String> imagenes;
   public ArrayList<String> arbol_modelo;
   public float pixeles_arbol_fractal ;
   public float pixeles_arbol_original;
   
   public ArrayList<String> arbol(File file) throws IOException{
      BufferedImage img = ImageIO.read(file);
      System.out.println(img);
      ArrayList<String> imagen = new ArrayList<>();
      for (int y = 0; y < img.getHeight(); y++) {
         for (int x = 0; x < img.getWidth(); x++) {
            //Retrieving contents of a pixel
            int pixel = img.getRGB(x,y);
            //Creating a Color object from pixel value
            Color color = new Color(pixel, true);
            //Retrieving the R G B values
            int red = color.getRed();
            int green = color.getGreen();
            int blue = color.getBlue();
            imagen.add(red+":"+green+":"+blue);
         }
      }
      System.out.println("Archivo leido");
      return imagen;
   }
   
   public float comparador(ArrayList<String> arbol,ArrayList<String> imagenes){
       int matches=0;
       pixeles_arbol_original=pixeles_arbol_fractal=0;
       for(int i=0;i <arbol.size();i++){
           if (!arbol.get(i).equals("255:255:255")){
               pixeles_arbol_original++;
               if(arbol.get(i).equals(imagenes.get(i))){
                   matches++;
                   //System.out.println(matches);
               }
           }
           if (!imagenes.get(i).equals("255:255:255")){
               pixeles_arbol_fractal++;
           }
           
       }
       return matches;
   }
   
    public float fitness(ArrayList<String> arbol_origen,ArrayList<String> arboleFractal){
       float matchearbol=0;
       float matchefractal=0;
       float matches=0;
       float total=0;
       
       matches=comparador(arbol_origen, arboleFractal);
       matchefractal= matches/pixeles_arbol_original;
       matchearbol = matches/pixeles_arbol_fractal;
       total = matchefractal+matchearbol;
       return total;
   }       
}