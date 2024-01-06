import entity.PARS;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import java.util.Arrays;
import java.util.Random;


public class SignI
{
	public static PARS signI(PARS pars)
	{
		/* Initial PARS */
		System.out.println("/* SignI */");
		final int n = pars.getN();
		final Pairing pairing = pars.getPairing();
		final Element[] pks = pars.getPks(), sks = pars.getSks();
		
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
		Random random = new Random();
		int l = random.nextInt(n);
		pars.setL(l);
		Element[] fs = new Element[n];
		for (int i = 0; i < n; ++i)
			if (l == i)
				fs[i] = k[i].duplicate().add(c.duplicate().mul(sks[i]));
			else
				fs[i] = k[i].duplicate();
		
		/* Output sigma */
		Element[] sigma = new Element[1 + n];
		sigma[0] = R.duplicate();
		for (int i = 0; i < n; ++i)
			sigma[i + 1] = fs[i].duplicate();
		pars.setSigma(sigma); // (R, fs)
		System.out.println("sigma = " + Arrays.toString(sigma));
		
		/* Return pars */
		System.out.println();
		return pars;
	}
}