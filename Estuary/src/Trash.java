
import java.util.Random;
/**
 * class trash extends class obstacle
 * Trash can be cans or plastic rings
 * @author cryst
 *
 */
public class Trash extends Obstacle{
	
    /**
	 * Constructor for trash objects
	 * @param h the height of the image
	*/
    public Trash(int h, int fHeight){
        
    	super();
    	
        // Set the height to the one determined in XBar
        this.setHeight(h);
        
        Random r = new Random();
        
        // 50/50 chance of creating can or plastic rings
        if(r.nextBoolean()){
            
        	this.setValue(-1);
            this.setPoints(0);
            
            double widthScale = .63;
            int iHeight = fHeight/10;
            int iWidth = (int)(iHeight*widthScale);
            
            this.setImgWidth(iWidth);
            this.setImgHeight(iHeight);
            type = "can";
            
        }else{
            
        	this.setValue(-2);
            this.setPoints(0);
            
            double widthScale = 1.33;
            int iHeight = fHeight/10;
            int iWidth = (int)(iHeight*widthScale);
            
            this.setImgWidth(iWidth);
            this.setImgHeight(iHeight);
            type = "plastic_rings";
            
        }  
        
        this.loadImage(this.getImgWidth(),this.getImgHeight());
        
    }
    /**
     * calls obstacle hit method
     * @param player
     */
    public void hit(Player player) {
    	super.hit(player);
    	
    	
    }

}
