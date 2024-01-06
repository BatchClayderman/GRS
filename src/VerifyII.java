import entity.PARS;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import java.util.Arrays;


public class VerifyII
{
	public static PARS verifyII(PARS pars) throws Exception
	{
		/* Initial PARS */
		System.out.println("/* VerifyII */");
		final int n = pars.getN(), log2n = (int)(Math.log(n) / Math.log(2));
		final Pairing pairing = pars.getPairing();
		final Element G = pars.getG();
		final Element[] pks = pars.getPks();
		final Element miu = pars.getMiu();
		final Element[] sigma = pars.getSigma(); // (L_B, R_B, R, f')
		
		/* Compute c */
		Element concatenated = PARS.concat(miu.duplicate(), sigma[log2n << 1].duplicate(), pairing);
		for (Element pk : pks)
			concatenated = PARS.concat(concatenated.duplicate(), pk.duplicate(), pairing);
		Element c = PARS.H(concatenated, pairing);
		
		/* Compute x */
		Element[] x = new Element[log2n];
		for (int i = 0; i < log2n; ++i)
			x[i] = PARS.H(PARS.concat(sigma[i], sigma[log2n + i], pairing), pairing);
		
		/* Compute s */
		Element[] s = new Element[n];
		for (int i = 0; i < n; ++i)
		{
			s[i] = (i & 1) == 1 ? x[log2n - 1].duplicate().invert() : x[log2n - 1].duplicate();
			for (int j = 1; j < log2n; ++j)
				s[i] = s[i].duplicate().mul(((i >> j) & 1) == 1 ? x[log2n - 1 - j].duplicate().invert() : x[log2n - 1 - j].duplicate());
		}
		
		/* Compute A and B */
		Element A = sigma[log2n << 1].duplicate().mul(G.duplicate().powZn(c));
		for (int i = 0; i < log2n; ++i)
		{
			Element x_i_inverted = x[i].duplicate().invert();
			A = A.duplicate().mul(sigma[i].duplicate().powZn(x[i].duplicate().mul(x[i]))).duplicate().mul(sigma[log2n + i].duplicate().powZn(x_i_inverted.duplicate().mul(x_i_inverted)));
		}
		Element B = pks[0].duplicate().powZn(sigma[(log2n << 1) + 1].duplicate().mul(s[0]));
		for (int i = 1; i < n; ++i)
			B = B.duplicate().mul(pks[i].duplicate().powZn(sigma[(log2n << 1) + 1].duplicate().mul(s[i])));
				
		/* Output A and B */
		System.out.println("A = " + A);
		System.out.println("B = " + B);
		if (!A.equals(B))
		{
			System.out.println("*** Debug Information Starts ***");
			System.out.println(pars);
			System.out.println("c = " + c);
			System.out.println("x = " + Arrays.toString(x));
			System.out.println("s = " + Arrays.toString(s));
			System.out.println("*** Debug Information Ends ***");
			throw new Exception("A and B are not equal. ");
		}
		
		/* Return pars */
		System.out.println();
		return pars;
	}
}