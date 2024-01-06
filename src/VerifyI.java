import entity.PARS;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;


public class VerifyI
{
	public static PARS verifyI(PARS pars) throws Exception
	{
		/* Initial PARS */
		System.out.println("/* VerifyI */");
		final int n = pars.getN();
		final Pairing pairing = pars.getPairing();
		final Element G = pars.getG();
		final Element[] pks = pars.getPks();
		final Element miu = pars.getMiu();
		final Element[] sigma = pars.getSigma(); // (R, fs)
		
		/* Compute c */
		Element concatenated = PARS.concat(miu.duplicate(), sigma[0].duplicate(), pairing);
		for (Element pk : pks)
			concatenated = PARS.concat(concatenated.duplicate(), pk.duplicate(), pairing);
		Element c = PARS.H(concatenated, pairing);
		
		/* Compute A */
		Element A = G.duplicate().powZn(c).duplicate().mul(sigma[0]);
		
		/* Compute B */
		Element B = pks[0].duplicate().powZn(sigma[1]);
		for (int i = 1; i < n; ++i)
			B = B.duplicate().mul(pks[i].duplicate().powZn(sigma[i + 1]));
		
		/* Output A and B */
		System.out.println("A = " + A);
		System.out.println("B = " + B);
		if (!A.equals(B))
		{
			System.out.println("*** Debug Information Starts ***");
			System.out.println(pars);
			System.out.println("c = " + c);
			System.out.println("*** Debug Information Ends ***");
			throw new Exception("A and B are not equal. ");
		}
		
		/* Return pars */
		System.out.println();
		return pars;
	}
}