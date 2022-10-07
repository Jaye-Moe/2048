package com._2048._2048;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Tile {
    private Image imageFile;
    private ImageView image;
    private final int xPos;
    private final int yPos;
    private int tileValue;

    public Tile(int x, int y) {
        this.imageFile = new Image("file:images/blank.png");
        this.image = new ImageView(this.imageFile);
        this.xPos = x;
        this.yPos = y;
        this.tileValue = 0;
    }

    public void updateImage(){
        if(this.tileValue==0){
            this.imageFile = new Image("file:images/blank.png");
        }else {
            this.imageFile = new Image("file:images/" + this.tileValue + ".png");
        }
        this.image = new ImageView(this.imageFile);
    }

    public void setTileValue(int tileValue){
        this.tileValue = tileValue;
    }

    public ImageView getTile(){
        return this.image;
    }

    public int getXPos(){
        return this.xPos;
    }

    public int getYPos(){
        return this.yPos;
    }

    public int getTileValue(){
        return this.tileValue;
    }
}
