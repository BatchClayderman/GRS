package entity;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Arrays;


public class PARS
{
	private final int n;
	private Pairing pairing;
	private Element G;
	private Element[] pks, sks;
	private Element miu;
	private int l;
	private Element[] sigma;
	
	public PARS (int n) throws Exception
	{
		if (n <= 1 || Math.log(n) / Math.log(2) != (int)(Math.log(n) / Math.log(2)))
			throw new Exception("The value of n should be a 2-based positive value that is greater than 2. ");
		this.n = n;
	}
	
	public int getN()
	{
		return this.n;
	}
	
	public void setPairing(Pairing pairing)
	{
		this.pairing = pairing;
		return;
	}
	
	public Pairing getPairing()
	{
		return this.pairing;
	}
	
	public void setG(Element G)
	{
		this.G = G;
		return;
	}
	
	public Element getG()
	{
		return this.G;
	}
	
	public void setPks(Element[] pks)
	{
		this.pks = pks;
		return;
	}
	
	public Element[] getPks()
	{
		return this.pks;
	}
	
	public void setSks(Element[] sks)
	{
		this.sks = sks;
		return;
	}
	
	public Element[] getSks()
	{
		return this.sks;
	}
	
	public void setMiu(Element miu)
	{
		this.miu = miu;
		return;
	}
	
	public Element getMiu()
	{
		return this.miu;
	}
	
	public void setL(int l)
	{
		this.l = l;
		return;
	}
	
	public int getL()
	{
		return this.l;
	}
	
	public void setSigma(Element[] sigma)
	{
		this.sigma = sigma;
		return;
	}
	
	public Element[] getSigma()
	{
		return this.sigma;
	}
	
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("n = " + this.n + "\n");
		sb.append("G = " + this.G + "\n");
		sb.append("pks = " + Arrays.toString(this.pks) + "\n");
		sb.append("sks = " + Arrays.toString(this.sks) + "\n");
		sb.append("miu = " + this.miu + "\n");
		sb.append("l = " + this.l + "\n");
		sb.append("sigma = " + Arrays.toString(this.sigma) + "\n");
		return sb.toString();
	}
	
		
	public static BigInteger getX(Element element)
	{
		return new BigInteger(element.toString().split(",")[0]);
	}
	
	public static Element getX(Element element, Pairing pairing)
	{
		Element newElement = pairing.getZr().newElement();
		newElement.set(PARS.getX(element));
		return newElement;
	}
	
	public static Element concat(Element element1, Element element2, Pairing pairing)
	{
		Element element = pairing.getZr().newElement();
		element.set(new BigInteger(element1.toString().split(",")[0] + element2.toString().split(",")[0]));
		return element;
	}
	
	public static Element H(Element concatenated, Pairing pairing)
	{
		MessageDigest messageDigest;
		try
		{
			messageDigest = MessageDigest.getInstance("SHA-256");
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
		byte[] hash = messageDigest.digest(concatenated.toBytes());
		return pairing.getZr().newElementFromHash(hash, 0, hash.length);
	}
}