import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public class GreyImage {
    protected short[] data;
    private final int dimX;
    private final int dimY;
    private final int size;

    public GreyImage(int dimX, int dimY) throws Exception {
        this.dimX = dimX;
        this.dimY = dimY;
        this.size = dimX*dimY;
        this.data = new short[size];

        if (dimX*dimY != data.length) {
            throw new Exception("Dimension pas bonne");
        }
    }

    public GreyImage(int dimX, int dimY, short[] data) throws Exception {

        if (dimX*dimY != data.length) {
            throw new Exception("Dimension pas bonne");
        }
        this.dimX = dimX;
        this.dimY = dimY;
        this.size = dimX*dimY;

        this.data = data;


    }

    public GreyImage(GreyImage image) {
        this.dimX = image.getSizeX();
        this.dimY = image.getSizeY();
        this.size = image.getSizeData();

        this.data = image.data;
    }

    public int getSizeX() {
        return dimX;
    }

    public int getSizeY() {
        return dimY;
    }

    public int getSizeData() {
        return size;
    }

    public boolean isPosValid(int x, int y) {
        return x * dimY + y >= 0 && x * dimY + y <= size;
    }

    public boolean isPosValid(int offset) {
        return offset >= 0 && offset <= size;
    }


    public short getPixel(int x, int y) {
        if(!isPosValid(x,y)) {
            return -1;
        }
        return data[y * dimX + x];
    }

    public void setPixel(int x, int y, short v) throws Exception {
        if(!isPosValid(x,y)) {
            throw new Exception();
        }
        data[y * dimX + x] = v;
    }

    public short getPixel(int offset)  {
        if(!isPosValid(offset)) {
            return -1;
        }
        return data[offset];
    }

    public void setPixel(int offset, short v) throws Exception {
        if(!isPosValid(offset)) {
            throw new Exception();
        }
        data[offset] = v;
    }


    public short getMax() {
        short max = data[0];
        for (int i = 1; i< data.length;i++) {
            if (data[i] > max) {
                max = data[i];
            }
        }
        return max;
    }

    public short getMin() {
        short min = data[0];
        for (int i = 1; i< data.length;i++) {
            if (data[i] < min) {
                min = data[i];
            }
        }
        return min;
    }

    public void negative() {
        short m = getMax();
        for (int i = 0; i<size; i++) {
            data[i] = (short) (m - data[i]);
        }
    }

    public void truncate(short min, short max) throws Exception {
        for (int i = 0; i<size; i++) {

            if(getPixel(i) < min) {
                setPixel(i, min);
            }

            if(getPixel(i) > max) {
                setPixel(i, max);
            }
        }
    }

    void seuiller(short seuil) {
        short max = this.getMax();
        for (int i = 0; i<this.size;i++) {
            if (data[i] < seuil) {
                data[i] = 0;
            }
            else {
                data[i] = max;
            }
        }
    }

    public static GreyImage loadPGM(String f) throws Exception {
        PGMFileIO l = new PGMFileIO(f);
        l.readPGM();

        short[] d = l.getData();
        GreyImage im = new GreyImage(l.getSizeX(), l.getSizeY(), d);
        return im;
    }

    public void savePGM(String f) throws IOException {
        PGMFileIO l = new PGMFileIO(f);

        l.writePGM(getSizeX(), getSizeY(), data);
    }

    public short[] getData() {
        return data;
    }

    void adjustContrast(int min, int max) {
        short gMin = this.getMin();
        short gMax = this.getMax();
        for(int i = 0; i<size; i++) {
            this.data[i] = (short) (min+(max-min)*(data[i]-gMin)/(gMax-gMin));
        }
    }

    void equalize(int min, int max)  {
        Histogram his = new Histogram(this);

        int[] cf = new int[his.getData().length];

        for(int i = 0; i<cf.length; i++) {
            if(i == 0) {
                cf[i] = his.getData()[i]+cf[i];
            }else {
                cf[i] = (his.getData()[i]+cf[i-1]);
            }
        }

        int gMin = this.getMin();


        for(int i=0;i<data.length;i++) {
            this.data[i] = (short)( (((max-min)*cf[data[i]-gMin])/size) + min);
        }

    }

    @Override
    public String toString() {
        return "GreyImage{" +
                "data=" + Arrays.toString(data) +
                '}';
    }

    public GreyImage convolve (Mask m) throws Exception {

        GreyImage filtre = null;
        try {
            filtre = new GreyImage(this.dimX, this.dimY);
        } catch (Exception e) {
            System.out.println("MARCHE PAS");
        }


        int p = (m.getSizeX() - 1) / 2;

        for(int i=p;i<this.getSizeX()-1;i++) {
            for(int j=p;j<this.getSizeY()-1;j++) {

                double sum=0;
                for(int k = 0;k<=2*p;k++) {
                    for(int l = 0; l<=2*p; l++) {
                        sum+=m.getPixel(k, l)*this.getPixel(i+k-p, j+l-p);
                    }
                }
                double v;
                if(m.getSumWeights() !=0) {
                    v = sum / m.getSumWeights();
                }
                else {
                    v = sum;
                }
                filtre.setPixel(i, j, (short) v);

            }
        }
        filtre.truncate((short)0,(short)255);
        return filtre;

    }

    GreyImage addRandomNoise(double p) throws Exception {
        GreyImage im = new GreyImage(this);
        double v;
        for (int i = 0; i < getSizeData(); i++) {
            if (Math.random() < p) {
                v = Math.random();
                if (v < 0.5) im.setPixel(i,(short)255);
                else im.setPixel(i,(short)0);
            }
        }

        return im;
    }

    public GreyImage addGaussianNoise(double mean, double std) throws Exception {

        GreyImage newImage = null;

        try {
            newImage = new GreyImage(this.dimX, this.dimY);
        } catch (Exception e) {
            System.out.println("MARCHE PAS");
        }

        Random r = new Random();
        double y;

        for(int i = 0; i<data.length;i++) {
            y = r.nextGaussian();
            y = mean + std*y;

            newImage.setPixel(i, (short)(getPixel(i)+y));
        }

        return newImage;
    }

    public GreyImage gradient(GreyImage Sx, GreyImage Sy) throws Exception {
        GreyImage newImage = null;

        try {
            newImage = new GreyImage(this.dimX, this.dimY);
        } catch (Exception e) {
            System.out.println("MARCHE PAS");
        }


        for (int i = 0; i<Sx.data.length;i++) {
            newImage.setPixel(i, (short) (Math.sqrt(Sx.getPixel(i) * Sx.getPixel(i) + Sy.getPixel(i) * Sy.getPixel(i))));
        }

        return newImage;
    }

    public GreyImage median(int N) throws Exception {

        GreyImage im = new GreyImage(getSizeX(), getSizeY());
        short[] mediane = new short[N*N];

        int p = (N - 1) / 2;
        int c = 0;


        for (int i = p; i < getSizeX()-p; i++) {
            for (int j = p; j < getSizeY()-p; j++) {
                for (int k = 0; k <= p*2; k++) {
                    for (int l = 0; l <= p*2; l++) {
                        mediane[c] = this.getPixel(i + k - p, j + l - p);
                        c++;
                    }
                }
                Arrays.sort(mediane);
                im.setPixel(i, j, mediane[N*N/2]);
                c = 0;
            }
        }
        return im;
    }

    public double computeNMSE(GreyImage im) {
        double up = 0;
        double down = 0;

        for(int i = 0; i < size; i++) {
            up+=(this.getPixel(i)-im.getPixel(i))*(this.getPixel(i)-im.getPixel(i));
            down+= this.getPixel(i)* this.getPixel(i);
        }

        return up/down;
    }

    public GreyImage erode(Mask B) throws Exception {

        GreyImage filtre = null;
        try {
            filtre = new GreyImage(this.dimX, this.dimY);
        } catch (Exception e) {
            System.out.println("MARCHE PAS");
        }


        int p = (B.getSizeX() - 1) / 2;

        for(int i=p;i<this.getSizeX()-1;i++) {
            for(int j=p;j<this.getSizeY()-1;j++) {

                if(this.getPixel(i,j) != 0) {
                    for(int k = 0;k<=2*p;k++) {
                        for(int l = 0; l<=2*p; l++) {
                            if(B.getPixel(i,j) == 1 && this.getPixel(i + k - p, j + l - p) == 0) {
                                this.setPixel(i,j, (short)255);
                            }
                        }
                    }
                }

            }
        }
        return filtre;
    }


}
