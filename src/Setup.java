import entity.PARS;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.pairing.a.TypeACurveGenerator;


public class Setup
{
	public static PARS setup(int n) throws Exception
	{
		/* Initial PARS */
		System.out.println("/* Setup */");
		PARS pars = new PARS(n);
		
		/* Select a 256-bit oval curve (secp256k1) */
		int rBits = 256;
		int qBits = 256;
		TypeACurveGenerator curveGenerator = new TypeACurveGenerator(rBits, qBits);
		PairingParameters params = curveGenerator.generate();
		Pairing pairing = PairingFactory.getPairing(params);
		pars.setPairing(pairing);
		
		/* Randomly select G */
		@SuppressWarnings("unchecked")
		Field<Element> G1 = pairing.getG1();
		Element G = G1.newRandomElement();
		pars.setG(G);
		
		/* Output parameters */
		System.out.println("rBits = " + rBits);
		System.out.println("qBits = " + qBits);
		System.out.println("G = " + G);
		
		/* Return pars */
		System.out.println();
		return pars;
	}
}