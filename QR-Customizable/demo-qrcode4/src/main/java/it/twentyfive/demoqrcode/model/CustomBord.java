package it.twentyfive.demoqrcode.model;

import java.awt.Color;
import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomBord {

    private String borderColor;
    private String bordSizes;
    private LogoUrl iconUrl;
    private int bordSizeTop;
    private int bordSizeRight;
    private int bordSizeBottom;
    private int bordSizeLeft;

    public Color getBorderColor(){
        return Color.decode(borderColor);
    }
    public ArrayList<Integer> setBordSizes(String bordSizes) {
        ArrayList<Integer> lista=new ArrayList<>();
        String[] bordSizeValues = bordSizes.split(" ");
        
        if (bordSizeValues.length == 4) {
            int bordSizeTop = Integer.parseInt(bordSizeValues[0]);
            int bordSizeRight = Integer.parseInt(bordSizeValues[1]);
            int bordSizeBottom = Integer.parseInt(bordSizeValues[2]);
            int bordSizeLeft = Integer.parseInt(bordSizeValues[3]);
            this.bordSizeTop = bordSizeTop;
            this.bordSizeRight = bordSizeRight;
            this.bordSizeBottom = bordSizeBottom;
            this.bordSizeLeft = bordSizeLeft;
            this.bordSizes = bordSizes; // Imposta il valore di bordSizes
            lista.add(bordSizeTop);
            lista.add(bordSizeRight);
            lista.add(bordSizeBottom);
            lista.add(bordSizeLeft);
        } else {
            // Gestire il caso in cui la stringa non contenga quattro valori
            throw new IllegalArgumentException("La stringa bordSizes deve contenere quattro valori interi separati da spazi.");
        }
        return lista;
    }
    
    
}
