public class Mask extends GreyImage{

    public Mask(int dimX, int dimY, short data[]) throws Exception {
        super(dimX, dimY, data);

    }

    public double getSumWeights() {
        double sum = 0;
        for(int i = 0; i < this.data.length; i++) {
            sum += (double)this.data[i];
        }

        return sum;
    }

    public static Mask makeBall1() throws Exception {

        short[] data = {0,1,0,1,1,1,0,1,0};
        Mask im = new Mask(3,3, data);

        return im;


    }

    public static Mask makeBall2() throws Exception {

        short[] data = {0,0,1,0,0,
                        0,1,1,1,0,
                        1,1,1,1,1,
                        0,1,1,1,0,
                        0,0,1,0,0};
        Mask im = new Mask(9,9, data);

        return im;


    }

}
