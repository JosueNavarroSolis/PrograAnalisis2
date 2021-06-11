/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto2aa;

/**
 * Clase para generar id numericos
 * @author Z170
 */
public class IDGenerator {
    
    private int nextId;
    
    private IDGenerator() {
        nextId=0;
    }
    
    public static IDGenerator getInstance() {
        return IDGeneratorHolder.INSTANCE;
    }
    
    private static class IDGeneratorHolder {

        private static final IDGenerator INSTANCE = new IDGenerator();
    }
    
    public int getNewId(){
        return nextId++;
    }
}
