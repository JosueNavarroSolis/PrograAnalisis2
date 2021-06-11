/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto2aa;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;

/**
 *
 * @author Z170
 */
public class FractalTree {
    
    //directorio de imagenes
    private final String IMG_DIR = "./treeImages/";
    //nombre de imagen del arbol
    private final String imageFilename;
    //id numerico
    private final int id;
    
    //Genes
    private float initialWidth;
    private float initialLength;
    private float[] widthDecrementRange;
    private float[] lengthDecrementRange;
    private float[] forkAngleRange;
    private float randomAngleFactor;
    private int[] branchesRange;
    private int treeDepth;
    private int maxDepthDecrement;
    
    //Padres
    private FractalTree parent1;
    private FractalTree parent2;
    
    //puntaje aptitud
    private float fitScore;
     
    
    public FractalTree(float initialWidth, float initialLength,
                       float[] widthDecrementRange, float[] lengthDecrementRange,
                       float[] forkAngleRange, float randomAngleFactor, int[] branchesRange, 
                       int treeDepth, int maxDepthDecrement) {
        
        this.id = IDGenerator.getInstance().getNewId();
        this.imageFilename =  IMG_DIR+Integer.toString(this.id)+".png";
        this.initialWidth = initialWidth;
        this.initialLength = initialLength;
        this.widthDecrementRange = widthDecrementRange;
        this.lengthDecrementRange = lengthDecrementRange;
        this.forkAngleRange = forkAngleRange;
        this.randomAngleFactor = randomAngleFactor;
        this.branchesRange = branchesRange;
        this.treeDepth = treeDepth;
        this.maxDepthDecrement = maxDepthDecrement;
        this.fitScore= 0f;
    }
 
