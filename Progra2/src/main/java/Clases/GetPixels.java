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
   public ArrayList<ArrayList<String>> imagenes;
   public ArrayList<String> arbol_modelo;
   public ArrayList<String> ListaFitness;
   public int posfit;
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
   public int cuenta(ArrayList<String> arbol){
       int pixeles_original=0;
       for(int i =0;i<arbol.size();i++){
           if (!arbol.get(i).equals("255:255:255")){
               pixeles_original++;
           }
       }
       return pixeles_original;
   }
   
   public int comparador(ArrayList<String> arbol,ArrayList<String> imagenes){
       int matches = 0;
       for(int i=0;i <arbol.size();i++){
           if (arbol.get(i).equals(imagenes.get(i)) && !arbol.get(i).equals("255:255:255")){
               matches++;
           }
       }
       return matches;
   }

    /**
     *
     * @param arbol
     * @param arbolesGeneracion
     * @param listaFitness
     * @param posfit
     */
    public void fitness(ArrayList<String> arbol,ArrayList<ArrayList<String>> arbolesGeneracion,ArrayList<String> listaFitness,int posfit){
       float matchearbol;
       float matchefractal;
       float matches;
       float total;
       for(int i = posfit;i < arbolesGeneracion.size();i++){
        matches= 0;
        matches = comparador(arbol, arbolesGeneracion.get(i));
        System.out.println(matches);
        matchefractal= matches/(cuenta(arbol));
        System.out.println(matchefractal);
        matchearbol = matches/cuenta(arbolesGeneracion.get(i));
        total = matchefractal+matchearbol;
        System.out.println(total);
        listaFitness.add(Float.toString(total));
            //
       }
   }       
}