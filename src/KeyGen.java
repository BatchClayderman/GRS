import entity.PARS;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;


public class KeyGen
{	
	public static PARS keyGen(PARS pars)
	{
		/* Initial PARS */
		System.out.println("/* KeyGen */");
		final int n = pars.getN();
		final Pairing pairing = pars.getPairing();
		final Element G = pars.getG();
		
		/* Input G and generate (pk, sk) */
		Element[] pks = new Element[n], sks = new Element[n];
		for (int i = 0; i < n; ++i)
		{
			Element sk = pairing.getZr().newRandomElement(); // generate sk
			Element pk = G.duplicate().powZn(sk.duplicate().invert()); // compute pk
			pks[i] = pk;
			sks[i] = sk;
		}
		
		/* Output result */
		for (int i = 0; i < n; ++i)
		{
			System.out.println("User " + (i + 1) + ": ");
			System.out.println("pk: " + pks[i]);
			System.out.println("sk: " + sks[i]);
		}
		pars.setPks(pks);
		pars.setSks(sks);
		
		/* Return pars */
		System.out.println();
		return pars;
	}
}