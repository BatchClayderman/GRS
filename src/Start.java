import entity.PARS;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;
import static jdk.nashorn.internal.ir.debug.ObjectSizeCalculator.getObjectSize;


public class Start
{
	public static String getCurrentTime()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss (z)");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		Date now = new Date();
		return sdf.format(now);
	}
	
	public static void main(String[] args) throws Exception
	{
		System.out.println("Program started at " + getCurrentTime() + ". ");
		System.out.println();
		System.out.println();
		int[] nValues = { 2, 4, 8, 16, 32 };
		HashMap<Integer, long[]> results = new HashMap<Integer, long[]>();
		for (int n : nValues)
		{
			System.out.println("/** n = " + n + " **/");
			long[] result = new long[6];
			PARS pars = Setup.setup(n); // Setup
			pars = KeyGen.keyGen(pars); // KeyGen
			
			long startTime = System.currentTimeMillis();
			pars = SignI.signI(pars); // SignI
			long endTime = System.currentTimeMillis();
			result[0] = endTime - startTime;
			result[1] = getObjectSize(pars.getSigma());
			
			startTime = System.currentTimeMillis();
			pars = VerifyI.verifyI(pars); // VerifyI
			endTime = System.currentTimeMillis();
			result[2] = endTime - startTime;
			
			startTime = System.currentTimeMillis();
			pars = SignII.signII(pars); // SignII
			endTime = System.currentTimeMillis();
			result[3] = endTime - startTime;
			result[4] = getObjectSize(pars.getSigma());
			
			startTime = System.currentTimeMillis();
			pars = VerifyII.verifyII(pars); // VerifyII
			endTime = System.currentTimeMillis();
			result[5] = endTime - startTime;
			
			results.put(n, result);
			System.out.println();
		}
		for (int n : nValues)
		{
			System.out.println("/** n = " + n + " **/");
			System.out.println("The time consumption of Sign I is " + results.get(n)[0] + " ms. ");
			//System.out.println("The size of the signature in Sign I is " + results.get(n)[1] + " Byte. ");
			System.out.println("The time consumption of Verify I is " + results.get(n)[2] + " ms. ");
			System.out.println("The time consumption of Sign II is " + results.get(n)[3] + " ms. ");
			//System.out.println("The size of the signature in Sign II is " + results.get(n)[4] + " Byte. ");
			System.out.println("The time consumption of Verify II is " + results.get(n)[5] + " ms. ");
			System.out.println();
		}
		System.out.println();
		System.out.println("Program ended at " + getCurrentTime() + ". ");
		return;
	}
}
