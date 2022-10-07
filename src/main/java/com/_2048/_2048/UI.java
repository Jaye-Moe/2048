package com._2048._2048;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import org.jetbrains.annotations.NotNull;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Random;

public class UI {
    private final ArrayList<Tile> tiles;
    private final ArrayList<Integer> oldTiles;
    private final Button startButton;
    private final Label scoreBoard;
    private int score;
    private final Label gameOver;
    private boolean gameIsOver;
    private final ImageView image;


    public UI(GridPane gridPane){
        this.tiles = new ArrayList<>();
        this.oldTiles = new ArrayList<>();
        this.startButton = new Button();
        this.scoreBoard = new Label();
        this.score = 0;
        this.generateStartButton();
        this.generateScoreBoard();
        this.gameOver = new Label();
        this.generateGameOver();
        this.gameIsOver= false;
        Image imageFile = new Image("file:images/reset_button.png");
        this.image = new ImageView(imageFile);
        this.image.setOnMouseClicked(e-> resetGame(gridPane));
    }

    public void resetGame(GridPane gridPane){
        this.gameIsOver = false;

        //RESET THE SCORE TO ZERO
        System.out.println("Reset button clicked");
        NumberFormat nf= NumberFormat.getInstance();
        nf.setMaximumFractionDigits(0);
        this.score = 0;
        this.scoreBoard.setText("Score: " + nf.format(this.score));

        //CLEAR THE BOARD AND RE-GENERATE TWO STARTING TILES
        this.tiles.clear();
        this.generateBoard();
        this.addTile();
        this.addTile();

        this.updateSquares(gridPane);
    }

    public void increaseScore(int pointsToAdd){
        //INCREASE THE SCORE AND REPRINT THE SCORE BOARD
        NumberFormat nf= NumberFormat.getInstance();
        nf.setMaximumFractionDigits(0);
        this.score = this.score + pointsToAdd;
        this.scoreBoard.setText("Score: " + nf.format(this.score));
    }

