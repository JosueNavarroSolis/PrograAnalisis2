/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto2aa;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javax.imageio.ImageIO;

/**
 *
 * @author Z170
 */
public class TreeGrower {
    
    //directorio de imagenes objetivo
    private static final String OBJECTIVE_IMG_DIR = "./objectiveImages/";
    //nombre de imagen objetivo
    private static final String OBJECTIVE_FILENAME = "37.png";
    //arbol objetivo procesado para comparacion
    private ArrayList<Boolean> objectiveTree;
    
    
    private ArrayList<FractalTree> currentGen;
    //lista de todas las generaciones
    private ArrayList<ArrayList<FractalTree>> generations;
    //promedio de aptitud de las generaciones
    private ArrayList<Float> generationsMeanFitScore;
    //acumulador de puntaje de aptitud para la generacion actual
    private float currGenFitnessSum;
    
    //individuo mas apto
    private FractalTree fittestIndividual;
    //generacion mas apta
    private int fittestGenerationNumber;
    private float fittestGenerationScore;
    
    //PARAMETROS DE SIMULACION
    private final int maxPopulation = 10;
    
    private final float minTreeWidth = 10;
    private final float maxTreeWidth = 50;
    private final float minWidthDecrement = 0.1f;
    private final float maxWidthDecrement = 0.6f;
    
    private final float minTreeLength = 40;
    private final float maxTreeLength = 170;
    private final float minLengthDecrement = 0.1f;
    private final float maxLengthDecrement = 0.5f;
    
    private final float minForkAngle = 20;
    private final float maxForkAngle = 180;
    
    private final int maxBranches = 4;
    private final int minTreeDepth = 6;
    private final int maxTreeDepth = 11;
    
    //metodo que maneja la simulacion completa
    public void simulate(int iterations, int treesPerIndividual) throws IOException{
        File objectiveTreeFile = new File(OBJECTIVE_IMG_DIR+OBJECTIVE_FILENAME);
        objectiveTree = leerArbol(objectiveTreeFile);
        createFirstGeneration();
        
        
        for(int i = 0; i<iterations; i++){
            //realizacion fitness
            currGenFitnessSum = 0f;
            for(FractalTree ft : currentGen){
                float individualFitScore = 0;
                for(int fitTestIndex = 0; fitTestIndex < treesPerIndividual; fitTestIndex++){
                    ft.generateTree();
                    ArrayList<Boolean> fractalImage = leerArbol(new File(ft.getImageFilename()));
                    individualFitScore += fitness(fractalImage);
                }
                
                currGenFitnessSum += individualFitScore/treesPerIndividual;
                ft.setFitScore(individualFitScore/treesPerIndividual);
                if(ft.getFitScore() > fittestIndividual.getFitScore()){
                    fittestIndividual = ft;
                }
            }
            
            ArrayList<FractalTree> nextGen = new ArrayList<>();
            while(nextGen.size() < maxPopulation){
                //seleccion
                FractalTree[] selectedParents = selection();
                //reproduccion
                FractalTree[] offsprings = reproduction(selectedParents);
                nextGen.add(offsprings[0]);
                nextGen.add(offsprings[1]);
            }
            
            //promedio de fitness de generacion
            System.out.println(currGenFitnessSum/(float)maxPopulation);
            generationsMeanFitScore.add(currGenFitnessSum/(float)maxPopulation);
            generations.add(currentGen);
            if(generationsMeanFitScore.get(i) > fittestGenerationScore){
                fittestGenerationNumber = i;
                fittestGenerationScore = generationsMeanFitScore.get(i);
            }
            //cambio de generacion
            currentGen = nextGen;
        }
    }
    
    //metodo para crear la primer generacion completamente aleatoria
    private void createFirstGeneration(){
        generations = new ArrayList<>();
        currentGen = new ArrayList<>();
        generationsMeanFitScore = new ArrayList<>();
        
        Random r = new Random();
        for(int i = 0; i<maxPopulation; i++){
            FractalTree ft = new FractalTree(getRandomInitialWidth(), getRandomInitialLength(),
                                             getRandomWidthDecrementRange(), getRandomLengthDecrementRange(),
                                             getRandomForkAngleRange(), r.nextFloat(), getRandomBranchesRange(),
                                             getRandomTreeDepth(), 1);
            currentGen.add(ft);
        }
        fittestIndividual = currentGen.get(0);
        fittestGenerationNumber = 0;
        fittestGenerationScore = 0;
    }
    
    //metodo de seleccion. retorna un array con dos individuos distintos para reproducirse.
    private FractalTree[] selection(){
        Random r = new Random();
        FractalTree[] parents = new FractalTree[2];
        
        float probabilitySum = 0;
        float parentIndex = r.nextFloat();
        for(FractalTree ft : currentGen){
            if((probabilitySum <= parentIndex) && ((probabilitySum+(ft.getFitScore()/currGenFitnessSum)) > parentIndex)){
                parents[0] = ft;
            }
            probabilitySum += ft.getFitScore()/currGenFitnessSum;
        }
   
        do{
            probabilitySum = 0;
            parentIndex = r.nextFloat();
            for(FractalTree ft : currentGen){
                if((probabilitySum <= parentIndex) && ((probabilitySum+(ft.getFitScore()/currGenFitnessSum)) > parentIndex)){
                    parents[1] = ft;
                }
                probabilitySum += ft.getFitScore()/currGenFitnessSum;
            }
        }while(parents[0] == parents[1]);
        
        return parents;
    }
    
