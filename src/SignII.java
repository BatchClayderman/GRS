import java.util.Arrays;

import entity.PARS;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;


public class SignII
{
	public static PARS signII(PARS pars)
	{
		/* Initial PARS */
		System.out.println("/* SignII */");
		final int n = pars.getN(), log2n = (int)(Math.log(n) / Math.log(2));
		final Pairing pairing = pars.getPairing();
		final Element[] pks = pars.getPks(), sks = pars.getSks();
		final int l = pars.getL();
		
		/* for i from 1 to n, select ki from zq* randomly */
		Element[] k = new Element[n];
		for (int i = 0; i < n; ++i)
			k[i] = pairing.getZr().newRandomElement();
		
		/* Compute R */
		Element R = pks[0].duplicate().powZn(k[0]);
		for (int i = 1; i < n; ++i)
			R = R.duplicate().mul(pks[i].duplicate().powZn(k[i]));
		
		/* Compute c */
		Element miu = pairing.getG1().newRandomElement(); // randomly generate miu (message)
		pars.setMiu(miu);
		Element concatenated = PARS.concat(miu.duplicate(), R.duplicate(), pairing);
		for (Element pk : pks)
			concatenated = PARS.concat(concatenated.duplicate(), pk.duplicate(), pairing);
		Element c = PARS.H(concatenated, pairing);
		
		/* Compute f */
		Element[] fs = new Element[n];
		for (int i = 0; i < n; ++i)
			if (l == i)
				fs[i] = k[i].duplicate().add(c.duplicate().mul(sks[i]));
			else
				fs[i] = k[i].duplicate();
		
		/* Repeat and loop */
		Element[] sigma = new Element[(log2n + 1) << 1], tempPks = pks.clone(); // sigma = (log2n L_B, log2n R_B, R, f')
		int round_cnt = 0, tempN = n;
		while (tempN > 1)
		{
			/* Set n' and initial four vectors */
			int n_ = tempN >> 1;
			Element[] f_L = new Element[n_], f_R = new Element[tempN - n_], p_L = new Element[n_], p_R = new Element[tempN - n_];
			for (int i = 0; i < n_; ++i)
			{
				f_L[i] = fs[i].duplicate();
				p_L[i] = tempPks[i].duplicate();
			}
			for (int i = n_; i < tempN; ++i)
			{
				f_R[i - n_] = fs[i].duplicate();
				p_R[i - n_] = tempPks[i].duplicate();
			}
			
			/* Compute L_B and R_B */
			Element L_B = p_L[0].duplicate().powZn(f_R[0]);
			for (int i = 1; i < n_; ++i)
				L_B = L_B.duplicate().mul(p_L[i].duplicate().powZn(f_R[i]));
			sigma[round_cnt] = L_B;
			Element R_B = p_R[0].duplicate().powZn(f_L[0]);
			for (int i = 1; i < n_; ++i)
				R_B = R_B.duplicate().mul(p_R[i].duplicate().powZn(f_L[i]));
			sigma[log2n + round_cnt] = R_B;
			
			/* Compute x */
			Element x = PARS.H(PARS.concat(L_B, R_B, pairing), pairing);
			
			/* Compute p' and f' */
			Element[] p_s = new Element[n_], f_s = new Element[n_];
			for (int i = 0; i < n_; ++i)
			{
				p_s[i] = p_R[i].duplicate().powZn(x.duplicate().invert()).duplicate().mul(p_L[i].duplicate().powZn(x));
				f_s[i] = f_R[i].duplicate().mul(x).duplicate().add(f_L[i].duplicate().mul(x.duplicate().invert()));
			}
			tempN = n_;
			tempPks = p_s;
			fs = f_s;
			++round_cnt;
		}
		
		/* Output sigma */
		sigma[log2n << 1] = R;
		sigma[(log2n << 1) + 1] = fs[0];
		pars.setSigma(sigma); // (L_B, R_B, R, f')
		System.out.println("sigma = " + Arrays.toString(sigma));
		
		/* Return pars */
		System.out.println();
		return pars;
	}
}