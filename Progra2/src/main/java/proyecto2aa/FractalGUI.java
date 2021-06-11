/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto2aa;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

/**
 *
 * @author Z170
 */
public class FractalGUI extends JFrame {
    
    private final String objectiveFilename;
    
    public FractalGUI(String objectiveFilename){
        super("Fractal Tree");
        setBounds(100, 100, 1000, 700);
        setResizable(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        this.objectiveFilename = objectiveFilename;
    }
    
    //metodo para configurar la ventana principal del programa. 
    //Recibe la lista de generaciones, lista de promedio de puntaje por generacion, el individuo mas apto
    //y la generacion con promedio de aptitud mas alta.
    public void setSimulation(ArrayList<ArrayList<FractalTree>> generations, ArrayList<Float> generationsMeanFitScore,
                              FractalTree fittestIndividual, int fittestGenerationNumber) throws IOException{
        JPanel jp = new JPanel();
        jp.setLayout(new BoxLayout(jp, BoxLayout.Y_AXIS));
        JScrollPane scroll = new JScrollPane(jp);
        this.getContentPane().add(scroll);
        
        jp.add(new JLabel("Objective Tree"));
        JLabel objectiveImageLabel = new JLabel(new ImageIcon(objectiveFilename));
        jp.add(objectiveImageLabel);
        jp.add(new JLabel("Fittest generation: #"+(fittestGenerationNumber+1)));
        jp.add(new JLabel("Fittest individual: #"+fittestIndividual.getId()+" Fit score: "+Float.toString(fittestIndividual.getFitScore())));
        
        for(int i = 0; i < generations.size(); i++){
            jp.add(new JLabel("Generation #"+(i+1+" Mean Fit Score: "+Float.toString(generationsMeanFitScore.get(i)))));
            JPanel jptmp = new JPanel();
            
            for(int j= 0; j < generations.get(i).size(); j++){
                FractalTree individual = generations.get(i).get(j);
                Image a = getScaledImage(ImageIO.read(new File(individual.getImageFilename())), 100, 100);
                
                
                Icon icon = new ImageIcon(a);
                JButton button = new JButton("Tree id: "+individual.getId()+
                                             " Fit score: "+Float.toString(individual.getFitScore()), icon);
                button.setVerticalTextPosition(SwingConstants.BOTTOM);
                button.setHorizontalTextPosition(SwingConstants.CENTER);
                button.addActionListener((ActionEvent e) -> {
                    generateIndividualInformationWindow(individual);
                });
                jptmp.add(button);
            }
            
            jp.add(jptmp);
        }
        
        
    }
    
    //Metodo para generar una ventana con informacion del individuo seleccionado.
    private void generateIndividualInformationWindow(FractalTree individual){
        JFrame frame = new JFrame("Information Tree id: "+individual.getId());
        frame.setBounds(100, 100, 1000, 700);
        frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        JPanel jp = new JPanel();
        jp.setLayout(new BoxLayout(jp, BoxLayout.Y_AXIS));
        JPanel jpImages = new JPanel();
        
        JLabel individualImage = new JLabel(new ImageIcon(individual.getImageFilename()));
        JLabel objectiveImage = new JLabel(new ImageIcon(objectiveFilename));
        
        jpImages.add(individualImage);
        jpImages.add(objectiveImage);
        
        JPanel jpGenes = new JPanel();
        JLabel geneInfo = new JLabel();
        geneInfo.setText(individual.toHTMLString());
        jpGenes.add(geneInfo);
        
        jp.add(jpImages);
        jp.add(jpGenes);
        
        if(individual.getParent1()!= null){
            JPanel jpParents = new JPanel();
            JLabel parent1Label = new JLabel("Parent 1:");
            JLabel parent1Info = new JLabel();
            parent1Info.setText(individual.getParent1().toHTMLString());
            JLabel parent2Label = new JLabel("Parent 2:");
            JLabel parent2Info = new JLabel();
            parent2Info.setText(individual.getParent2().toHTMLString());
            jpParents.add(parent1Label);
            jpParents.add(parent1Info);
            jpParents.add(parent2Label);
            jpParents.add(parent2Info);
            jp.add(jpParents);
        }
        
        JScrollPane scroll = new JScrollPane(jp);
        
        frame.add(scroll);
        
        frame.setVisible(true);
        
    }
    
    //Metodo para escalar una imagen Obtenido de: https://stackoverflow.com/questions/6714045/how-to-resize-jlabel-imageicon
    private Image getScaledImage(Image srcImg, int w, int h){
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();

        return resizedImg;
    }
    
}
