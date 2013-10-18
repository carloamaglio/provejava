package pressa;

public class Ginocchiera {

	public enum Tipi { KW20PI_600 };

	static final Dati[] ginocchiere = {
		// KW 20 PI  / PM 600
		new Dati(435.0, 155.0, 55.0, 560.0, 195.0, 330.0, 515.0, 100.5, 2.0, 2045.0, 6361.72512),
		
	};

	Tipi tipo;
	Dati dati;	// dati della ginocchiera corrente

	double FG;
	double BF2;
	double BC2;
	double BC;
	double alfa;
	double DH2;
	double BD2;
	double BD2_DH2;
	double k2;
	double BH_;
	double fgamma;
	double BN;
	double BP;
	double FE0;
	double c3;
	double massimaCorsaPM;

	static final int PPMLEN = 14000;
	static final int VPMLEN = 23000;
	int[] ppm = new int[PPMLEN];
	int[] vpm = new int[VPMLEN];

	public Ginocchiera(Tipi tipo) {
		set(tipo);
	}

	public void set(Tipi tipo) {
		this.tipo = tipo;
		dati = ginocchiere[0];
		fillTables();
	}

	public double tc2pm(int tc) {
		return ppm[tc];
	}

	void fillTables() {
		int i, j;
		
		FG = dati.BD + dati.DH;
		BF2 = dati.AF * dati.AF + dati.AB * dati.AB;
		BC2 = dati.BG * dati.BG + dati.CG * dati.CG;
		BC = Math.sqrt(BC2);
		alfa = Math.acos(dati.BG / BC);
		DH2 = dati.DH * dati.DH;
		BD2 = dati.BD * dati.BD;
		BD2_DH2 = BD2 - DH2;
		k2 = dati.k * dati.k;
		BH_ = Math.sqrt(BD2 - k2) + Math.sqrt(DH2 - k2);
		fgamma = Math.acos((BH_ * BH_ + BD2_DH2) / (dati.BD * BH_ * 2.0));
		BN = dati.AB + (BC * Math.cos(alfa + fgamma));
		BP = dati.AF - (BC * Math.sin(alfa + fgamma));
		FE0 = BN - Math.sqrt(dati.CE * dati.CE - BP * BP);
		c3 = BF2 + BC2 - dati.CE * dati.CE;


		j = (int) (dati.a * 10);	// corsa del pistone in decimi di mm
		for (i = 0; i <= j; i++) {
			double X = FE0 - (i / 10.0);
			double X1 = (X * X) - (2.0 * dati.AB * X);
			double X2 = Math.sqrt(BF2 + X1);
			double ABD = alfa + Math.acos((c3 + X1) / (2 * BC * X2)) + Math.acos((dati.AB - X) / X2);
			double sinABD = Math.sin(ABD);
			double y = FG + (dati.BD * Math.cos(ABD)) - Math.sqrt(DH2 - BD2 * sinABD * sinABD);
			ppm[i] = (int) (y * 10.0);
		}
		for (i=j+1; i<PPMLEN; i++)
			ppm[i] = ppm[j];

		massimaCorsaPM = ppm[(int)(dati.a * 10)];


		j = ppm[(int)(dati.a * 10)];
		for (i = 0; i <= j; i++) {
			double[] y = new double[12];
			y[0] = FG - (i / 10.);
			y[1] = y[0] * y[0];
			y[2] = (y[1] + BD2_DH2) / (2 * dati.BD * y[0]);
			if (y[2] == 1.) y[2] = 0.99999;
			y[3] = alfa + Math.acos(y[2]);
			y[4] = (BD2_DH2 - y[1]) / (2.0 * dati.BD * y[1] * Math.sqrt(1 - (y[2] * y[2])));
			y[5] = Math.sin(y[3]);
			y[6] = Math.cos(y[3]);
			y[7] = dati.AF - (BC * y[5]);
			y[8] = Math.sqrt((dati.CE * dati.CE) - (y[7] * y[7]));
			y[9] = (y[7] * y[6] / y[8]) + y[5];
			y[10] = -BC * y[4] * y[9];
//	   if (y[10] > 1.8) y[10] = 2.8;
//	   if (y[10] < 0.)  y[10] = 0.0;
			if (y[10] < 0.)
				y[10] = -y[10];
			y[11] = (y[10] * 1000.0) + 200.0;
			vpm[i] = (int)y[11];
		}
		for (i=j+1; i<VPMLEN; i++)
			vpm[i] = vpm[j];
	}
}

final class Dati {
	public final double a;						// C2 corsa pistone
	public final double AF;						// MC=H1-H2,		KW=H5+H4
	public final double CG;						// MC=H1-H2-H3=H4,	KW=H4
	public final double AB;						// MC=L1,			KW=L6
	public final double BG;						// MC=L3,			KW=L5
	public final double BD;						// MC=L2,			KW=L3
	public final double DH;						// MC=L5,			KW=L2
	public final double CE;						// MC=H5,			KW=H2
	public final double k;						// disallineamento
	public final double lunghezzaColonna;		// lunghezza colonna (gino_tot+piani)	MC= , KW=L11-L9
	public final double sezioneColonna;			// sezione colonna (area di una colonna PI*d*d/4)
	
	Dati(double a, double b, double c, double d, double e, double f, double g, double h, double k, double lc, double sc) {
		this.a = a;
		this.AF = b;
		this.CG = c;
		this.AB = d;
		this.BG = e;
		this.BD = f;
		this.DH = g;
		this.CE = h;
		this.k = k;
		this.lunghezzaColonna = lc;
		this.sezioneColonna = sc;
	}
}