    //reproduce dos individuos pasados por un array FractalTree. retorna dos nuevos individuos.
    private FractalTree[] reproduction(FractalTree[] parents){
        FractalTree[] offsprings = new FractalTree[2];
        Random r = new Random();
        
        int crossIndex = r.nextInt(4);
        
        switch(crossIndex){
            case 0:
                offsprings[0] = new FractalTree(parents[0].getInitialWidth(), parents[1].getInitialLength(),
                                                parents[1].getWidthDecrementRange(), parents[1].getLengthDecrementRange(),
                                                parents[1].getForkAngleRange(), parents[1].getRandomAngleFactor(),
                                                parents[1].getBranchesRange(), parents[1].getTreeDepth(), 1);
                offsprings[1] = new FractalTree(parents[1].getInitialWidth(), parents[0].getInitialLength(),
                                                parents[0].getWidthDecrementRange(), parents[0].getLengthDecrementRange(),
                                                parents[0].getForkAngleRange(), parents[0].getRandomAngleFactor(),
                                                parents[0].getBranchesRange(), parents[0].getTreeDepth(), 1);
                break;
            case 1:
                offsprings[0] = new FractalTree(parents[0].getInitialWidth(), parents[0].getInitialLength(),
                                                parents[1].getWidthDecrementRange(), parents[1].getLengthDecrementRange(),
                                                parents[1].getForkAngleRange(), parents[1].getRandomAngleFactor(),
                                                parents[1].getBranchesRange(), parents[1].getTreeDepth(), 1);
                offsprings[1] = new FractalTree(parents[1].getInitialWidth(), parents[1].getInitialLength(),
                                                parents[0].getWidthDecrementRange(), parents[0].getLengthDecrementRange(),
                                                parents[0].getForkAngleRange(), parents[0].getRandomAngleFactor(),
                                                parents[0].getBranchesRange(), parents[0].getTreeDepth(), 1);
                break;
            case 2:
                offsprings[0] = new FractalTree(parents[0].getInitialWidth(), parents[0].getInitialLength(),
                                                parents[0].getWidthDecrementRange(), parents[1].getLengthDecrementRange(),
                                                parents[1].getForkAngleRange(), parents[1].getRandomAngleFactor(),
                                                parents[1].getBranchesRange(), parents[1].getTreeDepth(), 1);
                offsprings[1] = new FractalTree(parents[1].getInitialWidth(), parents[1].getInitialLength(),
                                                parents[1].getWidthDecrementRange(), parents[0].getLengthDecrementRange(),
                                                parents[0].getForkAngleRange(), parents[0].getRandomAngleFactor(),
                                                parents[0].getBranchesRange(), parents[0].getTreeDepth(), 1);
                break;
            case 3:
                offsprings[0] = new FractalTree(parents[0].getInitialWidth(), parents[0].getInitialLength(),
                                                parents[0].getWidthDecrementRange(), parents[0].getLengthDecrementRange(),
                                                parents[1].getForkAngleRange(), parents[1].getRandomAngleFactor(),
                                                parents[1].getBranchesRange(), parents[1].getTreeDepth(), 1);
                offsprings[1] = new FractalTree(parents[1].getInitialWidth(), parents[1].getInitialLength(),
                                                parents[1].getWidthDecrementRange(), parents[1].getLengthDecrementRange(),
                                                parents[0].getForkAngleRange(), parents[0].getRandomAngleFactor(),
                                                parents[0].getBranchesRange(), parents[0].getTreeDepth(), 1);
                break;
        }
        
        offsprings[0].setParent1(parents[0]);
        offsprings[0].setParent2(parents[1]);
        //mutacion hijo 1
        if(r.nextFloat() < 0.2f){
            mutation(offsprings[0]);
        }
        
        offsprings[1].setParent1(parents[0]);
        offsprings[1].setParent2(parents[1]);
        //mutacion hijo 2
        if(r.nextFloat() < 0.2f){
            mutation(offsprings[1]);
        }
        
        return offsprings;
    }
    
    //metodo de mutacion. recibe un individuo y modifica aleatoriamente un gen
    private void mutation(FractalTree individual){
        System.out.println("mutated");
        Random r = new Random();
        int mutationIndex = r.nextInt(8);
        switch(mutationIndex){
            case 0:
                individual.setInitialWidth(getRandomInitialWidth());
                break;
            case 1:
                individual.setInitialLength(getRandomInitialLength());
                break;
            case 2:
                individual.setWidthDecrementRange(getRandomWidthDecrementRange());
                break;
            case 3:
                individual.setLengthDecrementRange(getRandomLengthDecrementRange());
                break;
            case 4:
                individual.setForkAngleRange(getRandomForkAngleRange());
                break;
            case 5:
                individual.setRandomAngleFactor(r.nextFloat());
                break;
            case 6:
                individual.setBranchesRange(getRandomBranchesRange());
                break;
            case 7:
                individual.setTreeDepth(getRandomTreeDepth());
                break;
        }
    }
    
