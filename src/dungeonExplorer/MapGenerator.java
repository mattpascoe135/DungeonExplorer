package dungeonExplorer;

import java.util.Random;

public class MapGenerator {
	/**
	 * 
	 * @param args
	 * 		MapGenerator width height,		e.g. MapGenerator 50 50
	 */
	public static void main(String[] args){
		/* Get the variables */
		int width;
		int height;
		try{
			width = Integer.parseInt(args[0]);
			height = Integer.parseInt(args[1]);
		} catch(NumberFormatException e) { 
			System.out.println("Invalid width and/or height");
			return;
		}
			
		char[][] map = genMap(width, height, 3, 10);
        
	}
    
    /**
     * 
     * @param width, width of the dungeon
     * @param height, height of the dungeon
     * @return
     *         return the completed map of the gird
     */
    public static char[][] genMap(int width, int height, int minSize, int maxSize){
        /* 
         * Create an empty grid, fill with blank spaces
         */
        char[][] map = new char[height][width];
        for(int i=0; i<height; i++){
            for(int j=0; j<width; j++){
                map[i][j] = '#';
            }
        }
        
        /*
         * Create a room in the center of the dungeon, place a ladder leading back up to the previous floor
         *         Where '@' symbol leads back to the previous level
         */
        Random rand = new Random();
        int roomHeight = minSize + rand.nextInt(maxSize-minSize);
        int roomWidth = minSize + rand.nextInt(maxSize-minSize);
        for(int i=0; i<roomHeight; i++){
            for(int j=0; j<roomWidth; j++){
                map[height/2-roomHeight/2+i][width/2-roomWidth/2+j] = '.';
            }
        }
        map[height/2][width/2]= '@';
        
        /*
         * Loop around until dungeon is complete
         * Find a wall and if there is enough space then create a random feature
         *         This includes:
         *                 - Room
         *                 - Corridor
         */
        int dungeonComplete = 0;
        while(dungeonComplete == 0){
            /*
             * Find a wall with enough space for a room or corridor
             */
            int locationFound = 0;
            while(locationFound == 0){
                /*
                 * Find a random location along a wall, check if it can fit either a room or corridor
                 *         TODO: Check to ensure that the outside of the room is surrounded by walls, so that it isnt overlapping into annother room
                 */
                int wallFound = 0;
                char direction = 'c';
                int x = 0;
                int y = 0;
                while(wallFound == 0){
                    x = rand.nextInt(width);
                    y = rand.nextInt(height);
                    //System.out.println("X: " + x + ", Y:" + y);
                    if(map[y][x] == '#'){
                        int count = 0;
                        if(y != 0){
                            if(map[y-1][x] == '.'){        //Check North
                                count++;
                                direction = 'S';
                            }
                        }
                        if(x < width-1){
                            if(map[y][x+1] == '.'){        //Check East
                                count++;
                                direction = 'W';
                            }
                        }
                        if(y < height-1){
                            if(map[y+1][x] == '.'){        //Check South
                                count++;
                                direction = 'N';
                            }
                        }
                        if(x != 0){
                            if(map[y][x-1] == '.'){        //Check West
                                count++;
                                direction = 'E';
                            }
                        }
                        if(count == 1){
                            wallFound = 1;
                        }
                    }
                }
                
                int feature = 20; //rand.nextInt(100);
                if(feature < 60){        //Check for a room, with a 60% chance
                    roomHeight = minSize + rand.nextInt(maxSize-minSize);
                    roomWidth = minSize + rand.nextInt(maxSize-minSize);
                    if(x+roomWidth+2 < width-1 && x-roomWidth > 0 && y+roomHeight+2 < height-1 && y-roomHeight > 0){
                        if(direction == 'N'){
                            int validLocation= 1;
                            for(int i=0; i<roomHeight+2; i++){
                                for(int j=0; j<roomWidth+2; j++){
                                    if(map[y-roomHeight-1+i][x-roomWidth/2-1+j] == '.'){
                                        validLocation = 0;
                                    }
                                }
                            }
                            if(validLocation == 1){
                                for(int i=0; i<roomHeight; i++){
                                    for(int j=0; j<roomWidth; j++){
                                        map[y-roomHeight+i][x-roomWidth/2+j] = '.';
                                    }
                                }
                                map[y][x] = 'D';
                            }
                        }else if(direction == 'E'){
                            int validLocation= 1;
                            for(int i=0; i<roomHeight+2; i++){
                                for(int j=0; j<roomWidth+2; j++){
                                    if(map[y-roomHeight/2-1+i][x+j] == '.'){
                                        validLocation = 0;
                                    }
                                }
                            }
                            if(validLocation == 1){
                                for(int i=0; i<roomHeight; i++){
                                    for(int j=0; j<roomWidth; j++){
                                        map[y-roomHeight/2+i][x+1+j] = '.';
                                    }
                                }
                                map[y][x] = 'D';
                            }
                        }else if(direction == 'S'){
                            int validLocation= 1;
                            for(int i=0; i<roomHeight+2; i++){
                                for(int j=0; j<roomWidth+2; j++){
                                    if(map[y+i][x-roomWidth/2-1+j] == '.'){
                                        validLocation = 0;
                                    }
                                }
                            }
                            if(validLocation == 1){
                                for(int i=0; i<roomHeight; i++){
                                    for(int j=0; j<roomWidth; j++){
                                        map[y+1+i][x-roomWidth/2+j] = '.';
                                    }
                                }
                                map[y][x] = 'D';
                            }
                        }else if(direction == 'W'){
                            int validLocation= 1;
                            for(int i=0; i<roomHeight+2; i++){
                                for(int j=0; j<roomWidth+2; j++){
                                    if(map[y-roomHeight/2-1+i][x-roomWidth-1+j] == '.'){
                                        validLocation = 0;
                                    }
                                }
                            }
                            if(validLocation == 1){
                                for(int i=0; i<roomHeight; i++){
                                    for(int j=0; j<roomWidth; j++){
                                        map[y-roomHeight/2+i][x-roomWidth+j] = '.';
                                    }
                                }
                                map[y][x] = 'D';
                            }
                        }
                    }
                }else{                    //Check for a corridor, with a 40% chance
                    int length = rand.nextInt(maxSize-minSize);
                    System.out.println("Corridor");
                    
                }
                locationFound = 1;
            }
            /*
             * Check to see if the map has been filled enough
             */
            int noEmptySpace = 0;
            for(int i=0; i<height; i++){
                for(int j=0; j<width; j++){
                    if(map[i][j] == '#'){
                        noEmptySpace++;
                    }
                }
            }
            if(noEmptySpace < width*height*7/10){        //Check if there is less than 70% empty space
                dungeonComplete = 1;
            }
        }
        
        /*
         * Place the stairs leading downstairs to the next level
         */
        int exitPlaced = 0;
        while(exitPlaced == 0){
        	int x = rand.nextInt(width-2)+1;
            int y = rand.nextInt(height-2)+1;
            if(map[y][x] == '.' && map[y-1][x] == '.' && map[y][x-1] == '.' && map[y+1][x] == '.' && map[y][x+1] == '.'){
            	map[y][x] = 'e';
            	exitPlaced = 1;
            }
        }
        
        for(int i=0; i<height; i++){
            String line = "";
            for(int j=0; j<width; j++){
                line += map[i][j];
            }
            System.out.println(line);
        }
        return null;
    }
}