    //Metodo que llama a dibujar el arbol y guarda el dibujo como una imagen png.
    public void generateTree(){
        BufferedImage bImg = new BufferedImage(400, 400, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = bImg.createGraphics();
        g2.fillRect(0, 0, 400, 400);
        g2.setColor(Color.black);
        drawTree(g2, 200, 400, -90, treeDepth, initialWidth, initialLength);
        
        try {
            ImageIO.write(bImg, "png", new File(imageFilename));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    //Metodo recursivo para dibujar arboles en el objeto g2. ( Modificado de: https://rosettacode.org/wiki/Fractal_tree#Java )
    private void drawTree(Graphics2D g2, int x1, int y1, double angle, int depth, float width, float length) {
        if (depth <= 0) return;
        Random r = new Random();
        int x2 = x1 + (int) (Math.cos(Math.toRadians(angle)) * length);
        int y2 = y1 + (int) (Math.sin(Math.toRadians(angle)) * length);
        
        g2.setStroke(new BasicStroke(width));
        g2.draw(new Line2D.Float(x1, y1, x2, y2));
        int branches = getRandomBranches();
        float forkAngle = getRandomForkAngle();
        float angleIncrement = forkAngle/(branches-1);
        double childAngle = angle - (forkAngle/2);
        if(branches == 1){
                childAngle = angle;
                angleIncrement = forkAngle;
            }
        for(int i = 0; i<branches; i++){
            float angleRandomness = r.nextFloat()*(angleIncrement/2)*(r.nextInt(3)-1)*randomAngleFactor;
            drawTree(g2, x2, y2, childAngle+angleRandomness, depth-getRandomDepthDecrement(), width*getRandomWidthDecrement(), length*getRandomLengthDecrement());
            childAngle += angleIncrement;
        }
    }
 
    //////////Metodos auxiliares para generacion de aleatorios segun rangos de parametros//////////
    private int getRandomBranches(){
        if(branchesRange[0]==branchesRange[1]){
            return branchesRange[0];
        }
        Random r = new Random();
        return r.nextInt(branchesRange[1]-branchesRange[0]+1)+branchesRange[0];
    }
    
    private float getRandomForkAngle(){
        Random r = new Random();
        return (r.nextFloat()*(forkAngleRange[1]-forkAngleRange[0]))+forkAngleRange[0];
    }
    
    private float getRandomWidthDecrement(){
        Random r = new Random();
        return 1 - ((r.nextFloat()*(widthDecrementRange[1]-widthDecrementRange[0]))+widthDecrementRange[0]);
    }
    
    private float getRandomLengthDecrement(){
        Random r = new Random();
        return 1 - ((r.nextFloat()*(lengthDecrementRange[1]-lengthDecrementRange[0]))+lengthDecrementRange[0]);
    }
    
    private int getRandomDepthDecrement(){
        Random r = new Random();
        return r.nextInt(maxDepthDecrement)+1;
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////
    
    
    
    //SETTERS

    public void setParent1(FractalTree parent1) {
        this.parent1 = parent1;
    }

    public void setParent2(FractalTree parent2) {
        this.parent2 = parent2;
    }

    public void setInitialWidth(float initialWidth) {
        this.initialWidth = initialWidth;
    }

    public void setInitialLength(float initialLength) {
        this.initialLength = initialLength;
    }

    public void setWidthDecrementRange(float[] widthDecrementRange) {
        this.widthDecrementRange = widthDecrementRange;
    }

    public void setLengthDecrementRange(float[] lengthDecrementRange) {
        this.lengthDecrementRange = lengthDecrementRange;
    }

    public void setForkAngleRange(float[] forkAngleRange) {
        this.forkAngleRange = forkAngleRange;
    }

    public void setRandomAngleFactor(float randomAngleFactor) {
        this.randomAngleFactor = randomAngleFactor;
    }

    public void setBranchesRange(int[] branchesRange) {
        this.branchesRange = branchesRange;
    }

    public void setTreeDepth(int treeDepth) {
        this.treeDepth = treeDepth;
    }

    public void setFitScore(float fitScore) {
        this.fitScore = fitScore;
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////

    //GETTERS
    
    public float getFitScore() {
        return fitScore;
    }

    public int getId() {
        return id;
    }

    public int getTreeDepth() {
        return treeDepth;
    }
    
    public String getImageFilename() {
        return imageFilename;
    }

    public float getInitialWidth() {
        return initialWidth;
    }

    public float getInitialLength() {
        return initialLength;
    }

    public float[] getWidthDecrementRange() {
        return widthDecrementRange;
    }

    public float[] getLengthDecrementRange() {
        return lengthDecrementRange;
    }

    public float[] getForkAngleRange() {
        return forkAngleRange;
    }

    public float getRandomAngleFactor() {
        return randomAngleFactor;
    }

    public int[] getBranchesRange() {
        return branchesRange;
    }

    public FractalTree getParent1() {
        return parent1;
    }

    public FractalTree getParent2() {
        return parent2;
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////
    
    
    //Metodo toString para interfaz
    public String toHTMLString(){
        String fractalTreeToStr = "<html><body>";
        fractalTreeToStr += "Tree id: "+getId()+"<br>"+
                            "Fitness Score: "+Float.toString(getFitScore())+"<br>"+
                            "Initial width: "+getInitialWidth()+"<br>"+
                            "Initial length: "+getInitialLength()+"<br>"+
                            "Width decrement range: ["+Float.toString(getWidthDecrementRange()[0]*100)+"%"+" - "+Float.toString(getWidthDecrementRange()[1]*100)+"%"+"]"+"<br>"+
                            "Length decrement range: ["+Float.toString(getLengthDecrementRange()[0]*100)+"%"+" - "+Float.toString(getLengthDecrementRange()[1]*100)+"%"+"]"+"<br>"+
                            "Branch angle range: ["+Float.toString(getForkAngleRange()[0])+" - "+Float.toString(getForkAngleRange()[1])+"]"+"<br>"+
                            "Random angle factor: "+Float.toString(getRandomAngleFactor()*100)+"%"+"<br>"+
                            "N of Branches range: ["+getBranchesRange()[0]+" - "+getBranchesRange()[1]+"]"+"<br>"+
                            "Tree depth: "+getTreeDepth();
        
        fractalTreeToStr += "</body></html>";
        
        return fractalTreeToStr;
    }
    
    
 
    //Para generar arboles manualmente
    /*
    public static void main(String[] args) {
        FractalTree ft = new FractalTree(20f, 100f, new float[]{0.3f, 0.5f}, new float[]{0.4f, 0.4f}, new float[]{40f,100f}, 0.5f, new int[]{3,4}, 10, 1);
        ft.generateTree();
    }
    */
    
}