    //metodos de fitness
    
    //Metodo para procesar una imagen para comparacion
    public ArrayList<Boolean> leerArbol(File file) throws IOException {
        BufferedImage img = ImageIO.read(file);
        ArrayList<Boolean> imagen = new ArrayList<>();
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                int pixel = img.getRGB(x, y);
                Color color = new Color(pixel, true);

                int red = color.getRed();
                int green = color.getGreen();
                int blue = color.getBlue();
                if (red == 255 && green == 255 && blue == 255) {
                    imagen.add(false);
                } else {
                    imagen.add(true);
                }
            }
        }

        return imagen;
    }
   
    //metodo fitness para comparar dos imagenes procesadas por leerArbol()
    private float fitness(ArrayList<Boolean> arbolFractal) {
        float matches = 0;
        float pixeles_arbol_original, pixeles_arbol_fractal;
        pixeles_arbol_original = pixeles_arbol_fractal = 0;
        for (int i = 0; i < objectiveTree.size(); i++) {
            if (objectiveTree.get(i)) {
                pixeles_arbol_original++;
                if (arbolFractal.get(i) && objectiveTree.get(i)) {
                    matches++;
                    //System.out.println(matches);
                }
            }
            if (arbolFractal.get(i)) {
                pixeles_arbol_fractal++;
            }

        }
        return ((matches / pixeles_arbol_fractal) + (matches / pixeles_arbol_original));
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////
    
    //metodos para generar parametros (genes) aleatorios segun los rangos indicados
    private float getRandomInitialWidth(){
        Random r = new Random();
        return (r.nextFloat()*(maxTreeWidth - minTreeWidth)) + minTreeWidth;
    }
    
    private float[] getRandomWidthDecrementRange(){
        Random r = new Random();
        float[] widthDecrementRange = new float[2];
        widthDecrementRange[0] = (r.nextFloat()*(maxWidthDecrement-minWidthDecrement)) + minWidthDecrement;
        widthDecrementRange[1] = (r.nextFloat()*(maxWidthDecrement-widthDecrementRange[0])) + widthDecrementRange[0];
        return widthDecrementRange;
    }
    
    private float getRandomInitialLength(){
        Random r = new Random();
        return (r.nextFloat()*(maxTreeLength - minTreeLength)) + minTreeLength;
    }
    
    private float[] getRandomLengthDecrementRange(){
        Random r = new Random();
        float[] lengthDecrementRange = new float[2];
        lengthDecrementRange[0] = (r.nextFloat()*(maxLengthDecrement-minLengthDecrement)) + minLengthDecrement;
        lengthDecrementRange[1] = (r.nextFloat()*(maxLengthDecrement-lengthDecrementRange[0])) + lengthDecrementRange[0];
        return lengthDecrementRange;
    }
    
    private float[] getRandomForkAngleRange(){
        Random r = new Random();
        float[] forkAngleRange = new float[2];
        forkAngleRange[0] = (r.nextFloat()*(maxForkAngle-minForkAngle)) + minForkAngle;
        forkAngleRange[1] = (r.nextFloat()*(maxForkAngle-forkAngleRange[0])) + forkAngleRange[0];
        return forkAngleRange;
    }
    
    private int[] getRandomBranchesRange(){
        Random r = new Random();
        int[] branchesRange = new int[2];
        branchesRange[0] = r.nextInt(maxBranches)+1;
        branchesRange[1] = Math.round((r.nextFloat()*(maxBranches-branchesRange[0])) + branchesRange[0]);
        return branchesRange;
    }
    
    private int getRandomTreeDepth(){
        Random r = new Random();
        return r.nextInt(maxTreeDepth-minTreeDepth)+minTreeDepth;
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////
   
    //getters y setters
    public ArrayList<ArrayList<FractalTree>> getGenerations() {
        return generations;
    }

    public ArrayList<Float> getGenerationsMeanFitScore() {
        return generationsMeanFitScore;
    }

    public FractalTree getFittestIndividual() {
        return fittestIndividual;
    }

    public int getFittestGenerationNumber() {
        return fittestGenerationNumber;
    }

    public static void main(String[] args) throws IOException {
        TreeGrower tg = new TreeGrower();
        tg.simulate(10, 5);

        FractalGUI gui = new FractalGUI(OBJECTIVE_IMG_DIR + OBJECTIVE_FILENAME);
        gui.setSimulation(tg.getGenerations(), tg.getGenerationsMeanFitScore(), tg.getFittestIndividual(), tg.getFittestGenerationNumber());
        gui.setVisible(true);
    }

}
