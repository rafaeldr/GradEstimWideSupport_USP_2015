import ij.*;

public class DOM {
	
	static public ImageAccess gSobelX(ImageAccess input) {
		int nx = input.getWidth();
		int ny = input.getHeight();
		
		int tamGradiente = 9;
		
		// Janela
		double arr[][] = new double[tamGradiente][tamGradiente];
		// Kernel do Gradiente
		double kernelx[][] =  {	{-4,   -3,   -2,   -1,   0,   1,  2,  3,  4},
								{-5,   -4,   -3,   -2,   0,   2,  3,  4,  5},
								{-6,   -5,   -4,   -3,   0,   3,  4,  5,  6},
								{-7,   -6,   -5,   -4,   0,   4,  5,  6,  7},
								{-8,   -7,   -6,   -5,   0,   5,  6,  7,  8},
								{-7,   -6,   -5,   -4,   0,   4,  5,  6,  7},
								{-6,   -5,   -4,   -3,   0,   3,  4,  5,  6},
								{-5,   -4,   -3,   -2,   0,   2,  3,  4,  5},
								{-4,   -3,   -2,   -1,   0,   1,  2,  3,  4} };
		
		double pixel, cont;
		
		ImageAccess out = new ImageAccess(nx, ny);
		for (int x = 0; x < nx; x++) {
			for (int y = 0; y < ny; y++) {
				input.getNeighborhood(x, y, arr);
				
				pixel = 0.0;
				cont = 0;
				for (int i = 0; i<tamGradiente;i++)
				{
					for(int j = 0;j<tamGradiente;j++)
					{
						double value = arr[i][j]*kernelx[i][j];
						pixel += value;
						if(value!=0)
						{
							cont++;
						}
					}
				}
				if(cont!=0)
				{
					pixel = pixel/cont;
				}
				
				out.putPixel(x, y, pixel);
			}
		}
		return out;
	}
	
	static public ImageAccess gSobelY(ImageAccess input) {
		int nx = input.getWidth();
		int ny = input.getHeight();
		
		int tamGradiente = 9;
		
		// Janela
		double arr[][] = new double[tamGradiente][tamGradiente];
		// Kernel do Gradiente
		double kernely[][] =  { {-4,  -5,  -6,  -7,  -8,  -7,  -6,  -5,  -4},
								{-3,  -4,  -5,  -6,  -7,  -6,  -5,  -4,  -3},
								{-2,  -3,  -4,  -5,  -6,  -5,  -4,  -3,  -2},
								{-1,  -2,  -3,  -4,  -5,  -4,  -3,  -2,  -1},
								{0,    0,   0,   0,   0,   0,   0,   0,   0},
								{1,    2,   3,   4,   5,   4,   3,   2,   1},
								{2,    3,   4,   5,   6,   5,   4,   3,   2},
								{3,    4,   5,   6,   7,   6,   5,   4,   3},
								{4,    5,   6,   7,   8,   7,   6,   5,   4} };
		
		double pixel, cont;
		
		ImageAccess out = new ImageAccess(nx, ny);
		for (int x = 0; x < nx; x++) {
			for (int y = 0; y < ny; y++) {
				input.getNeighborhood(x, y, arr);
				
				pixel = 0.0;
				cont = 0;
				for (int i = 0; i<tamGradiente;i++)
				{
					for(int j = 0;j<tamGradiente;j++)
					{
						double value = arr[i][j]*kernely[i][j];
						pixel += value;
						if(value!=0)
						{
							cont++;
						}
					}
				}
				if(cont!=0)
				{
					pixel = pixel/cont;
				}
				
				out.putPixel(x, y, pixel);
			}
		}
		return out;
	}	
	
	static public ImageAccess gSobel(ImageAccess input) {
		int nx = input.getWidth();
		int ny = input.getHeight();
		
		ImageAccess outGx = new ImageAccess(nx, ny);
		ImageAccess outGy = new ImageAccess(nx, ny);
		ImageAccess result = new ImageAccess(nx, ny);
		
		outGx = gSobelX(input);
		outGy = gSobelY(input);
		
		outGx.pow(2.0);
		outGy.pow(2.0);
		result.add(outGx, outGy);
		result.sqrt();
		
		return result;
	}
	
	static public ImageAccess domBright(ImageAccess input)
	{
		int nx = input.getWidth();
		int ny = input.getHeight();
		double max = input.getMaximum();
		ImageAccess domImage = new ImageAccess(nx, ny);
		
		double valueIn = 0.0;
		double valueOut = 0.0;
		
		for (int x=0; x<nx; x++)
		{
			for(int y=0; y<ny; y++)
			{
				valueIn = input.getPixel(x,y);
				valueOut = valueIn/max;
				domImage.putPixel(x, y, valueOut);
			}
		}
		
		return domImage;
	}
	
	static public ImageAccess domDark(ImageAccess input)
	{
		int nx = input.getWidth();
		int ny = input.getHeight();
		ImageAccess domBrightImage = new ImageAccess(nx, ny);
		ImageAccess domDarkImage = new ImageAccess(nx, ny);
		
		domBrightImage = domBright(input);
		
		double value = 0.0;
		
		for (int x=0; x<nx; x++)
		{
			for(int y=0; y<ny; y++)
			{
				value = 1 - domBrightImage.getPixel(x,y);
				domDarkImage.putPixel(x, y, value);
			}
		}
		
		return domDarkImage;
	}
	
	static public double domBright(ImageAccess input, int x, int y)
	{
		double valueIn = input.getPixel(x, y);
		double valueOut = valueIn/input.getMaximum();
		
		return valueOut;
	}
	
	static public double domDark(ImageAccess input, int x, int y)
	{
		double valueIn = domBright(input,x,y);
		double valueOut = 1-valueIn;
	
		return valueOut;
	}
	
}