    public void generateStartButton(){
        this.startButton.setText("Start Game");
        this.startButton.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 12));
    }

    public void generateGameOver(){
        this.gameOver.setText("No more valid moves");
        this.gameOver.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 16));
    }

    public void generateScoreBoard(){
        this.scoreBoard.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 16));

        NumberFormat nf= NumberFormat.getInstance();
        nf.setMaximumFractionDigits(0);

        this.scoreBoard.setText("Score: " + nf.format(this.score));
    }

    public void generateBoard(){
        //GENERATE A BOARD 4 TILES ACROSS AND 4 TILES DOWN
        int x = 1;
        int y = 1;

        while (x <= 4) {
            while (y <= 4) {
                Tile tile = new Tile(x, y);
                this.tiles.add(tile);
                y++;
            }
            x++;
            y = 1;
        }
    }

    public void updateSquares(@NotNull GridPane gridPane){
        gridPane.getChildren().clear();                         //CLEAR THE CURRENT BOARD
//        gridPane.add(this.startButton,4,0,1,1);
        if(this.gameIsOver){
            gridPane.add(this.gameOver,3,0,2,1);
            gridPane.add(getResetButton(),4,5,1,1);
        }
        gridPane.add(this.scoreBoard,1,0,2,1);      //RE-ADD THE SCOREBOARD TO THE BOARD
        for (Tile tile : this.tiles) {                          //LOOP THROUGH THE TILES AND ADD THEM TO THE BOARD
            tile.updateImage();
            gridPane.add(tile.getTile(), tile.getXPos(), tile.getYPos());
        }
    }

    public void setOldTiles(){
        //GENERATE A LIST OF THE CURRENT TILES TO LATER BE USED TO COMPARE TO THE UPDATED TILES
        this.oldTiles.clear();
        int i = 0;
        while (i < 16){
            this.oldTiles.add(tiles.get(i).getTileValue());
            i++;
        }
    }

    public boolean checkForChanges(){
        //COMPARE THE LIST OF THE OLD TILES TO THE NEW TILES TO SEE IF THERE ARE ANY CHANGES
        int i = 0;
        while(i < 16){
            if(this.oldTiles.get(i) != this.tiles.get(i).getTileValue()) {
                return true;
            }
            i++;
        }
        return false;
    }

    public boolean checkForValidMoves(){

        //LOOP THROUGH THE TILES AND RETURN TRUE IF THERE ARE ANY BLANKS
        int z = 0;
        while (z<16){
            if (this.tiles.get(z).getTileValue()==0){
                System.out.println("Board Has Blanks");
                return true;
            }
            z++;
        }

        //CHECK CELLS VERTICALLY FOR ANY CELLS THAT CAN BE ADDED TOGETHER
        int pass = 0;
        while (pass <4){
            int firstCellToCheck = 4*pass;
            int secondCellToCheck = (4*pass)+1;

            while (secondCellToCheck <4+(pass*4)){
                if (this.tiles.get(firstCellToCheck).getTileValue()==this.tiles.get(secondCellToCheck).getTileValue()){
                    return true;
                }
                firstCellToCheck++;
                secondCellToCheck++;
            }
            pass++;
        }

        //CHECK CELLS HORIZONTALLY
        pass = 0;
        while (pass <4){
            int firstCellToCheck = pass;
            int secondCellToCheck = pass + 4;

            while (secondCellToCheck<=12+pass){
                if (this.tiles.get(firstCellToCheck).getTileValue()==this.tiles.get(secondCellToCheck).getTileValue()){
                    return true;
                }
                firstCellToCheck = firstCellToCheck+4;
                secondCellToCheck = secondCellToCheck+4;
            }
            pass++;
        }
        System.out.println("NO MORE VALID MOVES");
            return false;

    }

    public boolean checkForBlanks(){
        //LOOP THROUGH THE TILES AND RETURN TRUE IF THERE ARE ANY BLANKS
        int z = 0;
        while (z<16){
            if (this.tiles.get(z).getTileValue()==0){
                System.out.println("Board Has Blanks");
                return true;
            }
            z++;
        }
        return false;
    }

    public void addTile(){
        Random rand = new Random();
        if (this.checkForBlanks()) {
            while (true) {                                                //LOOP UNTIL A TILE IS PLACED
                int newSpace = rand.nextInt(16);                  //GENERATE A NEW VALUE BETWEEN FROM 0 TO 15 (16 NUMBERS)
                System.out.println("Trying spot: " + newSpace);
                if (this.tiles.get(newSpace).getTileValue() == 0) {        //CHECK TO SEE IF THE RANDOM SPOT IS EMPTY OR NOT
                    int twoOrFour = rand.nextInt(10);             //GENERATE A NUMBER BETWEEN 0 AND 9 (10 NUMBERS)
                    if (twoOrFour == 0) {                               //10% CHANCE TO GENERATE A 4
                        this.tiles.get(newSpace).setTileValue(4);
                    } else {
                        this.tiles.get(newSpace).setTileValue(2);   //90% CHANGE TO GENERATE A 2
                    }
                    break;
                }
            }
        }else{
            System.out.println("NO WHERE TO PLACE A TILE");
        }
    }

    public void move(@NotNull String direction, GridPane gridPane){
        if (checkForValidMoves()) {
            this.setOldTiles();                                             //CREATE A LIST OF THE CURRENT TILES BEFORE MOVING
            if (direction.equals("UP")) {
                this.moveUp();
            }
            if (direction.equals("DOWN")) {
                this.moveDown();
            }
            if (direction.equals("RIGHT")) {
                this.moveRight();
            }
            if (direction.equals("LEFT")) {
                this.moveLeft();
            }

            this.updateSquares(gridPane);                                   //UPDATE THE BOARD AFTER THE TILES HAVE BEEN MOVED
            if (this.checkForChanges()) {                                     //CHECK TO SEE IF THE BOARD HAS CHANGED FROM PRIOR TO THE MOVE
                this.addTile();                                             //ADD A NEW TILE IF THE TILES HAVE MOVED
                this.updateSquares(gridPane);                               //UPDATE THE BOARD AFTER ADDING A TILE
            }
        }else{
            gameIsOver = true;
            this.updateSquares(gridPane);
            System.out.println("NO MORE VALID MOVES!");
        }
    }

    public void moveDown() {
        //CREATE A LIST OF TILES TO MOVE
        ArrayList<Integer> tilesToMove = new ArrayList<>();

        int pass = 0;
        while (pass < 4) {              //FOUR PASSES, ONE FOR EACH COLUMN
            tilesToMove.clear();        //CLEAR THE LIST OF TILES TO MOVE AT THE START OF EACH PASS (COLUMN)
            int x = pass * 4;   //TILES (X) START AT ZERO AND INCREASE BY 4 FOR EACH PASS
            while (x < (4 + (pass * 4))) {      //LOOP THROUGH TILES UNTIL THE FOURTH TILE.
                if (this.tiles.get(x).getTileValue() != 0) {
                    tilesToMove.add(this.tiles.get(x).getTileValue());    //ADD NON-ZERO TILES TO THE LIST OF TILES TO MOVE
                }
                this.tiles.get(x).setTileValue(0);  //NOT SURE IF THIS IS NEEDED ANYMORE, BUT DO NOT WANT TO REMOVE FOR NOW
                x++;
            }

            int length = tilesToMove.size();    //LOOP THROUGH THE LIST OF TILES TO MOVE
            if (length>=1){
                while (length>1){
                    if (tilesToMove.get(length-1) ==(int)tilesToMove.get(length-2)){    //IF THE LAST AND SECOND TO LAST TIME ARE THE SAME
                        int newValue = tilesToMove.get(length-1) + (int)tilesToMove.get(length-2);  //THEN COMBINE THEM
                        tilesToMove.set(length-1,newValue);      //SET THE LAST TILE TO THE COMBINED VALUE
                        tilesToMove.set(length-2,0);            //SET THE SECOND TO LAST TILE TO ZERO
                        this.increaseScore(newValue);           //INCREASE THE SCORE BY THE COMBINED VALUE
                    }
                    length--;
                }
            }

            tilesToMove.removeIf( name -> name.equals(0));  //REMOVE ANY ZEROS FROM THE LIST OF TILES THAT WERE GENERATED FROM COMBING TILES

            int lastLine = 3 + (pass * 4);
            length = tilesToMove.size();

            //MOVE ALL THE CELLS DOWN TO THE END
            while (length > 0) {
                int newValue = tilesToMove.get(length - 1);
                this.tiles.get(lastLine).setTileValue(newValue);    //MOVE THE LAST ITEM ON THE TILES TO MOVE LIST TO THE LAST CELL
                lastLine = lastLine - 1;
                length = length - 1;
            }
        pass++;
        }
    }

    public void moveUp() {
        //SEE COMMENTS FOR MOVEDOWN().  THIS IS THE SAME EXCEPT THE DIRECTION OF THE MOVEMENT AND PASSES ARE DIFFERENT.
        ArrayList<Integer> tilesToMove = new ArrayList<>();

        int pass = 0;

        while (pass < 4) {
            tilesToMove.clear();

            int x = 3 + (pass * 4);
            while (x >= (pass * 4)) {
                if (this.tiles.get(x).getTileValue() != 0) {
                    tilesToMove.add(this.tiles.get(x).getTileValue());
                }
                this.tiles.get(x).setTileValue(0);
                x--;
            }

            int length = tilesToMove.size();
            if (length>=1){
                while (length>1){
                    if (tilesToMove.get(length-1) ==(int)tilesToMove.get(length-2)){
                        int newValue = tilesToMove.get(length-1) + (int)tilesToMove.get(length-2);
                        tilesToMove.set(length-1,newValue);
                        tilesToMove.set(length-2,0);
                        this.increaseScore(newValue);
                    }

                    length--;
                }
            }


            tilesToMove.removeIf(name -> name.equals(0));

            int lastLine = pass * 4;
            length = tilesToMove.size();

            //MOVE ALL THE CELLS DOWN TO THE END
            while (length > 0) {
                int newValue = tilesToMove.get(length - 1);
                this.tiles.get(lastLine).setTileValue(newValue);
                lastLine = lastLine + 1;
                length = length - 1;
            }
            pass++;
        }



        }

    public void moveRight(){
        //SEE COMMENTS FOR MOVEDOWN().  THIS IS THE SAME EXCEPT THE DIRECTION OF THE MOVEMENT AND PASSES ARE DIFFERENT.
        ArrayList<Integer> tilesToMove = new ArrayList<>();

        int pass = 0;

        while (pass < 4) {
            tilesToMove.clear();

            int x = 12 +pass;
            while (x >= pass) {
                if (this.tiles.get(x).getTileValue() != 0) {
                    tilesToMove.add(this.tiles.get(x).getTileValue());
                }
                this.tiles.get(x).setTileValue(0);
                x=x-4;
            }

            int length = tilesToMove.size();
            if (length>=1){
                while (length>1){
                    if (tilesToMove.get(length-1) ==(int)tilesToMove.get(length-2)){
                        int newValue = tilesToMove.get(length-1) + (int)tilesToMove.get(length-2);
                        tilesToMove.set(length-1,newValue);
                        tilesToMove.set(length-2,0);
                        this.increaseScore(newValue);
                    }

                    length--;
                }
            }


            tilesToMove.removeIf(name -> name.equals(0));

            int lastLine = 12 + pass;
            length = tilesToMove.size();

            //MOVE ALL THE CELLS DOWN TO THE END

            x = 0;
            while (length > 0) {
                int newValue = tilesToMove.get(x);
                this.tiles.get(lastLine).setTileValue(newValue);
                lastLine = lastLine - 4;
                length = length - 1;
                x++;
            }
            pass++;
        }

    }

    public void moveLeft(){
        //SEE COMMENTS FOR MOVEDOWN().  THIS IS THE SAME EXCEPT THE DIRECTION OF THE MOVEMENT AND PASSES ARE DIFFERENT.
        ArrayList<Integer> tilesToMove = new ArrayList<>();

        int pass = 0;

        while (pass < 4) {
            tilesToMove.clear();

            int x = pass;
            while (x <= 12+(pass)) {
                if (this.tiles.get(x).getTileValue() != 0) {
                    tilesToMove.add(this.tiles.get(x).getTileValue());
                }
                this.tiles.get(x).setTileValue(0);
                x=x+4;
            }

            int length = tilesToMove.size();

            if (length >=1){
                while (length>1){
                    if (tilesToMove.get(length-1) ==(int)tilesToMove.get(length-2)){
                        int newValue = tilesToMove.get(length-1) + (int)tilesToMove.get(length-2);
                        tilesToMove.set(length-1,newValue);
                        tilesToMove.set(length-2,0);
                        this.increaseScore(newValue);
                    }

                    length--;
                }
            }


            tilesToMove.removeIf(name -> name.equals(0));

            int lastLine = pass;
            length = tilesToMove.size();

            //MOVE ALL THE CELLS DOWN TO THE END
            x = 0;
            while (length > 0) {
                int newValue = tilesToMove.get(x);
                this.tiles.get(lastLine).setTileValue(newValue);
                lastLine = lastLine + 4;
                length = length - 1;
                x++;
            }
            pass++;
        }
    }

    public ArrayList<Tile> getTiles(){
        return this.tiles;
    }

    public ImageView getResetButton(){
        return this.image;
    }




}
