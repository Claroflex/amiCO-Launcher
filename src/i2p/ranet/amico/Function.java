package i2p.ranet.amico;         
       
public class Function {           
	public static int abs(int i)
	{
		if (i<=0){
			i=i*(-1);
		}
		return i;
	}
	
	public static int getIntSize(int small,int medium,int large)
	{
		int width=AmiCO.mScreenWidth;
		int size=medium;
		if(width>0 && width<=480)
		{
			size=small;
		}else if(width>480 && width<=800){
			size=medium;
		}else{
			size=large;
		}
		return size;
	}	
}
